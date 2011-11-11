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

/**
 * Documentation: {@link ISphericalElements}
 * 
 * @author Simon Billemont
 * @see ISphericalElements
 */
public class SphericalElements extends StateVector implements ISphericalElements {

	/**
	 * Create a set of {@link SphericalElements} from another {@link StateVector}. Chooses itself what
	 * the best way of converting is.
	 * 
	 * <p>
	 * Guarantees that the return of a {@link SphericalElements} {@link StateVector}, but not necessarily
	 * a clone (can be the same {@link StateVector})
	 * </p>
	 * 
	 * @param state
	 *            {@link StateVector} to convert
	 * @param center
	 *            Body where the {@link KeplerElements} are formulated against
	 */
	public static SphericalElements as(IStateVector state, CelestialBody center) {
		Class<? extends IStateVector> clazz = state.getClass();
		if (SphericalElements.class.isAssignableFrom(clazz)) {
			SphericalElements s2 = (SphericalElements) state;
			if (s2.getCenterbody() == center)
				return s2;
			else {
				// TODO: more native implementation
			}
		} else if (IKeplerElements.class.isAssignableFrom(clazz)) {
			IKeplerElements k = KeplerElements.as(state, center);
			// TODO: implement Kepler conversion
			throw new UnsupportedOperationException("Not implemented yet");
		}
		ICartesianElements c = state.toCartesianElements();
		// TODO: implement Cartesian conversion
		throw new UnsupportedOperationException("Not implemented yet");
	}

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
	 * {@inheritDoc}
	 */
	@Override
	public ISphericalElements clone() {
		return new SphericalElements(r, alpha, delta, v, gamma, psi, centerbody);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(ISphericalElements state2) {
		return state2.getRadius() == r && state2.getRightAscension() == alpha &&
				state2.getDeclination() == delta && state2.getVelocity() == v &&
				state2.getFlightPathAngle() == gamma && state2.getFlightPathAzimuth() == psi;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CelestialBody getCenterbody() {
		return centerbody;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDeclination() {
		return delta;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getFlightPathAngle() {
		return gamma;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getFlightPathAzimuth() {
		return psi;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getRadius() {
		return r;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getRightAscension() {
		return alpha;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getVelocity() {
		return v;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCenterbody(CelestialBody centerbody) {
		this.centerbody = centerbody;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDeclination(double delta) {
		this.delta = delta;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setElements(double r, double alpha, double delta, double v, double gamma, double psi) {
		setRadius(r);
		setRightAscension(alpha);
		setDeclination(delta);
		setVelocity(v);
		setFlightPathAngle(gamma);
		setFlightPathAzimuth(psi);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFlightPathAngle(double gamma) {
		this.gamma = gamma;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFlightPathAzimuth(double psi) {
		this.psi = psi;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRadius(double r) {
		this.r = r;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRightAscension(double alpha) {
		this.alpha = alpha;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVelocity(double v) {
		this.v = v;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICartesianElements toCartesianElements() {
		// TODO: direct conversion
		IKeplerElements k = KeplerElements.as(this, centerbody);
		return k.toCartesianElements();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RealVector toVector() {
		return new ArrayRealVector(new double[] { r, alpha, delta, v, gamma, psi });
	}

}
