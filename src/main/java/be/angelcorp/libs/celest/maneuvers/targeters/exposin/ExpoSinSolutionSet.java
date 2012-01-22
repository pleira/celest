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
package be.angelcorp.libs.celest.maneuvers.targeters.exposin;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MathException;
import org.apache.commons.math.analysis.ComposableFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactoryImpl;
import org.apache.commons.math.optimization.OptimizationException;

import be.angelcorp.libs.math.functions.ExponentialSinusoid;
import be.angelcorp.libs.math.functions.domain.Domain;

/**
 * Creates a solution set for a given exposin problem. It contains all of the possible trajectories from
 * r1 to r2, without the time constraint. For the optimal solution, it solves the time of flight to match
 * the set dT.
 * 
 * @author Simon Billemont
 * 
 */
public class ExpoSinSolutionSet extends ComposableFunction {

	/**
	 * Computes the exposin K0 parameter
	 */
	private static double getK0(double r1, double k1, double phi) {
		double k0 = r1 * (Math.exp(-k1 * Math.sin(phi)));
		return k0;
	}

	/**
	 * Computes the exposin K1 parameter
	 */
	private static double getK1(double log, double tan_gamma_1, double k2, double theta, double gamma) {
		double signK1 = (log + (tan_gamma_1 / k2) * Math.sin(k2 * theta))
				/ (1 - Math.cos(k2 * theta));
		double k1 = Math.signum(signK1)
				* Math.sqrt(Math.pow(signK1, 2) + (Math.pow(Math.tan(gamma), 2)) / (k2 * k2));
		return k1;
	}

	/**
	 * Computes the exposin phi parameter
	 */
	private static double getPhi(double tan_gamma, double k1, double k2) {
		double phi = Math.acos(tan_gamma / (k1 * k2));
		return phi;

	}

	/**
	 * Domain (of gamma) over which this function has valid solutions.
	 * <p>
	 * <b>Unit: [-]</b>
	 * </p>
	 */
	private Domain	domain_gamma_1;

	/**
	 * Start radius of the spacecraft
	 * <p>
	 * <b>Unit: [m]</b>
	 * </p>
	 */
	private double	r1;
	/**
	 * Wanted travel time between r1 and r2
	 * <p>
	 * <b>Unit: [s]</b>
	 * </p>
	 */
	private double	dT;
	/**
	 * Cached value of log(r1 / r2)
	 * <p>
	 * <b>Unit: [-]</b>
	 * </p>
	 */
	private double	log;
	/**
	 * Cached value of the exposin parameter k2
	 * <p>
	 * <b>Unit: [-]</b>
	 * </p>
	 */
	private double	k2;
	/**
	 * Total traveled angle between r1 and r2
	 * <p>
	 * <b>Unit: [rad]</b>
	 * </p>
	 */
	private double	theta;
	/**
	 * Standard gravitational parameter of the center body.
	 * <p>
	 * <b>Unit: [m<sup>3</sup>/s<sup>2</sup>]</b>
	 * </p>
	 */
	private double	mu_center;

	public ExpoSinSolutionSet(double r1, double r2, double dT, double k2, double theta, double mu_center) {
		this.r1 = r1; // Store stuff
		this.dT = dT;
		this.k2 = k2;
		this.theta = theta;
		this.mu_center = mu_center;
		log = Math.log(r1 / r2); // cache value a s its commenly needed

		/* Compute the domain of gamma [gamma_min, gamma_max] */
		double delta = (2 * (1 - Math.cos(k2 * theta))) / Math.pow(k2, 4) - (log * log);
		double gamma_1_min = Math.atan((k2 / 2)
				* (-log * (1 / Math.tan(k2 * theta / 2)) - Math.sqrt(delta)));
		double gamma_1_max = Math.atan((k2 / 2)
				* (-log * (1 / Math.tan(k2 * theta / 2)) + Math.sqrt(delta)));

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
		double tan_gamma = Math.tan(gamma_1);
		/* Compute the exposin parameters */
		double k1 = getK1(log, tan_gamma, k2, theta, gamma_1);
		double phi = getPhi(tan_gamma, k1, k2);
		double k0 = getK0(r1, k1, phi);
		return new ExponentialSinusoid(k0, k1, k2, 0, phi);
	}

	/**
	 * Compute the function of gamma for which the travel time is the set dT.
	 * 
	 * @return The optimal gamma value
	 * @throws OptimizationException
	 */
	public double getOptimalSolution() throws OptimizationException {
		double gamma = Double.NaN;
		/* Try different solvers to find a root of the f(gamma) = tof - dT */
		try {
			gamma = new UnivariateRealSolverFactoryImpl().newBrentSolver().
					solve(this.add(-dT), domain_gamma_1.lowerBound, domain_gamma_1.upperBound, 0);
		} catch (Exception e) {
		}
		if (Double.isNaN(gamma))
			/* We could not solve the problem to tell the user */
			throw new OptimizationException(new MathException("Could not find root solution"));
		return gamma; // return the optimam gamma value
	}

	public double getThetaMax() {
		return theta;
	}

	/**
	 * Set the wanted travel time between r1 and r2
	 * 
	 * @param dT
	 *            Wanted travel time [s]
	 */
	public void setdT(double dT) {
		this.dT = dT;
	}

	/**
	 * {@inheritDoc} Find the time of flight for a given value of gamma.
	 */
	@Override
	public double value(double gamma) throws FunctionEvaluationException {
		double tan_gamma = Math.tan(gamma);
		/* Compute the exposin parameters */
		double k1 = getK1(log, tan_gamma, k2, theta, gamma);
		double phi = getPhi(tan_gamma, k1, k2);
		double k0 = getK0(r1, k1, phi);
		/* Formulate the tof equation */
		ExpoSinTOF tof = new ExpoSinTOF(k0, k1, k2, phi, theta, mu_center);
		double y = tof.value(gamma); // Return the actual tof value
		return y;
	}

}
