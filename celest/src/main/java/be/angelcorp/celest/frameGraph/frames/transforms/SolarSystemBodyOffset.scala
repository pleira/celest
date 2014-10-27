package be.angelcorp.celest.frameGraph.frames.transforms

import be.angelcorp.celest.body.Body
import be.angelcorp.celest.frameGraph.ReferenceSystem
import be.angelcorp.celest.frameGraph.transformations.TranslationalTransformFactory
import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.state.PosVel
import be.angelcorp.celest.time.Epoch

/**
 * Applies a translational offset between two frames, due to orbital motion of body inside another reference frame.
 *
 * @author Simon Billemont
 */
class SolarSystemBodyOffset[F0 <: ReferenceSystem, F1 <: ReferenceSystem](val fromFrame: F0, val toFrame: F1, val ephemeris: Epoch => PosVel[F0])
  extends TranslationalTransformFactory[F0, F1] {

  def this( fromFrame: F0, toFrame: F1, body: Body[F0] ) =
   this(fromFrame, toFrame, epoch => body.orbit(epoch).toPosVel)

  def cost(epoch: Epoch) = 0

  /**
   * Find the transformation between frame F0 and F1 at the specified epoch in the form of a pure translation.
   * @param epoch Epoch for the frame transformation.
   * @return Translation offset to use in the transform from F0 to F1.
   */
  override def translation(epoch: Epoch): Vec3 = ephemeris(epoch).position

}
