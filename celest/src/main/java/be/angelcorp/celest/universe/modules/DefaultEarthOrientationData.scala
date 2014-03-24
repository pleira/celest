package be.angelcorp.celest.universe.modules

import be.angelcorp.celest.universe.UniverseBuilder
import net.codingwell.scalaguice.ScalaModule
import be.angelcorp.celest.data.eop
import be.angelcorp.celest.data.eop.EarthOrientationData

trait DefaultEarthOrientationData extends UniverseBuilder {

  modules += DefaultEarthOrientationData.module

}

object DefaultEarthOrientationData {

  def module = new ScalaModule {
    override def configure() {
      bind[EarthOrientationData].to[eop.DefaultEarthOrientationData]
    }
  }

}
