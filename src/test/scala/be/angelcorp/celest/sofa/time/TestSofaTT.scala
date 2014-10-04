package be.angelcorp.celest.sofa.time

import org.scalatest.{FlatSpec, Matchers}

class TestSofaTT extends FlatSpec with Matchers {

  "SofaTT" should "provide 0.0 offset from/to TT" in {
    val tt  = new SofaTT
    val epoch_tt = new SofaEpoch(2453750.5, 0.892482639, tt)(tt)

    tt.offsetFromTT(epoch_tt) should equal (0.0)
    tt.offsetToTT(epoch_tt)   should equal (0.0)

    val epoch_tt2 = epoch_tt inTimeStandard tt
    epoch_tt2.dj1 should equal (epoch_tt.dj1)
    epoch_tt2.dj2 should equal (epoch_tt.dj2)
  }

}