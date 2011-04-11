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
package be.angelcorp.libs.celest.eom;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.body.bodyCollection.TwoBodyCollection;
import be.angelcorp.libs.celest.constants.EarthConstants;
import be.angelcorp.libs.celest.eom.forcesmodel.GravitationalForce;
import be.angelcorp.libs.celest.stateVector.CartesianDerivative;
import be.angelcorp.libs.celest.stateVector.CartesianElements;
import be.angelcorp.libs.celest.stateVector.StateDerivativeVector;
import be.angelcorp.libs.math.linear.Vector3D;

/**
 * Function that holds the calculates the acceleration in Cartesian coordinates when in the presence of
 * another spherical body
 * 
 * @author Simon Billemont
 * 
 */
public class TwoBody implements StateDerivatives {

	private TwoBodyCollection	bodies;
	private CelestialBody		body;

	/**
	 * Create a body in a simple earth system (where the given body is a satellite around the earth)
	 * 
	 * @param body
	 */
	public TwoBody(CelestialBody body) {
		bodies = new TwoBodyCollection(EarthConstants.body, body); // Create the twobody container
		bodies.other(body).setState(new CartesianElements()); // Set earth R=<0,0,0> and V=<0,0,0>
		this.body = body;

	}

	public TwoBody(TwoBodyCollection bodies, CelestialBody body) {
		this.bodies = bodies;
		this.body = body;
	}

	public TwoBodyCollection getBodies() {
		return bodies;
	}

	@Override
	public CelestialBody getBody() {
		return body;
	}

	@Override
	public StateDerivativeVector getDerivatives(double t) {
		GravitationalForce fg = new GravitationalForce(body, bodies.other(body));
		Vector3D A = fg.toAcceleration();

		CartesianElements thisState = body.getState().toCartesianElements();
		return new CartesianDerivative(thisState.getV(), A);
	}
}
