import be.angelcorp.celest.time.{EpochAnnotations, Epoch}

// Create a universe defining the fixed epochs ...
implicit val universe = new be.angelcorp.celest.universe.DefaultUniverse

// Explicitly get an instance of the J2000.0 Epoch
val j2000: Epoch = universe.instance[Epoch, EpochAnnotations.J2000]

// For shorthand notation, import the following
import be.angelcorp.celest.time.Epochs._

// Users can then simply retrieve a fixed as follows:
val j1950: Epoch = J1950
// Furthermore, the retrieval methods for these epochs can be directly passed to methods
println( J2000 )
