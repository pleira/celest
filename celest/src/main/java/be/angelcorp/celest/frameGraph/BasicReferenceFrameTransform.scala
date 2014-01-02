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
 * A common basis class for most ReferenceFrameTransforms. It defines a single epoch at which the
 * ReferenceFrameTransform is valid, and implements basic related to its construction and parent factory.
 *
 * @tparam F0 Transforms from this type of frame.
 * @tparam F1 Transforms to this typ of frame.
 * @tparam T  Type of the factory, used to create new instances of this ReferenceFrameTransform.
 *
 * @author Simon Billemont
 */
trait BasicReferenceFrameTransform[F0 <: ReferenceSystem, F1 <: ReferenceSystem, T <: ReferenceFrameTransformFactory[F0, F1]]
  extends ReferenceFrameTransform[F0, F1] {

  /**
   * Factory used to create this transform, and create transforms for alternative epochs.
   */
  def factory: T

  /**
   * Epoch at which the [[be.angelcorp.celest.frameGraph.BasicReferenceFrameTransform]] is guaranteed to be valid.
   */
  def epoch: Epoch

  /** {@inheritDoc} */
  override def add[F2 <: ReferenceSystem](other: ReferenceFrameTransform[F1, F2]): ReferenceFrameTransform[F0, F2] =
    factory.add(other.factory).transform(epoch)

  /** {@inheritDoc} */
  override def inverse: ReferenceFrameTransform[F1, F0] =
    factory.inverse.transform(epoch)

}
