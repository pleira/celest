/**
 * Copyright (C) 2011 simon <aodtorusan@gmail.com>
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
import be.angelcorp.libs.math.MathUtils2;

public class KeplerElements extends StateVector implements Keplerian {

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * Center body of the Kepler elements
	 */
	private CelestialBody	centerbody;

	/**
	 * Create the Kepler elements from direct numerical values
	 * 
	 * @param a
	 *            Semi-major axis [m]
	 * @param e
	 *            Eccentricity [-]
	 * @param i
	 *            Inclination [rad]
	 * @param omega
	 *            Argument of pericenter [rad]
	 * @param raan
	 *            Right ascension of ascending node [rad]
	 * @param trueA
	 *            True anomaly [rad]
	 */
	public KeplerElements(double a, double e, double i, double omega, double raan, double trueA) {
		this(a, e, i, omega, raan, trueA, null);
	}

	/**
	 * Create the Kepler elements from direct numerical values, with a center body to where the values
	 * hold
	 * 
	 * @param a
	 *            Semi-major axis [m]
	 * @param e
	 *            Eccentricity [-]
	 * @param i
	 *            Inclination [rad]
	 * @param omega
	 *            Argument of pericenter [rad]
	 * @param raan
	 *            Right ascension of ascending node [rad]
	 * @param trueA
	 *            True anomaly [rad]
	 * @param centerbody
	 *            The body that is being orbited by these elements
	 */
	public KeplerElements(double a, double e, double i, double omega, double raan, double trueA,
			CelestialBody centerbody) {
		super();
		this.a = a;
		this.e = e;
		this.i = i;
		this.omega = omega;
		this.raan = raan;
		this.trueA = trueA;
		this.centerbody = centerbody;
	}

	/**
	 * Create a set of Kepler elements from another {@link StateVector}. Chooses itself what the best way
	 * of converting is.
	 * 
	 * @param state
	 *            {@link StateVector} to convert
	 * @param center
	 *            Body where the {@link KeplerElements} are formulated against
	 */
	public KeplerElements(StateVector state, CelestialBody center) {
		Class<? extends StateVector> clazz = state.getClass();
		if (SphericalElements.class.isAssignableFrom(clazz)) {
			// See Fundamentals of astrodynamics - II , K.F. Wakker, p 16-4, eqn 16.1-16.7
			SphericalElements s = (SphericalElements) state;
			double mu = center.getMu();
			double rv2m = (s.r * s.v * s.v / mu);
			a = s.r / (2 - rv2m);
			e = Math.sqrt(1 - rv2m * (2 - rv2m) * Math.pow(Math.cos(s.gamma), 2));
			double E = Math.atan2(Math.sqrt(a / mu) * s.r * s.v * Math.sin(s.gamma), a - s.r);
			trueA = 2 * Math.atan(Math.sqrt((1 + e) / (1 - e)) * Math.tan(E / 2));
			trueA = MathUtils2.quadrantFix(trueA, E);
			i = Math.acos(Math.cos(s.delta) * Math.sin(s.psi));
			omega = Math.atan2(Math.sin(s.delta) / Math.sin(i),
					Math.cos(s.delta) * Math.cos(s.psi) / Math.sin(i)) - trueA;
			raan = s.alpha - Math.atan2(Math.tan(s.delta) / Math.tan(i), Math.cos(s.psi) / Math.sin(i));
		} else {
			KeplerElements k2 = KeplerEquations.cartesian2kepler(state.toCartesianElements(), center);
			setElements(k2.a, k2.e, k2.i, k2.omega, k2.raan, k2.trueA);
		}
		setCenterbody(center);
	}

	/**
	 * Create an identical copy of this object, holds the same values, but in a different object.
	 */
	@Override
	public KeplerElements clone() {
		return new KeplerElements(a, e, i, omega, raan, trueA, centerbody);
	}

	/**
	 * Tests if two sets of kepler elements hold the same variables (tests for equal
	 * a,e,i,&omega;,&Omega;,&nu;)
	 * 
	 * @param state2
	 *            Kepler elements to compare with
	 * @return True if the two sets contain the same orbital elements
	 */
	public boolean equals(KeplerElements state2) {
		return state2.getSemiMajorAxis() == a && state2.getEccentricity() == e &&
				state2.getInclination() == i && state2.getArgumentPeriapsis() == omega &&
				state2.getRaan() == raan && state2.getTrueAnomaly() == trueA;
	}

	/**
	 * @see KeplerElements#omega
	 */
	public double getArgumentPeriapsis() {
		return omega;
	}

	/**
	 * @see KeplerElements#centerbody
	 */
	public CelestialBody getCenterbody() {
		return centerbody;
	}

	/**
	 * @see KeplerElements#e
	 */
	public double getEccentricity() {
		return e;
	}

	/**
	 * @see KeplerElements#i
	 */
	public double getInclination() {
		return i;
	}

	/**
	 * Get the specific set of Keplerian equations that can be used with this set of Kepler elements
	 * <p>
	 * This means it returns KeplerCircular equations if the orbit is circular, KeplerEllipse if
	 * elliptical and so forth...
	 * </p>
	 * 
	 * @return A set of KeplerEquations to compute additional parameters with
	 */
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

	/**
	 * Get the type of orbit we are in (circular, elliptical, parabolic or hyperbolic)
	 */
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

	/**
	 * @see KeplerElements#raan
	 */
	public double getRaan() {
		return raan;
	}

	/**
	 * @see KeplerElements#a
	 */
	public double getSemiMajorAxis() {
		return a;
	}

	/**
	 * @see KeplerElements#trueA
	 */
	public double getTrueAnomaly() {
		return trueA;
	}

	/**
	 * @see KeplerElements#omega
	 */
	public void setArgumentPeriapsis(double omega) {
		this.omega = omega;
	}

	/**
	 * @see KeplerElements#centerbody
	 */
	public void setCenterbody(CelestialBody centerbody) {
		this.centerbody = centerbody;
	}

	/**
	 * @see KeplerElements#e
	 */
	public void setEccentricity(double e) {
		this.e = e;
	}

	/**
	 * Sets all the Kepler elements at once
	 * 
	 * @param a
	 *            Semi-major axis {@link KeplerElements#a} [m]
	 * @param e
	 *            Eccentricity {@link KeplerElements#e}[-]
	 * @param i
	 *            Inclination {@link KeplerElements#i} [rad]
	 * @param omega
	 *            Argument of pericenter {@link KeplerElements#omega} [rad]
	 * @param raan
	 *            Right acsention of the ascending node {@link KeplerElements#raan} [rad]
	 * @param trueA
	 *            True anomaly {@link KeplerElements#trueA} [rad]
	 */
	public void setElements(double a, double e, double i, double omega, double raan, double trueA) {
		setSemiMajorAxis(a);
		setEccentricity(e);
		setInclination(i);
		setArgumentPeriapsis(omega);
		setRaan(raan);
		setTrueAnomaly(trueA);
	}

	/**
	 * @see KeplerElements#i
	 */
	public void setInclination(double i) {
		this.i = i;
	}

	/**
	 * @see KeplerElements#raan
	 */
	public void setRaan(double raan) {
		this.raan = raan;
	}

	/**
	 * @see KeplerElements#a
	 */
	public void setSemiMajorAxis(double a) {
		this.a = a;
	}

	/**
	 * @see KeplerElements#trueA
	 */
	public void setTrueAnomaly(double trueA) {
		this.trueA = trueA;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CartesianElements toCartesianElements() {
		return getOrbitEqn().kepler2cartesian();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * [ a, e, i, omega, raan, trueA ]
	 * </p>
	 */
	@Override
	public RealVector toVector() {
		return new ArrayRealVector(new double[] { a, e, i, omega, raan, trueA });
	}

}
