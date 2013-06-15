/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
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

package be.angelcorp.libs.celest.frames

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import be.angelcorp.libs.celest.universe.DefaultUniverse
import be.angelcorp.libs.celest.time.JulianDate
import be.angelcorp.libs.math.linear.Matrix3D
import be.angelcorp.libs.celest.frames.implementations.transforms.J2000FrameBias
import be.angelcorp.libs.celest.unit.CelestTest
import be.angelcorp.libs.util.physics.Angle._

/**
 * The numerical values in these test cases where obtained from SOFA using the Microsoft visual studio 2012 debugger,
 * when running the following c++ snippet:
 * <pre>
 *  double epoch_jd, epoch_fraction; // UTC
 *  iauDtf2d("TT", 2013, 04, 27, 12, 33, 18.1938271, &epoch_jd, &epoch_fraction);
 *  std::cout << "Epcoh: 2013/04/27 12h33m18.1938271s TT = " << (epoch_fraction + epoch_jd) << "jd TT"<< std::endl;
 *
 *  double dpsi, deps;
 *  iauNut00a(epoch_jd, epoch_fraction, &dpsi, &deps);
 *  double N[3][3];
 *  iauNum00a(epoch_jd, epoch_fraction, N);
 * </pre>
 */
@RunWith(classOf[JUnitRunner])
class TestJ2000FrameBias extends FlatSpec with ShouldMatchers {

  implicit val universe = new DefaultUniverse

  "J2000FrameBias" should "conform to the sofa library" in {
    val epoch = new JulianDate(2013, 04, 27, 12, 33, 18.1938271, universe.TT)

    val sofaRotation = Matrix3D(
     0.9999999999999942,   -7.078279744199198E-8, 8.056217146976134E-8,
     7.078279477857338E-8,  0.9999999999999969,   3.306041454222136E-8,
    -8.056217380986972E-8, -3.306040883980552E-8, 0.9999999999999962
    )

    val j2000bias = new J2000FrameBias()
    val transform = j2000bias.getTransform(epoch)

    val error = CelestTest.matrixError(transform.M, sofaRotation)
    error should be ( 0.0 plusOrMinus ArcSecond.convert(1E-6) )
  }


}
