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

/**
 * Represents an epoch with a given hour, minute and seconds, from a known event, like the start of a
 * day.
 * 
 * <p>
 * An epoch in the form of hh:mm:ss.sss
 * </p>
 * 
 * @author simon
 * 
 */
public interface IHourMinSec {

	/**
	 * Get the amount of days represented by this set of {@link IHourMinSec}. For example:
	 * <ul>
	 * <li>h=0, m=0, s=0 &rarr; 0</li>
	 * <li>h=1, m=0, s=0 &rarr; 1/24</li>
	 * <li>h=24, m=0, s=0 &rarr; 1</li>
	 * </ul>
	 * 
	 * @return Fraction of a Julian day represented by this {@link IHourMinSec}.
	 */
	public abstract double getDayFraction();

	/**
	 * Get the integer amount of hours preceding the epoch.
	 * 
	 * @return Hours to the epoch
	 */
	public abstract int getHour();

	/**
	 * Get the integer amount of minutes preceding the epoch from the last hour.
	 * 
	 * @return Minutes to the epoch
	 */
	public abstract int getMinute();

	/**
	 * Get the equivalent amount of radians as this HMS. 24h == 360deg == 2&pi; rad
	 * 
	 * @return Radians to the epoch
	 */
	public abstract double getRadian();

	/**
	 * Get the amount of seconds preceding the epoch from the last complete minute.
	 * 
	 * @return Seconds to the epoch
	 */
	public abstract double getSecond();

}