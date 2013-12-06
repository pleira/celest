import be.angelcorp.celest.ephemeris.jplEphemeris

implicit val universe = new be.angelcorp.celest.universe.DefaultUniverse

// Get the necessary files
val urlBase = "http://static.angelcorp.be/projects/celest/testResources/"
// Alternatively use "ftp://ssd.jpl.nasa.gov/pub/eph/planets/ascii/de405/"
// Download the DE405 header file from NASA, and create a reader for reading it.
val headerFile = scala.io.Source.fromURL(java.net.URI.create(urlBase + "header.405").toURL).bufferedReader()
// Download the first two DE405 data files from NASA.
val dataFiles = Seq("ascp1600.405", "ascp1620.405").map(file => {
  scala.io.Source.fromURL(java.net.URI.create(urlBase + file).toURL).bufferedReader()
})

// Save the binary ephemeris in the following file:
val output = java.nio.file.Files.createTempFile("lnx1600", "405")

// Convert the ephemeris
jplEphemeris.asciiReader2binary(headerFile, dataFiles, output)

// If you have the paths to the files, instead of readers, use jplEphemeris.ascii2binary(...)