/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
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
package be.angelcorp.celest.constants

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import be.angelcorp.celest.time.JulianDate
import be.angelcorp.celest.universe.DefaultUniverse
import be.angelcorp.libs.math.linear.ImmutableVector3D
import be.angelcorp.libs.util.physics.Angle._
import be.angelcorp.libs.util.physics.Length._
import be.angelcorp.libs.util.physics.Time._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import be.angelcorp.celest.state.{PosVel, Spherical}

@RunWith(classOf[JUnitRunner])
class TestEarthConstants extends FlatSpec with ShouldMatchers {

  implicit val universe = new DefaultUniverse

  "EarthConstants" should "define the correct heliocentric kepler orbit at the J2000 epoch" in {
    val trajectory = EarthConstants.orbit

    // Validation state based on JPL Horizons data Body: Earth (399)
    // Frame: @Sun, ecliptic, J2000
    // AU/days/Degree

    val state_actual_k = trajectory(universe.J2000_EPOCH)
    val state_actual_c = state_actual_k.toPosVel
    val state_actual_s = Spherical(state_actual_c)

    val state_true_c = new PosVel(
      new ImmutableVector3D(-1.771351029694605E-01, 9.672416861070041E-01, -4.092421117973204E-06) * AU.getScaleFactor,
      new ImmutableVector3D(-1.720762505701730E-02, -3.158782207802509E-03, 1.050630211603302E-07) * (AU.getScaleFactor / day_julian.getScaleFactor),
      state_actual_k.frame
    )
    val state_true_s = Spherical(state_true_c)

    // error see http://ssd.jpl.nasa.gov/?planet_pos
    state_actual_c.position.norm should be(state_true_c.position.norm plusOrMinus 15E6) // R
    state_actual_s.rightAscension should be(state_true_s.rightAscension plusOrMinus ArcSecond.convert(40)) // RA
    state_actual_s.declination should be(state_true_s.declination plusOrMinus ArcSecond.convert(15)) // DEC
  }

  it should "define the correct heliocentric kepler orbit at the 2452000jd epoch" in {
    val trajectory = EarthConstants.orbit

    val state_actual_k = trajectory(new JulianDate(2452000d))
    val state_actual_c = state_actual_k.toPosVel
    val state_actual_s = Spherical(state_actual_c)

    val state_true_c = new PosVel(
      new ImmutableVector3D(-9.813457035727633E-01, -1.876731681458192E-01, 1.832964606032273E-06) * AU.getScaleFactor,
      new ImmutableVector3D(2.958686601622319E-03, -1.696218721190317E-02, -5.609085586954323E-07) * (AU.getScaleFactor / day.getScaleFactor),
      state_actual_k.frame
    )
    val state_true_s = Spherical(state_true_c)

    // error see http://ssd.jpl.nasa.gov/?planet_pos
    state_actual_c.position.norm should be(state_true_c.position.norm plusOrMinus 15E6) // R
    // TODO; why arcminute accuracy here ?
    state_actual_s.rightAscension should be(state_true_s.rightAscension plusOrMinus ArcMinute.convert(40)) // RA
    state_actual_s.declination should be(state_true_s.declination plusOrMinus ArcSecond.convert(15)) // DEC
  }
}
