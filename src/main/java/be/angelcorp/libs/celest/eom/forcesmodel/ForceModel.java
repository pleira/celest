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

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.physics.quantities.ObjectForce;
import be.angelcorp.libs.celest.physics.quantities.Torque;

import com.google.common.collect.ImmutableList;

/**
 * More public implementation of a {@link ForceModelCore}. This means you arr free to add/remove forces
 * yourself.
 * 
 * @author simon
 * 
 */
public class ForceModel extends ForceModelCore {

	/**
	 * Create a {@link ForceModel} for the given body
	 * 
	 * @param body
	 *            Bod where the forces act on
	 */
	public ForceModel(CelestialBody body) {
		super(body);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addForce(ObjectForce f) {
		super.addForce(f);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addTorque(Torque t) {
		super.addTorque(t);
	}

	/**
	 * Get all the forces currently acting on the spacecraft
	 * 
	 * @return An unchangeable list of all the forces
	 */
	public ImmutableList<ObjectForce> getForces() {
		return ImmutableList.copyOf(getForcesList());
	}

	/**
	 * Get all the pure torques currently acting on the spacecraft
	 * 
	 * @return An unchangeable list of all the pure torques
	 */
	public ImmutableList<Torque> getTorques() {
		return ImmutableList.copyOf(getTorquesList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeForce(ObjectForce f) {
		super.removeForce(f);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeTorque(Torque t) {
		super.removeTorque(t);
	}

}
