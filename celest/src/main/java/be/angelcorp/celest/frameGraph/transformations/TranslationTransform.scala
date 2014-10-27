/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
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

package be.angelcorp.celest.frameGraph.transformations

import be.angelcorp.celest.frameGraph.{BasicReferenceFrameTransform, ReferenceFrameTransformFactory, ReferenceSystem}
import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.math.rotation.Rotation
import be.angelcorp.celest.state.{PosVel, Orbit}
import be.angelcorp.celest.time.Epoch

/**
 * Transformation that consists only of a single translation between the two respective frameGraph frames.
 *
 * @param dx Positional offset to add to F0 coordinates to get to F1 coordinates.
 * @param epoch Epoch at which the transformation is valid.
 * @param factory Factory that produced this transformation.
 *
 * @tparam F0 Starting frame for this transformation.
 * @tparam F1 Resulting frame after this transformation.
 * @tparam T  Factory that produced this transformation.
 */
class TranslationTransform[F0 <: ReferenceSystem, F1 <: ReferenceSystem, T <: ReferenceFrameTransformFactory[F0, F1]](val dx: Vec3, val epoch: Epoch, val factory: T)
  extends BasicReferenceFrameTransform[F0, F1, T] {

  def transform(positionState: Orbit[F0]): Orbit[F1] = {
    val pv = positionState.toPosVel
    new PosVel(pv.position + dx, pv.velocity, factory.toFrame)
  }

  def transformOrientation(orientation: Rotation): Rotation =
    orientation

  def transformPos(position: Vec3): Vec3 =
    position + dx

  def transformPosVel(position: Vec3, velocity: Vec3): (Vec3, Vec3) =
    (position + dx, velocity)

  def transformPosVelAcc(position: Vec3, velocity: Vec3, acceleration: Vec3): (Vec3, Vec3, Vec3) =
    (position + dx, velocity, acceleration)

  override def transformVector(vector: Vec3): Vec3 =
    vector

}