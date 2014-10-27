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
import be.angelcorp.celest.time.Epoch

/**
 * An {@link ReferenceFrameTransform} that sequentially applies two other {@link ReferenceFrameTransform}'s:
 * <p>
 * $$ x_2 = T_{1 \to 2}( T_{0 \to 1}( x_0 )) $$
 * </p>
 * With;
 * <ul>
 * <li>\(x_a\): A state in reference frame a.</li>
 * <li>\(T_{a \to b}\): The transform of a state from reference frame a to b.</li>
 * </ul>
 *
 * @param factory    A factory capable of producing other [[be.angelcorp.celest.frameGraph.CompositeFrameTransform]]'s from F0 => F2.
 * @param epoch      The epoch at which this transform is guaranteed to be valid.
 * @param transform0 First transform to apply, F0 => F1.
 * @param transform1 Second transform to apply, F1 => F2.
 *
 * @tparam F0 Transform a state defined in this frame.
 * @tparam F1 Intermediate reference frame.
 * @tparam F2 Transform to a state in this frame.
 * @author Simon Billemont
 */
class CompositeFrameTransform[F0 <: ReferenceSystem, F1 <: ReferenceSystem, F2 <: ReferenceSystem](val factory: CompositeFrameTransformFactory[F0, F1, F2], val epoch: Epoch, val transform0: ReferenceFrameTransform[F0, F1], val transform1: ReferenceFrameTransform[F1, F2])
  extends BasicReferenceFrameTransform[F0, F2, CompositeFrameTransformFactory[F0, F1, F2]] {

  override def transform(positionState: Orbit[F0]): Orbit[F2] = {
    val positionState_f1 = transform0.transform(positionState)
    transform1.transform(positionState_f1)
  }

  override def transformOrientation(orientation: Rotation): Rotation = {
    val orientation_f1 = transform0.transformOrientation(orientation)
    transform1.transformOrientation(orientation_f1)
  }

  override def transformPos(position: Vec3): Vec3 = {
    val position_f1 = transform0.transformPos(position)
    transform1.transformPos(position_f1)
  }

  override def transformPosVel(position: Vec3, velocity: Vec3): (Vec3, Vec3) = {
    val pv_f1 = transform0.transformPosVel(position, velocity)
    val position_f1 = pv_f1._1
    val velocity_f1 = pv_f1._2
    transform1.transformPosVel(position_f1, velocity_f1)
  }

  override def transformPosVelAcc(position: Vec3, velocity: Vec3, acceleration: Vec3): (Vec3, Vec3, Vec3) = {
    val pva_f1 = transform0.transformPosVelAcc(position, velocity, acceleration)
    val position_f1 = pva_f1._1
    val velocity_f1 = pva_f1._2
    val acceleration_f1 = pva_f1._3
    transform1.transformPosVelAcc(position_f1, velocity_f1, acceleration_f1)
  }

  override def transformVector(vector: Vec3): Vec3 = {
    val vector0 = transform0.transformVector( vector )
    transform1.transformVector( vector0 )
  }

}
