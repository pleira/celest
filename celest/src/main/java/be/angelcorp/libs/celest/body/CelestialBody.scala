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

import be.angelcorp.libs.celest.constants.{Constants, SolarConstants}
import be.angelcorp.libs.celest.potential.IGravitationalPotential
import be.angelcorp.libs.celest.potential.PointMassPotential
import be.angelcorp.libs.celest.state.positionState.CartesianElements
import be.angelcorp.libs.celest.state.positionState.IPositionState

/**
 * Representation of a celestial body (eg planet/sun/satellite). It is a wrapper for a state vector and
 * the mass of the body
 *
 * @param state Body state vector.
 * @param mass  Body mass [kg].
 * @param gravityPotentialFactory Function that creates the gravitational potential of this object.
 *
 * @author simon
 */
class CelestialBody(val state: 	IPositionState,
										val mass: 	Double,
										gravityPotentialFactory: CelestialBody => IGravitationalPotential) extends ICelestialBody {

	/** The gravitational potential of this object. */
	lazy val gravitationalPotential = gravityPotentialFactory(this)

	/**
	 * Generic body with coordinates with R: <0, 0, 0> and V: <0, 0, 0> and with mass of our sun (one
	 * solar mass).
	 */
	def this() = this(new CartesianElements, SolarConstants.mass, new PointMassPotential(_))

	/**
	 * Create a generic body with given statevector (location/speed) and mass
	 *
	 * @param state State of the body.
	 * @param mass Mass of the body [kg].
	 */
	def this(state: IPositionState, mass: Double) = this(state, mass, new PointMassPotential(_))

	override def getGravitationalPotential: IGravitationalPotential = gravitationalPotential
	override def getMu: Double = Constants.mass2mu(mass)
	override def getState: IPositionState = state
	override def getTotalMass: Double = mass

}