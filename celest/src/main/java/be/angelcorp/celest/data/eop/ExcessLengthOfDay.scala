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
 * Container for the excess of length of day (LOD).
 * <p>
 * The excess of length of day is the difference between the astronomically determined duration of the day and
 * (86400 SI seconds). You can get the correct values from the IERS data products (Earth Orientation Data)
 * at [[http://www.iers.org/‎]].
 * </p>
 */
trait ExcessLengthOfDay {

  /**
   * Excess of length of day (LOD) is the difference between the astronomically determined duration of the day and (86400 SI seconds).
   *
   * @param epoch Epoch day from which to retrieve the lod.
   * @return Excess length of day on the considered epoch [s].
   */
  def lod(epoch: Epoch): Double

}
