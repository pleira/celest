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
package be.angelcorp.libs.celest.stateVector;

import static java.lang.Math.PI;

import java.util.Set;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.constants.EarthConstants;
import be.angelcorp.libs.celest.kepler.KeplerCircular;
import be.angelcorp.libs.celest.kepler.KeplerEquations;
import be.angelcorp.libs.celest.unit.Tests;
import be.angelcorp.libs.math.linear.Vectors;

import com.google.common.collect.Sets;

public class TestSphericalElements extends TestStateVector<SphericalElements> {

	private Set<SphericalElements>	testOrbits;
	private CelestialBody			earth	= EarthConstants.bodyCenter;

	@Override
	public Set<SphericalElements> getTestStateVectors() {
		testOrbits = Sets.newLinkedHashSet();
		/* Classical geo orbit */
		double rGEO = Math.pow(earth.getMu() / Math.pow((2. * PI) / (3600. * 24.), 2), 1. / 3.);
		testOrbits.add(
				new SphericalElements(rGEO, 0, 0, KeplerCircular.Vc(rGEO, earth.getMu()), 0, 0, earth));

		return testOrbits;
	}

	@Override
	public void testToFromCartesianElements(SphericalElements state) {
		ICartesianElements cart = state.toCartesianElements();

		// Check sphere vs sphere => cart => sphere
		SphericalElements s = SphericalElements.as(cart, earth);
		double angleTol = KeplerEquations.angleTolarance;
		assertEquals(state.r, s.r, state.r * 1E-6);
		assertEquals(state.v, s.v, state.v * 1E-6);
		Tests.assertEqualsAngle(state.alpha, s.alpha, angleTol);
		Tests.assertEqualsAngle(state.delta, s.delta, angleTol);
		Tests.assertEqualsAngle(state.gamma, s.gamma, angleTol);
		Tests.assertEqualsAngle(state.psi, s.psi, angleTol);

		// Check sphere => cart vs sphere => cart => sphere => cart
		ICartesianElements cart2 = s.toCartesianElements();
		assertTrue(Vectors.compare(cart.toVector(), cart2.toVector(), state.r * 1E-6));
	}

}
