package be.angelcorp.celest.sofa.time

import be.angelcorp.sofa.SofaLibrary
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.time.timeStandard.TimeStandard
import be.angelcorp.celest.sofa.time.SofaEpoch.SofaEpochBuilder

/**
  * Sofa representation of the Geocentric Coordinate Time (TCG).
  *
  * @see [[be.angelcorp.celest.time.timeStandard.TCGTime]]
  */
class SofaTCG(tt: TimeStandard) extends TimeStandard {

   /** Convert a TCG epoch to TT (note does not use the embedded TimeStandard). */
   def toTT( tcg: SofaEpoch ) = {
     val tt = new SofaEpochBuilder(this.tt)(this.tt)
     SofaLibrary.iauTcgtt(tcg.dj1, tcg.dj2, tt.t1, tt.t2)
     tt.result
   }

   /** Convert a TT epoch to TCG (note does not use the embedded TimeStandard). */
   def toTCG( tt: SofaEpoch ) = {
     val tcg = new SofaEpochBuilder(this)(this.tt)
     SofaLibrary.iauTttcg(tt.dj1, tt.dj2, tcg.t1, tcg.t2)
     tcg.result
   }

   override def offsetToTT(JD_this: Epoch): Double = {
     val tcg = SofaEpoch(JD_this)(this.tt)
     val tt  = toTT( tcg)
     tt relativeToS tcg
   }

   override def offsetFromTT(JD_tt: Epoch): Double = {
     val tt  = SofaEpoch(JD_tt)(this.tt)
     val tcg = toTCG( tt )
     tcg relativeToS tt
   }

 }
