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
package be.angelcorp.libs.celest.trajectory;

import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.math.FunctionEvaluationException;

import be.angelcorp.libs.celest.stateVector.IStateVector;
import be.angelcorp.libs.celest.stateVector.StateVector;

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
	private TreeMap<Double, IStateVector>	states	= Maps.newTreeMap();

	/**
	 * Create a new {@link DiscreteTrajectory}, with no known states
	 */
	public DiscreteTrajectory() {
	}

	/**
	 * Create a new {@link DiscreteTrajectory} with the given state at epoch 0.
	 * 
	 * @param state
	 *            State at epoch t=0s
	 */
	public DiscreteTrajectory(StateVector state) {
		addState(0, state);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addState(double t, IStateVector state) {
		states.put(t, state);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IStateVector evaluate(double t) throws FunctionEvaluationException {
		Entry<Double, IStateVector> entry = states.floorEntry(t);
		if (entry == null)
			throw new FunctionEvaluationException(t, "No state found before the given time index");
		return entry.getValue();
	}

}
