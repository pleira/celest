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
package be.angelcorp.libs.celest.stateVector;

import be.angelcorp.libs.celest.math.Cartesian;
import be.angelcorp.libs.math.linear.Vector3D;

/**
 * 
 * {@link StateVector} in the form of the Cartesian position R (x,y,z) and velocity V (&#7819;, &#7823;,
 * &#380;).
 * 
 * <pre>
 * Elements: { x,   y,   z,    &#7819;,     &#7823;,     &#380;  }
 * Units:    {[m], [m], [m], [m/s], [m/s], [m/s]}
 * </pre>
 * 
 * @author Simon Billemont
 * 
 */
public interface ICartesianElements extends Cartesian, IStateVector {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract ICartesianElements clone();

	/**
	 * Tests if two sets of Cartesian elements hold the same variables (tests for equal
	 * x,y,z,&#7819;,&#7823;,&#380;)
	 * 
	 * @param state2
	 *            Cartesian elements to compare with
	 * @return True if the two sets contain the same orbital elements
	 */
	public abstract boolean equals(ICartesianElements state2);

	/**
	 * Tests if two sets of Cartesian elements hold the same variables (tests for equal
	 * x,y,z,&#7819;,&#7823;,&#380;). It tests if all elements are equal using
	 * 
	 * <pre>
	 * abs(x1 -x2) < x1 * eps.
	 * </pre>
	 * 
	 * @param state2
	 *            Cartesian elements to compare with
	 * @param eps
	 *            Relative precision to test all the elements with. The actual test value is scaled with
	 *            each respective parameter of this vector.
	 * @return True if the two sets contain the same orbital elements
	 */
	public abstract boolean equals(ICartesianElements state2, double eps);

	/**
	 * Get the instantaneous position
	 * <p>
	 * <b>Unit: [m]</b>
	 * </p>
	 * 
	 * @return instantaneous position vector
	 */
	public abstract Vector3D getR();

	/**
	 * Get the instantaneous velocity
	 * <p>
	 * <b>Unit: [m/s<sup>2</sup>]</b>
	 * </p>
	 * 
	 * @return instantaneous velocity vector
	 */
	public abstract Vector3D getV();

	/**
	 * Set the instantaneous position
	 * <p>
	 * <b>Unit: [m]</b>
	 * </p>
	 * 
	 * @param r
	 *            new instantaneous position vector [m]
	 */
	public abstract void setR(Vector3D r);

	/**
	 * Set the instantaneous velocity
	 * <p>
	 * <b>Unit: [m/s<sup>2</sup>]</b>
	 * </p>
	 * 
	 * @param v
	 *            new instantaneous velocity vector [m/s^2]
	 */
	public abstract void setV(Vector3D v);

}