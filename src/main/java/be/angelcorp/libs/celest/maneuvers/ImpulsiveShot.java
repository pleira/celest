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
package be.angelcorp.libs.celest.maneuvers;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.body.Propellant;
import be.angelcorp.libs.celest.stateVector.CartesianElements;
import be.angelcorp.libs.celest.stateVector.StateVector;
import be.angelcorp.libs.math.linear.Vector3D;

public class ImpulsiveShot {

	public static StateVector kick(StateVector startState, double dV) {
		CartesianElements state = startState.toCartesianElements();
		return kick(state, state.V.normalize().multiply(dV));
	}

	public static StateVector kick(StateVector startState, Vector3D dV) {
		CartesianElements state = startState.toCartesianElements();
		state.V = state.V.add(dV);
		return state;
	}

	private CelestialBody	body;

	public ImpulsiveShot(CelestialBody body) {
		this.body = body;
	}

	public StateVector kick(double dV) {
		StateVector stateNew = kick(body.getState(), dV);
		body.setState(stateNew);
		return stateNew;
	}

	public StateVector kick(double dV, Propellant fuel) {
		StateVector stateNew = kick(body.getState(), dV);
		fuel.consumeDV(body, dV);
		body.setState(stateNew);
		return stateNew;
	}

	public StateVector kick(Vector3D dV) {
		StateVector stateNew = kick(body.getState(), dV);
		body.setState(stateNew);
		return stateNew;
	}

	public StateVector kick(Vector3D dV, Propellant fuel) {
		StateVector stateNew = kick(body.getState(), dV);
		fuel.consumeDV(body, dV.getNorm());
		body.setState(stateNew);
		return stateNew;
	}

}
