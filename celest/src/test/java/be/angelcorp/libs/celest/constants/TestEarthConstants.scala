/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
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
package be.angelcorp.libs.celest.constants

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import be.angelcorp.libs.celest.state.positionState.CartesianElements
import be.angelcorp.libs.celest.state.positionState.SphericalElements
import be.angelcorp.libs.celest.time.JulianDate
import be.angelcorp.libs.celest.universe.DefaultUniverse
import be.angelcorp.libs.math.linear.ImmutableVector3D
import be.angelcorp.libs.util.physics.Angle._
import be.angelcorp.libs.util.physics.Length._
import be.angelcorp.libs.util.physics.Time._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class TestEarthConstants  extends FlatSpec with ShouldMatchers  {

    implicit val universe = new DefaultUniverse

  "EarthConstants" should "define the correct heliocentric kepler orbit at the J2000 epoch" in {
    val trajectory = earthConstants.orbit

    // Validation state based on JPL Horizons data Body: Earth (399)
    // Frame: @Sun, ecliptic, J2000
    // AU/days/Degree

    val state_actual_k = trajectory.evaluate(universe.J2000_EPOCH)
    val state_actual_c = state_actual_k.toCartesianElements
    val state_actual_s = new SphericalElements(state_actual_c, state_actual_k.getCenterbody)

    val state_true_c = new CartesianElements(
      new ImmutableVector3D(-1.771351029694605E-01, 9.672416861070041E-01, -4.092421117973204E-06) *  AU.getScaleFactor,
      new ImmutableVector3D(-1.720762505701730E-02, -3.158782207802509E-03, 1.050630211603302E-07) * (AU.getScaleFactor / day_julian.getScaleFactor)
    )
    val state_true_s = new SphericalElements(state_true_c, state_actual_k.getCenterbody)

    // error see http://ssd.jpl.nasa.gov/?planet_pos
    state_actual_c.getR.norm         should be ( state_true_c.getR.norm         plusOrMinus 15E6 )                  // R
    state_actual_s.getRightAscension should be ( state_true_s.getRightAscension plusOrMinus ArcSecond.convert(40) ) // RA
    state_actual_s.getDeclination    should be ( state_true_s.getDeclination    plusOrMinus ArcSecond.convert(15) ) // DEC
  }

  it should "define the correct heliocentric kepler orbit at the 2452000jd epoch" in {
    val trajectory = universe.earthConstants.orbit

    val state_actual_k = trajectory.evaluate(new JulianDate(2452000d))
    val state_actual_c = state_actual_k.toCartesianElements
    val state_actual_s = new SphericalElements(state_actual_c, state_actual_k.getCenterbody)

    val state_true_c = new CartesianElements(
      new ImmutableVector3D(-9.813457035727633E-01, -1.876731681458192E-01, 1.832964606032273E-06) *  AU.getScaleFactor,
      new ImmutableVector3D(2.958686601622319E-03, -1.696218721190317E-02, -5.609085586954323E-07) * (AU.getScaleFactor / day.getScaleFactor)
    )
    val state_true_s = new SphericalElements(state_true_c, state_actual_k.getCenterbody)

    // error see http://ssd.jpl.nasa.gov/?planet_pos
    state_actual_c.getR.norm         should be ( state_true_c.getR.norm         plusOrMinus 15E6 )                  // R
    // TODO; why arcminute accuracy here ?
    state_actual_s.getRightAscension should be ( state_true_s.getRightAscension plusOrMinus ArcMinute.convert(40) ) // RA
    state_actual_s.getDeclination    should be ( state_true_s.getDeclination    plusOrMinus ArcSecond.convert(15) ) // DEC
  }
}
