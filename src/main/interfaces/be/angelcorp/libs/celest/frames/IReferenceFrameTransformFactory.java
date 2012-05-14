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
 * Factory construct capable of creating a {@link IReferenceFrameTransform} between two different
 * {@link IReferenceFrame}'s.
 * 
 * @author Simon Billemont
 * 
 * @param <F0>
 *            Transform from this {@link IReferenceFrame}.
 * @param <F1>
 *            Transform to this {@link IReferenceFrame}.
 */
public interface IReferenceFrameTransformFactory<F0 extends IReferenceFrame<F0>, F1 extends IReferenceFrame<F1>> {

	/**
	 * Rough estimate of the number of operations required to <b>create and apply</b> a
	 * {@link IReferenceFrame}.
	 * 
	 * <p>
	 * This method is used to find optimal (performance wide) transfers between different frames when
	 * multiple paths are possible. It is important to make this method <b>fast</b>. Possible checks
	 * include, checking the existence of cache variable, if all required data files have been loaded,
	 * ...
	 * </p>
	 * 
	 * @param epoch
	 *            Epoch at which to construct and apply a potential transform.
	 * @return Rough cost estimate of creating/applying this a {@link IReferenceFrameTransform}.
	 */
	public abstract double getCost(IJulianDate epoch);

	/**
	 * Get a new transform that, when applied, transforms from {@link F0} to {@link F1}.
	 * 
	 * @param epoch
	 *            Epoch at which the {@link IReferenceFrameTransform} must be valid.
	 * @return An transformation between the specified {@link IReferenceFrame}'s.
	 */
	public abstract IReferenceFrameTransform<F0, F1> getTransform(IJulianDate epoch);

}