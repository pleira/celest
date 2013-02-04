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

import be.angelcorp.libs.celest.ephemeris.IEphemeris;
import be.angelcorp.libs.celest.potential.IGravitationalPotential;
import be.angelcorp.libs.celest.state.positionState.IPositionState;

/**
 * Representation of a celestial body (eg planet/sun/satellite).
 *
 * @author simon
 */
public interface ICelestialBody {

    /** Get the local gravitational field produced by the body */
    IGravitationalPotential getGravitationalPotential();

    /**
     * Get the gravitational parameter of the celestial body (&mu; = G * m). This is the result of both
     * the dry and wet mass of the body.
     * <p>
     * <b>Unit: [m<sup>3</sup> / s<sup>2</sup>]</b>
     * </p>
     * @return Standard gravitational parameter of the body.
     */
    double getMu();

    /**
     * Get the celestial body state vector (position/velocity)
     * @return Body state vector
     */
    IPositionState getState();

    /**
     * Get the total mass of the celestial body.
     * <p>
     * <b>Unit: [kg]</b>
     * </p>
     * @return Mass of the body [kg].
     */
    double getTotalMass();

}
