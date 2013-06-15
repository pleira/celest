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
package be.angelcorp.libs.celest.potential;

import be.angelcorp.libs.celest.body.ICelestialBody;
import be.angelcorp.libs.math.linear.Vector3D;

/**
 * Create an ideal gravitational potential of a point mass, homogeneous sphere or body with a spherically
 * symmetric mass distribution.
 * 
 * @author Simon Billemont
 * 
 */
public interface IPointMassPotential extends IGravitationalPotential {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract Vector3D evaluate(Vector3D point);

	/**
	 * Get the body around which the gravity potential is centered
	 * 
	 * @return
	 */
	public abstract ICelestialBody getBody();

}
