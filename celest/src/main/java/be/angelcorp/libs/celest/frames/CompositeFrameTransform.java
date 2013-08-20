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

import be.angelcorp.libs.celest.state.Orbit;
import be.angelcorp.libs.celest.state.orientationState.IOrientationState;
import be.angelcorp.libs.celest.state.positionState.IPositionStateDerivative;
import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.math.rotation.IRotation;

/**
 * An {@link IReferenceFrameTransform} that sequentially applies two other
 * {@link IReferenceFrameTransform}'s:
 * <p>
 * $$ x_2 = T_{1 \to 2}( T_{0 \to 1}( x_0 )) $$
 * </p>
 * With;
 * <ul>
 * <li>\(x_a\): A state in reference frame a.</li>
 * <li>\(T_{a \to b}\): The transform of a state from reference frame a to b.</li>
 * </ul>
 * 
 * @author Simon Billemont
 * 
 * @param <F0>
 *            Transform a state defined in this frame.
 * @param <F1>
 *            Intermediate reference frame.
 * @param <F2>
 *            Transform to a state in this frame.
 */
public class CompositeFrameTransform<F0 extends IReferenceFrame, F1 extends IReferenceFrame, F2 extends IReferenceFrame>
		extends BasicReferenceFrameTransform<F0, F2, CompositeFrameTransformFactory<F0, F1, F2>> {

	/** First transformation to apply; F0 => F1 */
	private final IReferenceFrameTransform<F0, F1>	transform0;
	/** Second transformation to apply; F1 => F2 */
	private final IReferenceFrameTransform<F1, F2>	transform1;

	/**
	 * Create a {@link CompositeFrameTransform} from two other known {@link IReferenceFrameTransform}'s.
	 * 
	 * @param factory
	 *            A factory capable of producing other {@link CompositeFrameTransform}'s from F0 => F2.
	 * @param epoch
	 *            The epoch at which this transform is guaranteed to be valid.
	 * @param transform0
	 *            First transform to apply, F0 => F1.
	 * @param transform1
	 *            Second transform to apply, F1 => F2.
	 */
	public CompositeFrameTransform(CompositeFrameTransformFactory<F0, F1, F2> factory, IJulianDate epoch,
			IReferenceFrameTransform<F0, F1> transform0, IReferenceFrameTransform<F1, F2> transform1) {
		super(factory, epoch);
		this.transform0 = transform0;
		this.transform1 = transform1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IOrientationState transform(IOrientationState orientationState) {
		IOrientationState orientationState_f1 = transform0.transform(orientationState);
		IOrientationState orientationState_f2 = transform1.transform(orientationState_f1);
		return orientationState_f2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Orbit transform(Orbit positionState) {
		Orbit positionState_f1 = transform0.transform(positionState);
        Orbit positionState_f2 = transform1.transform(positionState_f1);
		return positionState_f2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPositionStateDerivative transform(Orbit positionState,
			IPositionStateDerivative positionStateDerivative) {
        Orbit positionState_f1 = transform0.transform(positionState);
		IPositionStateDerivative positionStateDerivative_f1 = transform0.transform(positionState, positionStateDerivative);
		IPositionStateDerivative positionStateDerivative_f2 = transform1.transform(positionState_f1, positionStateDerivative_f1);

		return positionStateDerivative_f2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3D transformAcceleration(Vector3D position, Vector3D velocity, Vector3D acceleration) {
		Vector3D position_f1 = transform0.transformPosition(position);
		Vector3D velocity_f1 = transform0.transformVelocity(position, velocity);
		Vector3D acceleration_f1 = transform0.transformAcceleration(position, velocity, acceleration);

		Vector3D acceleration_f2 = transform1.transformAcceleration(position_f1, velocity_f1, acceleration_f1);
		return acceleration_f2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRotation transformOrientation(IRotation orientation) {
		IRotation orientation_f1 = transform0.transformOrientation(orientation);
		IRotation orientation_f2 = transform1.transformOrientation(orientation_f1);
		return orientation_f2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3D transformOrientationRate(IRotation orientation, Vector3D orientationRate) {
		IRotation orientation_f1 = transform0.transformOrientation(orientation);
		Vector3D orientationRate_f1 = transform0.transformOrientationRate(orientation, orientationRate);

		Vector3D orientationRate_f2 = transform1.transformOrientationRate(orientation_f1, orientationRate_f1);
		return orientationRate_f2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3D transformPosition(Vector3D position) {
		Vector3D position_f1 = transform0.transformPosition(position);
		Vector3D position_f2 = transform1.transformPosition(position_f1);
		return position_f2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3D transformVelocity(Vector3D position, Vector3D velocity) {
		Vector3D position_f1 = transform0.transformPosition(position);
		Vector3D velocity_f1 = transform0.transformVelocity(position, velocity);

		Vector3D velocity_f2 = transform1.transformVelocity(position_f1, velocity_f1);
		return velocity_f2;
	}

}
