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
package be.angelcorp.celest.maneuvers.targeters.lambert

import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem
import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.state.{PosVel, Keplerian}
import be.angelcorp.celest.trajectory.{Trajectory, KeplerTrajectory}

/**
 * Create a solution to the classical Lambert problem (pure keplerian two-point boundary value problem).
 *
 * @param r1             The origin of the transfer orbit [m]
 * @param r2             The destination of the transfer orbit [m]
 * @param frame          Frame in which the motion takes place
 * @param departureEpoch Epoch of departure
 * @param arrivalEpoch   Epoch of arrival
 * @param f              F function (of the F and G functions, NOT SERIES)
 * @param g              G function (of the F and G functions, NOT SERIES)
 * @param g_dot          G dot function (of the F and G functions, NOT SERIES)
 *
 * @tparam F Frame type in which the motion takes place
 */
class LambertTrajectory[F <: BodyCenteredSystem]
(r1: Vec3, r2: Vec3, frame: F, departureEpoch: Epoch, arrivalEpoch: Epoch,
 f: Double, g: Double, g_dot: Double) extends Trajectory[F] {

  /**
   * This is the trajectory that the satellite flies from r1 to r2
   */
  lazy val traj: Epoch => Keplerian[F] = {
    val k = Keplerian(new PosVel(r1, v1, frame))
    new KeplerTrajectory(departureEpoch, k).apply
  }

  /**
   * Evaluate the unperturbed trajectory.
   */
  def apply(epoch: Epoch) = traj(epoch)

  /**
   * Get the inertial velocity at R1 needed to fly the trajectory to R2
   *
   * @return Inertial velocity at R1 [m/s]
   */
  def v1 = (r2 - (r1 * f)) / g

  /**
   * Get the inertial velocity when arriving R2
   *
   * @return Inertial velocity at R2 [m/s]
   */
  def v2 = ((r2 * g_dot) - r1) / g

}
