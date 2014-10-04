package be.angelcorp.celest.sofa.time

import org.scalatest.{FlatSpec, Matchers}

class TestSofaUTC extends FlatSpec with Matchers {

  // Test based on t_tcbtdb in t_sofa_c.c (of sofa release 10 with revision on 2014-02-20)
  "SofaUTC" should "work as the sofa iauTcbtdb function" in {
    val tt  = new SofaTT
    val tai = new SofaTAI(tt)
    val utc = new SofaUTC(tt, tai)

    val epoch_tai = new SofaEpoch(2453750.5, 0.892482639, tai)(tt)

    val epoch_utc = utc.toUTC( tai.toTT(epoch_tai ) )
    epoch_utc.dj1 should be (2453750.5 +- 1e-6)
    epoch_utc.dj2 should be (0.8921006945555555556 +- 1e-12)

    val epoch_utc2 = epoch_tai inTimeStandard utc
    epoch_utc2.dj1 should be (2453750.5 +- 1e-6)
    epoch_utc2.dj2 should be (0.8921006945555555556 +- 1e-12)
  }

  // Test based on t_utctai in t_sofa_c.c (of sofa release 10 with revision on 2014-02-20)
  it should "work as the sofa iauUtctai function" in {
    val tt  = new SofaTT
    val tai = new SofaTAI(tt)
    val utc = new SofaUTC(tt, tai)

    val epoch_utc = new SofaEpoch(2453750.5, 0.892100694, utc)(tt)

    val epoch_tai = tai.toTAI( utc.toTT(epoch_utc) )
    epoch_tai.dj1 should be (2453750.5 +- 1e-6 )
    epoch_tai.dj2 should be (0.8924826384444444444 +- 1e-12)

    val epoch_tai2 = epoch_utc inTimeStandard tai
    epoch_tai2.dj1 should be (2453750.5 +- 1e-6 )
    epoch_tai2.dj2 should be (0.8924826384444444444 +- 1e-12)
  }

}