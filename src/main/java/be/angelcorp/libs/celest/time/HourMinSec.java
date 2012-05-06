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

import be.angelcorp.libs.math.linear.Vector3D;

/**
 * 
 * Represents an epoch in the format of hh:mm:ss.ss, more information in {@link IHourMinSec}
 * 
 * @author Simon Billemont
 * @see IHourMinSec
 */
@Immutable
public class HourMinSec implements IHourMinSec {

	/**
	 * Integer number of hours closest to, but just before the epoch [hour]
	 */
	private final int		hour;
	/**
	 * Integer number of minutes closest to, but just before the epoch since the last complete hour
	 * [minute]
	 */
	private final int		minute;
	/**
	 * Number of seconds to the epoch from the preceding whole minute
	 */
	private final double	second;

	/**
	 * Create a HMS time based on known DMS time
	 * 
	 * @param dms
	 *            Known {@link IDegreeMinSec} time
	 */
	public HourMinSec(IDegreeMinSec dms) {
		// TODO: make it the seconds more accurate then an integer
		double rad = dms.getRadian();
		int[] arr = TimeUtils.rad_hms(rad);
		this.hour = arr[0];
		this.minute = arr[1];
		this.second = arr[2];
	}

	/**
	 * Create a hms time based on a given integer hour and minute and float second
	 * 
	 * @param hour
	 *            Hour of the epoch
	 * @param minute
	 *            Minute of the epoch
	 * @param second
	 *            Second of the epoch
	 */
	public HourMinSec(int hour, int minute, double second) {
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}

	@Override
	public double getDayFraction() {
		return (((second / 60. + minute) / 60.) + hour) / 24.;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHour() {
		return hour;
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
		return TimeUtils.hms_rad(hour, minute, second);
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
		return new Vector3D(hour, minute, second);
	}

	/**
	 * Represents the HMS epoch as a string in the form of 'hh:mm:ss.sss'
	 */
	@Override
	public String toString() {
		return String.format("%d:%d:%f", hour, minute, second);
	}

}
