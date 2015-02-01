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

/**
 * Represents an epoch with a given hour, minute and seconds, from a known event, like the start of a day.
 *
 * @param hour Integer number of hours closest to, but just before the epoch [hour]
 * @param minute Integer number of minutes closest to, but just before the epoch since the last complete hour [minute]
 * @param second Number of seconds to the epoch from the preceding whole minute [sec]
 *
 * @author Simon Billemont
 */
case class HourMinSec(hour: Int, minute: Int, second: Double) {

  /**
   * Get the amount of days represented by this set of object. For example:
   * <ul>
   * <li>h=0, m=0, s=0 &rarr; 0</li>
   * <li>h=1, m=0, s=0 &rarr; 1/24</li>
   * <li>h=24, m=0, s=0 &rarr; 1</li>
   * </ul>
   *
   * @return Fraction of a Julian day represented by this object.
   */
  def dayFraction = (((second / 60.0 + minute) / 60.0) + hour) / 24.0

  /**
   * Get the equivalent amount of radians as this HMS. 24h == 360deg == 2&pi; rad
   *
   * @return Radians to the epoch [rad].
   */
  def rad = TimeUtils.hms_rad(hour, minute, second)

  /** Represents the HMS epoch as a string in the form of 'hh:mm:ss.sss' */
  override def toString = "%d:%d:%f".format(hour, minute, second)

}

object HourMinSec {

  /**
   * Create a HMS time based on known DMS time.
   *
   * @param dms Degrees, minutes and sections time
   */
  def apply(dms: DegreeMinSec) = {
    val arr = TimeUtils.rad_hms(dms.rad)
    new HourMinSec(arr._1, arr._2, arr._3)
  }

}
