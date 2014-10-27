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

import org.apache.commons.math3.analysis.solvers.NewtonSolver
import org.apache.commons.math3.analysis.{UnivariateFunction, DifferentiableUnivariateFunction}

import math._
import be.angelcorp.celest.math._
import be.angelcorp.celest.state.Keplerian
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem

class KeplerParabola[F <: BodyCenteredSystem](k: Keplerian[F]) extends KeplerEquations(k) {

  lazy val anomaly = KeplerParabola.anomalyFromMean(k.meanAnomaly)

  lazy val arealVel = throw new UnsupportedOperationException()

  override lazy val flightPathAngle = trueAnomaly / 2

  def apocenter = Double.PositiveInfinity

  lazy val pericenter = semiLatusRectum / 2

  def period = Double.PositiveInfinity

  def semiLatusRectum = k.semiMajorAxis // TODO: better solution ?

  def totEnergyPerMass = 0

  override def visViva(r: Double) = 2 * μ / r

  lazy val trueAnomaly: Double = KeplerParabola.trueAnomalyFromAnomaly(anomaly)
}

object KeplerParabola {

  def radius(p: Double, B: Double) = (p / 2) * (1 + B * B)

  def anomalyFromMean(M: Double) = {
    val f = new DifferentiableUnivariateFunction() {
      override def derivative() =
        new UnivariateFunction() {
          override def value(B: Double) = 1 + 1 * B * B
        }

      override def value(B: Double) = B + pow(B, 3) / 3.0 - M
    }
    if (!(M.isNaN || M.isInfinite)) {
      val solver = new NewtonSolver()
      solver.solve(50, f, M)
    } else M
  }

  def anomalyFromTrue(trueA: Double) = {
    val nu = mod(trueA, 2 * Pi)
    if (nu > Pi)
      tan(nu - 2 * Pi)
    else
      tan(nu / 2)
  }

  def meanAnomalyFromAnomaly(anomaly: Double) = anomaly + (anomaly * anomaly * anomaly) / 3.0

  def meanAnomalyFromTrue(nu: Double) = meanAnomalyFromAnomaly(anomalyFromTrue(nu))

  def trueAnomalyFromAnomaly(B: Double) = 2.0 * atan(B)

  def trueAnomalyFromMean(M: Double) = trueAnomalyFromAnomaly(anomalyFromMean(M))

}
