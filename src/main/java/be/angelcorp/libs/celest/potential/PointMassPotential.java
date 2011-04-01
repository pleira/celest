/**
 * Copyright (C) 2011 simon <aodtorusan@gmail.com>
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
package be.angelcorp.libs.celest.potential;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.math.linear.Vector3D;

/**
 * Create an ideal gravitational potential of a point mass or a homogeneous sphere
 * 
 * @author simon
 * 
 */
public class PointMassPotential implements GravitationalPotential {

	/**
	 * Body that creates the current potential, (uses its mass or mu)
	 */
	private CelestialBody	body;

	/**
	 * Construct a potential based on a single celestial body
	 * 
	 * @param body
	 *            Body generating the potential
	 */
	public PointMassPotential(CelestialBody body) {
		this.body = body;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3D evaluate(Vector3D point) {
		return point.multiply(body.getMu() / Math.pow(point.getNorm(), 3)).negate();
	}
}
