package be.angelcorp.celest.time.timeStandard

import be.angelcorp.celest.time.Epoch

/**
 * <p>
 * Conversions based on:<br>
 * [1] D. Vallado et al. ,
 * <b>"Implementation Issues Surrounding the New IAU Reference Systems for Astrodynamics"</b>, 16th
 * AAS/AIAA Space Flight Mechanics Conference, Florida, January 2006
 * </p>
 *
 * @author Simon Billemont
 */
class TTTime extends TimeStandard {
  override def offsetFromTT(jd_tt: Epoch) = 0

  override def offsetToTT(jd_tt: Epoch) = 0
}
