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
package be.angelcorp.libs.celest.body

class BiPropellant( val isp: Double,
										val primairy: IPropellant,
										val primairyPercent: Double,
										val secondairy: IPropellant) extends IPropellant {

	override def consumeMass(dM: Double) {
		primairy.consumeMass(dM * primairyPercent)
		secondairy.consumeMass(dM * (1 - primairyPercent))
	}

//	def setPropellantMass(propellant: Double) {
//		primairy.propellantMass = propellant * primairyPercent
//		secondairy.propellantMass = propellant * (1 - primairyPercent)
//	}

	def propellantMass() = primairy.propellantMass + secondairy.propellantMass

	def getDvMax(body: ICelestialBody): Double = Propellant.dvMax(isp, propellantMass, body.getTotalMass)

	def getVeff: Double = Propellant.vEff(isp)

}
