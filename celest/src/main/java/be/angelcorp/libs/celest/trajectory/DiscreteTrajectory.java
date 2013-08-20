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

import java.util.Map.Entry;
import java.util.TreeMap;

import be.angelcorp.libs.celest.state.Orbit;
import be.angelcorp.libs.celest.time.IJulianDate;

import com.google.common.collect.Maps;

/**
 * Implements a {@link IDiscreteTrajectory}, a trajectory that evaluates based on a set of known states
 * at known epochs. Does <b>NOT</b> perform interpolation.
 * 
 * @author Simon Billemont
 * @see IDiscreteTrajectory
 */
public class DiscreteTrajectory implements IDiscreteTrajectory {

	/**
	 * Sorting container which sorts all known states at there respective epoch
	 */
	private TreeMap<IJulianDate, Orbit>	states	= Maps.newTreeMap();

	/**
	 * Create a new {@link DiscreteTrajectory}, with no known states
	 */
	public DiscreteTrajectory() {
	}

	/**
	 * Create a new {@link DiscreteTrajectory} with the given state at given epoch.
	 * 
	 * @param state
	 *            State at specified epoch
	 */
	public DiscreteTrajectory(Orbit state, IJulianDate epoch) {
		addState(epoch, state);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addState(IJulianDate t, Orbit state) {
		states.put(t, state);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Orbit evaluate(IJulianDate t) {
		Entry<IJulianDate, Orbit> entry = states.floorEntry(t);
		if (entry == null)
			throw new ArithmeticException("No state found before the the julian date " + t.getJD());
		return entry.getValue();
	}

}
