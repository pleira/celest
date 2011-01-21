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
package be.angelcorp.libs.celest.physics.quanteties;

import be.angelcorp.libs.math.linear.Vector3D;

public class FreeForce extends Force {

	private Vector3D	origin;

	public FreeForce() {
		this(Vector3D.ZERO, Vector3D.ZERO);
	}

	public FreeForce(Vector3D force) {
		this(force, Vector3D.ZERO);
	}

	public FreeForce(Vector3D force, Vector3D origin) {
		setForce(force);
		setOrigin(origin);
	}

	@Override
	public Force clone() {
		return new FreeForce(getForce().clone(), getOrigin().clone());
	}

	@Override
	public Vector3D getOffset() {
		return origin;
	}

	public Vector3D getOrigin() {
		return origin;
	}

	public void setOrigin(Vector3D origin) {
		this.origin = origin;
	}

}
