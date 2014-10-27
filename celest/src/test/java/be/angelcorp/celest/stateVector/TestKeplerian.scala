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
import be.angelcorp.celest.physics.Units._
import org.scalatest.{FlatSpec, Matchers}

class TestKeplerian extends FlatSpec with Matchers with CelestTest {

  val sunFrame = frames.BodyCenteredSystem(new Body[Null]() {
    def μ: Double = 132712440040.944000E9

    def orbit(epoch: Epoch) = ???
  })

  "Keplerian" should "convert the Cartesian elements from/to Keplerian for 2002 TZ300" in {
    /* (2002 TZ300) (TransNeptunian Object) see http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=2002%20TZ300 */
    // mu = 132712440040.944000E9; AU = 149597870700; degree = pi/180
    // [R,V] = randv(43.69 * AU, 0, 3.58*degree, 55.46*degree,334.67*degree, 1.25*degree, mu)
    val k_true = Keplerian(AU(43.69), 0.0, degrees(3.58), degrees(334.67),
      degrees(55.46), TrueAnomaly(degrees(1.25)), sunFrame)

    val pv = PosVel(5.575650800580360E12, 3.406285950357226E12, -0.166516757517559E12,
      -2.339772644074335E3, 3.842463945033625E3, 0.256885464567324E3, sunFrame)

    assertEquals(k_true, Keplerian(pv), AU(43.69 * 1E-10), 1E-15, 1E-15, 0.3, 1E-15, 0.3) // ω + M is correct
    assertEquals(pv, k_true.toPosVel, pv.position.norm * 1E-10, pv.velocity.norm * 1E-10)
  }

  it should "convert the Cartesian elements from/to Keplerian for 2000 QF226" in {
    /* (2000 QF226) (TransNeptunian Object) see http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=2000%20QF226 */
    // mu = 132712440040.944000E9; AU = 149597870700; degree = pi/180
    // [R,V] = randv(46.49 * AU, 0, 2.25*degree, 41.00*degree, 296.90*degree, 5.75*degree, mu)
    val k_true = Keplerian(AU(46.49), 0, degrees(2.25), degrees(296.90),
      degrees(41.00), TrueAnomaly(degrees(5.75)), sunFrame)

    val pv = PosVel(6.670590824266868E12, -1.954399520371328E12, -0.229898414537091E12,
      1.230889276648774E3, 4.190283177829139E3, 0.092524698681849E3, sunFrame)

    assertEquals(k_true, Keplerian(pv), AU(46.49 * 1E-10), 1E-15, 2E-15, 2 * math.Pi, 5E-15, 2 * math.Pi) // ω and M are ill defined for circular orbits
    assertEquals(pv, k_true.toPosVel, pv.position.norm * 1E-10, pv.velocity.norm * 1E-10)
  }

  it should "convert the Cartesian elements from/to Keplerian for the Earth" in {
    val k_true = new Keplerian(149.598261E9, 0.01671123, 0.124878, 1.99330267, 6.08665, 0, sunFrame)

    val pv = PosVel(-0.331617962895771E11, 1.423339778393807E11, 0.167105043530553E11,
      -2.949775938428787E4, -0.669095631311988E4, -0.154682704715132E4, sunFrame)

    assertEquals(k_true, Keplerian(pv), 149.598261E9 * 1E-6, 1E-8, 1E-8, 1E-8, 1E-8, 1E-8)
    assertEquals(pv, k_true.toPosVel, pv.position.norm * 1E-14, pv.velocity.norm * 1E-16)
  }

  it should "convert the Cartesian elements from/to Keplerian for the 2212 Hephaistos" in {
    /* 2212 Hephaistos (1978 SB) (Apollo [NEO]), see http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=2212 */
    val pv = PosVel(-2.973349749957259E10, -4.462450401546170E10, -0.524103964859935E10,
      5.567204076586791E4, -3.568386116822244E4, -1.201095262393990E4, sunFrame)

    val k_true = new Keplerian(AU(2.167001873), 0.8338, degrees(11.74),
      degrees(208.56), degrees(28.27), 0, sunFrame)

    assertEquals(k_true, Keplerian(pv), AU(2.167 * 1E-10), 1E-15, 1E-16, 1E-15, 1E-15, 1E-16)
    assertEquals(pv, k_true.toPosVel, pv.position.norm * 1E-10, pv.velocity.norm * 1E-10)
  }

  it should "convert the Cartesian elements from/to Keplerian for 2000 NL10" in {
    /* 105140 (2000 NL10) (Aten [NEO]), see http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=105140 */
    val pv = PosVel(1.568729987733919E11, 0.236439228464126E11, 0.761836182030944E11,
      -1.634472864086562E4, 1.073863544602837E4, -1.246760056449113E4, sunFrame)
    val k_true = Keplerian(AU(0.9143), 0.8171, degrees(32.52),
      degrees(281.56), degrees(237.44), TrueAnomaly(3.57456), sunFrame)

    assertEquals(k_true, Keplerian(pv), AU(0.9143 * 1E-10), 1E-5, 1E-5, 1E-5, 1E-5, 1E-5)
    assertEquals(pv, k_true.toPosVel, pv.position.norm * 1E-10, pv.velocity.norm * 1E-10)
  }

  it should "convert the Spherical elements from/to Keplerian for the Moon" in {
    // From jpl horizons (http://ssd.jpl.nasa.gov/horizons.cgi)
    // Converted to spherical using Fundamentals of astrodynamics and applications matlab code
    // Moon position:
    // JDCT = 2455562.500000000 = A.D. 2011-Jan-01 00:00:00.0000 (CT)
    // X =-1.947136151107762E+05 Y =-3.249790482942117E+05 Z =-1.934593293850985E+04 [km]
    // VX= 8.680230862574665E-01 VY=-5.629777269508974E-01 VZ= 7.784227958608481E-02 [km/s]
    val jplEarthFrame = frames.BodyCenteredSystem(new Body[Null]() {
      def μ: Double = 398600.440E9

      def orbit(epoch: Epoch) = ???
    })

    val kMoon = Keplerian(3.903213584163071E+08, 4.074916709908236e-002, 9.218093894982124E-2, 4.850831512485626E00,
      4.757761494574442E00, TrueAnomaly(1.079822859502195E00), jplEarthFrame)
    val pv = PosVel(-1.15037391547548e+008, -3.64408052048568e+008, -1.21513857899935e+007,
      +9.67536480042629e+002, -3.46497896859408e+002, +8.78970130662041e+001, jplEarthFrame)

    assertEquals(kMoon, Keplerian(pv), AU(3.903213584163071E+08 * 1E-15), 1E-15, 2E-15, 5E-14, 1E-15, 5E-14)
    assertEquals(pv, kMoon.toPosVel, pv.position.norm * 1E-14, pv.velocity.norm * 1E-14)

    val s = new Spherical(3.823277207168130E+08, -1.876578287892178E00, -3.178799710706510E-2, 1.031461835283901E+03,
      math.Pi / 2.0 - 1.535552685601132E00, 1.484255164403220E00, jplEarthFrame)
    assertEquals(kMoon, Keplerian(s), AU(3.903213584163071E+08 * 1E-15), 1E-15, 2E-15, 5E-14, 1E-15, 5E-14)
  }

}
