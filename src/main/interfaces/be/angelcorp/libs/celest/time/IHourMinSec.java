/**
 * Copyright (C) 2011 simon <aodtorusan@gmail.com>
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

	/**
	 * Set the integer amount of hours preceding the epoch.
	 * 
	 * @param hour
	 *            Hours to the epoch
	 */
	public abstract void setHour(int hour);

	/**
	 * Set the integer amount of minutes preceding the epoch from the last hour.
	 * 
	 * @param minute
	 *            Minutes to the epoch
	 */
	public abstract void setMinute(int minute);

	/**
	 * Set the equivalent amount of radians as this HMS. 24h == 360deg == 2&pi; rad
	 * 
	 * @param rad
	 *            Radians to the epoch
	 */
	public abstract void setRadian(double rad);

	/**
	 * Set the amount of seconds preceding the epoch from the last complete minute.
	 * 
	 * @param second
	 *            Seconds to the epoch
	 */
	public abstract void setSecond(double second);

}