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

import be.angelcorp.libs.math.linear.Vector3D

/**
 * Implementation of {@link ICumulativePotential} that constructs a gravity potential by combining a list of
 * other, known potentials.
 *
 * @param potentials List containing potentials that are to be summed.
 *
 * @author simon
 * @see ICumulativePotential
 */
class CumulativePotential(val potentials: List[IGravitationalPotential]) extends ICumulativePotential {

	/** @inheritdoc */
	override def evaluate(point: Vector3D) = potentials.foldLeft(Vector3D.ZERO)( (sum, p) => sum + p.evaluate(point) )

}
