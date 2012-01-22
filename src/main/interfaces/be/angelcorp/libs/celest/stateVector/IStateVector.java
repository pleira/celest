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

import org.apache.commons.math.linear.RealVector;

/**
 * This is the interface for each state vector (representation of a body's current location/velocity
 * coordinates).
 * 
 * <p>
 * StateVectors extending this class should also implement the following for convenience:
 * </p>
 * 
 * <pre>
 * public static IStateVector fromVector(RealVector vector);
 * </pre>
 * 
 * <p>
 * Furthermore it is also handy to implement the following routine:
 * </p>
 * 
 * <pre>
 * public static [StateVectorClass] as(IStateVector state, CelestialBody center);
 * </pre>
 */
public interface IStateVector extends Cloneable {

	/**
	 * Create a new {@link StateVector} with identical properties
	 */
	public abstract IStateVector clone();

	/**
	 * Tests if two StateVectors are equal. By default, this id done by comparing all elements of the
	 * {@link StateVector#toVector()} output.
	 * 
	 * @param obj
	 *            Compare the current {@link StateVector} with this ones
	 * @return true if they are equal
	 */
	public abstract boolean equals(IStateVector obj);

	/**
	 * Tests if two StateVectors are equal. By default, this id done by comparing all elements of the
	 * {@link StateVector#toVector()} output.
	 * 
	 * <p>
	 * It tests using a a relative error eps and applies the following test to each element:
	 * </p>
	 * 
	 * <pre>
	 * abs(vx1 - vx2) &lt; eps * vx1
	 * </pre>
	 * 
	 * @param obj
	 *            Compare the current {@link StateVector} with this ones.
	 * @param eps
	 *            Relative error to check against.
	 * @return true if they are equal.
	 */
	public abstract boolean equals(IStateVector obj, double eps);

	/**
	 * Convert the {@link StateVector} to an equivalent Cartesian one (R, V in Cartesian coordinates)
	 * 
	 * @return Cartesian equivalent state vector
	 */
	public abstract ICartesianElements toCartesianElements();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract String toString();

	/**
	 * Convert the current state vector to an equivalent vector form
	 * 
	 * @return Vector equivalent of the state vector
	 */
	public abstract RealVector toVector();

}