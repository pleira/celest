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
package be.angelcorp.libs.celest.constants;

import be.angelcorp.libs.celest.state.positionState.CartesianElements;
import be.angelcorp.libs.celest.state.positionState.NonSignuarElements;
import be.angelcorp.libs.celest.state.positionState.SphericalElements;
import be.angelcorp.libs.celest.time.JulianDate;
import be.angelcorp.libs.celest.trajectory.KeplerVariationTrajectory;
import be.angelcorp.libs.celest.unit.CelestTest;
import be.angelcorp.libs.math.linear.ImmutableVector3D;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.util.physics.Angle;
import be.angelcorp.libs.util.physics.Length;
import be.angelcorp.libs.util.physics.Time;

public class TestEarthConstants extends CelestTest {

	public void testOrbit() {
		KeplerVariationTrajectory trajectory = EarthConstants.orbit;

		// Validation state based on JPL Horizons data Body: Earth (399)
		// Frame: @Sun, ecliptic, J2000
		// AU/days/Degree

		NonSignuarElements state_actual_k = trajectory.evaluate(JulianDate.getJ2000());
		CartesianElements state_actual_c = state_actual_k.toCartesianElements();
		SphericalElements state_actual_s = new SphericalElements(state_actual_c, state_actual_k.getCenterbody());
		CartesianElements state_true_c = new CartesianElements(
				new ImmutableVector3D(-1.771351029694605E-01, 9.672416861070041E-01, -4.092421117973204E-06)
						.multiply(Length.AU.getScaleFactor()),
				new ImmutableVector3D(-1.720762505701730E-02, -3.158782207802509E-03, 1.050630211603302E-07)
						.multiply(Length.AU.getScaleFactor() / Time.day.getScaleFactor()));
		SphericalElements state_true_s = new SphericalElements(state_true_c, state_actual_k.getCenterbody());
		// error see http://ssd.jpl.nasa.gov/?planet_pos
		assertEquals(state_true_c.getR().norm(), state_actual_c.getR().norm(), 15e6); // R
		assertEquals(state_true_s.getRightAscension(), state_actual_s.getRightAscension(),
				Angle.convert(40, Angle.ARCSECOND)); // RA
		assertEquals(state_true_s.getDeclination(), state_actual_s.getDeclination(),
				Angle.convert(15, Angle.ARCSECOND)); // DEC

		state_actual_k = trajectory.evaluate(new JulianDate(2452000d));
		state_actual_c = state_actual_k.toCartesianElements();
		state_actual_s = new SphericalElements(state_actual_c, state_actual_k.getCenterbody());
		state_true_c = new CartesianElements(
				new ImmutableVector3D(-9.813457035727633E-01, -1.876731681458192E-01, 1.832964606032273E-06)
						.multiply(Length.AU.getScaleFactor()),
				new ImmutableVector3D(2.958686601622319E-03, -1.696218721190317E-02, -5.609085586954323E-07)
						.multiply(Length.AU.getScaleFactor() / Time.day.getScaleFactor()));
		state_true_s = new SphericalElements(state_true_c, state_actual_k.getCenterbody());
		assertEquals(state_true_c.getR().norm(), state_actual_c.getR().norm(), 15e6); // R
		assertEquals(state_true_s.getRightAscension(), state_actual_s.getRightAscension(),
				Angle.convert(40, Angle.ARCMINUTE)); // RA TODO; why arcminute accuracy here ?
		assertEquals(state_true_s.getDeclination(), state_actual_s.getDeclination(),
				Angle.convert(15, Angle.ARCSECOND)); // DEC

	}
}
