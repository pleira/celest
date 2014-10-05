package be.angelcorp.celest.sofa.time

import org.scalatest.{FlatSpec, Matchers}

class TestSofaTCB extends FlatSpec with Matchers {

  // Test based on t_tcbtdb in t_sofa_c.c (of sofa release 10 with revision on 2014-02-20)
  "SofaTCB" should "work as the sofa iauTcbtdb function" in {
    val tt  = new SofaTT
    val tdb = new SofaTDB(tt)
    val tcb = new SofaTCB(tt, tdb)

    val epoch_tcb = new SofaEpoch(2453750.5, 0.893019599, tcb)(tt)

    val epoch_tdb = tdb.toTDB( tcb.toTT(epoch_tcb ) )
    epoch_tdb.dj1 should be (2453750.5 +- 1e-6)
    epoch_tdb.dj2 should be (0.8928551362746343397 +- 1e-12)

    val epoch_tdb2 = epoch_tcb inTimeStandard tdb
    epoch_tdb2.dj1 should be (2453750.5 +- 1e-6)
    epoch_tdb2.dj2 should be (0.8928551362746343397 +- 1e-12)
  }

  // Test based on t_tdbtcb in t_sofa_c.c (of sofa release 10 with revision on 2014-02-20)
  it should "work as the sofa iauTdbtcb function" in {
    val tt  = new SofaTT
    val tdb = new SofaTDB(tt)
    val tcb = new SofaTCB(tt, tdb)

    val epoch_tdb = new SofaEpoch(2453750.5, 0.892855137, tdb)(tt)

    val epoch_tcb = tcb.toTCB( tdb.toTT(epoch_tdb) )
    epoch_tcb.dj1 should be (2453750.5 +- 1e-6 )
    epoch_tcb.dj2 should be (0.8930195997253656716 +- 1e-12)

    val epoch_tcb2 = epoch_tdb inTimeStandard tcb
    epoch_tcb2.dj1 should be (2453750.5 +- 1e-6 )
    epoch_tcb2.dj2 should be (0.8930195997253656716 +- 1e-12)
  }

}