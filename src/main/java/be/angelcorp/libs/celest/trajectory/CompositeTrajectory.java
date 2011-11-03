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

import com.google.common.collect.Maps;

/**
 * Implements a {@link ICompositeTrajectory}, a trajectory that effectively patches several trajectories
 * together. The sub-trajectory evaluated is the one with the starting epoch closest to but before the
 * requested epoch.
 * 
 * @author Simon Billemont
 * @see ICompositeTrajectory
 */
public class CompositeTrajectory implements ICompositeTrajectory {

	/**
	 * Sorted map that stores trajectories with there corresponding starting time
	 */
	private TreeMap<Double, ITrajectory>	trajectories	= Maps.newTreeMap();

	/**
	 * Make time relative to the start of the trajectory segment
	 * <p>
	 * tEval = t0 - t<br />
	 * where tEval is the time passed to the underlying trajectory<br />
	 * t0 is the start time of the trajectory <br />
	 * t is the global time at which this CompositeTrajectory is evaluated
	 * </p>
	 */
	private boolean							relativeTime	= true;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addTrajectory(ITrajectory t, double t0) {
		trajectories.put(t0, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IStateVector evaluate(double t) throws FunctionEvaluationException {
		Entry<Double, ITrajectory> entry = trajectories.floorEntry(t);
		ITrajectory trajectory = entry.getValue();

		if (trajectory == null)
			throw new FunctionEvaluationException(t, "No trajectory found");

		if (relativeTime)
			return trajectory.evaluate(t - entry.getKey());
		else
			return trajectory.evaluate(t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeTrajectory(ITrajectory trajectory) {
		for (Double t : trajectories.keySet()) {
			if (trajectories.get(t) == trajectory)
				trajectories.remove(t);
		}
	}

}
