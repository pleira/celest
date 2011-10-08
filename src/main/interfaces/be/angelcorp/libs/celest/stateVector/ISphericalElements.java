package be.angelcorp.libs.celest.stateVector;

import org.apache.commons.math.linear.RealVector;

import be.angelcorp.libs.celest.body.CelestialBody;

/**
 * Spherical elements are basically the length of the radius r and velocity vector v, and its orientation
 * with some right ascension &alpha;/flight path azimuth &psi; and declination &delta;/flight path angle
 * &gamma;
 * 
 * <pre>
 * Elements: { r,    &alpha;,     &delta;,     V,     &gamma;,     &psi;  }
 * Units:    {[m], [rad], [rad], [m/s], [rad], [rad]}
 * </pre>
 * 
 * <p>
 * This is similar to ADBRAV {&alpha;,&delta;,&beta;,r,&psi;,v} elements where &beta; = 90 - &gamma;
 * </p>
 * 
 * @author Simon Billemont
 * 
 */
public interface ISphericalElements extends IStateVector {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISphericalElements clone();

	/**
	 * Tests if two sets of {@link SphericalElements} hold the same variables (tests for equal
	 * r,&alpha;,&delta;,V,&gamma;,&psi;)
	 * 
	 * @param state2
	 *            {@link SphericalElements} to compare with
	 * @return True if the two sets contain the same orbital elements
	 */
	public abstract boolean equals(SphericalElements state2);

	/**
	 * @see SphericalElements#centerbody
	 */
	public abstract CelestialBody getCenterbody();

	/**
	 * @see SphericalElements#delta
	 */
	public abstract double getDeclination();

	/**
	 * @see SphericalElements#gamma
	 */
	public abstract double getFlightPathAngle();

	/**
	 * @see SphericalElements#psi
	 */
	public abstract double getFlightPathAzimuth();

	/**
	 * @see SphericalElements#r
	 */
	public abstract double getRadius();

	/**
	 * @see SphericalElements#alpha
	 */
	public abstract double getRightAscension();

	/**
	 * @see SphericalElements#c
	 */
	public abstract double getVelocity();

	/**
	 * @see SphericalElements#centerbody
	 */
	public abstract void setCenterbody(CelestialBody centerbody);

	/**
	 * @see SphericalElements#delta
	 */
	public abstract void setDeclination(double delta);

	/**
	 * Sets all the {@link SphericalElements} at once
	 * 
	 * @param r
	 *            Radius length [m]
	 * @param alpha
	 *            Right ascension [rad]
	 * @param delta
	 *            Declination [rad]
	 * @param v
	 *            Velocity [m/S]
	 * @param gamma
	 *            Flight path angle [rad]
	 * @param trueA
	 *            Flight path azimuth [rad]
	 */
	public abstract void setElements(double r, double alpha, double delta, double v, double gamma, double psi);

	/**
	 * @see SphericalElements#gamma
	 */
	public abstract void setFlightPathAngle(double gamma);

	/**
	 * @see SphericalElements#psi
	 */
	public abstract void setFlightPathAzimuth(double psi);

	/**
	 * @see SphericalElements#r
	 */
	public abstract void setRadius(double r);

	/**
	 * @see SphericalElements#alpha
	 */
	public abstract void setRightAscension(double alpha);

	/**
	 * @see SphericalElements#v
	 */
	public abstract void setVelocity(double v);

	/**
	 * {@inheritDoc}
	 * <p>
	 * [ a, e, i, omega, raan, trueA ]
	 * </p>
	 */
	@Override
	public abstract RealVector toVector();

}