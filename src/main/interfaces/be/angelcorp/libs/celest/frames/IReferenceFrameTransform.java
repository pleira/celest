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
package be.angelcorp.libs.celest.frames;

import be.angelcorp.libs.celest.state.orientationState.IOrientationState;
import be.angelcorp.libs.celest.state.positionState.IPositionState;
import be.angelcorp.libs.celest.state.positionState.IPositionStateDerivative;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.math.rotation.IRotation;

/**
 * Transform between different {@link IReferenceFrame}'s.
 * 
 * @author Simon Billemont
 * 
 * @param <F0>
 *            Transform from this {@link IReferenceFrame}.
 * @param <F1>
 *            Transform to this {@link IReferenceFrame}.
 */
public interface IReferenceFrameTransform<F0 extends IReferenceFrame, F1 extends IReferenceFrame> {

	/**
	 * Apply another {@link IReferenceFrameTransform} on top of this transform, so that the resulting
	 * transformation is a new transform which starts at the same frame as the staring frame of this
	 * transform, and results in the final frame of the given transformation.
	 * 
	 * @param other
	 *            {@link IReferenceFrameTransform} to add to this frame.
	 * @return A {@link IReferenceFrameTransform} from {@link F0} to {@link F2}.
	 */
	public abstract <F2 extends IReferenceFrame> IReferenceFrameTransform<F0, F2>
			add(IReferenceFrameTransform<F1, F2> other);

	/**
	 * Get a factory capable of creating this type of transform under alternative conditions.
	 * 
	 * @return A factory for this type of {@link IReferenceFrameTransform}.
	 */
	public abstract IReferenceFrameTransformFactory<F0, F1> getFactory();

	/**
	 * Get a new transform that, when applied, undoes the effects performed by this transformation. This
	 * means:
	 * 
	 * <pre>
	 * v = this.inverse().transform(this.transform(v))
	 * </pre>
	 * 
	 * @return An inverse transformation of this {@link IReferenceFrameTransform}
	 */
	public abstract IReferenceFrameTransform<F1, F0> inverse();

	/**
	 * Transform a {@link IOrientationState} into a new {@link IReferenceFrame}.
	 * 
	 * @param orientationState
	 *            Orientation to transform.
	 * @return A new {@link IOrientationState} which is equivalent to given {@link IOrientationState},
	 *         but in the new {@link IReferenceFrame}.
	 */
	public abstract IOrientationState transform(IOrientationState orientationState);

	/**
	 * Transform a {@link IPositionState} into a new {@link IReferenceFrame}.
	 * 
	 * @param positionState
	 *            Position to transform.
	 * @return A new {@link IPositionState} which is equivalent to given {@link IPositionState}, but in
	 *         the new {@link IReferenceFrame}.
	 */
	public abstract IPositionState transform(IPositionState positionState);

	/**
	 * Transform a {@link IPositionStateDerivative} into a new {@link IReferenceFrame}.
	 * 
	 * @param positionState
	 *            The position in frame F0.
	 * @param positionStateDerivative
	 *            Position derivatives in frame F0 to transform.
	 * @return A new {@link IPositionStateDerivative} which is equivalent to given
	 *         {@link IPositionStateDerivative}, but in the new {@link IReferenceFrame}.
	 */
	public abstract IPositionStateDerivative transform(IPositionState positionState,
			IPositionStateDerivative positionStateDerivative);

	/**
	 * Transform only the acceleration of a body into a new {@link IReferenceFrame}.
	 * 
	 * \begin{array}{rll}
	 * position = & \{ x, y, z \} & [m] \\
	 * velocity = & \{ \dot{x}, \dot{y}, \dot{z} \} & [m/s] \\
	 * acceleration = & \{ \ddot{x}, \ddot{y}, \ddot{z} \} & [m/s^2]
	 * \end{array}
	 * 
	 * @param position
	 *            Position before transform (in frame F0).
	 * @param velocity
	 *            Velocity before transform (in frame F0).
	 * @param acceleration
	 *            Acceleration before transform (in frame F0).
	 * @returnA new acceleration vector which is equivalent to given provided vector, but in the new
	 *          {@link IReferenceFrame} (frame F1).
	 */
	public abstract Vector3D transformAcceleration(Vector3D position, Vector3D velocity, Vector3D acceleration);

	/**
	 * Transform only the orientation of a body into a new {@link IReferenceFrame}.
	 * 
	 * @param orientation
	 *            Orientation to transform.
	 * @returnA new orientation which is equivalent to given provided orientation, but in the new
	 *          {@link IReferenceFrame}.
	 */
	public abstract IRotation transformOrientation(IRotation orientation);

	/**
	 * Transform only the orientation rate of a body into a new {@link IReferenceFrame}.
	 * 
	 * \begin{array}{rll}
	 * orientation = & \{ \omega_x, \omega_y, \omega_z \} & [rad] \\
	 * orientationRate = & \{ \dot{\omega}_x, \dot{\omega}_y, \dot{\omega}_z \} & [rad/s]
	 * \end{array}
	 * 
	 * @param orientation
	 *            Orientation in frame F0.
	 * @param orientationRate
	 *            Orientation rate in frame F0 to transform.
	 * @returnA new orientation rate which is equivalent to given provided orientation rate, but in the
	 *          new {@link IReferenceFrame}.
	 */
	public abstract Vector3D transformOrientationRate(IRotation orientation, Vector3D orientationRate);

	/**
	 * Transform only the position of a body into a new {@link IReferenceFrame}.
	 * 
	 * \begin{array}{rll}
	 * position = & \{ x, y, z \} & [m] \\
	 * \end{array}
	 * 
	 * @param position
	 *            Position before transform (in frame F0).
	 * @returnA new position vector which is equivalent to given provided vector, but in the new
	 *          {@link IReferenceFrame} (frame F1).
	 */
	public abstract Vector3D transformPosition(Vector3D position);

	/**
	 * Transform only the velocity of a body into a new {@link IReferenceFrame}.
	 * 
	 * \begin{array}{rll}
	 * position = & \{ x, y, z \} & [m] \\
	 * velocity = & \{ \dot{x}, \dot{y}, \dot{z} \} & [m/s]
	 * \end{array}
	 * 
	 * @param position
	 *            Position before transform (in frame F0).
	 * @param velocity
	 *            Velocity before transform (in frame F0).
	 * @returnA new velocity vector which is equivalent to given provided vector, but in the new
	 *          {@link IReferenceFrame} (frame F1).
	 */
	public abstract Vector3D transformVelocity(Vector3D position, Vector3D velocity);

}