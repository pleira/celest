/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
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
package be.angelcorp.celest.maneuvers.targeters.exposin;

import be.angelcorp.celest.body.ICelestialBody;
import be.angelcorp.celest.constants.SolarConstants;
import be.angelcorp.celest.maneuvers.targeters.TPBVP;
import be.angelcorp.celest.state.PosVel;
import be.angelcorp.celest.time.Epoch;
import be.angelcorp.libs.math.functions.ExponentialSinusoid;
import be.angelcorp.libs.math.linear.Vector3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.math3.util.FastMath.PI;
import static org.apache.commons.math3.util.FastMath.acos;

/**
 * Exponential sinusoid solution (low thrust solution) to the Lambert problem ( a two-point boundary
 * value problem, TPBVP). It assumes a constant low thrust by the spacecraft in the direction of the
 * instantaneous velocity vector.
 *
 * @author simon
 */
public class ExpoSin extends TPBVP {

    private static final Logger logger = LoggerFactory.getLogger(ExpoSin.class);

    /**
     * Standard gravitational parameter of the center body (body with the strongest influence on the
     * satellite). Default is the standard gravitational parameter of the sun.
     * <p>
     * <b>Unit: [m<sup>3</sup>/s<sup>2</sup>]</b>
     * </p>
     */
    private ICelestialBody center = SolarConstants.body();
    /**
     * Amount of rotations to perform around the center body in order to arrive at r2.
     * <p>
     * <b>Unit: [-]</b>
     * </p>
     */
    private int N = 0;
    /**
     * Exposin parameter k2 (Winding parameter). This must be assumed. Set using:
     * <ul>
     * <li>{@link ExpoSin#assumeK2(double)} Set the k2 directly</li>
     * <li>{@link ExpoSin#assumeK2FromN(int)} Set the k2 using a given amount of revolutions as guide</li>
     * </ul>
     * <p>
     * <b>Unit: [-]</b>
     * </p>
     */
    private double assumeK2 = 1. / 12;

    /**
     * Exposin problem with the given boundary conditions
     *
     * @param r1        StateVector that defines the starting position [m]
     * @param r2        StateVector that defines the wanted end position [m]
     * @param departure Epoch of departure (epoch at r1) [jd]
     * @param arrival   Epoch of arrival (epoch at r2) [jd]
     */
    public ExpoSin(PosVel r1, PosVel r2, Epoch departure, Epoch arrival) {
        super(r1, r2, departure, arrival);
    }

    /**
     * Set the k2 parameter (winding parameter) used in the exposin solution directly.
     *
     * @param assumeK2 New value for k2 [-]
     */
    public void assumeK2(double assumeK2) {
        this.assumeK2 = assumeK2;
    }

    /**
     * Set the k2 parameter (winding parameter) used in the exposin solution by inferring it from
     * the set amount of revolutions around the center body.
     */
    public void assumeK2FromN() {
        assumeK2FromN(N);
    }

    /**
     * Set the k2 parameter (winding parameter) used in the exposin solution by inferring it from
     * the given amount of revolutions around the center body.
     *
     * @param N Revolutions to assume [N]
     */
    public void assumeK2FromN(int N) {
        this.assumeK2 = 1. / (2 * N);
    }

    /**
     * Get all the possible exposin solutions for reaching r2 from r1 (without time constraignt). It
     * is a function of a generic parameter gamma.
     *
     * @return All possible exposin solutions
     */
    public ExpoSinSolutionSet getSolutionSet() {
        Vector3D r1vec = this.r1.toPosVel().position();
        Vector3D r2vec = this.r2.toPosVel().position();
        double r1 = r1vec.norm();
        double r2 = r2vec.norm();

        double psi = acos(r1vec.dot(r2vec) / (r1 * r2));
        double theta = psi + 2 * PI * N;

        return new ExpoSinSolutionSet(r1, r2, assumeK2, theta, center.getMu());
    }

    @Override
    public ExpoSinTrajectory getTrajectory() {
        // Create a set of exposin solutions leading from r1 to r2
        ExpoSinSolutionSet solutions = getSolutionSet();
        // Find the curve for which the tof is equal the the required tof
        double gammaOptimal = solutions.getOptimalSolution(getdT());
        logger.debug("Gamma optimal: {}", gammaOptimal);

        // Create a trajectory from the found solution
        ExponentialSinusoid solution = solutions.getExpoSin(gammaOptimal);
        return new ExpoSinTrajectory(solution, gammaOptimal, center, departureEpoch);
    }

    /**
     * Set the center celesital body
     */
    public void setCenter(ICelestialBody center) {
        this.center = center;
    }

    /**
     * Set the amount of revolutions that are to be made around the center body (default:0)
     *
     * @param n Nr of revolutions
     */
    public void setN(int n) {
        N = n;
    }
}
