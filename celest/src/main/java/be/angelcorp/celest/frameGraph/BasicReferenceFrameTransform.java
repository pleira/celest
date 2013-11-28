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

import be.angelcorp.celest.time.Epoch;

/**
 * A common basis class for most ReferenceFrameTransforms. It defines a single epoch at which the
 * ReferenceFrameTransform is valid, and implements basic related to its construction and parent factory.
 *
 * @author Simon Billemont
 * @tparam F0 Transforms from this type of frame.
 * @tparam F1 Transforms to this typ of frame.
 * @tparam T  Type of the factory, used to create new instances of this ReferenceFrameTransform.
 */
public abstract class BasicReferenceFrameTransform<F0 extends ReferenceFrame, F1 extends ReferenceFrame, T extends ReferenceFrameTransformFactory<F0, F1>>
        implements ReferenceFrameTransform<F0, F1> {

    /**
     * Factory used to create this transform, and create transforms for alternative epochs.
     */
    protected final T factory;
    /**
     * Epoch at which the {@link BasicReferenceFrameTransform} is guaranteed to be valid.
     */
    protected final Epoch epoch;

    /**
     * Construct a {@link BasicReferenceFrameTransform} using a given parent factory, and an epoch at
     * which this transform is valid.
     *
     * @param factory Factory used to create this transform, and create transforms for alternative epochs.
     * @param epoch   Epoch at which the {@link BasicReferenceFrameTransform} is guaranteed to be valid.
     */
    public BasicReferenceFrameTransform(T factory, Epoch epoch) {
        this.factory = factory;
        this.epoch = epoch;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <F2 extends ReferenceFrame> ReferenceFrameTransform<F0, F2> add(ReferenceFrameTransform<F1, F2> other) {
        ReferenceFrameTransformFactory<F0, F2> newFactory = factory.add(other.factory());
        ReferenceFrameTransform<F0, F2> transform = newFactory.transform(epoch);
        return transform;
    }

    /**
     * The epoch used to instantiate this transform.
     *
     * @return Epoch at which the {@link BasicReferenceFrameTransform} is guaranteed to be valid.
     * @see BasicReferenceFrameTransform#epoch
     */
    public Epoch epoch() {
        return epoch;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T factory() {
        return factory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceFrameTransform<F1, F0> inverse() {
        ReferenceFrameTransformFactory<F1, F0> inverseFactory = factory().inverse();
        ReferenceFrameTransform<F1, F0> inverse = inverseFactory.transform(epoch());
        return inverse;
    }

}
