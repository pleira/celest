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

import be.angelcorp.libs.celest.stateVector.StateVector;

import com.google.common.collect.Maps;

public class DiscreteTrajectory extends Trajectory {

	private TreeMap<Double, StateVector>	states	= Maps.newTreeMap();

	public DiscreteTrajectory() {

	}

	public DiscreteTrajectory(StateVector state) {
		addState(0, state);
	}

	public void addState(double t, StateVector state) {
		states.put(t, state);
	}

	@Override
	public StateVector evaluate(double t) throws FunctionEvaluationException {
		Entry<Double, StateVector> entry = states.floorEntry(t);
		if (entry == null)
			throw new FunctionEvaluationException(t, "No state found before the given time index");
		return entry.getValue();
	}

}
