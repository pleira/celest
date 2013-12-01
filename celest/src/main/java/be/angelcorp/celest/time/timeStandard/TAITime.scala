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
class TAITime extends TimeStandard {
  override def offsetFromTT(jd_tt: Epoch) = -32.184

  override def offsetToTT(jd_tai: Epoch) = 32.184
}
