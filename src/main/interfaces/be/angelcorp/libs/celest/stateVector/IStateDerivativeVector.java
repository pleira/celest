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
 * @author simon
 * 
 */
public interface IStateDerivativeVector {

	/**
	 * Create a new StateDerivativeVector with identical properties
	 */
	public abstract IStateDerivativeVector clone();

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