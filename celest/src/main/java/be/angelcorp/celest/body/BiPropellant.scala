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

class BiPropellant(val Isp: Double,
                   val primairy: Propellant,
                   val primairyPercent: Double,
                   val secondairy: Propellant) extends Propellant {

  override def consumeMass(dM: Double) {
    primairy.consumeMass(dM * primairyPercent)
    secondairy.consumeMass(dM * (1 - primairyPercent))
  }

  def wetMass = primairy.wetMass + secondairy.wetMass

  def ΔvMax(body: ICelestialBody): Double = MonoPropellant.ΔvMax(Isp, wetMass, body.getTotalMass)

  def Veff: Double = MonoPropellant.vEff(Isp)

}
