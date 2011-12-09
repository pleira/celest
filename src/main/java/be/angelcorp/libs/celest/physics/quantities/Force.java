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

public abstract class Force implements Comparable<Force>, Cloneable {

	private Vector3D	force;

	public Vector3D add(Force force2) {
		setForce(getForce().add(force2.getForce()));
		return getForce();
	}

	public void add(Vector3D term) {
		setForce(getForce().add(term));
	}

	@Override
	public int compareTo(Force o) {
		return Double.compare(getForce().getNormSq(), o.getForce().getNormSq());
	}

	public Vector3D getForce() {
		return force;
	}

	public abstract Vector3D getOffset();

	public Vector3D mult(float scalar) {
		setForce(getForce().multiply(scalar));
		return getForce();
	}

	public void setForce(Vector3D force) {
		this.force = force;

	}

	@Override
	public String toString() {
		return String.format("[Force f:%s]", getForce());
	}

	public Torque toTorque() {
		return toTorque(Vector3D.ZERO);
	}

	public Torque toTorque(Vector3D newOrigin) {
		Torque t = new Torque();
		Vector3D dR = Vector3DMath.relative(getOffset(), newOrigin);
		t.setTorque(Vector3DMath.cross(dR, getForce()));
		return t;
	}
}
