package be.angelcorp.celest.sofa.time

import org.scalatest.{FlatSpec, Matchers}

class TestSofaTDB extends FlatSpec with Matchers {

  // Test based on t_tdbtt in t_sofa_c.c (of sofa release 10 with revision on 2014-02-20)
  // TODO: sofa test depends on TDB-TT = -0.000201s
  ignore should "work as the sofa iauTdbtt function" in {
    val tt  = new SofaTT
    val tdb = new SofaTDB(tt)

    val epoch_tdb = new SofaEpoch(2453750.5, 0.892482639, tdb)(tt)

    val epoch_tt = tdb.toTT( epoch_tdb )
    epoch_tt.dj1 should be (2453750.5 +- 1e-6)
    epoch_tt.dj2 should be (0.8928551393263888889 +- 1e-12)

    val epoch_tt2 = epoch_tdb inTimeStandard tdb
    epoch_tt2.dj1 should be (2453750.5 +- 1e-6)
    epoch_tt2.dj2 should be (0.8928551393263888889 +- 1e-12)
  }

  // Test based on t_tttdb in t_sofa_c.c (of sofa release 10 with revision on 2014-02-20)
  // TODO: sofa test depends on TDB-TT = -0.000201s
  ignore should "work as the sofa iauTttdb function" in {
    val tt  = new SofaTT
    val tdb = new SofaTDB(tt)

    val epoch_tt = new SofaEpoch(2453750.5, 0.892862531, tt)(tt)

    val epoch_tdb = tdb.toTDB(epoch_tt)
    epoch_tdb.dj1 should be (2453750.5 +- 1e-6 )
    epoch_tdb.dj2 should be (0.8928551366736111111 +- 1e-12)

    val epoch_tdb2 = epoch_tt inTimeStandard tdb
    epoch_tdb2.dj1 should be (2453750.5 +- 1e-6 )
    epoch_tdb2.dj2 should be (0.8928551366736111111 +- 1e-12)
  }

}