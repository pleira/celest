package be.angelcorp.celest.sofa.time

import javax.inject.Inject
import be.angelcorp.sofa.SofaLibrary
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.time.timeStandard.{TimeStandard, UTCTime}
import be.angelcorp.celest.sofa.time.SofaEpoch.SofaEpochBuilder

/**
 * Sofa representation of Coordinated Universal Time (UTC).
 *
 * @see [[be.angelcorp.celest.time.timeStandard.UTCTime]]
 */
class SofaUTC@Inject()(val tt: TimeStandard, val tai: SofaTAI) extends UTCTime(tai) {

  /** Convert a UTC epoch to TT (note does not use the embedded TimeStandard). */
  def toTT( utc: SofaEpoch ) = {
    val tai = new SofaEpochBuilder( this.tai )(this.tt)
    SofaLibrary.iauUtctai( utc.dj1, utc.dj2, tai.t1, tai.t2 )
    this.tai.toTT( tai.result )
  }

  /** Convert a TT epoch to UTC (note does not use the embedded TimeStandard). */
  def toUTC( tt: SofaEpoch ) = {
    val tai = this.tai.toTAI( tt )
    val utc = new SofaEpochBuilder(this)(this.tt)
    SofaLibrary.iauTaiutc( tai.dj1, tai.dj2, utc.t1, utc.t2 )
    utc.result
  }

  override def offsetToTT(JD_utc: Epoch): Double = {
    val utc = SofaEpoch(JD_utc)(this.tt)
    val tt  = toTT(utc)
    tt relativeToS utc
  }

  override def offsetFromTT(JD_tt: Epoch): Double = {
    val tt  = SofaEpoch(JD_tt)(this.tt)
    val utc = toUTC( tt )
    utc relativeToS tt
  }

}
