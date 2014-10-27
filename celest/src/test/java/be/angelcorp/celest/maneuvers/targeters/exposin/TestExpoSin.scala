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
package be.angelcorp.celest.maneuvers.targeters.exposin

import be.angelcorp.celest.constants.Constants
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem
import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.state.PosVel
import be.angelcorp.celest.time.Epochs
import be.angelcorp.celest.physics.Units._
import be.angelcorp.celest.unit.CelestTest
import be.angelcorp.celest.universe.DefaultUniverse
import org.scalatest.{FlatSpec, Matchers}

import scala.math._

/**
 * Validation of the Exposin angular rate equations
 * <p>
 * Validation is done against matlab routines by Rody P.S. Oldenhuis:<br/>
 * <a href="http://www.mathworks.com/matlabcentral/fileexchange/29272">Skipping Stone - An interplanetary
 * space mission design tool</a><br />
 * <b>Rody P. Oldenhuis, "Trajectory optimization of a mission to the solar bow shock and minor planets",
 * Master�s Thesis, Delft University of Technology, 2010.</b>
 * </p>
 *
 * @author Simon Billemont
 */
class TestExpoSin extends FlatSpec with Matchers with CelestTest {

    implicit lazy val universe = new DefaultUniverse()

  "ExpoSin" should "generate the correct trajectory for n=0" in {
    val r1 = 151366683.169E3
    val r2 = 206953872.627E3
    val dTheta = 1.9532 + 0 * (2 * Pi)
    val k2 = 0.7013
    val dt = days(130)

    val t1 = Epochs.J2000(universe)
    val t2 = Epochs.J2000(universe).addS(dt)

    val s1 = new PosVel[BodyCenteredSystem]( Vec3(r1, 0, 0), Vec3.zero, null)
    val s2 = new PosVel[BodyCenteredSystem]( Vec3.spherical(dTheta, 0) * r2, Vec3.zero, null)

    val frame = BodyCenteredSystem(Constants.mass2mu(1.9891E30))
    val exposin = new ExpoSin(s1, s2, t1, t2, frame)
    exposin.assumeK2 = k2

    // Results as computed by the Matlab routine:
    val ml_V1 = Vec3(-2.951216831366131e+002, +3.310652568212942e+004, 0)
    val ml_V2 = Vec3(-2.572543389259841e+004, -2.886383731363452e+003, 0)
    val ml_k0 = 2.272595936284008e+011
    val ml_k1 = -4.065864323385938e-001
    val ml_k2 = 7.013000000000000e-001
    val ml_phi = 1.539528221815391e+000
    val ml_tf = 1.123200000000000e+007
    val ml_N = 0
    val ml_dth = 1.953200000000001e+000
    val ml_gamma1 = -8.914069361335221e-003
    val ml_gamma_m = -6.505324139843980e-001
    val ml_gamma_M = 7.999869059004543e-001

    val solutionSet = exposin.solutionSet
    assertEquals(ml_gamma_m, solutionSet.getDomain.lowerBound, 1E-1)
    assertEquals(ml_gamma_M, solutionSet.getDomain.upperBound, 1E-1)

    val trajectory = exposin.trajectory
    val shape = trajectory.exposin
    assertEquals(ml_k0, shape.getK0, ml_k0 * 1E-8)
    assertEquals(ml_k1, shape.getK1, 1E-8)
    assertEquals(ml_k2, shape.getK2, 1E-8)
    assertEqualsAngle(ml_phi, shape.getPhi, 1E-8)
    assertEquals(0, shape.getQ0, 1E-16)
    assertEqualsAngle(ml_gamma1, trajectory.gamma, 1E-8)

    val c1 = trajectory.apply(t1)
    val c2 = trajectory.apply(t2)
    assertEquals(r1, c1.position.norm, 1e-16)
    assertEquals(r2, c2.position.norm, 1)
    c1.velocity.x should be (ml_V1.x +- 1E-1)
    c1.velocity.y should be (ml_V1.y +- 1E-1)
    c1.velocity.z should be (ml_V1.z +- 1E-1)
    c2.velocity.x should be (ml_V2.x +- 1E-1)
    c2.velocity.y should be (ml_V2.y +- 1E-1)
    c2.velocity.z should be (ml_V2.z +- 1E-1)
  }

  it should "generate the correct trajectory for n=3" in {
    val k2 = 1.0 / 12.0
    val dt = days(1E-3)
    val N = 3
    val r1 = new PosVel[BodyCenteredSystem](Vec3(2, 0, 0), Vec3.zero, null)
    val r2 = new PosVel[BodyCenteredSystem](Vec3(0.2, -1, 0), Vec3.zero, null)

    val t1 = Epochs.J2000(universe)
    val t2 = Epochs.J2000(universe).addS(dt)

    val frame = BodyCenteredSystem(1E4)
    val exposin = new ExpoSin(r1, r2, t1, t2, frame)
    exposin.N = N
    exposin.assumeK2 = k2

    // Results as computed by the Matlab routine:
    val ml_V1 = Vec3(+4.279665287258510e+001, +5.461017978436156e+001, 0)
    val ml_V2 = Vec3(-8.557155856649688e+001, -4.647847748181721e+001, 0)
    val ml_k0 = 2.494219696832175e-004
    val ml_k1 = 1.300955416481242e+001
    val ml_k2 = 8.333333333333333e-002
    val ml_phi = 7.628619402579907e-001
    val ml_tf = 8.640000000000001e+001
    val ml_N = 3
    val ml_dth = 2.022295668848377e+001
    val ml_gamma1 = 6.647073338793147e-001
    val ml_gamma_m = -1.459915193676833e+000
    val ml_gamma_M = 1.459299027659339e+000

    val solutionSet = exposin.solutionSet
    assertEquals(ml_gamma_m, solutionSet.getDomain.lowerBound, 1E-1)
    assertEquals(ml_gamma_M, solutionSet.getDomain.upperBound, 1E-1)

    val trajectory = exposin.trajectory
    val shape = trajectory.exposin
    assertEquals(ml_k0, shape.getK0, ml_k0 * 1E-2)
    assertEquals(ml_k1, shape.getK1, 1E-2)
    assertEquals(ml_k2, shape.getK2, 1E-8)
    assertEqualsAngle(ml_phi, shape.getPhi, 1E-2)
    assertEquals(0, shape.getQ0, 1E-16)
    assertEqualsAngle(ml_gamma1, trajectory.gamma, 1E-2)

    val c1 = trajectory.apply(t1)
    val c2 = trajectory.apply(t2)
    assertEquals(r1.position.norm, c1.position.norm, 1e-15)
    assertEquals(r2.position.norm, c2.position.norm, 1)
    c1.velocity.x should be (ml_V1.x +- 1E-1)
    c1.velocity.y should be (ml_V1.y +- 1E-1)
    c1.velocity.z should be (ml_V1.z +- 1E-1)
    c2.velocity.x should be (ml_V2.x +- 1E-1)
    c2.velocity.y should be (ml_V2.y +- 1E-1)
    c2.velocity.z should be (ml_V2.z +- 1E-1)
  }
}
