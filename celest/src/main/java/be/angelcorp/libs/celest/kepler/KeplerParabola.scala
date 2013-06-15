/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *        http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.libs.celest.kepler;

import math._
import be.angelcorp.libs.celest.state.positionState.IKeplerElements
import org.apache.commons.math3.analysis.{UnivariateFunction, DifferentiableUnivariateFunction}
import org.apache.commons.math3.analysis.solvers.NewtonSolver
import be.angelcorp.libs.math.MathUtils2

class KeplerParabola(k: IKeplerElements) extends KeplerEquations(k) {

	override def anomalyFromMeanAnomaly(M: Double) = {
		val f = new DifferentiableUnivariateFunction() {
			override def derivative() =
				new UnivariateFunction() {
					override def value(B: Double) = 1 + 1 * B * B
				}

			override def value(B: Double) = B + pow(B, 3) / 3.0 - M
		}

		val solver = new NewtonSolver()
		solver.solve(50, f, M)
	}

	override def anomalyFromTrueAnomaly(trueA: Double) = {
		var nu = MathUtils2.mod(trueA, 2 * Pi)

		if (nu > Pi)
			nu = nu - 2 * Pi

		tan(nu / 2)
	}

	override def arealVel(µ: Double, a: Double, e: Double) = throw new UnsupportedOperationException()

	override def flightPathAngle() = k.getTrueAnomaly/ 2

	override def getApocenter() = Double.PositiveInfinity

	override def getFundamentalEquation(e: Double, M: Double) = throw new UnsupportedOperationException()

	override def getPericenter() = semiLatusRectum() / 2

	override def meanAnomalyFromAnomaly(anomaly: Double) = anomaly + (anomaly * anomaly * anomaly) / 3.0

	override def meanAnomalyFromTrue(nu: Double) = meanAnomalyFromAnomaly(anomaly())

	override def period(n: Double) = Double.PositiveInfinity

	override def semiLatusRectum() = k.getSemiMajorAxis// TODO: better solution ?

	override def totEnergyPerMass(µ: Double, a: Double) = 0

	override def trueAnomalyFromAnomaly(B: Double) = {
		val p = semiLatusRectum()
		val r = KeplerParabola.radius(p, B)
		atan2(p * B, (p - r))
	}

	override def visViva(r: Double) = 2 * k.getCenterbody.getMu / r

}

object KeplerParabola {

	def radius(p: Double, B: Double) = (p / 2) * (1 + B * B)

}
