package be.angelcorp.celest.universe.modules

import net.codingwell.scalaguice.ScalaModule
import be.angelcorp.celest.frameGraph.frames.{ICRF2, ICRS}

class DefaultFrames extends ScalaModule {

  def configure() {
    bind[ICRS] toInstance ICRF2()
  }

}
