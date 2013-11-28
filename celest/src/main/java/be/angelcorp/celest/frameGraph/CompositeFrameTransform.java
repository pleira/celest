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
package be.angelcorp.celest.frameGraph;

import be.angelcorp.celest.state.Orbit;
import be.angelcorp.celest.time.Epoch;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.math.rotation.IRotation;
import scala.Tuple2;
import scala.Tuple3;

/**
 * An {@link ReferenceFrameTransform} that sequentially applies two other {@link ReferenceFrameTransform}'s:
 * <p>
 * $$ x_2 = T_{1 \to 2}( T_{0 \to 1}( x_0 )) $$
 * </p>
 * With;
 * <ul>
 * <li>\(x_a\): A state in reference frame a.</li>
 * <li>\(T_{a \to b}\): The transform of a state from reference frame a to b.</li>
 * </ul>
 *
 * @param <F0> Transform a state defined in this frame.
 * @param <F1> Intermediate reference frame.
 * @param <F2> Transform to a state in this frame.
 * @author Simon Billemont
 */
public class CompositeFrameTransform<F0 extends ReferenceFrame, F1 extends ReferenceFrame, F2 extends ReferenceFrame>
        extends BasicReferenceFrameTransform<F0, F2, CompositeFrameTransformFactory<F0, F1, F2>> {

    /**
     * First transformation to apply; F0 => F1
     */
    private final ReferenceFrameTransform<F0, F1> transform0;
    /**
     * Second transformation to apply; F1 => F2
     */
    private final ReferenceFrameTransform<F1, F2> transform1;

    /**
     * Create a {@link CompositeFrameTransform} from two other known {@link ReferenceFrameTransform}'s.
     *
     * @param factory    A factory capable of producing other {@link CompositeFrameTransform}'s from F0 => F2.
     * @param epoch      The epoch at which this transform is guaranteed to be valid.
     * @param transform0 First transform to apply, F0 => F1.
     * @param transform1 Second transform to apply, F1 => F2.
     */
    public CompositeFrameTransform(CompositeFrameTransformFactory<F0, F1, F2> factory, Epoch epoch,
                                   ReferenceFrameTransform<F0, F1> transform0, ReferenceFrameTransform<F1, F2> transform1) {
        super(factory, epoch);
        this.transform0 = transform0;
        this.transform1 = transform1;
    }

    @Override
    public Orbit transform(Orbit positionState) {
        Orbit positionState_f1 = transform0.transform(positionState);
        Orbit positionState_f2 = transform1.transform(positionState_f1);
        return positionState_f2;
    }

    @Override
    public IRotation transformOrientation(IRotation orientation) {
        IRotation orientation_f1 = transform0.transformOrientation(orientation);
        IRotation orientation_f2 = transform1.transformOrientation(orientation_f1);
        return orientation_f2;
    }

    @Override
    public Vector3D transformPos(Vector3D position) {
        Vector3D position_f1 = transform0.transformPos(position);
        Vector3D position_f2 = transform1.transformPos(position_f1);
        return position_f2;
    }

    @Override
    public Tuple2<Vector3D, Vector3D> transformPosVel(Vector3D position, Vector3D velocity) {
        Tuple2<Vector3D, Vector3D> pv_f1 = transform0.transformPosVel(position, velocity);
        Vector3D position_f1 = pv_f1._1();
        Vector3D velocity_f1 = pv_f1._2();

        Tuple2<Vector3D, Vector3D> pv_f2 = transform1.transformPosVel(position_f1, velocity_f1);
        return pv_f2;
    }

    @Override
    public Tuple3<Vector3D, Vector3D, Vector3D> transformPosVelAcc(Vector3D position, Vector3D velocity, Vector3D acceleration) {
        Tuple3<Vector3D, Vector3D, Vector3D> pva_f1 = transform0.transformPosVelAcc(position, velocity, acceleration);
        Vector3D position_f1 = pva_f1._1();
        Vector3D velocity_f1 = pva_f1._2();
        Vector3D acceleration_f1 = pva_f1._3();

        Tuple3<Vector3D, Vector3D, Vector3D> pva_f2 = transform1.transformPosVelAcc(position_f1, velocity_f1, acceleration_f1);
        return pva_f2;
    }
}
