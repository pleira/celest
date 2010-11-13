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
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealVector;

import be.angelcorp.libs.celest.eom.TwoBodyCartesian;
import be.angelcorp.libs.celest.orbitIntegrator.CartRK4Propagator;

public class TestCartesianRK4 extends TestCase {

	/**
	 * Tests the propagation of geo satellite over one month. The end radius must be within 2m of the
	 * start geo radius (0.6m should be achieved)
	 * 
	 * @throws FunctionEvaluationException
	 */
	public void testRK4int() throws FunctionEvaluationException {
		CartRK4Propagator integrator = new CartRK4Propagator(TwoBodyCartesian.ZERO);
		RealVector x0 = new ArrayRealVector(new double[] { 42164000, 0, 0, 0, 3074.6663, 0 }); // Geostationairy

		integrator.setStepSize(1 * 60);
		RealVector ans2 = integrator.integrate(0, 30 * 24 * 360, x0);

		// System.out.println(x0.getSubVector(0, 3).getNorm());
		// System.out.println(ans2.getSubVector(0, 3).getNorm());
		// System.out.println(x0.getSubVector(0, 3).getNorm() - ans2.getSubVector(0, 3).getNorm());

		assertEquals(x0.getSubVector(0, 3).getNorm(),
				ans2.getSubVector(0, 3).getNorm(), 2);
	}

}
