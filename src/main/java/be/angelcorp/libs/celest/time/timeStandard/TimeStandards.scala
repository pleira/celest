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

class TAI extends ITimeStandard {
	override def offsetFromTT(jd_tt: IJulianDate) = -32.184
	override def offsetToTT(jd_tai: IJulianDate) = 32.184
}

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
 * <b>"Implementation Issues Surrounding the New IAU Reference Systems for Astrodynamics"</b>, 16th
 * AAS/AIAA Space Flight Mechanics Conference, Florida, January 2006
 * </p>
 *
 * @author Simon Billemont
 */
class TCB( j2000_epoch: IJulianDate) extends ITimeStandard {
	val deg2rad = Pi / 180.0
	override def offsetFromTT(jd_tt: IJulianDate) = {
		// See [1] equation 32
		val time_from_j2000_days = jd_tt.relativeTo( j2000_epoch )
		def Mλ = (246.11 + 0.90255617 * time_from_j2000_days) * deg2rad
		def M  = (357.53 + 0.98560028 * time_from_j2000_days) * deg2rad
		0.001658 * sin(M) + 0.000021 * sin(Mλ)
	}
	override def offsetToTT(jd_tcb: IJulianDate) = {
		//TODO: Use rootfinder or analytical expression?
		- offsetFromTT(jd_tcb)
	}
}

/**
 * Barycentric Dynamical Time.
 *
 * <p>
 * For most purposes, one may neglect the difference of less than 2 msec between Barycentric Dynamical
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
    val g     = 357.53 + 0.98560028 * JD_tt.relativeTo( j2000_epoch )
    val g_rad = g * Pi / 180.0
    0.001658 * sin(g_rad) + 0.000014 * sin(2 * g_rad)
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


