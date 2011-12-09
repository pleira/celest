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
import org.apache.commons.math.util.MathUtils;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.kepler.KeplerEquations;
import be.angelcorp.libs.math.MathUtils2;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.math.linear.Vector3DMath;

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
			return (SphericalElements) state;
		} else if (ISphericalElements.class.isAssignableFrom(clazz)) {
			ISphericalElements s = (ISphericalElements) state;
			return new SphericalElements(s.getRadius(), s.getRightAscension(), s.getDeclination(),
					s.getVelocity(), s.getFlightPathAngle(), s.getFlightPathAzimuth());
		} else if (IKeplerElements.class.isAssignableFrom(clazz)) {
			IKeplerElements k = KeplerElements.as(state, center);
			// TODO: direct conversion
			ICartesianElements c = state.toCartesianElements();
			return new SphericalElements(c, center);
		}
		ICartesianElements c = state.toCartesianElements();
		return new SphericalElements(c, center);
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
	 * Convert {@link ICartesianElements} to {@link SphericalElements}.
	 * 
	 * @param elements
	 *            Cartesian elements to convert [m] and [m/s]
	 */
	public SphericalElements(ICartesianElements elements, CelestialBody centerBody) {
		// See http://www.cdeagle.com/omnum/pdf/csystems.pdf
		// Orbital Mechanics with Numerit: Astrodynamic Coordinates
		final Vector3D R = elements.getR();
		final Vector3D V = elements.getV();
		setRadius(R.getNorm());
		setVelocity(V.getNorm());
		setRightAscension(Math.atan2(R.getY(),
				R.getX()));
		setDeclination(Math.atan2(R.getZ(),
				Math.sqrt(Math.pow(R.getX(), 2) + Math.pow(R.getY(), 2))));
		setFlightPathAngle(Math.PI / 2 - Vector3DMath.angle(R, V)); // TODO: Quadrant corrections ?
		setFlightPathAzimuth(Math.atan2(r * (R.getX() * V.getY() - R.getY() * V.getX()),
				R.getY() * (R.getY() * V.getZ() - R.getZ() * V.getY())
						- R.getX() * (R.getZ() * V.getX() - R.getX() * V.getZ())));
		setCenterbody(centerBody);
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
		double angleTol = KeplerEquations.angleTolarance;
		return MathUtils.equals(r, state2.getRadius(), r * 1E-10)
				&& MathUtils.equals(v, state2.getVelocity(), v * 1E-10)
				&& MathUtils2.equalsAngle(alpha, state2.getRightAscension(), angleTol)
				&& MathUtils2.equalsAngle(delta, state2.getDeclination(), angleTol)
				&& MathUtils2.equalsAngle(gamma, state2.getFlightPathAngle(), angleTol)
				&& MathUtils2.equalsAngle(psi, state2.getFlightPathAzimuth(), angleTol);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(ISphericalElements state2, double eps) {
		return MathUtils.equals(r, state2.getRadius(), r * eps)
				&& MathUtils.equals(v, state2.getVelocity(), v * eps)
				&& MathUtils2.equalsAngle(alpha, state2.getRightAscension(), alpha * eps)
				&& MathUtils2.equalsAngle(delta, state2.getDeclination(), delta * eps)
				&& MathUtils2.equalsAngle(gamma, state2.getFlightPathAngle(), gamma * eps)
				&& MathUtils2.equalsAngle(psi, state2.getFlightPathAzimuth(), psi * eps);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(IStateVector state2) {
		if (ISphericalElements.class.isAssignableFrom(state2.getClass()))
			return equals((ISphericalElements) state2);
		else
			return equals(state2);
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
		// See http://www.cdeagle.com/omnum/pdf/csystems.pdf
		// Orbital Mechanics with Numerit: Astrodynamic Coordinates
		double cosD = Math.cos(getDeclination());
		double sinD = Math.sin(getDeclination());
		double cosAl = Math.cos(getRightAscension());
		double sinAl = Math.sin(getRightAscension());
		double cosA = Math.cos(getFlightPathAzimuth());
		double sinA = Math.sin(getFlightPathAzimuth());
		double cosG = Math.cos(getFlightPathAngle());
		double sinG = Math.sin(getFlightPathAngle());

		Vector3D R = new Vector3D(
				r * cosD * cosAl,
				r * cosD * sinAl,
				r * sinD);
		Vector3D V = new Vector3D(
				v * (cosAl * (-cosA * cosG * sinD + sinG * cosD) - sinA * cosG * sinAl),
				v * (sinAl * (-cosA * cosG * sinD + sinG * cosD) + sinA * cosG * cosAl),
				v * (cosA * cosG * cosD + sinG * cosD));

		return new CartesianElements(R, V);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RealVector toVector() {
		return new ArrayRealVector(new double[] { r, alpha, delta, v, gamma, psi });
	}

}
