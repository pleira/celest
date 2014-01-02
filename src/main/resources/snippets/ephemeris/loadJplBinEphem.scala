import be.angelcorp.celest.ephemeris.jplEphemeris
import be.angelcorp.celest.ephemeris.jplEphemeris.JplEphemeris

implicit val universe = new be.angelcorp.celest.universe.DefaultUniverse

// Download the binary data file and save to disk
val urlBase = "http://static.angelcorp.be/projects/celest/testResources/lnx1600.405"
// Alternatively use "ftp://ssd.jpl.nasa.gov/pub/eph/planets/Linux/de405/lnx1600.405"
val path = java.nio.file.Files.createTempFile("lnx1600", "405")
val stream = java.net.URI.create(urlBase).toURL.openStream()
java.nio.file.Files.copy(stream, path, java.nio.file.StandardCopyOption.REPLACE_EXISTING)

// Read in the downloaded ephemeris file
val ephemeris: JplEphemeris[_] = jplEphemeris.fromBinary(path, 405)

// Read in the downloaded ephemeris file using a specified alignment and padding
val ephemeris2 = jplEphemeris.fromBinary(path, 405,
  endiannessHint = Some(java.nio.ByteOrder.LITTLE_ENDIAN),
  alignmentHint = Some(be.angelcorp.celest.util.PackedAlignment.instance)
)
