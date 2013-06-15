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
package be.angelcorp.libs.celest.trajectory;

import static java.lang.Math.PI;
import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.constants.EarthConstants;
import be.angelcorp.libs.celest.state.positionState.IKeplerElements;
import be.angelcorp.libs.celest.state.positionState.KeplerElements;
import be.angelcorp.libs.celest.time.JulianDate;
import be.angelcorp.libs.celest.unit.CelestTest;
import be.angelcorp.libs.celest.universe.DefaultUniverse;
import be.angelcorp.libs.celest.universe.Universe;
import be.angelcorp.libs.math.MathUtils2;
import be.angelcorp.libs.util.physics.Time;

public class TestKeplerTrajectory extends CelestTest {

    public static Universe universe = new DefaultUniverse();

	/**
	 * Check KeplerTrajectory using a geostationairy orbit, at specific hours, rotations should match
	 */
	public void testGeoStationairyOrbit() {
		CelestialBody earth = universe.earthConstants().bodyCenter();
		double a = Math.pow(earth.getMu() / Math.pow((2. * PI) / (3600. * 24.), 2), 1. / 3.);
		IKeplerElements k = new KeplerElements(a, 0, 0, 0, 0, 0, earth);
		KeplerTrajectory t = new KeplerTrajectory(k, universe.J2000_EPOCH());

		double time = 0;
		IKeplerElements k2_predict = new KeplerElements(a, 0, 0, 0, 0, 0);
		IKeplerElements k2_true = t.evaluate(universe.J2000_EPOCH().add(time, Time.second));
		CelestTest.assertEquals(
				String.format("At t=%f, the computed state %s is not equal to the predicted state %s",
						time, k2_predict, k2_true), k2_predict.toVector(), k2_true.toVector(), 1E-12);

		time = 6 * 3600; // 90�
		k2_predict = new KeplerElements(a, 0, 0, 0, 0, PI / 2);
		k2_true = t.evaluate(universe.J2000_EPOCH().add(time, Time.second));
		CelestTest.assertEquals(
				String.format("At t=%f, the computed state %s is not equal to the predicted state %s",
						time, k2_predict, k2_true), k2_predict.toVector(), k2_true.toVector(), 1E-12);

		time = 12 * 3600; // 180�
		k2_predict = new KeplerElements(a, 0, 0, 0, 0, PI);
		k2_true = t.evaluate(universe.J2000_EPOCH().add(time, Time.second));
		k2_true.setTrueAnomaly(MathUtils2.mod(k2_true.getTrueAnomaly(), 2 * PI)); // its -pi otherwise
		CelestTest.assertEquals(
				String.format("At t=%f, the computed state %s is not equal to the predicted state %s",
						time, k2_predict, k2_true), k2_predict.toVector(), k2_true.toVector(), 1E-12);

		time = 16 * 3600; // 240�
		k2_predict = new KeplerElements(a, 0, 0, 0, 0, PI * 4. / 3.);
		k2_true = t.evaluate(universe.J2000_EPOCH().add(time, Time.second));
		k2_true.setTrueAnomaly(MathUtils2.mod(k2_true.getTrueAnomaly(), 2 * PI)); // its -2/3 pi
		assertTrue(String.format("At t=%f, the computed state %s is not equal to the predicted state %s",
				time, k2_predict, k2_true), k2_predict.equals(k2_true, 1e-8));

	}

	/**
	 * Test a pseudo random 3d KeplerTrajectory
	 */
	public void testOrbit() {
		CelestialBody earth = universe.earthConstants().bodyCenter();
		// Some pseudo random start elements
		KeplerElements k = new KeplerElements(1E8, 0.3, 1.1, 0.3, 0.9, 0.2, earth);
		// Stats for these elements, result of getOrbitEqn is assumed to be correct
		double meanAnomaltyT0 = k.getOrbitEqn().meanAnomaly();
		double meanMotion = k.getOrbitEqn().meanMotion();

		// This should be the resulting true anomaly
		double deltaT = 500;// s
		KeplerElements k2 = k.clone();
		k2.setTrueAnomaly(k.getOrbitEqn().trueAnomalyFromMean(meanAnomaltyT0 + meanMotion * deltaT));

		// Find the elements according to the trajectory
		KeplerTrajectory trajectory = new KeplerTrajectory(k, universe.J2000_EPOCH());
		KeplerElements k3 =
				(KeplerElements) trajectory.evaluate(universe.J2000_EPOCH().add(deltaT, Time.second));

		// Check if they are equal
		assertTrue(String.format("The true kepler state %s is not equal to the expected state %s", k2, k3),
				k2.equals(k3, 1e-8));
		// assertEquals(MathUtils2.mod(k2.getTrueAnomaly(), 2 * PI),
		// MathUtils2.mod(k3.getTrueAnomaly(), 2 * PI), 1E-16);
		// assertEquals(k2.getSemiMajorAxis(), k3.getSemiMajorAxis(), 1E-16);
		// assertEquals(k2.getEccentricity(), k3.getEccentricity(), 1E-16);
		// assertEquals(k2.getInclination(), k3.getInclination(), 1E-16);
		// assertEquals(k2.getRaan(), k3.getRaan(), 1E-16);
		// assertEquals(k2.getArgumentPeriapsis(), k3.getArgumentPeriapsis(), 1E-16);
	}
}
