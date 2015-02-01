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
package be.angelcorp.celest.body

import be.angelcorp.celest.constants.{Constants, SolarConstants}
import be.angelcorp.celest.state.{Orbit, PosVel}
import be.angelcorp.celest.time.{Epochs, Epoch}
import be.angelcorp.celest.universe.Universe
import be.angelcorp.celest.frameGraph.ReferenceSystem

/**
 * Representation of a small celestial body.
 *
 * For bodies where the mass is large, use [[be.angelcorp.celest.body.Body]] to retain more precision.
 *
 * @param epoch Epoch on which the mass and the state is defined.
 * @param mass  Body mass [kg].
 * @param state The satellite orbital elements on the given epoch.
 *
 * @author Simon Billemont
 */
class Satellite[F <: ReferenceSystem](val epoch: Epoch, val mass: Double, val state: Orbit[F]) extends CelestialBody {

  /**
   * Construct a celestial body, based only on its mass.
   *
   * The epoch is fixed at J2000.0 and the state is stationary (position/velocity are zero).
   *
   * @param mass  Body mass [kg].
   */
  def this(mass: Double, frame: F)(implicit universe: Universe) =
    this(Epochs.J2000, mass, PosVel(frame))

  /**
   * Generic stationary body ( position and velocity zero) and with mass of our sun (one solar mass), at the J2000 epoch.
   */
  def this(frame: F)(implicit universe: Universe) = this(SolarConstants.mass, frame)

  override def μ: Double = Constants.mass2mu(mass)

}
