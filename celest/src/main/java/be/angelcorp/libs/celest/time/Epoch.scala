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
package be.angelcorp.libs.celest.time

import java.util.Date
import be.angelcorp.libs.util.physics.Time
import be.angelcorp.libs.celest.time.timeStandard.ITimeStandard

/**
 * A container for a Julian date
 * 
 * @author Simon Billemont
 */
trait Epoch extends Comparable[Epoch] {

	/**
	 * Add time to this epoch.
	 * 
	 * @param dt      Amount of time to add.
	 * @param format  Format of dt.
	 * @return        New [[be.angelcorp.libs.celest.time.Epoch]].
	 */
	def add(dt: Double, format: Time): Epoch

	/**
	 * Get the Julian date in a standard java date object.
	 * 
	 * @return JD in java.util.Date format.
	 */
	def date: Date

	/**
	 * Get the epoch Julian date.
	 * 
	 * @return JD The Julian date of the epoch.
	 */
	def jd: Double

  /**
	 * Get the Julian date in an alternate [[be.angelcorp.libs.celest.time.timeStandard.ITimeStandard]].
	 * 
	 * @return Julian date of the epoch, in an alternative [[be.angelcorp.libs.celest.time.timeStandard.ITimeStandard]].
	 */
	def inTimeStandard(timeStandard: ITimeStandard): Epoch

  /**
   * Get the [[be.angelcorp.libs.celest.time.timeStandard.ITimeStandard]] used within this Julian date.
   *
   * @return ITimeStandard used by this Epoch.
   */
  def timeStandard: ITimeStandard

	/**
	 * Get the amount of Julian days that this date is after the specified epoch:
	 * 
	 * <pre>
	 * this_time - epoch_time
	 * </pre>
	 * 
	 * @param epoch Epoch to which to find the relative time.
	 * @return Amount of julian days from the passed epoch to this epoch.
	 */
	def relativeTo(epoch: Epoch): Double

	/**
	 * Get the amount of Julian days that this date is after the specified epoch:
	 * 
	 * <pre>
	 * this_time - epoch_time
	 * </pre>
	 * 
	 * @param epoch       Epoch to which to find the relative time.
	 * @param timeformat  Format in which to return the time difference.
	 * @return Amount of time from the passed epoch to this epoch.
	 */
	def relativeTo(epoch: Epoch, timeformat: Time): Double

  /**
   * Get the fraction of the current (gregorian) day where 0 is midnight, and 0.5 is midday.
   *
   * @return Fraction in the current day [-]
   */
  def fractionInDay: Double

}