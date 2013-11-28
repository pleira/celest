import be.angelcorp.libs.celest.universe.{DefaultUniverse, Universe}

// Create an implicit universe
implicit val universe: Universe = new DefaultUniverse()

// Define a function requiring information from a universe
def printMessage(message: String)(implicit universeParam: Universe) {
  println( message + " " + universeParam.getClass.getSimpleName )
}

// Invoke the method, explicitly passing the universe
printMessage("Welcome to the universe")( universe )
// Invoke the method, implicitly passing the universe
printMessage("Welcome again to the universe")

