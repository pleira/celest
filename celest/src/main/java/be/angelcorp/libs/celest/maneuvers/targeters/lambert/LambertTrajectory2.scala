/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *        http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.libs.celest.maneuvers.targeters.lambert

import be.angelcorp.libs.celest.trajectory.KeplerTrajectory
import be.angelcorp.libs.celest.time.IJulianDate
import be.angelcorp.libs.celest.body.ICelestialBody
import be.angelcorp.libs.celest.state.{Keplerian, PosVel}

/**
 * The solution trajectory from a basic lambert problem (TPBVP). It consists of the full state vector at the
 * departure point and arrival point. The trajectory between the two points is a basic unperturbed two-body
 * Keplerian motion.
 *
 * @param origin State vector at the origin point.
 * @param destination State vector at the destination.
 * @param departure Departure time from the origin.
 * @param arrival Arrival time at the destination.
 * @param center Center body around which the trajectory takes place.
 */
class LambertTrajectory2(val origin: PosVel,
												 val destination: PosVel,
												 val departure: IJulianDate,
												 val arrival: IJulianDate,
												 val center: ICelestialBody) extends KeplerTrajectory( Keplerian(origin), departure );
