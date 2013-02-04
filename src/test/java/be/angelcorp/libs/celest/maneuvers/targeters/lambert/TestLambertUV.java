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
package be.angelcorp.libs.celest.maneuvers.targeters.lambert;

import be.angelcorp.libs.celest.constants.Constants;
import be.angelcorp.libs.math.linear.ImmutableVector3D;
import be.angelcorp.libs.math.linear.Vector3D$;
import org.junit.Ignore;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.state.positionState.CartesianElements;
import be.angelcorp.libs.celest.state.positionState.ICartesianElements;
import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.celest.time.JulianDate;
import be.angelcorp.libs.celest.trajectory.ITrajectory;
import be.angelcorp.libs.celest.unit.CelestTest;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.util.physics.Time;

@Ignore
public class TestLambertUV extends CelestTest {

	public void testRossettaLeg1() {
		// Rosetta Earth (launch) - Earth (swingby1)
		// data from NASA horizons, results generated using GTOP lambert targetter

		// [x2,y2,z2] = sph2cart(225*degree, 0*degree, au);
		// [x1,y1,z1] = sph2cart(30*degree, 21*degree, au);
		// [V1, V2] = lambert([x1,y1,z1]/1e3, [x2,y2,z2]/1e3, 300, 0, 1.32712428e11)
		// Result:
		// V1 = 27.4408 -14.0438 0.5468 (km/s)
		// V2 = -24.5242 18.6776 -0.4856 (km/s)

		CelestialBody center = new CelestialBody(new CartesianElements(), Constants.mu2mass(1.32712428E20));

		Vector3D r1 = new ImmutableVector3D(1.364377463519496E11, 6.129036612130551E10, 2.784835397959758E09);
		Vector3D r2 = new ImmutableVector3D(3.730051396741382E09, -1.495513611895726E11, 0.);

		IJulianDate departure = JulianDate.J2000_EPOCH;
		IJulianDate arrival = JulianDate.J2000_EPOCH.add(300, Time.day);
		LambertUV lambert = new LambertUV(
				new CartesianElements(r1, Vector3D$.MODULE$.ZERO()),
				new CartesianElements(r2, Vector3D$.MODULE$.ZERO()),
				center, departure, arrival, false);

		ITrajectory trajectory = lambert.getTrajectory();
		Vector3D v1Expected = new ImmutableVector3D(2.744082030955964E04, -1.404383002109151E04, 5.468193199081889e+002);
		Vector3D v2Expected = new ImmutableVector3D(-2.452424882838209E04, 1.867758520103548E04, -4.856158493467824e+002);

		// Check to 1 mm/s accuracy:
		ICartesianElements state1 = trajectory.evaluate(departure).toCartesianElements();
		assertTrue(state1.equals(new CartesianElements(r1, v1Expected), 1e-3));
		ICartesianElements state2 = trajectory.evaluate(arrival).toCartesianElements();
		assertTrue(state1.equals(new CartesianElements(r2, v2Expected), 1e-3));
	}
}
