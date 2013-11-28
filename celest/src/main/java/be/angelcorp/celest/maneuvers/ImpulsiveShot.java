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
package be.angelcorp.celest.maneuvers;

import be.angelcorp.celest.body.ICelestialBody;
import be.angelcorp.celest.body.Propellant;
import be.angelcorp.celest.state.PosVel;
import be.angelcorp.libs.math.linear.Vector3D;

public class ImpulsiveShot {

    public static PosVel kick(PosVel state, double dV) {
        return kick(state, state.velocity().normalize().multiply(dV));
    }

    public static PosVel kick(PosVel state, Vector3D dV) {
        return new PosVel(state.position(), state.velocity().$plus(dV), state.frame());
    }

    private ICelestialBody body;

    public ImpulsiveShot(ICelestialBody body) {
        this.body = body;
    }

    public PosVel kick(double dV) {
        PosVel stateNew = kick(body.getState().toPosVel(), dV);
        return stateNew;
    }

    public PosVel kick(double dV, Propellant fuel) {
        PosVel stateNew = kick(body.getState().toPosVel(), dV);
        fuel.consumeMass(dM(body, fuel, dV));
        return stateNew;
    }

    public PosVel kick(Vector3D dV) {
        PosVel stateNew = kick(body.getState().toPosVel(), dV);
        return stateNew;
    }

    public PosVel kick(Vector3D dV, Propellant fuel) {
        PosVel stateNew = kick(body.getState().toPosVel(), dV);
        fuel.consumeMass(dM(body, fuel, dV.norm()));
        return stateNew;
    }

    /**
     * Compute the amount of propellant used in ideal conditions (no gravity losses, Tsiolkovsky)
     *
     * @param body Body that consumes DV.
     * @param dV   DV achieve using this propellant.
     */
    public double dM(ICelestialBody body, Propellant propellant, double dV) {
        double m = body.getTotalMass();
        double dM = m - Math.exp(-dV / (propellant.Isp() * 9.81)) * m;
        return dM;
    }

}
