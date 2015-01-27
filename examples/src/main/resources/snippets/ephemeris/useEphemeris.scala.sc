import be.angelcorp.celest.ephemeris.jplEphemeris.{Earth, JplEphemeris}
import be.angelcorp.celest.frameGraph.frames.ICRS
import be.angelcorp.celest.state.PosVel
import be.angelcorp.celest.time.{Epoch, JulianDate}

implicit val universe = new be.angelcorp.celest.universe.DefaultUniverse

// Obtaining the ephemeris object from the current universe
val ephemeris = universe.instance[JplEphemeris[ICRS]]

// Create a table for the ephemeris of Earth
val start = new JulianDate(2000,1,1,0,0,0.0)
val earth = ephemeris.body( Earth() )
val earthEphem: IndexedSeq[(Epoch, PosVel[ICRS])] =
  for (date <- start until start + 365) yield {
    date -> earth.orbit( date )
  }

// Print the table
//println( earthEphem )

// You can also compute the Lunar librations angles, and the Earth nutation angles:
val (l, dl) = ephemeris.interpolateLibration( start )
val (psi, epsilon, dpsi, depsilon) = ephemeris.interpolateNutation( start )
