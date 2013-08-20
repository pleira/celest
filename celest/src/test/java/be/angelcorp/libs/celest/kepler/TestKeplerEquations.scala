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
package be.angelcorp.libs.celest.kepler

import scala.math._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import be.angelcorp.libs.celest.constants.Constants._
import be.angelcorp.libs.celest.kepler
import be.angelcorp.libs.math.linear.Vector3D
import be.angelcorp.libs.celest.unit.CelestTest._
import be.angelcorp.libs.celest.body.{ICelestialBody, CelestialBody}
import be.angelcorp.libs.math.MathUtils2._
import be.angelcorp.libs.celest.unit.CelestTest
import be.angelcorp.libs.celest.state.PosVel
import be.angelcorp.libs.celest.frames


@RunWith(classOf[JUnitRunner])
class TestKeplerEquations extends FlatSpec with ShouldMatchers {

	/************************************
	 * Non-Static function tests
	 ************************************/

  "KeplerEquations" should "evaluate the elliptical/parabolic/hyperbolic anomaly correctly from e/M" in {
		// Delegates calls, several samples:
		// Test based on validated samples in ReferenceKeplerAngles class
    ReferenceKeplerAngles.all.foreach( { sample =>
      val k       = ReferenceKeplerAngles.createKeplerElements(sample)
      val anomaly = k.anomaly

      expect(true, "Anomaly not correct for entry %s, computed %f".format(sample, anomaly))(
        ReferenceKeplerAngles.checkEqualAnomaly(sample, anomaly)
      )
    } )
	}

	it should "evaluate the true anomay correctly from e/M" in {
		// Delegates calls, several samples:
		// Test based on validated samples in ReferenceKeplerAngles class
    ReferenceKeplerAngles.all.foreach( { sample =>
      val k       = ReferenceKeplerAngles.createKeplerElements(sample)
      val anomaly = k.trueAnomaly

      expect(true, "Mean anomaly not correct for entry %s, computed %f".format(sample, anomaly))(
        ReferenceKeplerAngles.checkEqualTrueAnomaly(sample, anomaly)
      )
    } )
	}

	/************************************
	 * Static function tests
	 ************************************/

  //it should "compute the correct arguement of latitude" in {
  // public static double arguementOfLatitude(double w, double nu)
  // public static double arguementOfLatitude(Vector3D nodalVector, Vector3D radius)
  // TODO: Add tests
	//}

	it should "compute the correct cartesian to kepler conversion" in {
		val frame = new frames.BodyCentered{
      val centerBody = new CelestialBody(PosVel(), 5.9736E24)
    }
    val c = new PosVel(
				Vector3D(10157768.1264, -6475997.0091, 2421205.9518),
				Vector3D(1099.2953996,   3455.105924,  4355.0978095),
        Some(frame)
    )

    val k = kepler.cartesian2kepler(c, frame.centerBody.getMu )
		val expected = Array[Double](1.216495E7, 0.01404, 0.919398, 2.656017, 5.561776, 3.880560)
    val tol      = expected.map( _ * 1E-3 )

    assertEquals(expected, Array[Double](k._1, k._2, k._3, k._4, k._5, k._6), tol)
	}

  it should "compute the correct cartesian to kepler (2d) conversion" in {
    val frame = new frames.BodyCentered{
      val centerBody = new CelestialBody(PosVel(), 5.9736E24)
    }
		val c = new PosVel(
				Vector3D(10157768.1264, -6475997.0091, 2421205.9518),
				Vector3D(1099.2953996,   3455.105924,  4355.0978095),
        Some(frame)
    )

		val k = kepler.cartesian2kepler2D(c, frame.centerBody.getMu)

    k._1 should be (1.216495E7 plusOrMinus 1E4 )
		k._2 should be (0.01404    plusOrMinus 1E-5)
		k._3 should be (3.88056    plusOrMinus 1E-2)
	}

  it should "compute the correct eccentricity from Ra and Rp" in {
		// eccentricity(double, double)
		kepler.eccentricity(1,1) should be (0.0 plusOrMinus 1E-16)

    // Earth to mars orbit perihelion is the Earth's orbital radius, aphelion is the radius of Mars
    val Rp = 1.495978E11
    val Ra = 2.27987047E11
    kepler.eccentricity(Rp, Ra) should be (0.207606972 plusOrMinus 1E-9)
	}

  it should "compute the correct flight path angle" in {
		// http://www.wolframalpha.com/input/?i=mean+anomaly+5+radian+eccentricity+0.6
		val gamma = mod(kepler.flightPathAngle(0.6, -2.427), 2 * Pi)
		gamma should be (5.6597 plusOrMinus 1E-4)
	}

  it should "compute the kepler elements for cartesian elements" in {
    // Values from keplerCOE from Matlab Orbital_Library by Richard Rieber
		val c = kepler.kepler2cartesian( 1.216495E7, 0.01404, 0.919398, 2.656017, 5.561776, 3.880560, mass2mu(5.9736E24))
		val c_true = new PosVel(
				Vector3D(1.092882447232868e+007, -5.619415989750504e+006, -1.715953308630781e+005),
				Vector3D(1.466941526515634e+003, +3.108913288555892e+003, -4.504368922790057e+003)
    )

    CelestTest.assertEquals( c._1.toArray, c_true.position.toArray, c_true.position.toArray.map( v => abs( v * 1E-12 ) ) )
    CelestTest.assertEquals( c._2.toArray, c_true.velocity.toArray, c_true.velocity.toArray.map( v => abs( v * 1E-12 ) ) )
	}

}
