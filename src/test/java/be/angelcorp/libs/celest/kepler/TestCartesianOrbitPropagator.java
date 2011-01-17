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
package be.angelcorp.libs.celest.kepler;

import junit.framework.TestCase;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ode.AbstractIntegrator;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math.ode.sampling.StepHandler;
import org.apache.commons.math.ode.sampling.StepInterpolator;

import be.angelcorp.libs.celest.constants.EarthConstants;
import be.angelcorp.libs.celest.eom.TwoBodyCartesian;
import be.angelcorp.libs.celest.orbitIntegrator.CartesianOrbitPropagator;
import be.angelcorp.libs.celest.stateVector.CartesianElements;
import be.angelcorp.libs.celest.stateVector.KeplerElements;
import be.angelcorp.libs.util.exceptions.GenericRuntimeException;

public class TestCartesianOrbitPropagator extends TestCase {

	public static final double	delta	= 1E-16;

	/**
	 * Tests the propagation of geo satellite over one month. The end radius must be within 2m of the
	 * start geo radius (0.6m should be achieved)
	 * 
	 * @throws FunctionEvaluationException
	 */
	public void testRK4integrationTestGeo() throws Exception {
		CartesianOrbitPropagator integrator = new CartesianOrbitPropagator(60);
		/* Geostationairy start position */
		CartesianElements x0 = new CartesianElements(new double[] { 42164000, 0, 0, 0, 3074.6663, 0 });

		CartesianElements ans2 = integrator.integrate(TwoBodyCartesian.ZERO, 0, x0, 30 * 24 * 360);

		// System.out.println(x0.getSubVector(0, 3).getNorm());
		// System.out.println(ans2.getSubVector(0, 3).getNorm());
		// System.out.println(x0.getSubVector(0, 3).getNorm() - ans2.getSubVector(0, 3).getNorm());

		// assertEquals(x0.getSubVector(0, 3).getNorm(),
		// ans2.getSubVector(0, 3).getNorm(), 2);
	}

	public void testRK4integrationTestLeo() throws Exception {
		AbstractIntegrator rk4 = new ClassicalRungeKuttaIntegrator(1);
		StepHandler stepHandler = new StepHandler() {
			@Override
			public void handleStep(StepInterpolator interpolator, boolean isLast)
					throws DerivativeException {
				double t = interpolator.getCurrentTime();
				double[] y = interpolator.getInterpolatedState();
				if (Math.abs(t - 2) < delta) {
					double[] step1True = new double[] { 6640305.22, 16251.75, 0, -18.08, 8125.86, 0 };
					for (int i = 0; i < 6; i++)
						assertEquals(String.format(
								"Integration step 1, element %d is not correct (expected: was:",
								i, step1True[i], y[i]),
								step1True[i], y[i], delta);
				} else if (Math.abs(t - 4) < delta) {

				} else {
					throw new GenericRuntimeException(
							"Errr, time steps should either be 2 or 4 but t = %d"
									+ "(integrate from 0 to 4 with steps of 2)",
							t);
				}
			}

			@Override
			public boolean requiresDenseOutput() {
				return false;
			}

			@Override
			public void reset() {
			}

		};
		CartesianOrbitPropagator integrator = new CartesianOrbitPropagator(rk4);

		/* Leo orbit */
		KeplerElements k = new KeplerElements(7378137, 0.1, 0, 0, 0, 0);
		integrator.integrate(TwoBodyCartesian.ZERO, 0d,
				k.getOrbitEqn().kepler2cartesian(EarthConstants.mu), 4);
	}
}
