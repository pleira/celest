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
import be.angelcorp.libs.celest.body.bodyCollection.ITwoBodyCollection;
import be.angelcorp.libs.celest.body.bodyCollection.TwoBodyCollection;
import be.angelcorp.libs.celest.constants.EarthConstants;
import be.angelcorp.libs.celest.eom.forcesmodel.ForceModelCore;
import be.angelcorp.libs.celest.eom.forcesmodel.GravitationalForce_C;

/**
 * Function that holds the calculates the acceleration in Cartesian coordinates when in the presence of
 * another spherical body
 * 
 * @author Simon Billemont
 * 
 */
public class TwoBody extends ForceModelCore {

	/**
	 * {@link TwoBody} problem for a simple Earth system (where the given body is a satellite around the
	 * earth)
	 * 
	 * @param body
	 *            Satellite around the Earth
	 */
	public TwoBody(ICelestialBody body) {
		this(new TwoBodyCollection(EarthConstants.bodyCenter(), body), body);
	}

	/**
	 * {@link TwoBody} problem for a body in a given two body system
	 * 
	 * @param bodies
	 *            The two body system considered
	 * @param body
	 *            The body for which to compute the {@link IStateDerivatives}
	 */
	public TwoBody(ITwoBodyCollection bodies, ICelestialBody body) {
		super(body);
		addForce(new GravitationalForce_C(body, bodies.other(body)));
	}

}
