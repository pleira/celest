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
import be.angelcorp.libs.celest.physics.quantities.Force;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.math.linear.Vector3DMath;

public class PotentialForce extends Force {

	private CelestialBody	satellite;
	private CelestialBody	centerbody;

	public PotentialForce(CelestialBody satellite, CelestialBody centerbody) {
		this.satellite = satellite;
		this.centerbody = centerbody;
	}

	@Override
	public Force clone() {
		return new PotentialForce(satellite, centerbody);
	}

	/**
	 * Get the center body.
	 * <p>
	 * Body that pull on the satellite body
	 * </p>
	 * 
	 * @return satellite CelestialBody
	 */
	public CelestialBody getCenterbody() {
		return centerbody;
	}

	@Override
	public Vector3D getForce() {
		Vector3D r = Vector3DMath.relative(centerbody.getState().toCartesianElements().R,
				satellite.getState().toCartesianElements().R);
		Vector3D a = centerbody.getGravitationalPotential().evaluate(r);
		return a.multiply(satellite.getMass());
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
	 * Get the satellite body.
	 * <p>
	 * Body where the force acts upon
	 * </p>
	 * 
	 * @return satellite CelestialBody
	 */
	public CelestialBody getSatellite() {
		return satellite;
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
