/**
 * Copyright (C) 2011 Simon Billemont <aodtorusan@gmail.com>
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 3.0 Unported
 * (CC BY-NC 3.0) (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *        http://creativecommons.org/licenses/by-nc/3.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.libs.celest.kepler;

import be.angelcorp.libs.celest.math.CelestialRotate;
import be.angelcorp.libs.celest.stateVector.CartesianElements;
import be.angelcorp.libs.celest.stateVector.KeplerElements;
import be.angelcorp.libs.math.linear.Matrix3D;
import be.angelcorp.libs.math.linear.Vector3D;

public abstract class KeplerEquations {

	public static double	angleTolarance					= 1E-6;
	public static double	eccentricityTolarance			= 1E-3;
	public static double	eccentricAnomalyIterationTol	= 1E-6;

	/**
	 * Kepler orbital elements ECI Position orbit conversion
	 * <p>
	 * This function converts ECI Cartesian coordinates (R & V) to Kepler orbital elements (a, e, i, O,
	 * w, nu). This function is derived from algorithm 9 on pg. 120 of David A. Vallado's Fundamentals of
	 * Astrodynamics and Applications (2nd Edition)
	 * </p>
	 * 
	 * @param R
	 *            Radius vector from the center of the center object to the rotating satellite (inertial
	 *            coordinates)
	 * @param V
	 *            Velocity vector of the satellite wrt the center object
	 * @param mhu
	 *            Gravitational constant of body being orbited
	 * @return
	 */
	public static KeplerElements cartesian2kepler(CartesianElements state, double mhu) {
		Vector3D R = state.getR();
		Vector3D V = state.getV();

		Vector3D h = R.cross(V);// Specific angular momentum vector
		Vector3D N = Vector3D.K.cross(h);

		double rNorm = R.getNorm();
		double vNorm2 = V.getNormSq();
		double nNorm = N.getNorm();

		// Eccentricity vector
		Vector3D e_vec = (R.multiply(vNorm2 - mhu / rNorm).subtract(
				V.multiply(R.dot(V)))).multiply(1 / mhu);
		double ecc = e_vec.getNorm(); // Magnitude of eccentricity vector

		double zeta = vNorm2 / 2 - mhu / rNorm; // Specific mechanical energy of orbit

		double a;
		if ((1 - Math.abs(ecc)) > eccentricityTolarance) // Checking to see if orbit is parabolic
			a = -mhu / (2 * zeta); // Semi-major axis
		else
			a = Double.POSITIVE_INFINITY;

		double inc = Math.acos(h.getZ() / h.getNorm()); // inclination of orbit

		double Omega = Math.acos(N.getX() / nNorm); // Right ascension of ascending node
		if (N.getY() < 0) // Checking for quadrant
			Omega = 2 * Math.PI - Omega;

		double w = N.dot(e_vec) / (nNorm * ecc);
		w = Math.acos(w); // Argument of perigee
		if (e_vec.getZ() < 0) // Checking for quadrant
			w = 2 * Math.PI - w;

		double nu = Math.acos(e_vec.dot(R) / (ecc * rNorm)); // True anomaly
		if (R.dot(V) < 0) // Checking for quadrant
			nu = 2 * Math.PI - nu;

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

		return new KeplerElements(a, ecc, inc, w, Omega, nu);
	}

	/**
	 * Solves for eccentric anomaly, E, from a given mean anomaly, M, and eccentricity, ecc. Performs a
	 * simple Newton-Raphson iteration
	 * 
	 * @param M
	 *            Mean anomaly
	 * @param ecc
	 *            Eccentricity
	 * @return The eccentric anomaly
	 */
	public static double eccentricAnomaly(double M, double ecc) {
		double E;
		if ((M > -Math.PI && M < 0) || M > Math.PI)
			E = M - ecc;
		else
			E = M + ecc;

		double Etemp = E + (M - E + ecc * Math.sin(E)) / (1 - ecc * Math.cos(E));

		while (Math.abs(Etemp - E) > eccentricAnomalyIterationTol) {
			Etemp = E;
			E = Etemp + (M - Etemp + ecc * Math.sin(Etemp)) / (1 - ecc * Math.cos(Etemp));
		}
		return E;
	}

	/**
	 * Calculate the angular momentum vector.
	 * 
	 * @return angular momentum vector
	 */

	public static Vector3D getH(Vector3D R, Vector3D V) {
		return R.cross(V);
	}

	/**
	 * Compute the gravity gradient matrix (dg/dr).
	 * 
	 * @return Gravity gradient matrix.
	 */

	public static Matrix3D gravityGradient(Vector3D R, double mu) {
		double rmag = R.getNorm();
		double r2 = rmag * rmag;
		double muor3 = mu / (r2 * rmag);
		double jk = 3.0 * muor3 / (r2);
		Matrix3D grad = new Matrix3D();

		double xx = R.getX();
		double yy = R.getY();
		double zz = R.getZ();

		double[][] gg = grad.getDataRef();

		gg[0][0] = jk * xx * xx - muor3;
		gg[0][1] = jk * xx * yy;
		gg[0][2] = jk * xx * zz;

		gg[1][0] = gg[0][1];
		gg[1][1] = jk * yy * yy - muor3;
		gg[1][2] = jk * yy * zz;

		gg[2][0] = gg[0][2];
		gg[2][1] = gg[1][2];
		gg[2][2] = jk * zz * zz - muor3;

		return grad;
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
	 * @param a
	 *            Semi-major axis
	 * @param ecc
	 *            Eccentricity
	 * @param inc
	 *            Inclination of orbit
	 * @param Omega
	 *            Right ascension of ascending node
	 * @param w
	 *            Argument of perigee
	 * @param nu
	 *            True anomaly
	 * @param mhu
	 *            Gravitational constant of body being orbited
	 * @return RV in cartesian coordiantes
	 */
	public static CartesianElements kepler2cartesian(double a, double ecc, double inc, double Omega,
			double w, double nu, double mhu) {
		double p = a * (1 - ecc * ecc);

		// CREATING THE R VECTOR IN THE pqw COORDINATE FRAME
		Vector3D R_pqw = new Vector3D(p * Math.cos(nu) / (1 + ecc * Math.cos(nu)),
				p * Math.sin(nu) / (1 + ecc * Math.cos(nu)),
				0);

		// CREATING THE V VECTOR IN THE pqw COORDINATE FRAME
		Vector3D V_pqw = new Vector3D(-Math.sqrt(mhu / p) * Math.sin(nu),
				Math.sqrt(mhu / p) * (ecc + Math.cos(nu)),
				0);

		// ROTATING THE pqw VECOTRS INTO THE ECI FRAME (ijk)
		Vector3D R = CelestialRotate.PQW2ECI(w, Omega, inc).cross(R_pqw);
		Vector3D V = CelestialRotate.PQW2ECI(w, Omega, inc).cross(V_pqw);
		return new CartesianElements(R, V);
	}

	/**
	 * Compute the local gravitational acceleration
	 * 
	 * @return g vector
	 */

	public static Vector3D localGravity(Vector3D r, double mu) {
		double rmag = r.getNorm();
		double muor3 = mu / (rmag * rmag * rmag);

		Vector3D g = r.multiply(-muor3);
		return g;
	}

	/**
	 * Calculate the mean angular motion
	 * 
	 * @return Mean motion in [rad/s]
	 */
	public static double meanMotion(double mu, double a) {
		return Math.sqrt(mu / Math.abs(a * a * a));
	}

	private KeplerElements	k;

	public KeplerEquations(KeplerElements k) {
		this.k = k;
	}

	public double arealVel(double mu) {
		return arealVel(mu, k.getSemiMajorAxis(), k.getEccentricity());
	}

	/**
	 * Areal velocity in function of Semi-major axis and the gravitational constant
	 * 
	 * @param mu
	 *            Gravitational constant
	 * @param a
	 *            Semi-major axis
	 * @return Areal velocity \dot{A}
	 */
	public abstract double arealVel(double mu, double a, double e);

	/**
	 * Solves for eccentric anomaly, E, from a given mean anomaly, M, and eccentricity, ecc. Performs a
	 * simple Newton-Raphson iteration
	 * 
	 * @param M
	 *            Mean anomaly
	 * @param ecc
	 *            Eccentricity
	 * @return The eccentric anomaly
	 */
	public double eccentricAnomaly() {
		return eccentricAnomaly(k.getMeanAnomaly(), k.getEccentricity());
	}

	/**
	 * This function converts Kepler orbital elements (p,e,i,O,w,nu) to ECI cartesian coordinates. This
	 * function is derived from algorithm 10 on pg. 125 of David A. Vallado's Fundamentals of
	 * Astrodynamics and Applications (2nd Edition) <br/>
	 * 
	 * @param mhu
	 *            Gravitational constant of body being orbited
	 * @return RV in cartesian coordiantes
	 */
	public CartesianElements kepler2cartesian(double mhu) {
		return kepler2cartesian(k.getSemiMajorAxis(), k.getEccentricity(), k.getInclination(), k
				.getRaan(), k.getArgumentPeriapsis(), k.getTrueAnomaly(), mhu);
	}

	public double perifocalDistance() {
		return perifocalDistance(k.getSemiMajorAxis(), k.getEccentricity());
	}

	public abstract double perifocalDistance(double a, double e);

	public abstract double period(double n);

	public double totEnergyPerMass(double mu) {
		return totEnergyPerMass(mu, k.getSemiMajorAxis());
	}

	public abstract double totEnergyPerMass(double mu, double a);

	public double velocitySq(double mu, double r) {
		return velocitySq(mu, r, k.getSemiMajorAxis());
	}

	public abstract double velocitySq(double mu, double r, double a);

}
