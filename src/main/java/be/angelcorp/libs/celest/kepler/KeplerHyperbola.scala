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
package be.angelcorp.libs.celest.kepler

import math._
import be.angelcorp.libs.celest.state.positionState.IKeplerElements
import org.apache.commons.math3.analysis.{UnivariateFunction, DifferentiableUnivariateFunction}
import org.apache.commons.math3.util.FastMath

class KeplerHyperbola(k: IKeplerElements) extends KeplerEquations(k) {

	override def anomalyFromMeanAnomaly(M: Double) = KeplerHyperbola.hyperbolicAnomalyFromMean(M, k.getEccentricity)
	override def anomalyFromTrueAnomaly(nu: Double) = KeplerHyperbola.anomalyFromTrueAnomaly(nu, k.getEccentricity)

	override def arealVel(µ: Double, a: Double, e: Double) = sqrt(a * µ * (1 - e * e)) / 2

	override def getApocenter() = Double.PositiveInfinity
	override def getPericenter() = k.getSemiMajorAxis * (1 - k.getEccentricity)

	override def getFundamentalEquation(e: Double, M: Double) =
		new DifferentiableUnivariateFunction() {
			override def derivative() =
				new UnivariateFunction() {
					override def value(H: Double) = e * cosh(H) - 1
				}
			override def value(H: Double) = e * sinh(H) - H - M
		}

	override def meanAnomalyFromAnomaly(H: Double) = KeplerHyperbola.meanAnomalyFromAnomaly(H, k.getEccentricity)
	override def meanAnomalyFromTrue(nu: Double) = meanAnomalyFromAnomaly( anomalyFromTrueAnomaly(nu) )

	override def period(n: Double) = Double.PositiveInfinity

	override def semiLatusRectum() = k.getSemiMajorAxis * (1 - k.getEccentricity * k.getEccentricity)

	override def totEnergyPerMass(mu: Double, a: Double) = mu / (2 * a)

	override def trueAnomalyFromAnomaly(H: Double) = KeplerHyperbola.trueAnomalyFromAnomaly(H, k.getEccentricity)

}

object KeplerHyperbola {

	def anomalyFromTrueAnomaly(nu: Double, e: Double) = {
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
		var H=
			if (e < 1.6) {
				if (M > Pi)
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
		var H0 = 0.0
		do {
			H0 = H
			H = H + (M - e * sinh(H) + H) / (e * cosh(H) - 1)
		} while (abs(H - H0) < KeplerEquations.anomalyIterationTol)
		H
	}

	def meanAnomalyFromAnomaly(H: Double, e: Double) = e * sinh(H) - H

	def trueAnomalyFromAnomaly(H: Double, e: Double) = atan2(sqrt(e * e - 1) * sinh(H), e - cosh(H))

}
