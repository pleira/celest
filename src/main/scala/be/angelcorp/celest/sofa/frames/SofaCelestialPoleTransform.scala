package be.angelcorp.celest.sofa.frames

import javax.inject.Inject

import be.angelcorp.celest.data.eop.{EarthOrientationData, PoleProvider}
import be.angelcorp.celest.frameGraph.frames.{CIRS, GCRS}
import be.angelcorp.celest.frameGraph.transformations.ConstantRotationTransformFactory
import be.angelcorp.celest.sofa.SofaMatrix
import be.angelcorp.celest.sofa.time.SofaEpoch
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.time.timeStandard.TimeStandard
import be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations.TT
import be.angelcorp.libs.math.linear.Matrix3D
import be.angelcorp.sofa.SofaLibrary._
import org.bridj.Pointer._

/**
 * Transforms from the GCRS (geocentric celestial reference system) to CIRS (celestial intermediate reference system).
 *
 * - Computes the bias-precession-nutation angle formulation for (x,y) based in the SOFA’s routines for IAU 2006/2000A (iau_XYS06A).
 * - Applies IERS celestial pole corrections (dx, dy).
 * - From the transformation matrix (iau_C2IXYS)
 *
 * @param cipOffsets Celestial Intermediate Pole offsets (DX, DY from IERS)
 * @param gcrf The GCRS implementation to convert from.
 * @param cirf The CIRS implementation to convert to.
 * @param tt The terrestrial time definition.
 */
class SofaCelestialPoleTransform(val gcrf: GCRS, val cirf: CIRS, val cipOffsets: PoleProvider, @TT implicit val tt: TimeStandard) extends ConstantRotationTransformFactory[GCRS, CIRS] {

  /** Constructor that extracts the position of the CIP from an instance of `EarthOrientationData` */
  @Inject
  def this(gcrf: GCRS, cirf: CIRS, eop: EarthOrientationData, @TT tt: TimeStandard) = this(gcrf, cirf, eop.cipOffset, tt)

  override def rotationMatrix(epoch: Epoch): Matrix3D = {
    val epoch_tt = SofaEpoch( epoch.inTimeStandard(tt) )

    /* Retrieve the CIP corrections. */
    val (dx, dy) = cipOffsets.polarCoordinatesOn(epoch)

    /* Add CIP corrections. */
    val _x = allocateDouble()
    val _y = allocateDouble()
    val s  = allocateDouble()
    iauXys06a(epoch_tt.dj1, epoch_tt.dj2, _x, _y, s)
    println(s"x: %.16f".format(_x.get))
    println(s"y: %.16f".format(_y.get))
    println(s"s: %.16f".format(s.get))
    val x = _x.get + dx
    val y = _y.get + dy

    /* GCRS to CIRS matrix. */
    val mtx = new SofaMatrix()
    iauC2ixys (x, y, s.get, mtx)
    mtx.result
  }

  override def toFrame = cirf

  override def fromFrame = gcrf

  override def cost(epoch: Epoch): Double = 1.0 // TODO: Determine transform cost

}
