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
package be.angelcorp.libs.celest.potential;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.body.bodyCollection.BodyCollection;
import be.angelcorp.libs.celest.physics.quantities.Force;
import be.angelcorp.libs.math.linear.Vector3D;

/**
 * Force that represents the total gravitational pull on an object.
 * <p>
 * For this to work, you must populate the universe. That is put all the objects pulling on the satellite
 * body in a bodycontainer
 * </p>
 * 
 * @author simon
 * 
 */
public class CumulativePotentialForce extends Force {

	private BodyCollection	collection;
	private CelestialBody	satellite;

	public CumulativePotentialForce(CelestialBody satellite, BodyCollection collection) {
		this.satellite = satellite;
		this.collection = collection;
	}

	@Override
	public Force clone() {
		return new CumulativePotentialForce(satellite, collection);
	}

	/**
	 * Get the total gravitational force of all the bodies in the universe (accept itself)
	 * 
	 * @return Total gravitational pull
	 */
	@Override
	public Vector3D getForce() {
		Vector3D sum = Vector3D.ZERO;
		for (CelestialBody centerbody : collection.getBodies()) {
			if (centerbody != satellite) {
				PotentialForce f = new PotentialForce(satellite, centerbody);
				sum.add(f.getForce());
			}
		}
		return sum;
	}

	/**
	 * No offset of the force wrt the cg of the satellite
	 * 
	 * @return Vector3D(0, 0, 0)
	 */
	@Override
	public Vector3D getOffset() {
		return Vector3D.ZERO;
	}

	/**
	 * Cannot set the force as it computed from a set of potentials
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public void setForce(Vector3D force) {
		throw new UnsupportedOperationException();
	}
}
