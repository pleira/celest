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

package be.angelcorp.celest.frameGraph

import be.angelcorp.celest.frameGraph.frames.transforms._
import be.angelcorp.celest.math.geometry.Mat3
import be.angelcorp.celest.time.JulianDate
import be.angelcorp.celest.time.timeStandard.TimeStandards.TT
import be.angelcorp.celest.unit.CelestTest
import be.angelcorp.celest.universe.DefaultUniverse
import be.angelcorp.celest.physics.Units._
import org.scalatest.{FlatSpec, Matchers}

class TestIAU2000Nutation extends FlatSpec with Matchers with CelestTest {

  implicit val universe = new DefaultUniverse

  "IAU2000Nutation" should "match the IAU2000A SOFA values" in {
    val epoch = new JulianDate(2013,  4, 27, 12, 33, 18.1938271, TT)

    val sofaCoefficients = IAU2000NutationLoader.MHB2000_2000A
    val iau2000a = new IAU2000Nutation(null, null, sofaCoefficients, false)

    val (δψ, δε) = iau2000a.nutationParameters(epoch)
    arcSecond(δψ) should be(5.602459903893027e-005 +- arcSecond(1E-3))
    arcSecond(δε) should be(-2.969698055733905e-005 +- arcSecond(1E-3))

    val transform = iau2000a.transform(epoch)
    val sofaRotation = Mat3(
      0.9999999984306222, -5.14022386766078E-5, -2.228375087865647E-5,
      5.14029004140582E-5, 0.9999999982379326, 2.969640782823912E-5,
      2.228222437754798E-5, -2.969755323106726E-5, 0.9999999993107789)

    transform.M should be rotation (sofaRotation +- arcSecond(1E-3))
  }

  it should "match the IAU2006A SOFA values" in {
    val epoch = new JulianDate(2013,  4, 27, 12, 33, 18.1938271, TT)

    val sofaCoefficients = IAU2000NutationLoader.MHB2000_2000A
    val iau2000a = new IAU2000Nutation(null, null, sofaCoefficients, true)

    val (δψ, δε) = iau2000a.nutationParameters(epoch)
    arcSecond(δψ) should be(+5.6024604627857253e-005 +- arcSecond(1E-3))
    arcSecond(δε) should be(-2.9696969571191937e-005 +- arcSecond(1E-3))

    val transform = iau2000a.transform(epoch)
    val sofaRotation = Mat3(
      0.99999999843062182, -5.1402248291095109E-5, -2.2283742752159111E-5,
      5.1402910028059379E-5, 0.99999999823793240, 2.9696396842193717E-5,
      2.2282216251329819E-5, -2.9697542244855324E-5, 0.99999999931077943
    )

    transform.M should be rotation (sofaRotation +- arcSecond(1E-3))
  }

}
