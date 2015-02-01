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

class BiPropellant(val Isp: Double,
                   val primairy: Propellant,
                   val primairyPercent: Double,
                   val secondairy: Propellant) extends Propellant {

  override def consumeMass(dM: Double) = {
    val pri = primairy.consumeMass(dM * primairyPercent)
    val sec = secondairy.consumeMass(dM * (1 - primairyPercent))
    new BiPropellant(Isp, pri, primairyPercent, sec)
  }

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

  def propellantMass = primairy.propellantMass + secondairy.propellantMass

  def ΔvMax(body: CelestialBody): Double = MonoPropellant.ΔvMax(Isp, propellantMass, body.mass)

  def Veff: Double = MonoPropellant.vEff(Isp)

}
