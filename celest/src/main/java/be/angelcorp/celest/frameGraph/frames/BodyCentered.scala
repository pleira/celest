package be.angelcorp.celest.frameGraph.frames

import be.angelcorp.celest.body.CelestialBody
import be.angelcorp.celest.frameGraph.ReferenceFrame

/**
 * A body-centred frame of reference.
 */
trait BodyCentered extends ReferenceFrame {

  /**
   * Body at the frame center
   */
  def centerBody: CelestialBody

}

object BodyCentered {

  /**
   * Creates a basic body centered reference frame, centered on a specified body.
   * @param body Body at the origin of the reference frame.
   */
  def apply(body: CelestialBody): BodyCentered = new BodyCentered {
    /** Body at the frame center */
    def centerBody = body
  }

}
