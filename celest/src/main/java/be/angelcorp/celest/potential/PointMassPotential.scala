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
import be.angelcorp.libs.math.linear.Vector3D
import scala.math._

/**
 * Create an ideal gravitational potential of a point mass, homogeneous sphere or body with a spherically
 * symmetric mass distribution.
 *
 * @param body Body that creates the current potential, (uses its mass or mu).
 *
 * @author Simon Billemont
 * @see IPointMassPotential
 */
class PointMassPotential(val body: Satellite) extends GravitationalPotential {

  override def apply(point: Vector3D) =
    point.negate * (body.μ / pow(point.norm, 3))

}
