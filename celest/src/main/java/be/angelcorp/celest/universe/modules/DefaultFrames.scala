package be.angelcorp.celest.universe.modules

import javax.inject.{Inject, Singleton}
import scala.Some
import scala.collection.JavaConverters._
import net.codingwell.scalaguice.ScalaModule
import com.google.inject.{Injector, Provider}
import be.angelcorp.celest.data._
import be.angelcorp.celest.frameGraph._
import be.angelcorp.celest.frameGraph.frames._
import be.angelcorp.celest.frameGraph.frames.transforms._
import be.angelcorp.celest.universe.Universe

class DefaultFrames extends ScalaModule {

  /**
   * Create the bindings that load any data files required for the frames/tranformations
   */
  def configureData() {
    bind[EarthOrientationData].toProvider[DefaultEarthOrientationDataProvider].in(classOf[Singleton])
    bind[ExcessLengthOfDay].to[EarthOrientationData].in(classOf[Singleton])
    bind[UT1Provider].to[EarthOrientationData].in(classOf[Singleton])
    bind[PolarOrientationData].to[EarthOrientationData].in(classOf[Singleton])
  }

  /**
   * Create the bindings that bind the reference systems to their correct frame implementations
   */
  def configureSystems() {
    bind[ITRS] toInstance ITRF()
    bind[TIRS] toInstance TIRF()
    bind[ERS] toInstance ERF()
    bind[MODSystem] toInstance MOD()
    bind[J2000System] toInstance J2000()
    bind[GCRS] toInstance GCRF()
    bind[ICRS] toInstance ICRF2()
  }

  /**
   * Create the bindings for the reference frame graph transformations
   */
  def configureTransformations() {
    bind[PolarMotion[TIRS, ITRS]].toProvider[PolarMotionProvider].in(classOf[Singleton])
    bind[EarthRotationGAST[TIRS, ERS]].toProvider[EarthRotationGASTProvider].in(classOf[Singleton])
    bind[IAU2000Nutation[MODSystem, ERS]].toProvider[IAU2000NutationProvider].in(classOf[Singleton])
    bind[IAU2006Precession[MODSystem, J2000System]].toProvider[IAU2006PrecessionProvider].in(classOf[Singleton])
    bind[J2000FrameBias[J2000System, GCRS]].toProvider[J2000FrameBiasProvider].in(classOf[Singleton])
  }

  def configure() {
    configureData()
    configureSystems()

    // Create the reference frame graph
    bind[ReferenceFrameGraph].toProvider[GuiceReferenceFrameGraphProvider].in(classOf[Singleton])

    configureTransformations()
  }

}

class DefaultEarthOrientationDataProvider @Inject()(implicit val universe: Universe) extends Provider[DefaultEarthOrientationData] {
  def get() = new DefaultEarthOrientationData()
}

class PolarMotionProvider extends Provider[PolarMotion[TIRS, ITRS]] {
  @Inject implicit var universe: Universe = null
  @Inject var eop: EarthOrientationData = null
  @Inject var itrs: ITRS = null
  @Inject var tirs: TIRS = null

  def get = new PolarMotion(tirs, itrs, eop)
}

class EarthRotationGASTProvider extends Provider[EarthRotationGAST[TIRS, ERS]] {
  @Inject implicit var universe: Universe = null
  @Inject var nutation: IAU2000Nutation[MODSystem, ERS] = null
  @Inject var eop: EarthOrientationData = null
  @Inject var tirs: TIRS = null
  @Inject var ers: ERS = null

  def get = new EarthRotationGAST(tirs, ers, nutation, eop)
}

class IAU2000NutationProvider extends Provider[IAU2000Nutation[MODSystem, ERS]] {
  @Inject implicit var universe: Universe = null
  @Inject implicit var ers: ERS = null
  @Inject implicit var mod: MODSystem = null

  def get() = new IAU2000Nutation(mod, ers, IAU2000NutationLoader.IERS2010)
}

class IAU2006PrecessionProvider extends Provider[IAU2006Precession[MODSystem, J2000System]] {
  @Inject implicit var universe: Universe = null
  @Inject implicit var j2000: J2000System = null
  @Inject implicit var mod: MODSystem = null

  def get(): IAU2006Precession[MODSystem, J2000System] = new IAU2006Precession(mod, j2000)
}

class J2000FrameBiasProvider extends Provider[J2000FrameBias[J2000System, GCRS]] {
  @Inject implicit var universe: Universe = null
  @Inject implicit var j2000: J2000System = null
  @Inject implicit var gcrs: GCRS = null

  def get(): J2000FrameBias[J2000System, GCRS] = new J2000FrameBias[J2000System, GCRS](j2000, gcrs)
}

/**
 * Provider that automatically builds the reference frame graph from all transformations attached to the guice Injector.
 */
class GuiceReferenceFrameGraphProvider extends Provider[ReferenceFrameGraphImpl] {
  @Inject var injector: Injector = null

  def get(): ReferenceFrameGraphImpl = {
    val graph = ReferenceFrameGraphImpl.graph

    // Obtain a list of all bound transformations, together with there starting and ending frame.
    val transformations = injector.getAllBindings.keySet().asScala.map(key => {
      val thatClazz = key.getTypeLiteral.getRawType
      if (classOf[ReferenceFrameTransformFactory[_, _]].isAssignableFrom(thatClazz))
        Some(injector.getInstance(key).asInstanceOf[ReferenceFrameTransformFactory[ReferenceSystem, ReferenceSystem]])
      else
        None
    }).flatten

    // Create a list of all the transformation in the frame graph, and add them
    val frames = transformations.map(t => List(t.fromFrame, t.toFrame)).flatten
    frames.foreach(graph.addVertex)

    // Now add all the transformations between the frames
    transformations.foreach(t => {
      graph.addEdge(t.fromFrame, t.toFrame, t)
      graph.addEdge(t.toFrame, t.fromFrame, t.inverse)
    })

    new ReferenceFrameGraphImpl(graph)
  }

}
