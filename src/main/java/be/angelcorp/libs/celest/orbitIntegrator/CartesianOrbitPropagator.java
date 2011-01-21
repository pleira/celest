/**
 * Copyright (C) 2011 Simon Billemont <aodtorusan@gmail.com>
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 3.0 Unported
 * (CC BY-NC 3.0) (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *        http://creativecommons.org/licenses/by-nc/3.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.libs.celest.orbitIntegrator;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MathException;
import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.FirstOrderIntegrator;
import org.apache.commons.math.ode.nonstiff.ClassicalRungeKuttaIntegrator;

import be.angelcorp.libs.celest.math.Cartesian;
import be.angelcorp.libs.celest.math.CartesianMultivariateVectorFunction;
import be.angelcorp.libs.celest.stateVector.CartesianElements;
import be.angelcorp.libs.math.linear.Vector3D;

public class CartesianOrbitPropagator implements OrbitPropagator, Cartesian {

	private FirstOrderIntegrator	integrator;

	public CartesianOrbitPropagator(double stepSize) {
		this.integrator = new ClassicalRungeKuttaIntegrator(stepSize);
	}

	public CartesianOrbitPropagator(FirstOrderIntegrator integrator) {
		this.integrator = integrator;
	}

	public CartesianElements integrate(final CartesianMultivariateVectorFunction accelleration,
			double t0, CartesianElements y0, double t) throws MathException {
		/* Wrap the accelleration in ode for for the integrator */
		FirstOrderDifferentialEquations eqn = new FirstOrderDifferentialEquations() {
			@Override
			public void computeDerivatives(double t, double[] y, double[] yDot)
					throws DerivativeException {
				/* point: ____ [rx, ry, rz, vx, vy, vz] */
				/* derivative: [vx, vy, vz, ax, ay, az], note no t derivative */
				try {
					RealVector aVector = accelleration.value(new Vector3D(y[0], y[1], y[2]));
					double[] a = aVector.getData();
					yDot[0] = y[3];
					yDot[1] = y[4];
					yDot[2] = y[5];
					yDot[3] = a[0];
					yDot[4] = a[1];
					yDot[5] = a[2];
				} catch (FunctionEvaluationException e) {
					throw new DerivativeException(e);
				}
			}

			@Override
			public int getDimension() {
				return 6;
			}
		};

		double[] y = new double[6]; // End state container
		/* Integrate the orbit with the given equations and conditions */
		double tEnd = integrator.integrate(eqn, t0, y0.toVector().getData(), t, y);

		/* Add a check to see if tEnd +-= t ? */

		/* Wrap the result in a fancy format and return */
		return new CartesianElements(y);
	}
}
