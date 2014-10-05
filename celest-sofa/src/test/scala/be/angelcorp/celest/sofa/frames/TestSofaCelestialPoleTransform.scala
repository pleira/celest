package be.angelcorp.celest.sofa.frames

import be.angelcorp.celest.data.eop.PoleProvider
import be.angelcorp.celest.frameGraph.frames.{CIRF, GCRF}
import be.angelcorp.celest.physics.Units._
import be.angelcorp.celest.sofa.time.{SofaEpoch, SofaTT}
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.universe.DefaultUniverseBuilder
import org.scalatest.{FlatSpec, Matchers}

class TestSofaCelestialPoleTransform extends FlatSpec with Matchers {

  /**
   * Based on "A Demonstration of SOFA's Earth Orientation Routines" by Catherine Hohenkerk (HM Nautical Almanac Office, UK)
   * Online: http://syrte.obspm.fr/journees2013/index.php?index=tutorial
   *         http://syrte.obspm.fr/journees2013/powerpoint/Tutorial_SOFA_Demo_jsr13.tar
   * See src/test/cpp/src/TestSofaCelestialPoleTransform.cpp
   */
  "SofaCelestialPoleTransform" should "produce the same output as sofa" in {
    implicit val universe = new DefaultUniverseBuilder().result // An empty universe
    val tt = new SofaTT()
    val cipOffset = new PoleProvider {
      override def polarCoordinatesOn(epoch: Epoch) = ( arcSeconds(-0.2e-6), arcSeconds(-0.1e-6) )
    }

    val epoch = new SofaEpoch(2456550.5, 0.72994425925925921, tt)(tt)
    val factory = new SofaCelestialPoleTransform(new GCRF(), new CIRF(), cipOffset, tt)
    val transform = factory.rotationMatrix( epoch )

    transform.m00 should be (+0.999999083339440 +- 1E-15)
    transform.m01 should be (-0.000000009906291 +- 1E-15)
    transform.m02 should be (-0.001354001580088 +- 1E-15)
    transform.m10 should be (+0.000000055780499 +- 1E-15)
    transform.m11 should be (+0.999999999426057 +- 1E-15)
    transform.m12 should be (+0.000033880431031 +- 1E-15)
    transform.m20 should be (+0.001354001578976 +- 1E-15)
    transform.m21 should be (-0.000033880475501 +- 1E-15)
    transform.m22 should be (+0.999999082765498 +- 1E-15)
  }

}
