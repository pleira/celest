package be.angelcorp.celest.time.timeStandard

import javax.inject.Inject
import be.angelcorp.celest.time.EpochAnnotations.TT_EPOCH
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations.TDB

/**
 * Barycentric Coordinate Time.
 *
 * <p>
 * Conversions based on:<br>
 * [1] D. Vallado et al. ,
 * D.A. Vallado, <b>"Fundamentals of Astrodynamics and Applications"</b>, 2007, ISBN: 978-0-387-71831-6, p199 equation 3-50.
 * </p>
 *
 * @author Simon Billemont
 */
class TCBTime @Inject()(@TDB tdb: TimeStandard, @TT_EPOCH tt_epoch: Epoch) extends TimeStandard {

  val Lb = +1.55051976772E-8
  val TDB0 = -6.55E-5

  override def offsetFromTT(jd_tt: Epoch) = {
    // See [1] equation 3-50
    val TCB_TDB = Lb * jd_tt.relativeToS(tt_epoch) + TDB0
    val jd_tdb = jd_tt.addS(TCB_TDB)
    TCB_TDB + tdb.offsetFromTT(jd_tdb)
  }

  override def offsetToTT(jd_tcb: Epoch) = {
    //TODO: Appropriation, need a rootfinder or something similar
    -offsetFromTT(jd_tcb)
  }
}
