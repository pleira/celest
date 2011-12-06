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
 * Documentation: {@link IKeplerDerivative}
 * 
 * @author Simon Billemont
 * @see IKeplerDerivative
 */
public class KeplerDerivative extends StateDerivativeVector implements IKeplerDerivative {

	/**
	 * {@inheritDoc}
	 */
	public static KeplerDerivative fromVector(RealVector vector) {
		if (vector.getDimension() != 6)
			throw new MatrixIndexException("Vector must have 6 indices: [da, de, di, domega, draan, dtrueA]");
		double[] d = vector.getData();
		return new KeplerDerivative(d[0], d[1], d[2], d[3], d[4], d[5]);
	}

	/**
	 * Semi-major axis variation (da/dt)
	 * <p>
	 * The change of the semi-major axis (see {@link KeplerElements#a}) per second
	 * </p>
	 * <p>
	 * <b>[m/s]</b>
	 * </p>
	 */
	protected double		da;

	/**
	 * Eccentricity variation (de/dt)
	 * <p>
	 * The change of the eccentricity (see {@link KeplerElements#e}) per second
	 * </p>
	 * <p>
	 * <b>[-/s]</b>
	 * </p>
	 */
	protected double		de;
	/**
	 * Inclination variation (di/dt)
	 * <p>
	 * The change of the inclination (see {@link KeplerElements#i}) per second
	 * </p>
	 * <p>
	 * <b>[rad/s]</b>
	 * </p>
	 */
	protected double		di;
	/**
	 * Argument of periapsis variation (d&omega;/dt)
	 * <p>
	 * The change of the argument of periapsis (see {@link KeplerElements#omega}) per second
	 * </p>
	 * <p>
	 * <b>[rad/s]</b>
	 * </p>
	 */
	protected double		domega;
	/**
	 * Variation of the right ascension of the ascending node (d(RAAN), d&Omega;/dt)
	 * <p>
	 * The change of the right ascension of the ascending node (see {@link KeplerElements#raan}) per
	 * second
	 * </p>
	 * <p>
	 * <b>[rad/s]</b>
	 * </p>
	 */
	protected double		draan;

	/**
	 * Mean anomaly variation (dM/dt)
	 * <p>
	 * The change of the mean anomaly per second
	 * </p>
	 * <p>
	 * <b>[rad]</b>
	 * </p>
	 */
	protected double		dM;

	/**
	 * Center body of the Kepler elements
	 */
	private CelestialBody	centerbody;

	/**
	 * Create the Kepler derivative from direct numerical values
	 * 
	 * @param da
	 *            Semi-major axis variation [m/s]
	 * @param de
	 *            Eccentricity variation [-/s]
	 * @param di
	 *            Inclination variation [rad/s]
	 * @param domega
	 *            Argument of pericenter variation [rad/s]
	 * @param draan
	 *            Right ascension of ascending node variation [rad/s]
	 * @param dM
	 *            Mean anomaly variation [rad/s]
	 */
	public KeplerDerivative(double da, double de, double di, double domega, double draan, double dM) {
		this(da, de, di, domega, draan, dM, null);
	}

	/**
	 * Create the Kepler derivatives from direct numerical values, with a center body to where the values
	 * hold
	 * 
	 * @param da
	 *            Semi-major axis variation [m/s]
	 * @param de
	 *            Eccentricity variation [-/s]
	 * @param di
	 *            Inclination variation [rad/s]
	 * @param domega
	 *            Argument of pericenter variation [rad/s]
	 * @param draan
	 *            Right ascension of ascending node variation [rad/s]
	 * @param dM
	 *            Mean anomaly variation [rad/s]
	 * @param centerbody
	 *            The body that is being orbited by these elements
	 */
	public KeplerDerivative(double da, double de, double di, double domega, double draan, double dM,
			CelestialBody centerbody) {
		super();
		this.da = da;
		this.de = de;
		this.di = di;
		this.domega = domega;
		this.draan = draan;
		this.dM = dM;
		this.centerbody = centerbody;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public KeplerDerivative clone() {
		return new KeplerDerivative(da, de, di, domega, draan, dM, centerbody);
	}

	@Override
	public boolean equals(IKeplerDerivative state2) {
		return super.equals(state2);
	}

	@Override
	public boolean equals(IKeplerDerivative state2, double eps) {
		return super.equals(state2, eps);
	}

	/**
	 * Documentation: {@link KeplerDerivative#domega}
	 * 
	 * @see KeplerDerivative#domega
	 */
	@Override
	public double getArgumentPeriapsisVariation() {
		return domega;
	}

	/**
	 * Documentation: {@link KeplerDerivative#centerbody}
	 * 
	 * @see KeplerDerivative#centerbody
	 */
	@Override
	public CelestialBody getCenterbody() {
		return centerbody;
	}

	/**
	 * Documentation: {@link KeplerDerivative#de}
	 * 
	 * @see KeplerDerivative#de
	 */
	@Override
	public double getEccentricityVariation() {
		return de;
	}

	/**
	 * Documentation: {@link KeplerDerivative#di}
	 * 
	 * @see KeplerDerivative#di
	 */
	@Override
	public double getInclinationVariation() {
		return di;
	}

	/**
	 * Documentation: {@link KeplerDerivative#dM}
	 * 
	 * @see KeplerDerivative#dM
	 */
	@Override
	public double getMeanAnomalyVariation() {
		return dM;
	}

	/**
	 * Documentation: {@link KeplerDerivative#draan}
	 * 
	 * @see KeplerDerivative#draan
	 */
	@Override
	public double getRaanVariation() {
		return draan;
	}

	/**
	 * Documentation: {@link KeplerDerivative#da}
	 * 
	 * @see KeplerDerivative#da
	 */
	@Override
	public double getSemiMajorAxisVariation() {
		return da;
	}

	/**
	 * Documentation: {@link KeplerDerivative#domega}
	 * 
	 * @see KeplerDerivative#domega
	 */
	@Override
	public void setArgumentPeriapsisVariation(double domega) {
		this.domega = domega;
	}

	/**
	 * Documentation: {@link KeplerDerivative#centerbody}
	 * 
	 * @see KeplerDerivative#centerbody
	 */
	@Override
	public void setCenterbody(CelestialBody centerbody) {
		this.centerbody = centerbody;
	}

	/**
	 * Documentation: {@link KeplerDerivative#de}
	 * 
	 * @see KeplerDerivative#de
	 */
	@Override
	public void setEccentricityVariation(double de) {
		this.de = de;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setElements(double da, double de, double di, double domega, double draan, double dM) {
		setSemiMajorAxisVariation(da);
		setEccentricityVariation(de);
		setInclinationVariation(di);
		setArgumentPeriapsisVariation(domega);
		setRaanVariation(draan);
		setMeanAnomalyVariation(dM);
	}

	/**
	 * Documentation: {@link KeplerDerivative#di}
	 * 
	 * @see KeplerDerivative#di
	 */
	@Override
	public void setInclinationVariation(double di) {
		this.di = di;
	}

	/**
	 * Documentation: {@link KeplerDerivative#dM}
	 * 
	 * @see KeplerDerivative#dM
	 */
	@Override
	public void setMeanAnomalyVariation(double dM) {
		this.dM = dM;
	}

	/**
	 * Documentation: {@link KeplerDerivative#draan}
	 * 
	 * @see KeplerDerivative#draan
	 */
	@Override
	public void setRaanVariation(double draan) {
		this.draan = draan;
	}

	/**
	 * Documentation: {@link KeplerDerivative#da}
	 * 
	 * @see KeplerDerivative#da
	 */
	@Override
	public void setSemiMajorAxisVariation(double da) {
		this.da = da;
	}

	@Override
	public CartesianDerivative toCartesianDerivative() {
		throw new UnsupportedOperationException("Cannot convert KeplerDerivative to CartesianDerivative!");
	}

	/**
	 * @see IKeplerElements#toVector()
	 */
	@Override
	public RealVector toVector() {
		return new ArrayRealVector(new double[] { da, de, di, domega, draan, dM });
	}

}
