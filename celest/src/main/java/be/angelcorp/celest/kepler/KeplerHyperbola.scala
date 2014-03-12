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

import math._
import org.apache.commons.math3.util.FastMath
import be.angelcorp.celest.state.Keplerian
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem

class KeplerHyperbola[F <: BodyCenteredSystem](k: Keplerian[F]) extends KeplerEquations(k) {

  lazy val anomaly = KeplerHyperbola.hyperbolicAnomalyFromMean(k.meanAnomaly, k.eccentricity)

  lazy val arealVel = sqrt(semiLatusRectum * μ) / 2

  lazy val apocenter = Double.PositiveInfinity

  lazy val pericenter = k.semiMajorAxis * (1 - k.e)

  lazy val period = Double.PositiveInfinity

  lazy val semiLatusRectum = k.semiMajorAxis * (1 - k.e * k.e)

  lazy val totEnergyPerMass = μ / (2 * k.semiMajorAxis)

  lazy val trueAnomaly = KeplerHyperbola.trueAnomalyFromAnomaly(anomaly, k.eccentricity)

}

object KeplerHyperbola {

  def hyperbolicAnomalyFromTrue(nu: Double, e: Double) = {
    // acos(-1 / e) == half defection angle
    if (abs(nu) < acos(-1 / e)) {
      val sine = (sin(nu) * sqrt(e * e - 1.0)) / (1.0 + e * cos(nu))
      FastMath.asinh(sine)
    } else {
      Double.NaN
    }
  }

  def hyperbolicAnomalyFromMean(M: Double, e: Double) = {
    /* Generate an initial guess */
    var H0 =
      if (e < 1.6) {
        if (((M < 0.0) && (M > -Pi)) || M > Pi)
          M - e
        else
          M + e
      } else {
        if (e < 3.6 && M > Pi)
          M - e
        else
          M / (e - 1)
      }

    /* Iterate until H is accurate enough */
    var H = H0 + (M - e * sinh(H0) + H0) / (e * cosh(H0) - 1)
    do {
      H0 = H
      H = H + (M - e * sinh(H) + H) / (e * cosh(H) - 1)
    } while (abs(H - H0) > KeplerEquations.anomalyIterationTol)
    H
  }

  def meanAnomalyFromAnomaly(H: Double, e: Double) = e * sinh(H) - H

  def meanAnomalyFromTrue(nu: Double, e: Double) = meanAnomalyFromAnomaly(hyperbolicAnomalyFromTrue(nu, e), e)

  def trueAnomalyFromAnomaly(H: Double, e: Double) = atan2(sqrt(e * e - 1) * sinh(H), e - cosh(H))

  def trueAnomalyFromMean(M: Double, e: Double) = trueAnomalyFromAnomaly(hyperbolicAnomalyFromMean(M, e), e)

}
