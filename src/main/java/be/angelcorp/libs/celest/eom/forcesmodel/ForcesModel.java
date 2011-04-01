/**
 * Copyright (C) 2011 Simon Billemont <aodtorusan@gmail.com>
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 3.0 Unported
 * (CC BY-NC 3.0) (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *        http://creativecommons.org/licenses/by-nc/3.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.libs.celest.eom.forcesmodel;

import java.util.List;

import be.angelcorp.libs.celest.physics.quantities.Force;
import be.angelcorp.libs.celest.physics.quantities.Torque;

import com.google.common.collect.ImmutableList;

public class ForcesModel {

	private List<Force>		forces;
	private List<Torque>	torques;

	public void addForce(Force f) {
		forces.add(f);
	}

	public void addTorque(Torque t) {
		torques.add(t);
	}

	public ImmutableList<Force> getForces() {
		return ImmutableList.copyOf(forces);
	}

	public ImmutableList<Torque> getTorques() {
		return ImmutableList.copyOf(torques);
	}

	public void removeForce(Force f) {
		forces.remove(f);
	}

	public void removeTorque(Torque t) {
		torques.remove(t);
	}

}
