package be.angelcorp.celest.sofa.time

import org.scalatest.{FlatSpec, Matchers}

class TestSofaTCG extends FlatSpec with Matchers {

  // Test based on iauTttcg in t_sofa_c.c (of sofa release 10 with revision on 2014-02-20)
  "SofaTCG" should "work as the sofa iauTcbtdb function" in {
    val tt  = new SofaTT
    val tcg = new SofaTCG(tt)

    val epoch_tt = new SofaEpoch(2453750.5, 0.892482639, tt)(tt)

    val epoch_tcg = tcg.toTCG( epoch_tt )
    epoch_tcg.dj1 should be (2453750.5 +- 1e-6)
    epoch_tcg.dj2 should be (0.8924900312508587113 +- 1e-12)

    val epoch_tcg2 = epoch_tt inTimeStandard tcg
    epoch_tcg2.dj1 should be (2453750.5 +- 1e-6)
    epoch_tcg2.dj2 should be (0.8924900312508587113 +- 1e-12)
  }

  // Test based on t_tcgtt in t_sofa_c.c (of sofa release 10 with revision on 2014-02-20)
  it should "work as the sofa iauTcgtt function" in {
    val tt  = new SofaTT
    val tcg = new SofaTCG(tt)

    val epoch_tcg = new SofaEpoch(2453750.5, 0.892862531, tcg)(tt)

    val epoch_tt = tcg.toTT(epoch_tcg)
    epoch_tt.dj1 should be (2453750.5 +- 1e-6 )
    epoch_tt.dj2 should be (0.8928551387488816828 +- 1e-12)

    val epoch_tt2 = epoch_tcg inTimeStandard tt
    epoch_tt2.dj1 should be (2453750.5 +- 1e-6 )
    epoch_tt2.dj2 should be (0.8928551387488816828 +- 1e-12)
  }

}