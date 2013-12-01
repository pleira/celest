package be.angelcorp.celest.time.timeStandard

import javax.inject.Inject
import be.angelcorp.celest.time.EpochAnnotations.J2000
import be.angelcorp.celest.time.Epoch
import scala.math._

/**
 * Barycentric Dynamical Time.
 *
 * <p>
 * For most purposes, one may neglect the difference of appoximatly 1.7 msec between Barycentric Dynamical
 * Time (TDB) and Terrestrial Time (TT). (Fränz and Harper, <b>"Heliospheric Coordinate Systems"</b>)
 * </p>
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
class TDBTime @Inject()(@J2000 j2000_epoch: Epoch) extends TimeStandard {

  def offsetFromTT(JD_tt: Epoch) = {
    val J2000_offset = JD_tt.relativeTo(j2000_epoch)
    val M = (357.53 + 0.98560028 * J2000_offset) * (Pi / 180.0)
    val ΔM_λ = (246.11 + 0.90255617 * J2000_offset) * (Pi / 180.0)
    0.001658 * sin(M) + 0.000014 * sin(ΔM_λ)
  }

  def offsetToTT(JD_tdb: Epoch) = -offsetFromTT(JD_tdb)

}
