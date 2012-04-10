/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
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
import be.angelcorp.libs.celest.state.positionState.IPositionStateDerivative;

/**
 * Computes the derivatives of the state vector of a specific body
 * 
 * @author simon
 * 
 */
public interface IStateDerivatives {

	/**
	 * Body for which the state vector derivatives are computed
	 */
	public CelestialBody getBody();

	/**
	 * Compute the state derivatives of the body after x seconds
	 * 
	 * @param t
	 *            Time [s]
	 * @return Derivatives of the current state vector
	 */
	public IPositionStateDerivative getDerivatives(double t);

}
