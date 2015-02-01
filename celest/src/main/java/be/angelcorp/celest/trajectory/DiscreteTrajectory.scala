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
package be.angelcorp.celest.trajectory

import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.state.Orbit
import be.angelcorp.celest.frameGraph.ReferenceSystem

/**
 * Create a trajectory based on a set of known states at known epochs. Then upon evaluation, the state
 * closest to, but before is returned as the state at the requested epoch.
 *
 * This trajectory does <b>NOT</b> perform interpolation between the states.
 *
 * @param states Container for all the known states at there respective epoch.
 *
 * @author Simon Billemont
 */
class DiscreteTrajectory[F <: ReferenceSystem](val states: java.util.TreeMap[Epoch, Orbit[F]]) {

  /**
   * Construct an empty DiscreteTrajectory
   */
  def this() = this(new java.util.TreeMap[Epoch, Orbit[F]]())

  def apply(epoch: Epoch) = {
    val entry = states.floorEntry(epoch)
    if (entry == null)
      throw new ArithmeticException("No state found before the the julian date " + epoch)
    else
      entry.getValue
  }

}
