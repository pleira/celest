/**
 * Copyright (C) 2010 Simon Billemont <aodtorusan@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.libs.celest.physics.quanteties;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.math.linear.Vector3D;

public class ObjectForce extends Force {

	private CelestialBody	object;
	private Vector3D		offset;

	public ObjectForce(CelestialBody object) {
		this(object, Vector3D.ZERO);
	}

	public ObjectForce(CelestialBody object, Vector3D force) {
		setObject(object);
		setForce(force);
	}

	@Override
	public Force clone() {
		return new ObjectForce(getObject(), getForce().clone());
	}

	public CelestialBody getObject() {
		return object;
	}

	@Override
	public Vector3D getOffset() {
		return offset;
	}

	public void setObject(CelestialBody object) {
		this.object = object;
	}

	public void setOffset(Vector3D offset) {
		this.offset = offset;
	}

	public Vector3D toAcceleration() {
		// F = m a
		// a = F/m
		return getForce().divide(getObject().getMass());
	}
}
