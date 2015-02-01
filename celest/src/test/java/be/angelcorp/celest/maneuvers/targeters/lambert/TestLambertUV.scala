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
package be.angelcorp.celest.maneuvers.targeters.lambert

import be.angelcorp.celest.unit.CelestTest
import org.scalatest.{FlatSpec, Matchers}

class TestLambertUV extends FlatSpec with Matchers with CelestTest {

//    public static Universe universe = new DefaultUniverse();
//
//    public void testRossettaLeg1() {
//        // Rosetta Earth (launch) - Earth (swingby1)
//        // data from NASA horizons, results generated using GTOP lambert targetter
//
//        // [x2,y2,z2] = sph2cart(225*degree, 0*degree, au);
//        // [x1,y1,z1] = sph2cart(30*degree, 21*degree, au);
//        // [V1, V2] = lambert([x1,y1,z1]/1e3, [x2,y2,z2]/1e3, 300, 0, 1.32712428e11)
//        // Result:
//        // V1 = 27.4408 -14.0438 0.5468 (km/s)
//        // V2 = -24.5242 18.6776 -0.4856 (km/s)
//
//        Satellite center = new Satellite(Constants.mu2mass(1.32712428E20), null, universe);
//
//        Vector3D r1 = new ImmutableVector3D(1.364377463519496E11, 6.129036612130551E10, 2.784835397959758E09);
//        Vector3D r2 = new ImmutableVector3D(3.730051396741382E09, -1.495513611895726E11, 0.);
//
//        Epoch departure = Epochs.J2000(universe);
//        Epoch arrival = Epochs.J2000(universe).addS(300);
//        LambertUV<?> lambert = new LambertUV(
//                new PosVel(r1, Vector3D$.MODULE$.ZERO(), null),
//                new PosVel(r2, Vector3D$.MODULE$.ZERO(), null),
//                BodyCenteredSystem$.MODULE$.apply(center), departure, arrival, false);
//
//        Trajectory trajectory = lambert.trajectory();
//        Vector3D v1Expected = new ImmutableVector3D(2.744082030955964E04, -1.404383002109151E04, 5.468193199081889e+002);
//        Vector3D v2Expected = new ImmutableVector3D(-2.452424882838209E04, 1.867758520103548E04, -4.856158493467824e+002);
//
//        // Check to 1 mm/s accuracy:
//        PosVel state1 = trajectory.apply(departure).toPosVel();
//        assertEquals(new PosVel(r1, v1Expected, null), state1, 1E-3, 1E-3);
//        PosVel state2 = trajectory.apply(arrival).toPosVel();
//        assertEquals(new PosVel(r2, v2Expected, null), state2, 1E-3, 1E-3);
//    }
}
