/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
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

package be.angelcorp.celest.data.eop

import be.angelcorp.celest.time.Epoch

/**
 * Container for the UT1 offset from UTC (UT1-UTC).
 * <p>
 * You can get the correct values from the IERS data products (Earth Orientation Data) at [[http://www.iers.org/‎]].
 * </p>
 */
trait UT1Provider {

  /**
   * Get the UT1 - UTC offset in seconds at the specific UTC epoch.
   *
   * @param jd_utc UTC epoch on which to get the UT1 - UTC difference
   * @return Seconds between the UTC and UT11 timestandard (UT1 - UTC).
   */
  def UT1_UTC(jd_utc: Epoch): Double

}
