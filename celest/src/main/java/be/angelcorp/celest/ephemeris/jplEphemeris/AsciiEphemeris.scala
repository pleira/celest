/**
 * Copyright (C) 2012 Simon Billemont <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package be.angelcorp.celest.ephemeris.jplEphemeris

import org.slf4j.LoggerFactory
import scala.util.parsing.combinator.JavaTokenParsers
import scala.util.parsing.input.Reader
import scala.collection.mutable.ListBuffer
import scala.collection.immutable.ListMap
import be.angelcorp.celest.time.JulianDate
import be.angelcorp.celest.universe.Universe
import be.angelcorp.celest.time.timeStandard.TimeStandards.TDB
import be.angelcorp.celest.frameGraph.ReferenceSystem
import be.angelcorp.celest.frameGraph.frames.ICRS

class AsciiEphemeris[F <: ReferenceSystem](val metadata: Metadata, private val recordsList: ListBuffer[DataRecord], val frame: F) extends JplEphemeris[F] {

  def records = recordsList.iterator

  def addRecords(newRecords: Seq[DataRecord]) = {
    val existingRecords = recordsList.map(_.begin)
    val filteredRecords = newRecords.filterNot(entry => existingRecords.exists(_ == entry.begin))
    recordsList ++= filteredRecords
  }

  def getRecord(index: Int): DataRecord = {
    // Offset, to correct for the fact that the ephemeris might be a subset
    val offset = recordsList.headOption match {
      case Some(head) => epoch2index(head.end)
      case _ => 0
    }

    recordsList(index - offset)
  }


}

class AsciiParser(implicit universe: Universe) extends JavaTokenParsers {
  private val logger = LoggerFactory.getLogger(getClass)

  def ephemeris(input: Reader[Char]): AsciiEphemeris[ICRS] = parseAll(ephemeris, input) match {
    case Success(jde, _) => jde
    case NoSuccess(err, in) => throw new java.util.InputMismatchException(err + s" (at ${in.pos}):\n" + in.pos.longString)
  }

  def ephemeris(input: java.io.Reader): AsciiEphemeris[ICRS] = parseAll(ephemeris, input) match {
    case Success(jde, _) => jde
    case NoSuccess(err, in) => throw new java.util.InputMismatchException(err + s" (at ${in.pos}):\n" + in.pos.longString)
  }

  def ephemeris(input: java.lang.CharSequence): AsciiEphemeris[ICRS] = parseAll(ephemeris, input) match {
    case Success(jde, _) => jde
    case NoSuccess(err, in) => throw new java.util.InputMismatchException(err + s" (at ${in.pos}):\n" + in.pos.longString)
  }


  def ephemerisData(metadata: Metadata, input: Reader[Char]): List[DataRecord] = parseAll(ephemerisData, input) match {
    case Success(dataRecords, _) => dataRecords(metadata)
    case NoSuccess(err, in) => throw new java.util.InputMismatchException(err + s" (at ${in.pos}):\n" + in.pos.longString)
  }

  def ephemerisData(metadata: Metadata, input: java.io.Reader): List[DataRecord] = parseAll(ephemerisData, input) match {
    case Success(dataRecords, _) => dataRecords(metadata)
    case NoSuccess(err, in) => throw new java.util.InputMismatchException(err + s" (at ${in.pos}):\n" + in.pos.longString)
  }

  def ephemerisData(metadata: Metadata, input: java.lang.CharSequence): List[DataRecord] = parseAll(ephemerisData, input) match {
    case Success(dataRecords, _) => dataRecords(metadata)
    case NoSuccess(err, in) => throw new java.util.InputMismatchException(err + s" (at ${in.pos}):\n" + in.pos.longString)
  }

  /** Parser pattern to parse and convert the header file followed by (optional) data records into a ephemeris object */
  private def ephemeris: Parser[AsciiEphemeris[ICRS]] = {
    (((size ~ group101 ~ group103 ~ group104 ~ group105) ^^ {
      case x =>
        // TODO: this is a bit silly
        val coefficientInfo = x._2
        val g104 = x._1._2
        val g103 = x._1._1._2
        val g101 = x._1._1._1._2
        val sz = x._1._1._1._1

        val tags = g104._2
        val range = JulianDate(g103._1, TDB).until(JulianDate(g103._2, TDB), g103._3)

        new Metadata(sz._2, g101(0), g101(1), g101(2),
          tags, range, tags.get("AU").get, tags.get("EMRAT").get,
          coefficientInfo, tags.get("DENUM").get.toInt)
    }) ~ group107 <~ rep(textLine)) ^^ {
      case x => new AsciiEphemeris(
        x._1,
        ListBuffer() ++= x._2.map(y => new DataRecord(x._1, y._3.toArray.take(x._1.recordEntries))),
        universe.instance[ICRS]
      )
    }
  }

  /** Parser pattern to parse additional data files (not the header, eg ascp2000.405) */
  private def ephemerisData: Parser[Metadata => List[DataRecord]] =
    (group107Data <~ rep(textLine)) ^^ {
      case x => metadata => x.map(y => new DataRecord(metadata, y._3.toArray.take(metadata.recordEntries)))
    }

  /** Parser to parse the first header line (with KSIZE and NCOEFF) */
  private def size: Parser[(Int, Int)] =
    (("""\s*KSIZE\s*=?\s*""".r ~> integer <~ """\s*""".r) ~
      ("""NCOEFF\s*=?\s*""".r ~> integer <~ """\s*""".r)) ^^ {
      case x => (x._1, x._2)
    }

  /** Parser to parse group 1010, the ephemeris string description */
  private def group101: Parser[List[String]] =
    """GROUP\s*1010\s*""".r ~> repN(3, textLine) <~ """\s*""".r

  /** Parser to parse group 1030, the ephemeris epoch range and record size */
  private def group103: Parser[(Double, Double, Double)] =
    ("""GROUP\s*1030\s*""".r ~> repN(3, double <~ """\s*""".r)) ^^ {
      case x => (x(0), x(1), x(2))
    }

  /** Parser to parse group 1040|1041, the additional metadata tags */
  private def group104: Parser[(Int, Map[String, Double])] =
    (group1040 ~ group1041) ^^ {
      x => (x._1._1, ListMap(x._1._2 zip x._2._2: _*))
    }

  /** Parser to parse group 1040, the additional metadata tag names */
  private def group1040: Parser[(Int, List[String])] =
    (("""GROUP\s*1040\s+""".r ~> integer) ~ rep(keyword)) ^^ {
      x => (x._1, x._2)
    }

  /** Parser to parse group 1041, the additional metadata tag values */
  private def group1041: Parser[(Int, List[Double])] =
    (("""GROUP\s*1041\s+""".r ~> integer) ~ rep(double)) ^^ {
      x => (x._1, x._2)
    }

  /** Parser to parse group 105, record metadata for each planet (entry point, nr coefficients, granules) */
  private def group105: Parser[List[RecordMetadata]] =
    ("""GROUP\s*1050\s+""".r ~> rep(integer)) ^^ {
      x =>
        val y = x.sliding(x.size / 3, x.size / 3).toList
        val z = y.transpose
        val u = z.collect {
          case List(x, y, z) => new RecordMetadata(x, y, z)
        }
        u
    }

  /** Parser to parse group 1070, all the data records */
  private def group107: Parser[List[(Int, Int, List[Double])]] =
    """GROUP\s+1070\s*""".r ~> group107Data

  /** Parser to parse group 1070, all the data records (but without the group title included) */
  private def group107Data: Parser[List[(Int, Int, List[Double])]] =
    rep(repN(2, integer) ~ rep(double)) ^^ {
      case x => x.map(y => (y._1(0), y._1(1), y._2))
    }

  /** Parser to read one line of text information */
  private def textLine: Parser[String] = """\s*""".r ~> """[^\n]+""".r

  /** Parser to read one word that is not GROUP */
  private def keyword: Parser[String] = not( """GROUP""".r) ~> """[^\s]+""".r <~ """s*""".r

  /** Parser to read integer number */
  private def integer: Parser[Int] = wholeNumber ^^ (_.toInt)

  /** Parser to read double floating point number */
  private def double: Parser[Double] = floatingPointNumber ^^ (_.toDouble)

  /**
   * The format as specified by JavaTokenParsers and toDouble cannot parse 'D' as the exponent specifier.
   * eg. 0.405D+03
   */
  override def floatingPointNumber: Parser[String] =
    """-?(\d+\.\d*|\d*\.\d+)([eEdD][+-]?\d+)?[fFdD]?""".r ^^ {
      _.replace('D', 'E')
    }

}
