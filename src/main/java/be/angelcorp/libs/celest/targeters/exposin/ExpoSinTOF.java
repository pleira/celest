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

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.ComposableFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.integration.LegendreGaussIntegrator;
import org.apache.commons.math.analysis.integration.UnivariateRealIntegrator;

import be.angelcorp.libs.math.functions.ExponentialSinusoid;

public class ExpoSinTOF extends ComposableFunction {

	/**
	 * Exposin k1 parameter
	 * <p>
	 * <b>Unit: [-]</b>
	 * </p>
	 */
	protected final double				k1;
	/**
	 * Exposin k2 parameter
	 * <p>
	 * <b>Unit: [-]</b>
	 * </p>
	 */
	protected final double				k2;
	/**
	 * Exposin phi parameter
	 * <p>
	 * <b>Unit: [rad]</b>
	 * </p>
	 */
	protected final double				phi;
	protected final double				theta_f;
	/**
	 * Standard gravitational parameter of the certer body.
	 * <p>
	 * <b>Unit: [m<sup>3</sup>/s<sup>2</sup>]</b>
	 * </p>
	 */
	protected final double				mu;

	protected ExponentialSinusoid		expo;
	private UnivariateRealIntegrator	integrator;

	public ExpoSinTOF(double k0, double k1, double k2, double phi, double theta_final, double mu) {
		this.k1 = k1;
		this.k2 = k2;
		this.phi = phi;
		this.theta_f = theta_final;
		this.mu = mu;
		expo = new ExponentialSinusoid(k0, k1, k2, 0, phi);
		integrator = new LegendreGaussIntegrator(5, 100);
	}

	@Override
	public double value(final double gamma) throws FunctionEvaluationException {
		UnivariateRealFunction theta_dot = new UnivariateRealFunction() {
			@Override
			public double value(double theta) throws FunctionEvaluationException {
				double r = expo.value(theta);
				double s = Math.sin(k2 * theta + phi);
				double theta_dot = Math.sqrt(
							(Math.pow(r, 3) / mu)
									* (Math.pow(Math.tan(gamma), 2) + k1 * k2 * k2 * s + 1));
				return theta_dot;
			}
		};
		try {
			return integrator.integrate(theta_dot, 0, theta_f);
		} catch (Exception e) {
			return Double.NaN;
		}
	}
}