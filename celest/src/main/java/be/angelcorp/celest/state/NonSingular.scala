/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
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
package be.angelcorp.celest.state

import be.angelcorp.celest.kepler._
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem

/**
 * Holds the state using the non-singular Keplerian elements:
 *
 * <pre>
 * Elements: { a,   e,    i,   &omega;_true,  &Omega;,   &lambda;_mean }
 * Units:    {[m], [-], [rad], [rad], [rad],  [rad] }
 * </pre>
 *
 * The true longitude of the perihelion (&omega;_true) and the mean longitude can be derived from the kepler elements as follows:
 * <pre>
 * cos(&omega;_true) = dot(<1,0,0>, ecc_vec) / |ecc_vec|
 * &rarr; &omega;_true approx &Omega; + &omega; (if i small)
 *
 * &lambda;_mean = &Omega; + &omega; + M = &omega;_true + M
 * </pre>
 *
 * @param a Semi-major axis, this is one half of the major axis, and thus runs from the center, through a focus,
 *          and to the edge of the ellipse. [m]
 * @param e Eccentricity may be interpreted as a measure of how much this shape deviates from a circle. [-]
 * @param i Inclination element is the angle is between the reference plane XY plane and the orbit. The inclination
 *          ranges from 0 to &pi; rad.[rad]
 * @param ω_true True longitude of perihelion, this is the angle measured eastward from the vernal equinox
 *               (I unit vector) to the eccentricity vector (pointing to the pericenter) [rad].
 * @param Ω Right ascension of the ascending node. This is the angle from a reference direction, called
 *          the origin of longitude, to the direction of the ascending node, measured in a reference plane. [rad]
 * @param λ_mean Mean longitude is the location (angle) of the satellite from the vernal equinox [rad]
 * @param frame Frame in which this set of elements is defined.
 *
 * @author Simon Billemont
 * @see IKeplerElements
 */
class NonSingular[F <: BodyCenteredSystem]
(val a: Double,
 val e: Double,
 val i: Double,
 val ω_true: Double,
 val Ω: Double,
 val λ_mean: Double,
 val frame: F) extends Orbit[F] {

  /** A more verbose name for a [m] */
  def semiMajorAxis = a

  /** A more verbose name for e [-] */
  def eccentricity = e

  /** A more verbose name for i [rad] */
  def inclination = i

  /** The Keplerian argument of pericenter [rad] */
  def ω = ω_true - Ω

  /** The Keplerian argument of pericenter [rad] */
  def argumentOfPeriapsis = ω

  /** A more verbose name for Ω [rad] */
  def rightAscension = Ω

  /** True longitude of perihelion ω_true */
  def longitudePerihelion = ω_true

  /** Mean longitude */
  def meanLongitude = λ_mean

  /** The Keplerian mean anomaly M */
  def meanAnomaly = λ_mean - ω - Ω

  /** The Keplerian true anomaly &nu;. */
  def ν = trueAnomalyFromMean(meanAnomaly, e)

  /** The Keplerian true anomaly &nu;. */
  def trueAnomaly = ν

  /** Finds the satellite position in the orbit using the generic anomaly (eccentric/hyperbolic/parabolic). */
  def anomaly = anomalyFromMean(meanAnomaly, e)

  def toKeplerian = new Keplerian(a, e, i, ω, Ω, meanAnomaly, frame)

  def toPosVel: PosVel[F] = toKeplerian.toPosVel

}
