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
package be.angelcorp.libs.celest.maneuvers.targeters;

import org.apache.commons.math.MathException;

import be.angelcorp.libs.celest.stateVector.StateVector;
import be.angelcorp.libs.celest.trajectory.Trajectory;

/**
 * 
 * Base class for the Lambert problem, meaning finding the transfer arc for a two-point boundary value
 * problem (TPBVP). Here the start and end position are known, as well as the time that the satellite is
 * to travel between the two points.
 * 
 * @author Simon Billemont
 * 
 */
public abstract class TPBVP {

	/**
	 * Travel time between r1 and r2
	 * <p>
	 * <b>Unit: [s]</b>
	 * </p>
	 */
	protected double		dT;
	/**
	 * Start position
	 * <p>
	 * <b>If converted to Cartesian coordinates; unit: [m]</b>
	 * </p>
	 */
	protected StateVector	r1;
	/**
	 * Targeted end position
	 * <p>
	 * <b>If converted to Cartesian coordinates; unit: [m]</b>
	 * </p>
	 */
	protected StateVector	r2;

	/**
	 * Construct a TPBVP problem with known, start/end points and the travel time between r1 and r2.
	 * 
	 * @param r1
	 *            Start position
	 * @param r2
	 *            End position
	 * @param dT
	 *            Travel time
	 */
	public TPBVP(StateVector r1, StateVector r2, double dT) {
		this.r1 = r1;
		this.r2 = r2;
		this.dT = dT;
	}

	/**
	 * Find the optimal trajectory according to this targeted between r1->r2 for the given travel time.
	 * 
	 * @return An optimal trajectory
	 * @throws MathException
	 */
	public abstract Trajectory getTrajectory() throws MathException;

}
