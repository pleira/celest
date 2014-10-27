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
package be.angelcorp.celest.math.functions;

import static org.apache.commons.math3.util.FastMath.cos;
import static org.apache.commons.math3.util.FastMath.exp;
import static org.apache.commons.math3.util.FastMath.sin;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;

/**
 * Exponential sinusoid function defined by;
 * 
 * <pre>
 * r(&theta;) = k0 exp( q0 &theta; + k1 sin(k2 &theta; + &phi; ) )
 * r&#775;(&theta;) = k0 exp( q0 &theta; + k1 sin(k2 &theta; + &phi; ) ) (q0 + k1 k2 cos( k2 &theta; + &phi;) )
 * </pre>
 * 
 * @author Simon Billemont
 * 
 */
public class ExponentialSinusoid implements DifferentiableUnivariateFunction {

	/**
	 * First derivative of the exponential sinusoid function:
	 * 
	 * <pre>
	 * r&#775;(&theta;) = k0 exp( q0 &theta; + k1 sin(k2 &theta; + &phi; ) ) (q0 + k1 k2 cos( k2 &theta; + &phi;) )
	 * </pre>
	 * 
	 * @author Simon Billemont
	 * @see {@link be.angelcorp.celest.math.functions.ExponentialSinusoid}
	 * 
	 */
	public class ExponentialSinusoid1stDerivative implements UnivariateFunction {
		@Override
		public double value(double theta) {
			return exp(k1 * sin(k2 * theta + phi)) * k0 * (q0 + k1 * k2 * cos(k2 * theta + phi));
		}
	}

	/**
	 * Scaling factor constant
	 * <p>
	 * <b>Unit: Determines output unit</b>
	 * </p>
	 */
	private final double	k0;
	/**
	 * Dynamic range parameter
	 * <p>
	 * <b>Unit: [-]</b>
	 * </p>
	 */
	private final double	k1;
	/**
	 * Winding parameter
	 * <p>
	 * <b>Unit: [-]</b>
	 * </p>
	 */
	private final double	k2;
	/**
	 * Constant
	 * <p>
	 * <b>Unit: [-]</b>
	 * </p>
	 */
	private final double	q0;
	/**
	 * Phase angle
	 * <p>
	 * <b>Unit: [rad]</b>
	 * </p>
	 */
	private final double	phi;

	/**
	 * @param k0
	 *            scaling factor [m]
	 * @param k1
	 *            dynamic range parameter [-]
	 * @param k2
	 *            winding parameter [-]
	 * @param q0
	 *            constant [rad-1]
	 * @param phi
	 *            phase angle [rad]
	 */
	public ExponentialSinusoid(double k0, double k1, double k2, double q0, double phi) {
		this.k0 = k0;
		this.k1 = k1;
		this.k2 = k2;
		this.q0 = q0;
		this.phi = phi;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UnivariateFunction derivative() {
		return new ExponentialSinusoid1stDerivative();
	}

	/**
	 * Get the scaling factor constant
	 * 
	 * @see be.angelcorp.celest.math.functions.ExponentialSinusoid#k0
	 */
	public double getK0() {
		return k0;
	}

	/**
	 * Get the dynamic range parameter
	 * 
	 * @see be.angelcorp.celest.math.functions.ExponentialSinusoid#k1
	 */
	public double getK1() {
		return k1;
	}

	/**
	 * Get the winding parameter
	 * 
	 * @see be.angelcorp.celest.math.functions.ExponentialSinusoid#k2
	 */
	public double getK2() {
		return k2;
	}

	/**
	 * Get the phase angle
	 * 
	 * @see be.angelcorp.celest.math.functions.ExponentialSinusoid#phi
	 */
	public double getPhi() {
		return phi;
	}

	/**
	 * Get the constant q0
	 * 
	 * @see be.angelcorp.celest.math.functions.ExponentialSinusoid#q0
	 */
	public double getQ0() {
		return q0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("ExponentialSinusoid {k0=%f, k1=%f, k2=%f, q0=%f, phi=%f}", k0, k1, k2, q0, phi);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double value(double theta) {
		double r = k0 * exp(q0 * theta + k1 * sin(k2 * theta + phi));
		return r;
	}

}
