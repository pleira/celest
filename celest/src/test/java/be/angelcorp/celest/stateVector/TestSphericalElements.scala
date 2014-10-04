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
package be.angelcorp.celest.stateVector

import be.angelcorp.celest.body.Body
import be.angelcorp.celest.frameGraph.frames
import be.angelcorp.celest.state.{TrueAnomaly, _}
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.unit.CelestTest
import org.scalatest.{FlatSpec, Matchers}

import scala.math._

class TestSphericalElements extends FlatSpec with Matchers with CelestTest {

  val jplEarthFrame = frames.BodyCenteredSystem(new Body[Null]() {
    def μ: Double = 398600.440E9

    def orbit(epoch: Epoch) = ???
  })

  "Spherical" should "convert the Cartesian elements from/to Spherical for geostationairy orbits" in {
    /* Classical geo orbit */
    val rGEO = pow(jplEarthFrame.centerBody.μ / pow((2.0 * Pi) / (3600.0 * 24.0), 2), 1.0 / 3.0)
    val vc = sqrt(jplEarthFrame.centerBody.μ / rGEO)

    val pv = PosVel(rGEO, 0, 0, 0, vc, 0, jplEarthFrame)

    val s_expected = new Spherical(rGEO, 0, 0, vc, 0, Pi / 2.0, jplEarthFrame)
    val s_actual = Spherical(pv)

    assertEquals(s_expected, s_actual, rGEO * 1E-16, 1E-16, 1E-16, vc * 1E-16, 1E-16, 1E-16)
    assertEquals(pv, s_expected.toPosVel, pv.position.norm * 1E-16, pv.velocity.norm * 1E-16)
  }

  "Spherical" should "convert the Keplerian elements from/to Spherical for the lunar position" in {
    // The moon
    // From JPL horizons (http://ssd.jpl.nasa.gov/horizons.cgi)
    // Converted to spherical using Fundamentals of astrodynamics and applications matlab code
    // Moon position:
    // JDCT = 2455562.500000000 = A.D. 2011-Jan-01 00:00:00.0000 (CT)
    // X =-1.947136151107762E+05 Y =-3.249790482942117E+05 Z =-1.934593293850985E+04 [km]
    // VX= 8.680230862574665E-01 VY=-5.629777269508974E-01 VZ= 7.784227958608481E-02 [km/s]
    val k = Keplerian(3.903213584163071E+08, 4.074916709908236E-002, 9.218093894982124E-2,
      4.850831512485626E+00, 4.757761494574442E+00, TrueAnomaly(1.079822859502195E00), jplEarthFrame)
    val s_expected = new Spherical(3.823277207168130E+08, -1.876578287892178E00, -3.178799710706510E-2,
      1.0315839033233294E+03, Pi / 2.0 - 1.535552685601132E00, 1.484255164403220E00, jplEarthFrame)

    assertEquals(s_expected, Spherical(k), 3.823277207168130E+08 * 1E-16, 1E-15, 5E-16, 0.2, 1E-16, 1E-15)
  }

  "Spherical" should "return itself when cloned" in {
    val s_expected = new Spherical(3.823277207168130E+08, -1.876578287892178E00, -3.178799710706510E-2,
      1.031461835283901E+03, Pi / 2.0 - 1.535552685601132E00, 1.484255164403220E00, jplEarthFrame)
    assertEquals(s_expected, Spherical(s_expected), 3.823277207168130E+08 * 1E-16, 1E-16, 1E-16, 1.0315839033233294E+03 * 1E-16, 1E-16, 1E-16)
  }

}
