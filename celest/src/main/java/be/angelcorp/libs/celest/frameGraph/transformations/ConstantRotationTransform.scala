/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
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

package be.angelcorp.libs.celest.frameGraph.transformations

import be.angelcorp.libs.celest.state.orientationState.{OrientationState, IOrientationState}
import be.angelcorp.libs.celest.state.positionState._
import be.angelcorp.libs.math.linear.{Matrix3D, Vector3D}
import be.angelcorp.libs.math.rotation.{RotationMatrix, IRotation}
import be.angelcorp.libs.celest.time.Epoch
import be.angelcorp.libs.celest.state.{Orbit, PosVel}
import be.angelcorp.libs.celest.frameGraph.{BasicReferenceFrameTransform, ReferenceFrameTransformFactory, ReferenceFrame}

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
class ConstantRotationTransform[ F0 <: ReferenceFrame, F1 <: ReferenceFrame, T <: ReferenceFrameTransformFactory[F0, F1] ](val M: RotationMatrix, epoch: Epoch, factory: T)
  extends BasicReferenceFrameTransform[F0, F1, T](factory, epoch) {

  def transform(positionState: Orbit): Orbit = {
    val pv = positionState.toPosVel
    new PosVel( M !* pv.position, M !* pv.velocity )
  }

  def transformOrientation(orientation: IRotation): IRotation =
    orientation.applyTo( M )

  def transformPos(position: Vector3D): Vector3D =
     M !* position

  def transformPosVel(position: Vector3D, velocity: Vector3D): (Vector3D, Vector3D) =
    (M !* position, M !* velocity)

  def transformPosVelAcc(position: Vector3D, velocity: Vector3D, acceleration: Vector3D): (Vector3D, Vector3D, Vector3D) =
    (M !* position, M !* velocity, M !* acceleration)

}