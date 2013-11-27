import be.angelcorp.libs.celest.data._
import be.angelcorp.libs.celest.ephemeris.jplEphemeris
implicit val universe = new be.angelcorp.libs.celest.universe.DefaultUniverse
def toFile(fname: String, content: String) { new java.io.FileWriter(java.nio.file.Paths.get(fname).toFile).write(content) }

// Load/create any existing ephemeris.
// Here it is binary, but it can be any type
val ephemerisFile = getPath("be.angelcorp.celest.test.ephemeris", "DE405-1980-2020", "bin")
val ephemeris     = jplEphemeris.fromBinary( ephemerisFile, 405 )

// Save the ephemeris header
val headerData: String = jplEphemeris.toAsciiHeader( ephemeris )
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
val headerString: String = jplEphemeris.toAsciiHeader( ephemeris )
val dataString:   String = jplEphemeris.toAsciiData( ephemeris.records.zipWithIndex.toSeq )
toFile("ephemeris.txt", headerString + dataString)
