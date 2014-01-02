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
package be.angelcorp.celest.maneuvers.targeters

import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem
import be.angelcorp.celest.state.PosVel
import be.angelcorp.celest.trajectory.Trajectory

/**
 * Base class for the Lambert problem, meaning finding the transfer arc for a two-point boundary value
 * problem (TPBVP). Here the start and end position are known, as well as the time that the satellite is
 * to travel between the two points.
 *
 * @author Simon Billemont
 */
trait TPBVP[F <: BodyCenteredSystem] {

  /**
   * Epoch at which the satellite or body departs at r1
   * <p>
   * <b>Unit: [jd]</b>
   * </p>
   */
  def departureEpoch: Epoch

  /**
   * Epoch at which the satellite or body arrives at r2
   * <p>
   * <b>Unit: [jd]</b>
   * </p>
   */
  def arrivalEpoch: Epoch

  /**
   * Start position
   * <p>
   * <b>If converted to Cartesian coordinates; unit: [m]</b>
   * </p>
   */
  def r1: PosVel[F]

  /**
   * Targeted end position
   * <p>
   * <b>If converted to Cartesian coordinates; unit: [m]</b>
   * </p>
   */
  def r2: PosVel[F]

  /**
   * Frame in which the motion takes place.
   */
  def frame: F

  /**
   * Travel time in seconds between departure and arrival
   *
   * @return Travel time [s]
   */
  def travelTime = arrivalEpoch.relativeToS(departureEpoch)

  /**
   * Find the optimal trajectory according to this targeted between r1->r2 for the given travel time.
   *
   * @return An optimal trajectory
   */
  def trajectory: Trajectory[F]

}
