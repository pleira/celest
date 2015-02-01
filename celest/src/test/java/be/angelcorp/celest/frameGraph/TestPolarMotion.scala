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

import be.angelcorp.celest.data.eop.PoleProvider
import be.angelcorp.celest.frameGraph.frames.transforms.PolarMotion
import be.angelcorp.celest.math.geometry.Mat3
import be.angelcorp.celest.physics.Units._
import be.angelcorp.celest.time.timeStandard.TimeStandards.TT
import be.angelcorp.celest.time.{Epoch, JulianDate}
import be.angelcorp.celest.unit.CelestTest
import be.angelcorp.celest.universe.DefaultUniverse
import org.scalatest.{FlatSpec, Matchers}

/**
 * The numerical values in these test cases where obtained from SOFA using the Microsoft visual studio 2012 debugger,
 * when running the following c++ snippet:
 * <pre>
 * double epoch_jd, epoch_fraction; // UTC
 * iauDtf2d("TT", 2013, 04, 27, 12, 33, 18.1938271, &epoch_jd, &epoch_fraction);
 * std::cout << "Epcoh: 2013/04/27 12h33m18.1938271s TT = " << (epoch_fraction + epoch_jd) << "jd TT"<< std::endl;
 *
 * double W[3][3];
 * double xp = 100 * DMAS2R; // = 100 [mas]
 * double yp = 200 * DMAS2R; // = 200 [mas]
 * iauPom00( xp, yp, 0.0, W );
 * </pre>
 */
class TestPolarMotion extends FlatSpec with Matchers with CelestTest {

  implicit val universe = new DefaultUniverse

  "J2000FrameBias" should "conform to the sofa library" in {
    val epoch = new JulianDate(2013,  4, 27, 12, 33, 18.1938271, TT)

    val sofaRotation = Mat3(
      0.9999999999998824, 0, 4.848136811095171e-007,
      4.700886107818658e-013, 0.9999999999995299, -9.696273622188061e-007,
      -4.848136811092892e-007, 9.696273622189201e-007, 0.9999999999994124
    )

    val pole = new PoleProvider {
      val xp = arcSeconds(100E-3)
      // = 100 mas
      val yp = arcSeconds(200E-3)

      // = 200 mas
      def polarCoordinatesOn(epoch: Epoch): (Double, Double) = (xp, yp)
    }
    val polarMotion = new PolarMotion(null, null, pole)
    val transform = polarMotion.transform(epoch)

    transform.M should be rotation (sofaRotation +- arcSeconds(1E-6))
  }


}
