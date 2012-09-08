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

import be.angelcorp.libs.math.linear.*;
import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.math.CelestialRotate;
import be.angelcorp.libs.celest.state.positionState.CartesianElements;
import be.angelcorp.libs.celest.state.positionState.ICartesianElements;
import be.angelcorp.libs.celest.state.positionState.IKeplerElements;
import be.angelcorp.libs.celest.state.positionState.KeplerElements;

public abstract class KeplerEquations {

	public static double	angleTolarance			= 1E-6;
	public static double	eccentricityTolarance	= 1E-4;
	public static double	anomalyIterationTol		= 1E-6;

	/**
	 * Compute the argument of latitude. This is the angle between the nodal vector (n) and the radius
	 * vector (r). This is often used for circular inclined orbits, when arguement of pericenter is
	 * undefined.
	 * 
	 * <pre>
	 * u = &nu; + &omega; = cos<sup>-1</sup>( (n_vec . r_vec) / (|n_vec| |r_vec|) )
	 * </pre>
	 * 
	 * @param w
	 *            Argument of pericenter [rad]
	 * @param nu
	 *            True anomaly [rad]
	 * @return Argument of latitude [rad]
	 */
	public static double arguementOfLatitude(double w, double nu) {
		return w + nu;
	}

	/**
	 * Compute the argument of latitude. This is the angle between the nodal vector (n) and the radius
	 * vector (r). This is often used for circular inclined orbits, when arguement of pericenter is
	 * undefined.
	 * 
	 * <pre>
	 * u = &nu; + &omega; = cos<sup>-1</sup>( (n_vec . r_vec) / (|n_vec| |r_vec|) )
	 * </pre>
	 * 
	 * @param nodalVector
	 *            Vector pointing towards the ascending node of the orbit
	 * @param radius
	 *            Vector pointing to the satellite
	 * @return Argument of latitude [rad]
	 */
	public static double arguementOfLatitude(Vector3D nodalVector, Vector3D radius) {
		double u = Math.acos(nodalVector.dot(radius) / (nodalVector.norm() * radius.norm()));
		if (radius.z() < 0)// Checking for quadrant
			u = 2 * Math.PI - u;
		return u;
	}

	/**
	 * Kepler orbital elements ECI Position orbit conversion
	 * <p>
	 * This function converts ECI Cartesian coordinates (R & V) to Kepler orbital elements (a, e, i, O,
	 * w, nu). This function is derived from algorithm 9 on pg. 120 of David A. Vallado's Fundamentals of
	 * Astrodynamics and Applications (2nd Edition)
	 * </p>
	 * 
	 * @param state
	 *            Current Cartesian state of the object relative to the center of the orbited object
	 *            (Radius vector from the center of the center object to the rotating satellite (inertial
	 *            coordinates)
	 * @param body
	 *            Center body begin orbited
	 * @return
	 */
	public static KeplerElements cartesian2kepler(ICartesianElements state, CelestialBody body) {
		KeplerElements k = cartesian2kepler(state, body.getMu());
		k.setCenterbody(body);
		return k;
	}

	/**
	 * Kepler orbital elements ECI Position orbit conversion
	 * <p>
	 * This function converts ECI Cartesian coordinates (R & V) to Kepler orbital elements (a, e, i, O,
	 * w, nu). This function is derived from algorithm 9 on pg. 120 of David A. Vallado's Fundamentals of
	 * Astrodynamics and Applications (2nd Edition)
	 * </p>
	 * 
	 * @param state
	 *            Current Cartesian state of the object relative to the center of the orbited object
	 *            (Radius vector from the center of the center object to the rotating satellite (inertial
	 *            coordinates)
	 * @param mhu
	 *            Gravitational constant of body being orbited
	 * @return
	 */
	public static KeplerElements cartesian2kepler(ICartesianElements state, double mhu) {
		Vector3D R = state.getR();
		Vector3D V = state.getV();

		Vector3D h = R.cross(V);// Specific angular momentum vector
		Vector3D N = Vector3D$.MODULE$.K().cross(h);

		double rNorm = R.norm();
		double vNorm2 = V.normSq();
		double nNorm = N.norm();

		// Eccentricity vector
		Vector3D e_vec = (R.multiply(vNorm2 - mhu / rNorm).$minus(V.multiply(R.dot(V)))).multiply(1 / mhu);
		double ecc = e_vec.norm(); // Magnitude of eccentricity vector

		double zeta = vNorm2 / 2 - mhu / rNorm; // Specific mechanical energy of orbit

		double a;
		if ((1 - Math.abs(ecc)) > eccentricityTolarance) // Checking to see if orbit is parabolic
			a = -mhu / (2 * zeta); // Semi-major axis
		else
			a = Double.POSITIVE_INFINITY;

		double inc = Math.acos(h.z() / h.norm()); // inclination of orbit

		double Omega = Math.acos(N.x() / nNorm); // Right ascension of ascending node
		if (N.y() < 0) // Checking for quadrant
			Omega = 2 * Math.PI - Omega;

		double w = N.dot(e_vec) / (nNorm * ecc);
		w = Math.acos(w); // Argument of perigee
		if (e_vec.z() < 0) // Checking for quadrant
			w = 2 * Math.PI - w;

		double nu = e_vec.angle(R); // True anomaly
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

		KeplerElements k = new KeplerElements(a, ecc, inc, w, Omega, nu);
		fix2dOrbit(k);
		return k;
	}

	/**
	 * Get the 2d kepler elements from a set of Cartesian elements (true anomaly and eccentricity). This
	 * is more optimal if only the true anomaly and eccentricity are needed.
	 * 
	 * @param state
	 *            State of the satellite
	 * @param center
	 *            Center body
	 * @return Subset of kepler elements
	 */
	public static IKeplerElements cartesian2kepler2D(ICartesianElements state, CelestialBody center) {
		Vector3D R = state.getR();
		Vector3D V = state.getV();
		double mhu = center.getMu();

		double rNorm = R.norm();
		double vNorm2 = V.normSq();

		Vector3D e_vec = (R.multiply(vNorm2 - mhu / rNorm).$minus(V.multiply(R.dot(V)))).multiply(1 / mhu);
		double ecc = e_vec.norm();

		double a;
		if ((1 - Math.abs(ecc)) > eccentricityTolarance) // Checking to see if orbit is parabolic
			a = (mhu * rNorm) / (2 * mhu - rNorm * vNorm2);
		else
			a = Double.POSITIVE_INFINITY;

		double nu = Math.acos(e_vec.dot(R) / (ecc * rNorm)); // True anomaly
		if (R.dot(V) < 0) // Checking for quadrant
			nu = 2 * Math.PI - nu;

		return new KeplerElements(a, ecc, Double.NaN, Double.NaN, Double.NaN, nu);
	}

	/**
	 * Compute the eccentricity from an elliptical orbit using the pericenter and apocenter distances
	 * 
	 * @param rp
	 *            Apocenter radius [m]
	 * @param ra
	 *            Pericenter radius [m]
	 * @return eccentricty
	 */
	public static double eccentricity(double rp, double ra) {
		return (ra - rp) / (ra + rp);
	}

	/**
	 * Fixes Argument of pericenter and Right ascension of ascending node (NaN to 0)
	 * 
	 * @param k
	 *            Kepler elements to fix
	 */
	public static void fix2dOrbit(IKeplerElements k) {
		if (Double.isNaN(k.getArgumentPeriapsis()))
			k.setArgumentPeriapsis(0);
		if (Double.isNaN(k.getRaan()))
			k.setRaan(0);
	}

	/**
	 * Compute the flight path angle &gamma; of the instantanius velocity vector. (Angle between the
	 * tangent on the radius vector and the velocity vector
	 * 
	 * @param e
	 *            Eccentricity [-]
	 * @param nu
	 *            True anomaly [rad]
	 * @return Flight path angle &gamma; [rad]
	 */
	public static double flightPathAngle(double e, double nu) {
		return Math.atan(e * Math.sin(nu) / (1 + e * Math.cos(nu)));
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
		double rmag = R.norm();
		double r2 = rmag * rmag;
		double muor3 = mu / (r2 * rmag);
		double jk = 3.0 * muor3 / (r2);

		double xx = R.x();
		double yy = R.y();
		double zz = R.z();

		double gg00 = jk * xx * xx - muor3;
        double gg01 = jk * xx * yy;
        double gg02 = jk * xx * zz;

        double gg10 = gg01;
        double gg11 = jk * yy * yy - muor3;
        double gg12 = jk * yy * zz;

        double gg20 = gg02;
        double gg21 = gg12;
		double gg22 = jk * zz * zz - muor3;

		return new ImmutableMatrix3D(gg00, gg01, gg02, gg10, gg11, gg12, gg20, gg21, gg22);
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
	 * @return Cartesian state vector
	 */
	public static CartesianElements kepler2cartesian(double a, double ecc, double inc, double Omega,
			double w, double nu, double mhu) {
		double p = a * (1 - ecc * ecc);

		// CREATING THE R VECTOR IN THE pqw COORDINATE FRAME
		Vector3D R_pqw = new ImmutableVector3D(
                p * Math.cos(nu) / (1 + ecc * Math.cos(nu)),
				p * Math.sin(nu) / (1 + ecc * Math.cos(nu)),
				0);

		// CREATING THE V VECTOR IN THE pqw COORDINATE FRAME
		Vector3D V_pqw = new ImmutableVector3D(
                -Math.sqrt(mhu / p) * Math.sin(nu),
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
		double rmag = r.norm();
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
	 * @param radius
	 *            Satellite position vector
	 * @return The true longitude
	 */
	public static double trueLongitude(Vector3D radius) {
		double lambda_true = Math.acos(radius.x() / radius.norm());
		if (radius.y() < 0)// Checking for quadrant
			lambda_true = 2 * Math.PI - lambda_true;
		return lambda_true;
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
	 * @param radius
	 *            Satellite position vector
	 * @return The true longitude
	 */
	public static double trueLongitudeApproximation(double raan, double w, double nu) {
		return raan + w + nu;
	}

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
	 * @param RAAN
	 *            Right ascension of the ascending node [rad]
	 * @param w
	 *            Argument of pericenter [RAD]
	 * @return True longitude of periapse [RAD]
	 */
	public static double trueLongitudeOfPeriapse(Vector3D eccentricityVector) {
		double w_true = Math.acos(eccentricityVector.x() / eccentricityVector.norm());
		if (eccentricityVector.y() < 0)// Checking for quadrant
			w_true = 2 * Math.PI - w_true;
		return w_true;
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
	 * @param RAAN
	 *            Right ascension of the ascending node [rad]
	 * @param w
	 *            Argument of pericenter [RAD]
	 * @return True longitude of periapse [RAD]
	 */
	public static double trueLongitudeOfPeriapseApproximate(double RAAN, double w) {
		return RAAN + w;
	}

	/**
	 * Evaluate the Vis Viva equation
	 * <p>
	 * V<sup>2</sup> = &mu; (2/r - 1/a)
	 * </p>
	 * 
	 * @param mu
	 *            Standard gravitational parmeter [m^3/s^2]
	 * @param r
	 *            Radius [m]
	 * @param a
	 *            Semi-major axis [m]
	 * @return Current velocity squared [m/s]
	 */
	public static double visViva(double mu, double r, double a) {
		return mu * (2 / r - 1 / a);
	}

	/**
	 * Kepler elements to compute the equations with
	 */
	protected IKeplerElements	k;

	/**
	 * Create a set of equations for a specific set of Kepler elements. This allows for automatic
	 * substitution of variables in the equations.
	 * 
	 * @param k
	 *            Kepler elements to use in the substitutions.
	 */
	public KeplerEquations(IKeplerElements k) {
		this.k = k;
	}

	/**
	 * Get the Eccentric (E), Parabolic(B) or Hyperbolic(H) anomaly (Depends on which exact orbit which
	 * is returend)
	 * 
	 * @return The Eccentric/Parabolic/Hyperbolic anomaly [rad]
	 */
	public double anomaly() {
		return anomalyFromTrueAnomaly(k.getTrueAnomaly());
	}

	/**
	 * Get the Eccentric (E), Parabolic(B) or Hyperbolic(H) anomaly (Depends on which exact orbit which
	 * is returend) from for the current orbital elements, except for the exact position which is given.
	 * 
	 * @param M
	 *            Mean anomlay to create the anomly from [rad]
	 * 
	 * @return The Eccentric/Parabolic/Hyperbolic anomaly [rad]
	 */
	public abstract double anomalyFromMeanAnomaly(double M);

	/**
	 * Get the Eccentric (E), Parabolic(B) or Hyperbolic(H) anomaly (Depends on which exact orbit which
	 * is returend) from for the current orbital elements, except for the exact position which is given.
	 * 
	 * @param nu
	 *            ,&nu;, True anomlay to create the anomly from [rad]
	 * 
	 * @return The Eccentric/Parabolic/Hyperbolic anomaly [rad]
	 */
	public abstract double anomalyFromTrueAnomaly(double nu);

	/**
	 * Areal velocity in function of Semi-major axis and the gravitational constant
	 * 
	 * @return Areal velocity \dot{A}
	 */
	public double arealVel() {
		return arealVel(k.getCenterbody().getMu(), k.getSemiMajorAxis(), k.getEccentricity());
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
	 * @see KeplerEquations#arguementOfLatitude(double, double)
	 * @return
	 */
	public double arguementOfLatitude() {
		return arguementOfLatitude(k.getArgumentPeriapsis(), k.getTrueAnomaly());
	}

	/**
	 * Compute the flight path angle &gamma; for the current positions. (Angle between the tangent on the
	 * radius vector and the velocity vector
	 * 
	 * @return Flight path angle &gamma; [rad]
	 */
	public double flightPathAngle() {
		return flightPathAngle(k.getEccentricity(), k.getTrueAnomaly());
	}

	/**
	 * Compute the apcocenter distance (distance from the center body to the furthest point in the
	 * orbit).
	 * 
	 * @return Apocenter distance [m]
	 */
	public abstract double getApocenter();

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
	protected abstract DifferentiableUnivariateFunction getFundamentalEquation(final double e, final double M);

	/**
	 * Compute the pericenter distance (distance from the center body to the closest point in the orbit).
	 * 
	 * @return Pericenter distance [m]
	 */
	public abstract double getPericenter();

	/**
	 * This function converts Kepler orbital elements (p,e,i,O,w,nu) to ECI cartesian coordinates. This
	 * function is derived from algorithm 10 on pg. 125 of David A. Vallado's Fundamentals of
	 * Astrodynamics and Applications (2nd Edition) <br/>
	 * 
	 * @param mhu
	 *            Gravitational constant of body being orbited
	 * @return RV in cartesian coordiantes
	 */
	public CartesianElements kepler2cartesian() {
		return kepler2cartesian(k.getSemiMajorAxis(), k.getEccentricity(), k.getInclination(), k
				.getRaan(), k.getArgumentPeriapsis(), k.getTrueAnomaly(), k.getCenterbody().getMu());
	}

	/**
	 * Compute the mean anomaly for the current position of the celestial body
	 * 
	 * @return Mean anomaly [rad]
	 */
	public double meanAnomaly() {
		return meanAnomalyFromAnomaly(anomaly());
	}

	/**
	 * Find the mean anomaly for this set of Kepler elements with the given anomaly (Eccentric E,
	 * Parabolic B, Hyperbolic H).
	 * 
	 * @param anomaly
	 *            Anomaly to find the mean anomaly from [rad]
	 * @return Mean anomaly at the given true anomaly [rad]
	 */
	public abstract double meanAnomalyFromAnomaly(double anomaly);

	/**
	 * Find the mean anomaly for this set of Kepler elements with the given true anomaly.
	 * 
	 * @param nu
	 *            True anomaly to find the mean anomaly from [rad]
	 * @return Mean anomaly at the given true anomaly [rad]
	 */
	public abstract double meanAnomalyFromTrue(double nu);

	/**
	 * Compute the mean motion of the body in the orbit
	 * 
	 * @return Mean motion [rad/s]
	 */
	public double meanMotion() {
		return meanMotion(k.getCenterbody().getMu(), k.getSemiMajorAxis());
	}

	/**
	 * Compute the period of the orbit (sidereal)
	 * 
	 * @param n
	 *            Mean motion [rad/s]
	 * @return Period time [s]
	 */
	public double period() {
		return period(meanMotion());
	}

	/**
	 * Compute the period (sidereal) for a given mean motion
	 * 
	 * @param n
	 *            Mean motion [rad/s]
	 * @return Period time [s]
	 */
	public abstract double period(double n);

	/**
	 * @param p
	 *            Focal parameter
	 * @param e
	 *            Eccentricity [-]
	 * @param nu
	 *            True anomaly [rad]
	 * @return current radius [r]
	 */
	public double radius(double p, double e, double nu) {
		return p / (1 + e * Math.cos(nu));
	}

	/**
	 * Compute the semi-latus rectum (focal parameter) of the orbit
	 * 
	 * @return semi-latus rectum [m]
	 */
	public abstract double semiLatusRectum();

	/**
	 * Compute the total energy per unit mass for the orbit
	 * 
	 * @return Total energy per unit mass [m^2/s^2]
	 */
	public double totEnergyPerMass() {
		return totEnergyPerMass(k.getCenterbody().getMu(), k.getSemiMajorAxis());
	}

	/**
	 * Compute the total energy per unit mass for a given orbit
	 * 
	 * @param mu
	 *            Standard gravitational parameter [m^3/s^2]
	 * @param a
	 *            Semi-major axis [m]
	 * @return Total energy per unit mass [m^2/s^2]
	 */
	public abstract double totEnergyPerMass(double mu, double a);

	/**
	 * Compute the true anomaly from the current anomaly (Eccentric E, Parabolic B, or Hyperbolic H)
	 * 
	 * @return True anomaly [rad]
	 */
	public double trueAnomalyFromAnomaly() {
		return trueAnomalyFromAnomaly(anomaly());
	}

	/**
	 * Compute the true anomaly from the anomaly (Eccentric E, Parabolic B, or Hyperbolic H)
	 * 
	 * @param anomaly
	 *            Anomaly [rad]
	 * @return True anomaly [rad]
	 */
	public abstract double trueAnomalyFromAnomaly(double anomaly);

	/**
	 * Compute the true anomaly from the current mean anomaly
	 * 
	 * @return True anomaly [rad]
	 */
	public double trueAnomalyFromMean() {
		return trueAnomalyFromAnomaly(anomaly());
	}

	/**
	 * Compute the true anomaly from the mean anomaly
	 * 
	 * @param M
	 *            Mean anomaly [rad]
	 * @return True anomaly [rad]
	 */
	public double trueAnomalyFromMean(double M) {
		return trueAnomalyFromAnomaly(anomalyFromMeanAnomaly(M));
	}

	/**
	 * @see KeplerEquations#trueLongitude(Vector3D)
	 */
	public double trueLongitude() {
		double ecc = k.getEccentricity();
		double nu = k.getTrueAnomaly();
		double p = k.getSemiMajorAxis() * (1 - ecc * ecc);
		Vector3D R_pqw = new ImmutableVector3D(p * Math.cos(nu) / (1 + ecc * Math.cos(nu)),
				p * Math.sin(nu) / (1 + ecc * Math.cos(nu)),
				0);
		Vector3D R = CelestialRotate.PQW2ECI(
				k.getArgumentPeriapsis(), k.getRaan(), k.getInclination()).cross(R_pqw);
		return trueLongitude(R);
	}

	/**
	 * @see KeplerEquations#trueLongitudeOfPeriapseApproximate(Vector3D)
	 */
	public double trueLongitudeOfPeriapse() {
		double raan = k.getRaan();
		double w = k.getArgumentPeriapsis();
		double i = k.getInclination();
		Vector3D e_unit_vec = new ImmutableVector3D(
				Math.cos(raan) * Math.cos(w) - Math.cos(i) * Math.sin(raan) * Math.sin(w),
				Math.sin(raan) * Math.cos(w) + Math.cos(i) * Math.cos(raan) * Math.sin(w),
				Math.sin(i) * Math.sin(w));
		return trueLongitudeOfPeriapse(e_unit_vec.multiply(k.getEccentricity()));
	}

	/**
	 * @see KeplerEquations#trueLongitudeOfPeriapseApproximate(double, double)
	 */
	public double trueLongitudeOfPeriapseApproximate() {
		return trueLongitudeOfPeriapseApproximate(k.getRaan(), k.getArgumentPeriapsis());
	}

	/**
	 * Compute the velocity squared in a given point in the orbit (by given r)
	 * 
	 * @param r
	 *            Radius of the position [m]
	 * @return velocity squared [m<sup>2</sup>/s<sup>2</sup>]
	 */
	public double visViva(double r) {
		return visViva(k.getCenterbody().getMu(), r, k.getSemiMajorAxis());
	}
}
