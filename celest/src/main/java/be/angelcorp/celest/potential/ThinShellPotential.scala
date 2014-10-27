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

import be.angelcorp.celest.body.Satellite
import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.frameGraph.ReferenceSystem


/**
 * Create an ideal gravitational potential of thin spherical shell. This is equivalent to a point mass,
 * when the point considered is outside the shell and when inside the shell 0(see Gauss' law for
 * gravity).
 *
 * @param radius Radius of the thin shell [m].
 * @param body Body creating the potential.
 *
 * @author Simon Billemont
 */
class ThinShellPotential[F <: ReferenceSystem](body: Satellite[F], val radius: Double) extends PointMassPotential(body) {

  val radiusSq = radius * radius

  override def apply(point: Vec3) =
    if (point.normSq < radiusSq)
    // Valid where R is outside the shell, R >= R2
    // U = Constants.GRAVITATIONAL_CONSTANT / r and is constant, so dU/dr = 0
      Vec3.zero
    else
    // Valid where R is inside the shell, R <= R1
    // U = GM/r = spherically symmetric
      super.apply(point)

}
