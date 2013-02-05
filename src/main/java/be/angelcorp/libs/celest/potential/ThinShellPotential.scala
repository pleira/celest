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
package be.angelcorp.libs.celest.potential

import be.angelcorp.libs.celest.body.CelestialBody
import be.angelcorp.libs.math.linear.Vector3D

/**
 * Implementation of a [[be.angelcorp.libs.celest.potential.IThinShellPotential]], a potential similar to a spherically symmetric mass
 * distribution outside the shell, and inside it is zero.
 *
 * @param radius Radius of the thin shell [m].
 * @param body Body creating the potential.
 *
 * @author Simon Billemont
 * @see IThinShellPotential
 * 
 */
class ThinShellPotential(body: CelestialBody, val radius: Double) extends PointMassPotential(body) with IThinShellPotential {

	val radiusSq = radius * radius

	/** @inheritdoc */
	override def evaluate(point: Vector3D) =
		if (point.normSq < radiusSq)
			// Valid where R is outside the shell, R >= R2
			// U = Constants.GRAVITATIONAL_CONSTANT / r and is constant, so dU/dr = 0
			Vector3D.ZERO
		else
			// Valid where R is inside the shell, R <= R1
			// U = GM/r = spherically symmetric
			super.evaluate(point)

}