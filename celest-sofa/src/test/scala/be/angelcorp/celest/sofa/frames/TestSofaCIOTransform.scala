package be.angelcorp.celest.sofa.frames

import be.angelcorp.celest.data.eop.{PoleProvider, UT1Provider}
import be.angelcorp.celest.frameGraph.frames.{GCRF, ITRF2000}
import be.angelcorp.celest.physics.Units._
import be.angelcorp.celest.sofa.time._
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.universe.DefaultUniverseBuilder
import org.scalatest.{FlatSpec, Matchers}

class TestSofaCIOTransform extends FlatSpec with Matchers {

  /**
   * Based on "A Demonstration of SOFA's Earth Orientation Routines" by Catherine Hohenkerk (HM Nautical Almanac Office, UK)
   * Online: http://syrte.obspm.fr/journees2013/index.php?index=tutorial
   *         http://syrte.obspm.fr/journees2013/powerpoint/Tutorial_SOFA_Demo_jsr13.tar
   * See src/test/cpp/src/TestSofaCIOTransform.cpp
   */
  "SofaCIOTransform" should "produce the same output as sofa" in {
    implicit val universe = new DefaultUniverseBuilder().result // An empty universe
    val ut1Time = new UT1Provider { override def UT1_UTC(jd_utc: Epoch) = +0.02792e0 }
    val tt  = new SofaTT()
    val tai = new SofaTAI(tt)
    val utc = new SofaUTC(tt, tai)
    val ut1 = new SofaUT1(tt, utc, ut1Time)
    val cip = new PoleProvider {
      override def polarCoordinatesOn(epoch: Epoch) = ( arcSeconds(+0.1574e0), arcSeconds(+0.3076e0) )
    }

    val epoch = new SofaEpoch(2456550.5, 0.72994425925925921, tt)(tt)
    val factory = new SofaCIOTransform(new GCRF(), new ITRF2000(), cip, tt, ut1)
    val transform = factory.rotationMatrix( epoch )

    transform.m00 should be (-0.222199829375610 +- 1E-15)
    transform.m01 should be (-0.975001109581690 +- 1E-15)
    transform.m02 should be (+0.000268588762397 +- 1E-15)
    transform.m10 should be (+0.975000204156859 +- 1E-15)
    transform.m11 should be (-0.222199989186271 +- 1E-15)
    transform.m12 should be (-0.001329172564104 +- 1E-15)
    transform.m20 should be (+0.001355625144927 +- 1E-15)
    transform.m21 should be (-0.000033467818783 +- 1E-15)
    transform.m22 should be (+0.999999080579763 +- 1E-15)
  }

}
