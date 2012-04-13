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

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.LegendreGaussIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;

import be.angelcorp.libs.math.functions.ExponentialSinusoid;

public class ExpoSinTOF implements UnivariateFunction {

	/**
	 * Exposin k1 parameter
	 * <p>
	 * <b>Unit: [-]</b>
	 * </p>
	 */
	protected final double			k1;
	/**
	 * Exposin k2 parameter
	 * <p>
	 * <b>Unit: [-]</b>
	 * </p>
	 */
	protected final double			k2;
	/**
	 * Exposin phi parameter
	 * <p>
	 * <b>Unit: [rad]</b>
	 * </p>
	 */
	protected final double			phi;
	/**
	 * End angle for theta (total rotation that is executed)
	 * <p>
	 * <b>Unit: [rad]</b>
	 * </p>
	 */
	protected final double			theta_f;
	/**
	 * Standard gravitational parameter of the center body.
	 * <p>
	 * <b>Unit: [m<sup>3</sup>/s<sup>2</sup>]</b>
	 * </p>
	 */
	protected final double			mu;

	/**
	 * Basic exponential sinusoid, for which the time of flight can be computed (in function of gamma).
	 * Note this only hold valif information for the same gamma where the exposin parameters where
	 * constructed with.
	 * <p>
	 * <b>Unit: tof(&gamma;) = [s]</b>
	 * </p>
	 */
	protected ExponentialSinusoid	expo;
	/**
	 * Function integrator (used to integrate from theta is 0 to theta_f.
	 */
	private UnivariateIntegrator	integrator;

	/**
	 * Create the time of flight function for a given set of exposin parameters
	 * 
	 * @param k0
	 *            Exposin param
	 * @param k1
	 *            Exposin param
	 * @param k2
	 *            Exposin param
	 * @param phi
	 *            Exposin param
	 * @param theta_final
	 *            Total rotation angle of the exposin
	 * @param mu
	 *            Standard gravitation parameter of the center body
	 */
	public ExpoSinTOF(double k0, double k1, double k2, double phi, double theta_final, double mu) {
		this.k1 = k1;
		this.k2 = k2;
		this.phi = phi;
		this.theta_f = theta_final;
		this.mu = mu;
		/* Make an exposin of the given parameters */
		expo = new ExponentialSinusoid(k0, k1, k2, 0, phi);
		/* Make a new function intgrator */
		integrator = new LegendreGaussIntegrator(5, 1e-12, 1e-12);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Note you should use the same gamma as was used to make the exposin parameters
	 * </p>
	 */
	@Override
	public double value(final double gamma) {
		/* The d(theta)/dt equation */
		UnivariateFunction theta_dot = new UnivariateFunction() {
			@Override
			public double value(double theta) {
				/* d(theta)/dt = sqrt(r^3 (tan^2(g) + k1 k2^2 s +s)/ mu) */
				double r = expo.value(theta);
				double s = Math.sin(k2 * theta + phi);
				double theta_dot = Math.sqrt(
						(Math.pow(r, 3) / mu)
								* (Math.pow(Math.tan(gamma), 2) + k1 * k2 * k2 * s + 1));
				return theta_dot;
			}
		};
		try {
			return integrator.integrate(50, theta_dot, 0, theta_f);
		} catch (Exception e) {
			return Double.POSITIVE_INFINITY; /* wrap the error and deal with it later */
		}
	}
}