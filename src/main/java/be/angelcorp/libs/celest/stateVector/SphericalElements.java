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

public class SphericalElements extends StateVector {

	/**
	 * {@inheritDoc}
	 */
	public static SphericalElements fromVector(RealVector vector) {
		if (vector.getDimension() != 6)
			throw new MatrixIndexException("Vector must have 6 indices: [a, e, i, omega, raan, trueA]");
		double[] d = vector.getData();
		return new SphericalElements(d[0], d[1], d[2], d[3], d[4], d[5]);
	}

	/**
	 * Radius length
	 * <p>
	 * Distance from the center of the orbited object to the satellite
	 * </p>
	 * <p>
	 * <b>Unit: [m]</b>
	 * </p>
	 */
	protected double		r;

	/**
	 * Right ascension, &alpha;
	 * <p>
	 * 
	 * </p>
	 * <p>
	 * <b>Unit: [rad]</b>
	 * </p>
	 */
	protected double		alpha;

	/**
	 * Declination, &delta;
	 * <p>
	 * </p>
	 * <p>
	 * <b>Unit: [rad]</b>
	 * </p>
	 */
	protected double		delta;
	/**
	 * Velocity
	 * <p>
	 * </p>
	 * <p>
	 * <b>Unit: [m/s]</b>
	 * </p>
	 */
	protected double		v;
	/**
	 * Flight path angle &gamma;
	 * <p>
	 * In orbit plane angle between the radius vector and velocity vector
	 * </p>
	 * <p>
	 * <b>Unit: [rad]</b>
	 * </p>
	 */
	protected double		gamma;
	/**
	 * Flight path azimuth, &psi;
	 * <p>
	 * Out of plane angle of the velocity vector
	 * </p>
	 * <p>
	 * <b>Unit: [rad]</b>
	 * </p>
	 */
	protected double		psi;

	/**
	 * Center body, around the centerbody object, this satellite is orbiting
	 */
	private CelestialBody	centerbody;

	/**
	 * Create the {@link SphericalElements} from direct numerical values
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
	public SphericalElements(double r, double alpha, double delta, double v, double gamma,
			double psi) {
		this(r, alpha, delta, v, gamma, psi, null);
	}

	/**
	 * Create the {@link SphericalElements} from direct numerical values, with a center body to where the
	 * values hold
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
	 * @param centerbody
	 *            The body that is being orbited by these elements
	 */
	public SphericalElements(double r, double alpha, double delta, double v, double gamma,
			double psi, CelestialBody centerbody) {
		super();
		this.r = r;
		this.alpha = alpha;
		this.delta = delta;
		this.v = v;
		this.gamma = gamma;
		this.psi = psi;
		this.centerbody = centerbody;
	}

	/**
	 * Create a set of {@link SphericalElements} from another {@link StateVector}. Chooses itself what
	 * the best way of converting is.
	 * 
	 * @param state
	 *            {@link StateVector} to convert
	 * @param center
	 *            Body where the {@link KeplerElements} are formulated against
	 */
	public SphericalElements(StateVector state, CelestialBody center) {
		Class<? extends StateVector> clazz = state.getClass();
		if (KeplerElements.class.isAssignableFrom(clazz)) {
			KeplerElements k = (KeplerElements) state;
			setCenterbody(center);
			// TODO: implement Kepler conversion
			throw new UnsupportedOperationException("Not implemented yet");
		} else {
			CartesianElements c = state.toCartesianElements();
			setCenterbody(center);
			// TODO: implement Cartesian conversion
			throw new UnsupportedOperationException("Not implemented yet");
		}
	}

	/**
	 * Create an identical copy of this object, holds the same values, but in a different object.
	 */
	@Override
	public SphericalElements clone() {
		return new SphericalElements(r, alpha, delta, v, gamma, psi, centerbody);
	}

	/**
	 * Tests if two sets of {@link SphericalElements} hold the same variables (tests for equal
	 * r,&alpha;,&delta;,V,&gamma;,&psi;)
	 * 
	 * @param state2
	 *            {@link SphericalElements} to compare with
	 * @return True if the two sets contain the same orbital elements
	 */
	public boolean equals(SphericalElements state2) {
		return state2.r == r && state2.alpha == alpha && state2.delta == delta &&
				state2.v == v && state2.gamma == gamma && state2.psi == psi;
	}

	/**
	 * @see KeplerElements#centerbody
	 */
	public CelestialBody getCenterbody() {
		return centerbody;
	}

	/**
	 * @see SphericalElements#delta
	 */
	public double getDeclination() {
		return delta;
	}

	/**
	 * @see SphericalElements#gamma
	 */
	public double getFlightPathAngle() {
		return gamma;
	}

	/**
	 * @see SphericalElements#psi
	 */
	public double getFlightPathAzimuth() {
		return psi;
	}

	/**
	 * @see SphericalElements#r
	 */
	public double getRadius() {
		return r;
	}

	/**
	 * @see SphericalElements#alpha
	 */
	public double getRightAscension() {
		return alpha;
	}

	/**
	 * @see SphericalElements#c
	 */
	public double getVelocity() {
		return v;
	}

	/**
	 * @see SphericalElements#centerbody
	 */
	public void setCenterbody(CelestialBody centerbody) {
		this.centerbody = centerbody;
	}

	/**
	 * @see SphericalElements#delta
	 */
	public void setDeclination(double delta) {
		this.delta = delta;
	}

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
	public void setElements(double r, double alpha, double delta, double v, double gamma, double psi) {
		setRadius(r);
		setRightAscension(alpha);
		setDeclination(delta);
		setVelocity(v);
		setFlightPathAngle(gamma);
		setFlightPathAzimuth(psi);
	}

	/**
	 * @see SphericalElements#gamma
	 */
	public void setFlightPathAngle(double gamma) {
		this.gamma = gamma;
	}

	/**
	 * @see SphericalElements#psi
	 */
	public void setFlightPathAzimuth(double psi) {
		this.psi = psi;
	}

	/**
	 * @see SphericalElements#r
	 */
	public void setRadius(double r) {
		this.r = r;
	}

	/**
	 * @see SphericalElements#alpha
	 */
	public void setRightAscension(double alpha) {
		this.alpha = alpha;
	}

	/**
	 * @see SphericalElements#v
	 */
	public void setVelocity(double v) {
		this.v = v;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CartesianElements toCartesianElements() {
		// TODO: direct conversion
		KeplerElements k = new KeplerElements(this, centerbody);
		return k.toCartesianElements();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * [ a, e, i, omega, raan, trueA ]
	 * </p>
	 */
	@Override
	public RealVector toVector() {
		return new ArrayRealVector(new double[] { r, alpha, delta, v, gamma, psi });
	}

}
