package be.angelcorp.celest.sofa.universe

import be.angelcorp.celest.sofa.frames.{SofaCIOTransform, SofaCelestialPoleTransform, SofaEarthRotation, SofaPolarMotion}
import be.angelcorp.celest.universe.UniverseBuilder
import net.codingwell.scalaguice.ScalaModule


trait SofaFrameTransformations extends UniverseBuilder {

  modules += SofaFrameTransformations.module
  
}

object SofaFrameTransformations {
  
  def module = new ScalaModule {
    override def configure() = {
      bind[SofaCIOTransform].asEagerSingleton()
      bind[SofaCelestialPoleTransform].asEagerSingleton()
      bind[SofaEarthRotation].asEagerSingleton()
      bind[SofaPolarMotion].asEagerSingleton()
    }
  }

}
