package be.angelcorp.celest.time.timeStandard

import javax.inject.Inject
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations.TAI

/**
 * GPS Time.
 *
 * <p>
 * Conversions based on:<br>
 * http://stjarnhimlen.se/comp/time.html
 * </p>
 *
 * @author Simon Billemont
 */
class GPSTime @Inject()(@TAI tai: TimeStandard) extends TimeStandard {

  override def offsetFromTT(jd_tt: Epoch) = tai.offsetFromTT(jd_tt) - 19

  override def offsetToTT(jd_gps: Epoch) = -offsetFromTT(jd_gps)

}

