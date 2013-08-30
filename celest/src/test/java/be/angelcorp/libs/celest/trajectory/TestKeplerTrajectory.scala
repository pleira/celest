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
package be.angelcorp.libs.celest.trajectory

import math._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import be.angelcorp.libs.util.physics.Time._
import be.angelcorp.libs.celest.universe.DefaultUniverse
import be.angelcorp.libs.celest.constants.EarthConstants
import be.angelcorp.libs.celest.frames
import be.angelcorp.libs.celest.state.{TrueAnomaly, Keplerian}
import be.angelcorp.libs.celest.unit.CelestTest._

@RunWith(classOf[JUnitRunner])
class TestKeplerTrajectory extends FlatSpec with ShouldMatchers  {

  implicit val universe = new DefaultUniverse

  "KeplerTrajectory" should "correctly propagate geostationairy orbits" in {
    val earthFrame =  frames.BodyCentered(EarthConstants.bodyCenter)

		val a = pow(earthFrame.centerBody.getMu / pow((2.0 * Pi) / 86400.0, 2), 1.0 / 3.0)
		val k = new Keplerian(a, 0, 0, 0, 0, 0, Some(earthFrame))
    val t = new KeplerTrajectory(universe.J2000_EPOCH, k)

		val time1 = 0 // 0deg
    val k2_predict1 = new Keplerian(a, 0, 0, 0, 0, 0, Some(earthFrame))
    val k2_true1 = t(universe.J2000_EPOCH.add(time1, second))
		assertEquals( k2_true1, k2_predict1, a * 1E-12, 1E-12, 1E-12, 1E-12, 1E-12, 1E-12 )

		val time2 = 6 * 3600; // 90deg
    val k2_predict2 = Keplerian.apply(a, 0, 0, 0, 0, TrueAnomaly(Pi/2), Some(earthFrame))
		val k2_true2 = t(universe.J2000_EPOCH.add(time2, second))
    assertEquals( k2_true2, k2_predict2, a * 1E-12, 1E-12, 1E-12, 1E-12, 1E-12, 1E-12 )

		val time3 = 12 * 3600; // 180deg
    val k2_predict3 = Keplerian.apply(a, 0, 0, 0, 0, TrueAnomaly(Pi), Some(earthFrame))
    val k2_true3 = t(universe.J2000_EPOCH.add(time3, second))
		assertEquals( k2_true3, k2_predict3, a * 1E-12, 1E-12, 1E-12, 1E-12, 1E-12, 1E-12 )

		val time4 = 16 * 3600; // 240deg
    val k2_predict4 = Keplerian.apply(a, 0, 0, 0, 0, new TrueAnomaly(Pi * 4.0 / 3.0), Some(earthFrame))
    val k2_true4 = t(universe.J2000_EPOCH.add(time4, second))
		assertEquals( k2_true4, k2_predict4, a * 1E-8, 1E-8, 1E-8, 1E-8, 1E-8, 1E-8 )
	}

  it should "correctly propagate a fixed 3d orbit" in {
    val earthFrame = frames.BodyCentered(EarthConstants.bodyCenter)

		// Some pseudo random start elements
		val k = Keplerian.apply(1E8, 0.3, 1.1, 0.3, 0.9, TrueAnomaly(0.2), Some(earthFrame))
		// Stats for these elements, result of getOrbitEqn is assumed to be correct
		val meanAnomalyT0 = k.meanAnomaly
    val meanMotion    = k.quantities.meanMotion

		// This should be the resulting true anomaly
		val deltaT = 500;// s
		val k2 = new Keplerian(k.a, k.e, k.i, k.ω, k.Ω, meanAnomalyT0 + meanMotion * deltaT, Some(earthFrame))

		// Find the elements according to the trajectory
		val trajectory = new KeplerTrajectory(universe.J2000_EPOCH, k)
    val k3 = trajectory(universe.J2000_EPOCH.add(deltaT, second))

		// Check if they are equal
    assertEquals( k2, k3, k.a * 1E-8, 1E-8, 1E-8, 1E-8, 1E-8, 1E-8 )
	}
}