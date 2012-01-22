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
package be.angelcorp.libs.celest.kepler;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.cosh;
import static java.lang.Math.sin;
import static java.lang.Math.sinh;
import static java.lang.Math.sqrt;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.util.FastMath;

import be.angelcorp.libs.celest.stateVector.IKeplerElements;

public class KeplerHyperbola extends KeplerEquations {

	public static double anomalyFromTrueAnomaly(double nu, double e) {
		// acos(-1 / e) == half defection angle
		if (abs(nu) < acos(-1 / e)) {
			double sine = (sin(nu) * sqrt(e * e - 1.)) / (1 + e * cos(nu));
			double H = FastMath.asinh(sine);
			return H;
		} else {
			return Double.NaN;
		}
	}

	public static double hyperbolicAnomalyFromMean(double M, final double e) {
		/* Generate an initial guess */
		double H;
		if (e < 1.6) {
			if (M > PI)
				H = M - e;
			else
				H = M + e;
		} else {
			if (e < 3.6 && M > PI)
				H = M - e;
			else
				H = M / (e - 1);
		}

		/* Iterate until H is accurate enough */
		double H0;
		do {
			H0 = H;
			H = H + (M - e * sinh(H) + H) / (e * cosh(H) - 1);
		} while (abs(H - H0) < anomalyIterationTol);

		return H;
	}

	public static double meanAnomalyFromAnomaly(double H, double e) {
		double M = e * sinh(H) - H;
		return M;
	}

	public static double trueAnomalyFromAnomaly(double H, double e) {
		final double nu = atan2(sqrt(e * e - 1) * sinh(H), e - cosh(H));
		return nu;
	}

	public KeplerHyperbola(IKeplerElements k) {
		super(k);
	}

	@Override
	public double anomalyFromMeanAnomaly(double M) {
		return hyperbolicAnomalyFromMean(M, k.getEccentricity());
	}

	@Override
	public double anomalyFromTrueAnomaly(double nu) {
		return anomalyFromTrueAnomaly(nu, k.getEccentricity());
	}

	@Override
	public double arealVel(double mu, double a, double e) {
		return Math.sqrt(a * mu * (1 - e * e)) / 2;
	}

	@Override
	public double getApocenter() {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	protected DifferentiableUnivariateRealFunction getFundamentalEquation(final double e, final double M) {
		return new DifferentiableUnivariateRealFunction() {
			@Override
			public UnivariateRealFunction derivative() {
				return new UnivariateRealFunction() {
					@Override
					public double value(double H) throws FunctionEvaluationException {
						return e * cosh(H) - 1;
					}
				};
			}

			@Override
			public double value(double H) throws FunctionEvaluationException {
				return e * sinh(H) - H - M;
			}
		};
	}

	@Override
	public double getPericenter() {
		return k.getSemiMajorAxis() * (1 - k.getEccentricity());
	}

	@Override
	public double meanAnomalyFromAnomaly(double H) {
		return meanAnomalyFromAnomaly(H, k.getEccentricity());
	}

	@Override
	public double meanAnomalyFromTrue(double nu) {
		double H = anomalyFromTrueAnomaly(nu);
		return meanAnomalyFromAnomaly(H);
	}

	@Override
	public double period(double n) {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public double semiLatusRectum() {
		return k.getSemiMajorAxis() * (1 - k.getEccentricity() * k.getEccentricity());
	}

	@Override
	public double totEnergyPerMass(double mu, double a) {
		return mu / (2 * a);
	}

	@Override
	public double trueAnomalyFromAnomaly(double H) {
		return trueAnomalyFromAnomaly(H, k.getEccentricity());
	}

}
