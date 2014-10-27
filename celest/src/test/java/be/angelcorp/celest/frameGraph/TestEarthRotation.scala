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

import be.angelcorp.celest.data.eop.ExcessLengthOfDay
import be.angelcorp.celest.frameGraph.frames.transforms._
import be.angelcorp.celest.math.geometry.Mat3
import be.angelcorp.celest.time.timeStandard.TimeStandards.TT
import be.angelcorp.celest.time.{Epoch, Epochs, JulianDate}
import be.angelcorp.celest.unit.CelestTest
import be.angelcorp.celest.universe.DefaultUniverse
import be.angelcorp.celest.physics.Units._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class TestEarthRotation extends FlatSpec with ShouldMatchers with CelestTest {

  implicit val universe = new DefaultUniverse

  "EarthRotation" should "calculate the same earth rotation angle as SOFA" in {
    val epoch = new JulianDate(2013,  4, 27, 12, 33, 18.1938271, TT)

    EarthRotation.θ_ERA(epoch) should be(0.7597905005561572 +- arcSecond(1E-3))
  }

  "EarthRotationGAST" should "calculate the same angualr values as SOFA" in {
    val epoch = new JulianDate(2013,  4, 27, 12, 33, 18.1938271, TT)
    val t = epoch.inTimeStandard(TT).relativeTo(Epochs.J2000) / 36525.0
    val mockLOD = new ExcessLengthOfDay {
      def lod(epoch: Epoch) = 0.0
    }

    val nutation = new IAU2000Nutation(null, null, IAU2000NutationLoader.MHB2000_2000A, false)
    val transformer = new EarthRotationGAST(null, null, nutation, mockLOD)
    // Equation of the Equinoxes
    transformer.equationOfEquinoxes(t) should be(5.139302755607717E-5 +- arcSecond(1E-3))
    // Greenwich Mean Sidereal Time (GMST)
    transformer.θ_GMST2000(epoch) should be(0.76276902688084813 +- arcSecond(1E-3))
    // Greenwich apparent sidereal time (GAST)
    transformer.θ_GAST2000(epoch) should be(0.7628204199084042 +- arcSecond(1E-3))
  }

  it should "calculate the same transformation matrix as SOFA" in {
    val epoch = new JulianDate(2013,  4, 27, 12, 33, 18.1938271, TT)
    val mockLOD = new ExcessLengthOfDay {
      def lod(epoch: Epoch) = 0.0
    }

    // Transform factory under test
    val nutation = new IAU2000Nutation(null, null, IAU2000NutationLoader.MHB2000_2000A, false)
    val transformer = new EarthRotationGAST(null, null, nutation, mockLOD)
    val transformParams = transformer.transform(epoch).parameters
    val R = transformParams.rotation.toMatrix.mtx

    val sofaMatrix = Mat3(
      0.722890082608463, -0.6909630442117216, 0,
      0.6909630442117216, 0.722890082608463, 0,
      0, 0, 1
    )

    sofaMatrix should be rotation (R +- arcSecond(1E-3))
  }

}
