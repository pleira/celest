package be.angelcorp.celest.frameGraph.frames

import be.angelcorp.celest.body.CelestialBody
import be.angelcorp.celest.frameGraph.ReferenceSystem

/**
 * A body-centred frame of reference.
 */
trait BodyCenteredSystem extends ReferenceSystem {

  /**
   * Body at the frame center
   */
  def centerBody: CelestialBody

}

object BodyCenteredSystem {

  /**
   * Creates a basic body centered reference frame, centered on a specified body.
   * @param body Body at the origin of the reference frame.
   */
  def apply(body: CelestialBody): BodyCenteredSystem = new BodyCenteredSystem {
    /** Body at the frame center */
    def centerBody = body
  }

}
