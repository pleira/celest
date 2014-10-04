package be.angelcorp.celest.sofa.frames

import javax.inject.Inject

import be.angelcorp.celest.data.eop.{EarthOrientationData, PoleProvider}
import be.angelcorp.celest.frameGraph.frames.{ITRS, TIRS}
import be.angelcorp.celest.frameGraph.transformations.ConstantRotationTransformFactory
import be.angelcorp.celest.sofa.SofaMatrix
import be.angelcorp.celest.sofa.time.SofaEpoch
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.time.timeStandard.TimeStandard
import be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations.TT
import be.angelcorp.libs.math.linear.Matrix3D
import be.angelcorp.sofa.SofaLibrary._

/**
 * Transformation from TIRS to ITRS. Corrects for the TIO Locator & polar motion (wobble).
 *
 * - Extract the position of the CIP.
 * - Determine the TIO locator (on the CIP equator) (iauSp00).
 * - Create the resulting transformation matrix (iauPom00).
 *
 * @param tirf The TIRF implementation to convert from.
 * @param itrf The ITRF implementation to convert to.
 * @param pole IERS polar motion, coordinates of CIP wrt ITRS.
 * @param tt   The terrestrial time definition.
 */
class SofaPolarMotion(val tirf: TIRS, val itrf: ITRS, val pole: PoleProvider, @TT implicit val tt: TimeStandard) extends ConstantRotationTransformFactory[TIRS, ITRS] {

  /** Constructor that extracts the position of the CIP from an instance of `EarthOrientationData` */
  @Inject
  def this(tirf: TIRS, itrf: ITRS, eop: EarthOrientationData, @TT tt: TimeStandard) = this(tirf, itrf, eop.cip, tt)

  override def rotationMatrix(epoch: Epoch): Matrix3D = {
    // The transformation epoch, in TT
    val epoch_tt = SofaEpoch( epoch.inTimeStandard(tt) )

    // Position of the CIP in ITRS
    val (xp, yp) = pole.polarCoordinatesOn(epoch)
    // TIO locator, fixing the position on the equator of the Celestial Intermediate Pole.
    val s = iauSp00(epoch_tt.dj1, epoch_tt.dj2)

    // Form the transformation matrix
    val mtx = new SofaMatrix
    iauPom00 (xp, yp, s, mtx)
    mtx.result
  }

  override def toFrame   = itrf

  override def fromFrame = tirf

  override def cost(epoch: Epoch): Double = 1.0 //TODO determine construction cost

}
