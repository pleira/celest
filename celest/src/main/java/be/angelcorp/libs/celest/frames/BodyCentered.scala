package be.angelcorp.libs.celest.frames

import be.angelcorp.libs.celest.body.ICelestialBody

/**
 * A body-centred frame of reference.
 */
trait BodyCentered extends IReferenceFrame {

  /**
   * Body at the frame center
   * @return
   */
  def centerBody: ICelestialBody

}
