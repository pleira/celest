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
package be.angelcorp.libs.celest.body;

import be.angelcorp.libs.celest.constants.Constants;
import be.angelcorp.libs.celest.constants.SolarConstants;
import be.angelcorp.libs.celest.potential.IGravitationalPotential;
import be.angelcorp.libs.celest.potential.PointMassPotential;
import be.angelcorp.libs.celest.stateVector.CartesianElements;
import be.angelcorp.libs.celest.stateVector.IStateVector;

/**
 * Representation of a celestial body (eg planet/sun/satellite). It is a wrapper for a state vector and
 * the mass of the body
 * 
 * @author simon
 * 
 */
public class CelestialBody {

	/**
	 * Body state vector
	 */
	private IStateVector				state;
	/**
	 * Body mass
	 */
	private double					dryMass;
	/**
	 * Gravitational potential of this object
	 */
	private IGravitationalPotential	gravitationalPotential	= new PointMassPotential(this);

	/**
	 * Generic body with coordinates with R: <0, 0, 0> and V: <0, 0, 0> and with mass of our sun (one
	 * solar mass)
	 */
	public CelestialBody() {
		this(new CartesianElements(), SolarConstants.mass);
	}

	/**
	 * Create a generic body with given statevector (location/speed) and mass
	 * 
	 * @param state
	 *            State of the body
	 * @param mass
	 *            Mass of the body [kg]
	 */
	public CelestialBody(IStateVector state, double mass) {
		setState(state);
		setMass(mass);
	}

	/**
	 * Get the dry mass of the celestial body (mass without the propellant mass)
	 * <p>
	 * <b>Unit: [kg]</b>
	 * </p>
	 * 
	 * @return Mass of the body
	 */
	public double getDryMass() {
		return dryMass;
	}

	/**
	 * Get the local gravitational field produced by the body
	 * 
	 * @return
	 */
	public IGravitationalPotential getGravitationalPotential() {
		return gravitationalPotential;
	}

	/**
	 * Get the total mass of the celestial body
	 * <p>
	 * <b>Unit: [kg]</b>
	 * </p>
	 * 
	 * @return Mass of the body
	 */
	public double getMass() {
		return getDryMass() + getWetMass();
	}

	/**
	 * Get the gravitational parameter of the celestial body (&mu; = G * m)
	 * <p>
	 * <b>Unit: [m<sup>3</sup> / s<sup>2</sup>]</b>
	 * </p>
	 * 
	 * @return Mu of the body
	 */
	public double getMu() {
		return Constants.mass2mu(getMass());
	}

	/**
	 * Get the celestial body state vector (speed/velocity)
	 * 
	 * @return Body state vector
	 */
	public IStateVector getState() {
		return state;
	}

	/**
	 * Get the wet mass of the body (Propellant mass)
	 * <p>
	 * <b>Unit: [kg]</b>
	 * </p>
	 * 
	 * @return Mass of the body
	 */
	public double getWetMass() {
		return 0;
	}

	/**
	 * Set the mass of the celestial body
	 * 
	 * @param mass
	 *            new mass of the body [kg]
	 */
	public void setMass(double mass) {
		this.dryMass = mass;
	}

	/**
	 * Set the state of the celestial body (speed/location)
	 * 
	 * @param state
	 *            new body state
	 */
	public void setState(IStateVector state) {
		this.state = state;
	}

}
