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

import be.angelcorp.libs.celest.body.bodyCollection.TwoBodyCollection;
import be.angelcorp.libs.celest.constants.EarthConstants;
import be.angelcorp.libs.celest.universe.DefaultUniverse;
import be.angelcorp.libs.celest.universe.Universe;
import org.apache.commons.math3.analysis.function.Abs;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.eom.TwoBody;
import be.angelcorp.libs.celest.state.positionState.CartesianElements;
import be.angelcorp.libs.celest.state.positionState.ICartesianDerivative;
import be.angelcorp.libs.celest.state.positionState.ICartesianElements;
import be.angelcorp.libs.celest.state.positionState.IKeplerElements;
import be.angelcorp.libs.celest.state.positionState.KeplerElements;
import be.angelcorp.libs.celest.stateIntegrator.CommonsMathPropagator;
import be.angelcorp.libs.celest.time.JulianDate;
import be.angelcorp.libs.celest.unit.CelestTest;
import be.angelcorp.libs.util.exceptions.GenericRuntimeException;
import be.angelcorp.libs.util.physics.Time;

public class TestOrbitPropagatorImpl extends CelestTest {

	public static final double	delta	= 1E-3; // 0.1%

    public static Universe universe = new DefaultUniverse();
    public static CelestialBody earth = universe.earthConstants().bodyCenter();

	/**
	 * Tests the propagation of geo satellite over one month. The end radius must be within 2m of the
	 * start geo radius (0.6m should be achieved)
	 */
	public void testRK4integrationTestGeo() throws Exception {

		/* Geostationairy start position */
		CartesianElements x0 = new CartesianElements(new double[] { 42164000, 0, 0, 0, 3074.6663, 0 });

		/* Create the two body problem */
        CelestialBody b = new CelestialBody(x0, 1);
		TwoBody tb = new TwoBody( new TwoBodyCollection( b, earth ), b );

		FirstOrderIntegrator integrator = new ClassicalRungeKuttaIntegrator(60.);
		CommonsMathPropagator<ICartesianElements, ICartesianDerivative> propagator =
				new CommonsMathPropagator<>(integrator, tb, universe);

		ICartesianElements ans2 = propagator.integrate(
                universe.J2000_EPOCH(), universe.J2000_EPOCH().add(1, Time.month), x0);
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
			public void handleStep(StepInterpolator interpolator, boolean isLast) {
				double t = interpolator.getCurrentTime() - universe.J2000_EPOCH().getJD();
				double[] y = interpolator.getInterpolatedState();
				if (Math.abs(t - 2) < delta) {
					double[] step1True = new double[] { 6640305.22, 16251.75, 0, -18.08, 8125.86, 0 };
					CelestTest.assertEquals("Step one is not computed correctly", step1True, y,
							new ArrayRealVector(step1True).map(new Abs()).mapMultiply(delta).toArray());
				} else if (Math.abs(t - 4) < delta) {
					double[] step2True = new double[] { 6640287.14, 32503.54, 0, -36.16, 8125.84, 0 };
					CelestTest.assertEquals("Step two is not computed correctly", step2True, y,
							new ArrayRealVector(step2True).map(new Abs()).mapMultiply(delta).toArray());
				} else {
					throw new GenericRuntimeException(
							"Errr, time steps should either be 2 or 4 but t = %f"
									+ "(integrate from 0 to 4 with steps of 2)",
							t);
				}
			}

			@Override
			public void init(double t0, double[] y0, double t) {
			}

		});

		/* Leo orbit */
		IKeplerElements k = new KeplerElements(7378137, 0.1, 0, 0, 0, 0, earth);
		CartesianElements c = k.getOrbitEqn().kepler2cartesian();
		/* Create the two body problem */
        CelestialBody b = new CelestialBody(c, 1);
		TwoBody tb = new TwoBody( new TwoBodyCollection(b, earth), b );

		CommonsMathPropagator<ICartesianElements, ICartesianDerivative> integrator =
				new CommonsMathPropagator<>(rk4, tb, universe);
		integrator.integrate(universe.J2000_EPOCH(), universe.J2000_EPOCH().add(4, Time.day), c);
	}
}
