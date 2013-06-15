/**
 * Copyright (C) 2012 Simon Billemont <simon@angelcorp.be>
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

package be.angelcorp.libs.celest

import be.angelcorp.libs.math.linear.{Matrix3D, Vector3D}
import body.CelestialBody
import math.CelestialRotate
import scala.math._
import state.positionState
import state.positionState._

package object kepler {

	/**
	 * Compute the argument of latitude. This is the angle between the nodal vector (n) and the radius
	 * vector (r). This is often used for circular inclined orbits, when argument of pericenter is
	 * undefined.
	 *
	 * <pre>
	 * u = &nu; + &omega; = cos<sup>-1</sup>( (n_vec . r_vec) / (|n_vec| |r_vec|) )
	 * </pre>
	 *
	 * @param w Argument of pericenter [rad]
	 * @param nu True anomaly [rad]
	 * @return Argument of latitude [rad]
	 */
	def arguementOfLatitude(w: Double, nu: Double) = w + nu

	/**
	 * Compute the argument of latitude. This is the angle between the nodal vector (n) and the radius
	 * vector (r). This is often used for circular inclined orbits, when argument of pericenter is
	 * undefined.
	 *
	 * <pre>
	 * u = &nu; + &omega; = cos<sup>-1</sup>( (n_vec . r_vec) / (|n_vec| |r_vec|) )
	 * </pre>
	 *
	 * @param nodalVector Vector pointing towards the ascending node of the orbit
	 * @param radius Vector pointing to the satellite
	 * @return Argument of latitude [rad]
	 */
	def arguementOfLatitude(nodalVector: Vector3D, radius: Vector3D) = {
		val u = acos(nodalVector.dot(radius) / (nodalVector.norm * radius.norm))
		if (radius.z < 0)// Checking for quadrant
			2 * Pi - u
		else
			u
	}

	/**
	 * Kepler orbital elements ECI Position orbit conversion
	 * <p>
	 * This function converts ECI Cartesian coordinates (R & V) to Kepler orbital elements (a, e, i, O,
	 * w, nu). This function is derived from algorithm 9 on pg. 120 of David A. Vallado's Fundamentals of
	 * Astrodynamics and Applications (2nd Edition)
	 * </p>
	 *
	 * @param state Current Cartesian state of the object relative to the center of the orbited object
	 *              (Radius vector from the center of the center object to the rotating satellite (inertial
	 *              coordinates)
	 * @param body Center body begin orbited
	 * @return
	 */
	def cartesian2kepler(state: ICartesianElements, body: CelestialBody): KeplerElements =
		new KeplerElements(kepler.cartesian2kepler(state, body.getMu), body)

	/**
	 * Kepler orbital elements ECI Position orbit conversion
	 * <p>
	 * This function converts ECI Cartesian coordinates (R & V) to Kepler orbital elements (a, e, i, O,
	 * w, nu). This function is derived from algorithm 9 on pg. 120 of David A. Vallado's Fundamentals of
	 * Astrodynamics and Applications (2nd Edition)
	 * </p>
	 *
	 * @param state Current Cartesian state of the object relative to the center of the orbited object
	 *              (Radius vector from the center of the center object to the rotating satellite (inertial
	 *              coordinates)
	 * @param µ Gravitational constant of body being orbited
	 * @return
	 */
	def cartesian2kepler(state: ICartesianElements, µ: Double) = {
		val R = state.getR
		val V = state.getV

		val h = R !* V// Specific angular momentum vector
		val N = Vector3D.K !* h

		val rNorm  = R.norm
		val vNorm2 = V.normSq
		val nNorm  = N.norm

		// Eccentricity vector
		val e_vec = ( (R * (vNorm2 - µ / rNorm)) - (V * R.dot(V)) ) / µ
		val ecc = e_vec.norm // Magnitude of eccentricity vector

		val zeta = vNorm2 / 2 - µ / rNorm; // Specific mechanical energy of orbit

		val a = if ((1 - abs(ecc)) > KeplerEquations.eccentricityTolarance) // Checking to see if orbit is parabolic
			-µ / (2 * zeta); // Semi-major axis
		else
			Double.PositiveInfinity

		val inc = acos(h.z / h.norm) // inclination of orbit

		var Omega = acos(N.x / nNorm) // Right ascension of ascending node
		if (N.y < 0) // Checking for quadrant
			Omega = 2 * Pi - Omega

		var w = acos( N.dot(e_vec) / (nNorm * ecc) ) // Argument of perigee
		if (e_vec.z < 0) // Checking for quadrant
			w = 2 * Pi - w

		var nu = e_vec.angle(R); // True anomaly
		if (R.dot(V) < 0) // Checking for quadrant
			nu = 2 * Pi - nu

		// // /////////// Special Cases \\\\\\\\\\\\
		// // Elliptical Equatorial
		// double w_true = Math.acos(e_vec.getX() / ecc); // True longitude of periapse
		// if (e_vec.getY() < 0)// Checking for quadrant
		// w_true = 2 * pi - w_true;
		//
		// // Circular Inclined
		// double u_true = Math.acos(N.dot(R) / (nNorm * rNorm)); // Argument of
		// // Latitude
		// if (R.getZ() < 0)// Checking for quadrant
		// u_true = 2 * pi - u_true;
		//
		// // Circular Equatorial
		// double lambda_true = Math.acos(R.getX() / rNorm); // True Longitude
		// if (R.getY() < 0)// Checking for quadrant
		// lambda_true = 2 * pi - lambda_true;
		//
		// return new ExtendedKeplerStateVector(
		// lambda_true, ecc, inc, w, Omega, nu, w_true, u_true, lambda_true);

		val k = new KeplerElements(a, ecc, inc, w, Omega, nu)
		fix2dOrbit(k)
	}

	/**
	 * Get the 2d kepler elements from a set of Cartesian elements (true anomaly and eccentricity). This
	 * is more optimal if only the true anomaly and eccentricity are needed.
	 *
	 * @param state State of the satellite
	 * @param center Center body
	 * @return Subset of kepler elements
	 */
	def cartesian2kepler2D(state: ICartesianElements, center: CelestialBody) = {
		val R = state.getR
		val V = state.getV
		val µ = center.getMu

		val rNorm = R.norm
		val vNorm2 = V.normSq

		val e_vec = ( (R * (vNorm2 - µ / rNorm)) - ( V * R.dot(V)) ) / µ
		val ecc = e_vec.norm

		val a = if ((1 - abs(ecc)) > KeplerEquations.eccentricityTolarance) // Checking to see if orbit is parabolic
			(µ * rNorm) / (2 * µ - rNorm * vNorm2)
		else
			Double.PositiveInfinity

		var nu = acos(e_vec.dot(R) / (ecc * rNorm)); // True anomaly
		if (R.dot(V) < 0) // Checking for quadrant
			nu = 2 * Pi - nu

		new KeplerElements(a, ecc, Double.NaN, Double.NaN, Double.NaN, nu)
	}

	/**
	 * Compute the eccentricity from an elliptical orbit using the pericenter and apocenter distances
	 *
	 * @param rp Apocenter radius [m]
	 * @param ra Pericenter radius [m]
	 * @return eccentricty
	 */
	def eccentricity(rp: Double, ra: Double) = (ra - rp) / (ra + rp)

	/**
	 * Fixes Argument of pericenter and Right ascension of ascending node (NaN to 0)
	 *
	 * @param k Kepler elements to fix
	 */
	def fix2dOrbit(k: IKeplerElements) =
		new KeplerElements(
			k.getSemiMajorAxis,
			k.getEccentricity,
			k.getInclination,
			(if (k.getArgumentPeriapsis.isNaN) 0 else k.getArgumentPeriapsis),
			(if (k.getRaan.isNaN) 0 else k.getRaan),
			k.getTrueAnomaly
		)

	/**
	 * Compute the flight path angle &gamma; of the instantanius velocity vector. (Angle between the
	 * tangent on the radius vector and the velocity vector
	 *
	 * @param e Eccentricity [-]
	 * @param nu True anomaly [rad]
	 * @return Flight path angle &gamma; [rad]
	 */
	def flightPathAngle(e: Double, nu: Double) = atan(e * sin(nu) / (1 + e * cos(nu)))

	/**
	 * Calculate the angular momentum vector.
	 *
	 * @return angular momentum vector
	 */
	def getH(R: Vector3D, V: Vector3D) = R !* V

	/**
	 * Compute the gravity gradient matrix (dg/dr).
	 *
	 * @return Gravity gradient matrix.
	 */
	def gravityGradient(R: Vector3D, µ: Double) = {
		val rmag = R.norm
		val r2 = rmag * rmag
		val muor3 = µ / (r2 * rmag)
		val jk = 3.0 * muor3 / (r2)

		val xx = R.x
		val yy = R.y
		val zz = R.z

		val gg00 = jk * xx * xx - muor3
		val gg01 = jk * xx * yy
		val gg02 = jk * xx * zz

		val gg10 = gg01
		val gg11 = jk * yy * yy - muor3
		val gg12 = jk * yy * zz

		val gg20 = gg02
		val gg21 = gg12
		val gg22 = jk * zz * zz - muor3

		Matrix3D(gg00, gg01, gg02, gg10, gg11, gg12, gg20, gg21, gg22)
	}

	/**
	 * This function converts Kepler orbital elements (p,e,i,O,w,nu) to ECI cartesian coordinates. This
	 * function is derived from algorithm 10 on pg. 125 of David A. Vallado's Fundamentals of
	 * Astrodynamics and Applications (2nd Edition) <br/>
	 * <br/>
	 * <b>WARNING:</b>
	 * <ul>
	 * <li>If the orbit is near equatorial and near circular, set w = 0 and Omega = 0 and nu to the true
	 * longitude.</li>
	 * <li>If the orbit is inclined and near circular, set w = 0 and nu to the argument of latitude.</li>
	 * <li>
	 * If the orbit is near equatorial and elliptical, set Omega = 0 and w to the true longitude of
	 * periapse.</li>
	 * </ul>
	 *
	 * @param a Semi-major axis
	 * @param ecc Eccentricity
	 * @param inc Inclination of orbit
	 * @param Omega Right ascension of ascending node
	 * @param w Argument of perigee
	 * @param nu True anomaly
	 * @param µ Gravitational constant of body being orbited
	 * @return Cartesian state vector
	 */
	def kepler2cartesian(a: Double, ecc: Double, inc: Double, Omega: Double, w: Double, nu: Double, µ: Double) = {
		val p = a * (1 - ecc * ecc)

		// CREATING THE R VECTOR IN THE pqw COORDINATE FRAME
		val R_pqw = Vector3D(
			p * cos(nu) / (1 + ecc * cos(nu)),
			p * sin(nu) / (1 + ecc * cos(nu)),
			0)

		// CREATING THE V VECTOR IN THE pqw COORDINATE FRAME
		val V_pqw = Vector3D(
			-sqrt(µ / p) * sin(nu),
			sqrt(µ / p) * (ecc + cos(nu)),
			0)

		// ROTATING THE pqw VECOTRS INTO THE ECI FRAME (ijk)
		val R = CelestialRotate.PQW2ECI(w, Omega, inc) !* R_pqw
		val V = CelestialRotate.PQW2ECI(w, Omega, inc) !* V_pqw
		new CartesianElements(R, V)
	}

	/**
	 * Compute the local gravitational acceleration
	 *
	 * @return g vector
	 */
	def localGravity(r: Vector3D, µ: Double) = {
		val rmag = r.norm
		val muor3 = µ / pow(rmag, 3)
		r * -muor3
	}

	/**
	 * Calculate the mean angular motion
	 *
	 * @return Mean motion in [rad/s]
	 */
	def meanMotion(µ: Double, a: Double) = sqrt(µ / abs(a * a * a))

	/**
	 * Compute the true longitude, the angle from the x-axis to the position of the satellite, measured
	 * eastwards.
	 *
	 * <pre>
	 * cos( &lambda;<sub>true</sub> ) = r_vec<sub>x</sub> / |r_vec|
	 * </pre>
	 * <p>
	 * Note when the orbit is known to be equatorial (low inclination), use
	 * {@link KeplerEquations#trueLongitudeApproximation(double, double, double)} instead as it is
	 * faster.
	 * </p>
	 *
	 * @param radius Satellite position vector
	 * @return The true longitude
	 */
	def trueLongitude(radius: Vector3D) = {
		val lambda_true = acos(radius.x / radius.norm)
		if (radius.y < 0)// Checking for quadrant
			2 * Pi - lambda_true
		else
			lambda_true
	}

	/**
	 * Compute the true longitude, the angle from the x-axis to the position of the satellite, measured
	 * eastwards.
	 *
	 * <pre>
	 * &lambda;<sub>true</sub> &#8776; &Omega; + &omega; + &nu;
	 * </pre>
	 * <p>
	 * Note only use this when the orbit is known to be equatorial (low inclination). Alternatively use
	 * {@link KeplerEquations#trueLongitude(Vector3D))}.
	 * </p>
	 *
	 * @return The true longitude
	 */
	def trueLongitudeApproximation(raan: Double, w: Double, nu: Double) = raan + w + nu

	/**
	 * Return the true longitude of periapse:
	 *
	 * <pre>
	 * cos(w_true) = (a&#773;;c)
	 * </pre>
	 * <p>
	 * When the known to be low, use
	 * {@link KeplerEquations#trueLongitudeOfPeriapseApproximate(double, double)}, as it performs faster.
	 * </p>
	 *
	 * @return True longitude of periapse [RAD]
	 */
	def trueLongitudeOfPeriapse(eccentricityVector: Vector3D) = {
		val w_true = acos(eccentricityVector.x / eccentricityVector.norm)
		if (eccentricityVector.y < 0)// Checking for quadrant
			2 * Pi - w_true
		else
			w_true
	}

	/**
	 * Return an approximation to the true longitude of periapse:
	 *
	 * <pre>
	 * w_true = RAAN + w
	 * </pre>
	 * <p>
	 * Note this approximation is only valid when the inclination i, is low (equatorial). To get accurate
	 * results, use {@link KeplerEquations#trueLongitudeOfPeriapse(Vector3D)}
	 * </p>
	 *
	 * @param RAAN Right ascension of the ascending node [rad]
	 * @param w Argument of pericenter [RAD]
	 * @return True longitude of periapse [RAD]
	 */
	def trueLongitudeOfPeriapseApproximate(RAAN: Double, w: Double) = RAAN + w

	/**
	 * Evaluate the Vis Viva equation
	 * <p>
	 * V<sup>2</sup> = &mu; (2/r - 1/a)
	 * </p>
	 *
	 * @param µ Standard gravitational parmeter [m^3/s^2]
	 * @param r Radius [m]
	 * @param a Semi-major axis [m]
	 * @return Current velocity squared [m/s]
	 */
	def visViva(µ: Double, r: Double, a: Double) = µ * (2 / r - 1 / a)
}
