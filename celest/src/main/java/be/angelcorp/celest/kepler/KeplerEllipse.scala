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
package be.angelcorp.celest.kepler

import scala.math._
import be.angelcorp.libs.math.MathUtils2
import be.angelcorp.celest.state.Keplerian
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem

class KeplerEllipse[F <: BodyCenteredSystem](k: Keplerian[F]) extends KeplerEquations(k) {

  lazy val anomaly = KeplerEllipse.eccentricAnomalyFromMean(k.meanAnomaly, k.eccentricity)

  val arealVel = sqrt(k.a * μ * (1 - k.e * k.e)) / 2

  val apocenter = k.semiMajorAxis * (1 + k.eccentricity)

  val pericenter = k.semiMajorAxis * (1 - k.eccentricity)

  val period = 2 * Pi / meanMotion

  val semiLatusRectum = k.semiMajorAxis * (1 - k.e * k.e)

  val totEnergyPerMass = -μ / (2 * k.semiMajorAxis)

  lazy val trueAnomaly = KeplerEllipse.trueAnomalyFromEccentric(anomaly, k.eccentricity)

}

object KeplerEllipse {

  /**
   * Solves for eccentric anomaly (E), from a given mean anomaly (M), and eccentricity (e).
   *
   * This is done by solving Kepler's equation for ellipses using a basic Newton-Raphson iteration scheme.
   *
   * @param MA Mean anomaly [rad]
   * @param e Eccentricity [-]
   * @return The eccentric anomaly [rad]
   */
  def eccentricAnomalyFromMean(MA: Double, e: Double) = {
    val M = MathUtils2.mod(MA, 2 * Pi)

    var E = if ((M > -Pi && M < 0) || M > Pi) M - e else M + e
    var Etemp = E + (M - E + e * sin(E)) / (1 - e * cos(E))

    while (abs(Etemp - E) > KeplerEquations.anomalyIterationTol) {
      Etemp = E
      E = Etemp + (M - Etemp + e * sin(Etemp)) / (1 - e * cos(Etemp))
    }
    E
  }

  /**
   * Compute the eccentric anomaly E from the true anomaly &nu;.
   *
   * @param nu True anomaly [rad]
   * @param e Eccentricity [-]
   * @return Eccentric anomaly [rad]
   */
  def eccentricAnomalyFromTrue(nu: Double, e: Double) = atan2(sqrt(1 - e * e) * sin(nu), e + cos(nu))

  /**
   * Compute the true anomaly &nu; from the eccentric anomaly E;.
   *
   * @param E Eccentric anomaly [rad]
   * @param e Eccentricity [-]
   * @return True anomaly [rad]
   */
  def trueAnomalyFromEccentric(E: Double, e: Double) = atan2(sin(E) * sqrt(1 - e * e), cos(E) - e)

  /**
   * Compute the true anomaly &nu; from the mean anomaly M;.
   *
   * @param M Mean anomaly [rad]
   * @param e Eccentricity [-]
   * @return True anomaly [rad]
   */
  def trueAnomalyFromMean(M: Double, e: Double) = trueAnomalyFromEccentric(eccentricAnomalyFromMean(M, e), e)

  /**
   * Compute the mean anomaly M from the true anomaly &nu;.
   *
   * @param nu True anomaly [rad]
   * @param e Eccentricity [-]
   * @return Mean anomaly [rad]
   */
  def meanAnomalyFromTrue(nu: Double, e: Double) = meanAnomalyFromEccentric(eccentricAnomalyFromTrue(nu, e), e)

  /**
   * Compute the mean anomaly M from the eccentric anomaly E.
   *
   * @param EA Eccentric anomaly [rad]
   * @param e Eccentricity [-]
   * @return Mean anomaly [rad]
   */
  def meanAnomalyFromEccentric(EA: Double, e: Double) = EA - e * sin(EA)

}
