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
package be.angelcorp.libs.celest.state.orientationState;

import org.apache.commons.math3.linear.RealVector;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.state.IState;

/**
 * This is the interface for the orientation or attitude of a specific body's (e.g. the current
 * oritentation and oritational rates).
 * 
 * <p>
 * {@link IOrientationState}'s extending this class should also implement the following for convenience:
 * </p>
 * 
 * <pre>
 * public static {@link IOrientationState} fromVector(RealVector vector);
 * </pre>
 * 
 * <p>
 * Furthermore it is also handy to implement the following routine:
 * </p>
 * 
 * <pre>
 * public static [StateVectorClass] as({@link IOrientationState} state, {@link CelestialBody} center);
 * </pre>
 */
public interface IOrientationState extends IState {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IOrientationState clone();

	/**
	 * Tests if two {@link IOrientationState} are equal.
	 * 
	 * @param obj
	 *            Compare the current {@link IOrientationState} with this ones.
	 * @return true if all elements match exactly.
	 */
	public abstract boolean equals(IOrientationState obj);

	/**
	 * Tests if two {@link IOrientationState}'s are equal.
	 * 
	 * <p>
	 * It tests using a a relative error eps and applies the following test to each element:
	 * </p>
	 * 
	 * <pre>
	 * abs(vx1 - vx2) &lt; eps
	 * </pre>
	 * 
	 * @param obj
	 *            Compare the current {@link IOrientationState} with this ones.
	 * @param eps
	 *            Relative error to check against.
	 * @return true if all elements are within the given tolerance range.
	 */
	public abstract boolean equals(IOrientationState obj, double eps);

	/**
	 * Convert the {@link IOrientationState} to an equivalent quaternion one.
	 * 
	 * @return Quaternion equivalent state vector.
	 */
	public abstract IQuaternionOrientation toCartesianElements();

	/**
	 * Convert the current state vector to an equivalent vector form
	 * 
	 * @return Vector equivalent of the state vector
	 */
	@Override
	public abstract RealVector toVector();

}