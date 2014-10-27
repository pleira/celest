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

import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.physics.Units._
import be.angelcorp.celest.state.{PosVel, Spherical}
import be.angelcorp.celest.time.{Epochs, JulianDate}
import be.angelcorp.celest.universe.DefaultUniverse
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class TestEarthConstants extends FlatSpec with ShouldMatchers {

  implicit val universe = new DefaultUniverse

  "EarthConstants" should "define the correct heliocentric kepler orbit at the J2000 epoch" in {
    val trajectory = EarthConstants.orbit

    // Validation state based on JPL Horizons data Body: Earth (399)
    // Frame: @Sun, ecliptic, J2000
    // AU/days/Degree

    val state_actual_k = trajectory(Epochs.J2000)
    val state_actual_c = state_actual_k.toPosVel
    val state_actual_s = Spherical(state_actual_c)

    val state_true_c = new PosVel(
      new Vec3(-1.771351029694605E-01, 9.672416861070041E-01, -4.092421117973204E-06) * AU,
      new Vec3(-1.720762505701730E-02, -3.158782207802509E-03, 1.050630211603302E-07) * (AU / julianDay),
      state_actual_k.frame
    )
    val state_true_s = Spherical(state_true_c)

    println( state_actual_k.a )
    println( state_actual_k.e )
    println( state_actual_k.i )
    println( state_actual_k.ω )
    println( state_actual_k.Ω )
    println( state_actual_k.meanAnomaly )
    println( state_actual_c )
    println( state_true_c )

    // error see http://ssd.jpl.nasa.gov/?planet_pos
    state_actual_c.position.norm should be(state_true_c.position.norm +- 15E6) // R
    state_actual_s.rightAscension should be(state_true_s.rightAscension +- arcSecond(40)) // RA
    state_actual_s.declination should be(state_true_s.declination +- arcSecond(15)) // DEC
  }

  it should "define the correct heliocentric kepler orbit at the 2452000jd epoch" in {
    val trajectory = EarthConstants.orbit

    val state_actual_k = trajectory(new JulianDate(2452000d))
    val state_actual_c = state_actual_k.toPosVel
    val state_actual_s = Spherical(state_actual_c)

    val state_true_c = new PosVel(
      new Vec3(-9.813457035727633E-01, -1.876731681458192E-01, 1.832964606032273E-06) * AU,
      new Vec3(2.958686601622319E-03, -1.696218721190317E-02, -5.609085586954323E-07) * (AU / julianDay),
      state_actual_k.frame
    )
    val state_true_s = Spherical(state_true_c)

    // error see http://ssd.jpl.nasa.gov/?planet_pos
    state_actual_c.position.norm should be(state_true_c.position.norm +- 15E6) // R
    // TODO; why arcminute accuracy here ?
    state_actual_s.rightAscension should be(state_true_s.rightAscension +- arcMinute(40)) // RA
    state_actual_s.declination should be(state_true_s.declination +- arcSecond(15)) // DEC
  }
}
