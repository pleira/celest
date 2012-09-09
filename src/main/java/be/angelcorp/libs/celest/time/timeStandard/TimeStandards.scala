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

object TimeStandards {

	/**
   * International Atomic Time
	 */
	lazy val TAI = new TAI()

	/**
	 * Barycentric Coordinate Time
	 */
	lazy val TCB = new TCB()

	/**
	 * Geocentric Coordinate Time
	 */
	lazy val TCG = new TCG()

	/**
	 * Barycentric Dynamical Time
	 */
	lazy val TDB = new TDB()

	/**
	 * Terrestrial Dynamical Time (is now TT)
	 */
	lazy val TDT = TT

	/**
	 * Terrestrial Time
	 */
	lazy val TT = new TT()

	/**
	 * Universal Time UT1
	 */
	//lazy val UT1 = new UT1();

	/**
	 * Coordinated Universal Time
	 */
	lazy val UTC = new UTC()

}

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
class TCB extends ITimeStandard {
	val deg2rad = Pi / 180.0
	override def offsetFromTT(jd_tt: IJulianDate) = {
		// See [1] equation 32
		val time_from_j2000_days = jd_tt.relativeTo( JulianDate.J2000_EPOCH )
		def Mλ = (246.11 + 0.90255617 * time_from_j2000_days) * deg2rad
		def M  = (357.53 + 0.98560028 * time_from_j2000_days) * deg2rad
		0.001658 * sin(M) + 0.000021 * sin(Mλ)
	}
	override def offsetToTT(jd_tcb: IJulianDate) = {
		//TODO: Use rootfinder or analytical expression?
		- offsetFromTT(jd_tcb)
	}
}
