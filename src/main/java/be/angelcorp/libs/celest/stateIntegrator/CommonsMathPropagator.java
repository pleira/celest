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
package be.angelcorp.libs.celest.stateIntegrator;

import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.FirstOrderIntegrator;

import be.angelcorp.libs.celest.math.Cartesian;
import be.angelcorp.libs.celest.state.IState;
import be.angelcorp.libs.celest.state.IStateDerivative;
import be.angelcorp.libs.celest.state.IStateEquation;
import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.celest.time.JulianDate;

public class CommonsMathPropagator<Y extends IState, DY extends IStateDerivative>
		implements IStateIntegrator<Y, DY>, Cartesian {

	private class StateEquationsWrapper implements FirstOrderDifferentialEquations {

		@Override
		public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
			Y yState = equations.createState(new ArrayRealVector(y, false));
			DY dyState = equations.calculateDerivatives(new JulianDate(t), yState);

			RealVector dyVector = dyState.toVector();
			if (dyVector instanceof ArrayRealVector) {
				System.arraycopy(((ArrayRealVector) dyVector).getDataRef(), 0, yDot, 0, dyVector.getDimension());
			} else {
				System.arraycopy(dyVector.getData(), 0, yDot, 0, dyVector.getDimension());
			}
		}

		@Override
		public int getDimension() {
			return equations.getDimension();
		}

	}

	protected FirstOrderIntegrator			integrator;
	protected final IStateEquation<Y, DY>	equations;

	public CommonsMathPropagator(FirstOrderIntegrator integrator, IStateEquation<Y, DY> equations) {
		this.integrator = integrator;
		this.equations = equations;
	}

	@Override
	public Y integrate(IJulianDate t0, IJulianDate t, Y y0) {
		// Wrap the types to commons math compatible types
		FirstOrderDifferentialEquations cm_equations = new StateEquationsWrapper();
		double[] cm_y0 = y0.toVector().toArray();
		double cm_t0 = t0.getJD();
		double cm_t = t.getJD();

		// Propagate the orbit
		double[] cm_y = new double[cm_equations.getDimension()];
		try {
			double cm_t_end = integrator.integrate(cm_equations, cm_t0, cm_y0, cm_t, cm_y);
		} catch (Exception e) {
			throw new RuntimeException(e); // TODO: remove temp code
		}

		// Unwrap to libs.celst types
		Y y_end = equations.createState(new ArrayRealVector(cm_y, false));
		return y_end;
	}
}
