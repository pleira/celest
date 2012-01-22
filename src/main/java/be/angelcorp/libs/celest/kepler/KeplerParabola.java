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
import static java.lang.Math.atan2;
import static java.lang.Math.pow;

import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory;

import be.angelcorp.libs.celest.stateVector.IKeplerElements;
import be.angelcorp.libs.math.MathUtils2;

public class KeplerParabola extends KeplerEquations {

	public static double radius(double p, double B) {
		final double r = (p / 2) * (1 + B * B);
		return r;
	}

	public KeplerParabola(IKeplerElements k) {
		super(k);
	}

	@Override
	public double anomalyFromMeanAnomaly(final double M) {
		UnivariateRealFunction f = new DifferentiableUnivariateRealFunction() {

			@Override
			public UnivariateRealFunction derivative() {
				return new UnivariateRealFunction() {
					@Override
					public double value(double B) {
						return 1 + 1 * B * B;
					}
				};
			}

			@Override
			public double value(double B) {
				return B + pow(B, 3) / 3. - M;
			}
		};

		UnivariateRealSolver solver = UnivariateRealSolverFactory.newInstance().newNewtonSolver();
		try {
			final double B = solver.solve(f, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
			return B;
		} catch (Exception e) {
			throw new MathRuntimeException(e);
		}
	}

	@Override
	public double anomalyFromTrueAnomaly(double nu) {
		nu = MathUtils2.mod(nu, 2 * PI);

		if (nu > PI)
			nu = nu - 2 * PI;

		final double B = StrictMath.tan(nu / 2);
		return B;
	}

	@Override
	public double arealVel(double mu, double a, double e) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public double flightPathAngle() {
		final double gamma = k.getTrueAnomaly() / 2;
		return gamma;
	}

	@Override
	public double getApocenter() {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	protected DifferentiableUnivariateRealFunction getFundamentalEquation(double e, double M) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public double getPericenter() {
		return semiLatusRectum() / 2;
	}

	@Override
	public double meanAnomalyFromAnomaly(double anomaly) {
		final double M = anomaly + (anomaly * anomaly * anomaly) / 3.0;
		return M;
	}

	@Override
	public double meanAnomalyFromTrue(double nu) {
		return meanAnomalyFromAnomaly(anomaly());
	}

	@Override
	public double period(double n) {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public double semiLatusRectum() {
		return k.getSemiMajorAxis();// TODO: better solution ?
	}

	@Override
	public double totEnergyPerMass(double mu, double a) {
		return 0;
	}

	@Override
	public double trueAnomalyFromAnomaly(double B) {
		final double p = semiLatusRectum();
		final double r = radius(p, B);
		final double nu = atan2(p * B, (p - r));
		return nu;
	}

	@Override
	public double visViva(double r) {
		return 2 * k.getCenterbody().getMu() / r;
	}

}
