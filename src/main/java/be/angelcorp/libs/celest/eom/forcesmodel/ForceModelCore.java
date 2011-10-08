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
package be.angelcorp.libs.celest.eom.forcesmodel;

import java.util.List;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.eom.IStateDerivatives;
import be.angelcorp.libs.celest.physics.quantities.ObjectForce;
import be.angelcorp.libs.celest.physics.quantities.Torque;
import be.angelcorp.libs.celest.stateVector.CartesianDerivative;
import be.angelcorp.libs.celest.stateVector.ICartesianElements;
import be.angelcorp.libs.celest.stateVector.IStateDerivativeVector;
import be.angelcorp.libs.math.linear.Vector3D;

import com.google.common.collect.Lists;

/**
 * Determines the state derivatives of an object, by taking into account all the forces acting on that
 * object. These can be gravitational, atmospheric, solar pressure, ...
 * 
 * @author simon
 * 
 */
public class ForceModelCore implements IStateDerivatives {

	/**
	 * Body where all the forces/torques act on
	 */
	private CelestialBody		body;

	/**
	 * Forces acting on the specific body
	 */
	private List<ObjectForce>	forces	= Lists.newLinkedList();
	/**
	 * Torques acting on the specific body
	 */
	private List<Torque>		torques	= Lists.newLinkedList();

	/**
	 * Create a force model for the given body
	 * 
	 * @param body
	 *            Body where all the forces act on
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

	/**
	 * @see ForceModelCore#body
	 */
	@Override
	public CelestialBody getBody() {
		return body;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IStateDerivativeVector getDerivatives(double t) {
		Vector3D a = Vector3D.ZERO;
		for (ObjectForce f : getForcesList()) {
			Vector3D dA = f.toAcceleration();
			a = a.add(dA);
		}

		ICartesianElements thisState = body.getState().toCartesianElements();
		return new CartesianDerivative(thisState.getV(), a);
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
