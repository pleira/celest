package be.angelcorp.celest.frameGraph.transformations

import be.angelcorp.celest.frameGraph.{BasicReferenceFrameTransformFactory, ReferenceSystem}
import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.time.Epoch

trait TranslationalTransformFactory[F0 <: ReferenceSystem, F1 <: ReferenceSystem]
  extends BasicReferenceFrameTransformFactory[F0, F1] {

  /**
   * Find the transformation between frame F0 and F1 at the specified epoch in the form of a pure translation.
   * @param epoch Epoch for the frame transformation.
   * @return Translation offset to use in the transform from F0 to F1.
   */
  def translation(epoch: Epoch): Vec3

  /**
   * Factory that generates the inverse transformation from F1 => F0 by inverting the rotation matrix (= transpose)
   */
  class InverseTranslationalTransformFactory extends BasicReferenceFrameTransformFactory[F1, F0] {
    def cost(epoch: Epoch) = TranslationalTransformFactory.this.cost(epoch)

    def transform(epoch: Epoch) =
      new TranslationTransform[F1, F0, InverseTranslationalTransformFactory](-translation(epoch), epoch, this)

    def inverse = TranslationalTransformFactory.this

    def fromFrame: F1 = TranslationalTransformFactory.this.toFrame

    def toFrame: F0 = TranslationalTransformFactory.this.fromFrame
  }

  val inverse = new InverseTranslationalTransformFactory

  def transform(epoch: Epoch) =
    new TranslationTransform[F0, F1, TranslationalTransformFactory[F0, F1]](translation(epoch), epoch, this)


}
