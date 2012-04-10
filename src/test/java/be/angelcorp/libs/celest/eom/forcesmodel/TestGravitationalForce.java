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
package be.angelcorp.libs.celest.eom.forcesmodel;

import junit.framework.TestCase;
import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.constants.EarthConstants;
import be.angelcorp.libs.celest.constants.SolarConstants;
import be.angelcorp.libs.celest.kepler.KeplerCircular;
import be.angelcorp.libs.celest.state.positionState.KeplerElements;
import be.angelcorp.libs.celest.state.positionState.SphericalElements;
import be.angelcorp.libs.celest.unit.Tests;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.util.physics.Length;

public class TestGravitationalForce extends TestCase {

	public void testForce() throws Exception {
		/* Test the f/a in a simple earth system (norm only) */
		CelestialBody earth = EarthConstants.bodyCenter;
		CelestialBody sat = new CelestialBody(
				new KeplerElements(10E6, 0, 0, 0, 0, 0, earth), 5);
		GravitationalForce_C g = new GravitationalForce_C(sat, earth);
		assertEquals(19.93d, g.getForce().getNorm(), 1E-1);
		assertEquals(3.986d, g.toAcceleration().getNorm(), 1E-2);

		/* Test the f/a in a simple earth system all components */
		CelestialBody sat1 = new CelestialBody(new SphericalElements(10E6, Math.PI / 3, 0,
				KeplerCircular.vc(10E6, earth.getMu()), 0, 0, earth), 5);
		GravitationalForce_C g1 = new GravitationalForce_C(sat1, earth);
		Tests.assertEquals(
				new Vector3D(Math.cos(Math.PI / 3) * -19.93d, Math.sin(Math.PI / 3) * -19.93d, 0),
				g1.getForce(), 1E-1);
		Tests.assertEquals(
				new Vector3D(Math.cos(Math.PI / 3) * -3.986d, Math.sin(Math.PI / 3) * -3.986d, 0),
				g1.toAcceleration(), 1E-2);

		/* Test the f/a in a simple sun system (norm only) */
		CelestialBody sun = SolarConstants.body;
		CelestialBody sat2 = new CelestialBody(
				new KeplerElements(Length.convert(1, Length.AU), 0, 0, 0, 0, 0, sun), 5);
		GravitationalForce_C g2 = new GravitationalForce_C(sat2, sun);
		assertEquals(0.02965d, g2.getForce().getNorm(), 1E-4);
		assertEquals(0.005930d, g2.toAcceleration().getNorm(), 1E-5);
	}
}
