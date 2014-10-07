import be.angelcorp.celest.resources._
import be.angelcorp.celest.ephemeris.jplEphemeris
import be.angelcorp.celest.ephemeris.jplEphemeris.JplEphemeris
import be.angelcorp.celest.time.{JulianDate, Epoch}

implicit val universe = new be.angelcorp.celest.universe.DefaultUniverse

// Load the DE405 ephemeris
val ephemerisResource = Resources.find( ResourceDescription("be.angelcorp.celest.test.ephemeris", "DE405-1980-2020", extension = "bin") ).get
val ephemerisFile = PathResource( ephemerisResource ).path // Make sure that the resource is mapped to a file, and is not eg. a stream
val ephemeris = jplEphemeris.fromBinary(ephemerisFile, 405)

// Range of the new ephemeris
val start: Epoch = new JulianDate(2451545.0)
val end: Epoch = new JulianDate(2451600.0)

// Create a subset ranging from 2451545jd to 2451600jd
val subEphemeris: JplEphemeris[_] = jplEphemeris.SubsetEphemeris(ephemeris, start, end)
