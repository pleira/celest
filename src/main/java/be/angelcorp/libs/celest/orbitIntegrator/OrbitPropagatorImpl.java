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
package be.angelcorp.libs.celest.orbitIntegrator;

import org.apache.commons.math.MathException;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.FirstOrderIntegrator;
import org.apache.commons.math.ode.nonstiff.ClassicalRungeKuttaIntegrator;

import be.angelcorp.libs.celest.eom.IStateDerivatives;
import be.angelcorp.libs.celest.math.Cartesian;
import be.angelcorp.libs.celest.stateVector.CartesianDerivative;
import be.angelcorp.libs.celest.stateVector.CartesianElements;
import be.angelcorp.libs.celest.stateVector.ICartesianElements;

public class OrbitPropagatorImpl implements OrbitPropagator, Cartesian {

	protected FirstOrderIntegrator	integrator;
	private double					tEnd;

	public OrbitPropagatorImpl(double stepSize) {
		this.integrator = new ClassicalRungeKuttaIntegrator(stepSize);
	}

	public OrbitPropagatorImpl(FirstOrderIntegrator integrator) {
		this.integrator = integrator;
	}

	public double getEndTime() {
		return tEnd;
	}

	public ICartesianElements integrate(final IStateDerivatives dState,
			double t0, double t) throws MathException {
		tEnd = Double.NaN;
		/* Wrap the accelleration in ode for for the integrator */
		FirstOrderDifferentialEquations eqn = new FirstOrderDifferentialEquations() {
			@Override
			public void computeDerivatives(double t, double[] y, double[] yDot)
					throws DerivativeException {
				/* point: ____ [rx, ry, rz, vx, vy, vz] */
				/* derivative: [vx, vy, vz, ax, ay, az], note no t derivative */
				CartesianDerivative derivs = dState.getDerivatives(t).toCartesianDerivative();
				System.arraycopy(derivs.toVector().toArray(), 0, yDot, 0, 6);
				// RealVector aVector = accelleration.value(new Vector3D(y[0], y[1], y[2]));
				// double[] a = aVector.getData();
				// yDot[0] = y[3];
				// yDot[1] = y[4];
				// yDot[2] = y[5];
				// yDot[3] = a[0];
				// yDot[4] = a[1];
				// yDot[5] = a[2];
			}

			@Override
			public int getDimension() {
				return 6;
			}
		};

		double[] y = new double[6]; // End state container
		/* Integrate the orbit with the given equations and conditions */
		ICartesianElements startState = dState.getBody().getState().toCartesianElements();
		tEnd = integrator.integrate(eqn, t0, startState.toVector().getData(), t, y);

		/* Add a check to see if tEnd +-= t ? */

		/* Wrap the result in a fancy format and return */
		return new CartesianElements(y);
	}

}
