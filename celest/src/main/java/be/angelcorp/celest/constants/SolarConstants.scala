/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.celest.constants

import be.angelcorp.celest.body.Satellite
import be.angelcorp.celest.universe.Universe

/**
 * Different constants specific to the sun. Has constants in the following categories:
 * <ul>
 * <li>Orbital parameters</li>
 * <li>Physical constants</li>
 * <li>Geometric constants</li>
 * </ul>
 *
 * @author Simon Billemont, TUDelft, Faculty Aerospace Engineering (aodtorusan@gmail.com or
 *         s.billemont@student.tudelft.nl)
 */
object SolarConstants {

  /**
   * Standard gravitational parameter of the sun.
   *
   * Value from the JPL DE 421 ephemeris:  'The Planetary and Lunar Ephemeris DE 421'
   * by W. M. Folkner, J. G. Williams, D. H. Boggs
   * [online] ftp://ssd.jpl.nasa.gov/pub/eph/planets/ioms/de421.iom.v1.pdf
   * <p>
   * <b>Unit: [m<sup>3</sup>/s<sup>2</sup>]</b>
   * </p>
   */
  val mu = 132712440040.944000E9

  /**
   * Mass of the sun.
   * <p>
   * <b>Unit: [kg]</b>
   * </p>
   */
  val mass = Constants.mu2mass(mu)

  /**
   * Mean density of the entire sun (average).
   * <p>
   * <b>Unit: [kg/m<sup>3</sup>]</b>
   * </p>
   */
  val meanDensity = 1.408E3

  /**
   * Mean solar radius.
   * <p>
   * <b>Unit: [m]</b>
   * </p>
   */
  val radiusMean = 6.955E8

  /**
   * Radius of the sun at the equator (great circle).
   * <p>
   * <b>Unit: [m]</b>
   * </p>
   */
  val radiusEquatorial = 4.379E9

  /**
   * How the sun is "deformed" relative to a perfect sphere.
   * <p>
   * flattening = (a - b) / (a) <br />
   * with:
   * <ul>
   * <li>a: distance center to the equator</li>
   * <li>b: distance center to the poles</li>
   * </ul>
   * </p>
   * <p>
   * <b>Unit: [-]</b>
   * </p>
   */
  val flattening = 9E-6

  /**
   * Suns total surface area.
   * <p>
   * <b>Unit: [m<sup>2</sup>]</b>
   * </p>
   */
  val surfaceArea = 6.0877E18

  /**
   * Suns total volume.
   * <p>
   * <b>Unit: [m<sup>3</sup>]</b>
   * </p>
   */
  val volume = 1.412E27

  /** Celestial body representation of the sun. */
  def body(implicit universe: Universe) = new Satellite(mass, null)
}
