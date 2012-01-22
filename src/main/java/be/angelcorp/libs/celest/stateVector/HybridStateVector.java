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
package be.angelcorp.libs.celest.stateVector;

import org.apache.commons.math.linear.RealVector;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.kepler.KeplerEquations;
import be.angelcorp.libs.celest.kepler.KeplerOrbitTypes;
import be.angelcorp.libs.math.linear.Vector3D;

public class HybridStateVector extends StateVector implements ICartesianElements, IKeplerElements,
		ISphericalElements {

	private enum MasterType {
		CARTESIAN, KEPLER, SPHERICAL;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * WARN, you must reinitialize the central body
	 * </p>
	 */
	public static HybridStateVector fromVector(RealVector vector) {
		ICartesianElements cart = CartesianElements.fromVector(vector);
		return new HybridStateVector(cart, null);
	}

	private ICartesianElements	cartesian;
	private IKeplerElements		keplerian;
	private ISphericalElements	spherical;
	private MasterType			master;

	private CelestialBody		center;

	private HybridStateVector() {
	}

	public HybridStateVector(IStateVector vector, CelestialBody center) {
		this.center = center;
		Class<? extends IStateVector> clazz = vector.getClass();
		if (ICartesianElements.class.isAssignableFrom(clazz)) {
			reset(MasterType.CARTESIAN, vector);
		} else if (IKeplerElements.class.isAssignableFrom(clazz)) {
			reset(MasterType.KEPLER, vector);
		} else if (ISphericalElements.class.isAssignableFrom(clazz)) {
			reset(MasterType.SPHERICAL, vector);
		} else {
			reset(MasterType.CARTESIAN, vector.toCartesianElements());
		}
	}

	@Override
	public HybridStateVector clone() {
		HybridStateVector h = new HybridStateVector();
		h.master = master;
		h.cartesian = cartesian.clone();
		h.keplerian = keplerian.clone();
		h.spherical = spherical.clone();
		h.center = center;
		return h;
	}

	@Override
	public boolean equals(ICartesianElements state2) {
		return getCartesian().equals(state2);
	}

	@Override
	public boolean equals(ICartesianElements state2, double eps) {
		return getCartesian().equals(state2, eps);
	}

	@Override
	public boolean equals(IKeplerElements state2) {
		return getKeplerian().equals(state2);
	}

	@Override
	public boolean equals(IKeplerElements state2, double eps) {
		return getKeplerian().equals(state2, eps);
	}

	@Override
	public boolean equals(ISphericalElements state2) {
		return getSpherical().equals(state2);
	}

	@Override
	public boolean equals(ISphericalElements state2, double eps) {
		return getSpherical().equals(state2, eps);
	}

	@Override
	public boolean equals(IStateVector state2) {
		Class<? extends IStateVector> clazz = state2.getClass();
		if (ICartesianElements.class.isAssignableFrom(clazz)) {
			return equals((ICartesianElements) state2);
		} else if (IKeplerElements.class.isAssignableFrom(clazz)) {
			return equals((IKeplerElements) state2);
		} else if (ISphericalElements.class.isAssignableFrom(clazz)) {
			return equals((ISphericalElements) state2);
		} else {
			return equals(state2.toCartesianElements());
		}
	}

	@Override
	public double getArgumentPeriapsis() {
		return getKeplerian().getArgumentPeriapsis();
	}

	public ICartesianElements getCartesian() {
		if (cartesian == null)
			makeCartesian();
		return cartesian;
	}

	@Override
	public CelestialBody getCenterbody() {
		return center;
	}

	@Override
	public double getDeclination() {
		return getSpherical().getDeclination();
	}

	@Override
	public double getEccentricity() {
		return getKeplerian().getEccentricity();
	}

	@Override
	public double getFlightPathAngle() {
		return getSpherical().getFlightPathAngle();
	}

	@Override
	public double getFlightPathAzimuth() {
		return getSpherical().getFlightPathAzimuth();
	}

	@Override
	public double getInclination() {
		return getKeplerian().getInclination();
	}

	public IKeplerElements getKeplerian() {
		if (keplerian == null)
			makeKeplerian();
		return keplerian;
	}

	@Override
	public KeplerEquations getOrbitEqn() {
		return getKeplerian().getOrbitEqn();
	}

	@Override
	public KeplerOrbitTypes getOrbitType() {
		return getKeplerian().getOrbitType();
	}

	@Override
	public Vector3D getR() {
		return getCartesian().getR();
	}

	@Override
	public double getRaan() {
		return getKeplerian().getRaan();
	}

	@Override
	public double getRadius() {
		return getSpherical().getRadius();
	}

	@Override
	public double getRightAscension() {
		return getSpherical().getRightAscension();
	}

	@Override
	public double getSemiMajorAxis() {
		return getKeplerian().getSemiMajorAxis();
	}

	public ISphericalElements getSpherical() {
		if (spherical == null)
			makeSpherical();
		return spherical;
	}

	@Override
	public double getTrueAnomaly() {
		return getKeplerian().getTrueAnomaly();
	}

	@Override
	public Vector3D getV() {
		return getCartesian().getV();
	}

	@Override
	public double getVelocity() {
		return getSpherical().getVelocity();
	}

	private void makeCartesian() {
		cartesian = CartesianElements.as(master == MasterType.KEPLER ? keplerian : spherical);
	}

	private void makeKeplerian() {
		keplerian = KeplerElements.as(master == MasterType.CARTESIAN ? cartesian : spherical, center);
	}

	private void makeSpherical() {
		spherical = SphericalElements.as(master == MasterType.KEPLER ? keplerian : cartesian, center);
	}

	private void reset(MasterType type, IStateVector v) {
		cartesian = null;
		keplerian = null;
		spherical = null;
		switch (type) {
			case CARTESIAN:
				cartesian = (ICartesianElements) v;
				break;
			case KEPLER:
				keplerian = (IKeplerElements) v;
				break;
			case SPHERICAL:
				spherical = (ISphericalElements) v;
				break;
		}
	}

	@Override
	public void setArgumentPeriapsis(double omega) {
		getKeplerian().setArgumentPeriapsis(omega);
		reset(MasterType.KEPLER, keplerian);
	}

	@Override
	public void setCenterbody(CelestialBody centerbody) {
		this.center = centerbody;
		if (keplerian != null)
			keplerian.setCenterbody(centerbody);
		if (spherical != null)
			spherical.setCenterbody(centerbody);
	}

	@Override
	public void setDeclination(double delta) {
		getSpherical().setDeclination(delta);
		reset(MasterType.SPHERICAL, spherical);
	}

	@Override
	public void setEccentricity(double e) {
		getKeplerian().setEccentricity(e);
		reset(MasterType.KEPLER, keplerian);
	}

	@Override
	public void setElements(double a, double e, double i, double omega, double raan, double trueA) {
		getKeplerian().setElements(a, e, i, omega, raan, trueA);
		reset(MasterType.KEPLER, keplerian);
	}

	@Override
	public void setFlightPathAngle(double gamma) {
		getSpherical().setFlightPathAngle(gamma);
		reset(MasterType.SPHERICAL, spherical);
	}

	@Override
	public void setFlightPathAzimuth(double psi) {
		getSpherical().setFlightPathAzimuth(psi);
		reset(MasterType.SPHERICAL, spherical);
	}

	@Override
	public void setInclination(double i) {
		getKeplerian().setInclination(i);
		reset(MasterType.KEPLER, keplerian);
	}

	@Override
	public void setR(Vector3D r) {
		getCartesian().setR(r);
		reset(MasterType.CARTESIAN, cartesian);
	}

	@Override
	public void setRaan(double raan) {
		getKeplerian().setRaan(raan);
		reset(MasterType.KEPLER, keplerian);
	}

	@Override
	public void setRadius(double r) {
		getSpherical().setRadius(r);
		reset(MasterType.SPHERICAL, spherical);
	}

	@Override
	public void setRightAscension(double alpha) {
		getSpherical().setRightAscension(alpha);
		reset(MasterType.SPHERICAL, spherical);
	}

	@Override
	public void setSemiMajorAxis(double a) {
		getKeplerian().setSemiMajorAxis(a);
		reset(MasterType.KEPLER, keplerian);
	}

	@Override
	public void setTrueAnomaly(double trueA) {
		getKeplerian().setTrueAnomaly(trueA);
		reset(MasterType.KEPLER, keplerian);
	}

	@Override
	public void setV(Vector3D v) {
		getCartesian().setV(v);
		reset(MasterType.CARTESIAN, cartesian);
	}

	@Override
	public void setVelocity(double v) {
		getSpherical().setVelocity(v);
		reset(MasterType.SPHERICAL, spherical);
	}

	@Override
	public ICartesianElements toCartesianElements() {
		return getCartesian();
	}

	@Override
	public RealVector toVector() {
		return getCartesian().toVector();
	}

}
