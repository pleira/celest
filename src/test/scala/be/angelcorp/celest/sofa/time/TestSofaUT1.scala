package be.angelcorp.celest.sofa.time

import be.angelcorp.celest.data.eop.UT1Provider
import be.angelcorp.celest.time.Epoch
import org.scalatest.{FlatSpec, Matchers}

class TestSofaUT1 extends FlatSpec with Matchers {

  // The utc-ut1 step used in the sofa test function t_ttut1 and t_ut1tt
  val ut1provider = new UT1Provider {
    override def UT1_UTC(jd_utc: Epoch) = +0.3341 // = + 32.184 (TT-TAI) + 33 (TAI-UTC)+ 0.3341 (UTC-UT1) = 64.8499 TT-UT1
  }

  // Test based on t_ttut1 in t_sofa_c.c (of sofa release 10 with revision on 2014-02-20)
  "SofaUT1" should "work as the sofa iauTtut1 function" in {
    val tt  = new SofaTT
    val tai = new SofaTAI(tt)
    val utc = new SofaUTC(tt, tai)
    val ut1 = new SofaUT1(tt, utc, ut1provider)

    val epoch_tt = new SofaEpoch(2453750.5, 0.892855139, tt)(tt)

    val epoch_ut1 = ut1.toUT1( epoch_tt )
    epoch_ut1.dj1 should be (2453750.5 +- 1e-6)
    epoch_ut1.dj2 should be (0.8921045614537037037 +- 1e-12)

    val epoch_ut1_2 = epoch_tt inTimeStandard ut1
    epoch_ut1_2.dj1 should be (2453750.5 +- 1e-6)
    epoch_ut1_2.dj2 should be (0.8921045614537037037 +- 1e-12)
  }

  // Test based on t_ut1tt in t_sofa_c.c (of sofa release 10 with revision on 2014-02-20)
  it should "work as the sofa iauUt1tt function" in {
    val tt  = new SofaTT
    val tai = new SofaTAI(tt)
    val utc = new SofaUTC(tt, tai)
    val ut1 = new SofaUT1(tt, utc, ut1provider)

    val epoch_ut1 = new SofaEpoch(2453750.5, 0.892104561, ut1)(tt)

    val epoch_tt = ut1.toTT(epoch_ut1)
    epoch_tt.dj1 should be (2453750.5 +- 1e-6 )
    epoch_tt.dj2 should be (0.8928551385462962963 +- 1e-12)

    val epoch_tt2 = epoch_ut1 inTimeStandard tt
    epoch_tt2.dj1 should be (2453750.5 +- 1e-6 )
    epoch_tt2.dj2 should be (0.8928551385462962963 +- 1e-12)
  }

}