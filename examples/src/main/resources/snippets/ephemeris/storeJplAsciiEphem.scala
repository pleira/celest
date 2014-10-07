import be.angelcorp.celest.resources._
import be.angelcorp.celest.ephemeris.jplEphemeris

implicit val universe = new be.angelcorp.celest.universe.DefaultUniverse
def toFile(fname: String, content: String) {
  val path = java.nio.file.Paths.get(fname)           // File in which to save the content
  new java.io.FileWriter(path.toFile).write(content)  // Save the actual content
  path.toFile.deleteOnExit()                          // Ask the JVM to remove the file after the example is run
}

// Load/create any existing ephemeris.
// Here it is binary, but it can be any type
val ephemerisResource = Resources.find( ResourceDescription("be.angelcorp.celest.test.ephemeris", "DE405-1980-2020", extension = "bin") ).get
val ephemerisFile = PathResource( ephemerisResource ).path // Make sure that the resource is mapped to a file, and is not eg. a stream
val ephemeris = jplEphemeris.fromBinary(ephemerisFile, 405)

// Save the ephemeris header
val headerData: String = jplEphemeris.toAsciiHeader(ephemeris)
toFile("header.txt", headerData)

// Save all the data in the ephemeris over groups of 10 records:
// Create a list of all the records, with their respective index
val records = ephemeris.records.zipWithIndex
// Group the records in batches of 10 (so that there are 10 records per file saved) and add the batch number
val batches = records.grouped(10).zipWithIndex
// Save each batch to a separate file
batches.foreach {
  case (subRecords, batchNr) =>
    // For each subset, convert the selected records to string format
    val dataString = jplEphemeris.toAsciiData(subRecords)
    // And save the result to a new data file
    toFile("datafile_" + batchNr + ".txt", dataString)
}


// Alternative: Save the header and all the data in a single file (not recommended)
val headerString: String = jplEphemeris.toAsciiHeader(ephemeris)
val dataString: String = jplEphemeris.toAsciiData(ephemeris.records.zipWithIndex.toSeq)
toFile("ephemeris.txt", headerString + dataString)

