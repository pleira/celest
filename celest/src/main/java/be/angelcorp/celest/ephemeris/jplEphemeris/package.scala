package be.angelcorp.celest.ephemeris

import java.nio.file.{Files, Path}
import java.io.{Reader, FileReader, RandomAccessFile}
import java.nio.channels.FileChannel.MapMode
import java.nio.{ByteBuffer, ByteOrder}
import java.text.{DecimalFormatSymbols, DecimalFormat, NumberFormat}
import java.util.Locale
import java.nio.file.StandardOpenOption._
import scala.Some
import scala.math._
import scala.io.Source
import scala.collection.immutable.ListMap
import org.slf4j.LoggerFactory
import be.angelcorp.celest.universe.Universe
import be.angelcorp.celest.util._
import be.angelcorp.celest.time.JulianDate
import be.angelcorp.celest.time.timeStandard.TimeStandards.TDB
import be.angelcorp.celest.frameGraph.frames.ICRS

package object jplEphemeris {
  private val logger = LoggerFactory.getLogger(getClass)

  /**
   * Create a JplEphemeris object from a set of ascii data files.
   *
   * - This loads all the data files in memory!
   * - Make sure you add all the data files in the correct temporal sequence.
   * - Make sure to start with the first data file (you cannot skip epochs you don't need)
   *
   * @param header    Path to the ephemeris header file.
   * @param dataFiles Path to any supplementary data files.
   */
  def fromAscii(header: Path, dataFiles: Seq[Path])(implicit universe: Universe): AsciiEphemeris[ICRS] =
    fromAscii(new FileReader(header.toFile), dataFiles.map(x => new FileReader(x.toFile)))

  /**
   * Create a JplEphemeris object from a set of ascii data file readers.
   *
   * - This loads all the data files in memory!
   * - Make sure you add all the data files in the correct temporal sequence.
   * - Make sure to start with the first data file (you cannot skip epochs you don't need)
   *
   * @param header    The ephemeris header file reader.
   * @param dataFiles Any supplementary data file readers.
   */
  def fromAscii(header: Reader, dataFiles: Seq[Reader])(implicit universe: Universe): AsciiEphemeris[ICRS] = {
    val parser = new AsciiParser()
    // Create the ephemeris object from the header
    val ephemeris = parser.ephemeris(header)
    val metadata = ephemeris.metadata
    // Add all the additional data records to the ephemeris
    for (dataFile <- dataFiles)
      ephemeris.addRecords(parser.ephemerisData(metadata, dataFile))
    ephemeris
  }

  /**
   * Create a JplEphemeris object from a binary data file.
   *
   * @param path           Path to the ephemeris file to read.
   * @param deNumber       Number of the ephemeris the parse eg 405 (required for determining the binary file layout).
   * @param alignmentHint  Optional hint; Alignment (padding) strategy used for creating the binary ephemeris file.
   * @param endiannessHint Optional hint; Fix the endiannes of the binary ephemeris file.
   * @param tagCountHint   Optional hint; Fix the maximum number of tags embedded in the data file (usually 400).
   */
  def fromBinary(path: Path, deNumber: Int,
                 alignmentHint: Option[AlignmentStrategy] = None, endiannessHint: Option[ByteOrder] = None, tagCountHint: Option[Int] = None)(implicit universe: Universe) = {
    // Open ephemeris file.
    val file = new RandomAccessFile(path.toFile, "r")
    // Map the first data from the file to a buffer
    val headerData = file.getChannel.map(MapMode.READ_ONLY, 0, min(file.length(), 81440))

    // Endian options to try
    val endianOptions = endiannessHint match {
      case Some(e) => List(e)
      case _ => List(ByteOrder.LITTLE_ENDIAN, ByteOrder.BIG_ENDIAN)
    }
    // Alignment options to try
    val alignmentOptions = alignmentHint match {
      case Some(a) => List(a)
      case _ => List(PackedAlignment.instance, X64Alignment.instance, MsvcX86Alignment.instance, GccX86Alignment.instance)
    }
    // Tag count options to try
    val tagCountOptions = tagCountHint match {
      case Some(t) => List(t)
      case _ => List(400, 1000)
    }

    // Try to detect the exact format the data file based on combinations of the previous options
    //   Endianness: Byte order
    //   Alignment:  Extra padding between variables
    //   Tag count:  Number of tags defines eg [AU => ...m]
    val (endianness, alignment, tagCount) = {
      // Create a list of all posible combinations
      val combinations = for (e <- endianOptions; a <- alignmentOptions; t <- tagCountOptions) yield (e, a, t)

      // Find the combination that results in the correct DE identifier being found
      combinations.find(entry => {
        // Skip over all the header fields before the DE idenifier
        headerData.order(entry._1)
        val access = new RichByteBuffer(headerData, entry._2)
        access.buffer.position(84 * 3 + 6 * entry._3) // assumption: chars/strings are 1 byte aligned ...
        access.getAlignedDouble
        access.getAlignedDouble
        access.getAlignedDouble
        access.getAlignedInt
        access.getAlignedDouble
        access.getAlignedDouble
        for (i <- 0 until 12) yield new RecordMetadata(access.getAlignedInt, access.getAlignedInt, access.getAlignedInt)

        // Check if this endianness, alignment, and tag count result in the DE identifier
        access.getAlignedInt == deNumber
      }) match {
        case Some(combination) => combination
        case None => throw new RuntimeException(s"No suitable combination of endianness, alignment, and tag count found that results in DE idenifier $deNumber. Tried the following combinations: $combinations")
      }
    }
    headerData.order(endianness)

    /** Determine the size and number of records in the data file */
    val recordEntries: Int = 1018 // TODO: detect this number, as it is sometimes different
    val recordSize: Int = recordEntries * 8

    // Read in the metadata
    lazy val metadata = {
      headerData.rewind()
      val alignedHeaderData = new RichByteBuffer(headerData, alignment)

      val label1 = alignedHeaderData.getAlignedCString(84).replaceAll("[\r\n]+", "")
      val label2 = alignedHeaderData.getAlignedCString(84).replaceAll("[\r\n]+", "")
      val label3 = alignedHeaderData.getAlignedCString(84).replaceAll("[\r\n]+", "")
      val tagNames = for (i <- 0 until tagCount) yield alignedHeaderData.getAlignedCString(6)
      val ephemStart = alignedHeaderData.getAlignedDouble
      val ephemEnd = alignedHeaderData.getAlignedDouble
      val ephemStep = alignedHeaderData.getAlignedDouble
      val numConst = alignedHeaderData.getAlignedInt
      val AU = alignedHeaderData.getAlignedDouble
      val EMRAT = alignedHeaderData.getAlignedDouble
      val coeffPtr = for (i <- 0 until 12) yield new RecordMetadata(alignedHeaderData.getAlignedInt, alignedHeaderData.getAlignedInt, alignedHeaderData.getAlignedInt)
      val DENUM = alignedHeaderData.getAlignedInt
      val libratPtr = new RecordMetadata(alignedHeaderData.getAlignedInt, alignedHeaderData.getAlignedInt, alignedHeaderData.getAlignedInt)

      // Skip over the padding of the first record
      headerData.position(recordSize)

      val tagValues = for (i <- 0 until tagCount) yield headerData.getDouble
      val tags = ListMap(tagNames.map(_.trim) zip tagValues filterNot (_._1.matches( """^\s*$""")): _*)

      // Skip over the padding of the second record
      headerData.position(2 * recordSize)
      // Read the real start date of the ephemeris (it might be a subset of the full range)
      val realEphemStart = headerData.getDouble

      if (deNumber != DENUM)
        logger.warn(s"Loaded binary ephemeris with id $DENUM, but expected $deNumber")

      // Number of data records in this ephemeris
      val records = file.length / recordSize - 2 // 2 header records exist

      // Real range of the data, the header has a tendency to lie (most libraries dont change this when subsetting the ephemeris)
      val range = JulianDate(realEphemStart, TDB).until(JulianDate(realEphemStart + records * ephemStep, TDB), ephemStep)

      new Metadata(recordEntries, label1, label2, label3, tags,
        range, AU, EMRAT, coeffPtr.toList ::: List(libratPtr), DENUM)
    }

    new BinaryEphemeris(metadata, file, endianness)
  }

  /**
   * Serialize the header information of a JplEphemeris object to an ascii string.
   *
   * @param ephemeris Ephemeris object to serialze.
   * @return String containing the ascii header of the given ephemeris.
   */
  def toAsciiHeader(ephemeris: JplEphemeris[_]) = {
    val metadata = ephemeris.metadata
    val step = if (metadata.range.step % 1.0 == 0) f"${metadata.range.step}%11.0f." else f"${metadata.range.step}%12f"
    f"""KSIZE=${metadata.recordEntries * 2}%6d    NCOEFF=${metadata.recordEntries}%6d
        |
        |GROUP   1010
        |
        |${metadata.label1}${metadata.label2}${metadata.label3}
        |GROUP   1030
        |
        |${metadata.range.start.jd}%12.2f${metadata.range.end.jd}%12.2f$step
        |
        |GROUP   1040
        |
        |${metadata.numConst}%6d
        |${metadata.tags.grouped(10).foldLeft("")((str, group) => str + group.foldLeft("")((line, elem) => line + f"  ${elem._1}%6s") + '\n')}
        |GROUP   1041
        |
        |${metadata.numConst}%6d
        |${metadata.tags.grouped(3).foldLeft("")((str, group) => str + group.foldLeft("")((line, elem) => line + format(elem._2)) + '\n')}
        |GROUP   1050
        |
        |${metadata.coeffPtr.map(x => Seq(x.entryPoint, x.nrCoefficients, x.nrGranules).map(x => f"$x%6d")).transpose.foldLeft("")((str, group) => str + group.mkString + '\n')}
        |GROUP   1070
        |
        |
    """.stripMargin
  }

  /**
   * Convert a sequence of records bundled with there index in the ephemeris to ascii string form.
   *
   * @param records List of data records and there index in the epehemris.
   * @return String containing the ascii form of the given records.
   */
  def toAsciiData(records: Seq[(DataRecord, Int)]): String = {
    records.map(entry => {
      val record = entry._1
      val recordId = entry._2
      f"$recordId%6d${record.metadata.recordEntries}%6d\n" +
        record.data.grouped(3).foldLeft("")((str, group) => {
          str + group.foldLeft("")((line, elem) => line + format(elem)) + '\n'
        })
    }).mkString
  }

  /**
   * Convert a subset of the records in an ephemeris file to ascii string form.
   *
   * @param ephemeris   Ephemeris object containing the records and data.
   * @param recordRange Optional, range of indices of the records to serialize (default: all records)
   * @return String containing the ascii form of the selected records.
   */
  def toAsciiData(ephemeris: JplEphemeris[_], recordRange: Option[Range]): String = {
    toAsciiData(recordRange match {
      case Some(range) => range.map(index => (ephemeris.getRecord(index), index))
      case _ => ephemeris.records.zipWithIndex.toSeq
    })

  }

  /**
   * Save an ephemeris object to disk in binary format.
   *
   * @param ephmeris   Ephemerise to save.
   * @param file       File to save to.
   * @param endianness Optional, endianness to use while saving (byte order).
   * @param alignment  Optional, alignment to use while saving (alignment/padding).
   */
  def toBinary(ephmeris: JplEphemeris[_], file: Path, endianness: ByteOrder = ByteOrder.LITTLE_ENDIAN, alignment: AlignmentStrategy = PackedAlignment.instance) {
    val writeChannel = Files.newByteChannel(file, WRITE, CREATE)

    // Shorthand for the metadata
    val metadata = ephmeris.metadata
    // This is the list of tags that will be stored, stretched to the required number of elements
    val tags = metadata.tags.toList.padTo(if (metadata.tags.size <= 400) 400 else 1000, ("      ", 0.0)).map(
      (entry) => (entry._1.padTo(6, ' '), entry._2)
    )

    val buffer = ByteBuffer.allocateDirect(metadata.recordEntries * 8)
    buffer.order(endianness)
    val alignedRecord = new RichByteBuffer(buffer, alignment)

    // RECORD 1
    alignedRecord.putAlignedCString(metadata.label1, 84)
    alignedRecord.putAlignedCString(metadata.label2, 84)
    alignedRecord.putAlignedCString(metadata.label3, 84)
    tags.foreach(tag => alignedRecord.putAlignedCString(tag._1.padTo(6, ' '), 6))
    alignedRecord.putAlignedDouble(metadata.range.start.jd)
    alignedRecord.putAlignedDouble(metadata.range.end.jd)
    alignedRecord.putAlignedDouble(metadata.range.step)
    alignedRecord.putAlignedInt(metadata.numConst)
    alignedRecord.putAlignedDouble(metadata.AU)
    alignedRecord.putAlignedDouble(metadata.EMRAT)
    metadata.coeffPtr.dropRight(1 /*libration droped*/).foreach(entry => {
      alignedRecord.putAlignedInt(entry.entryPoint)
      alignedRecord.putAlignedInt(entry.nrCoefficients)
      alignedRecord.putAlignedInt(entry.nrGranules)
    })
    alignedRecord.putAlignedInt(metadata.headerID)
    metadata.coeffPtr.takeRight(1).foreach(entry => {
      alignedRecord.putAlignedInt(entry.entryPoint)
      alignedRecord.putAlignedInt(entry.nrCoefficients)
      alignedRecord.putAlignedInt(entry.nrGranules)
    })
    (buffer.position until buffer.limit).foreach(pos => buffer.put(0: Byte)) // Set the remaining bytes to zero, strictly not required
    buffer.flip() // Prepare the buffer for writing
    writeChannel.write(alignedRecord.buffer) // Write the buffer to the file
    buffer.rewind()

    // RECORD 2
    tags.foreach(tag => alignedRecord.putAlignedDouble(tag._2))
    (buffer.position until buffer.limit).foreach(pos => buffer.put(0: Byte)) // Set the remaining bytes to zero, strictly not required
    alignedRecord.buffer.flip() // Prepare the buffer for writing
    writeChannel.write(alignedRecord.buffer) // Write the buffer to the file
    buffer.rewind()

    // RECORD 3 - N
    ephmeris.records.foreach(record => {
      buffer.asDoubleBuffer().put(record.data)
      writeChannel.write(buffer)
      buffer.rewind()
    })
    writeChannel.close()
  }

  /**
   * Convert an ascii form of the JPL ephemeris to the equivalent binary form.
   *
   * @param header    Ascii header file.
   * @param dataFiles Ascii data files (can be Nil if the data is contained in the header).
   * @param output    Output file for the binary ephemeris.
   * @param endianness Optional endianess to use for the ephemeris.
   * @param alignment  Optional alignment to use for the ephemeris.
   */
  def ascii2binary(header: Path, dataFiles: Seq[Path], output: Path, endianness: ByteOrder = ByteOrder.LITTLE_ENDIAN, alignment: AlignmentStrategy = PackedAlignment.instance)(implicit universe: Universe) {
    asciiReader2binary(new FileReader(header.toFile), dataFiles.map(x => new FileReader(x.toFile)), output: Path, endianness, alignment)
  }

  /**
   * Convert an ascii form of the JPL ephemeris to the equivalent binary form.
   *
   * @param header    Ascii header file reader.
   * @param dataFiles Ascii data file readers (can be Nil if the data is contained in the header).
   * @param output    Output file for the binary ephemeris.
   * @param endianness Optional endianess to use for the ephemeris.
   * @param alignment  Optional alignment to use for the ephemeris.
   */
  def asciiReader2binary(header: Reader, dataFiles: Seq[Reader], output: Path, endianness: ByteOrder = ByteOrder.LITTLE_ENDIAN, alignment: AlignmentStrategy = PackedAlignment.instance)(implicit universe: Universe) {
    val parser = new AsciiParser()
    // Create the ephemeris object from the header
    val ephemeris = parser.ephemeris(header)

    // Write out the two header records
    toBinary(ephemeris, output, endianness, alignment)

    // Reopen the file to add the additional records
    val writeChannel = Files.newByteChannel(output, WRITE, APPEND)
    val buffer = ByteBuffer.allocateDirect(ephemeris.metadata.recordEntries * 8)
    buffer.order(endianness)

    // Add all the additional data records to the ephemeris
    var lastRecord: Option[DataRecord] = None
    for (dataFile <- dataFiles;
         record <- parser.ephemerisData(ephemeris.metadata, dataFile)) {
      lastRecord match {
        case Some(rec) if rec.begin == record.begin => // Skip identical records
        case _ => // Write the record:
          buffer.asDoubleBuffer().put(record.data)
          writeChannel.write(buffer)
          buffer.rewind()
      }
      lastRecord = Some(record)
    }
    writeChannel.close()
  }

  /**
   * Computes the reference values of a JPL ephemeris test data set.
   *
   * The results returned are the raw test line, followed by the pared data, and the expected|computed value for that line.
   *
   * Based of off:
   * - ftp://ftp.astro.amu.edu.pl/pub/jpleph/version_1.3/testeph.c
   * - http://cow.physics.wisc.edu/~craigm/idl/down/jplephinterp.pro
   *
   * @param ephemeris     JPL ephemeris to test.
   * @param testFile      The test data file source.
   * @param maximumTests  Maximum number of test lines to evaluate (for all lines, pass None).
   * @return List of evaluated test lines, each containing:
   *         - Test string
   *         - {de_number, calender_date, Julian_Date, target_body, center_body, coordinate_id, coordinate_value}
   *         - Expected value, computed value [AU|AU/s|rad|rad/s]
   */
  def test(ephemeris: JplEphemeris[_], testFile: Source, maximumTests: Option[Int] = None)(implicit universe: Universe) = {
    // Ephemeris AU is in [km] not in [m]
    val AU = ephemeris.metadata.AU * 1E3
    // Read header of file, up to and including the EOT line
    val testDataStr = testFile.getLines().dropWhile(!_.contains("EOT")).drop(1).toList

    // The testpo.xxx file uses slightly different body definitions, so transform to the correct one
    def correctBody(id: Int) = {
      // Testpo.xxx uses the following conventions:
      //   1 = Mercury            8 = Neptune
      //   2 = Venus              9 = Pluto
      //   3 = Earth             10 = Moon
      //   4 = Mars              11 = Sun
      //   5 = Jupiter           12 = Solar system barycenter
      //   6 = Saturn            13 = Earth-Moon barycenter
      //   7 = Uranus            14 = Nutations (longitude and obliquity; untested)
      //                         15 = Librations
      id match {
        case 1 => Mercury()
        case 2 => Venus()
        case 3 => Earth()
        case 4 => Mars()
        case 5 => Jupiter()
        case 6 => Saturn()
        case 7 => Uranus()
        case 8 => Neptune()
        case 9 => Pluto()
        case 10 => Moon()
        case 11 => Sun()
        case 12 => SSB()
        case 13 => EMB()
      }
    }

    // Reduce tests to only the specified maximum
    (maximumTests match {
      case Some(max) => testDataStr.take(max)
      case _ => testDataStr
    }).map(line => {
      // Read the test data, line-by-line
      val elements = line.split( """\s+""")
      val denum = elements(0).toInt // Id of the epheleris eg. 405
      val caldate = elements(1) // String of the calender date of the test epoch
      val jd = elements(2).toDouble // Julian date of the test epoch TDB
      val targ = elements(3).toInt // Target body to compute the ephemeris off (or libration/nutation)
      val cent = elements(4).toInt // Center body. Ephemeris should be relative to this body
      val coord = elements(5).toInt // Index of the coordinate to test
      val value = elements(6).toDouble // Expected value of the coordinate [AU|AU/s|rad|rad/s]

      // The test epoch
      val epoch = JulianDate(jd, TDB)

      // Compute the true value as by the given ephemeris
      val trueValue = try {
        targ match {
          case 14 => // Nutations
            val nutations = ephemeris.interpolateNutation(epoch)
            coord match {
              case 1 => nutations._1
              case 2 => nutations._2
              case 3 => nutations._3
              case _ => nutations._4
            }
          case 15 => // Libration
            val precession = ephemeris.interpolateLibration(epoch)
            coord match {
              case 1 => precession._1.x
              case 2 => precession._1.y
              case 3 => precession._1.z
              case 4 => precession._2.x
              case 5 => precession._2.y
              case _ => precession._2.z
            }
          case target => // Planetairy state
            val bodyState = ephemeris.interpolateState(epoch, correctBody(target))
            val centerState = ephemeris.interpolateState(epoch, correctBody(cent))
            val resultPosition = (bodyState.position - centerState.position) / AU // in [AU]
            val resultVelocity = (bodyState.velocity - centerState.velocity) * 86400.0 / AU // in [AY/day]
            coord match {
              case 1 => resultPosition.x
              case 2 => resultPosition.y
              case 3 => resultPosition.z
              case 4 => resultVelocity.x
              case 5 => resultVelocity.y
              case _ => resultVelocity.z
            }
        }
      } catch {
        case e: Throwable => Double.NaN
      }

      (line, (denum, caldate, jd, targ, cent, coord, value), (value, trueValue))
    })
  }

  /**
   * Format floating point numbers as:
   *
   * 0.299221166597053480D-01
   * 0.166716555849284460D00
   */
  private val fformat = {
    val decForm = NumberFormat.getInstance(Locale.ENGLISH).asInstanceOf[DecimalFormat]
    val newSymbols = new DecimalFormatSymbols(Locale.ENGLISH)
    newSymbols.setExponentSeparator("D")
    newSymbols.setMinusSign('-')
    decForm.setDecimalFormatSymbols(newSymbols)
    decForm.applyPattern(".000000000000000000E00")
    decForm.setMaximumIntegerDigits(0)
    decForm.setPositivePrefix("  0")
    decForm.setNegativePrefix(" -0")
    decForm
  }

  /**
   * Format floating point numbers as:
   *
   * 0.299221166597053480D-01
   * 0.166716555849284460D+00
   *
   * Difference with `fformat` is that the sign of the exponent is always included.
   */
  private def format(d: Double) = fformat.format(d).replaceAll("D(?=[0-9])", "D+")

}
