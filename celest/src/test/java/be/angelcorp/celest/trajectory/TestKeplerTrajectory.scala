/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.celest.trajectory

import be.angelcorp.celest.constants.EarthConstants
import be.angelcorp.celest.frameGraph.frames
import be.angelcorp.celest.state.{Keplerian, TrueAnomaly}
import be.angelcorp.celest.time.Epochs
import be.angelcorp.celest.unit.CelestTest
import be.angelcorp.celest.universe.DefaultUniverse
import org.scalatest.{FlatSpec, Matchers}

import scala.math._

class TestKeplerTrajectory extends FlatSpec with Matchers with CelestTest {

  implicit val universe = new DefaultUniverse

  "KeplerTrajectory" should "correctly propagate geostationairy orbits" in {
    val earthFrame = frames.BodyCenteredSystem(EarthConstants.body)

    val a = pow(earthFrame.centerBody.μ / pow((2.0 * Pi) / 86400.0, 2), 1.0 / 3.0)
    val k = new Keplerian(a, 0, 0, 0, 0, 0, earthFrame)
    val t = new KeplerTrajectory(Epochs.J2000, k)

    val time1 = 0 // 0deg
    val k2_predict1 = new Keplerian(a, 0, 0, 0, 0, 0, earthFrame)
    val k2_true1 = t(Epochs.J2000.addS(time1))
    assertEquals(k2_true1, k2_predict1, a * 1E-12, 1E-12, 1E-12, 1E-12, 1E-12, 1E-12)

    val time2 = 6 * 3600
    // 90deg
    val k2_predict2 = Keplerian.apply(a, 0, 0, 0, 0, TrueAnomaly(Pi / 2), earthFrame)
    val k2_true2 = t(Epochs.J2000.addS(time2))
    assertEquals(k2_true2, k2_predict2, a * 1E-12, 1E-12, 1E-12, 1E-12, 1E-12, 1E-12)

    val time3 = 12 * 3600
    // 180deg
    val k2_predict3 = Keplerian.apply(a, 0, 0, 0, 0, TrueAnomaly(Pi), earthFrame)
    val k2_true3 = t(Epochs.J2000.addS(time3))
    assertEquals(k2_true3, k2_predict3, a * 1E-12, 1E-12, 1E-12, 1E-12, 1E-12, 1E-12)

    val time4 = 16 * 3600
    // 240deg
    val k2_predict4 = Keplerian.apply(a, 0, 0, 0, 0, new TrueAnomaly(Pi * 4.0 / 3.0), earthFrame)
    val k2_true4 = t(Epochs.J2000.addS(time4))
    assertEquals(k2_true4, k2_predict4, a * 1E-8, 1E-8, 1E-8, 1E-8, 1E-8, 1E-8)
  }

  it should "correctly propagate a fixed 3d orbit" in {
    val earthFrame = frames.BodyCenteredSystem(EarthConstants.body)

    // Some pseudo random start elements
    val k = Keplerian.apply(1E8, 0.3, 1.1, 0.3, 0.9, TrueAnomaly(0.2), earthFrame)
    // Stats for these elements, result of getOrbitEqn is assumed to be correct
    val meanAnomalyT0 = k.meanAnomaly
    val meanMotion = k.quantities.meanMotion

    // This should be the resulting true anomaly
    val deltaT = 500
    // s
    val k2 = new Keplerian(k.a, k.e, k.i, k.ω, k.Ω, meanAnomalyT0 + meanMotion * deltaT, earthFrame)

    // Find the elements according to the trajectory
    val trajectory = new KeplerTrajectory(Epochs.J2000, k)
    val k3 = trajectory(Epochs.J2000.addS(deltaT))

    // Check if they are equal
    assertEquals(k2, k3, k.a * 1E-8, 1E-8, 1E-8, 1E-8, 1E-8, 1E-8)
  }
}
