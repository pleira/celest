package be.angelcorp.celest.time.timeStandard

import javax.inject.Inject
import be.angelcorp.celest.time.EpochAnnotations.TT_EPOCH
import be.angelcorp.celest.time.Epoch

/**
 * Geocentric Coordinate Time.
 *
 * <p>
 * Conversions based on:<br>
 * [1] D. Vallado et al. ,
 * <b>"Implementation Issues Surrounding the New IAU Reference Systems for Astrodynamics"</b>, 16th
 * AAS/AIAA Space Flight Mechanics Conference, Florida, January 2006
 * </p>
 *
 * @author Simon Billemont
 */
class TCGTime @Inject()(@TT_EPOCH tt_epoch: Epoch) extends TimeStandard {

  /**
   * LG is a scale constant accounting for the Earth's gravitational and rotational potential affecting
   * the rate of clocks according to the IAU-specified relativistic metric. IAU Resolution B1.9 (2000)
   * recommends LG as a defining constant, so the relationship cannot change. [1]
   */
  private val L_g = 6.969290134E-10

  def offsetFromTT(JD_tt: Epoch) = L_g * JD_tt.relativeToS(tt_epoch)

  def offsetToTT(JD_tcg: Epoch) = -L_g * JD_tcg.relativeToS(tt_epoch)

}
