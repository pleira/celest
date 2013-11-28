import be.angelcorp.celest.universe.modules
import be.angelcorp.celest.universe.Universe
import be.angelcorp.libs.math.linear.Vector3D
import com.google.inject.{Provides, AbstractModule, Guice, Injector}

// A custom module you create/use
val myModule = new AbstractModule {
  def configure() = bind(classOf[Vector3D]).toInstance(Vector3D(1, 2, 3))
}

// Create a custom universe
implicit val universe = new Universe {
  // Ignore the framegraph
  def frames = ???

  // ??? is a method that throws NotImplementedError
  // Make TT, TAI, UTC, and UT1 all the same timeline (so effectively there is no TAI/UTC/UT1)
  val TT = new be.angelcorp.celest.time.timeStandard.TT()

  def TAI = TT

  def UTC = TT

  def UT1 = TT

  // The other time scales are undefined
  def TCG = ???

  def TDT = ???

  def TDB = ???

  def TCB = ???

  def injector: Injector = {
    val universe = this // <= used as a handle to this universe instance in the following AbstractModule (so class[Universe] => this)
    Guice.createInjector(
      new AbstractModule {
        def configure() = {
          // Bind the default behaviour for resource resolution
          install(new modules.DefaultAether)
          // Install the other custom module
          install(myModule)
          // Bind Class[Universe] to our custom implementation
          bind(classOf[Universe]).toInstance(universe)
        }

        @Provides // Provides a list of repositories used by Aether for resource resolution
        def provideRemoteRepositories = Seq(
          new org.eclipse.aether.repository.RemoteRepository.Builder("resources", "default", "file://customResources").build()
        )
      }
    )
  }
}
