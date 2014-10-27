package be.angelcorp.celest.frameGraph.transformations

import be.angelcorp.celest.frameGraph.{BasicReferenceFrameTransformFactory, ReferenceSystem}
import be.angelcorp.celest.math.geometry.Mat3
import be.angelcorp.celest.time.Epoch

trait ConstantRotationTransformFactory[F0 <: ReferenceSystem, F1 <: ReferenceSystem]
  extends BasicReferenceFrameTransformFactory[F0, F1] {

  /**
   * Find the transformation between frame F0 and F1 at the specified epoch in the form of a pure rotation matrix.
   * @param epoch Epoch for the frame transformation.
   * @return Rotation matrix to transform from F0 to F1.
   */
  def rotationMatrix(epoch: Epoch): Mat3

  /**
   * Factory that generates the inverse transformation from F1 => F0 by inverting the rotation matrix (= transpose)
   */
  class InverseConstantRotationTransformFactory extends BasicReferenceFrameTransformFactory[F1, F0] {
    def cost(epoch: Epoch) = ConstantRotationTransformFactory.this.cost(epoch)

    def transform(epoch: Epoch) =
      new ConstantRotationTransform[F1, F0, InverseConstantRotationTransformFactory](rotationMatrix(epoch).transpose, epoch, this)

    def inverse = ConstantRotationTransformFactory.this

    def fromFrame: F1 = ConstantRotationTransformFactory.this.toFrame

    def toFrame: F0 = ConstantRotationTransformFactory.this.fromFrame
  }

  def inverse = new InverseConstantRotationTransformFactory

  def transform(epoch: Epoch) =
    new ConstantRotationTransform[F0, F1, ConstantRotationTransformFactory[F0, F1]](rotationMatrix(epoch), epoch, this)


}