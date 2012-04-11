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
package be.angelcorp.libs.celest.state.positionState;

import org.apache.commons.math.linear.RealVector;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.state.IState;

/**
 * This is the interface for each position state (representation of a body's current location/velocity
 * coordinates).
 * 
 * <p>
 * {@link IPositionState}'s extending this class should also implement the following for convenience:
 * </p>
 * 
 * <pre>
 * public static {@link IPositionState} fromVector(RealVector vector);
 * </pre>
 * 
 * <p>
 * Furthermore it is also handy to implement the following routine:
 * </p>
 * 
 * <pre>
 * public static [StateVectorClass] as({@link IPositionState} state, {@link CelestialBody} center);
 * </pre>
 */
public interface IPositionState extends IState {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPositionState clone();

	/**
	 * Tests if two {@link IPositionState} are equal. By default, this is done by comparing all elements
	 * of the {@link IPositionState#toVector()} output.
	 * 
	 * @param obj
	 *            Compare the current {@link IPositionState} with this ones
	 * @return true if they are equal
	 */
	public abstract boolean equals(IPositionState obj);

	/**
	 * Tests if two {@link IPositionState}'s are equal. By default, this is done by comparing all
	 * elements of the {@link IPositionState#toVector()} output.
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
	 *            Compare the current {@link IPositionState} with this ones.
	 * @param eps
	 *            Relative error to check against.
	 * @return true if they are equal.
	 */
	public abstract boolean equals(IPositionState obj, double eps);

	/**
	 * Convert the {@link IPositionState} to an equivalent Cartesian one (R, V in Cartesian coordinates)
	 * 
	 * @return Cartesian equivalent state vector
	 */
	public abstract ICartesianElements toCartesianElements();

	/**
	 * Convert the current state vector to an equivalent vector form
	 * 
	 * @return Vector equivalent of the state vector
	 */
	@Override
	public abstract RealVector toVector();

}