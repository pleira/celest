package be.angelcorp.celest.body

import be.angelcorp.celest.constants.Constants
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.state.Orbit
import be.angelcorp.celest.frameGraph.ReferenceSystem

/**
 * Create a CelestialBody, based on its standard gravitational parameter μ, and its time-varying orbit.
 *
 * @author Simon Billemont
 */
trait Body[F <: ReferenceSystem] extends CelestialBody {

  /**
   * Get the total mass of the celestial body.
   * <p>
   * <b>Unit: [kg]</b>
   * </p>
   * @return Mass of the planet [kg].
   */
  def mass: Double = Constants.mu2mass(μ)

  /**
   * Ephemeris for the planet.
   * @param epoch Epoch on which to retrieve the epoch.
   * @return The orbital elements for the body on the given epoch.
   */
  def orbit(epoch: Epoch): Orbit[F]

}
