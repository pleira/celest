/**
 * Copyright (C) 2010 Simon Billemont <aodtorusan@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.libs.celest.targeters.exposin;

import java.util.List;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MathException;
import org.apache.commons.math.analysis.ComposableFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.optimization.OptimizationException;

import be.angelcorp.libs.math.functions.ExponentialSinusoid;
import be.angelcorp.libs.math.functions.Sign;
import be.angelcorp.libs.math.functions.UnivariateRealSolvers;
import be.angelcorp.libs.math.functions.domain.Domain;

/**
 * Computes the tof for a given exposin problem
 * 
 * 
 * @author simon
 * 
 */
public class ExpoSinSolutionSet extends ComposableFunction {

	private static double getK0(double r1, double k1, double phi) {
		double k0 = r1 * (Math.exp(-k1 * Math.sin(phi)));
		return k0;
	}

	private static double getK1(double log, double tan_gamma, double k2, double theta, double gamma) {
		double signK1 = (log + (tan_gamma / k2) * Math.sin(k2 * theta))
				/ (1 - Math.cos(k2 * theta));
		double k1 = Sign.eval(signK1)
				* Math.sqrt(Math.pow(signK1, 2) + (Math.pow(Math.tan(gamma), 2)) / (k2 * k2));
		return k1;
	}

	private static double getPhi(double tan_gamma, double k1, double k2) {
		double phi = Math.acos(tan_gamma / (k1 * k2));
		return phi;

	}

	private Domain	domain;

	private double	r1;
	private double	dT;
	private double	log;
	private double	k2;
	private double	theta;
	private double	mu_center;

	public ExpoSinSolutionSet(double r1, double r2, double dT, double k2, double theta, double mu_center) {
		this.r1 = r1;
		this.dT = dT;
		this.k2 = k2;
		this.theta = theta;
		this.mu_center = mu_center;
		log = Math.log(r1 / r2);

		double delta = (2 * (1 - Math.cos(k2 * theta))) / Math.pow(k2, 4) - (log * log);
		double gamma_min = Math.atan((k2 / 2)
				* (-log * (1 / Math.tan(k2 * theta / 2)) - Math.sqrt(delta)));
		double gamma_max = Math.atan((k2 / 2)
				* (-log * (1 / Math.tan(k2 * theta / 2)) + Math.sqrt(delta)));

		domain = new Domain(gamma_min, gamma_max);
	}

	public Domain getDomain() {
		return domain;
	}

	public ExponentialSinusoid getExpoSin(double gamma) {
		double tan_gamma = Math.tan(gamma);
		double k1 = getK1(log, tan_gamma, k2, theta, gamma);
		double phi = getPhi(tan_gamma, k1, k2);
		double k0 = getK0(r1, k1, phi);
		return new ExponentialSinusoid(k0, k1, k2, 0, phi);
	}

	public double getOptimalSolution() throws OptimizationException {
		double gamma = Double.NaN;
		List<UnivariateRealSolver> solvers = UnivariateRealSolvers.newInstance().newSolverList();
		for (UnivariateRealSolver solver : solvers) {
			try {
				gamma = solver.solve(this.add(-dT), domain.lowerBound, domain.upperBound);
				break;
			} catch (Exception e) {
			}
		}
		if (gamma == Double.NaN)
			throw new OptimizationException(new MathException("Could not find root solution"));
		return gamma;
	}

	@Override
	public double value(double gamma) throws FunctionEvaluationException {
		double tan_gamma = Math.tan(gamma);
		double k1 = getK1(log, tan_gamma, k2, theta, gamma);
		double phi = getPhi(tan_gamma, k1, k2);
		double k0 = getK0(r1, k1, phi);
		ExpoSinTOF tof = new ExpoSinTOF(k0, k1, k2, phi, theta, mu_center);
		double y = tof.value(gamma);
		return y;

	}
}
