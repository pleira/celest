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
package be.angelcorp.celest.maneuvers.targeters.exposin

import be.angelcorp.celest.constants.SolarConstants
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem
import be.angelcorp.celest.physics.Units
import be.angelcorp.celest.time.Epochs
import be.angelcorp.celest.unit.CelestTest
import be.angelcorp.celest.universe.DefaultUniverse
import org.scalatest.{FlatSpec, Matchers}

import scala.math._

class TestExpoSinSolution extends FlatSpec with Matchers with CelestTest {

  implicit val universe = new DefaultUniverse()

  /**
   * <p>
   * Source of the data:<br/>
   * <b>Tatiana Paulino, "Analytical representations of low-thrust trajectories", Master's Thesis,
   * Delft University of Technology, 2008.</b><br/>
   * Table 9.1 & 9.2
   * </p>
   */
  "ExpoSinSolutionSet" should "generate results conform to test case 1" in {
    // INPUT scenario 1
    val r1 = 151366683.169E3
    val r2 = 206953872.627E3
    val k2 = 0.7013
    val gamma = -0.03858
    val dTheta = 1.9532 + 0 * (2 * Pi)

    // EXPECTED OUTPUT
    val R_tol = 1E1
    // No date is given so I cannot verify the hyperbolic excess velocities (relative to the planet)
    // val V1 = 4.2057E3 + velocity of the Earth?
    // val V2 = 7.6969E3 + velocity of the Mars?
    // val V_tol = 1E-1;
    val tof = Units.day((123.4091 + 126.2335) / 2)
    val tof_tol = Units.day(2)
    // val fuel = 27.5688 or 27.1462

    val solutionset = new ExpoSinSolutionSet(r1, r2, k2, dTheta, SolarConstants.mu)

    val calculated_tof = solutionset.value(gamma)
    calculated_tof should equal(tof +- tof_tol)

    val t1 = Epochs.J2000
    val t2 = Epochs.J2000.addS(calculated_tof)
    val solution = solutionset.getExpoSin(gamma)
    val trajectory = new ExpoSinTrajectory(solution, BodyCenteredSystem(SolarConstants.body), t1)

    val c1 = trajectory.apply(t1)
    val c2 = trajectory.apply(t2)
    // Equal R
    assertEqualsAngle(dTheta, c1.position.angle(c2.position), 1e-3)
    c1.position.norm should equal(r1 +- R_tol)
    c2.position.norm should equal(r2 +- R_tol)
    // Equal V
    // assertEquals(V1, c1.getV().getNorm(), V_tol);
    // assertEquals(V2, c2.getV().getNorm(), V_tol);
  }

  /**
   * <p>
   * Source of the data:<br/>
   * <b>Tatiana Paulino, "Analytical representations of low-thrust trajectories", Master's Thesis,
   * Delft University of Technology, 2008.</b><br/>
   * Table 9.1 & 9.2
   * </p>
   */
  it should "generate results conform to test case 2" in {
    // INPUT scenario 1
    val r1 = 150950940.668E3
    val r2 = 207035807.816E3
    val k2 = 0.3192
    val gamma = 0.02342
    val dTheta = 1.7915 + 1 * (2 * Pi)

    // EXPECTED OUTPUT
    val R_tol = 1E1
    // No date is given so I cannot verify the hyperbolic excess velocities (relative to the planet)
    // val V1 = 0.7339E3 + velocity of the Earth?
    // val V2 = 0.2569E3 + velocity of the Mars?
    // val V_tol = 1E-1;
    val tof = Units.day((624.0333 + 624.3738) / 2)
    val tof_tol = Units.day(0.5)
    // val fuel = 132.3082 or 124.1012

    val solutionset = new ExpoSinSolutionSet(r1, r2, k2, dTheta, SolarConstants.mu)

    val calculated_tof = solutionset.value(gamma)
    calculated_tof should equal(tof +- tof_tol)

    val t1 = Epochs.J2000
    val t2 = Epochs.J2000.addS(calculated_tof)
    val solution = solutionset.getExpoSin(gamma)
    val trajectory = new ExpoSinTrajectory(solution, BodyCenteredSystem(SolarConstants.body), t1)

    val c1 = trajectory.apply(t1)
    val c2 = trajectory.apply(t2)

    // Equal R
    assertEqualsAngle(dTheta, c1.position.angle(c2.position), 1e-3)
    c1.position.norm should equal(r1 +- R_tol)
    c2.position.norm should equal(r2 +- R_tol)
    // Equal V
    // assertEquals(V1, c1.getV().getNorm(), V_tol);
    // assertEquals(V2, c2.getV().getNorm(), V_tol);
  }

  /**
   * <p>
   * Source of the data:<br/>
   * <b>Tatiana Paulino, "Analytical representations of low-thrust trajectories", Master's Thesis,
   * Delft University of Technology, 2008.</b><br/>
   * Table 9.1 & 9.2
   * </p>
   */
  it should "generate results conform to test case 3" in {
    // INPUT scenario 1
    val r1 = 147943444.631E3
    val r2 = 222257727.478E3
    val k2 = 0.1524
    val gamma = 0.01048
    val dTheta = 0.0419 + 3 * (2 * Pi)

    // EXPECTED OUTPUT
    val R_tol = 1E1
    // No date is given so I cannot verify the hyperbolic excess velocities (relative to the planet)
    // val V1 = 0.3218E3 + velocity of the Earth?
    // val V2 = 0.0818E3 + velocity of the Mars?
    // val V_tol = 1E-1;
    val tof = Units.day((1552.3217 + 1552.6437) / 2)
    val tof_tol = Units.day(0.5)
    // double fuel = 183.4770 or 167.8474

    val solutionset = new ExpoSinSolutionSet(r1, r2, k2, dTheta, SolarConstants.mu)

    val calculated_tof = solutionset.value(gamma)
    calculated_tof should equal(tof +- tof_tol)

    val t1 = Epochs.J2000
    val t2 = Epochs.J2000.addS(calculated_tof)
    val solution = solutionset.getExpoSin(gamma)
    val trajectory = new ExpoSinTrajectory(solution, BodyCenteredSystem(SolarConstants.body), t1)

    val c1 = trajectory.apply(t1)
    val c2 = trajectory.apply(t2)

    // Equal R
    assertEqualsAngle(dTheta, c1.position.angle(c2.position), 1e-3)
    c1.position.norm should equal(r1 +- R_tol)
    c2.position.norm should equal(r2 +- R_tol)
    // Equal V
    // assertEquals(V1, c1.getV().getNorm(), V_tol);
    // assertEquals(V2, c2.getV().getNorm(), V_tol);
  }

}
