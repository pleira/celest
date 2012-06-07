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

import org.jgrapht.WeightedGraph;

import be.angelcorp.libs.celest.time.IJulianDate;

import com.google.common.base.Predicate;

/**
 * Abstract description of what the framegraph should be capable of. This is mainly a front-end for the
 * JgraphT back-end that does all the work. It defines several convenience methods for finding
 * transformations between different frames in the internal graph structure.
 * 
 * @author Simon Billemont
 */
public interface IReferenceFrameGraph {

	/**
	 * Get a JGrapgT graph, that describes, and is directly linked to the framegraph. All methods that
	 * modify the returned graph, must also modify this frame graph structure.
	 * 
	 * @return A JGraphT graph description of this internal graph structure.
	 */
	public abstract WeightedGraph<IReferenceFrame, IReferenceFrameTransformFactory<?, ?>> getGraph();

	/**
	 * Find a transformation between two {@link IReferenceFrame}'s. The source and destination
	 * {@link IReferenceFrame} are identified using a predicate, where the the first frame to match the
	 * predicate is used as either source or destination frame respectively. The generated transformation
	 * is valid for the specified epoch.
	 * 
	 * @param from
	 *            Predicate to find the origin reference frame.
	 * @param to
	 *            Predicate to find the destination reference frame.
	 * @param epoch
	 *            The epoch at which the {@link IReferenceFrameTransform} must be valid.
	 * @return An {@link IReferenceFrameTransform} describing the transformation between the first
	 *         matched frame of the "from" and "to" predicates.
	 * @throws ReferenceFrameTransformationException
	 *             When no frame was found for either source/destination or no transformation chain
	 *             existed between the two frames.
	 */
	public abstract IReferenceFrameTransform<?, ?> getTransform(Predicate<IReferenceFrame> from,
			Predicate<IReferenceFrame> to, IJulianDate epoch) throws ReferenceFrameTransformationException;

	/**
	 * Find a transformation between two known {@link IReferenceFrame}'s.
	 * 
	 * <p>
	 * Note: Both frames need to be attached to this reference frame graph, and a transformation chain
	 * must exist between the two frames in order to yield a valid resulting transformation.
	 * </p>
	 * 
	 * @param from
	 *            Origin reference frame.
	 * @param to
	 *            Destination reference frame.
	 * @param epoch
	 *            The epoch at which the {@link IReferenceFrameTransform} must be valid.
	 * @return An {@link IReferenceFrameTransform} describing the transformation between the two provided
	 *         reference frames.
	 * @throws ReferenceFrameTransformationException
	 *             When no possible transformation chain was found between the two frames.
	 */
	public abstract <T extends IReferenceFrame, V extends IReferenceFrame> IReferenceFrameTransform<T, V>
			getTransform(T from, V to, IJulianDate epoch) throws ReferenceFrameTransformationException;

	/**
	 * Find a factory capable of generating transformations between two {@link IReferenceFrame}'s. The
	 * source and destination {@link IReferenceFrame} are identified using a predicate, where the the
	 * first frame to match the predicate is used as either source or destination frame respectively. The
	 * generated transformation is valid for the specified epoch.
	 * 
	 * @param from
	 *            Predicate to find the origin reference frame.
	 * @param to
	 *            Predicate to find the destination reference frame.
	 * @param epoch
	 *            The epoch at which the {@link IReferenceFrameTransform} must be valid.
	 * @return An {@link IReferenceFrameTransformFactory} capable of producing transformations between
	 *         the first matched frame of the "from" and "to" predicates.
	 * @throws ReferenceFrameTransformationException
	 *             When no frame was found for either source/destination or no transformation chain
	 *             existed between the two frames.
	 */
	public abstract IReferenceFrameTransformFactory<?, ?> getTransformFactory(Predicate<IReferenceFrame> from,
			Predicate<IReferenceFrame> to) throws ReferenceFrameTransformationException;

	/**
	 * Find a factory capable of producing transformations between two known {@link IReferenceFrame}'s.
	 * 
	 * <p>
	 * Note: Both frames need to be attached to this reference frame graph, and a transformation chain
	 * must exist between the two frames in order to yield a valid transformation factory.
	 * </p>
	 * 
	 * @param from
	 *            Origin reference frame.
	 * @param to
	 *            Destination reference frame.
	 * @param epoch
	 *            The epoch at which the {@link IReferenceFrameTransform} must be valid.
	 * @return An {@link IReferenceFrameTransformFactory} capable of producing transformations between
	 *         the two provided reference frames.
	 * @throws ReferenceFrameTransformationException
	 *             When no possible transformation chain was found between the two frames.
	 */
	public abstract <T extends IReferenceFrame, V extends IReferenceFrame> IReferenceFrameTransformFactory<T, V>
			getTransformFactory(T from, V to) throws ReferenceFrameTransformationException;

}