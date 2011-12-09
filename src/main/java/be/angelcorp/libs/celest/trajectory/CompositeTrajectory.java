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
import be.angelcorp.libs.celest.time.IJulianDate;

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
	private TreeMap<IJulianDate, ITrajectory>	trajectories	= Maps.newTreeMap();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addTrajectory(ITrajectory t, IJulianDate t0) {
		trajectories.put(t0, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IStateVector evaluate(IJulianDate t) throws FunctionEvaluationException {
		Entry<IJulianDate, ITrajectory> entry = trajectories.floorEntry(t);
		if (entry == null)
			throw new FunctionEvaluationException(t.getJD(), "No trajectory found");

		ITrajectory trajectory = entry.getValue();
		return trajectory.evaluate(t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeTrajectory(ITrajectory trajectory) {
		for (IJulianDate t : trajectories.keySet()) {
			if (trajectories.get(t) == trajectory)
				trajectories.remove(t);
		}
	}

}
