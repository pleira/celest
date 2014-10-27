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
package be.angelcorp.celest.frameGraph.transformations

import be.angelcorp.celest.frameGraph.{BasicReferenceFrameTransform, ReferenceFrameTransformFactory, ReferenceSystem}
import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.math.rotation.Rotation
import be.angelcorp.celest.state.{Orbit, PosVel}
import be.angelcorp.celest.time.Epoch

/**
 * The default implementation for a reference frame transformation, including kinematic effects:
 * <p>
 * $$
 * \begin{array}{rl}
 * \vec{r}^* = & \tilde{R} \vec{r} \\
 * \vec{v}^* = & \tilde{R} \left[ \vec{v} + \vec{\omega} \times \vec{r} \right] \\
 * \vec{a}^* = & \tilde{R} \left[ \vec{a} + 2 \vec{\omega} \times \vec{v} + \vec{\alpha} \times
 * \vec{r} + \vec{\omega} \times \left( \vec{\omega} \times \vec{r} \right) \right]
 * \end{array}
 * $$
 * </p>
 * <p>Where: </p>
 * <ul>
 * <li>\( \vec{r} \) is the Cartesian position in the original frame F0.</li>
 * <li>\( \vec{v} \) is the Cartesian velocity in the original frame F0.</li>
 * <li>\( \vec{a} \) is the Cartesian acceleration in the original frame F0.</li>
 * <li>\( \vec{\box}^* \) is the p/v/a in the new frame F1.</li>
 * <li>\( \tilde{R} \) is the {@link IRotation} transform part (eg. rotation matrix).</li>
 * <li>\( \vec{\omega} \) is the rotationalRate of frame F1 relative to F0.</li>
 * <li>\( \vec{\alpha} \) is the rotationalAcceleration of frame F1 relative to F0.</li>
 * </ul>
 * <p/>
 * <p>
 * A new ReferenceFrameTransform can be created using a given parent factory, capable of creating new `ReferenceFrameTransform`'s
 * like this one, but for various other epochs. Furthermore the epoch at which this class is guaranteed to be valid and
 * finally, a set of transformation parameters describing the relation between the origin and destination frameGraph.
 * </p>
 * <p>
 * The work in this class is mainly based on:
 * </p>
 * <ul>
 * <li>Richard H. Battin, <b>"An Introduction to the Mathematics and Methods of Astrodynamics"</b>, AIAA
 * Education Series, 1999, ISBN 1563473429</li>
 * </ul>
 *
 * @param factory    Factory which produced this frame.
 * @param epoch      Epoch at which this transform is valid.
 * @param parameters Parameters describing the transform between the two frameGraph.
 *
 * @tparam F0 Origin reference frame type.
 * @tparam F1 Destination reference frame type.
 * @author Simon Billemont
 */
class KinematicTransformation[F0 <: ReferenceSystem, F1 <: ReferenceSystem](val factory: ReferenceFrameTransformFactory[F0, F1], val epoch: Epoch, val parameters: TransformationParameters)
  extends BasicReferenceFrameTransform[F0, F1, ReferenceFrameTransformFactory[F0, F1]] {

  override def transform(positionState: Orbit[F0]): Orbit[F1] = {
    val cartesianElements = positionState.toPosVel

    val p_f0 = cartesianElements.position
    val v_f0 = cartesianElements.velocity

    val pv_f1 = transformPosVel(p_f0, v_f0)
    val p_f1 = pv_f1._1
    val v_f1 = pv_f1._2

    new PosVel(p_f1, v_f1, factory.toFrame)
  }

  override def transformOrientation(orientation: Rotation): Rotation =
    throw new UnsupportedOperationException("Not implemented yet")

  /**
   * Transforms the position in frame F0 to the frame F1 using:
   * <p/>
   * $$ \vec{r}^* = \tilde{R} \vec{r} $$
   * <p/>
   * Where:
   * <ul>
   * <li>\( \vec{r}^* \) is the Cartesian position in the new frame F1.</li>
   * <li>\( \vec{r} \) is the Cartesian position in the original frame F0.</li>
   * <li>\( \tilde{R} \) is the {@link IRotation} transform part (eg. rotation matrix).</li>
   * </ul>
   * <p/>
   * <p>
   * Based on: [battin] page 101, eqn 2.49
   * </p>
   */
  override def transformPos(position: Vec3) = {
    val p_f01 = position + parameters.translation
    parameters.rotation.applyTo(p_f01)
  }

  /**
   * Transforms a velocity in frame F0 to the frame F1 using:
   * <p/>
   * $$ \vec{r}^* = \tilde{R} \vec{r} $$
   * $$ \vec{v}^* = \tilde{R} \left[ \vec{v} + \vec{\omega} \times \vec{r} \right] $$
   * <p/>
   * Where:
   * <ul>
   * <li>\( \vec{v}^* \) is the Cartesian velocity in the new frame F1.</li>
   * <li>\( \vec{r} \) is the Cartesian position in the original frame F0.</li>
   * <li>\( \vec{v} \) is the Cartesian velocity in the original frame F0.</li>
   * <li>\( \tilde{R} \) is the {@link IRotation} transform part (eg. rotation matrix).</li>
   * <li>\( \vec{\omega} \) is the rotationalRate of frame F1 relative to F0.</li>
   * </ul>
   * <p/>
   * <p>
   * Based on:
   * [battin] page 101, eqn 2.49
   * [battin] page 102, eqn 2.52
   * </p>
   */
  override def transformPosVel(position: Vec3, velocity: Vec3) = {
    val p_f01 = position + parameters.translation
    val p_f1 = parameters.rotation.applyTo(p_f01)

    val cross = parameters.rotationRate.cross(p_f01)
    val v_f01 = velocity + parameters.velocity + cross
    val v_f1 = parameters.rotation.applyTo(v_f01)

    (p_f1, v_f1)
  }

  /**
   * Transforms a velocity in frame F0 to the frame F1 using:
   * <p/>
   * $$ \vec{r}^* = \tilde{R} \vec{r} $$
   * $$ \vec{v}^* = \tilde{R} \left[ \vec{v} + \vec{\omega} \times \vec{r} \right] $$
   * $$ \vec{a}^* = \tilde{R} \left[ \vec{a} + 2 \vec{\omega} \times \vec{v} + \vec{\alpha} \times
   * \vec{r} + \vec{\omega} \times \left( \vec{\omega} \times \vec{r} \right) \right] $$
   * <p/>
   * Where:
   * <ul>
   * <li>\( \vec{a}^* \) is the Cartesian acceleration in the new frame F1.</li>
   * <li>\( \vec{r} \) is the Cartesian position in the original frame F0.</li>
   * <li>\( \vec{v} \) is the Cartesian velocity in the original frame F0.</li>
   * <li>\( \vec{a} \) is the Cartesian acceleration in the original frame F0.</li>
   * <li>\( \tilde{R} \) is the {@link IRotation} transform part (eg. rotation matrix).</li>
   * <li>\( \vec{\omega} \) is the rotationalRate of frame F1 relative to F0.</li>
   * <li>\( \vec{\alpha} \) is the rotationalAcceleration of frame F1 relative to F0.</li>
   * </ul>
   * <p>
   * Based on:
   * [battin] page 101, eqn 2.49
   * [battin] page 102, eqn 2.52
   * [battin] page 102, eqn 2.54
   * </p>
   */
  override def transformPosVelAcc(position: Vec3, velocity: Vec3, acceleration: Vec3): (Vec3, Vec3, Vec3) = {
    val p_f01 = position + parameters.translation
    val p_f1 = parameters.rotation.applyTo(p_f01)

    val cross = parameters.rotationRate.cross(p_f01)
    val v_f01 = velocity + parameters.velocity + cross
    val v_f1 = parameters.rotation.applyTo(v_f01)

    // a_observed = \vec{a}
    val observed = acceleration + parameters.acceleration
    // a_coriolis = 2 \vec{\omega} \times \vec{v}
    val coriolis = (parameters.rotationRate * 2).cross(velocity + parameters.velocity)
    // a_euler = \vec{\alpha} \times \vec{r}
    val euler = parameters.rotationAcceleration.cross(p_f01)
    // a_centripetal = omega \times ( \vec{\omega} \times \vec{r} )
    val centripetal = parameters.rotationRate.cross(parameters.rotationRate.cross(p_f01))

    // a = R [ a_observed + a_coriolis + a_euler + a_centripetal ]
    val a_f1 = parameters.rotation.applyTo(observed + coriolis + euler + centripetal)

    (p_f1, v_f1, a_f1)
  }

  override def transformVector(vector: Vec3): Vec3 = {
    parameters.rotation.applyTo(vector)
  }

}
