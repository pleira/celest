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

import be.angelcorp.celest.body.CelestialBody;
import be.angelcorp.celest.math.geometry.Vec3;
import be.angelcorp.celest.physics.atmosphere.IAtmosphere;
import be.angelcorp.celest.state.Orbit;

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

abstract public class AtmosphericDrag {

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
     * Atmosphere creating the drag
     */
    protected IAtmosphere atmosphere;

    /**
     * @param cd         coefficient of drag
     * @param area       drag cross-sectional area
     * @param atmosphere The atmosphere creating the drag
     */
    public AtmosphericDrag(double cd, double area, IAtmosphere atmosphere) {
        this.cd = cd;
        this.area = area;
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
     * @param planet    State of the planet to which this atmosphere belongs.
     * @param body      The body traveling through the atmosphere.
     * @param bodyOrbit State of the body traveling through the atmosphere.
     */
    public Vec3 getForce(Orbit<?> planet, CelestialBody body, Orbit<?> bodyOrbit) {
        return toAcceleration(planet, body, bodyOrbit).$times(body.mass());
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
     * @param planet    State of the planet to which this atmosphere belongs.
     * @param body      The body traveling through the atmosphere.
     * @param bodyOrbit State of the body traveling through the atmosphere.
     */
    public Vec3 toAcceleration(Orbit<?> planet, CelestialBody body, Orbit<?> bodyOrbit) {
        Vec3 r = bodyOrbit.toPosVel().position().$minus(
                planet.toPosVel().position());
        Vec3 v = bodyOrbit.toPosVel().velocity().$minus(
                planet.toPosVel().velocity());

        // compute the atmospheric density
        double rho = atmosphere.computeDensity(r); // [kg/m^3]

        // compute the relative speed
        Vec3 vAtm = atmosphere.computeV(r); // [m/s]
        Vec3 vr = v.$minus(vAtm);
        double vrmag = vr.norm();

        // form -1/2 (Cd*A/m) rho
        double beta = cd * area / body.mass(); // [m^2/kg]
        double coeff = -0.5 * beta * rho;
        double coeff2 = coeff * vrmag;

        // compute the acceleration in inertial frame (m/s^2)
        Vec3 drag = vr.$times(coeff2);
        return drag;
    }

}
