import be.angelcorp.celest.universe.DefaultUniverse
import com.google.inject.{AbstractModule, Module}

// Define a basic class
class MyClass(val message: String)

implicit val universe = new DefaultUniverse {
  override def configurationModules: Iterable[Module] = super.configurationModules ++ Seq(
    // Add a new module to the injector configuration to create a binding for MyClass
    new AbstractModule {
      // Create a link between the MyClass class and an implementation of that class
      def configure() = bind(classOf[MyClass]).toInstance(new MyClass("Hello injector"))
    }
  )
}

// Print the message of the MyClass instance held by the injector
println(universe.injector.getInstance(classOf[MyClass]).message)
