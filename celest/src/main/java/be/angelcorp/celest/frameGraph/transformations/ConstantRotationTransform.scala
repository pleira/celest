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
import be.angelcorp.celest.math.geometry.{Vec3, Mat3}
import be.angelcorp.celest.math.rotation.Rotation
import be.angelcorp.celest.math.rotation.RotationMatrix._
import be.angelcorp.celest.state.{Orbit, PosVel}
import be.angelcorp.celest.time.Epoch

/**
 * Transformation that consists only of a single rotation between the two respective frameGraph. This transformation neglects
 * any additional angular velocities and accelerations between the two frameGraph.
 *
 * @param M Rotation matrix between from frame F1 => F0.
 * @param epoch Epoch at which the transformation is valid.
 * @param factory Factory that produced this transformation.
 *
 * @tparam F0 Starting frame for this transformation.
 * @tparam F1 Resulting frame after this transformation.
 * @tparam T  Factory that produced this transformation.
 */
class ConstantRotationTransform[F0 <: ReferenceSystem, F1 <: ReferenceSystem, T <: ReferenceFrameTransformFactory[F0, F1]](val M: Mat3, val epoch: Epoch, val factory: T)
  extends BasicReferenceFrameTransform[F0, F1, T] {

  def transform(positionState: Orbit[F0]): Orbit[F1] = {
    val pv = positionState.toPosVel
    new PosVel(M * pv.position, M * pv.velocity, factory.toFrame)
  }

  def transformOrientation(orientation: Rotation): Rotation =
    orientation.applyTo(M)

  def transformPos(position: Vec3): Vec3 =
    M * position

  def transformPosVel(position: Vec3, velocity: Vec3): (Vec3, Vec3) =
    (M * position, M * velocity)

  def transformPosVelAcc(position: Vec3, velocity: Vec3, acceleration: Vec3): (Vec3, Vec3, Vec3) =
    (M * position, M * velocity, M * acceleration)

  override def transformVector(vector: Vec3): Vec3 =
    M * vector

}