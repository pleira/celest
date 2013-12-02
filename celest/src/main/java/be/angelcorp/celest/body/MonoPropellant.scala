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

import scala.math._

class MonoPropellant(val Isp: Double = 0.0, val propellantMass: Double = 0.0) extends Propellant {

  /**
   * Compute the amount of propellant used in ideal conditions (no gravity losses, Tsiolkovsky)
   * <p>
   * For non ideal maneuvers,correct the dV term with a &Delta;dV, eg see:<br />
   * J.Weiss, B. Metzger, M. Gallmeister, Orbit maneuvers with finite thrust, MBB/ERNO, ESA CR(P)-1910,
   * 3 volumes, 1983
   * </p>
   *
   * @param body Body that consumes DV.
   * @param dV   ΔV achieve using this propellant.
   */
  def consumeDV(body: CelestialBody, dV: Double) = {
    val m: Double = body.mass
    val dM: Double = m - exp(-dV / Veff) * m
    consumeMass(dM)
  }

  /**
   * Reduce the propellant mass of the propellant with the specified quantity.
   * @param dM Amount of consumed propellant [kg]
   */
  def consumeMass(dM: Double) = {
    new MonoPropellant(Isp, dM)
  }

  /**
   * Compute the maximum dV that can be given with this engine (for an impulse maneuver, Tsiolkovsky).
   *
   * @param body Body that contains the engine.
   * @return Maximum dV that can be given with the given amount of propellant.
   */
  def ΔvMax(body: CelestialBody): Double = MonoPropellant.ΔvMax(Isp, propellantMass, body.mass)

  /**
   * Get the effective exhaust velocity
   * <p>Veff = Isp * G0</p>
   *
   * <p>
   * <b>Unit: [m/s]</b>
   * </p>
   *
   * @return The effecitive exhaust velocuty
   */
  def Veff: Double = MonoPropellant.vEff(Isp)

}

object MonoPropellant {
  private final val G0: Double = 9.81

  def vEff(isp: Double) = isp * G0

  def ΔvMax(isp: Double, propellantMass: Double, bodyMass: Double): Double =
    vEff(isp) * log(bodyMass / (bodyMass - propellantMass))
}
