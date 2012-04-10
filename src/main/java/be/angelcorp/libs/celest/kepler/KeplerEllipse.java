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
package be.angelcorp.libs.celest.kepler;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;

import be.angelcorp.libs.celest.state.positionState.IKeplerElements;
import be.angelcorp.libs.math.MathUtils2;

public class KeplerEllipse extends KeplerEquations {

	/**
	 * Solves for eccentric anomaly, E, from a given mean anomaly, M, and eccentricity, ecc. Performs a
	 * simple Newton-Raphson iteration
	 * 
	 * @param M
	 *            Mean anomaly
	 * @param ecc
	 *            Eccentricity
	 * @return The eccentric anomaly
	 */
	public static double eccentricAnomaly(double M, double ecc) {
		double E;
		M = MathUtils2.mod(M, 2 * PI);
		if ((M > -Math.PI && M < 0) || M > Math.PI)
			E = M - ecc;
		else
			E = M + ecc;

		double Etemp = E + (M - E + ecc * Math.sin(E)) / (1 - ecc * Math.cos(E));

		while (Math.abs(Etemp - E) > anomalyIterationTol) {
			Etemp = E;
			E = Etemp + (M - Etemp + ecc * Math.sin(Etemp)) / (1 - ecc * Math.cos(Etemp));
		}
		return E;
	}

	/**
	 * Compute the eccentric anomaly E from the true anomaly &nu;.
	 * 
	 * @param nu
	 *            True anomaly [rad]
	 * @param e
	 *            Eccentricity [-]
	 * @return Eccentric anomaly [rad]
	 */
	public static double eccentricAnomalyFromTrue(double nu, double e) {
		return Math.atan2(Math.sqrt(1 - e * e) * Math.sin(nu), e + Math.cos(nu));
	}

	public KeplerEllipse(IKeplerElements k) {
		super(k);
	}

	@Override
	public double anomaly() {
		return anomalyFromTrueAnomaly(k.getTrueAnomaly());
	}

	@Override
	public double anomalyFromMeanAnomaly(double M) {
		double E = eccentricAnomaly(M, k.getEccentricity());
		return E;
	}

	@Override
	public double anomalyFromTrueAnomaly(double nu) {
		return eccentricAnomalyFromTrue(nu, k.getEccentricity());
	}

	@Override
	public double arealVel(double mu, double a, double e) {
		return Math.sqrt(a * mu * (1 - e * e)) / 2;
	}

	/**
	 * Solves for eccentric anomaly, E, from a given mean anomaly, M, and eccentricity, ecc. Performs a
	 * simple Newton-Raphson iteration
	 * 
	 * @return The eccentric anomaly
	 */
	public double eccentricAnomaly() {
		return eccentricAnomaly(meanAnomalyFromTrue(k.getTrueAnomaly()), k.getEccentricity());
	}

	@Override
	public double getApocenter() {
		return k.getSemiMajorAxis() * (1 + k.getEccentricity());
	}

	@Override
	protected DifferentiableUnivariateRealFunction getFundamentalEquation(final double e, final double M) {
		return new DifferentiableUnivariateRealFunction() {
			@Override
			public UnivariateRealFunction derivative() {
				return new UnivariateRealFunction() {
					@Override
					public double value(double H) throws FunctionEvaluationException {
						return 1 - e * cos(H);
					}
				};
			}

			@Override
			public double value(double E) throws FunctionEvaluationException {
				return E - e * sin(E) - M;
			}
		};
	}

	@Override
	public double getPericenter() {
		return k.getSemiMajorAxis() * (1 - k.getEccentricity());
	}

	@Override
	public double meanAnomalyFromAnomaly(double E) {
		final double M = E - k.getEccentricity() * sin(E);
		return M;
	}

	@Override
	public double meanAnomalyFromTrue(double nu) {
		final double E = anomalyFromTrueAnomaly(nu);
		return meanAnomalyFromAnomaly(E);
	}

	@Override
	public double period(double n) {
		return 2 * Math.PI / n;
	}

	public double periodMu(double mu) {
		return period(meanMotion(mu, k.getSemiMajorAxis()));
	}

	@Override
	public double semiLatusRectum() {
		final double e = k.getEccentricity();
		return k.getSemiMajorAxis() * (1 - e * e);
	}

	@Override
	public double totEnergyPerMass(double mu, double a) {
		return -mu / (2 * a);
	}

	@Override
	public double trueAnomalyFromAnomaly(double E) {
		final double e = k.getEccentricity();
		final double nu = atan2(sin(E) * sqrt(1 - e * e), cos(E) - e);
		return nu;
	}

}
