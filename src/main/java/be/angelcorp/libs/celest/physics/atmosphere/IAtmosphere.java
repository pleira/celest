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
package be.angelcorp.libs.celest.physics.atmosphere;

import be.angelcorp.libs.math.linear.Vector3D;

public interface IAtmosphere {

	/**
	 * Abstract class requires the subclass to compute the atmospheric density.
	 * 
	 * @param ref
	 *            EarthRef object.
	 * @param r
	 *            Position vector.
	 * @return Atmospheric density in kg/m^3
	 */
	abstract public double computeDensity(Vector3D r);

	abstract public Vector3D computeV(Vector3D r);
	// VectorN we = new VectorN(0, 0, omega_e);
	// VectorN wxr = we.crossProduct(r);
}
