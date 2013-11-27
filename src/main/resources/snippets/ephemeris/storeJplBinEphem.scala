import be.angelcorp.libs.celest.data._
import be.angelcorp.libs.celest.ephemeris.jplEphemeris
import be.angelcorp.libs.celest.util.PackedAlignment
implicit val universe = new be.angelcorp.libs.celest.universe.DefaultUniverse

// Load/create any existing ephemeris.
// Here it is binary, but it can be any type
val ephemerisFile = getPath("be.angelcorp.celest.test.ephemeris", "DE405-1980-2020", "bin")
val ephemeris     = jplEphemeris.fromBinary( ephemerisFile, 405 )

// File in which the ephemeris will be saved
val path =  java.nio.file.Paths.get("epehemeris.bin")

// Actual serialization, of the ephemeris to the given file
be.angelcorp.libs.celest.ephemeris.jplEphemeris.toBinary(ephemeris, path)

// You can also specify what alignment/padding and endianness needs to be used
be.angelcorp.libs.celest.ephemeris.jplEphemeris.toBinary(
  ephemeris,
  path,
  endianness = java.nio.ByteOrder.LITTLE_ENDIAN,
  alignment  = PackedAlignment.instance
)
