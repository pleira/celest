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
package be.angelcorp.celest.state.positionState;

import be.angelcorp.celest.body.CelestialBody;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * Documentation: {@link IKeplerDerivative}
 *
 * @author Simon Billemont
 * @see IKeplerDerivative
 */
public class NonSingularDerivative extends StateDerivativeVector {

    /**
     * {@inheritDoc}
     */
    public static NonSingularDerivative fromVector(RealVector vector) {
        if (vector.getDimension() != 6)
            throw new ArithmeticException("Vector must have 6 indices: [da, de, di, domega_true, draan, dlambda_M]");
        return new NonSingularDerivative(
                vector.getEntry(0),
                vector.getEntry(1),
                vector.getEntry(2),
                vector.getEntry(3),
                vector.getEntry(4),
                vector.getEntry(5));
    }

    /**
     * Semi-major axis variation (da/dt)
     * <p>
     * The change of the semi-major axis (see {@link NonSignuarElements#a}) per second
     * </p>
     * <p>
     * <b>Unit: [m/s]</b>
     * </p>
     */
    protected double da;

    /**
     * Eccentricity variation (de/dt)
     * <p>
     * The change of the eccentricity (see {@link NonSignuarElements#e}) per second
     * </p>
     * <p>
     * <b>Unit: [-/s]</b>
     * </p>
     */
    protected double de;
    /**
     * Inclination variation (di/dt)
     * <p>
     * The change of the inclination (see {@link NonSignuarElements#i}) per second
     * </p>
     * <p>
     * <b>Unit: [rad/s]</b>
     * </p>
     */
    protected double di;
    /**
     * Longitude of perihelion variation (d&omega;_true/dt)
     * <p>
     * The change of the longitude of perihelion (see {@link NonSignuarElements#omega_true}) per second
     * </p>
     * <p>
     * <b>Unit: [rad/s]</b>
     * </p>
     */
    protected double domega_true;
    /**
     * Variation of the right ascension of the ascending node (d(RAAN), d&Omega;/dt)
     * <p>
     * The change of the right ascension of the ascending node (see {@link NonSignuarElements#raan}) per
     * second
     * </p>
     * <p>
     * <b>Unit: [rad/s]</b>
     * </p>
     */
    protected double draan;

    /**
     * Mean longitude variation (d&lambda;_M/dt)
     * <p>
     * The change of the mean longitude (see {@link NonSignuarElements#lambda_M}) per second
     * </p>
     * <p>
     * <b>Unit: [rad]</b>
     * </p>
     */
    protected double dlambda_M;

    /**
     * Center body of the {@link NonSignuarElements}
     */
    private CelestialBody centerbody;

    /**
     * Create the Kepler derivative from direct numerical values
     *
     * @param da          Semi-major axis variation [m/s]
     * @param de          Eccentricity variation [-/s]
     * @param di          Inclination variation [rad/s]
     * @param domega_true Longitude of perihelion variation [rad/s]
     * @param draan       Right ascension of ascending node variation [rad/s]
     * @param dlambda_M   Mean longitude variation [rad/s]
     */
    public NonSingularDerivative(double da, double de, double di, double domega_true, double draan, double dlambda_M) {
        this(da, de, di, domega_true, draan, dlambda_M, null);
    }

    /**
     * Create the Kepler derivatives from direct numerical values, with a center body to where the values
     * hold
     *
     * @param da          Semi-major axis variation [m/s]
     * @param de          Eccentricity variation [-/s]
     * @param di          Inclination variation [rad/s]
     * @param domega_true Longitude of perihelion variation [rad/s]
     * @param draan       Right ascension of ascending node variation [rad/s]
     * @param dlambda_M   Mean longitude variation [rad/s]
     * @param centerbody  The body that is being orbited by these elements
     */
    public NonSingularDerivative(double da, double de, double di, double domega_true, double draan, double dlambda_M,
                                 CelestialBody centerbody) {
        super();
        this.da = da;
        this.de = de;
        this.di = di;
        this.domega_true = domega_true;
        this.draan = draan;
        this.dlambda_M = dlambda_M;
        this.centerbody = centerbody;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NonSingularDerivative clone() {
        return new NonSingularDerivative(da, de, di, domega_true, draan, dlambda_M, centerbody);
    }

    /**
     * Documentation: {@link NonSingularDerivative#centerbody}
     *
     * @see NonSingularDerivative#centerbody
     */
    public CelestialBody getCenterbody() {
        return centerbody;
    }

    /**
     * Documentation: {@link NonSingularDerivative#de}
     *
     * @see NonSingularDerivative#de
     */
    public double getEccentricityVariation() {
        return de;
    }

    /**
     * Documentation: {@link NonSingularDerivative#di}
     *
     * @see NonSingularDerivative#di
     */
    public double getInclinationVariation() {
        return di;
    }

    /**
     * Documentation: {@link NonSingularDerivative#domega}
     *
     * @see NonSingularDerivative#domega
     */
    public double getLongitudePerihelionVariation() {
        return domega_true;
    }

    /**
     * Documentation: {@link NonSingularDerivative#dM}
     *
     * @see NonSingularDerivative#dM
     */
    public double getMeanLongitudeVariation() {
        return dlambda_M;
    }

    /**
     * Documentation: {@link NonSingularDerivative#draan}
     *
     * @see NonSingularDerivative#draan
     */
    public double getRaanVariation() {
        return draan;
    }

    /**
     * Documentation: {@link NonSingularDerivative#da}
     *
     * @see NonSingularDerivative#da
     */
    public double getSemiMajorAxisVariation() {
        return da;
    }

    /**
     * Documentation: {@link NonSingularDerivative#centerbody}
     *
     * @see NonSingularDerivative#centerbody
     */
    public void setCenterbody(CelestialBody centerbody) {
        this.centerbody = centerbody;
    }

    /**
     * Documentation: {@link NonSingularDerivative#de}
     *
     * @see NonSingularDerivative#de
     */
    public void setEccentricityVariation(double de) {
        this.de = de;
    }

    /**
     * {@inheritDoc}
     */
    public void setElements(double da, double de, double di, double domega_true, double draan, double dlambda_true) {
        setSemiMajorAxisVariation(da);
        setEccentricityVariation(de);
        setInclinationVariation(di);
        setLongitudePerihelionVariation(domega_true);
        setRaanVariation(draan);
        setMeanLongitudeVariation(dlambda_true);
    }

    /**
     * Documentation: {@link NonSingularDerivative#di}
     *
     * @see NonSingularDerivative#di
     */
    public void setInclinationVariation(double di) {
        this.di = di;
    }

    /**
     * Documentation: {@link NonSingularDerivative#domega}
     *
     * @see NonSingularDerivative#domega
     */
    public void setLongitudePerihelionVariation(double domega_true) {
        this.domega_true = domega_true;
    }

    /**
     * Documentation: {@link NonSingularDerivative#dM}
     *
     * @see NonSingularDerivative#dM
     */
    public void setMeanLongitudeVariation(double dlambda_M) {
        this.dlambda_M = dlambda_M;
    }

    /**
     * Documentation: {@link NonSingularDerivative#draan}
     *
     * @see NonSingularDerivative#draan
     */
    public void setRaanVariation(double draan) {
        this.draan = draan;
    }

    /**
     * Documentation: {@link NonSingularDerivative#da}
     *
     * @see NonSingularDerivative#da
     */
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
        return new ArrayRealVector(new double[]{da, de, di, domega_true, draan, dlambda_M});
    }

}
