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

import be.angelcorp.libs.celest.time.IJulianDate;

/**
 * A {@link ICompositeTrajectory} is a trajectory that is split in discrete trajectory segments.
 * Trajectories are added with a starting time. When this trajectory is evaluated, the evaluation is
 * delegated to the sub-trajectory which starts closest to, but before the requested epoch.
 * 
 * @author Simon Billemont
 */
public interface ICompositeTrajectory extends ITrajectory {

	/**
	 * Add a new trajectory segment with a given starting time.
	 * 
	 * @param t
	 *            Trajectory segment
	 * @param t0
	 *            Time when the trajectory segment starts
	 */
	public abstract void addTrajectory(ITrajectory t, IJulianDate t0);

	/**
	 * Removes a sub-trajectory segment this {@link CompositeTrajectory}.
	 * 
	 * @param trajectory
	 *            Segment to remove
	 */
	public abstract void removeTrajectory(ITrajectory trajectory);

}