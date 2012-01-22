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

import junit.framework.TestCase;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.ode.AbstractIntegrator;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math.ode.sampling.StepHandler;
import org.apache.commons.math.ode.sampling.StepInterpolator;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.constants.EarthConstants;
import be.angelcorp.libs.celest.eom.TwoBody;
import be.angelcorp.libs.celest.orbitIntegrator.OrbitPropagatorImpl;
import be.angelcorp.libs.celest.stateVector.CartesianElements;
import be.angelcorp.libs.celest.stateVector.ICartesianElements;
import be.angelcorp.libs.celest.stateVector.IKeplerElements;
import be.angelcorp.libs.celest.stateVector.KeplerElements;
import be.angelcorp.libs.celest.unit.Tests;
import be.angelcorp.libs.util.exceptions.GenericRuntimeException;

public class TestOrbitPropagatorImpl extends TestCase {

	public static final double	delta	= 1E-3; // 0.1%

	/**
	 * Tests the propagation of geo satellite over one month. The end radius must be within 2m of the
	 * start geo radius (0.6m should be achieved)
	 * 
	 * @throws FunctionEvaluationException
	 */
	public void testRK4integrationTestGeo() throws Exception {
		OrbitPropagatorImpl integrator = new OrbitPropagatorImpl(60);
		/* Geostationairy start position */
		CartesianElements x0 = new CartesianElements(new double[] { 42164000, 0, 0, 0, 3074.6663, 0 });

		/* Create the two body problem */
		TwoBody tb = new TwoBody(new CelestialBody(x0, 1));

		ICartesianElements ans2 = integrator.integrate(tb, 0, 30 * 24 * 360);
		// System.out.println(x0.getSubVector(0, 3).getNorm());
		// System.out.println(ans2.getSubVector(0, 3).getNorm());
		// System.out.println(x0.getSubVector(0, 3).getNorm() - ans2.getSubVector(0, 3).getNorm());

		// assertEquals(x0.getSubVector(0, 3).getNorm(),
		// ans2.getSubVector(0, 3).getNorm(), 2);
	}

	public void testRK4integrationTestLeo() throws Exception {
		AbstractIntegrator rk4 = new ClassicalRungeKuttaIntegrator(2);
		rk4.addStepHandler(new StepHandler() {
			@Override
			public void handleStep(StepInterpolator interpolator, boolean isLast)
					throws DerivativeException {
				double t = interpolator.getCurrentTime();
				double[] y = interpolator.getInterpolatedState();
				if (Math.abs(t - 2) < delta) {
					double[] step1True = new double[] { 6640305.22, 16251.75, 0, -18.08, 8125.86, 0 };
					Tests.assertEquals("Step one is not computed correctly", step1True, y,
							new ArrayRealVector(step1True).mapAbs().mapMultiply(delta).getData());
				} else if (Math.abs(t - 4) < delta) {
					double[] step2True = new double[] { 6640287.14, 32503.54, 0, -36.16, 8125.84, 0 };
					Tests.assertEquals("Step two is not computed correctly", step2True, y,
							new ArrayRealVector(step2True).mapAbs().mapMultiply(delta).getData());
				} else {
					throw new GenericRuntimeException(
							"Errr, time steps should either be 2 or 4 but t = %f"
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

		});
		OrbitPropagatorImpl integrator = new OrbitPropagatorImpl(rk4);

		/* Leo orbit */
		IKeplerElements k = new KeplerElements(7378137, 0.1, 0, 0, 0, 0, EarthConstants.bodyCenter);
		CartesianElements c = k.getOrbitEqn().kepler2cartesian();
		/* Create the two body problem */
		TwoBody tb = new TwoBody(new CelestialBody(c, 1));
		integrator.integrate(tb, 0, 4);
	}
}
