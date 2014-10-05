package be.angelcorp.celest.sofa.frames

import javax.inject.Inject

import be.angelcorp.celest.frameGraph.frames.{CIRS, TIRS}
import be.angelcorp.celest.frameGraph.transformations.ConstantRotationTransformFactory
import be.angelcorp.celest.sofa._
import be.angelcorp.celest.sofa.time.{SofaEpoch, SofaUT1}
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.time.timeStandard.TimeStandard
import be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations.{TT, UT1}
import be.angelcorp.sofa.SofaLibrary._

/**
 * Transforms from CIRS to TIRS based on the Earth rotation angle.
 *
 * - Compute the ERA (iauEra00)
 * - Create transformation matrix (iauRz)
 *
 * @param cirf The CIRS frame implementation to transform from.
 * @param tirf The TIRF frame implementation to transform to.
 * @param ut1  The definition of UT1 to employ.
 * @param tt   The definition of TT  to employ.
 */
class SofaEarthRotation@Inject()(val cirf: CIRS, val tirf: TIRS, @UT1 val ut1: SofaUT1, @TT implicit val tt: TimeStandard) extends ConstantRotationTransformFactory[CIRS, TIRS] {

  override def rotationMatrix(epoch: Epoch) = {
    // The transformation epoch, in UT1
    val epoch_ut1 = ut1.toUT1( SofaEpoch(epoch) )

    // The earth rotation angle at the specified epoch (angle from CIO to the TIO)
    val era = iauEra00(epoch_ut1.dj1, epoch_ut1.dj2)

    // Create the rotation matrix from CIRS to TIRS
    val mtx = new SofaMatrix()
    iauIr(mtx) // Initialize to the idenity matrix
    iauRz(era, mtx) // Add the ERA rotation around the z-axis
    mtx.result
  }

  override def toFrame = tirf

  override def fromFrame = cirf

  override def cost(epoch: Epoch) = 1.0 //TODO: Frame transform cost

}
