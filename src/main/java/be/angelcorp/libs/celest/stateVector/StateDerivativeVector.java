/**
 * Copyright (C) 2011 Simon Billemont <aodtorusan@gmail.com>
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 3.0 Unported
 * (CC BY-NC 3.0) (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *        http://creativecommons.org/licenses/by-nc/3.0/
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
 * State vector that hold the state of a celestial body.
 * 
 * @author simon
 * 
 */
public abstract class StateDerivativeVector {

	/**
	 * Restore the StateDerivativeVector from a vector
	 * 
	 * @param vector
	 *            Vector to restore the the state from
	 * @return State vector as contained in the given vector
	 */
	public static StateDerivativeVector fromVector(RealVector vector) {
		throw new UnsupportedOperationException("This method must be overwritten");
	}

	/**
	 * Create a new StateDerivativeVector with identical properties
	 */
	@Override
	public abstract StateDerivativeVector clone();

	/**
	 * Convert the StateDerivativeVector to an equivalent Cartesian one (V,A in Cartesian coordinates)
	 * 
	 * @return Cartesian equivalent state derivative vector
	 */
	public abstract CartesianDerivative toCartesianDerivative();

	@Override
	public String toString() {
		return toVector().toString();
	}

	/**
	 * Convert the current state vector to an equivalent vector form
	 * 
	 * @return Vector equivalent of the state vector
	 */
	public abstract RealVector toVector();
}
