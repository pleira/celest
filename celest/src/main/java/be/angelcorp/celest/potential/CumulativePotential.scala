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
package be.angelcorp.celest.potential

import be.angelcorp.celest.math.geometry.Vec3

/**
 * Create a GravitationalPotential that is the result of the summation of a set of independent
 * GravitationalPotentials
 *
 * @param potentials List containing potentials that are to be summed.
 *
 * @author Simon Billemont
 */
class CumulativePotential(val potentials: List[GravitationalPotential]) extends GravitationalPotential {

  override def apply(point: Vec3) =
    potentials.foldLeft(Vec3.zero)((sum, p) => sum + p(point))

}
