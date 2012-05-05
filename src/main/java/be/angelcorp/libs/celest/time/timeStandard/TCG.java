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
package be.angelcorp.libs.celest.time.timeStandard;

import javax.annotation.concurrent.Immutable;

import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.celest.time.JulianDate;
import be.angelcorp.libs.util.physics.Time;

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
 * 
 */
@Immutable
public class TCG implements ITimeStandard {

	/**
	 * LG is a scale constant accounting for the Earth’s gravitational and rotational potential affecting
	 * the rate of clocks according to the IAU-specified relativistic metric. IAU Resolution B1.9 (2000)
	 * recommends LG as a defining constant, so the relationship cannot change. [1]
	 */
	private static final double	L_g			= 6.969290134E-10;

	/** TCG singleton instance */
	private static TCG			instance	= new TCG();

	/** Get the TCG singleton instance */
	public static TCG get() {
		return instance;
	}

	/** {@inheritDoc} */
	@Override
	public double offsetFromTAI(IJulianDate JD_tai) {
		// Equation (28)
		double seconds_offset_tai_tt = TimeStandards.TT.offsetFromTAI(JD_tai);
		double seconds_since_TAI_epoch = JD_tai.relativeTo(JulianDate.TAI_EPOCH, Time.second);
		double TCG = L_g * seconds_since_TAI_epoch + seconds_offset_tai_tt;

		return TCG;
	}

	/** {@inheritDoc} */
	@Override
	public double offsetToTAI(IJulianDate JD_tcg) {
		// Epoch doesn't matter, its constant
		double seconds_offset_tt_tai = TimeStandards.TT.offsetToTAI(JulianDate.TAI_EPOCH);
		double seconds_since_TCG_epoch = JD_tcg.relativeTo(JulianDate.TCG_EPOCH, Time.second);
		double offset_TT = -L_g * seconds_since_TCG_epoch;
		double offset_TAI = offset_TT + seconds_offset_tt_tai;

		return offset_TAI;
	}

}
