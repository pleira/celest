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

  def configure {
    bind[EarthOrientationData].toProvider[DefaultEarthOrientationDataProvider].in(classOf[Singleton])
    bind[ExcessLengthOfDay].to[EarthOrientationData].in(classOf[Singleton])
    bind[UT1Provider].to[EarthOrientationData].in(classOf[Singleton])

    // Some more loose frames:
    bind[ICRS] toInstance ICRF2()
    bind[GCRS] toInstance GCRF()
    bind[ITRS] toInstance ITRF()

    // Create the reference frame graph
    bind[ReferenceFrameGraph].toProvider[GuiceReferenceFrameGraphProvider].in(classOf[Singleton])

    // Create the reference frame graph transformations
    bind[EarthRotationGAST[TIRF, ERF]].toProvider[EarthRotationGASTProvider]
    bind[IAU2000Nutation[ERF, MOD]].toProvider[IAU2000NutationProvider]
    bind[IAU2006Precession[MOD, J2000]].toProvider[IAU2006PrecessionProvider]
  }

}

class DefaultEarthOrientationDataProvider @Inject()(implicit val universe: Universe) extends Provider[DefaultEarthOrientationData] {
  def get() = new DefaultEarthOrientationData()
}

class EarthRotationGASTProvider extends Provider[EarthRotationGAST[TIRF, ERF]] {
  @Inject implicit var universe: Universe = null
  @Inject var nutation: IAU2000Nutation[ERF, MOD] = null
  @Inject var eop: EarthOrientationData = null

  def get = new EarthRotationGAST(TIRF(), ERF(), nutation, eop)
}

class IAU2000NutationProvider extends Provider[IAU2000Nutation[ERF, MOD]] {
  @Inject implicit var universe: Universe = null

  def get(): IAU2000Nutation[ERF, MOD] = new IAU2000Nutation(ERF(), MOD(), IAU2000NutationLoader.IERS2010)
}

class IAU2006PrecessionProvider extends Provider[IAU2006Precession[MOD, J2000]] {
  @Inject implicit var universe: Universe = null

  def get(): IAU2006Precession[MOD, J2000] = new IAU2006Precession[MOD, J2000](MOD(), J2000())
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
      println(thatClazz.getSimpleName)
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
