/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
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

package be.angelcorp.celest.maneuvers.targeters.lambert

import be.angelcorp.celest.body.Satellite
import be.angelcorp.celest.constants.Constants._
import be.angelcorp.celest.frameGraph.frames
import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.state.PosVel
import be.angelcorp.celest.time.Epochs
import be.angelcorp.celest.unit.CelestTest
import be.angelcorp.celest.universe.DefaultUniverse
import org.scalatest._

/**
 * All reference values computed with:
 * Robust solver for Lambert's orbital-boundary value problem [matlab data] by Rody Oldenhuis
 */
class TestLambert2 extends FlatSpec with Matchers with CelestTest {

  implicit val universe = new DefaultUniverse()

  "The lambert targetter" should "solve the N=0 revolutions case" in {
    val r1 = Vec3(4949101.4221185260000000, 859402.4430396953800000, -151535.8379946680200000) //m
    val r2 = Vec3(3648349.9884584765000000, 4281879.3154454567000000, -755010.8514505261600000) //m
    val tf = 0.1192989774980580 // days
    val N = 0
    val mu = 3986004418E5 // m^3/s^2

    val centerFrame = frames.BodyCenteredSystem(new Satellite(mu2mass(mu), null))

    val s0 = new PosVel(r1, Vec3.zero, centerFrame)
    val s1 = new PosVel(r2, Vec3.zero, centerFrame)
    val t0 = Epochs.J2000
    val te = t0 + tf

    val v1_prograde_true = Vec3(10096.6831628413530000, 4333.9040463802048000, -764.1842151784204600) // m/s
    val v2_prograde_true = Vec3(-8110.7563755037318000, -6018.4641067406237000, 1061.2176044438429000) // m/s
    val v1_retrograde_true = Vec3(1315.7261324536535000, -10735.1341404250650000, 1892.8937904815141000) // m/s
    val v2_retrograde_true = Vec3(5601.4255704605439000, -8298.3753483701657000, 1463.2274699636678000) // m/s

    val lambertProLeft = new Lambert2(s0, s1, t0, te, centerFrame, N, true, true)
    val trajectoryProLeft = lambertProLeft.trajectory
    trajectoryProLeft.origin.toPosVel.velocity      should be (v1_prograde_true +- 2E-6)
    trajectoryProLeft.destination.toPosVel.velocity should be (v2_prograde_true +- 2E-6)

    val lambertProRight = new Lambert2(s0, s1, t0, te, centerFrame, N, true, false)
    val trajectoryProRight = lambertProRight.trajectory
    trajectoryProRight.origin.toPosVel.velocity      should be (v1_prograde_true +- 2E-6)
    trajectoryProRight.destination.toPosVel.velocity should be (v2_prograde_true +- 2E-6)

    val lambertRetLeft = new Lambert2(s0, s1, t0, te, centerFrame, N, false, true)
    val trajectoryRetLeft = lambertRetLeft.trajectory
    trajectoryRetLeft.origin.toPosVel.velocity      should be (v1_retrograde_true +- 2E-6)
    trajectoryRetLeft.destination.toPosVel.velocity should be (v2_retrograde_true +- 2E-6)

    val lambertRetRight = new Lambert2(s0, s1, t0, te, centerFrame, N, false, false)
    val trajectoryRetRight = lambertRetRight.trajectory
    trajectoryRetRight.origin.toPosVel.velocity      should be (v1_retrograde_true +- 2E-6)
    trajectoryRetRight.destination.toPosVel.velocity should be (v2_retrograde_true +- 2E-6)
  }

  "The lambert targetter" should "solve the N=1 revolutions case" in {
    val r1 = Vec3(4949101.4221185260000000, 859402.4430396953800000, -151535.8379946680200000) //m
    val r2 = Vec3(3648349.9884584765000000, 4281879.3154454567000000, -755010.8514505261600000) //m
    val tf = 0.1192989774980580 // days
    val N = 1
    val mu = 3986004418E5 // m^3/s^2

    val centerFrame = frames.BodyCenteredSystem(new Satellite(mu2mass(mu), null))

    val s0 = new PosVel(r1, Vec3.zero, centerFrame)
    val s1 = new PosVel(r2, Vec3.zero, centerFrame)
    val t0 = Epochs.J2000
    val te = t0 + tf

    val v1_prograde_left = Vec3(-1265.9264854521123000, 10660.0671819508730000, -1879.6574603427921000) //m/s
    val v2_prograde_left = Vec3(-5584.6019382811455000, 8204.5589196619712000, -1446.6851023487011000) //m/s
    val v1_prograde_right = Vec3(8918.2511158662492000, 4409.3440789097822000, -777.4863283389091700) //m/s
    val v2_prograde_right = Vec3(-7506.6196898669295000, -4929.4928888196837000, 869.2025975094234100) //m/s
    val v1_retrograde_left = Vec3(-9921.3226492572248000, -4341.8848035061446000, 765.5914379862049400) //m/s
    val v2_retrograde_left = Vec3(8018.9435079575824000, 5858.5741700537847000, -1033.0246946621844000) //m/s
    val v1_retrograde_right = Vec3(629.8861440442892700, -9747.3484276097843000, 1718.7205181538370000) //m/s
    val v2_retrograde_right = Vec3(5396.8566431760819000, -7036.9490066396938000, 1240.8039717402089000) //m/s

    val lambertProLeft = new Lambert2(s0, s1, t0, te, centerFrame, N, true, true)
    val trajectoryProLeft = lambertProLeft.trajectory
    trajectoryProLeft.origin.toPosVel.velocity      should be (v1_prograde_left +- 3E-6)
    trajectoryProLeft.destination.toPosVel.velocity should be (v2_prograde_left +- 3E-6)

    val lambertProRight = new Lambert2(s0, s1, t0, te, centerFrame, N, true, false)
    val trajectoryProRight = lambertProRight.trajectory
    trajectoryProRight.origin.toPosVel.velocity      should be (v1_prograde_right +- 4E-6)
    trajectoryProRight.destination.toPosVel.velocity should be (v2_prograde_right +- 4E-6)

    val lambertRetLeft = new Lambert2(s0, s1, t0, te, centerFrame, N, false, true)
    val trajectoryRetLeft = lambertRetLeft.trajectory
    trajectoryRetLeft.origin.toPosVel.velocity      should be (v1_retrograde_left +- 3E-6)
    trajectoryRetLeft.destination.toPosVel.velocity should be (v2_retrograde_left +- 3E-6)

    val lambertRetRight = new Lambert2(s0, s1, t0, te, centerFrame, N, false, false)
    val trajectoryRetRight = lambertRetRight.trajectory
    trajectoryRetRight.origin.toPosVel.velocity      should be (v1_retrograde_right +- 4E-6)
    trajectoryRetRight.destination.toPosVel.velocity should be (v2_retrograde_right +- 4E-6)
  }

  "The lambert targetter" should "solve the N=4 revolutions case" in {
    val r1 = Vec3(4949101.4221185260000000, 859402.4430396953800000, -151535.8379946680200000) //m
    val r2 = Vec3(3648349.9884584765000000, 4281879.3154454567000000, -755010.8514505261600000) //m
    val tf = 0.1192989774980580 // days
    val N = 4
    val mu = 3986004418E5 // m^3/s^2

    val centerFrame = frames.BodyCenteredSystem(new Satellite(mu2mass(mu), null))

    val s0 = new PosVel(r1, Vec3.zero, centerFrame)
    val s1 = new PosVel(r2, Vec3.zero, centerFrame)
    val t0 = Epochs.J2000
    val te = t0 + tf

    val v1_prograde_left = Vec3(1618.0817850488665000, 7225.3760295109969000, -1274.0287397669906000) //m/s
    val v2_prograde_left = Vec3(-5148.0510872930554000, 3378.2948229580488000, -595.6845260752310200) //m/s
    val v1_prograde_right = Vec3(4812.3627329127148000, 5280.0203047887999000, -931.0100384227980600) //m/s
    val v2_prograde_right = Vec3(-5759.8462467333111000, -731.1159298546271000, 128.9154644591281700) //m/s
    val v1_retrograde_left = Vec3(-5076.6084677018716000, -5182.5921327988517000, 913.8308230198654200) //m/s
    val v2_retrograde_left = Vec3(5847.8739683299273000, 1028.8393989069427000, -181.4121448431730500) //m/s
    val v1_retrograde_right = Vec3(-2546.0418701126791000, -6488.7220023867440000, 1144.1367593374396000) //m/s
    val v2_retrograde_right = Vec3(5224.7179917529947000, -2070.4309653571795000, 365.0728408867440200) //m/s

    val lambertProLeft = new Lambert2(s0, s1, t0, te, centerFrame, N, true, true)
    val trajectoryProLeft = lambertProLeft.trajectory
    trajectoryProLeft.origin.toPosVel.velocity      should be (v1_prograde_left +- 2E-5)
    trajectoryProLeft.destination.toPosVel.velocity should be (v2_prograde_left +- 2E-5)

    val lambertProRight = new Lambert2(s0, s1, t0, te, centerFrame, N, true, false)
    val trajectoryProRight = lambertProRight.trajectory
    trajectoryProRight.origin.toPosVel.velocity      should be (v1_prograde_right +- 2E-5)
    trajectoryProRight.destination.toPosVel.velocity should be (v2_prograde_right +- 2E-5)

    val lambertRetLeft = new Lambert2(s0, s1, t0, te, centerFrame, N, false, true)
    val trajectoryRetLeft = lambertRetLeft.trajectory
    trajectoryRetLeft.origin.toPosVel.velocity      should be (v1_retrograde_left +- 3E-5)
    trajectoryRetLeft.destination.toPosVel.velocity should be (v2_retrograde_left +- 3E-5)

    val lambertRetRight = new Lambert2(s0, s1, t0, te, centerFrame, N, false, false)
    val trajectoryRetRight = lambertRetRight.trajectory
    trajectoryRetRight.origin.toPosVel.velocity      should be (v1_retrograde_right +- 2E-5)
    trajectoryRetRight.destination.toPosVel.velocity should be (v2_retrograde_right +- 3E-5)
  }

  "The lambert targetter" should " not solve the N=5 revolutions case" in {
    try {
      val r1 = Vec3(4949101.4221185260000000, 859402.4430396953800000, -151535.8379946680200000) //m
      val r2 = Vec3(3648349.9884584765000000, 4281879.3154454567000000, -755010.8514505261600000) //m
      val tf = 0.1192989774980580 // days
      val N = 5
      val mu = 3986004418E5 // m^3/s^2

      val centerFrame = frames.BodyCenteredSystem(new Satellite(mu2mass(mu), null))

      val s0 = new PosVel(r1, Vec3.zero, centerFrame)
      val s1 = new PosVel(r2, Vec3.zero, centerFrame)
      val t0 = Epochs.J2000
      val te = t0 + tf

      new Lambert2(s0, s1, t0, te, centerFrame, N, true, true).trajectory
      fail("The case of N=5 should not be solvable")
    } catch {
      case e: ArithmeticException =>
    }
  }

}
