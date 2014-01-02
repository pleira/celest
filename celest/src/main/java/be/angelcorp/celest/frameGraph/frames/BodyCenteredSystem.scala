package be.angelcorp.celest.frameGraph.frames

import be.angelcorp.celest.body.CelestialBody
import be.angelcorp.celest.frameGraph.ReferenceSystem
import be.angelcorp.celest.constants.Constants

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

  /**
   * Creates a basic body centered reference frame, centered on body with a specific standard gravitational parameter.
   * @param mu Standard gravitational parameter of the body [m^3/s^2].
   */
  def apply(mu: Double): BodyCenteredSystem = new BodyCenteredSystem {
    /** Body at the frame center */
    val centerBody = new CelestialBody {
      def mass: Double = Constants.mu2mass(μ)

      def μ: Double = mu
    }
  }

}
