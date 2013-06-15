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

package be.angelcorp.libs.celest.body;

public interface IPropellant {

    /**
     * Specific impulse of the propellant.
     * @return Propellant specific impulse [1/s]
     */
    public double isp();

    /**
     * Reduce the propellant mass of the propellant with the specified quantity.
     * @param dM Amount of consumed propellant [kg]
     */
    public void consumeMass(double dM);

    /**
     * Amount of propellant mass left.
     * @return Propellant mass [kg].
     */
    public double propellantMass();

    /**
     * Compute the maximum dV that can be given with this engine (for an impulse maneuver, Tsiolkovsky).
     *
     * @param body Body that contains the engine.
     * @return Maximum dV that can be given with the given amount of propellant.
     */
    public double getDvMax(ICelestialBody body);

    /**
     * Get the effective exhaust velocity.
     * <p>Veff = Isp * G0</p>
     *
     * <p>
     * <b>Unit: [m/s]</b>
     * </p>
     *
     * @return The effective exhaust velocity.
     */
    public double getVeff();

}
