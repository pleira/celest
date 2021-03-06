import be.angelcorp.celest.resources._
import be.angelcorp.celest.ephemeris.jplEphemeris
import be.angelcorp.celest.util.PackedAlignment

implicit val universe = new be.angelcorp.celest.universe.DefaultUniverse

// Load/create any existing ephemeris.
// Here it is binary, but it can be any type
val ephemerisResource = Resources.find( ResourceDescription("be.angelcorp.celest.test.ephemeris", "DE405-1980-2020", extension = "bin") ).get
val ephemerisFile = PathResource( ephemerisResource ).path // Make sure that the resource is mapped to a file, and is not eg. a stream
val ephemeris = jplEphemeris.fromBinary(ephemerisFile, 405)

// File in which the ephemeris will be saved
val path = java.nio.file.Paths.get("ephemeris.bin")

// Actual serialization, of the ephemeris to the given file
jplEphemeris.toBinary(ephemeris, path)

// You can also specify what alignment/padding and endianness needs to be used
jplEphemeris.toBinary(
  ephemeris,
  path,
  endianness = java.nio.ByteOrder.LITTLE_ENDIAN,
  alignment = PackedAlignment.instance
)

// Cleanup the ehemeris after the example
java.nio.file.Files.deleteIfExists(path)
