package be.angelcorp.celest.sofa.frames

import be.angelcorp.celest.data.eop.UT1Provider
import be.angelcorp.celest.frameGraph.frames.{CIRF, TIRF}
import be.angelcorp.celest.sofa.time._
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.universe.DefaultUniverseBuilder
import org.scalatest.{FlatSpec, Matchers}

class TestSofaEarthRotation extends FlatSpec with Matchers {

  /**
   * Based on "A Demonstration of SOFA's Earth Orientation Routines" by Catherine Hohenkerk (HM Nautical Almanac Office, UK)
   * Online: http://syrte.obspm.fr/journees2013/index.php?index=tutorial
   *         http://syrte.obspm.fr/journees2013/powerpoint/Tutorial_SOFA_Demo_jsr13.tar
   * See src/test/cpp/src/TestSofaEarthRotation.cpp
   */
  "SofaEarthRotation" should "produce the same output as sofa" in {
    implicit val universe = new DefaultUniverseBuilder().result // An empty universe
    val ut1Time = new UT1Provider { override def UT1_UTC(jd_utc: Epoch) = +0.02792e0 }
    val tt  = new SofaTT()
    val tai = new SofaTAI(tt)
    val utc = new SofaUTC(tt, tai)
    val ut1 = new SofaUT1(tt, utc, ut1Time)

    val epoch = new SofaEpoch(2456550.5, 0.72994425925925921, tt)(tt)
    val factory = new SofaEarthRotation(new CIRF(), new TIRF(), ut1, tt)
    val transform = factory.rotationMatrix( epoch )

    transform.m00 should be (-0.222199979674368 +- 1E-15)
    transform.m01 should be (-0.975001112323832 +- 1E-15)
    transform.m02 should be (-0.000000000000000 +- 1E-15)
    transform.m10 should be (+0.975001112323832 +- 1E-15)
    transform.m11 should be (-0.222199979674368 +- 1E-15)
    transform.m12 should be (+0.000000000000000 +- 1E-15)
    transform.m20 should be (+0.000000000000000 +- 1E-15)
    transform.m21 should be (+0.000000000000000 +- 1E-15)
    transform.m22 should be (+1.000000000000000 +- 1E-15)
  }

}
