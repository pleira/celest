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

import be.angelcorp.libs.celest.frames.ReferenceFrameTransformFactory.TransformationParameters;
import be.angelcorp.libs.celest.state.Orbit;
import be.angelcorp.libs.celest.state.PosVel;
import be.angelcorp.libs.celest.state.orientationState.IOrientationState;
import be.angelcorp.libs.celest.state.positionState.CartesianDerivative;
import be.angelcorp.libs.celest.state.positionState.ICartesianDerivative;
import be.angelcorp.libs.celest.state.positionState.IPositionStateDerivative;
import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.math.rotation.IRotation;

/**
 * The default implementation for a reference frame transformation, including kinematic effects:
 * <p>
 * $$
 * \begin{array}{rl}
 * \vec{r}^* = & \tilde{R} \vec{r} \\
 * \vec{v}^* = & \tilde{R} \left[ \vec{v} + \vec{\omega} \times \vec{r} \right] \\
 * \vec{a}^* = & \tilde{R} \left[ \vec{a} + 2 \vec{\omega} \times \vec{v} + \vec{\alpha} \times
 * \vec{r} + \vec{\omega} \times \left( \vec{\omega} \times \vec{r} \right) \right]
 * \end{array}
 * $$
 * </p>
 * <p>Where: </p>
 * <ul>
 * <li>\( \vec{r} \) is the Cartesian position in the original frame F0.</li>
 * <li>\( \vec{v} \) is the Cartesian velocity in the original frame F0.</li>
 * <li>\( \vec{a} \) is the Cartesian acceleration in the original frame F0.</li>
 * <li>\( \vec{\box}^* \) is the p/v/a in the new frame F1.</li>
 * <li>\( \tilde{R} \) is the {@link IRotation} transform part (eg. rotation matrix).</li>
 * <li>\( \vec{\omega} \) is the rotationalRate of frame F1 relative to F0.</li>
 * <li>\( \vec{\alpha} \) is the rotationalAcceleration of frame F1 relative to F0.</li>
 * </ul>
 * 
 * <p>
 * The work in this class is mainly based on:
 * </p>
 * <ul>
 * <li>Richard H. Battin, <b>"An Introduction to the Mathematics and Methods of Astrodynamics"</b>, AIAA
 * Education Series, 1999, ISBN 1563473429</li>
 * </ul>
 * 
 * @author Simon Billemont
 * 
 * @param <F0>
 *            Origin reference frame type.
 * @param <F1>
 *            Destination reference frame type.
 */
public class ReferenceFrameTransform<F0 extends IReferenceFrame, F1 extends IReferenceFrame> extends
		BasicReferenceFrameTransform<F0, F1, IReferenceFrameTransformFactory<F0, F1>> {

	/**
	 * A wrapper around all the varius required transformation parametes such as:
	 * <ul>
	 * <li>translation</li>
	 * <li>velocity</li>
	 * <li>acceleration</li>
	 * <li>rotation</li>
	 * <li>rotationRate</li>
	 * <li>rotationAcceleration</li>
	 * </ul>
	 */
	private final TransformationParameters	parameters;

    /**
	 * Create a new {@link IReferenceFrameTransform} using a given parent factory, capable of creating
	 * new {@link ReferenceFrameTransform}'s like this one, but for various other epochs. Furthermore a
	 * the epoch at which this class is guaranteed to be valid and finally, a set of transformation
	 * parameters describing the relation between the origin and destination frames.
	 * 
	 * @param factory
	 *            Factory which produced this frame.
	 * @param epoch
	 *            Epoch at which this transform is valid.
	 * @param parameters
	 *            Parameters describing the transform between the two frames.
	 */
	public ReferenceFrameTransform(IReferenceFrameTransformFactory<F0, F1> factory, IJulianDate epoch,
			TransformationParameters parameters) {
		super(factory, epoch);
		this.parameters = parameters;
	}

    /**
     * Returns the internal parameters used to perform the transformation.
     * @return Transformation parameters.
     */
    public TransformationParameters getParameters() {
        return parameters;
    }

	/**
	 * <b>Not implemented yet!</b>
	 */
	@Override
	public IOrientationState transform(IOrientationState orientationState) {
		// TODO Auto-generated method stub
		// return null;
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PosVel transform(Orbit positionState) {
        PosVel cartesianElements = positionState.toPosVel();

		Vector3D p_f0 = cartesianElements.position();
		Vector3D v_f0 = cartesianElements.velocity();

		Vector3D p_f1 = transformPosition(p_f0);
		Vector3D v_f1 = transformVelocity(p_f0, v_f0);

        final scala.Option<BodyCentered> none = scala.Option.apply(null);
        PosVel state_f1 = new PosVel(p_f1, v_f1, none);
		return state_f1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CartesianDerivative transform(Orbit positionState,
			IPositionStateDerivative positionStateDerivative) {
        PosVel cartesianElements = positionState.toPosVel();
		ICartesianDerivative cartesianDerivative = positionStateDerivative.toCartesianDerivative();

		Vector3D p_f0 = cartesianElements.position();
		Vector3D v_f0 = cartesianDerivative.getV();
		Vector3D a_f0 = cartesianDerivative.getA();

		Vector3D v_f1 = transformVelocity(p_f0, v_f0);
		Vector3D a_f1 = transformAcceleration(p_f0, v_f0, a_f0);

		CartesianDerivative derivative_f1 = new CartesianDerivative(v_f1, a_f1);
		return derivative_f1;
	}

	/**
	 * Transforms a velocity in frame F0 to the frame F1 using:
	 * 
	 * $$ \vec{a}^* = \tilde{R} \left[ \vec{a} + 2 \vec{\omega} \times \vec{v} + \vec{\alpha} \times
	 * \vec{r} + \vec{\omega} \times \left( \vec{\omega} \times \vec{r} \right) \right] $$
	 * 
	 * Where:
	 * <ul>
	 * <li>\( \vec{a}^* \) is the Cartesian acceleration in the new frame F1.</li>
	 * <li>\( \vec{r} \) is the Cartesian position in the original frame F0.</li>
	 * <li>\( \vec{v} \) is the Cartesian velocity in the original frame F0.</li>
	 * <li>\( \vec{a} \) is the Cartesian acceleration in the original frame F0.</li>
	 * <li>\( \tilde{R} \) is the {@link IRotation} transform part (eg. rotation matrix).</li>
	 * <li>\( \vec{\omega} \) is the rotationalRate of frame F1 relative to F0.</li>
	 * <li>\( \vec{\alpha} \) is the rotationalAcceleration of frame F1 relative to F0.</li>
	 * </ul>
	 * <p>
	 * Based on: [battin] page 102, eqn 2.54
	 * </p>
	 */
	@Override
	public Vector3D transformAcceleration(Vector3D p, Vector3D v, Vector3D a) {
		Vector3D p2 = p.add(parameters.translation);
		Vector3D v2 = v.add(parameters.velocity);

		// a_observed = \vec{a}
		Vector3D observed = a.add(parameters.acceleration);
		// a_coriolis = 2 \vec{\omega} \times \vec{v}
		Vector3D coriolis = parameters.rotationRate.multiply(2).cross(v2);
		// a_euler = \vec{\alpha} \times \vec{r}
		Vector3D euler = parameters.rotationAcceleration.cross(p2);
		// a_centripetal = omega \times ( \vec{\omega} \times \vec{r} )
		Vector3D centripetal = parameters.rotationRate.cross(parameters.rotationRate.cross(p2));

		// a = R [ a_observed + a_coriolis + a_euler + a_centripetal ]
		Vector3D a_f1 = parameters.rotation.applyTo(observed.add(coriolis).add(euler).add(centripetal));
		return a_f1;
	}

	/**
	 * <b>Not implemented yet!</b>
	 */
	@Override
	public IRotation transformOrientation(IRotation orientation) {
		// TODO Auto-generated method stub
		// return null;
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * <b>Not implemented yet!</b>
	 */
	@Override
	public Vector3D transformOrientationRate(IRotation orientation, Vector3D orientationRate) {
		// TODO Auto-generated method stub
		// return null;
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Transforms the position in frame F0 to the frame F1 using:
	 * 
	 * $$ \vec{r}^* = \tilde{R} \vec{r} $$
	 * 
	 * Where:
	 * <ul>
	 * <li>\( \vec{r}^* \) is the Cartesian position in the new frame F1.</li>
	 * <li>\( \vec{r} \) is the Cartesian position in the original frame F0.</li>
	 * <li>\( \tilde{R} \) is the {@link IRotation} transform part (eg. rotation matrix).</li>
	 * </ul>
	 * 
	 * <p>
	 * Based on: [battin] page 101, eqn 2.49
	 * </p>
	 */
	@Override
	public Vector3D transformPosition(Vector3D p) {
		Vector3D p_f01 = p.add(parameters.translation);
		Vector3D p_f1 = parameters.rotation.applyTo(p_f01);
		return p_f1;
	}

	/**
	 * Transforms a velocity in frame F0 to the frame F1 using:
	 * 
	 * $$ \vec{v}^* = \tilde{R} \left[ \vec{v} + \vec{\omega} \times \vec{r} \right] $$
	 * 
	 * Where:
	 * <ul>
	 * <li>\( \vec{v}^* \) is the Cartesian velocity in the new frame F1.</li>
	 * <li>\( \vec{r} \) is the Cartesian position in the original frame F0.</li>
	 * <li>\( \vec{v} \) is the Cartesian velocity in the original frame F0.</li>
	 * <li>\( \tilde{R} \) is the {@link IRotation} transform part (eg. rotation matrix).</li>
	 * <li>\( \vec{\omega} \) is the rotationalRate of frame F1 relative to F0.</li>
	 * </ul>
	 * 
	 * <p>
	 * Based on: [battin] page 102, eqn 2.52
	 * </p>
	 */
	@Override
	public Vector3D transformVelocity(Vector3D p, Vector3D v) {
		Vector3D cross = parameters.rotationRate.cross(p.add(parameters.translation));
		Vector3D v_f01 = v.add(parameters.velocity).add(cross);
		Vector3D v_f1 = parameters.rotation.applyTo(v_f01);
		return v_f1;
	}
}
