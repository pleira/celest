/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.celest.frameGraph

import be.angelcorp.celest.time.Epoch

/**
 * Abstract description a collection of interlinked frameGraph.
 * <p>
 * The ReferenceFrameGraph is used to find transforms between any reference frame that was
 * registered to it. This way, uses do not need to know all the individual transforms required to
 * transform between any of the frameGraph, even if they require multiple complex transformations.
 * </p>
 * <p>
 * There are two flavors to get a ReferenceFrameTransformFactory or ReferenceFrameTransform. The first
 * is when the actual source/destination frame instance is known, the other is using a predicate to
 * identify the specific frameGraph.
 * </p>
 *
 * <p>
 * Note: The first frame to match either the origin/destination predicate is used.
 * </p>
 *
 * @author Simon Billemont
 */
trait ReferenceFrameGraph {

  /**
   * Attach a new frame instance to this frame graph. If the frame already exists, no duplicate of the
   * frame is attached (the same instance can only be attached once).
   *
   * @param frame Frame to attach to this graph.
   */
  def attachFrame(frame: ReferenceSystem)

  /**
   * Attach a transform between two existing ReferenceFrames in this graph.
   * <p>
   * If either frame does not exist in the frame graph, no action is performed.<br/>
   * If the same transform instance was already connected to the graph, its origin/destination frame
   * will be replaced by the passed frameGraph.
   * </p>
   *
   * @param frame1    Origin reference frame of the transformation.
   * @param frame2    Destination reference frame of the transformation.
   * @param transform Transformation factory to to produce transforms between the origin and destination frame.
   */
  def attachTransform[F1 <: ReferenceSystem, F2 <: ReferenceSystem](frame1: F1, frame2: F2, transform: ReferenceFrameTransformFactory[F1, F2])

  /**
   * Find the first frame from all the connected ReferenceFrames that match a specified
   * predicate. If none of the frameGraph match, null is returned.
   *
   * @param frame_predicate Predicate to identify a specific frame.
   * @return The first frame to match the given predicate or else null.
   */
  def findReferenceFrame(frame_predicate: ReferenceSystem => Boolean): Option[ReferenceSystem]

  /**
   * Search for all the ReferenceFrameTransformFactories connected to the provided frame
   * instance. If the frame was not attached to this graph, then an empty iterator is returned.
   *
   * @param frame Find all factories to connected to this frame instance.
   * @return Iterator over all connected factories.
   */
  def findReferenceFrameTransforms(frame: ReferenceSystem): Iterable[ReferenceFrameTransformFactory[_, _]]

  /**
   * Searches for the first frame to match the given predicate. After this, an iterator over all the
   * IReferenceFrameTransformFactories connected to the found frame are returned. If the frame
   * was not found in this graph, then an empty iterator is returned.
   *
   * @param frame_predicate Find all factories to connected to the first matching frame with this predicate.
   * @return Iterator over all connected factories.
   */
  def findReferenceFrameTransforms(frame_predicate: ReferenceSystem => Boolean): Iterable[ReferenceFrameTransformFactory[_, _]]

  /**
   * Returns an iterator that iterates over all the registered ReferenceFrames.
   *
   * @return Iterator for all the frameGraph in this graph.
   */
  def getReferenceFrames: Iterable[ReferenceSystem]

  /**
   * Find a transformation between two ReferenceFrames. The source and destination ReferenceFrame are identified
   * using a predicate, where the the first frame to match the predicate is used as either source or destination
   * frame respectively. The generated transformation is valid for the specified epoch.
   *
   * @param from  Predicate to find the origin reference frame.
   * @param to    Predicate to find the destination reference frame.
   * @param epoch The epoch at which the ReferenceFrameTransform must be valid.
   * @return An ReferenceFrameTransform describing the transformation between the first matched frame of the "from" and "to" predicates.
   */
  def getTransform(from: ReferenceSystem => Boolean, to: ReferenceSystem => Boolean, epoch: Epoch): Option[ReferenceFrameTransform[_, _]]

  /**
   * Find a transformation between two known ReferenceFrames.
   *
   * <p>
   * Note: Both frameGraph need to be attached to this reference frame graph, and a transformation chain
   * must exist between the two frameGraph in order to yield a valid resulting transformation.
   * </p>
   *
   * @param from  Origin reference frame.
   * @param to    Destination reference frame.
   * @param epoch The epoch at which the ReferenceFrameTransform must be valid.
   * @return An ReferenceFrameTransform describing the transformation between the two provided reference frameGraph.
   */
  def getTransform[F <: ReferenceSystem, T <: ReferenceSystem](from: F, to: T, epoch: Epoch): Option[ReferenceFrameTransform[F, T]]

  /**
   * Find a factory capable of generating transformations between two ReferenceFrames. The source and destination
   * ReferenceFrame are identified using a predicate, where the the first frame to match the predicate is used as
   * either source or destination frame respectively. The generated transformation is valid for the specified epoch.
   *
   * @param from  Predicate to find the origin reference frame.
   * @param to    Predicate to find the destination reference frame.
   * @return An ReferenceFrameTransformFactory capable of producing transformations between the first matched frame of
   *         the "from" and "to" predicates.
   */
  def getTransformFactory(from: ReferenceSystem => Boolean, to: ReferenceSystem => Boolean): Option[ReferenceFrameTransformFactory[_, _]]

  /**
   * Find a factory capable of producing transformations between two known ReferenceFrames.
   *
   * <p>
   * Note: Both frameGraph need to be attached to this reference frame graph, and a transformation chain
   * must exist between the two frameGraph in order to yield a valid transformation factory.
   * </p>
   *
   * @param from  Origin reference frame.
   * @param to    Destination reference frame.
   * @return A ReferenceFrameTransformFactory capable of producing transformations between the two provided reference frameGraph.
   */
  def getTransformFactory[F <: ReferenceSystem, T <: ReferenceSystem](from: F, to: T): Option[ReferenceFrameTransformFactory[F, T]]

}
