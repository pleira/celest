/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
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
package be.angelcorp.libs.celest.eom.forcesmodel;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.body.ICelestialBody;
import be.angelcorp.libs.celest.constants.EarthConstants;
import be.angelcorp.libs.celest.constants.SolarConstants;
import be.angelcorp.libs.celest.kepler.KeplerCircular;
import be.angelcorp.libs.celest.state.positionState.KeplerElements;
import be.angelcorp.libs.celest.state.positionState.SphericalElements;
import be.angelcorp.libs.celest.unit.CelestTest;
import be.angelcorp.libs.celest.universe.DefaultUniverse;
import be.angelcorp.libs.celest.universe.Universe;
import be.angelcorp.libs.math.linear.ImmutableVector3D;
import be.angelcorp.libs.util.physics.Length;

public class TestGravitationalForce extends CelestTest {

    public static Universe universe = new DefaultUniverse();

	public void testForce() throws Exception {
		/* Test the f/a in a simple earth system (norm only) */
		CelestialBody earth = universe.earthConstants().bodyCenter();
		ICelestialBody sat = new CelestialBody(
				new KeplerElements(10E6, 0, 0, 0, 0, 0, earth), 5);
		GravitationalForce_C g = new GravitationalForce_C(sat, earth);
		assertEquals(19.93d, g.getForce().norm(), 1E-1);
		assertEquals(3.986d, g.toAcceleration().norm(), 1E-2);

		/* Test the f/a in a simple earth system all components */
		ICelestialBody sat1 = new CelestialBody(new SphericalElements(10E6, Math.PI / 3, 0,
				KeplerCircular.vc(10E6, earth.getMu()), 0, 0, earth), 5);
		GravitationalForce_C g1 = new GravitationalForce_C(sat1, earth);
		CelestTest.assertTrue(
				new ImmutableVector3D(Math.cos(Math.PI / 3) * -19.93d, Math.sin(Math.PI / 3) * -19.93d, 0).equals(
                        g1.getForce(), 1E-1
                ) );
		CelestTest.assertTrue(
				new ImmutableVector3D(Math.cos(Math.PI / 3) * -3.986d, Math.sin(Math.PI / 3) * -3.986d, 0).equals(
				    g1.toAcceleration(), 1E-2
                ) );

		/* Test the f/a in a simple sun system (norm only) */
		CelestialBody sun = SolarConstants.body();
		ICelestialBody sat2 = new CelestialBody(
				new KeplerElements(Length.convert(1, Length.AU), 0, 0, 0, 0, 0, sun), 5);
		GravitationalForce_C g2 = new GravitationalForce_C(sat2, sun);
		assertEquals(0.02965d, g2.getForce().norm(), 1E-4);
		assertEquals(0.005930d, g2.toAcceleration().norm(), 1E-5);
	}
}
