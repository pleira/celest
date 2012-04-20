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

import be.angelcorp.libs.celest.state.positionState.ICartesianElements;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.math.rotation.Quaternion;

/**
 * 
 * {@link IOrientationState} saved in the form of a Quaternion and the rates around each of the three
 * axes (x -> &alpha;,y -> &beta;,z -> &gamma;).
 * 
 * <pre>
 * Elements: {q0,  q1,  q2,  q3,     &alpha;,       &beta;,       &gamma;   }
 * Units:    {[-], [-], [-], [-], [rad/s], [rad/s], [rad/s]}
 * </pre>
 * 
 * @author Simon Billemont
 * 
 */
public interface IQuaternionOrientation extends IOrientationState {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IQuaternionOrientation clone();

	/**
	 * Tests if two sets of {@link IQuaternionOrientation} elements hold the same variables. The tests
	 * are performed using relative precision:
	 * 
	 * <pre>
	 * abs(x1 - x2) < x1 * eps.
	 * </pre>
	 * 
	 * @param state2
	 *            {@link IQuaternionOrientation} elements to compare with.
	 * @param eps
	 *            Relative precision to test all the elements with.
	 * @return True if the two sets contain the same orbital elements.
	 */
	public abstract boolean equals(ICartesianElements state2, double eps);

	/**
	 * Tests if two sets of {@link IQuaternionOrientation} elements hold the same variables.
	 * 
	 * @param state2
	 *            {@link IQuaternionOrientation} elements to compare with.
	 * @return True if the two sets contain the same orbital elements.
	 */
	public abstract boolean equals(IQuaternionOrientation state2);

	/**
	 * Get the instantaneous orientation (attitude).
	 * 
	 * @return Instantaneous orientation rotation.
	 */
	public abstract Quaternion getRotation();

	/**
	 * Get the rotation rate velocity.
	 * <p>
	 * <b>Unit: [rad/s]</b>
	 * </p>
	 * 
	 * @return Instantaneous rotation rate.
	 */
	public abstract Vector3D getRotationRate();

	/**
	 * Set the instantaneous rotation.
	 * 
	 * @param q
	 *            New instantaneous orientation rotation.
	 */
	public abstract void setRotation(Quaternion q);

	/**
	 * Set the instantaneous rotation rate.
	 * <p>
	 * <b>Unit: [rad/s]</b>
	 * </p>
	 * 
	 * @param v
	 *            New instantaneous velocity vector [rad/s].
	 */
	public abstract void setRotationRate(Vector3D v);
}