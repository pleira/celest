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
package be.angelcorp.celest.maneuvers.targeters;

import be.angelcorp.celest.state.PosVel;
import be.angelcorp.celest.time.Epoch;
import be.angelcorp.celest.trajectory.Trajectory;

/**
 * Base class for the Lambert problem, meaning finding the transfer arc for a two-point boundary value
 * problem (TPBVP). Here the start and end position are known, as well as the time that the satellite is
 * to travel between the two points.
 *
 * @author Simon Billemont
 */
public abstract class TPBVP {

    /**
     * Epoch at which the satellite or body departs at r1
     * <p>
     * <b>Unit: [jd]</b>
     * </p>
     */
    protected Epoch departureEpoch;
    /**
     * Epoch at which the satellite or body arrives at r2
     * <p>
     * <b>Unit: [jd]</b>
     * </p>
     */
    protected Epoch arrivalEpoch;
    /**
     * Start position
     * <p>
     * <b>If converted to Cartesian coordinates; unit: [m]</b>
     * </p>
     */
    protected PosVel r1;
    /**
     * Targeted end position
     * <p>
     * <b>If converted to Cartesian coordinates; unit: [m]</b>
     * </p>
     */
    protected PosVel r2;

    /**
     * Construct a TPBVP problem with known, start/end points and the travel time between r1 and r2.
     *
     * @param r1        Start position
     * @param r2        End position
     * @param departure Epoch of departure (epoch at r1)
     * @param arrival   Epoch of arrival (epoch at r2)
     */
    public TPBVP(PosVel r1, PosVel r2, Epoch departure, Epoch arrival) {
        this.r1 = r1;
        this.r2 = r2;
        this.departureEpoch = departure;
        this.arrivalEpoch = arrival;
    }

    /**
     * Travel time in seconds between departure and arrival
     *
     * @return Travel time [s]
     */
    public double getdT() {
        return arrivalEpoch.relativeToS(departureEpoch);
    }

    /**
     * Find the optimal trajectory according to this targeted between r1->r2 for the given travel time.
     *
     * @return An optimal trajectory
     */
    public abstract Trajectory getTrajectory();

}
