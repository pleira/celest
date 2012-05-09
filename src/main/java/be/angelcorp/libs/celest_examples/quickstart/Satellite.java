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
package be.angelcorp.libs.celest_examples.quickstart;

import be.angelcorp.libs.celest.body.BiPropellant;
import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.body.Propellant;
import be.angelcorp.libs.celest.state.positionState.IPositionState;

public class Satellite extends CelestialBody {

	/* Different propellants */
	private Propellant	hydrazine			= new Propellant(225, 1550);
	private Propellant	nitrogenTetroxide	= new Propellant(0, 1200);
	private Propellant	xenon				= new Propellant(2000, 400);

	/**
	 * Make a new satellite with a given start state
	 */
	public Satellite(IPositionState s) {
		super(s, 0); // Set the state of the satellite
		/* Set the dry mass of the satellite */
		setDryMass(6170
				- hydrazine.getPropellantMass()
				- nitrogenTetroxide.getPropellantMass()
				- xenon.getPropellantMass());
	}

	public Propellant getHydrazine() {
		return hydrazine;
	}

	public Propellant getHydrazineLAE() {
		double oxToFuel = 1.3;
		return new BiPropellant(340, hydrazine, 1 / (1 + oxToFuel), nitrogenTetroxide);
	}

	public Propellant getNitrogenTetroxide() {
		return nitrogenTetroxide;
	}

	@Override
	public double getWetMass() {
		return hydrazine.getPropellantMass() + nitrogenTetroxide.getPropellantMass() + xenon.getPropellantMass();
	}

	public Propellant getXenonHTC() {
		return xenon;
	}

}
