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

import be.angelcorp.libs.celest.time.IJulianDate;

/**
 * A common basis class for most {@link IReferenceFrameTransform}'s. It defines a sinlge epoch at which
 * the {@link IReferenceFrameTransform} is valid, and implements basic related to its construction and
 * parent factory.
 * 
 * @author Simon Billemont
 * 
 * @param <F0>
 *            Transforms from this type of frame.
 * @param <F1>
 *            Transforms to this typ of frame.
 * @param <T>
 *            Type of the factory, used to create new instances of this {@link IReferenceFrameTransform}.
 */
public abstract class BasicReferenceFrameTransform<F0 extends IReferenceFrame, F1 extends IReferenceFrame, T extends IReferenceFrameTransformFactory<F0, F1>>
		implements IReferenceFrameTransform<F0, F1> {

	/** Factory used to create this transform, and create transforms for alternative epochs. */
	protected final T			factory;
	/** Epoch at which the {@link BasicReferenceFrameTransform} is guaranteed to be valid. */
	protected final IJulianDate	epoch;

	/**
	 * Construct a {@link BasicReferenceFrameTransform} using a given parent factory, and an epoch at
	 * which this transform is valid.
	 * 
	 * @param factory
	 *            Factory used to create this transform, and create transforms for alternative epochs.
	 * @param epoch
	 *            Epoch at which the {@link BasicReferenceFrameTransform} is guaranteed to be valid.
	 */
	public BasicReferenceFrameTransform(T factory, IJulianDate epoch) {
		this.factory = factory;
		this.epoch = epoch;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <F2 extends IReferenceFrame> IReferenceFrameTransform<F0, F2> add(IReferenceFrameTransform<F1, F2> other) {
		IReferenceFrameTransformFactory<F0, F2> newFactory = factory.add(other.getFactory());
		IReferenceFrameTransform<F0, F2> transform = newFactory.getTransform(epoch);
		return transform;
	}

	/**
	 * The epoch used to instantiate this transform.
	 * 
	 * @return
	 *         Epoch at which the {@link BasicReferenceFrameTransform} is guaranteed to be valid.
	 * @see BasicReferenceFrameTransform#epoch
	 */
	public IJulianDate getEpoch() {
		return epoch;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getFactory() {
		return factory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IReferenceFrameTransform<F1, F0> inverse() {
		IReferenceFrameTransformFactory<F1, F0> inverseFactory = getFactory().inverse();
		IReferenceFrameTransform<F1, F0> inverse = inverseFactory.getTransform(getEpoch());
		return inverse;
	}

}
