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
package be.angelcorp.libs.celest.time;

import javax.annotation.concurrent.Immutable;

import be.angelcorp.libs.math.linear.ImmutableVector3D;
import be.angelcorp.libs.math.linear.Vector3D;

/**
 * 
 * Represents a direction in the format of aa<sup>o</sup>mm'ss.ss", more information in
 * {@link IDegreeMinSec}
 * 
 * @author Simon Billemont
 * @see IDegreeMinSec
 */
@Immutable
public class DegreeMinSec implements IDegreeMinSec {

	/** Integer number of degrees closest to, but just before the epoch */
	private final int		degree;
	/** Integer number of minutes closest to, but just before the epoch since the last complete degree */
	private final int		minute;
	/** Number of seconds to the direction from the preceding whole minute */
	private final double	second;

	/**
	 * Create a DMS angle based on known HMS angle
	 * 
	 * @param hms
	 *            {@link IHourMinSec} angle
	 */
	public DegreeMinSec(IHourMinSec hms) {
		double rad = hms.getRadian();
		// TODO:change the array output so we have double seconds
		int[] arr = TimeUtils.rad_dms(rad);
		degree = arr[0];
		minute = arr[1];
		second = arr[2];
	}

	/**
	 * Create a DMS angle based on a given integer degrees and minutes and float seconds
	 * 
	 * @param deg
	 *            Degrees to the direction
	 * @param minute
	 *            Minute to the direction
	 * @param second
	 *            Second to the direction
	 */
	public DegreeMinSec(int deg, int minute, double second) {
		this.degree = deg;
		this.minute = minute;
		this.second = second;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDegree() {
		return degree;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMinute() {
		return minute;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getRadian() {
		return TimeUtils.dms_rad(degree, minute, second);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getSecond() {
		return second;
	}

	/**
	 * Get the time as a vector
	 * 
	 * @return A vector containing <hour, min, sec>
	 */
	public Vector3D getTime() {
		return new ImmutableVector3D(degree, minute, second);
	}

	/**
	 * Represents the HMS epoch as a string in the form of aa<sup>o</sup>mm'ss.sss"
	 */
	@Override
	public String toString() {
		return String.format("%ddeg%d'%f\"", degree, minute, second);
	}

}
