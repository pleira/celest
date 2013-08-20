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
import be.angelcorp.libs.celest.body.ICelestialBody;
import be.angelcorp.libs.celest.constants.EarthConstants;
import be.angelcorp.libs.celest.frames.BodyCentered;
import be.angelcorp.libs.celest.state.Keplerian;
import be.angelcorp.libs.celest.state.TrueAnomaly;
import be.angelcorp.libs.celest.unit.CelestTest;
import be.angelcorp.libs.celest.universe.DefaultUniverse;
import be.angelcorp.libs.celest.universe.Universe;
import be.angelcorp.libs.math.MathUtils2;
import be.angelcorp.libs.util.physics.Time;
import scala.Some;

public class TestKeplerTrajectory extends CelestTest {

    public static Universe universe = new DefaultUniverse();

	/**
	 * Check KeplerTrajectory using a geostationairy orbit, at specific hours, rotations should match
	 */
	public void testGeoStationairyOrbit() {
		BodyCentered earthFrame = new BodyCentered() {
            @Override
            public ICelestialBody centerBody() {
                return EarthConstants.bodyCenter();
            }
        };

		double a = Math.pow(earthFrame.centerBody().getMu() / Math.pow((2. * PI) / (3600. * 24.), 2), 1. / 3.);
		Keplerian k = new Keplerian(a, 0, 0, 0, 0, 0, new Some<BodyCentered>(earthFrame));
		KeplerTrajectory t = new KeplerTrajectory(k, universe.J2000_EPOCH());

		double time = 0;
		Keplerian k2_predict = new Keplerian(a, 0, 0, 0, 0, 0, new Some<BodyCentered>(earthFrame));
        Keplerian k2_true = t.evaluate(universe.J2000_EPOCH().add(time, Time.second));
		CelestTest.assertEquals( k2_true, k2_predict, a * 1E-12, 1E-12, 1E-12, 1E-12, 1E-12, 1E-12 );

		time = 6 * 3600; // 90�
		k2_predict = Keplerian.apply(a, 0, 0, 0, 0, new TrueAnomaly(PI/2), new Some<BodyCentered>(earthFrame));
		k2_true = t.evaluate(universe.J2000_EPOCH().add(time, Time.second));
        CelestTest.assertEquals( k2_true, k2_predict, a * 1E-12, 1E-12, 1E-12, 1E-12, 1E-12, 1E-12 );

		time = 12 * 3600; // 180�
		k2_predict = Keplerian.apply(a, 0, 0, 0, 0, new TrueAnomaly(PI), new Some<BodyCentered>(earthFrame));
		k2_true = t.evaluate(universe.J2000_EPOCH().add(time, Time.second));
		CelestTest.assertEquals( k2_true, k2_predict, a * 1E-12, 1E-12, 1E-12, 1E-12, 1E-12, 1E-12 );

		time = 16 * 3600; // 240�
		k2_predict = Keplerian.apply(a, 0, 0, 0, 0, new TrueAnomaly(PI * 4. / 3.), new Some<BodyCentered>(earthFrame));
		k2_true = t.evaluate(universe.J2000_EPOCH().add(time, Time.second));
		CelestTest.assertEquals( k2_true, k2_predict, a * 1E-8, 1E-8, 1E-8, 1E-8, 1E-8, 1E-8 );
	}

	/**
	 * Test a pseudo random 3d KeplerTrajectory
	 */
	public void testOrbit() {
        BodyCentered earthFrame = new BodyCentered() {
            @Override
            public ICelestialBody centerBody() {
                return EarthConstants.bodyCenter();
            }
        };
		// Some pseudo random start elements
		Keplerian k = Keplerian.apply(1E8, 0.3, 1.1, 0.3, 0.9, new TrueAnomaly(0.2), new Some<BodyCentered>(earthFrame));
		// Stats for these elements, result of getOrbitEqn is assumed to be correct
		double meanAnomaltyT0 = k.meanAnomaly();
		double meanMotion = k.quantities().meanMotion();

		// This should be the resulting true anomaly
		double deltaT = 500;// s
		Keplerian k2 = new Keplerian(k.a(), k.e(), k.i(), k.ω(), k.Ω(), meanAnomaltyT0 + meanMotion * deltaT, new Some<BodyCentered>(earthFrame));

		// Find the elements according to the trajectory
		KeplerTrajectory trajectory = new KeplerTrajectory(k, universe.J2000_EPOCH());
        Keplerian k3 = trajectory.evaluate(universe.J2000_EPOCH().add(deltaT, Time.second));

		// Check if they are equal
        CelestTest.assertEquals( k2, k3, k.a() * 1E-8, 1E-8, 1E-8, 1E-8, 1E-8, 1E-8 );
	}
}
