import be.angelcorp.celest.time.JulianDate
import be.angelcorp.celest.time.timeStandard.TimeStandards._

implicit val universe = new be.angelcorp.celest.universe.DefaultUniverse

val epoch = new JulianDate(2013, 10, 26, 1, 53, 28.988, UTC)

// Compute the amount of days between two epochs (note does not correct for the different time standard)
epoch relativeTo new JulianDate(2451545.0, TT)
// For this correction, first convert into the same format
val epoch_tt = epoch inTimeStandard TT
val δ = epoch_tt relativeTo new JulianDate(2451545.0, TT)

// Basic operators (all arguments are in days)
val epoch2 = epoch + 1.0
val epoch3 = epoch - 1.0

// Basic comparison
if (epoch2 > epoch )   println( "epoch2 is later than epoch" )
if (epoch  < epoch3)   println( "epoch3 is later than epoch" )
if (epoch == epoch_tt) println( "epoch and epoch_tt are equal" )

// Get the java representation of the epoch
val java_date = epoch.date
