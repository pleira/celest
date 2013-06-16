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
package be.angelcorp.libs.celest.maneuvers;

import be.angelcorp.libs.celest.body.ICelestialBody;
import be.angelcorp.libs.celest.body.IPropellant;
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

	private ICelestialBody	body;

	public ImpulsiveShot(ICelestialBody body) {
		this.body = body;
	}

	public IPositionState kick(double dV) {
		IPositionState stateNew = kick(body.getState(), dV);
		return stateNew;
	}

	public IPositionState kick(double dV, IPropellant fuel) {
		IPositionState stateNew = kick(body.getState(), dV);
        fuel.consumeMass( dM( body, fuel, dV ) );
		return stateNew;
	}

	public IPositionState kick(Vector3D dV) {
		IPositionState stateNew = kick(body.getState(), dV);
		return stateNew;
	}

	public IPositionState kick(Vector3D dV, IPropellant fuel) {
		IPositionState stateNew = kick(body.getState(), dV);
		fuel.consumeMass( dM( body, fuel, dV.norm() ) );
		return stateNew;
	}

    /**
     * Compute the amount of propellant used in ideal conditions (no gravity losses, Tsiolkovsky)
     *
     * @param body Body that consumes DV.
     * @param dV   DV achieve using this propellant.
     */
    public double dM(ICelestialBody body, IPropellant propellant, double dV) {
        double m  = body.getTotalMass();
        double dM = m - Math.exp(-dV / (propellant.isp() * 9.81)) * m;
        return dM;
    }

}
