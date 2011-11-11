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

import java.util.Set;

import be.angelcorp.libs.celest.constants.EarthConstants;
import be.angelcorp.libs.celest.constants.SolarConstants;
import be.angelcorp.libs.celest.kepler.KeplerEquations;
import be.angelcorp.libs.celest.kepler.KeplerOrbitTypes;
import be.angelcorp.libs.celest.unit.Tests;
import be.angelcorp.libs.math.linear.Vectors;
import be.angelcorp.libs.util.physics.Angle;
import be.angelcorp.libs.util.physics.Length;

import com.google.common.collect.Sets;

public class TestKeplerElements extends TestStateVector<KeplerElements> {

	private Set<KeplerElements>	testOrbits;

	@Override
	public Set<KeplerElements> getTestStateVectors() {
		testOrbits = Sets.newLinkedHashSet();
		/* Object with 0 eccentricity */
		/* (2002 TZ300) (TransNeptunian Object) see http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=2002%20TZ300 */
		// TODO: M = Angle.convert(359.93, Angle.DEG) => trueA
		testOrbits.add(new KeplerElements(Length.convert(43.69, Length.AU), 0,
				Angle.convert(3.58, Angle.DEG), Angle.convert(334.67, Angle.DEG),
				Angle.convert(55.46, Angle.DEG), 0, SolarConstants.body));
		/* (2000 QF226) (TransNeptunian Object) see http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=2000%20QF226 */
		testOrbits.add(new KeplerElements(Length.convert(46.49, Length.AU), 0,
				Angle.convert(2.25, Angle.DEG), Angle.convert(296.90, Angle.DEG),
				Angle.convert(41.00, Angle.DEG), 0, SolarConstants.body));

		/* Object near 0 eccentricity */
		// The earth : )
		testOrbits.add(EarthConstants.solarOrbit);

		/* Objects highly elliptical orbits */
		/* 2212 Hephaistos (1978 SB) (Apollo [NEO]), see http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=2212 */
		// TODO: M = Angle.convert(12.63, Angle.DEG) => trueA
		testOrbits.add(new KeplerElements(Length.convert(2.167, Length.AU), 0.8338,
				Angle.convert(11.74, Angle.DEG), Angle.convert(208.56, Angle.DEG),
				Angle.convert(28.27, Angle.DEG), 0, SolarConstants.body));
		/* 105140 (2000 NL10) (Aten [NEO]), see http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=105140 */
		// TODO: M = Angle.convert(293.30, Angle.DEG) => trueA
		testOrbits.add(new KeplerElements(Length.convert(0.9143, Length.AU), 0.8171,
				Angle.convert(32.52, Angle.DEG), Angle.convert(281.56, Angle.DEG),
				Angle.convert(237.44, Angle.DEG), 0, SolarConstants.body));

		return testOrbits;
	}

	@Override
	public void testToFromCartesianElements(KeplerElements state) {
		CartesianElements cart = state.toCartesianElements();
		KeplerElements k = KeplerEquations.cartesian2kepler(cart, state.getCenterbody());

		double angleTol = KeplerEquations.angleTolarance;
		assertEquals(state.a, k.a, 1);
		assertEquals(state.e, k.e, KeplerEquations.eccentricityTolarance);
		Tests.assertEqualsAngle(state.i, k.i, angleTol);

		if (state.isEquatorial()) {
			if (state.getOrbitType() == KeplerOrbitTypes.Circular) {
				// No raan / w defined, use true longitude instead
				// TODO:ADD TEST CASE FOR THIS
				Tests.assertEqualsAngle(
						state.getOrbitEqn().trueLongitude(), k.getOrbitEqn().trueLongitude(), angleTol);
			} else {
				// No raan, use true longitude of periapsis instead
				// TODO:ADD TEST CASE FOR THIS
				Tests.assertEqualsAngle(state.getOrbitEqn().trueLongitudeOfPeriapse(),
						k.getOrbitEqn().trueLongitudeOfPeriapse(), angleTol);
				Tests.assertEqualsAngle(state.omega, k.omega, angleTol);
			}
		} else {
			if (state.getOrbitType() == KeplerOrbitTypes.Circular) {
				// No w defined, use argument of latitude instead
				Tests.assertEqualsAngle(state.getOrbitEqn().arguementOfLatitude(),
						k.getOrbitEqn().arguementOfLatitude(), angleTol);
				Tests.assertEqualsAngle(state.raan, k.raan, angleTol);
			} else {
				// w, raan, nu are properly defined
				Tests.assertEqualsAngle(state.raan, k.raan, angleTol);
				Tests.assertEqualsAngle(state.omega, k.omega, angleTol);
				Tests.assertEqualsAngle(state.trueA, k.trueA, angleTol);
			}
		}

		// This should again work for everything
		CartesianElements cart2 = k.toCartesianElements();
		assertTrue(Vectors.compare(cart.toVector(), cart2.toVector(), state.getSemiMajorAxis() * 1E-6));
	}

}
