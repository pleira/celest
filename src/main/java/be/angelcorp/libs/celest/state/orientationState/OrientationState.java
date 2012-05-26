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

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;

import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.math.rotation.IRotation;
import be.angelcorp.libs.math.rotation.Quaternion;

/**
 * @author Simon Billemont
 * @see IOrientationState
 */
public class OrientationState implements IOrientationState {

	/** Orientation of the current state */
	private final IRotation	rotation;
	/**
	 * Orientation derivative of the current state:
	 * $$ \dot\omega = \{ \dot\omega_x, \dot\omega_y, \dot\omega_z \} [rad/s] $$
	 */
	private final Vector3D	rotationRate;

	/**
	 * Construct a new {@link OrientationState} from a known orientation and its derivative.
	 * 
	 * @param rotation
	 *            Orientation of the body.
	 * @param rotationRate
	 *            Orientation rate of the body.
	 */
	public OrientationState(IRotation rotation, Vector3D rotationRate) {
		this.rotation = rotation;
		this.rotationRate = rotationRate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(IOrientationState obj) {
		return rotation.equals(obj.getRotation()) && rotationRate.equals(obj.getRotationRate());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(IOrientationState obj, double eps) {
		double delta = Rotation.distance(rotation.toQuaternion(), obj.getRotation().toQuaternion());
		return Precision.equals(delta, 0, eps) && rotationRate.equals(obj.getRotationRate(), eps);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRotation getRotation() {
		return rotation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3D getRotationRate() {
		return rotationRate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RealVector toVector() {
		Quaternion q = rotation.toQuaternion();
		ArrayRealVector v = new ArrayRealVector(4 + 3);
		v.setEntry(0, q.getQ0());
		v.setEntry(1, q.getQ1());
		v.setEntry(2, q.getQ2());
		v.setEntry(3, q.getQ3());
		v.setEntry(4, rotationRate.getX());
		v.setEntry(5, rotationRate.getY());
		v.setEntry(6, rotationRate.getZ());
		return v;
	}

}