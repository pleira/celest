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
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem;
import be.angelcorp.celest.physics.quantities.ObjectForce;
import be.angelcorp.celest.physics.quantities.Torque;
import be.angelcorp.celest.state.IStateEquation;
import be.angelcorp.celest.state.PosVel;
import be.angelcorp.celest.state.positionState.CartesianDerivative;
import be.angelcorp.celest.state.positionState.ICartesianDerivative;
import be.angelcorp.celest.time.Epoch;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.math.linear.Vector3D$;
import com.google.common.collect.Lists;
import org.apache.commons.math3.linear.RealVector;

import java.util.List;

/**
 * Determines the state derivatives of an object, by taking into account all the forces acting on that
 * object. These can be gravitational, atmospheric, solar pressure, ...
 *
 * @author simon
 */
public class ForceModelCore implements IStateEquation<PosVel, ICartesianDerivative> {

    /**
     * Body where all the forces/torques act on
     */
    private CelestialBody body;

    /**
     * Forces acting on the specific body
     */
    private List<ObjectForce> forces = Lists.newLinkedList();
    /**
     * Torques acting on the specific body
     */
    private List<Torque> torques = Lists.newLinkedList();

    /**
     * Create a force model for the given body
     *
     * @param body Body where all the forces act on
     */
    public ForceModelCore(CelestialBody body) {
        this.body = body;
    }

    /**
     * Add a force that acts on the body
     *
     * @see ForceModelCore#forces
     */
    protected void addForce(ObjectForce f) {
        forces.add(f);
    }

    /**
     * Add a torque that acts on the body
     *
     * @see ForceModelCore#torques
     */
    protected void addTorque(Torque t) {
        torques.add(t);
    }

    @Override
    public ICartesianDerivative calculateDerivatives(Epoch t, PosVel y) {
        Vector3D a = Vector3D$.MODULE$.ZERO();
        for (ObjectForce f : getForcesList()) {
            Vector3D dA = f.toAcceleration();
            a = a.add(dA);
        }
        return new CartesianDerivative(y.velocity(), a);
    }

    @Override
    public PosVel createState(RealVector y) {
        final scala.Option<BodyCenteredSystem> none = scala.Option.apply(null);
        return PosVel.apply(y.getEntry(0), y.getEntry(1), y.getEntry(2), y.getEntry(3), y.getEntry(4), y.getEntry(5), none);
    }

    /**
     * @see ForceModelCore#body
     */
    public CelestialBody getBody() {
        return body;
    }

    @Override
    public int getDimension() {
        return 6;
    }

    /**
     * @see ForceModelCore#forces
     */
    protected List<ObjectForce> getForcesList() {
        return forces;
    }

    /**
     * @see ForceModelCore#torques
     */
    protected List<Torque> getTorquesList() {
        return torques;
    }

    /**
     * Remove a force acting on the body
     *
     * @see ForceModelCore#forces
     */
    protected void removeForce(ObjectForce f) {
        forces.remove(f);
    }

    /**
     * Remove a torque acting on the body
     *
     * @see ForceModelCore#torques
     */
    protected void removeTorque(Torque t) {
        torques.remove(t);
    }

}
