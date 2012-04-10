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
package be.angelcorp.libs.celest.maneuvers;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.body.Propellant;
import be.angelcorp.libs.celest.state.positionState.ICartesianElements;
import be.angelcorp.libs.celest.state.positionState.IPositionState;
import be.angelcorp.libs.math.linear.Vector3D;

public class ImpulsiveShot {

	public static IPositionState kick(IPositionState startState, double dV) {
		ICartesianElements state = startState.toCartesianElements();
		return kick(state, state.getV().normalize().multiply(dV));
	}

	public static IPositionState kick(IPositionState startState, Vector3D dV) {
		ICartesianElements state = startState.toCartesianElements();
		state.setV(state.getV().add(dV));
		return state;
	}

	private CelestialBody	body;

	public ImpulsiveShot(CelestialBody body) {
		this.body = body;
	}

	public IPositionState kick(double dV) {
		IPositionState stateNew = kick(body.getState(), dV);
		body.setState(stateNew);
		return stateNew;
	}

	public IPositionState kick(double dV, Propellant fuel) {
		IPositionState stateNew = kick(body.getState(), dV);
		fuel.consumeDV(body, dV);
		body.setState(stateNew);
		return stateNew;
	}

	public IPositionState kick(Vector3D dV) {
		IPositionState stateNew = kick(body.getState(), dV);
		body.setState(stateNew);
		return stateNew;
	}

	public IPositionState kick(Vector3D dV, Propellant fuel) {
		IPositionState stateNew = kick(body.getState(), dV);
		fuel.consumeDV(body, dV.getNorm());
		body.setState(stateNew);
		return stateNew;
	}

}
