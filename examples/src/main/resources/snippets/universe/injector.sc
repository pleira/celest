import be.angelcorp.celest.universe.DefaultUniverseBuilder
import net.codingwell.scalaguice.ScalaModule

// Define a basic class
class MyClass(val message: String)

// Create a builder for the universe, in this object all the wiring for the final universe takes  place.
val builder = new DefaultUniverseBuilder {
  // Add a new module to the injector configuration to create a binding for MyClass
  modules += new ScalaModule {
    // Create a link between the MyClass class and an implementation of that class
    def configure() = bind[MyClass].toInstance(new MyClass("Hello injector"))
  }
}

// Create the actual universe
implicit val universe = builder.result

// Print the message of the MyClass instance held by the injector
println(universe.instance[MyClass].message)
