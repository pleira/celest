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

import be.angelcorp.libs.celest.state.positionState.IKeplerElements
import scala.math._
import org.apache.commons.math3.analysis.{UnivariateFunction, DifferentiableUnivariateFunction}
import be.angelcorp.libs.math.MathUtils2

class KeplerEllipse(k: IKeplerElements) extends KeplerEquations(k) {

	override def anomaly() = anomalyFromTrueAnomaly(k.getTrueAnomaly)
	override def anomalyFromMeanAnomaly(M: Double)  = KeplerEllipse.eccentricAnomaly(M, k.getEccentricity)
	override def anomalyFromTrueAnomaly(nu: Double) = KeplerEllipse.eccentricAnomalyFromTrue(nu, k.getEccentricity)

	override def arealVel(µ: Double, a: Double, e: Double) = sqrt(a * µ * (1 - e * e)) / 2

	/**
	 * Solves for eccentric anomaly, E, from a given mean anomaly, M, and eccentricity, ecc. Performs a
	 * simple Newton-Raphson iteration
	 * 
	 * @return The eccentric anomaly
	 */
	def eccentricAnomaly() =
		KeplerEllipse.eccentricAnomaly(meanAnomalyFromTrue(k.getTrueAnomaly), k.getEccentricity)

	override def getFundamentalEquation(e: Double, M: Double) =
		new DifferentiableUnivariateFunction(){
			override def derivative() = {
				new UnivariateFunction() {
					override def value(H: Double) =1 - e * cos(H)
				}
			}
			override def value(E: Double) = E - e * sin(E) - M
		}

	override def getApocenter()  = k.getSemiMajorAxis * (1 + k.getEccentricity)
	override def getPericenter() = k.getSemiMajorAxis * (1 - k.getEccentricity)

	override def meanAnomalyFromAnomaly(E: Double) = E - k.getEccentricity * sin(E)
	override def meanAnomalyFromTrue(nu: Double)   = meanAnomalyFromAnomaly( anomalyFromTrueAnomaly(nu) )

	override def period(n: Double)  = 2 * Pi / n

	override def semiLatusRectum()  = k.getSemiMajorAxis * (1 - k.getEccentricity * k.getEccentricity)

	override def totEnergyPerMass(µ: Double, a: Double) = -µ / (2 * a)

	override def trueAnomalyFromAnomaly(E: Double) = {
		val e = k.getEccentricity
		atan2(sin(E) * sqrt(1 - e * e), cos(E) - e)
	}

}

object KeplerEllipse {

	/**
	 * Solves for eccentric anomaly, E, from a given mean anomaly, M, and eccentricity, ecc. Performs a
	 * simple Newton-Raphson iteration
	 *
	 * @param MA Mean anomaly
	 * @param e Eccentricity
	 * @return The eccentric anomaly
	 */
	def eccentricAnomaly(MA: Double, e: Double) = {
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
	def eccentricAnomalyFromTrue(nu: Double, e: Double)  = atan2( sqrt(1 - e * e) * sin(nu), e + cos(nu))

}
