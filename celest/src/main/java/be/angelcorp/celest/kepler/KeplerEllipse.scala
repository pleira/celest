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

import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem
import be.angelcorp.celest.math._
import be.angelcorp.celest.state.Keplerian
import org.apache.commons.math3.util.FastMath.atan2

import scala.math._

class KeplerEllipse[F <: BodyCenteredSystem](k: Keplerian[F]) extends KeplerEquations(k) {

  lazy val anomaly = KeplerEllipse.eccentricAnomalyFromMean(k.meanAnomaly, k.eccentricity)

  lazy val arealVel = sqrt(k.a * μ * (1 - k.e * k.e)) / 2

  val apocenter = k.semiMajorAxis * (1 + k.eccentricity)

  val pericenter = k.semiMajorAxis * (1 - k.eccentricity)

  lazy val period = 2 * Pi / meanMotion

  val semiLatusRectum = k.semiMajorAxis * (1 - k.e * k.e)

  lazy val totEnergyPerMass = -μ / (2 * k.semiMajorAxis)

  lazy val trueAnomaly = KeplerEllipse.trueAnomalyFromEccentric(anomaly, k.eccentricity)

}

object KeplerEllipse {

  /**
   * Solves for eccentric anomaly (E), from a given mean anomaly (M), and eccentricity (e).
   *
   * This is done by solving Kepler's equation for ellipses using a basic Newton-Raphson iteration scheme and <b>first</b>-order iteration/starting value.
   *
   * @param MA Mean anomaly [rad]
   * @param e Orbit eccentricity [-]
   * @return The eccentric anomaly [rad]
   */
  def eccentricAnomalyFromMeanOld(MA: Double, e: Double) = {
    val M = mod(MA, 2 * Pi)

    var E = if ((M > -Pi && M < 0) || M > Pi) M - e else M + e
    var Etemp = E + (M - E + e * sin(E)) / (1 - e * cos(E))

    while (abs(Etemp - E) > KeplerEquations.anomalyIterationTol) {
      Etemp = E
      E = Etemp + (M - Etemp + e * sin(Etemp)) / (1 - e * cos(Etemp))
    }
    E
  }

  /**
   * Solves for eccentric anomaly (E), from a given mean anomaly (M), and eccentricity (e).
   *
   * This is done by solving Kepler's equation for ellipses using a basic Newton-Raphson iteration scheme and <b>third</b>-order iteration/starting value.
   *
   * See:
   * <a href="http://murison.alpheratz.net/dynamics/twobody/KeplerIterations_summary.pdf">
   *  Marc A. Murison, "A Practical Method for Solving the Kepler Equation", U.S. Naval Observatory, Washington DC, 6 November 2006
   * </a>
   *
   * @param MA Mean anomaly [rad]
   * @param e Orbit Eccentricity [-]
   * @param tol Absolute accuracy of the obtained eccentric anomaly [rad]
   * @return The eccentric anomaly [rad]
   */
  def eccentricAnomalyFromMean(MA: Double, e: Double, tol: Double = 1E-14) = {
    val Mnorm = mod(MA, 2 * Pi)

    // Initial guess
    val t34 = e * e
    val t35 = e * t34
    val t33 = cos(Mnorm)
    var E0 = Mnorm + (-1.0/2.0*t35+e+(t34+3.0/2.0*t33*t35)*t33)*sin(Mnorm)

    var dE = tol + 1.0
    while (dE > tol) {
      val t1 = cos(E0)
      val t2 = -1.0+e*t1
      val t3 = sin(E0)
      val t4 = e*t3
      val t5 = -E0+t4+Mnorm
      val t6 = t5/(1.0/2.0*t5*t4/t2+t2)
      val E  = E0 - t5/((1.0/2.0*t3 - 1.0/6.0*t1*t6)*e*t6+t2)
      dE = abs(E-E0)
      E0 = E
    }
    E0
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
