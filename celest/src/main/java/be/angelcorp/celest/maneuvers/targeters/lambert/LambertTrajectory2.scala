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

import be.angelcorp.celest.trajectory.KeplerTrajectory
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.state.{Keplerian, PosVel}
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem

/**
 * The solution trajectory from a basic lambert problem (TPBVP). It consists of the full state vector at the
 * departure point and arrival point. The trajectory between the two points is a basic unperturbed two-body
 * Keplerian motion.
 *
 * @param origin State vector at the origin point.
 * @param destination State vector at the destination.
 * @param departure Departure time from the origin.
 * @param arrival Arrival time at the destination.
 * @param frame Frame in which the trajectory takes place.
 */
class LambertTrajectory2[F <: BodyCenteredSystem]
(val origin: PosVel[F],
 val destination: PosVel[F],
 val departure: Epoch,
 val arrival: Epoch,
 val frame: F) extends KeplerTrajectory(departure, Keplerian(origin))
