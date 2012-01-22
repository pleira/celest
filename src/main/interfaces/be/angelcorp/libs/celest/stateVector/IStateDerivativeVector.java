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
 * This hold the derivatives of the state of a given body
 * 
 * <p>
 * StateDerivatives extending this class should also implement the following for convenience:
 * </p>
 * 
 * <pre>
 * public static IStateDerivativeVector fromVector(RealVector vector);
 * </pre>
 * 
 * @author Simon Billemont
 * 
 */
public interface IStateDerivativeVector extends Cloneable {

	/**
	 * Create a new StateDerivativeVector with identical properties
	 */
	public abstract IStateDerivativeVector clone();

	/**
	 * Tests if two {@link IStateDerivativeVector} are equal. By default, this id done by comparing all
	 * elements of the {@link IStateDerivativeVector#toVector()} output. Each elemet must have an
	 * identical value to be considered equal.
	 * 
	 * @param obj
	 *            Compare the current {@link IStateDerivativeVector} with this ones.
	 * @return true if they are equal.
	 */
	public abstract boolean equals(IStateDerivativeVector obj);

	/**
	 * Tests if two {@link IStateDerivativeVector} are equal. By default, this id done by comparing all
	 * elements of the {@link IStateDerivativeVector#toVector()} output.
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
	 *            Compare the current {@link IStateDerivativeVector} with this ones.
	 * @param eps
	 *            Relative error to check against.
	 * @return true if they are equal.
	 */
	public abstract boolean equals(IStateDerivativeVector obj, double eps);

	/**
	 * Convert the StateDerivativeVector to an equivalent Cartesian one (V,A in Cartesian coordinates)
	 * 
	 * @return Cartesian equivalent state derivative vector
	 */
	public abstract CartesianDerivative toCartesianDerivative();

	/**
	 * Convert the current state vector to an equivalent vector form
	 * 
	 * @return Vector equivalent of the state vector
	 */
	public abstract RealVector toVector();

}