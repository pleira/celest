package be.angelcorp.celest.sofa.time

import javax.inject.Inject
import be.angelcorp.sofa.SofaLibrary
import be.angelcorp.celest.time.timeStandard.TimeStandard
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.data.UT1Provider
import be.angelcorp.celest.sofa.time.SofaEpoch.SofaEpochBuilder

/**
 * Sofa representation of Universal Time (UT1).
 *
 * @see [[be.angelcorp.celest.time.timeStandard.UT1Time]]
 */
class SofaUT1@Inject()( tt: TimeStandard, ut1Provider: UT1Provider, utc: SofaUTC ) extends TimeStandard {

  /** Convert an UT1 epoch to TT (note does not use the embedded TimeStandard). */
  def toTT(ut1: SofaEpoch) = {
    val tt  = new SofaEpochBuilder(this.tt)(this.tt)
    val utc = ut1 // TODO: Not entirely correct, up to 1s difference between UTC <> UT1
    SofaLibrary.iauUt1tt(ut1.dj1, ut1.dj2, ut1Provider.UT1_UTC( utc ), tt.t1, tt.t2)
    tt.result
  }

  /** Convert a TT epoch to UT1 (note does not use the embedded TimeStandard). */
  def toUT1(tt: SofaEpoch) = {
    val ut1 = new SofaEpochBuilder(this)(this.tt)
    SofaLibrary.iauUt1tt(tt.dj1, tt.dj2, ut1Provider.UT1_UTC( utc.toUTC(tt) ), ut1.t1, ut1.t2)
    ut1.result
  }

  override def offsetToTT(JD_this: Epoch): Double = {
    val ut1 = SofaEpoch(JD_this)(this.tt)
    val tt  = toTT( ut1 )
    tt relativeToS ut1
  }

  override def offsetFromTT(JD_tt: Epoch): Double = {
    val tt  = SofaEpoch(JD_tt)(this.tt)
    val ut1 = toUT1( tt )
    ut1 relativeToS tt
  }

}
