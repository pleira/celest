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

import be.angelcorp.libs.celest.state.positionState._
import be.angelcorp.libs.math.linear.Vector3D
import be.angelcorp.libs.celest.math.CelestialRotate
import scala.math._
import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction
import be.angelcorp.libs.celest.kepler


abstract class KeplerEquations(k: IKeplerElements) {

	/**
	 * Get the Eccentric (E), Parabolic(B) or Hyperbolic(H) anomaly (Depends on which exact orbit which
	 * is returend)
	 * 
	 * @return The Eccentric/Parabolic/Hyperbolic anomaly [rad]
	 */
	def anomaly() = anomalyFromTrueAnomaly(k.getTrueAnomaly())

	/**
	 * Get the Eccentric (E), Parabolic(B) or Hyperbolic(H) anomaly (Depends on which exact orbit which
	 * is returend) from for the current orbital elements, except for the exact position which is given.
	 * 
	 * @param M Mean anomlay to create the anomly from [rad]
	 * 
	 * @return The Eccentric/Parabolic/Hyperbolic anomaly [rad]
	 */
	def anomalyFromMeanAnomaly(M: Double): Double

	/**
	 * Get the Eccentric (E), Parabolic(B) or Hyperbolic(H) anomaly (Depends on which exact orbit which
	 * is returend) from for the current orbital elements, except for the exact position which is given.
	 * 
	 * @param nu, &nu;, True anomlay to create the anomly from [rad]
	 * 
	 * @return The Eccentric/Parabolic/Hyperbolic anomaly [rad]
	 */
	def anomalyFromTrueAnomaly(nu: Double): Double

	/**
	 * Areal velocity in function of Semi-major axis and the gravitational constant
	 * 
	 * @return Areal velocity \dot{A}
	 */
	def arealVel(): Double = arealVel(k.getCenterbody.getMu, k.getSemiMajorAxis, k.getEccentricity)

	/**
	 * Areal velocity in function of Semi-major axis and the gravitational constant
	 * 
	 * @param µ Gravitational constant
	 * @param a Semi-major axis
	 * @param e Eccentricity
	 * @return Areal velocity \dot{A}
	 */
	def arealVel(µ: Double, a: Double, e: Double): Double

	/**
	 * @see KeplerEquations#arguementOfLatitude(double, double)
	 * @return
	 */
	def arguementOfLatitude() = kepler.arguementOfLatitude(k.getArgumentPeriapsis(), k.getTrueAnomaly())

	/**
	 * Compute the flight path angle &gamma; for the current positions. (Angle between the tangent on the
	 * radius vector and the velocity vector
	 * 
	 * @return Flight path angle &gamma; [rad]
	 */
	def flightPathAngle() = kepler.flightPathAngle(k.getEccentricity(), k.getTrueAnomaly())

	/**
	 * Compute the apcocenter distance (distance from the center body to the furthest point in the
	 * orbit).
	 * 
	 * @return Apocenter distance [m]
	 */
	def getApocenter(): Double

	/**
	 * Create a functional representation of the Fundamental (Kepler) Equation linling the
	 * (Hyperbolic/Parabolic/Eccentric) anomlay to the mean anomaly. This is done in the form of a
	 * root-finding function so that when f(anomlay) = 0 when the Mean anomaly to construct the eqyation
	 * and the anomlay passed in are equivalent.
	 * 
	 * @param e
	 * @param M
	 * @return
	 */
	protected def getFundamentalEquation(e: Double, M: Double): DifferentiableUnivariateFunction

	/**
	 * Compute the pericenter distance (distance from the center body to the closest point in the orbit).
	 * 
	 * @return Pericenter distance [m]
	 */
	def getPericenter(): Double

	/**
	 * This function converts Kepler orbital elements (p,e,i,O,w,nu) to ECI cartesian coordinates. This
	 * function is derived from algorithm 10 on pg. 125 of David A. Vallado's Fundamentals of
	 * Astrodynamics and Applications (2nd Edition) <br/>
	 *
	 * @return RV in cartesian coordiantes
	 */
	def kepler2cartesian() =
		kepler.kepler2cartesian(k.getSemiMajorAxis, k.getEccentricity, k.getInclination,
			k.getRaan, k.getArgumentPeriapsis, k.getTrueAnomaly, k.getCenterbody.getMu)

	/**
	 * Compute the mean anomaly for the current position of the celestial body
	 * 
	 * @return Mean anomaly [rad]
	 */
	def meanAnomaly() = meanAnomalyFromAnomaly(anomaly())

	/**
	 * Find the mean anomaly for this set of Kepler elements with the given anomaly (Eccentric E,
	 * Parabolic B, Hyperbolic H).
	 * 
	 * @param anomaly Anomaly to find the mean anomaly from [rad]
	 * @return Mean anomaly at the given true anomaly [rad]
	 */
	def meanAnomalyFromAnomaly(anomaly: Double): Double

	/**
	 * Find the mean anomaly for this set of Kepler elements with the given true anomaly.
	 * 
	 * @param nu True anomaly to find the mean anomaly from [rad]
	 * @return Mean anomaly at the given true anomaly [rad]
	 */
	def meanAnomalyFromTrue(nu: Double): Double

	/**
	 * Compute the mean motion of the body in the orbit
	 * 
	 * @return Mean motion [rad/s]
	 */
	def meanMotion() = kepler.meanMotion(k.getCenterbody.getMu, k.getSemiMajorAxis)

	/**
	 * Compute the period of the orbit (sidereal)
	 *
	 * @return Period time [s]
	 */
	def period(): Double = period( meanMotion() )

	def periodMu(µ: Double) = period(kepler.meanMotion(µ, k.getSemiMajorAxis))

	/**
	 * Compute the period (sidereal) for a given mean motion
	 * 
	 * @param n Mean motion [rad/s]
	 * @return Period time [s]
	 */
	def period(n: Double): Double

	/**
	 * @param p Focal parameter
	 * @param e Eccentricity [-]
	 * @param nu True anomaly [rad]
	 * @return current radius [r]
	 */
	def radius(p: Double, e: Double, nu: Double) = p / (1 + e * cos(nu))

	/**
	 * Compute the semi-latus rectum (focal parameter) of the orbit
	 * 
	 * @return semi-latus rectum [m]
	 */
	def semiLatusRectum(): Double

	/**
	 * Compute the total energy per unit mass for the orbit
	 * 
	 * @return Total energy per unit mass [m^2/s^2]
	 */
	def totEnergyPerMass(): Double = totEnergyPerMass(k.getCenterbody.getMu, k.getSemiMajorAxis)

	/**
	 * Compute the total energy per unit mass for a given orbit
	 * 
	 * @param µ Standard gravitational parameter [m^3/s^2]
	 * @param a Semi-major axis [m]
	 * @return Total energy per unit mass [m^2/s^2]
	 */
	def totEnergyPerMass(µ: Double, a: Double): Double

	/**
	 * Compute the true anomaly from the current anomaly (Eccentric E, Parabolic B, or Hyperbolic H)
	 * 
	 * @return True anomaly [rad]
	 */
	def trueAnomalyFromAnomaly(): Double = trueAnomalyFromAnomaly(anomaly())

	/**
	 * Compute the true anomaly from the anomaly (Eccentric E, Parabolic B, or Hyperbolic H)
	 * 
	 * @param anomaly Anomaly [rad]
	 * @return True anomaly [rad]
	 */
	def trueAnomalyFromAnomaly(anomaly: Double): Double

	/**
	 * Compute the true anomaly from the current mean anomaly
	 * 
	 * @return True anomaly [rad]
	 */
	def trueAnomalyFromMean() = trueAnomalyFromAnomaly(anomaly())

	/**
	 * Compute the true anomaly from the mean anomaly
	 * 
	 * @param M Mean anomaly [rad]
	 * @return True anomaly [rad]
	 */
	def trueAnomalyFromMean(M: Double) = trueAnomalyFromAnomaly(anomalyFromMeanAnomaly(M))

	/**
	 * @see KeplerEquations#trueLongitude(Vector3D)
	 */
	def trueLongitude() = {
		val e  = k.getEccentricity
		val nu = k.getTrueAnomaly
		val p  = k.getSemiMajorAxis* (1 - e * e)
		val R_pqw = Vector3D(p * cos(nu) / (1 + e * cos(nu)), p * sin(nu) / (1 + e * cos(nu)), 0)
		val R = CelestialRotate.PQW2ECI(k.getArgumentPeriapsis, k.getRaan, k.getInclination) !* R_pqw
		kepler.trueLongitude(R)
	}

	/**
	 * @see KeplerEquations#trueLongitudeOfPeriapseApproximate(Vector3D)
	 */
	def trueLongitudeOfPeriapse() = {
		val raan = k.getRaan
		val w = k.getArgumentPeriapsis
		val i = k.getInclination
		val e_unit_vec = Vector3D(
				cos(raan) * cos(w) - cos(i) * sin(raan) * sin(w),
				sin(raan) * cos(w) + cos(i) * cos(raan) * sin(w),
				sin(i) * sin(w))
		kepler.trueLongitudeOfPeriapse(e_unit_vec * k.getEccentricity)
	}

	/**
	 * @see KeplerEquations#trueLongitudeOfPeriapseApproximate(double, double)
	 */
	def trueLongitudeOfPeriapseApproximate() = kepler.trueLongitudeOfPeriapseApproximate(k.getRaan, k.getArgumentPeriapsis)

	/**
	 * Compute the velocity squared in a given point in the orbit (by given r)
	 * 
	 * @param r
	 *            Radius of the position [m]
	 * @return velocity squared [m<sup>2</sup>/s<sup>2</sup>]
	 */
	def visViva(r: Double) = kepler.visViva(k.getCenterbody.getMu, r, k.getSemiMajorAxis)
}

object KeplerEquations {

	var angleTolarance				= 1E-6
	var eccentricityTolarance	= 1E-4
	var anomalyIterationTol		= 1E-6

}

