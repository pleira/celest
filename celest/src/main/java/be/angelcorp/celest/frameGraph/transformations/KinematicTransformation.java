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
package be.angelcorp.celest.frameGraph.transformations;

import be.angelcorp.celest.frameGraph.BasicReferenceFrameTransform;
import be.angelcorp.celest.frameGraph.ReferenceFrameTransformFactory;
import be.angelcorp.celest.frameGraph.ReferenceSystem;
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem;
import be.angelcorp.celest.state.Orbit;
import be.angelcorp.celest.state.PosVel;
import be.angelcorp.celest.time.Epoch;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.math.rotation.IRotation;
import scala.Tuple2;
import scala.Tuple3;

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
 * <p/>
 * <p>
 * The work in this class is mainly based on:
 * </p>
 * <ul>
 * <li>Richard H. Battin, <b>"An Introduction to the Mathematics and Methods of Astrodynamics"</b>, AIAA
 * Education Series, 1999, ISBN 1563473429</li>
 * </ul>
 *
 * @param <F0> Origin reference frame type.
 * @param <F1> Destination reference frame type.
 * @author Simon Billemont
 */
public class KinematicTransformation<F0 extends ReferenceSystem, F1 extends ReferenceSystem> extends
        BasicReferenceFrameTransform<F0, F1, ReferenceFrameTransformFactory<F0, F1>> {

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
    private final KinematicTransformationFactory.TransformationParameters parameters;

    /**
     * Create a new ReferenceFrameTransform using a given parent factory, capable of creating
     * new {@link be.angelcorp.celest.frameGraph.ReferenceFrameTransform}'s like this one, but for various other epochs. Furthermore a
     * the epoch at which this class is guaranteed to be valid and finally, a set of transformation
     * parameters describing the relation between the origin and destination frameGraph.
     *
     * @param factory    Factory which produced this frame.
     * @param epoch      Epoch at which this transform is valid.
     * @param parameters Parameters describing the transform between the two frameGraph.
     */
    public KinematicTransformation(ReferenceFrameTransformFactory<F0, F1> factory, Epoch epoch,
                                   KinematicTransformationFactory.TransformationParameters parameters) {
        super(factory, epoch);
        this.parameters = parameters;
    }

    /**
     * Returns the internal parameters used to perform the transformation.
     *
     * @return Transformation parameters.
     */
    public KinematicTransformationFactory.TransformationParameters getParameters() {
        return parameters;
    }

    @Override
    public Orbit transform(Orbit positionState) {
        PosVel cartesianElements = positionState.toPosVel();

        Vector3D p_f0 = cartesianElements.position();
        Vector3D v_f0 = cartesianElements.velocity();

        Tuple2<Vector3D, Vector3D> pv_f1 = transformPosVel(p_f0, v_f0);
        Vector3D p_f1 = pv_f1._1();
        Vector3D v_f1 = pv_f1._2();

        final scala.Option<BodyCenteredSystem> none = scala.Option.apply(null);
        PosVel state_f1 = new PosVel(p_f1, v_f1, none);
        return state_f1;
    }

    @Override
    public IRotation transformOrientation(IRotation orientation) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Transforms the position in frame F0 to the frame F1 using:
     * <p/>
     * $$ \vec{r}^* = \tilde{R} \vec{r} $$
     * <p/>
     * Where:
     * <ul>
     * <li>\( \vec{r}^* \) is the Cartesian position in the new frame F1.</li>
     * <li>\( \vec{r} \) is the Cartesian position in the original frame F0.</li>
     * <li>\( \tilde{R} \) is the {@link IRotation} transform part (eg. rotation matrix).</li>
     * </ul>
     * <p/>
     * <p>
     * Based on: [battin] page 101, eqn 2.49
     * </p>
     */
    @Override
    public Vector3D transformPos(Vector3D position) {
        Vector3D p_f01 = position.add(parameters.translation);
        Vector3D p_f1 = parameters.rotation.applyTo(p_f01);
        return p_f1;
    }

    /**
     * Transforms a velocity in frame F0 to the frame F1 using:
     * <p/>
     * $$ \vec{r}^* = \tilde{R} \vec{r} $$
     * $$ \vec{v}^* = \tilde{R} \left[ \vec{v} + \vec{\omega} \times \vec{r} \right] $$
     * <p/>
     * Where:
     * <ul>
     * <li>\( \vec{v}^* \) is the Cartesian velocity in the new frame F1.</li>
     * <li>\( \vec{r} \) is the Cartesian position in the original frame F0.</li>
     * <li>\( \vec{v} \) is the Cartesian velocity in the original frame F0.</li>
     * <li>\( \tilde{R} \) is the {@link IRotation} transform part (eg. rotation matrix).</li>
     * <li>\( \vec{\omega} \) is the rotationalRate of frame F1 relative to F0.</li>
     * </ul>
     * <p/>
     * <p>
     * Based on:
     * [battin] page 101, eqn 2.49
     * [battin] page 102, eqn 2.52
     * </p>
     */
    @Override
    public Tuple2<Vector3D, Vector3D> transformPosVel(Vector3D position, Vector3D velocity) {
        Vector3D p_f01 = position.add(parameters.translation);
        Vector3D p_f1 = parameters.rotation.applyTo(p_f01);

        Vector3D cross = parameters.rotationRate.cross(p_f01);
        Vector3D v_f01 = velocity.add(parameters.velocity).add(cross);
        Vector3D v_f1 = parameters.rotation.applyTo(v_f01);

        return new Tuple2(p_f1, v_f1);
    }

    /**
     * Transforms a velocity in frame F0 to the frame F1 using:
     * <p/>
     * $$ \vec{r}^* = \tilde{R} \vec{r} $$
     * $$ \vec{v}^* = \tilde{R} \left[ \vec{v} + \vec{\omega} \times \vec{r} \right] $$
     * $$ \vec{a}^* = \tilde{R} \left[ \vec{a} + 2 \vec{\omega} \times \vec{v} + \vec{\alpha} \times
     * \vec{r} + \vec{\omega} \times \left( \vec{\omega} \times \vec{r} \right) \right] $$
     * <p/>
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
     * Based on:
     * [battin] page 101, eqn 2.49
     * [battin] page 102, eqn 2.52
     * [battin] page 102, eqn 2.54
     * </p>
     */
    @Override
    public Tuple3<Vector3D, Vector3D, Vector3D> transformPosVelAcc(Vector3D position, Vector3D velocity, Vector3D acceleration) {
        Vector3D p_f01 = position.add(parameters.translation);
        Vector3D p_f1 = parameters.rotation.applyTo(p_f01);

        Vector3D cross = parameters.rotationRate.cross(p_f01);
        Vector3D v_f01 = velocity.add(parameters.velocity).add(cross);
        Vector3D v_f1 = parameters.rotation.applyTo(v_f01);

        // a_observed = \vec{a}
        Vector3D observed = acceleration.add(parameters.acceleration);
        // a_coriolis = 2 \vec{\omega} \times \vec{v}
        Vector3D coriolis = parameters.rotationRate.multiply(2).cross(velocity.add(parameters.velocity));
        // a_euler = \vec{\alpha} \times \vec{r}
        Vector3D euler = parameters.rotationAcceleration.cross(p_f01);
        // a_centripetal = omega \times ( \vec{\omega} \times \vec{r} )
        Vector3D centripetal = parameters.rotationRate.cross(parameters.rotationRate.cross(p_f01));

        // a = R [ a_observed + a_coriolis + a_euler + a_centripetal ]
        Vector3D a_f1 = parameters.rotation.applyTo(observed.add(coriolis).add(euler).add(centripetal));

        return new Tuple3(p_f1, v_f1, a_f1);
    }
}
