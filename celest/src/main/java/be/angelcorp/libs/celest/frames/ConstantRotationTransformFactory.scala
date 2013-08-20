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

package be.angelcorp.libs.celest.frames

import be.angelcorp.libs.celest.state.orientationState.{OrientationState, IOrientationState}
import be.angelcorp.libs.celest.state.positionState._
import be.angelcorp.libs.math.linear.{Matrix3D, Vector3D}
import be.angelcorp.libs.math.rotation.{RotationMatrix, IRotation}
import be.angelcorp.libs.celest.time.IJulianDate
import be.angelcorp.libs.celest.state.{Orbit, PosVel}

trait ConstantRotationTransformFactory[ F0 <: IReferenceFrame, F1 <: IReferenceFrame ]
  extends BasicReferenceFrameTransformFactory[F0, F1] {

  /**
   * Find the transformation between frame F0 and F1 at the specified epoch in the form of a pure rotation matrix.
   * @param epoch Epoch for the frame transformation.
   * @return Rotation matrix to transform from F0 to F1.
   */
  def rotationMatrix(epoch: IJulianDate): Matrix3D

  /**
   * Factory that generates the inverse transformation from F1 => F0 by inverting the rotation matrix (= transpose)
   */
  class InverseConstantRotationTransformFactory extends BasicReferenceFrameTransformFactory[F1, F0] {
    def getCost(epoch: IJulianDate) = ConstantRotationTransformFactory.this.getCost(epoch)

    def getTransform(epoch: IJulianDate) =
      new ConstantRotationTransform[F1, F0, InverseConstantRotationTransformFactory]( rotationMatrix(epoch).transpose(), epoch, this )

    def inverse() = ConstantRotationTransformFactory.this
  }

  def inverse() = new InverseConstantRotationTransformFactory

  def getTransform(epoch: IJulianDate) =
    new ConstantRotationTransform[F0, F1, ConstantRotationTransformFactory[F0, F1]]( rotationMatrix(epoch), epoch, this )

}

/**
 * Transformation that consists only of a single rotation between the two respective frames. This transformation neglects
 * any additional angular velocities and accelerations between the two frames.
 *
 * @param M Rotation matrix between from frame F1 => F0.
 * @param epoch Epoch at which the transformation is valid.
 * @param factory Factory that produced this transformation.
 *
 * @tparam F0 Starting frame for this transformation.
 * @tparam F1 Resulting frame after this transformation.
 * @tparam T  Factory that produced this transformation.
 */
class ConstantRotationTransform[ F0 <: IReferenceFrame, F1 <: IReferenceFrame, T <: IReferenceFrameTransformFactory[F0, F1] ](val M: RotationMatrix, epoch: IJulianDate, factory: T)
  extends BasicReferenceFrameTransform[F0, F1, T](factory, epoch) {

  def transform(orientationState: IOrientationState) =
    new OrientationState( orientationState.getRotation.applyTo(M), M !* orientationState.getRotationRate)

  def transform(positionState: Orbit) = {
    val pv = positionState.toPosVel
    new PosVel( M !* pv.position, M !* pv.velocity )
  }

  def transform(positionState: Orbit, positionStateDerivative: IPositionStateDerivative) = {
    val va = positionStateDerivative.toCartesianDerivative
    new CartesianDerivative( M !* va.getV, M !* va.getA )
  }

  def transformAcceleration(position: Vector3D, velocity: Vector3D, acceleration: Vector3D) =
    M !* acceleration

  def transformOrientation(orientation: IRotation) =
    orientation.applyTo( M )

  def transformOrientationRate(orientation: IRotation, orientationRate: Vector3D) =
    M !* orientationRate

  def transformPosition(position: Vector3D) =
    M !* position

  def transformVelocity(position: Vector3D, velocity: Vector3D) =
    M !* velocity

}