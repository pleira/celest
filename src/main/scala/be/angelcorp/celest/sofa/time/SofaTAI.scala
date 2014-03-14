package be.angelcorp.celest.sofa.time

import be.angelcorp.celest.time.timeStandard.TimeStandard
import be.angelcorp.celest.time.Epoch
import be.angelcorp.sofa.SofaLibrary._
import be.angelcorp.sofa.SofaLibrary
import be.angelcorp.celest.sofa.time.SofaEpoch.SofaEpochBuilder

/**
 * Sofa representation of the International Atomic Time (TAI).
 *
 * Based on the sofa functions iauTttai/iauTaitt or the macro's TTMTAI and DAYSEC.
 *
 * @see [[be.angelcorp.celest.time.timeStandard.TAITime]]
 */
class SofaTAI(val tt: TimeStandard) extends TimeStandard {

  /** Convert a TAI epoch to TT (note does not use the embedded TimeStandard). */
  def toTAI( tt: SofaEpoch ) = {
    val tai = new SofaEpochBuilder(this)(this.tt)
    SofaLibrary.iauTttai( tt.dj1, tt.dj2, tai.t1, tai.t2 )
    tai.result
  }

  /** Convert a TT epoch to TAI (note does not use the embedded TimeStandard). */
  def toTT( tai: SofaEpoch ) = {
    val tt = new SofaEpochBuilder(this.tt)(this.tt)
    SofaLibrary.iauTaitt( tai.dj1, tai.dj2, tt.t1, tt.t2 )
    tt.result
  }

  override def offsetToTT(JD_this: Epoch) = TTMTAI/DAYSEC

  override def offsetFromTT(JD_tt: Epoch) = - TTMTAI/DAYSEC

}
