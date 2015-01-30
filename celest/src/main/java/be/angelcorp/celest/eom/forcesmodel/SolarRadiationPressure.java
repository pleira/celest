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
import be.angelcorp.celest.constants.Constants;
import be.angelcorp.celest.math.geometry.Vec3;
import be.angelcorp.celest.physics.EM.IEMspectrum;
import be.angelcorp.celest.state.Orbit;

/**
 * Force created by a stars radiation pressure on a satellite. Internally uses cartesian form.
 *
 * @author simon
 */
public class SolarRadiationPressure {

    /**
     * Cross sectional (reflective) area
     * <p>
     * <b>Unit: [m<sup>2</sup>]</b>
     * </p>
     */
    private double area;
    /**
     * Satellite coefficient of reflectivity
     * <p>
     * <b>Unit: [-]</b>
     * </p>
     */
    private double CR;

    /**
     * Flux that the star emits
     */
    private IEMspectrum spectrum;

    /**
     * Solar radiation pressure force
     *
     * @param area      Effective area of the satellite {@link SolarRadiationPressure#area}
     * @param CR        Reflectivity coefficient {@link SolarRadiationPressure#CR}
     * @param emmition  The emmision sprectum of the star {@link SolarRadiationPressure#spectrum}
     */
    public SolarRadiationPressure(double area, double CR, IEMspectrum emmition) {
        this.area = area;
        this.CR = CR;
        this.spectrum = emmition;
    }

    /**
     * @see SolarRadiationPressure#area
     */
    public double getArea() {
        return area;
    }

    /**
     * @see SolarRadiationPressure#CR
     */
    public double getCR() {
        return CR;
    }

    /**
     * @param starOrbit      The star that creates emits the flux.
     * @param satellite      Satellite the force acts upon.
     * @param satelliteOrbit Orbit of the satellite on which the force acts?
     */
    public Vec3 getForce(Orbit<?> starOrbit, CelestialBody satellite, Orbit<?> satelliteOrbit) {
        return toAcceleration(starOrbit, satellite, satelliteOrbit).$times(satellite.mass());
    }

    /**
     * @see SolarRadiationPressure#spectrum
     */
    public IEMspectrum getSpectrum() {
        return spectrum;
    }

    /**
     * @see SolarRadiationPressure#area
     */
    public void setArea(double area) {
        this.area = area;
    }

    /**
     * @see SolarRadiationPressure#CR
     */
    public void setCR(double cR) {
        CR = cR;
    }

    /**
     * @see SolarRadiationPressure#spectrum
     */
    public void setSpectrum(IEMspectrum spectrum) {
        this.spectrum = spectrum;
    }

    /**
     * @param starOrbit      The star that creates emits the flux.
     * @param satellite      Satellite the force acts upon.
     * @param satelliteOrbit Orbit of the satellite on which the force acts?
     */
    public Vec3 toAcceleration(Orbit<?> starOrbit, CelestialBody satellite, Orbit<?> satelliteOrbit) {
        // Relative position vector of spacecraft w.r.t. Sun (from the sun to s/c)
        Vec3 d = satelliteOrbit.toPosVel().position().$minus(starOrbit.toPosVel().position());

        double dnorm = d.norm();
        double dcube = dnorm * dnorm * dnorm;

        double Ls = getSpectrum().totalFlux() / d.normSq(); // [W]

        double factor = CR * (area / satellite.mass()) * Ls / (4 * Math.PI * Constants.SPEED_LIGHT() * dcube);

        return d.$times(factor);
    }

}
