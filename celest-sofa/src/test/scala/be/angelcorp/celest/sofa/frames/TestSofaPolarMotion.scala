package be.angelcorp.celest.sofa.frames

import be.angelcorp.celest.data.eop.PoleProvider
import be.angelcorp.celest.frameGraph.frames.{ITRF2000, TIRF}
import be.angelcorp.celest.physics.Units._
import be.angelcorp.celest.sofa.time._
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.universe.DefaultUniverseBuilder
import org.scalatest.{FlatSpec, Matchers}

class TestSofaPolarMotion extends FlatSpec with Matchers {

  /**
   * Based on "A Demonstration of SOFA's Earth Orientation Routines" by Catherine Hohenkerk (HM Nautical Almanac Office, UK)
   * Online: http://syrte.obspm.fr/journees2013/index.php?index=tutorial
   *         http://syrte.obspm.fr/journees2013/powerpoint/Tutorial_SOFA_Demo_jsr13.tar
   * See src/test/cpp/src/TestSofaPolarMotion.cpp
   */
  "SofaPolarMotion" should "produce the same output as sofa" in {
    implicit val universe = new DefaultUniverseBuilder().result // An empty universe
    val tt  = new SofaTT()
    val cip = new PoleProvider {
      override def polarCoordinatesOn(epoch: Epoch) = ( arcSeconds(+0.1574e0), arcSeconds(+0.3076e0) )
    }

    val epoch = new SofaEpoch(2456550.5, 0.72994425925925921, tt)(tt)
    val factory = new SofaPolarMotion(new TIRF(), new ITRF2000(), cip, tt)
    val transform = factory.rotationMatrix( epoch )

    transform.m00 should be (+0.999999999999709 +- 1E-15)
    transform.m01 should be (-0.000000000031232 +- 1E-15)
    transform.m02 should be (+0.000000763096734 +- 1E-15)
    transform.m10 should be (+0.000000000032370 +- 1E-15)
    transform.m11 should be (+0.999999999998888 +- 1E-15)
    transform.m12 should be (-0.000001491286883 +- 1E-15)
    transform.m20 should be (-0.000000763096734 +- 1E-15)
    transform.m21 should be (+0.000001491286883 +- 1E-15)
    transform.m22 should be (+0.999999999998597 +- 1E-15)
  }

}