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
package be.angelcorp.celest.kepler

import be.angelcorp.celest.body.Satellite
import be.angelcorp.celest.constants.Constants._
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem
import be.angelcorp.celest.kepler
import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.math._
import be.angelcorp.celest.state.PosVel
import be.angelcorp.celest.unit.CelestTest
import be.angelcorp.celest.universe.DefaultUniverse
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.math._


class TestKeplerEquations extends FlatSpec with ShouldMatchers with CelestTest {

  implicit val universe = new DefaultUniverse

  /** **********************************
    * Non-Static function tests
    * ***********************************/

  "KeplerEquations" should "evaluate the elliptical/parabolic/hyperbolic anomaly correctly from e/M" in {
    // Delegates calls, several samples:
    // Test based on validated samples in ReferenceKeplerAngles class
    ReferenceKeplerAngles.all.foreach({
      sample =>
        val k = ReferenceKeplerAngles.createKeplerElements(sample)
        val anomaly = k.anomaly

        expect(true, "Anomaly not correct for entry %s, computed %f".format(sample, anomaly))(
          ReferenceKeplerAngles.checkEqualAnomaly(sample, anomaly)
        )
    })
  }

  it should "evaluate the true anomay correctly from e/M" in {
    // Delegates calls, several samples:
    // Test based on validated samples in ReferenceKeplerAngles class
    ReferenceKeplerAngles.all.foreach({
      sample =>
        val k = ReferenceKeplerAngles.createKeplerElements(sample)
        val anomaly = k.trueAnomaly

        expect(true, "Mean anomaly not correct for entry %s, computed %f".format(sample, anomaly))(
          ReferenceKeplerAngles.checkEqualTrueAnomaly(sample, anomaly)
        )
    })
  }

  /** **********************************
    * Static function tests
    * ***********************************/

  //it should "compute the correct arguement of latitude" in {
  // public static double arguementOfLatitude(double w, double nu)
  // public static double arguementOfLatitude(Vec3 nodalVector, Vec3 radius)
  // TODO: Add tests
  //}

  it should "compute the correct cartesian to kepler conversion" in {
    val frame = new BodyCenteredSystem {
      val centerBody = new Satellite(5.9736E24, null)
    }
    val c = new PosVel(
      Vec3(10157768.1264, -6475997.0091, 2421205.9518),
      Vec3(1099.2953996, 3455.105924, 4355.0978095),
      frame
    )

    val k = kepler.cartesian2kepler(c, frame.centerBody.μ)
    val expected = Array[Double](1.216495E7, 0.01404, 0.919398, 2.656017, 5.561776, 3.880560)
    val tol = expected.map(_ * 1E-3)

    expected(0) should be (k._1 +- tol(0))
    expected(1) should be (k._2 +- tol(1))
    expected(2) should be (k._3 +- tol(2))
    expected(3) should be (k._4 +- tol(3))
    expected(4) should be (k._5 +- tol(4))
    expected(5) should be (k._6 +- tol(5))
  }

  it should "compute the correct cartesian to kepler (2d) conversion" in {
    val frame = new BodyCenteredSystem {
      val centerBody = new Satellite(5.9736E24, null)
    }
    val c = new PosVel(
      Vec3(10157768.1264, -6475997.0091, 2421205.9518),
      Vec3(1099.2953996, 3455.105924, 4355.0978095),
      frame
    )

    val k = kepler.cartesian2kepler2D(c, frame.centerBody.μ)

    k._1 should be(1.216495E7 +- 1E4)
    k._2 should be(0.01404 +- 1E-5)
    k._3 should be(3.88056 +- 1E-2)
  }

  it should "compute the correct eccentricity from Ra and Rp" in {
    // eccentricity(double, double)
    kepler.eccentricity(1, 1) should be(0.0 +- 1E-16)

    // Earth to mars orbit perihelion is the Earth's orbital radius, aphelion is the radius of Mars
    val Rp = 1.495978E11
    val Ra = 2.27987047E11
    kepler.eccentricity(Rp, Ra) should be(0.207606972 +- 1E-9)
  }

  it should "compute the correct flight path angle" in {
    // http://www.wolframalpha.com/input/?i=mean+anomaly+5+radian+eccentricity+0.6
    val gamma = mod(kepler.flightPathAngle(0.6, -2.427), 2 * Pi)
    gamma should be(5.6597 +- 1E-4)
  }

  it should "compute the kepler elements for cartesian elements" in {
    // Values from keplerCOE from Matlab Orbital_Library by Richard Rieber
    val c = kepler.kepler2cartesian(1.216495E7, 0.01404, 0.919398, 2.656017, 5.561776, 3.880560, mass2mu(5.9736E24))
    val c_true = new PosVel(
      Vec3(1.092882447232868e+007, -5.619415989750504e+006, -1.715953308630781e+005),
      Vec3(1.466941526515634e+003, +3.108913288555892e+003, -4.504368922790057e+003),
      null
    )

    assertEquals( new PosVel(c._1, c._2, null ), c_true, 1E-5, 1E-9)
  }

}
