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
package be.angelcorp.libs.celest.potential;

import be.angelcorp.libs.math.field.Vector3dField;
import be.angelcorp.libs.math.linear.Vector3D;

/**
 * Represents the LOCAL gravitational field or potential created by an object
 * 
 * <p>
 * As stated, this is the local potential. This means that the given point is with respect to the center
 * of mass of the object creating the potential.
 * </p>
 * <p>
 * The potential represents the acceleration created by the object creating the potential on a unit mass
 * </p>
 * 
 * @author simon
 * 
 */
public interface IGravitationalPotential extends Vector3dField<Vector3D> {

	/**
	 * Evaluate the gravitational potential at a given point to the the acceleration caused by this
	 * potential
	 * 
	 * <p>
	 * <b>Unit: [m/s<sup>2</sup>]</b>
	 * </p>
	 * 
	 * @param point
	 *            Point relative to the center of mass of the body being evaluated
	 * @return Local acceleration for a unit mass a the location of the given point
	 */
	@Override
	public Vector3D evaluate(Vector3D point);

}
