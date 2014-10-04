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

import be.angelcorp.celest.frameGraph.frames.transforms.IAU2006Precession
import be.angelcorp.celest.time.timeStandard.TimeStandards.TT
import be.angelcorp.celest.time.{Epochs, JulianDate}
import be.angelcorp.celest.unit.CelestTest
import be.angelcorp.celest.universe.DefaultUniverse
import be.angelcorp.libs.math.linear.Matrix3D
import be.angelcorp.libs.util.physics.Angle._
import org.scalatest.{FlatSpec, Matchers}

class TestIAU2006Precession extends FlatSpec with Matchers with CelestTest {

  implicit val universe = new DefaultUniverse

  "IAU2006Precession" should "calculate the correct precession angles" in {
    val epoch = new JulianDate(2013,  4, 27, 12, 33, 18.1938271, TT)
    val t = epoch.inTimeStandard(TT).relativeTo(Epochs.J2000) / 36525.0

    ArcSecond.convert(IAU2006Precession.ε0) should be(0.4090926006005829 plusOrMinus ArcSecond.convert(1E-3))
    ArcSecond.convert(IAU2006Precession.ψA(t)) should be(0.003253545093079898 plusOrMinus ArcSecond.convert(1E-3))
    ArcSecond.convert(IAU2006Precession.ωA(t)) should be(0.4090925882904625 plusOrMinus ArcSecond.convert(1E-2))
    ArcSecond.convert(IAU2006Precession.PA(t)) should be(2.728279880902066E-6 plusOrMinus ArcSecond.convert(1E-3))
    ArcSecond.convert(IAU2006Precession.QA(t)) should be(-3.022417535341198E-5 plusOrMinus ArcSecond.convert(1E-3))
    ArcSecond.convert(IAU2006Precession.εA(t)) should be(0.4090623554064873 plusOrMinus ArcSecond.convert(1E-3))
    ArcSecond.convert(IAU2006Precession.χA(t)) should be(6.612030301108906E-6 plusOrMinus ArcSecond.convert(1E-3))

    ArcSecond.convert(IAU2006Precession.γ_(t)) should be(6.859296282563124E-6 plusOrMinus ArcSecond.convert(0.1))
    ArcSecond.convert(IAU2006Precession.φ_(t)) should be(0.4090623764338111 plusOrMinus ArcSecond.convert(0.1))
    ArcSecond.convert(IAU2006Precession.ψ_(t)) should be(0.003253771956647474 plusOrMinus ArcSecond.convert(0.1))
  }

  "IAU2006Precession" should "calculate the correct precession matrix" in {
    val epoch = new JulianDate(2013,  4, 27, 12, 33, 18.1938271, TT)

    val iau2006 = new IAU2006Precession(null, null)
    val transform = iau2006.transform(epoch)

    val sofaRotation = Matrix3D(
      0.9999947269423086, 0.002978452278685739, 0.001294182985972787,
      -0.0029784522572169, 0.999995564399349, -1.943924919700635E-6,
      -0.001294183035381498, -1.910747566558069E-6, 0.9999991625429594
    )

    println(transform.M)

    val error = matrixError(transform.M, sofaRotation)
    error should be(0.0 plusOrMinus ArcSecond.convert(1E-2))
  }

}
