package be.angelcorp.celest.sofa.time

import be.angelcorp.celest.time.timeStandard.TimeStandard
import be.angelcorp.sofa.SofaLibrary
import be.angelcorp.celest.sofa.time.SofaEpoch.SofaEpochBuilder
import be.angelcorp.celest.time.Epoch

/**
 * Sofa representation of the Barycentric Coordinate Time (TCB).
 *
 * @see [[be.angelcorp.celest.time.timeStandard.TCBTime]]
 */
class SofaTCB(tt: TimeStandard, tdb: SofaTDB) extends TimeStandard {

  /** Convert a TCB epoch to TT (note does not use the embedded TimeStandard). */
  def toTT( tcb: SofaEpoch ) = {
    val tdb = new SofaEpochBuilder(this.tdb)(this.tt)
    SofaLibrary.iauTcbtdb(tcb.dj1, tcb.dj2, tdb.t1, tdb.t2)
    this.tdb.toTT( tdb.result )
  }

  /** Convert a TT epoch to TCB (note does not use the embedded TimeStandard). */
  def toTCB( tt: SofaEpoch ) = {
    val tdb  = this.tdb.toTDB( tt )
    val tcb = new SofaEpochBuilder(this)(this.tt)
    SofaLibrary.iauTdbtcb(tdb.dj1, tdb.dj2, tcb.t1, tcb.t2)
    tcb.result
  }

  override def offsetToTT(JD_this: Epoch): Double = {
    val tcb = SofaEpoch(JD_this)(this.tt)
    val tt  = toTT( tcb )
    tt relativeToS tcb
  }

  override def offsetFromTT(JD_tt: Epoch): Double = {
    val tt  = SofaEpoch(JD_tt)(this.tt)
    val tcb = toTCB( tt )
    tcb relativeToS tt
  }


}
