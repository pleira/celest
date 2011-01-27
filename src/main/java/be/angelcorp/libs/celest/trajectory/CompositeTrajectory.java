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
package be.angelcorp.libs.celest.trajectory;

import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.math.FunctionEvaluationException;

import be.angelcorp.libs.celest.stateVector.StateVector;

import com.google.common.collect.Maps;

/**
 * 
 * @author simon
 * 
 */
public class CompositeTrajectory extends Trajectory {

	private TreeMap<Double, Trajectory>	trajectories	= Maps.newTreeMap();
	/**
	 * Make time relative to the start of the trajectory segment
	 * <p>
	 * tEval = t0 - t<br />
	 * where tEval is the time passed to the underlying trajectory<br />
	 * t0 is the start time of the trajectory <br />
	 * t is the global time at which this CompositeTrajectory is evaluated
	 * </p>
	 */
	private boolean						relativeTime	= true;

	public void addTrajectory(Trajectory t, double t0) {
		trajectories.put(t0, t);
	}

	@Override
	public StateVector evaluate(double t) throws FunctionEvaluationException {
		Entry<Double, Trajectory> entry = trajectories.floorEntry(t);
		Trajectory trajectory = entry.getValue();

		if (trajectory == null)
			throw new FunctionEvaluationException(t, "No trajectory found");
		if (relativeTime)
			return trajectory.evaluate(t - entry.getKey());
		else
			return trajectory.evaluate(t);
	}

	public void removeTrajectory(Trajectory trajectory) {
		for (Double t : trajectories.keySet()) {
			if (trajectories.get(t) == trajectory)
				trajectories.remove(t);
		}
	}

}
