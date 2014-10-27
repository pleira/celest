import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.universe.modules
import be.angelcorp.celest.universe.Universe
import com.google.inject.{Provides, Guice, Injector}
import net.codingwell.scalaguice.ScalaModule

// A custom module you create/use
val myModule = new ScalaModule {
  def configure() = bind[Vec3].toInstance(Vec3(1, 2, 3))
}

// Create a custom universe
implicit val universe = new Universe {
  // Ignore the framegraph
  def frames = ???

  def injector: Injector = {
    val universe = this // <= used as a handle to this universe instance in the following AbstractModule (so class[Universe] => this)
    Guice.createInjector(
      new ScalaModule {
        def configure() = {
          // Bind the default behaviour for resource resolution
          install(new modules.DefaultAether)
          // Bind the default behaviour for timescales and epochs
          install(new modules.DefaultTime)
          // Install the other custom module
          install(myModule)
          // Bind Class[Universe] to our custom implementation
          bind[Universe].toInstance(universe)
        }

        @Provides // Provides a list of repositories used by Aether for resource resolution
        def provideRemoteRepositories = Seq(
          new org.eclipse.aether.repository.RemoteRepository.Builder("resources", "default", "file://customResources").build()
        )
      }
    )
  }
}
