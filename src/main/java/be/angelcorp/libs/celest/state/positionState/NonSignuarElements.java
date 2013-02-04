/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
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
package be.angelcorp.libs.celest.state.positionState;

import be.angelcorp.libs.celest.body.ICelestialBody;
import be.angelcorp.libs.celest.kepler.*;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.math.MathUtils2;

/**
 * Holds the state using the non-singualar Keplerian elements:
 * 
 * <pre>
 * Elements: { a,   e,    i,   &omega;_true,  &Omega;,    &lambda;_M }
 * Units:    {[m], [-], [rad], [rad], [rad], [rad]}
 * </pre>
 * 
 * <ul>
 * <li><b>a:</b> Semi-major axis [m]
 * <li><b>e:</b> Eccentricity [-]
 * <li><b>i:</b> Inclination [rad]
 * <li><b>&omega;_true:</b> Longitude of perihelion [rad]
 * <li><b>&Omega;:</b> Right ascension of the ascending node [rad]
 * <li><b>&lambda;_M:</b> Mean longitude [rad]
 * </ul>
 * 
 * @author Simon Billemont
 * @see IKeplerElements
 */
public class NonSignuarElements extends PositionState implements IKeplerElements {

	/**
	 * Create a set of {@link NonSignuarElements} from another {@link PositionState}. Chooses itself what
	 * the best way of converting is.
	 * <p>
	 * Guarantees that the return of a {@link NonSignuarElements} {@link PositionState}, but not
	 * necessarily a clone (can be the same {@link PositionState})
	 * </p>
	 * 
	 * @param state
	 *            {@link PositionState} to convert
	 * @param center
	 *            Central body where the {@link NonSignuarElements} are formulated against
	 */
	public static NonSignuarElements as(IPositionState state, ICelestialBody center) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	public static NonSignuarElements fromVector(RealVector vector) {
		if (vector.getDimension() != 6)
			throw new ArithmeticException("Vector must have 6 indices: [a, e, i, omega_true, raan, L_M]");
		return new NonSignuarElements(
				vector.getEntry(0),
				vector.getEntry(1),
				vector.getEntry(2),
				vector.getEntry(3),
				vector.getEntry(4),
				vector.getEntry(5));
	}

	/**
	 * Semi-major axis
	 * <p>
	 * The semi-major axis is one half of the major axis, and thus runs from the center, through a focus,
	 * and to the edge of the ellipse.
	 * </p>
	 * <p>
	 * <b>Unit: [m]</b>
	 * </p>
	 */
	protected double		a;

	/**
	 * Eccentricity
	 * <p>
	 * Eccentricity may be interpreted as a measure of how much this shape deviates from a circle.
	 * </p>
	 * <p>
	 * <b>Unit: [-]</b>
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
	 * <b>Unit: [rad]</b>
	 * </p>
	 */
	protected double		i;
	/**
	 * Longitude of perihelion (&omega;_true)
	 * <p>
	 * L is the angle measured eastward from the vernal equinox (I unit vector) to the eccentricity:
	 * </p>
	 * 
	 * <pre>
	 * cos(&omega;_true) = dot(<1,0,0>, ecc_vec) / |ecc_vec|
	 * &rarr; &omega;_true approx &Omega; + &omega; (if i small)
	 * </pre>
	 * <p>
	 * <b>Unit: [rad]</b>
	 * </p>
	 */
	protected double		omega_true;
	/**
	 * Right ascension of the ascending node (RAAN, &Omega;)
	 * <p>
	 * It is the angle from a reference direction, called the origin of longitude, to the direction of
	 * the ascending node, measured in a reference plane.
	 * </p>
	 * <p>
	 * <b>Unit: [rad]</b>
	 * </p>
	 */
	protected double		raan;

	/**
	 * Mean longditude
	 * <p>
	 * The mean longditude is the location (angle) of the satellite from the vernal exuinox:
	 * </p>
	 * 
	 * <pre>
	 * &lambda;_M = &Omega; + &omega; + M = &omega;_true + M
	 * </pre>
	 * 
	 * 0
	 * <p>
	 * <b>Unit: [rad]</b>
	 * </p>
	 */
	protected double		lambda_M;

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
	 * @param omega_true
	 *            &omega;_true, Argument of pericenter [rad]
	 * @param raan
	 *            &Omega;, Right ascension of ascending node [rad]
	 * @param lambda_M
	 *            &lambda;_M, Mean longditude [rad]
	 */
	public NonSignuarElements(double a, double e, double i, double omega_true, double raan, double lambda_M) {
		this(a, e, i, omega_true, raan, lambda_M, null);
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
	 * @param omega_true
	 *            &omega;_true, Argument of pericenter [rad]
	 * @param raan
	 *            &Omega;, Right ascension of ascending node [rad]
	 * @param lambda_M
	 *            &lambda;_M, Mean longditude [rad]
	 * @param centerbody
	 *            The body that is being orbited by these elements
	 */
	public NonSignuarElements(double a, double e, double i, double omega_true, double raan, double lambda_M,
			CelestialBody centerbody) {
		super();
		this.a = a;
		this.e = e;
		this.i = i;
		this.omega_true = omega_true;
		this.raan = raan;
		this.lambda_M = lambda_M;
		this.centerbody = centerbody;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NonSignuarElements clone() {
		return new NonSignuarElements(a, e, i, omega_true, raan, lambda_M, centerbody);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(IKeplerElements state2) {
		NonSignuarElements ns = NonSignuarElements.as(state2, state2.getCenterbody());
		return equals(ns);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(IKeplerElements state2, double eps) {
		NonSignuarElements ns = NonSignuarElements.as(state2, state2.getCenterbody());
		return equals(ns, eps);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(IPositionState state2) {
		if (NonSignuarElements.class.isAssignableFrom(state2.getClass()))
			return equals((NonSignuarElements) state2);
		else
			return super.equals(state2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(IPositionState state2, double eps) {
		if (NonSignuarElements.class.isAssignableFrom(state2.getClass()))
			return equals((NonSignuarElements) state2, eps);
		else
			return super.equals(state2, eps);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(NonSignuarElements state2) {
		return equals(state2, KeplerEquations.angleTolarance(), 1e-12, KeplerEquations.eccentricityTolarance());
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(NonSignuarElements state2, double eps) {
		return equals(state2, eps, eps, eps);
	}

	public boolean equals(NonSignuarElements state2, double angle_eps, double distance_eps,
			double eccentricity_eps) {
		boolean equal = true;

		// Do the orbit checks
		equal &= Precision.equals(a, state2.getSemiMajorAxis(), a * distance_eps);
		equal &= Precision.equals(e, state2.getEccentricity(), e * eccentricity_eps);
		equal &= MathUtils2.equalsAngle(i, state2.getInclination(), angle_eps);
		equal &= MathUtils2.equalsAngle(omega_true, state2.getLongitudePerihelion(), angle_eps);
		equal &= MathUtils2.equalsAngle(raan, state2.getRaan(), angle_eps);
		equal &= MathUtils2.equalsAngle(lambda_M, state2.getMeanLongditude(), angle_eps);

		return equal;
	}

	/**
	 * Documentation: {@link KeplerElements#omega}
	 * 
	 * @see KeplerElements#omega
	 */
	@Override
	public double getArgumentPeriapsis() {
		return omega_true - raan;
	}

	/**
	 * Documentation: {@link NonSignuarElements#centerbody}
	 * 
	 * @see NonSignuarElements#centerbody
	 */
	@Override
	public CelestialBody getCenterbody() {
		return centerbody;
	}

	/**
	 * Documentation: {@link NonSignuarElements#e}
	 * 
	 * @see NonSignuarElements#e
	 */
	@Override
	public double getEccentricity() {
		return e;
	}

	/**
	 * Documentation: {@link NonSignuarElements#i}
	 * 
	 * @see NonSignuarElements#i
	 */
	@Override
	public double getInclination() {
		return i;
	}

	/**
	 * Documentation: {@link NonSignuarElements#omega_true}
	 * 
	 * @see NonSignuarElements#omega_true
	 */
	public double getLongitudePerihelion() {
		return omega_true;
	}

	/**
	 * Documentation: {@link NonSignuarElements#lambda_M}
	 * 
	 * @see NonSignuarElements#lambda_M
	 */
	public double getMeanLongditude() {
		return lambda_M;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public KeplerEquations getOrbitEqn() {
        KeplerOrbitType type = getOrbitType();
        if (Circular.class.isInstance( type ) )
            return new KeplerCircular(this);
        else if (Elliptical.class.isInstance( type ) )
            return new KeplerEllipse(this);
        else if (Hyperbolic.class.isInstance( type ) )
            return new KeplerHyperbola(this);
        else // if (Parabolic.class.isInstance( type ) )
            return new KeplerParabola(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public KeplerOrbitType getOrbitType() {
		double tol = KeplerEquations.eccentricityTolarance();
		if (e < tol) {
			return new Circular();
		} else if (e < 1 - tol) {
			return new Elliptical();
		} else if (e < 1 + tol) {
			return new Parabolic();
		} else {
			return new Hyperbolic();
		}
	}

	/**
	 * Documentation: {@link NonSignuarElements#raan}
	 * 
	 * @see NonSignuarElements#raan
	 */
	@Override
	public double getRaan() {
		return raan;
	}

	/**
	 * Documentation: {@link NonSignuarElements#a}
	 * 
	 * @see NonSignuarElements#a
	 */
	@Override
	public double getSemiMajorAxis() {
		return a;
	}

	/**
	 * Documentation: {@link KeplerElements#trueA}
	 * 
	 * @see KeplerElements#trueA
	 */
	@Override
	public double getTrueAnomaly() {
		return getOrbitEqn().trueAnomalyFromMean(lambda_M - omega_true);
	}

	public boolean isEquatorial() {
		return Math.abs(getInclination()) < KeplerEquations.angleTolarance();
	}

	/**
	 * Documentation: {@link KeplerElements#omega}
	 * 
	 * @see KeplerElements#omega
	 */
	@Override
	public void setArgumentPeriapsis(double omega) {
		this.omega_true = omega + raan;
	}

	/**
	 * Documentation: {@link NonSignuarElements#centerbody}
	 * 
	 * @see NonSignuarElements#centerbody
	 */
	@Override
	public void setCenterbody(CelestialBody centerbody) {
		this.centerbody = centerbody;
	}

	/**
	 * Documentation: {@link NonSignuarElements#e}
	 * 
	 * @see NonSignuarElements#e
	 */
	@Override
	public void setEccentricity(double e) {
		this.e = e;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setElements(double a, double e, double i, double omega, double raan, double trueA) {
		setSemiMajorAxis(a);
		setEccentricity(e);
		setInclination(i);
		setArgumentPeriapsis(omega);
		setRaan(raan);
		setTrueAnomaly(trueA);
	}

	/**
	 * Documentation: {@link NonSignuarElements#i}
	 * 
	 * @see NonSignuarElements#i
	 */
	@Override
	public void setInclination(double i) {
		this.i = i;
	}

	/**
	 * Documentation: {@link NonSignuarElements#omega_true}
	 * 
	 * @see NonSignuarElements#omega_true
	 */
	public void setLongitudePerihelion(double omega_true) {
		this.omega_true = omega_true;
	}

	/**
	 * Documentation: {@link NonSignuarElements#lambda_M}
	 * 
	 * @see NonSignuarElements#lambda_M
	 */
	public void setMeanLongditude(double lambda_M) {
		this.lambda_M = lambda_M;
	}

	/**
	 * Documentation: {@link NonSignuarElements#raan}
	 * 
	 * @see NonSignuarElements#raan
	 */
	@Override
	public void setRaan(double raan) {
		this.raan = raan;
	}

	/**
	 * Documentation: {@link NonSignuarElements#a}
	 * 
	 * @see NonSignuarElements#a
	 */
	@Override
	public void setSemiMajorAxis(double a) {
		this.a = a;
	}

	/**
	 * Documentation: {@link KeplerElements#trueA}
	 * 
	 * @see KeplerElements#trueA
	 */
	@Override
	public void setTrueAnomaly(double trueA) {
		double M = getOrbitEqn().meanAnomalyFromTrue(trueA);
		this.lambda_M = M + omega_true;
	}

	/**
	 * @see IKeplerElements#toCartesianElements()
	 */
	@Override
	public CartesianElements toCartesianElements() {
		return getOrbitEqn().kepler2cartesian();
	}

	/**
	 * @see IKeplerElements#toVector()
	 */
	@Override
	public RealVector toVector() {
		return new ArrayRealVector(new double[] { a, e, i, omega_true, raan, lambda_M });
	}

}
