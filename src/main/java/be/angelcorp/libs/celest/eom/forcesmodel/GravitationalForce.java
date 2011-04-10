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
import be.angelcorp.libs.math.linear.Vector3D;

public abstract class GravitationalForce extends ObjectForce {

	/**
	 * Body generating the gravitationalForce
	 */
	private CelestialBody	gravitationBody;

	public GravitationalForce(CelestialBody gravitationBody, CelestialBody satellite) {
		super(satellite);
		this.gravitationBody = gravitationBody;
	}

	@Override
	public Vector3D getForce() {
		Vector3D rCenter = getGravitationBody().getState().toCartesianElements().getR();
		Vector3D rSatellite = getObject().getState().toCartesianElements().getR();
		Vector3D g = gravitationBody.getGravitationalPotential().evaluate(rSatellite.subtract(rCenter));
		return g.multiply(getObject().getMass());
	}

	public CelestialBody getGravitationBody() {
		return gravitationBody;
	}

	public void setGravitationBody(CelestialBody gravitationBody) {
		this.gravitationBody = gravitationBody;
	}

}
