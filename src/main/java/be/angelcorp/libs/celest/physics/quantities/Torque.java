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
package be.angelcorp.libs.celest.physics.quantities;

import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.math.linear.Vector3DMath;

public class Torque {

	private Vector3D	torque;

	public Torque() {
		this(Vector3D.ZERO);
	}

	public Torque(Force f, Vector3D arm) {
		setTorque(Vector3DMath.cross(arm, f.getForce()));
	}

	public Torque(Torque torque2) {
		setTorque(torque2.getTorque().clone());
	}

	public Torque(Vector3D torque) {
		setTorque(torque);
	}

	public Torque(Vector3D force, Vector3D arm) {
		setTorque(Vector3DMath.cross(arm, force));
	}

	public void add(Torque torque2) {
		torque = torque.add(torque2.getTorque());
	}

	public void add(Vector3D term) {
		torque = torque.add(term);
	}

	@Override
	public Torque clone() {
		return new Torque(this);
	}

	public Vector3D getTorque() {
		return torque;
	}

	public void setTorque(Vector3D torque) {
		this.torque = torque;
	}

	@Override
	public String toString() {
		return String.format("[Torque t:%s]", getTorque());
	}
}
