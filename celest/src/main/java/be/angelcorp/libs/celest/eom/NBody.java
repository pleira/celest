/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
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

import be.angelcorp.libs.celest.body.ICelestialBody;
import be.angelcorp.libs.celest.body.bodyCollection.IBodyCollection;
import be.angelcorp.libs.celest.eom.forcesmodel.ForceModelCore;
import be.angelcorp.libs.celest.eom.forcesmodel.GravitationalForce_C;

/**
 * Create the EOM for a single body in an N-Body environment
 * 
 * @author Simon Billemont
 * 
 */
public class NBody extends ForceModelCore {

	/**
	 * @param body
	 *            Body for which the {@link IStateDerivatives} are computed
	 * @param bodies
	 *            All the bodies in the system
	 */
	public NBody(ICelestialBody body, IBodyCollection bodies) {
		super(body);
		for (ICelestialBody body2 : bodies.getBodies()) {
			if (!body2.equals(body))
				addForce(new GravitationalForce_C(body, body2));
		}
	}

}
