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
package be.angelcorp.celest.unit

import be.angelcorp.celest.math.geometry.Mat3
import be.angelcorp.celest.math.rotation.{RotationMatrix, Rotation, Quaternion}
import be.angelcorp.celest.state.{Keplerian, PosVel, Spherical}
import org.apache.commons.math3.linear.RealVector
import org.apache.commons.math3.stat.descriptive.SummaryStatistics
import org.scalactic.{TripleEqualsSupport, Prettifier}
import org.scalatest.matchers.{BePropertyMatcher, BeMatcher, MatchResult, Matcher}
import org.scalatest.{Assertions, Matchers}

trait CelestTest extends Assertions with Matchers with MtxMatchers with RotationMatchers {

  /**
     * Check if two angles are equal within the given range. Note this will test 2*pi and 0 as equal (or
     * any value close to it). This means that the actual angles are compared, and not the numeric values
     * ( since 2*pi and 0 are not within linear tolerance, but the domain folds back).
     *
     * @param a   Angle 1 [rad]
     * @param b   Angle 2 [rad]
     * @param tol Tolerance to which the angles need to be equal [rad]
     */
    def assertEqualsAngle(a: Double, b: Double, tol: Double) {
        assertEqualsAngle("Angle not equal <" + a + "> and <" + b + "> with <" + tol + ">", a, b, tol)
    }

    /**
     * Check if two angles are equal within the given range. Note this will test 2*pi and 0 as equal (or
     * any value close to it)
     * <p/>
     * <p>
     * This is a computational intensive test, it uses 10 Math calls, with 8 of these trigonometric
     * functions
     * </p>
     *
     * @param message Failiure message
     * @param a       Angle 1
     * @param b       Angle 2
     * @param tol     Tolerance to which the angles need to be equal
     */
    def assertEqualsAngle(message: String, a: Double, b: Double, tol: Double) =
      angleEquals(a, b, tol)

    /**
     * Get statistics on the element wise error vector between two vectors
     * <p>
     * error = v2 - v1
     * </p>
     *
     * @param v1
     * @param v2
     * @return
     */
    def getStatistics(v1: Array[Double], v2: Array[Double]): SummaryStatistics = {
        val stats = new SummaryStatistics()
        for (i <- 0 until v1.length)
            stats.addValue(v2(i) - v1(i))
        stats
    }

    /**
     * @see CelestTest#getStatistics(double[], double[])
     */
    def getStatistics(v1: RealVector, v2: RealVector): SummaryStatistics =
        getStatistics(v1.toArray, v2.toArray)

    def angleEquals(expected: Double, actual: Double, tolerance: Double) {
      val difference = Math.abs((expected - actual + Math.PI) % (2 * Math.PI) - Math.PI)
      assert( difference < tolerance, f"Angle not equal. Expected $expected%.16f != $actual%.16f actual, difference $difference%e > tolerance $tolerance%e")
    }

    def assertEquals(expected: PosVel[_], actual: PosVel[_], positionError: Double, velocityError: Double) {
      assertEquals(expected.position.norm, actual.position.norm, positionError)
      assertEquals(expected.position.x, actual.position.x, positionError)
      assertEquals(expected.position.y, actual.position.y, positionError)
      assertEquals(expected.position.z, actual.position.z, positionError)

      assertEquals(expected.velocity.norm, actual.velocity.norm, velocityError)
      assertEquals(expected.velocity.x, actual.velocity.x, velocityError)
      assertEquals(expected.velocity.y, actual.velocity.y, velocityError)
      assertEquals(expected.velocity.z, actual.velocity.z, velocityError)
    }

    def assertEquals(expected: Keplerian[_], actual: Keplerian[_],
                     deltaSemiMajorAxis: Double, deltaEccentricity: Double, deltaInclination: Double,
                     deltaPericenter: Double, deltaRightAscension: Double, deltaMeanAnomaly: Double) {
        assertEquals(expected.semiMajorAxis, actual.semiMajorAxis, deltaSemiMajorAxis)
        assertEquals(expected.eccentricity, actual.eccentricity, deltaEccentricity)
        angleEquals(expected.inclination, actual.inclination, deltaInclination)
        angleEquals(expected.argumentOfPeriapsis, actual.argumentOfPeriapsis, deltaPericenter)
        angleEquals(expected.rightAscension, actual.rightAscension, deltaRightAscension)
        angleEquals(expected.meanAnomaly, actual.meanAnomaly, deltaMeanAnomaly)
    }

    def assertEquals(expected: Spherical[_], actual: Spherical[_],
                     deltaRadius: Double, deltaRightAscension: Double, deltaDeclination: Double,
                     deltaVelocity: Double, deltaFlightPathAngle: Double, deltaFlightPathAzimuth: Double) {
        assertEquals(expected.radius, actual.radius, deltaRadius)
        angleEquals(expected.rightAscension, actual.rightAscension, deltaRightAscension)
        angleEquals(expected.declination, actual.declination, deltaDeclination)
        assertEquals(expected.velocity, actual.velocity, deltaVelocity)
        angleEquals(expected.flightPathAngle, actual.flightPathAngle, deltaFlightPathAngle)
        angleEquals(expected.flightPathAzimuth, actual.flightPathAzimuth, deltaFlightPathAzimuth)
    }

  def assertEquals( expected: Double, real: Double, tol: Double ) =
    real should be ( expected +- tol )
}
