/**
 * Copyright (C) 2012 Simon Billemont <simon@angelcorp.be>
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

import be.angelcorp.celest.frameGraph.frames.HeliosphericCoordinateFrames
import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.state.PosVel
import be.angelcorp.celest.time.JulianDate
import be.angelcorp.celest.time.timeStandard.TimeStandards.TT
import be.angelcorp.celest.unit.CelestTest
import be.angelcorp.celest.universe.DefaultUniverse
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers


class TestHeliosphericCoordinateFrames extends FlatSpec with CelestTest {

  implicit val universe = new DefaultUniverse()
  val transforms = new HeliosphericCoordinateFrames()
  /**
   * This is a numerical example for the transformations described in
   * Fraenz/Harper, 'Heliospheric Coordinate Systems' and verifies based on Tables 8 and 9 of the paper.
   */
  val referenceEpoch = new JulianDate(1996, 28, 8, 16, 46,  0, TT)
  val Re = 6378.14E3
  val referenceDistances = Map(
    //(transforms.GEO_T(), 			Vec3( 6.9027400, -1.6362400,  1.9166900)),
    (transforms.GEI_D_TRUE(), Vec3(-5.7864335, -4.1039357, 1.9166900)),
    (transforms.GEI_D_MEAN(), Vec3(-5.7864918, -4.1039136, 1.9165612)),
    (transforms.HAE_D(), Vec3(-5.7864918, -3.0028771, 3.3908764)),
    (transforms.HAE_J2000(), Vec3(-5.7840451, -3.0076174, 3.3908496)),
    (transforms.GEI_J2000(), Vec3(-5.7840451, -4.1082375, 1.9146822))
    //(transforms.HGC_J2000(), 	Vec3(-5.4328785,  4.1138243,  2.7493786)),
    //(transforms.HEE_D(), 			Vec3(-4.0378470, -5.1182566,  3.3908764)),
    //(transforms.HEEQ_D(), 		Vec3(-4.4132668, -5.1924440,  2.7496187)),
    //(transforms.HCD(), 				Vec3(-4.3379628,  5.2555187,  2.7496187)),
    //(transforms.GSE_D(), 			Vec3( 4.0378470,  5.1182566,  3.3908764)),
    //(transforms.GSM_D(), 			Vec3( 4.0378470,  6.0071917,  1.2681645)),
    //(transforms.SM_D(), 			Vec3( 3.3601371,  6.0071917,  2.5733108)),
    //(transforms.MAG_D(), 			Vec3( 3.3344557,  6.0215108,  2.5732497)),
    //(transforms.HGRTN_E(), 		Vec3( 4.0360303,  5.1931904, -3.2771992))
  )

  "GEI_J2000" should "transform to GEI_D_MEAN" in {
    val pos_calc = transforms.transform_GEI_J2000_To_GEI_D_MEAN.transform(referenceEpoch).transform(
      new PosVel(referenceDistances.get(transforms.GEI_J2000()).get * Re, Vec3.zero, null)
    )
    val pos_true = referenceDistances.get(transforms.GEI_D_MEAN()).get * Re
    pos_calc.toPosVel.position should be (pos_true +- (5E-3 * Re))
  }

  "GEI_D_MEAN" should "transform to GEI_D_TRUE" in {
    val pos_calc = transforms.transform_GEI_D_MEAN_To_GEI_D_TRUE.transform(referenceEpoch).transform(
      new PosVel(referenceDistances.get(transforms.GEI_D_MEAN()).get * Re, Vec3.zero, null)
    )
    val pos_true = referenceDistances.get(transforms.GEI_D_TRUE()).get * Re
    pos_calc.toPosVel.position should be (pos_true +- (1E-3 * Re))
  }

  "GEI_J2000" should "transform to HAE_J2000" in {
    val pos_calc = transforms.transform_GEI_J2000_To_HAE_J2000.transform(referenceEpoch).transform(
      new PosVel(referenceDistances.get(transforms.GEI_J2000()).get * Re, Vec3.zero, null)
    )
    val pos_true = referenceDistances.get(transforms.HAE_J2000()).get * Re
    pos_calc.toPosVel.position should be (pos_true +- (1E-6 * Re))
  }

  "HAE_J2000" should "transform to HAE_D" in {
    val pos_calc = transforms.transform_HAE_J2000_To_HAE_D.transform(referenceEpoch).transform(
      new PosVel(referenceDistances.get(transforms.HAE_J2000()).get * Re, Vec3.zero, null)
    )
    val pos_true = referenceDistances.get(transforms.HAE_D()).get * Re
    pos_calc.toPosVel.position should be (pos_true +- (5E-3 * Re))
  }

  "GEI_D_MEAN" should "transform to HAE_D" in {
    val pos_calc = transforms.transform_GEI_D_MEAN_To_HAE_D.transform(referenceEpoch).transform(
      new PosVel(referenceDistances.get(transforms.GEI_D_MEAN()).get * Re, Vec3.zero, null)
    )
    val pos_true = referenceDistances.get(transforms.HAE_D()).get * Re
    pos_calc.toPosVel.position should be (pos_true +- (1E-3 * Re))
  }

}
