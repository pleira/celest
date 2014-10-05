package be.angelcorp.celest.sofa.frames

import javax.inject.Inject

import be.angelcorp.celest.data.eop.{EarthOrientationData, PoleProvider}
import be.angelcorp.celest.frameGraph.frames.{GCRS, ITRS}
import be.angelcorp.celest.frameGraph.transformations.ConstantRotationTransformFactory
import be.angelcorp.celest.sofa.SofaMatrix
import be.angelcorp.celest.sofa.time.SofaEpoch
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.time.timeStandard.TimeStandard
import be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations.{TT, UT1}
import be.angelcorp.sofa.SofaLibrary._

/**
 * A direct transformation by SOFA from GCRS to ITRS, based in the SOFA iauC2t06a function.
 *
 * Forms the celestial to terrestrial matrix on given the epoch based on the IAU 2006 precession and IAU 2000A nutation models.
 *
 * Using this direct transformation avoids needles copying of data bewteen the C and JVM context
 *
 * @param gcrf The GCRS implementation to convert from.
 * @param itrf The ITRS implementation to convert to.
 * @param cip  IERS polar motion, coordinates of CIP wrt ITRS.
 * @param tt   The definition of TT  to employ.
 * @param ut1  The definition of UT1 to employ.
 */
class SofaCIOTransform(gcrf: GCRS, itrf: ITRS, cip: PoleProvider, @TT tt: TimeStandard, @UT1 ut1: TimeStandard) extends ConstantRotationTransformFactory[GCRS, ITRS] {

  /** Constructor that extracts the position of the CIP from an instance of `EarthOrientationData` */
  @Inject
  def this(gcrf: GCRS, itrf: ITRS, eop: EarthOrientationData, @TT tt: TimeStandard, @UT1 ut1: TimeStandard) =
    this(gcrf, itrf, eop.cip, tt, ut1)

  override def rotationMatrix(epoch: Epoch) = {
    // The transformation epoch, in TT
    val epoch_tt  = SofaEpoch( epoch inTimeStandard tt )(tt)
    // The transformation epoch, in UT1
    val epoch_ut1 = SofaEpoch( epoch inTimeStandard ut1 )(tt)

    // Retrieve the CIP coordinates
    val (xp, yp) = cip.polarCoordinatesOn( epoch )

    // Form the transformation matrix
    val mtx = new SofaMatrix()
    iauC2t06a ( epoch_tt.dj1, epoch_tt.dj2, epoch_ut1.dj1, epoch_ut1.dj2, xp, yp, mtx )
    mtx.result
  }

  override def toFrame = itrf

  override def fromFrame = gcrf

  override def cost(epoch: Epoch): Double = 1.0 // TODO: Determine frame cost

}
