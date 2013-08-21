package be.angelcorp.libs.celest.frames

import be.angelcorp.libs.celest.body.ICelestialBody

/**
 * A body-centred frame of reference.
 */
trait BodyCentered extends IReferenceFrame {

  /**
   * Body at the frame center
   */
  def centerBody: ICelestialBody

}

object BodyCentered {

  /**
   * Creates a basic body centered reference frame, centered on a specified body.
   * @param body Body at the origin of the reference frame.
   */
  def apply(body: ICelestialBody): BodyCentered = new BodyCentered {
    /** Body at the frame center */
    def centerBody = body
  }

}
