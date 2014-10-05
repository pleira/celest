package be.angelcorp.celest.sofa.time

import org.scalatest.{FlatSpec, Matchers}

class TestSofaTAI extends FlatSpec with Matchers {

  // Test based on t_taitt in t_sofa_c.c (of sofa release 10 with revision on 2014-02-20)
  "SofaTAI" should "work as the sofa iauTaitt function" in {
    val tt  = new SofaTT()
    val tai = new SofaTAI( tt )

    val epoch_tai = new SofaEpoch(2453750.5, 0.892482639, tai)(tt)

    val epoch_tt = tai.toTT( epoch_tai )
    epoch_tt.dj1 should be (2453750.5   +- 1e-6 )
    epoch_tt.dj2 should be (0.892855139 +- 1e-12)

    val epoch_tt2 = epoch_tai inTimeStandard tt
    epoch_tt2.dj1 should be (2453750.5   +- 1e-6 )
    epoch_tt2.dj2 should be (0.892855139 +- 1e-12)
  }

  // Test based on t_tttai in t_sofa_c.c (of sofa release 10 with revision on 2014-02-20)
  it should "work as the sofa iauTttai function" in {
    val tt  = new SofaTT()
    val tai = new SofaTAI( tt )

    val epoch_tt = new SofaEpoch(2453750.5, 0.892482639, tt)(tt)

    val epoch_tai = tai.toTAI( epoch_tt )
    epoch_tai.dj1 should be (2453750.5   +- 1e-6 )
    epoch_tai.dj2 should be (0.892110139 +- 1e-12)

    val epoch_tai2 = epoch_tt inTimeStandard tai
    epoch_tai2.dj1 should be (2453750.5   +- 1e-6 )
    epoch_tai2.dj2 should be (0.892110139 +- 1e-12)
  }

}