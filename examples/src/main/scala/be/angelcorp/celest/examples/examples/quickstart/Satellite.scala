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
package be.angelcorp.celest.examples.examples.quickstart

import be.angelcorp.celest.body.{Propellant, BiPropellant, Satellite, MonoPropellant}
import be.angelcorp.celest.state.{Keplerian, PosVel, Orbit}
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem

class MySatellite[F <: BodyCenteredSystem]
(e: Epoch, s: Orbit[F],
 val hydrazine: Propellant,
 val nitrogenTetroxide: Propellant,
 val xenon: Propellant) extends Satellite(e, 0, s) {

  /**
   * Get the propellant view for the bipropellant LAE engine.
   */
  def getHydrazineLAE = {
    val oxToFuel = 1.3
    new BiPropellant(340, hydrazine, 1 / (1 + oxToFuel), nitrogenTetroxide)
  }

  /**
   * Perform a kick using the Hydrazine LAE engine. This instantaneously changes the state of the satellite by increasing
   * the satellite velocity norm with the given ΔV quantity.
   *
   * @param ΔV Amount of ΔV to add the the current velocity vector [m/s].
   * @return The new satellite sate.
   */
  def kickLAE(ΔV: Double) = {
    val HLAE = getHydrazineLAE.consumeDV(this, ΔV)
    val currentState = s.toPosVel
    val newState = new PosVel(currentState.position, currentState.velocity + (s.toPosVel.velocity.normalized * ΔV), currentState.frame)
    new MySatellite(e, newState, HLAE.primairy, HLAE.secondairy, xenon)
  }

  /**
   * Propagate the current state forward using kepler propagation (unperturbed) for the given amount of seconds.
   * @param Δt_seconds Number of seconds to advance the current epoch & state.
   * @return The new satellite state.
   */
  def propagateFor(Δt_seconds: Double) = {
    val newEpoch = epoch.addS(Δt_seconds)
    val newState = Keplerian(state).propagateFor(Δt_seconds)
    new MySatellite(newEpoch, newState, hydrazine, nitrogenTetroxide, xenon)
  }

  override val mass = 6170 -
    (1550 - hydrazine.propellantMass) - //  (initial propellant mass - current propellant mass)
    (1200 - nitrogenTetroxide.propellantMass) -
    (400 - xenon.propellantMass)

}

object MySatellite {

  def apply[F <: BodyCenteredSystem](epoch: Epoch, state: Orbit[F]) = {
    /* Initial propellant quantities */
    val hydrazine = new MonoPropellant(225, 1550)
    val nitrogenTetroxide = new MonoPropellant(0, 1200)
    val xenon = new MonoPropellant(2000, 400)

    new MySatellite(epoch, state, hydrazine, nitrogenTetroxide, xenon)
  }

}
