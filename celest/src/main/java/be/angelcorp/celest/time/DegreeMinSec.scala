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
 * Represents a angle/epoch in the format of aa<sup>o</sup>mm'ss.ss", from a reference direction or epoch.
 *
 * @param degree Integer number of degrees closest to, but just before the epoch
 * @param minute Integer number of minutes closest to, but just before the epoch since the last complete degree
 * @param second Number of seconds to the direction from the preceding whole minute
 *
 * @author Simon Billemont
 */
case class DegreeMinSec(degree: Int, minute: Int, second: Double) {

  /**
   * Get the angle representation as an equivalent amount of radians (complete DMS => rad)
   * @return Radians represented by this amount of degrees, minutes and seconds. [rad]
   */
  def rad = TimeUtils.dms_rad(degree, minute, second)

  /** Represents the HMS epoch as a string in the form of aa°mm'ss.sss" */
  override def toString = "%d°%d'%f\"".format(degree, minute, second)

}

object DegreeMinSec {

  /**
   * Create a DMS angle based on known HMS angle
   *
   * @param hms [[be.angelcorp.libs.celest.time.HourMinSec]] angle.
   */
  def apply(hms: HourMinSec) = {
    val dms = TimeUtils.rad_dms(hms.rad)
    new DegreeMinSec(dms._1, dms._2, dms._3)
  }

}
