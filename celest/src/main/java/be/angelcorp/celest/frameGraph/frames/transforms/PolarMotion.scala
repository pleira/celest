package be.angelcorp.celest.frameGraph.frames.transforms

import be.angelcorp.celest.frameGraph.ReferenceSystem
import be.angelcorp.celest.frameGraph.transformations.ConstantRotationTransformFactory
import be.angelcorp.celest.math.geometry.Mat3
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.data.eop.PoleProvider

/**
 * Applies a polar motion transformation.
 *
 * Generally transforms between [[be.angelcorp.celest.frameGraph.frames.ITRS]] <=> [[be.angelcorp.celest.frameGraph.frames.TIRS]]
 *
 * </p>
 * =References=
 * 1) D. Vallado et al. ,<b>"Implementation Issues Surrounding the New IAU Reference Systems for Astrodynamics"</b>, 16th AAS/AIAA Space Flight Mechanics Conference, Florida, January 2006<br/>
 * 2) G. Petit, B. Luzum (eds.).,<b>"IERS Conventions (2010)"</b>, IERS Technical Note 36, Frankfurt am Main: Verlag des Bundesamts für Kartographie und Geodäsie, 2010. 179 pp., ISBN 3-89888-989-6<br/>
 *
 * @author Simon Billemont
 */
class PolarMotion[F0 <: ReferenceSystem, F1 <: ReferenceSystem](val fromFrame: F0, val toFrame: F1, val eop: PoleProvider)
  extends ConstantRotationTransformFactory[F0, F1] {

  def cost(epoch: Epoch) = 0

  def rotationMatrix(epoch: Epoch) = {
    val (x_p, y_p) = eop.polarCoordinatesOn(epoch)
    Mat3.rotateX(-y_p) dot Mat3.rotateY(-x_p)
  }

}
