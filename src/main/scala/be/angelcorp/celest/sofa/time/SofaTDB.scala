package be.angelcorp.celest.sofa.time

import be.angelcorp.sofa.SofaLibrary
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.time.timeStandard.TimeStandard
import be.angelcorp.celest.sofa.time.SofaEpoch.SofaEpochBuilder

/**
 * Sofa representation of the Barycentric Dynamical Time (TDB).
 *
 * @see [[be.angelcorp.celest.time.timeStandard.TDBTime]]
 */
class SofaTDB(tt: TimeStandard) extends TimeStandard {

  def dtr(tdb_tt: SofaEpoch) = {
    // Note: topocentric terms based on lon=0 & lat=0
    SofaLibrary.iauDtdb(tdb_tt.dj1, tdb_tt.dj2, 0, 0, 6378.1, 0)
  }

  /** Convert a TDB epoch to TT (note does not use the embedded TimeStandard). */
  def toTT( tdb: SofaEpoch ) = {
    val tt = new SofaEpochBuilder(this.tt)(this.tt)
    SofaLibrary.iauTdbtt(tdb.dj1, tdb.dj2, dtr(tdb), tt.t1, tt.t2)
    tt.result
  }

  /** Convert a TT epoch to TDB (note does not use the embedded TimeStandard). */
  def toTDB( tt: SofaEpoch ) = {
    val tdb = new SofaEpochBuilder(this)(this.tt)
    SofaLibrary.iauTttdb(tt.dj1, tt.dj2, dtr(tt), tdb.t1, tdb.t2)
    tdb.result
  }

  override def offsetToTT(JD_this: Epoch): Double = {
    val tdb = SofaEpoch(JD_this)(this.tt)
    val tt  = toTT( tdb )
    tt relativeToS tdb
  }

  override def offsetFromTT(JD_tt: Epoch): Double = {
    val tt  = SofaEpoch(JD_tt)(this.tt)
    val tdb = toTDB( tt )
    tdb relativeToS tt
  }

}
