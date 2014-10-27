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
package be.angelcorp.celest.maneuvers.targeters.exposin;

import be.angelcorp.celest.maneuvers.targeters.TPBVP;
import be.angelcorp.celest.math.functions.ExponentialSinusoid;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.RombergIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.analysis.solvers.RiddersSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.util.FastMath;

import static org.apache.commons.math3.util.FastMath.*;

/**
 * Creates a solution set for a given exposin problem. It contains all of the possible trajectories from
 * r1 to r2, without the time constraint. For the optimal solution, it solves the time of flight to match
 * the set dT.
 *
 * @author Simon Billemont
 */
public class ExpoSinSolutionSet implements UnivariateFunction {

    /**
     * Computes the exposin k0 parameter
     *
     * @see ExponentialSinusoid#k0
     */
    private static double getK0(double r1, double k1, double phi) {
        double k0 = r1 / exp(k1 * sin(phi));
        return k0;
    }

    /**
     * Computes the exposin k1 parameter
     *
     * @see ExponentialSinusoid#k1
     */
    private static double getK1(double log, double tan_gamma_1, double k2, double theta) {
        double k1_base = log + (tan_gamma_1 / k2) * sin(k2 * theta);
        double k1 = Math.signum(k1_base)
                * Math.sqrt(pow(k1_base / (1 - cos(k2 * theta)), 2) + (pow(tan_gamma_1, 2)) / (k2 * k2));
        return k1;
    }

    /**
     * Computes the exposin &phi; parameter
     *
     * @see ExponentialSinusoid#phi
     */
    private static double getPhi(double tan_gamma_1, double k1, double k2) {
        double phi = acos(tan_gamma_1 / (k1 * k2));
        return phi;

    }

    /**
     * Domain (of &gamma;_1, flight path angle at departure) over which this function has valid
     * solutions.
     * <p>
     * <b>Unit: [rad]</b>
     * </p>
     */
    private Domain domain_gamma_1;

    /**
     * Start radius of the transfer orbit (r1 of the {@link TPBVP})
     * <p>
     * <b>Unit: [m]</b>
     * </p>
     */
    private double r1;
    /**
     * Cached value of log(r1 / r2)
     * <p>
     * <b>Unit: [-]</b>
     * </p>
     */
    private double log;
    /**
     * Cached value of the exposin parameter k2
     * <p>
     * <b>Unit: [-]</b>
     * </p>
     */
    private double k2;
    /**
     * Total traveled angle between r1 and r2
     * <p>
     * <b>Unit: [rad]</b>
     * </p>
     */
    private double theta;
    /**
     * Standard gravitational parameter of the center body.
     * <p>
     * <b>Unit: [m<sup>3</sup>/s<sup>2</sup>]</b>
     * </p>
     */
    private double mu_center;

    public ExpoSinSolutionSet(double r1, double r2, double k2, double theta, double mu_center) {
        // Store the exposin base variables
        this.r1 = r1;
        this.k2 = k2;
        this.theta = theta;
        this.mu_center = mu_center;

        // Cache values commonly used
        log = FastMath.log(r1 / r2);

		/* Compute the domain of gamma [gamma_min, gamma_max] */
        double sqrt_delta = sqrt((2 * (1 - cos(k2 * theta))) / pow(k2, 4) - (log * log));
        double gamma_1_min = atan((k2 / 2)
                * (-log * (1 / tan(k2 * theta / 2)) - sqrt_delta));
        double gamma_1_max = atan((k2 / 2)
                * (-log * (1 / tan(k2 * theta / 2)) + sqrt_delta));
        domain_gamma_1 = new Domain(gamma_1_min, gamma_1_max);
    }

    /**
     * Domain of gamma over which this function yields valid results
     */
    public Domain getDomain() {
        return domain_gamma_1;
    }

    /**
     * Get the exponential sinusiod trajectory that is linked to a given gamma value.
     */
    public ExponentialSinusoid getExpoSin(double gamma_1) {
        /* Compute the exposin parameters */
        double tan_gamma_1 = tan(gamma_1);
        double k1 = getK1(log, tan_gamma_1, k2, theta);
        double phi = getPhi(tan_gamma_1, k1, k2);
        double k0 = getK0(r1, k1, phi);
        // Return the given exposin
        return new ExponentialSinusoid(k0, k1, k2, 0, phi);
    }

    /**
     * Compute the value of &gamma;1 (flight path angle at departure, r1), when the transfer time between
     * r1 and r2 is equal the the required time passed to this function.
     *
     * @param dT The required travel time from r1-r2 [s]
     * @return The optimal gamma value
     */
    public double getOptimalSolution(final double dT) {
        // Root finding technique
        UnivariateSolver solver = new RiddersSolver(1E-10);

        // Find the root of where the tof equals dT
        UnivariateFunction rootFunction = new UnivariateFunction() {
            @Override
            public double value(double gamma_1) {
                return ExpoSinSolutionSet.this.value(gamma_1) - dT;
            }
        };
        double gamma = solver.solve(50, rootFunction, domain_gamma_1.lowerBound, domain_gamma_1.upperBound);

        // return the optimal gamma value
        return gamma;
    }

    /**
     * Maximum rotation angle, angle between r1 and r2, including any complete loops around the central
     * body.
     * <p>
     * <b><Unit: [rad]/b>
     * </p>
     */
    public double getThetaMax() {
        return theta;
    }

    /**
     * Find the time of flight for a given value of &gamma;_1 (flight path angle at departure point).
     */
    @Override
    public double value(double gamma_1) {
		/* Create the exposin for the given departure flight path angle */
        ExponentialSinusoid exposin = getExpoSin(gamma_1);

		/* Formulate the tof equation */
        final ExpoSinAngularRate theta_dot = new ExpoSinAngularRate(exposin, mu_center);
        UnivariateFunction tof = new UnivariateFunction() {
            @Override
            public double value(double theta) {
                return 1. / theta_dot.value(theta);
            }
        };

        // Integrate the tof equation from 0 to theta max to find the total time of flight
        UnivariateIntegrator integrator = new RombergIntegrator(1e-6, 1e-6, 2, 16);
        double timeOfFlight = integrator.integrate(4096, tof, 0, theta);

        return timeOfFlight;
    }

    class Domain {
        public final double lowerBound;
        public final double upperBound;
        public Domain( double lowerBound, double upperBound ) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }
    }

}
