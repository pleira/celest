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

import be.angelcorp.celest.frameGraph.BasicReferenceFrameTransformFactory
import be.angelcorp.celest.frameGraph.ReferenceFrameTransform
import be.angelcorp.celest.frameGraph.ReferenceSystem
import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.math.rotation.Rotation
import be.angelcorp.celest.time.Epoch

/**
 * Abstract factory that can produce transforms, once the [[be.angelcorp.celest.frameGraph.ReferenceFrameTransformFactory# c a l c u l a t e P a r a m e t e r s ( b e.a n g e l c o r p.c e l e s t.t i m e.E p o c h )]] is implemented.
 * <p>
 * The input for this factory are the [[be.angelcorp.celest.frameGraph.transformations.TransformationParameters]],
 * which describe the kinematic relation between the origin and destination frameGraph. From these parameters, a
 * [[be.angelcorp.celest.frameGraph.ReferenceFrameTransform]] is constructed that can perform the actual transformation
 * between the two frames.
 * </p>
 *
 * @tparam F0 Origin frame of the produced transformation
 * @tparam F1 Destination frame of the produced transformation
 * @author Simon Billemont
 */
trait KinematicTransformationFactory[F0 <: ReferenceSystem, F1 <: ReferenceSystem]
  extends BasicReferenceFrameTransformFactory[F0, F1] {

  /**
   * Calculate the varius transformation parameters between frame F0 and F1 at a specific epoch.
   *
   * @param date Epoch at which the parameters must be valid.
   * @return A set of TransformationParameters for the transformation.
   */
  def calculateParameters(date: Epoch): TransformationParameters

  /** {@inheritDoc} */
  override def transform(epoch: Epoch) = {
    val param = calculateParameters(epoch)
    new KinematicTransformation(this, epoch, param)
  }

  /** {@inheritDoc} */
  override def inverse = new ReferenceFrameTransformInverseFactory(this)

}

/**
 * A special form of the [[be.angelcorp.celest.frameGraph.ReferenceFrameTransformFactory]] which produces the
 * inverse transform given the same [[be.angelcorp.celest.frameGraph.transformations.TransformationParameters]].
 *
 * @param factory Factory that produces non-inverse transforms.
 *
 * @author Simon Billemont
 */
class ReferenceFrameTransformInverseFactory[F0 <: ReferenceSystem, F1 <: ReferenceSystem](factory: KinematicTransformationFactory[F0, F1])
  extends BasicReferenceFrameTransformFactory[F1, F0] {

  /** {@inheritDoc} */
  override def cost(epoch: Epoch): Double =
    factory.cost(epoch) + 198 // 3 * 22 vector operations = 3*22*3 operations roughly

  /** {@inheritDoc} */
  override def transform(epoch: Epoch): ReferenceFrameTransform[F1, F0] = {
    // Calculate the non-inverted parameters
    val param = factory.calculateParameters(epoch)

    // Transform to inverted parameters

    // r* = R (r + dr)
    // r* - R dr = R r
    // iR r* - dr = r
    // iR (r* - R dr) = r
    val inverse_translation = -param.rotation.applyTo(param.translation)
    val inverse_orientation = param.rotation.inverse()

    // v* = R [ (v + dv) + w x (r + dr) ]
    // iR v* = (v + dv) + w x (r + dr)
    // iR v* - dv - w x (r + dr) = v
    // iR [ (v* - R dV) - R w x (r + dr) ] = v
    val inverse_velocity = -param.rotation.applyTo(param.velocity)
    val inverse_orientationRate = param.rotation.applyInverseTo(-param.rotationRate)

    val inverse_accelleration = -param.rotation.applyTo(param.acceleration)
    val inverse_orientationAcelleration = param.rotation.applyInverseTo(-param.rotationAcceleration)

    // Wrap the inverted parameters in a TransformationParameters
    val inverted_param = new TransformationParameters(epoch,
      inverse_translation, inverse_velocity, inverse_accelleration,
      inverse_orientation, inverse_orientationRate, inverse_orientationAcelleration)

    // Create the transformation
    new KinematicTransformation(this, epoch, inverted_param)
  }

  /** {@inheritDoc} */
  def inverse = factory

  /** {@inheritDoc} */
  def fromFrame: F1 = factory.toFrame

  /** {@inheritDoc} */
  def toFrame: F0 = factory.fromFrame

}

/**
 * A wrapper for the transformation parameters required by the [[be.angelcorp.celest.frameGraph.ReferenceFrameTransform]]
 * transformation. This includes;
 * <p/>
 * <ul>
 * <li>translation [m]</li>
 * <li>velocity [m/s]</li>
 * <li>acceleration [m/s<sup>2</sup>]</li>
 * <li>rotation[rad]</li>
 * <li>rotationRate[rad/s]</li>
 * <li>rotationAcceleration[rad/s<sup>2</sup>]</li>
 * </ul>
 *
 * @author Simon Billemont
 */
case class TransformationParameters(epoch: Epoch,
                                    translation: Vec3,
                                    velocity: Vec3,
                                    acceleration: Vec3,
                                    rotation: Rotation,
                                    rotationRate: Vec3,
                                    rotationAcceleration: Vec3)