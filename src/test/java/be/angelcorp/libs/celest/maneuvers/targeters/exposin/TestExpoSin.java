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
package be.angelcorp.libs.celest.maneuvers.targeters.exposin;

import junit.framework.TestCase;

import org.apache.commons.math.linear.ArrayRealVector;

import be.angelcorp.libs.celest.state.positionState.CartesianElements;
import be.angelcorp.libs.celest.time.JulianDate;
import be.angelcorp.libs.celest.unit.Tests;
import be.angelcorp.libs.math.functions.ExponentialSinusoid;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.util.physics.Length;
import be.angelcorp.libs.util.physics.Time;

public class TestExpoSin extends TestCase {

	public void testSimpleExposin() throws Exception {
		// Expected values (verified)
		double[] actualGammaOpt = new double[] { 0.24023167373332216, -1.0864393175309273,
				-1.051300061165213, -1.042626811261495, -1.0394394817990522, -1.0378227332097778 };
		double[][] actualParam = new double[][] {
				// k0, k1, k2, phi
				new double[] { 1.9073362453261113E12,
						-3.888517626507795, 0.08333333333333333, 2.427909185841054 },
				new double[] { 1.6059096694397289E41,
						-72.81246468759714, 0.08333333333333333, 1.2522092080126825 },
				new double[] { 1.254325392557468E25,
						-38.31615772987497, 0.08333333333333333, 0.9912714794537701 },
				new double[] { 1.4585901197880762E19,
						-27.593269205443356, 0.08333333333333333, 0.7297207320804159 },
				new double[] { 4.540791272005266E15,
						-22.87741606492031, 0.08333333333333333, 0.468029743535396 },
				new double[] { 1.0561171322268133E13,
						-20.782459077597395, 0.08333333333333333, 0.2062958001926609 },
		};

		for (int n = 0; n <= 5; n++) {
			/* Create a new exposin problem */
			ExpoSin expo = new ExpoSin(
					new CartesianElements( // Start state vector
							new Vector3D(Length.convert(1, Length.AU), 0, 0), Vector3D.ZERO),
					new CartesianElements( // End state vector R: <0, 5 AU, 0> V: <0, 0, 0>
							new Vector3D(0, Length.convert(1.5, Length.AU), 0), Vector3D.ZERO),
					JulianDate.getJ2000(), JulianDate.getJ2000().add(0.35, Time.year)); // TOF, Not
			// required
			expo.assumeK2(1. / 12); /* Sets exposin k2 */
			expo.setN(n); /* Sets the revolutions we should make */

			/* Get the optimal exposin trajectory (where tf is the wanted value) */
			ExpoSinTrajectory trajectory = expo.getTrajectory();
			ExponentialSinusoid f = trajectory.getExposin();

			assertEquals(actualGammaOpt[n], trajectory.getGamma(), 1E-6 * Math.abs(actualGammaOpt[n]));
			/* Output exposin parameter details */
			Tests.assertEquals(
					actualParam[n],
					new double[] { f.getK0(), f.getK1(), f.getK2(), f.getPhi() },
					new ArrayRealVector(actualParam[n]).mapAbs().mapMultiply(1E-4).getData());
		}
	}
}
