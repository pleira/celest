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
package be.angelcorp.libs.celest.trajectory;

import be.angelcorp.libs.celest.state.positionState.IPositionState;
import be.angelcorp.libs.celest.time.IJulianDate;

/**
 * Create a trajectory based on a set of known states at known epochs. Then upon evaluation, the state
 * closest to, but before is returned as the state at the requested epoch. This trajectory does
 * <b>NOT</b> perform interpolation between the states.
 * 
 * @author Simon Billemont
 */
public interface IDiscreteTrajectory extends ITrajectory {

	/**
	 * Add a new key, a known state at a known epoch.
	 * 
	 * @param t
	 *            Epoch where the state is known [s]
	 * @param state
	 *            Known state at the given epoch.
	 */
	public abstract void addState(IJulianDate t, IPositionState state);

}