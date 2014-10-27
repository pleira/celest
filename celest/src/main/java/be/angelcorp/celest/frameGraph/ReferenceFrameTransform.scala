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

import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.math.rotation.Rotation
import be.angelcorp.celest.state.Orbit

/**
 * Transform between different ReferenceFrames.
 *
 * @tparam F0 Transform from this ReferenceFrame.
 * @tparam F1 Transform to this ReferenceFrame.
 *
 * @author Simon Billemont
 */
trait ReferenceFrameTransform[F0 <: ReferenceSystem, F1 <: ReferenceSystem] {

  /**
   * Apply another ReferenceFrameTransform on top of this transform, so that the resulting
   * transformation is a new transform which starts at the same frame as the staring frame of this
   * transform, and results in the final frame of the given transformation.
   *
   * @param other ReferenceFrameTransform to add to this frame.
   * @return A ReferenceFrameTransform from F0 to F2.
   */
  def add[F2 <: ReferenceSystem](other: ReferenceFrameTransform[F1, F2]): ReferenceFrameTransform[F0, F2]

  /**
   * Get a factory capable of creating this type of transform under alternative conditions.
   *
   * @return A factory for this type of ReferenceFrameTransform.
   */
  def factory: ReferenceFrameTransformFactory[F0, F1]

  /**
   * Get a new transform that, when applied, undoes the effects performed by this transformation. This means:
   *
   * <pre>
   * v = this.inverse().transform(this.transform(v))
   * </pre>
   *
   * @return An inverse transformation of this ReferenceFrameTransform.
   */
  def inverse: ReferenceFrameTransform[F1, F0]

  /**
   * Transform an orbit into the new ReferenceFrame.
   *
   * @param positionState Position to transform.
   * @return A new orbit which is equivalent to given orbit, but in the new ReferenceFrame.
   */
  def transform(positionState: Orbit[F0]): Orbit[F1]


  ///**
  // * Transform a {@link IPositionStateDerivative} into a new {@link IReferenceFrame}.
  // *
  // * @param positionState The position in frame F0.
  // * @param positionStateDerivative Position derivatives in frame F0 to transform.
  // * @return A new {@link IPositionStateDerivative} which is equivalent to given
  // *         {@link IPositionStateDerivative}, but in the new {@link IReferenceFrame}.
  // */
  //def transform(positionState: Orbit, positionStateDerivative: IPositionStateDerivative): IPositionStateDerivative

  /**
   * Transform a vector into the new ReferenceFrame.
   *
   * @param vector A vector to transform.
   * @return A vector which is equivalent to given vector, but in the new ReferenceFrame (only rotation is applied).
   */
  def transformVector(vector: Vec3): Vec3

  /**
   * Transform only the rotation of a body into a new ReferenceFrame.
   *
   * @param orientation Orientation to transform.
   * @return A new orientation which is equivalent to given provided orientation, but in the new ReferenceFrame.
   */
  def transformOrientation(orientation: Rotation): Rotation

  /**
   * Transform only the position of a body into a new `IReferenceFrame`.
   *
   * \begin{array}{rll}
   * position = & \{ x, y, z \} & [m] \\
   * \end{array}
   *
   * @param position Position before transform (in frame F0).
   * @return A new position vector which is equivalent to given provided vector, but in the new ReferenceFrame (frame F1).
   */
  def transformPos(position: Vec3): Vec3

  /**
   * Transform the position and velocity of a body into a new ReferenceFrame.
   *
   * \begin{array}{rll}
   * position = & \{ x, y, z \} & [m] \\
   * velocity = & \{ \dot{x}, \dot{y}, \dot{z} \} & [m/s]
   * \end{array}
   *
   * @param position  Position before transform (in frame F0).
   * @param velocity  Velocity before transform (in frame F0).
   * @return A new position and velocity vector which is equivalent to given provided vector,
   *         but in the new ReferenceFrame (frame F1).
   */
  def transformPosVel(position: Vec3, velocity: Vec3): (Vec3, Vec3)

  /**
   * Transform the position, velocity and acceleration of a body into a new ReferenceFrame.
   *
   * \begin{array}{rll}
   * position = & \{ x, y, z \} & [m] \\
   * velocity = & \{ \dot{x}, \dot{y}, \dot{z} \} & [m/s] \\
   * acceleration = & \{ \ddot{x}, \ddot{y}, \ddot{z} \} & [m/s²]
   * \end{array}
   *
   * @param position      Position before transform (in frame F0).
   * @param velocity      Velocity before transform (in frame F0).
   * @param acceleration  Acceleration before transform (in frame F0).
   * @return A new position, velocity, and acceleration vector which is equivalent to given provided vector,
   *         but in the new ReferenceFrame (frame F1).
   */
  def transformPosVelAcc(position: Vec3, velocity: Vec3, acceleration: Vec3): (Vec3, Vec3, Vec3)

}