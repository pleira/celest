/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *        http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.libs.celest.time.timeStandard

import be.angelcorp.libs.celest.time.{JulianDate, IJulianDate}
import scala.math._
import java.lang.Math
import scala.Math
import be.angelcorp.libs.util.physics.Time

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
class TAI extends ITimeStandard {
	override def offsetFromTT(jd_tt: IJulianDate) = -32.184
	override def offsetToTT(jd_tai: IJulianDate) = 32.184
}

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
class TT extends ITimeStandard {
	override def offsetFromTT(jd_tt: IJulianDate) = 0
	override def offsetToTT(jd_tt: IJulianDate) = 0
}

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
class TCB( tdb: ITimeStandard, tcb_epoch: IJulianDate) extends ITimeStandard {

  val Lb   = +1.55051976772E-8
  val TDB0 = -6.55E-5

	override def offsetFromTT(jd_tt: IJulianDate) = {
    // See [1] equation 3-50
    val TCB_TDB = Lb * jd_tt.relativeTo(tcb_epoch, Time.second) + TDB0
    val jd_tdb  = jd_tt.add( TCB_TDB, Time.second )
    TCB_TDB + tdb.offsetFromTT( jd_tdb )
  }

	override def offsetToTT(jd_tcb: IJulianDate) = {
    //TODO: Appropriation, need a rootfinder or something similar
    -offsetFromTT(jd_tcb)
	}
}

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
class TDB( j2000_epoch: IJulianDate ) extends ITimeStandard {

  def offsetFromTT(JD_tt: IJulianDate) = {
    val J2000_offset = JD_tt.relativeTo( j2000_epoch )
    val M    = (357.53 + 0.98560028 * J2000_offset) * (Pi/180.0)
    val ΔM_λ = (246.11 + 0.90255617 * J2000_offset) * (Pi/180.0)
    0.001658 * sin(M) + 0.000014 * sin(ΔM_λ)
  }

  def offsetToTT(JD_tdb: IJulianDate) = -offsetFromTT(JD_tdb)

}

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
class TCG( tt_epoch: IJulianDate ) extends ITimeStandard {

  /**
   * LG is a scale constant accounting for the Earth's gravitational and rotational potential affecting
   * the rate of clocks according to the IAU-specified relativistic metric. IAU Resolution B1.9 (2000)
   * recommends LG as a defining constant, so the relationship cannot change. [1]
   */
  private val L_g = 6.969290134E-10

  def offsetFromTT(JD_tt: IJulianDate) =  L_g * JD_tt.relativeTo(  tt_epoch , Time.second)
  def offsetToTT(JD_tcg: IJulianDate)  = -L_g * JD_tcg.relativeTo( tt_epoch , Time.second)

}

