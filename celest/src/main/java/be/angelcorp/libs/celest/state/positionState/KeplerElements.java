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
package be.angelcorp.libs.celest.state.positionState;

import be.angelcorp.libs.celest.kepler.*;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.math.MathUtils2;

/**
 * Documentation: {@link IKeplerElements}
 * 
 * @author Simon Billemont
 * @see IKeplerElements
 */
public class KeplerElements extends PositionState implements IKeplerElements {

	/**
	 * Create a set of Kepler elements from another {@link PositionState}. Chooses itself what the best
	 * way of converting is.
	 * <p>
	 * Guarantees that the return of a {@link KeplerElements} {@link PositionState}, but not necessarily
	 * a clone (can be the same {@link PositionState})
	 * </p>
	 * 
	 * @param state
	 *            {@link PositionState} to convert
	 * @param center
	 *            Body where the {@link KeplerElements} are formulated against
	 */
	public static IKeplerElements as(IPositionState state, CelestialBody center) {
		Class<? extends IPositionState> clazz = state.getClass();
		if (IKeplerElements.class.isAssignableFrom(clazz)) {
			IKeplerElements k2 = (IKeplerElements) state;
			if (k2.getCenterbody().equals(center))
				return k2;
			else {
				// TODO: more native implementation
			}
		} else if (ISphericalElements.class.isAssignableFrom(clazz)) {
			// See Fundamentals of astrodynamics - II , K.F. Wakker, p 16-4, eqn 16.1-16.7
			SphericalElements s = (SphericalElements) state;
			double mu = center.getMu();
			double rv2m = (s.r * s.v * s.v / mu);
			double a = s.r / (2 - rv2m);
			double e = Math.sqrt(1 - rv2m * (2 - rv2m) * Math.pow(Math.cos(s.gamma), 2));
			double E = Math.atan2(Math.sqrt(a / mu) * s.r * s.v * Math.sin(s.gamma), a - s.r);
			double trueA = 2 * Math.atan(Math.sqrt((1 + e) / (1 - e)) * Math.tan(E / 2));
			trueA = MathUtils2.quadrantFix(trueA, E);
			double i = Math.acos(Math.cos(s.delta) * Math.sin(s.psi));
			double omega = Math.atan2(Math.sin(s.delta) / Math.sin(i),
					Math.cos(s.delta) * Math.cos(s.psi) / Math.sin(i)) - trueA;
			double raan = s.alpha
					- Math.atan2(Math.tan(s.delta) / Math.tan(i), Math.cos(s.psi) / Math.sin(i));
			return new KeplerElements(a, e, i, omega, raan, trueA, center);
		}
		ICartesianElements c = state.toCartesianElements();
		return package$.MODULE$.cartesian2kepler(c, center);
	}

	/**
	 * {@inheritDoc}
	 */
	public static KeplerElements fromVector(RealVector vector) {
		if (vector.getDimension() != 6)
			throw new ArithmeticException("Vector must have 6 indices: [a, e, i, omega, raan, trueA]");
		return new KeplerElements(
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
	 * The semi-major axis is one half of the major axis, and thus runs from the centre, through a focus,
	 * and to the edge of the ellipse.
	 * </p>
	 * <b> Parabolic orbits;
	 * <p>
	 * If the orbit is parabolic, this parameter represents the semi-latus rectem for the parabolic
	 * orbit!
	 * </p>
	 * </b>
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
	 * Create a new set of {@link KeplerElements}, identical to the given set of elements, except for the
	 * fast variable (True anomaly &nu;), which is provided by the user.
	 * 
	 * @param k
	 *            Basis for all the constant Kepler elements.
	 * @param trueA
	 *            New value for the true anomaly [rad].
	 */
	public KeplerElements(IKeplerElements k, double trueA) {
		this(k.getSemiMajorAxis(),
				k.getEccentricity(),
				k.getInclination(),
				k.getArgumentPeriapsis(),
				k.getRaan(),
				trueA,
				k.getCenterbody());
	}

    /**
     * Create a new set of {@link KeplerElements}, identical to the given set of elements, except for the
     * centerbody, which is provided by the user.
     *
     * @param k
     *            Basis for all the constant Kepler elements.
     * @param centerbody
     *            New value for the center body.
     */
    public KeplerElements(IKeplerElements k, CelestialBody centerbody) {
        this(k.getSemiMajorAxis(),
                k.getEccentricity(),
                k.getInclination(),
                k.getArgumentPeriapsis(),
                k.getRaan(),
                k.getTrueAnomaly(),
                centerbody);
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public KeplerElements clone() {
		return new KeplerElements(a, e, i, omega, raan, trueA, centerbody);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(IKeplerElements state2) {
		return equals(state2, KeplerEquations.angleTolarance(), 1e-10, KeplerEquations.eccentricityTolarance());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(IKeplerElements state2, double eps) {
		return equals(state2, eps, eps, eps);
	}

	public boolean equals(IKeplerElements state2, double angle_eps, double distance_eps,
			double eccentricity_eps) {
		boolean equal = true;

		// Do the orbit checks
		equal &= Precision.equals(a, state2.getSemiMajorAxis(), a * distance_eps);
		equal &= Precision.equals(e, state2.getEccentricity(), e * eccentricity_eps);
		equal &= MathUtils2.equalsAngle(i, state2.getInclination(), angle_eps);

		if (!equal) // Return if false, else do more checks
			return false;

		// Due to special orbits, the specific elements might not be the same, but they can still
		// represent the same orbit
		if (isEquatorial()) {
			if (Circular.class.isInstance( getOrbitType() ) ) {
				// No raan / w defined, use true longitude instead
				equal &= MathUtils2.equalsAngle(
						getOrbitEqn().trueLongitude(), state2.getOrbitEqn().trueLongitude(), angle_eps);
			} else {
				// No raan, use true longitude of periapsis instead
				equal &= MathUtils2.equalsAngle(getOrbitEqn().trueLongitudeOfPeriapse(),
						state2.getOrbitEqn().trueLongitudeOfPeriapse(), angle_eps);
				equal &= MathUtils2.equalsAngle(omega, state2.getArgumentPeriapsis(), angle_eps);
			}
		} else {
            if (Circular.class.isInstance( getOrbitType() ) ) {
				// No w defined, use argument of latitude instead
				equal &= MathUtils2.equalsAngle(getOrbitEqn().arguementOfLatitude(),
						state2.getOrbitEqn().arguementOfLatitude(), angle_eps);
				equal &= MathUtils2.equalsAngle(raan, state2.getRaan(), angle_eps);
			} else {
				// w, raan, nu are properly defined
				equal &= MathUtils2.equalsAngle(raan, state2.getRaan(), angle_eps);
				equal &= MathUtils2.equalsAngle(omega, state2.getArgumentPeriapsis(), angle_eps);
				equal &= MathUtils2.equalsAngle(trueA, state2.getTrueAnomaly(), angle_eps);
			}
		}
		return equal;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(IPositionState state2) {
		if (IKeplerElements.class.isAssignableFrom(state2.getClass()))
			return equals((IKeplerElements) state2);
		else
			return super.equals(state2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(IPositionState state2, double eps) {
		if (IKeplerElements.class.isAssignableFrom(state2.getClass()))
			return equals((IKeplerElements) state2, eps);
		else
			return super.equals(state2, eps);
	}

	/**
	 * Documentation: {@link KeplerElements#omega}
	 * 
	 * @see KeplerElements#omega
	 */
	@Override
	public double getArgumentPeriapsis() {
		return omega;
	}

	/**
	 * Documentation: {@link KeplerElements#centerbody}
	 * 
	 * @see KeplerElements#centerbody
	 */
	@Override
	public CelestialBody getCenterbody() {
		return centerbody;
	}

	/**
	 * Documentation: {@link KeplerElements#e}
	 * 
	 * @see KeplerElements#e
	 */
	@Override
	public double getEccentricity() {
		return e;
	}

	/**
	 * Documentation: {@link KeplerElements#i}
	 * 
	 * @see KeplerElements#i
	 */
	@Override
	public double getInclination() {
		return i;
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
	 * Documentation: {@link KeplerElements#raan}
	 * 
	 * @see KeplerElements#raan
	 */
	@Override
	public double getRaan() {
		return raan;
	}

	/**
	 * Documentation: {@link KeplerElements#a}
	 * 
	 * @see KeplerElements#a
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
		return trueA;
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
		this.omega = omega;
	}

	/**
	 * Documentation: {@link KeplerElements#centerbody}
	 * 
	 * @see KeplerElements#centerbody
	 */
	@Override
	public void setCenterbody(CelestialBody centerbody) {
		this.centerbody = centerbody;
	}

	/**
	 * Documentation: {@link KeplerElements#e}
	 * 
	 * @see KeplerElements#e
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
	 * Documentation: {@link KeplerElements#i}
	 * 
	 * @see KeplerElements#i
	 */
	@Override
	public void setInclination(double i) {
		this.i = i;
	}

	/**
	 * Documentation: {@link KeplerElements#raan}
	 * 
	 * @see KeplerElements#raan
	 */
	@Override
	public void setRaan(double raan) {
		this.raan = raan;
	}

	/**
	 * Documentation: {@link KeplerElements#a}
	 * 
	 * @see KeplerElements#a
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
		this.trueA = trueA;
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
		return new ArrayRealVector(new double[] { a, e, i, omega, raan, trueA });
	}

}
