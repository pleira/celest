/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.celest.time

import java.util.Date
import be.angelcorp.celest.physics.Units._
import be.angelcorp.celest.time.timeStandard.TimeStandard

/**
 * A container for a Julian date
 *
 * @author Simon Billemont
 */
trait Epoch extends Comparable[Epoch] {

  /**
   * Add time [julian day] to this epoch.
   *
   * @param dt      Julian days to add.
   * @return        New [[be.angelcorp.celest.time.Epoch]].
   */
  def add(dt: Double): Epoch

  /**
   * Add time [second] to this epoch.
   *
   * @param dt      Seconds to add.
   * @return        New [[be.angelcorp.celest.time.Epoch]].
   */
  def addS(dt: Double): Epoch = add(dt / julianDay)

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
   * Get the Julian date in an alternate [[be.angelcorp.celest.time.timeStandard.TimeStandard]].
   *
   * @return Julian date of the epoch, in an alternative [[be.angelcorp.celest.time.timeStandard.TimeStandard]].
   */
  def inTimeStandard(timeStandard: TimeStandard): Epoch

  /**
   * Get the [[be.angelcorp.celest.time.timeStandard.TimeStandard]] used within this Julian date.
   *
   * @return ITimeStandard used by this Epoch.
   */
  def timeStandard: TimeStandard

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
   * Get the amount of SI seconds that this date is after the specified epoch:
   *
   * <pre>
   * this_time - epoch_time
   * </pre>
   *
   * @param epoch Epoch to which to find the relative time.
   * @return Amount of seconds from the passed epoch to this epoch.
   */
  def relativeToS(epoch: Epoch): Double = relativeTo(epoch) * julianDay

  /**
   * Get the fraction of the current (gregorian) day where 0 is midnight, and 0.5 is midday.
   *
   * @return Fraction in the current day [-]
   */
  def fractionInDay: Double = (jd - 0.5) % 1.0

  /**
   * Move the epoch forward by the specified amount of days.
   *
   * @param days Amount of Julian days to add to this epoch.
   * @return A new epoch, shifted by the specified number of days.
   */
  def +(days: Double): Epoch = add(days)

  /**
   * Move the epoch backwards by the specified amount of days.
   *
   * @param days Amount of Julian days to subtract from this epoch.
   * @return A new epoch, shifted by the specified number of days.
   */
  def -(days: Double): Epoch = add(-days)

  /**
   * Check if a specified epoch is equal to this epoch.
   * This means the Julian date of this instance should be equal to the value compared against.
   *
   * @param epoch Epoch to compare to.
   * @return True if this epoch is equal to the parameter epoch.
   */
  def ==(epoch: Epoch) = this.relativeTo(epoch) == 0

  /**
   * Check if a specified epoch is after this epoch.
   * This means the Julian date of this instance should be smaller then the value compared against.
   *
   * @param epoch Epoch to compare to.
   * @return True if this epoch is before the parameter epoch.
   */
  def <(epoch: Epoch) = this.relativeTo(epoch) < 0

  /**
   * Check if a specified epoch is equal to or after this epoch.
   * This means the Julian date of this instance should be equal or smaller then the value compared against.
   *
   * @param epoch Epoch to compare to.
   * @return True if this epoch is equal to or before the parameter epoch.
   */
  def <=(epoch: Epoch) = this.relativeTo(epoch) <= 0

  /**
   * Check if a specified epoch is before this epoch.
   * This means the Julian date of this instance should be larger then the value compared against.
   *
   * @param epoch Epoch to compare to.
   * @return True if this epoch is after the parameter epoch.
   */
  def >(epoch: Epoch) = this.relativeTo(epoch) > 0

  /**
   * Check if a specified epoch is equal to or before this epoch.
   * This means the Julian date of this instance should be equal to or larger then the value compared against.
   *
   * @param epoch Epoch to compare to.
   * @return True if this epoch is equal to or after the parameter epoch.
   */
  def >=(epoch: Epoch) = this.relativeTo(epoch) >= 0

  /**
   * Create a `TimeRange` from this instance up to but not including the end Epoch.
   *
   * @param end The final bound of the range to make.
   * @return A [[be.angelcorp.celest.time.TimeRange]] from `this` up to but not including `end`.
   */
  def until(end: Epoch): TimeRange = TimeRange(this, end)

  /**
   * Create a `TimeRange` from this instance up to but not including the end Epoch with a predefined step in Julian days.
   *
   * @param end The final bound of the range to make.
   * @param step The number to increase by for each step of the range.
   * @return A [[be.angelcorp.celest.time.TimeRange]] from `this` up to but not including `end`.
   */
  def until(end: Epoch, step: Double): TimeRange = TimeRange(this, end, step)

  /**
   * Create a `TimeRange` from this instance up to and including the end Epoch with a predefined step in Julian days.
   *
   * @param end The final bound of the range to make.
   * @return A [[be.angelcorp.celest.time.TimeRange]] from `'''this'''` up to and including `end`.
   */
  def to(end: Epoch): TimeRange.Inclusive = TimeRange.inclusive(this, end)

  /**
   * Create a `TimeRange` from this instance up to and including the end Epoch with a predefined step in Julian days.
   *
   * @param end The final bound of the range to make.
   * @param step The number to increase by for each step of the range.
   * @return A [[be.angelcorp.celest.time.TimeRange]] from `'''this'''` up to and including `end`.
   */
  def to(end: Epoch, step: Double): TimeRange.Inclusive = TimeRange.inclusive(this, end, step)

}