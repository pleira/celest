import be.angelcorp.celest.time.timeStandard.{TimeStandardAnnotations, TimeStandard}

// Create a universe defining TT/TAI/UTC/...
implicit val universe = new be.angelcorp.celest.universe.DefaultUniverse

// Explicitly get an instance of the TT time standard
val TT_time = universe.getInstance(classOf[TimeStandard], classOf[TimeStandardAnnotations.TT])

// For shorthand notation, import the following
import be.angelcorp.celest.time.timeStandard.TimeStandards._
// Users can simply retrieve TT as follows:
val UTC_time = UTC
// Time standards can be directly passed to methods too
println( TAI )
