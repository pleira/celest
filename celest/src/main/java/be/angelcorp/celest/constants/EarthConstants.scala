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

import Constants._
import be.angelcorp.celest.state.positionState._
import be.angelcorp.celest.body.Satellite
import be.angelcorp.celest.physics.Units._
import be.angelcorp.celest.trajectory.KeplerVariationTrajectory
import be.angelcorp.celest.universe.Universe
import be.angelcorp.celest.state.{NonSingular, Keplerian}
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem
import be.angelcorp.celest.time.Epochs

/**
 * Different constants specific to the earth. Has constants in the following categories:
 * <ul>
 * <li>Orbital parameters</li>
 * <li>Physical constants</li>
 * <li>Geometric constants</li>
 * </ul>
 *
 * @author Simon Billemont, TUDelft, Faculty Aerospace Engineering (aodtorusan@gmail.com or
 *         s.billemont@student.tudelft.nl)
 */
object EarthConstants {

  /**
   * Standard gravitational parameter of the earth
   *
   * Value from the JPL DE 421 ephemeris:  'The Planetary and Lunar Ephemeris DE 421'
   * by W. M. Folkner, J. G. Williams, D. H. Boggs
   * [online] ftp://ssd.jpl.nasa.gov/pub/eph/planets/ioms/de421.iom.v1.pdf
   *
   * <p>
   * <b>Unit: [m<sup>3</sup>/s<sup>2</sup>]</b>
   * </p>
   */
  val mu = 398600.436233E9

  /**
   * Mass of the earth.
   *
   * <p>
   * <b>Unit: [kg]</b>
   * </p>
   */
  val mass = mu2mass(mu)

  /**
   * Mean density of the entire earth (average)
   *
   * http://nssdc.gsfc.nasa.gov/planetary/factsheet/earthfact.html
   * <p>
   * <b>Unit: [kg/m<sup>3</sup>]</b>
   * </p>
   */
  val meanDensity = 5515E0

  /**
   * Mean earth radius
   *
   * http://nssdc.gsfc.nasa.gov/planetary/factsheet/earthfact.html
   * <p>
   * <b>Unit: [m]</b>
   * </p>
   */
  val radiusMean = 6371E3

  /**
   * Radius of the earth at the equator (great circle)
   *
   * http://nssdc.gsfc.nasa.gov/planetary/factsheet/earthfact.html
   * <p>
   * <b>Unit: [m]</b>
   * </p>
   */
  val radiusEquatorial = 6378.1E3

  /**
   * Radius of the earth at the poles (great circle)
   *
   * http://nssdc.gsfc.nasa.gov/planetary/factsheet/earthfact.html
   * <p>
   * <b>Unit: [m]</b>
   * </p>
   */
  val radiusPolar = 6356.8E3

  /**
   * How the earth is "deformed" relative to a perfect sphere
   *
   * http://nssdc.gsfc.nasa.gov/planetary/factsheet/earthfact.html
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
  val flattening = 0.0033528

  /**
   * Circumference of the earth (not confirmed mean circumference)
   * <p>
   * <b>Unit: [m]</b>
   * </p>
   */
  val circumference = 40075.16E3

  /**
   * Earth total surface area
   * <p>
   * <b>Unit: [m<sup>2</sup>]</b>
   * </p>
   */
  val surfaceArea = 510072000E6

  /**
   * Earth total volume
   * <p>
   * <b>Unit: [m<sup>3</sup>]</b>
   * </p>
   */
  val volume = 1.08321E9 * 10E12

  /**
   * Semi-major axis (a) of the orbit of the earth around the sun at the J2000 epoch.
   *
   * http://nssdc.gsfc.nasa.gov/planetary/factsheet/earthfact.html
   * <p>
   * <b>Unit: [m]</b>
   * </p>
   */
  val semiMajorAxis = astronomicalUnit(1.00000011)

  /**
   * Eccentricity (e) of the orbit of the earth around the sun at the J2000 epoch.
   *
   * http://nssdc.gsfc.nasa.gov/planetary/factsheet/earthfact.html
   * <p>
   * <b>Unit: [-]</b>
   * </p>
   */
  val eccentricity = 0.01671022
  /**
   * Inclination (i) of the orbit of the earth around the sun at the J2000 epoch.
   *
   * http://nssdc.gsfc.nasa.gov/planetary/factsheet/earthfact.html
   * <p>
   * <b>Unit: [rad]</b>
   * </p>
   */
  val inclination = degrees(0.00005)

  /**
   * Argument of perigee (&omega;) of the orbit of the earth around the sun at the J2000 epoch.
   *
   * http://nssdc.gsfc.nasa.gov/planetary/factsheet/earthfact.html
   * <p>
   * <b>Unit: [rad]</b>
   * </p>
   */
  val argPerigee = degrees(102.94719)

  /**
   * Right ascension of the ascending node (&Omega;) of the orbit of the earth around the sun at the J2000 epoch.
   *
   * http://nssdc.gsfc.nasa.gov/planetary/factsheet/earthfact.html
   * <p>
   * <b>Unit: [rad]</b>
   * </p>
   */
  val raan = degrees(-11.26064)

  /**
   * The mean anomaly of the Earth orbit around the sun at the J2000 epoch.
   *
   * http://nssdc.gsfc.nasa.gov/planetary/factsheet/earthfact.html
   * <p>
   * <b>Unit: [rad]</b>
   * </p>
   */
  val mean_anomaly = degrees(100.46435)

  /** Kepler state vector of the earth, around the sun at the J2000 epoch */
  lazy val solarOrbit =
    new Keplerian(semiMajorAxis, eccentricity, inclination, argPerigee, raan, mean_anomaly, null) // TODO: set correct frame

  /** Celestial body representation of the earth around the sun at the J2000 epoch. */
  def body(implicit universe: Universe) = new Satellite(mass, null)

  def orbit(implicit universe: Universe) = {
    // Values based on
    // Simon, J., Bretagnon, P., Chapront, J., Chapront-Touzé, M., Francou, G., Laskar, J., 1994. Numerical expressions
    // for precession formulas and mean elements for the moon and the planets. Astron. Astrophys. 282 (2), 663–683.

    val jpl_sun = new Satellite(mu2mass(1.32712440018E20), null)

    val a0 = astronomicalUnit(1.0000010)
    val e0 = 0.0167086
    val i0 = 0.0
    val w_bar0 = degrees(102.9373481)
    val W0 = degrees(174.8731758)
    val L0 = degrees(100.4664568)
    val keplerAtJ2000 = new NonSingular(a0, e0, i0, w_bar0, W0, L0, new BodyCenteredSystem {
      //TODO: Use an actual frame
      def centerBody = jpl_sun
    })

    val centuryToS = julianCentury
    val da0 = 0.0 / centuryToS
    val de0 = -0.0000478 / centuryToS
    val di0 = degrees(-0.0008568) / centuryToS
    val dw_bar0 = degrees(0.0048746) / centuryToS
    val dW0 = degrees(-0.2780134) / centuryToS
    val dL0 = degrees(35999.3728565) / centuryToS
    val keplerVariation = new NonSingularDerivative(da0, de0, di0, dw_bar0, dW0, dL0)

    new KeplerVariationTrajectory(Epochs.J2000, keplerAtJ2000, keplerVariation)
  }
}
