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
package be.angelcorp.celest.eom.forcesmodel;

import be.angelcorp.celest.body.ICelestialBody;
import be.angelcorp.celest.physics.atmosphere.IAtmosphere;
import be.angelcorp.celest.physics.quantities.ObjectForce;
import be.angelcorp.libs.math.linear.Vector3D;

/**
 * The AtmosphericDrag class computes the acceleration due to drag on a satellite using atmospheric model
 * <p/>
 * <p>
 * <b>Note: If you use tabluated values for &rho; of the atmosphere, then you also need to use the
 * associate Cd and S of those values, not those computed for the satellite itself !!!</b> <br />
 * If no value of Cd was given, 2.2 probably the value they used. This is because most of these tables
 * where created from satellite measurements of actual satellites where they fixed Cd,S,M so &rho; was
 * the only unknow. This means that &rho; also contains other information but the density, that are
 * related to Cd and S.
 * </p>
 *
 * @author simon
 */

abstract public class AtmosphericDrag extends ObjectForce {

    /**
     * Drag coefficient
     * <p>
     * Usually ranges from 2.2 to 2.8, NOT THE SAME CD AS FOR AIRCRAFT. Cd contains corrections for the
     * physical models, &rho; and S.
     * </p>
     * <p>
     * <b>Unit: [-]</b>
     * </p>
     */
    protected double cd;
    /**
     * Cross sectional area
     * <p>
     * <b>Unit: [m<sup>2</sup>]</b>
     * </p>
     */
    protected double area;
    /**
     * Planet with the atmosphere around it
     */
    protected ICelestialBody planet;
    /**
     * Atmosphere creating the drag
     */
    protected IAtmosphere atmosphere;

    /**
     * @param satellite  The satellite experiencing the drag
     * @param cd         coefficient of drag
     * @param area       drag cross-sectional area
     * @param planet     The planet to which the atmosphere belongs
     * @param atmosphere The atmosphere creating the drag
     */
    public AtmosphericDrag(ICelestialBody satellite, double cd, double area,
                           ICelestialBody planet, IAtmosphere atmosphere) {
        super(satellite);
        this.cd = cd;
        this.area = area;
        this.planet = planet;
        this.atmosphere = atmosphere;
    }

    /**
     * @see AtmosphericDrag#area
     */
    public double getArea() {
        return area;
    }

    /**
     * @see AtmosphericDrag#atmosphere
     */
    public IAtmosphere getAtmosphere() {
        return atmosphere;
    }

    /**
     * @see AtmosphericDrag#cd
     */
    public double getCd() {
        return cd;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector3D getForce() {
        return toAcceleration().multiply(getObject().getTotalMass());
    }

    /**
     * @see AtmosphericDrag#planet
     */
    public ICelestialBody getPlanet() {
        return planet;
    }

    /**
     * @see AtmosphericDrag#area
     */
    public void setArea(double area) {
        this.area = area;
    }

    /**
     * @see AtmosphericDrag#atmosphere
     */
    public void setAtmosphere(IAtmosphere atmosphere) {
        this.atmosphere = atmosphere;
    }

    /**
     * @see AtmosphericDrag#cd
     */
    public void setCd(double cd) {
        this.cd = cd;
    }

    /**
     * @see AtmosphericDrag#planet
     */
    public void setPlanet(ICelestialBody planet) {
        this.planet = planet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector3D toAcceleration() {
        Vector3D r = getObject().getState().toPosVel().position().$minus(
                getPlanet().getState().toPosVel().position());
        Vector3D v = getObject().getState().toPosVel().velocity().$minus(
                getPlanet().getState().toPosVel().velocity());

        // compute the atmospheric density
        double rho = atmosphere.computeDensity(r); // [kg/m^3]

        // compute the relative speed
        Vector3D vAtm = atmosphere.computeV(r); // [m/s]
        Vector3D vr = v.$minus(vAtm);
        double vrmag = vr.norm();

        // form -1/2 (Cd*A/m) rho
        double beta = cd * area / getObject().getTotalMass(); // [m^2/kg]
        double coeff = -0.5 * beta * rho;
        double coeff2 = coeff * vrmag;

        // compute the acceleration in inertial frame (m/s^2)
        Vector3D drag = vr.multiply(coeff2);
        return drag;
    }

}
