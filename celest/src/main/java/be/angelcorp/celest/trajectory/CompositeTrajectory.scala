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
import be.angelcorp.celest.frameGraph.ReferenceSystem

/**
 * This is a trajectory that is split in discrete trajectory segments, effectively patches several trajectories together.
 *
 * Trajectories are added with a starting time. When this trajectory is evaluated, the evaluation is
 * delegated to the sub-trajectory which starts closest to, but before the requested epoch.
 *
 * @param trajectories Sorted map that stores trajectories with there corresponding starting time.
 *
 * @author Simon Billemont
 */
class CompositeTrajectory[F <: ReferenceSystem](val trajectories: java.util.TreeMap[Epoch, Trajectory[F]]) {

  /**
   * Construct an empty CompositeTrajectory
   */
  def this() = this(new java.util.TreeMap[Epoch, Trajectory[F]]())

  def this(frame: F) = this(new java.util.TreeMap[Epoch, Trajectory[F]]())

  def apply(epoch: Epoch) = {
    val entry = trajectories.floorEntry(epoch)
    if (entry == null)
      throw new ArithmeticException("No trajectory found for julian date " + epoch)
    else
      entry.getValue()(epoch)
  }

}
