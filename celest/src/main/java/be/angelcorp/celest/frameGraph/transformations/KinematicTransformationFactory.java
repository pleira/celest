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

import be.angelcorp.celest.frameGraph.BasicReferenceFrameTransformFactory;
import be.angelcorp.celest.frameGraph.ReferenceFrameTransform;
import be.angelcorp.celest.frameGraph.ReferenceFrameTransformFactory;
import be.angelcorp.celest.frameGraph.ReferenceSystem;
import be.angelcorp.celest.time.Epoch;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.math.rotation.IRotation;

/**
 * Abstract factory that can produce transforms, once the
 * {@link be.angelcorp.celest.frameGraph.ReferenceFrameTransformFactory#calculateParameters(be.angelcorp.celest.time.Epoch)} is implemented.
 * <p>
 * The input for this factory are the {@link TransformationParameters}, which describe the kinematic
 * relation between the origin and destination frameGraph. From these parameters, a
 * {@link be.angelcorp.celest.frameGraph.ReferenceFrameTransform} is constructed that can perform the actual transformation between the
 * two frameGraph.
 * </p>
 *
 * @param <F0> Origin frame of the produced transformation
 * @param <F1> Destination frame of the produced transformation
 * @author Simon Billemont
 */
public abstract class KinematicTransformationFactory<F0 extends ReferenceSystem, F1 extends ReferenceSystem>
        extends BasicReferenceFrameTransformFactory<F0, F1> {

    /**
     * A special form of the {@link be.angelcorp.celest.frameGraph.ReferenceFrameTransformFactory} which produces the inverse transform
     * given the same {@link TransformationParameters}.
     *
     * @author Simon Billemont
     */
    public class ReferenceFrameTransformInverseFactory extends BasicReferenceFrameTransformFactory<F1, F0> {

        /**
         * Factory that produces non-inverse transforms.
         */
        KinematicTransformationFactory<F0, F1> factory;

        /**
         * Constructs a factory that produces the inverse frame of the provided
         * {@link be.angelcorp.celest.frameGraph.ReferenceFrameTransformFactory}.
         *
         * @param factory Base factory that produces non-inverse transforms.
         */
        public ReferenceFrameTransformInverseFactory(KinematicTransformationFactory<F0, F1> factory) {
            this.factory = factory;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double cost(Epoch epoch) {
            return factory.cost(epoch) + 198; // 3 * 22 vector operations = 3*22*3 operations roughly
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ReferenceFrameTransform<F1, F0> transform(Epoch epoch) {
            // Calculate the non-inverted parameters
            TransformationParameters param = factory.calculateParameters(epoch);

            // Transform to inverted parameters

            // r* = R (r + dr)
            // r* - R dr = R r
            // iR r* - dr = r
            // iR (r* - R dr) = r
            Vector3D inverse_translation = param.rotation.applyTo(param.translation).negate();
            IRotation inverse_orientation = param.rotation.inverse();

            // v* = R [ (v + dv) + w x (r + dr) ]
            // iR v* = (v + dv) + w x (r + dr)
            // iR v* - dv - w x (r + dr) = v
            // iR [ (v* - R dV) - R w x (r + dr) ] = v
            Vector3D inverse_velocity = param.rotation.applyTo(param.velocity).negate();
            Vector3D inverse_orientationRate = param.rotation.applyInverseTo(param.rotationRate.negate());

            Vector3D inverse_accelleration = param.rotation.applyTo(param.acceleration).negate();
            Vector3D inverse_orientationAcelleration =
                    param.rotation.applyInverseTo(param.rotationAcceleration.negate());

            // Wrap the inverted parameters in a TransformationParameters
            TransformationParameters inverted_param = new TransformationParameters(epoch,
                    inverse_translation, inverse_velocity, inverse_accelleration,
                    inverse_orientation, inverse_orientationRate, inverse_orientationAcelleration);

            // Create the transformation
            ReferenceFrameTransform<F1, F0> inverted_transform = new KinematicTransformation<>(this, epoch, inverted_param);
            return inverted_transform;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ReferenceFrameTransformFactory<F0, F1> inverse() {
            return factory;
        }

    }

    /**
     * A wrapper for the transformation parameters required by the {@link ReferenceFrameTransform}
     * transformation. This includes;
     * <p/>
     * <ul>
     * <li>translation [m]</li>
     * <li>velocity [m/s]</li>
     * <li>acceleration [m/s<sup>2</sup>]</li>
     * <li>rotation[rad]</li>
     * <li>rotationRate[rad/s]</li>
     * <li>rotationAcceleration[rad/s<sup>2</sup>]</li>
     * </ul>
     *
     * @author Simon Billemont
     */
    public class TransformationParameters {
        public final Epoch epoch;
        public final Vector3D translation;
        public final Vector3D velocity;
        public final Vector3D acceleration;
        public final IRotation rotation;
        public final Vector3D rotationRate;
        public final Vector3D rotationAcceleration;

        public TransformationParameters(Epoch epoch,
                                        Vector3D translation, Vector3D velocity, Vector3D acceleration,
                                        IRotation rotation, Vector3D rotationRate, Vector3D rotationAcceleration) {
            this.epoch = epoch;
            this.translation = translation;
            this.velocity = velocity;
            this.acceleration = acceleration;
            this.rotation = rotation;
            this.rotationRate = rotationRate;
            this.rotationAcceleration = rotationAcceleration;
        }
    }

    /**
     * Calculate the varius transformation parameters between frame F0 and F1 at a specific epoch.
     *
     * @param date Epoch at which the parameters must be valid.
     * @return A set of {@link TransformationParameters} for the transformation.
     */
    protected abstract TransformationParameters calculateParameters(Epoch date);

    /**
     * {@inheritDoc}
     */
    @Override
    public KinematicTransformation<F0, F1> transform(Epoch epoch) {
        TransformationParameters p = calculateParameters(epoch);
        KinematicTransformation<F0, F1> transform = new KinematicTransformation<>(this, epoch, p);
        return transform;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceFrameTransformInverseFactory inverse() {
        ReferenceFrameTransformInverseFactory inverse_factory = new ReferenceFrameTransformInverseFactory(this);
        return inverse_factory;
    }

}
