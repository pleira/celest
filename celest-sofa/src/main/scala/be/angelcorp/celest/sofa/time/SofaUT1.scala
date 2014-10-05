package be.angelcorp.celest.sofa.time

import javax.inject.Inject

import be.angelcorp.celest.data.eop.{EarthOrientationData, UT1Provider}
import be.angelcorp.celest.sofa.time.SofaEpoch.SofaEpochBuilder
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.time.timeStandard.TimeStandard
import be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations.{TT, UTC}
import be.angelcorp.sofa.SofaLibrary

/**
 * Sofa representation of Universal Time (UT1).
 *
 * @see [[be.angelcorp.celest.time.timeStandard.UT1Time]]
 */
class SofaUT1( tt: TimeStandard, utc: SofaUTC, ut1Provider: UT1Provider ) extends TimeStandard {

  @Inject
  def this( @TT tt: TimeStandard, @UTC utc: SofaUTC, eop: EarthOrientationData ) = this(tt, utc, eop.ut1_utc)

  /** Convert an UT1 epoch to TT (note does not use the embedded TimeStandard). */
  def toTT(ut1: SofaEpoch) = {
    val utc  = new SofaEpochBuilder(this.tt)(this.tt)
    SofaLibrary.iauUt1utc(ut1.dj1, ut1.dj2, ut1Provider.UT1_UTC( ut1 ), utc.t1, utc.t2)
    this.utc.toTT( utc.result )
  }

  /** Convert a TT epoch to UT1 (note does not use the embedded TimeStandard). */
  def toUT1(tt: SofaEpoch) = {
    val ut1 = new SofaEpochBuilder(this)(this.tt)
    val epoch_utc = utc.toUTC(tt)
    SofaLibrary.iauUtcut1(epoch_utc.dj1, epoch_utc.dj2, ut1Provider.UT1_UTC( epoch_utc ), ut1.t1, ut1.t2)
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
