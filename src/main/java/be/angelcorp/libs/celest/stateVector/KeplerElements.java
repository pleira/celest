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
package be.angelcorp.libs.celest.stateVector;

import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.MatrixIndexException;
import org.apache.commons.math.linear.RealVector;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.kepler.KeplerCircular;
import be.angelcorp.libs.celest.kepler.KeplerEllipse;
import be.angelcorp.libs.celest.kepler.KeplerEquations;
import be.angelcorp.libs.celest.kepler.KeplerHyperbola;
import be.angelcorp.libs.celest.kepler.KeplerOrbitTypes;
import be.angelcorp.libs.celest.kepler.KeplerParabola;
import be.angelcorp.libs.celest.math.Keplerian;

public class KeplerElements extends StateVector implements Keplerian {

	public static KeplerElements fromVector(RealVector vector) {
		if (vector.getDimension() != 6)
			throw new MatrixIndexException("Vector must have 6 indices: [a, e, i, omega, raan, trueA]");
		double[] d = vector.getData();
		return new KeplerElements(d[0], d[1], d[2], d[3], d[4], d[5]);
	}

	/**
	 * Semi-major axis
	 * <p>
	 * The semi-major axis is one half of the major axis, and thus runs from the centre, through a focus,
	 * and to the edge of the ellipse.
	 * </p>
	 * <p>
	 * <b>[m]</b>
	 * </p>
	 */
	protected double		a;

	/**
	 * Eccentricity
	 * <p>
	 * Eccentricity may be interpreted as a measure of how much this shape deviates from a circle.
	 * </p>
	 */
	protected double		e;

	/**
	 * Inclination
	 * <p>
	 * This element tells you what the angle is between the ecliptic and the orbit. The inclination
	 * ranges from 0 to &pi; rad.
	 * </p>
	 * <p>
	 * <b>[rad]</b>
	 * </p>
	 */
	protected double		i;
	/**
	 * argument of periapsis (&omega;)
	 * <p>
	 * &omega; is the angle between the orbit's periapsis (the point of closest approach to the central
	 * point) and the orbit's ascending node (the point where the body crosses the plane of reference
	 * from South to North). The angle is measured in the orbital plane and in the direction of motion.
	 * </p>
	 * <p>
	 * <b>[rad]</b>
	 * </p>
	 */
	protected double		omega;
	/**
	 * Right ascension of the ascending node (RAAN, &Omega;)
	 * <p>
	 * It is the angle from a reference direction, called the origin of longitude, to the direction of
	 * the ascending node, measured in a reference plane.
	 * </p>
	 * <p>
	 * <b>[rad]</b>
	 * </p>
	 */
	protected double		raan;
	/**
	 * True anomaly
	 * <p>
	 * <b>[rad]</b>
	 * </p>
	 */
	protected double		trueA;

	private CelestialBody	centerbody;

	public KeplerElements(double a, double e, double i, double omega,
			double raan, double trueA) {
		this(a, e, i, omega, raan, trueA, null);
	}

	public KeplerElements(double a, double e, double i, double omega,
			double raan, double trueA, CelestialBody centerbody) {
		super();
		this.a = a;
		this.e = e;
		this.i = i;
		this.omega = omega;
		this.raan = raan;
		this.trueA = trueA;
		this.centerbody = centerbody;
	}

	@Override
	public KeplerElements clone() {
		return new KeplerElements(a, e, i, omega, raan, trueA, centerbody);
	}

	/**
	 * Calculates the eccentric anomaly.
	 * 
	 * @return Eccentric anomaly [rad]
	 */

	public double eccentricAnomaly() {
		double cta = Math.cos(trueA);
		double e0 = Math.acos((e + cta) / (1.0 + e * cta));
		return e0;
	}

	public boolean equals(KeplerElements state2) {
		return state2.getSemiMajorAxis() == a && state2.getEccentricity() == e &&
				state2.getInclination() == i && state2.getArgumentPeriapsis() == omega &&
				state2.getRaan() == raan && state2.getTrueAnomaly() == trueA;
	}

	public double getArgumentPeriapsis() {
		return omega;
	}

	public CelestialBody getCenterbody() {
		return centerbody;
	}

	public double getEccentricity() {
		return e;
	}

	public double getInclination() {
		return i;
	}

	/**
	 * Calculate the mean anomaly.
	 * 
	 * @return Mean anomaly [rad]
	 */

	public double getMeanAnomaly() {
		double ea = eccentricAnomaly();
		double m = ea - e * Math.sin(ea);
		return m;
	}

	public KeplerEquations getOrbitEqn() {
		switch (getOrbitType()) {
			case Circular:
				return new KeplerCircular(this);
			case Elliptical:
				return new KeplerEllipse(this);
			case Parabolic:
				return new KeplerParabola(this);
			case Hyperbolic:
				return new KeplerHyperbola(this);
		}
		throw new UnsupportedOperationException();
	}

	public KeplerOrbitTypes getOrbitType() {
		double tol = KeplerEquations.eccentricityTolarance;
		if (e < tol) {
			return KeplerOrbitTypes.Circular;
		} else if (e < 1 - tol) {
			return KeplerOrbitTypes.Elliptical;
		} else if (e < 1 + tol) {
			return KeplerOrbitTypes.Parabolic;
		} else {
			return KeplerOrbitTypes.Hyperbolic;
		}
	}

	public double getRaan() {
		return raan;
	}

	public double getSemiMajorAxis() {
		return a;
	}

	public double getTrueAnomaly() {
		return trueA;
	}

	public void setArgumentPeriapsis(double omega) {
		this.omega = omega;
	}

	public void setCenterbody(CelestialBody centerbody) {
		this.centerbody = centerbody;
	}

	public void setEccentricity(double e) {
		this.e = e;
	}

	public void setInclination(double i) {
		this.i = i;
	}

	public void setRaan(double raan) {
		this.raan = raan;
	}

	public void setSemiMajorAxis(double a) {
		this.a = a;
	}

	public void setTrueAnomaly(double trueA) {
		this.trueA = trueA;
	}

	@Override
	public CartesianElements toCartesianElements() {
		return getOrbitEqn().kepler2cartesian();
	}

	@Override
	public RealVector toVector() {
		return new ArrayRealVector(new double[] { a, e, i, omega, raan, trueA });
	}

}
