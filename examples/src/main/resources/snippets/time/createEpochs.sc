import be.angelcorp.celest.time.JulianDate
import be.angelcorp.celest.time.timeStandard.TimeStandards._

implicit val universe = new be.angelcorp.celest.universe.DefaultUniverse

// From a known Julian date, and given the TimeStandard
var j2000 = new JulianDate(2451545.0, TT)
// From a Gregorian calender date
j2000 = new JulianDate(2000, 1, 1, 11, 59, 27.816, TAI)
// From a year, and the fractional days in that year
val tt = TT
j2000 = new JulianDate(2000, 0.5, tt)
