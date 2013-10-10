/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
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
package be.angelcorp.libs.celest_examples.examples.quickstart

import be.angelcorp.libs.celest.body.BiPropellant
import be.angelcorp.libs.celest.body.CelestialBody
import be.angelcorp.libs.celest.body.MonoPropellant
import be.angelcorp.libs.celest.state.Orbit

class Satellite(s: Orbit) extends CelestialBody(s, 0) {

	/* Different propellants */
	val hydrazine			    = new MonoPropellant(225,  1550)
  val nitrogenTetroxide	= new MonoPropellant(0,    1200)
  val xenon				      = new MonoPropellant(2000, 400 )

  def getHydrazineLAE = {
		val oxToFuel = 1.3
		new BiPropellant(340, hydrazine, 1 / (1 + oxToFuel), nitrogenTetroxide)
	}

	override def getTotalMass = 6170 - hydrazine.wetMass - nitrogenTetroxide.wetMass - xenon.wetMass

}
