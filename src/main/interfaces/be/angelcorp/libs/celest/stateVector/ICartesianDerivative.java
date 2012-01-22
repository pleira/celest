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
 * Stores the state derivatives of an object state (usually {@link CartesianElements}). This is done
 * using Cartesian velocity V (&#7819;, &#7823;, &#380;) and its acceleration A (x&#776;, y&#776;,
 * z&#776;).
 * 
 * <pre>
 * Elements: {  &#7819;,     &#7823;,     &#380;,     x&#776;,      y&#776;,      z&#776;  }
 * Units:    {[m/s], [m/s], [m/s], [m/s&#178;], [m/s&#178;], [m/s&#178;]}
 * </pre>
 * 
 * @author Simon Billemont
 * 
 */
public interface ICartesianDerivative extends Cartesian, IStateDerivativeVector {

	/**
	 * Create a deep copy of this object. It returns an exact copy of this {@link IStateDerivativeVector}
	 * , but with no direct link to the object it was created against. This means changing the original
	 * {@link IStateDerivativeVector} does not effect the values of the newly created
	 * {@link IStateDerivativeVector}
	 */
	@Override
	public abstract ICartesianDerivative clone();

	/**
	 * Check if two {@link ICartesianDerivative} are exactly equal (two NaN elements are considered
	 * equal).
	 * 
	 * @param other
	 *            Other vector to compare against
	 * @return True if all the elements of the two {@link ICartesianDerivative} are equal.
	 */
	public abstract boolean equals(ICartesianDerivative other);

	/**
	 * Check if two {@link ICartesianDerivative} are equal (two NaN elements are considered equal).
	 * 
	 * <p>
	 * It tests using a a relative error eps and applies the following test to each element:
	 * </p>
	 * 
	 * <pre>
	 * abs(vx1 - vx2) &lt; eps * vx1
	 * </pre>
	 * 
	 * @param other
	 *            Other vector to compare against.
	 * @param eps
	 *            Relative error to check against.
	 * @return True if all the elements of the two {@link ICartesianDerivative} are equal.
	 */
	public abstract boolean equals(ICartesianDerivative other, double eps);

	/**
	 * Get the acceleration vector (the derivative of the velocity vector of the
	 * {@link ICartesianElements})
	 * 
	 * @return The acceleration vector [m/s<sup>2</sup>]
	 */
	public abstract Vector3D getA();

	/**
	 * Get the velocity vector (the derivative of the position vector of the {@link ICartesianElements},
	 * equal to it velocity elements)
	 * 
	 * @return The velocity vector [m/s]
	 */
	public abstract Vector3D getV();

	/**
	 * Set the acceleration vector (the derivative of the velocity vector of the
	 * {@link ICartesianElements})
	 * 
	 * @param a
	 *            The acceleration vector [m/s<sup>2</sup>]
	 */
	public abstract void setA(Vector3D a);

	/**
	 * Set the velocity vector (the derivative of the position vector of the {@link ICartesianElements},
	 * equal to it velocity elements)
	 * 
	 * @param v
	 *            The velocity vector [m/s]
	 */
	public abstract void setV(Vector3D v);

}