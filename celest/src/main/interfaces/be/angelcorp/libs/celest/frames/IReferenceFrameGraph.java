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

import java.util.Iterator;

import be.angelcorp.libs.celest.time.Epoch;

import com.google.common.base.Predicate;

/**
 * Abstract description a collection of interlinked frames.
 * <p>
 * The {@link IReferenceFrameGraph} is used to find transforms between any reference frame that was
 * registered to it. This way, uses do not need to know all the individual transforms required to
 * transform between any of the frames, even if they require multiple complex transformations.
 * </p>
 * <p>
 * There are two flavors to get a {@link IReferenceFrameTransformFactory} or
 * {@link IReferenceFrameTransform}. The first is when the actual source/destination frame instance is
 * known, the other is using a {@link Predicate} to identify the specific frames.
 * </p>
 * <p>
 * Note: The first frame to match either the origin/destination {@link Predicate} is used.
 * </p>
 * 
 * @author Simon Billemont
 */
public interface IReferenceFrameGraph {

	/**
	 * Attach a new frame instance to this frame graph. If the frame already exists, no duplicate of the
	 * frame is attached (the same instance can only be attached once).
	 * 
	 * @param frame
	 *            Frame to attach to this graph.
	 */
	public abstract void attachFrame(IReferenceFrame frame);

	/**
	 * Attach a transform between two existing {@link IReferenceFrame}'s in this graph.
	 * <p>
	 * If either frame does not exist in the frame graph, no action is performed.<br/>
	 * If the same transform instance was already connected to the graph, its origin/destination frame
	 * will be replaced by the passed frames.
	 * </p>
	 * 
	 * @param frame1
	 *            Origin reference frame of the transformation.
	 * @param frame2
	 *            Destination reference frame of the transformation.
	 * @param transform
	 *            Transformation factory to to produce transforms between the origin and destination
	 *            frame.
	 */
	public abstract <F1 extends IReferenceFrame, F2 extends IReferenceFrame> void attachTransform(
			F1 frame1, F2 frame2, IReferenceFrameTransformFactory<F1, F2> transform);

	/**
	 * Find the first frame from all the connected {@link IReferenceFrame}'s that match a specified
	 * {@link Predicate}. If none of the frames match, null is returned.
	 * 
	 * @param frame_predicate
	 *            {@link Predicate} to identify a specific frame.
	 * @return The first frame to match the given predicate or else null.
	 */
	public abstract IReferenceFrame findReferenceFrame(Predicate<IReferenceFrame> frame_predicate);

	/**
	 * Search for all the {@link IReferenceFrameTransformFactory}'s connected to the provided frame
	 * instance. If the frame was not attached to this graph, then an empty iterator is returned.
	 * 
	 * @param frame
	 *            Find all factories to connected to this frame instance.
	 * @return Iterator over all connected factories.
	 */
	public abstract Iterator<IReferenceFrameTransformFactory<?, ?>> findReferenceFrameTransforms(IReferenceFrame frame);

	/**
	 * Searches for the first frame to match the given predicate. After this, an iterator over all the
	 * {@link IReferenceFrameTransformFactory}'s connected to the found frame are returned. If the frame
	 * was not found in this graph, then an empty iterator is returned.
	 * 
	 * @param frame_predicate
	 *            Find all factories to connected to the first matching frame with this predicate.
	 * @return Iterator over all connected factories.
	 */
	public abstract Iterator<IReferenceFrameTransformFactory<?, ?>> findReferenceFrameTransforms(
			Predicate<IReferenceFrame> frame_predicate);

	/**
	 * Returns an iterator that iterates over all the registered {@link IReferenceFrame}'s.
	 * 
	 * @return Iterator for all the frames in this graph.
	 */
	public abstract Iterator<IReferenceFrame> getReferenceFrames();

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
			Predicate<IReferenceFrame> to, Epoch epoch) throws ReferenceFrameTransformationException;

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
			getTransform(T from, V to, Epoch epoch) throws ReferenceFrameTransformationException;

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
	 * @return An {@link IReferenceFrameTransformFactory} capable of producing transformations between
	 *         the two provided reference frames.
	 * @throws ReferenceFrameTransformationException
	 *             When no possible transformation chain was found between the two frames.
	 */
	public abstract <T extends IReferenceFrame, V extends IReferenceFrame> IReferenceFrameTransformFactory<T, V>
			getTransformFactory(T from, V to) throws ReferenceFrameTransformationException;

}
