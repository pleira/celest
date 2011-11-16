/**
 * Copyright (C) 2011 simon <aodtorusan@gmail.com>
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
package be.angelcorp.libs.celest.stateVector;

import static java.lang.Math.PI;

import java.util.Set;

import junit.framework.AssertionFailedError;
import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.constants.EarthConstants;
import be.angelcorp.libs.math.linear.Vector3D;

import com.google.common.collect.Sets;

public class TestSphericalElements extends TestStateVector<SphericalElements> {

	/**
	 * {@link StateVectorComparisonCase} specialization for {@link KeplerElements}, because
	 * KeplerElements#as requires a {@link CelestialBody}
	 * 
	 * @author simon
	 */
	private class SphericalStateVectorComparison<InitiationState extends IStateVector> extends
			StateVectorComparisonCase<InitiationState, SphericalElements> {

		private CelestialBody	body	= TestSphericalElements.earth;

		public SphericalStateVectorComparison(
				InitiationState testValidationState, SphericalElements testInitiationState) {
			super(testValidationState, testInitiationState);
		}

		public SphericalStateVectorComparison(
				InitiationState testValidationState, SphericalElements testInitiationState, CelestialBody body) {
			super(testValidationState, testInitiationState);
			this.body = body;
		}

		@Override
		protected SphericalElements doConvert(InitiationState sourceState) {
			try {
				return (SphericalElements) SphericalElements.class.getDeclaredMethod("as",
						IStateVector.class, CelestialBody.class).invoke(null, sourceState, body);
			} catch (Exception e) {
				throw new AssertionFailedError("Could not invoke comparison");
			}
		}
	}

	private static CelestialBody	earth	= EarthConstants.bodyCenter;

	@Override
	public Set<StateVectorComparisonCase<?, SphericalElements>> getTestStateVectors() {
		Set<StateVectorComparisonCase<?, SphericalElements>> s = Sets.newHashSet();

		/* Classical geo orbit */
		double rGEO = Math.pow(earth.getMu() / Math.pow((2. * PI) / (3600. * 24.), 2), 1. / 3.);
		s.add(new SphericalStateVectorComparison<IStateVector>(
				new CartesianElements(
						new Vector3D(4.22444282180791e+007, 0.00000000000000e+000, 0.00000000000000e+000),
						new Vector3D(0.00000000000000e+000, 3.07173798871147e+003, 0.00000000000000e+000)),
				new SphericalElements(rGEO, 0, 0, 3.0717379887E3, 0, Math.PI / 2, earth)));

		// The moon
		// From JPL horizons (http://ssd.jpl.nasa.gov/horizons.cgi)
		// Converted to spherical using Fundamentals of astrodynamics and applications matlab code
		// Moon position:
		// JDCT = 2455562.500000000 = A.D. 2011-Jan-01 00:00:00.0000 (CT)
		// X =-1.947136151107762E+05 Y =-3.249790482942117E+05 Z =-1.934593293850985E+04 [km]
		// VX= 8.680230862574665E-01 VY=-5.629777269508974E-01 VZ= 7.784227958608481E-02 [km/s]
		/*
		 * 
		 * // KeplerElements => SphericalElements NOT implemented yet
		 * 
		 * s.add(new SphericalStateVectorComparison<KeplerElements>( new
		 * KeplerElements(3.903213584163071E+08, 4.074916709908236e-002, 9.218093894982124E-2,
		 * 4.850831512485626E00, 4.757761494574442E00, 1.079822859502195E00, earth), new
		 * SphericalElements(3.823277207168130E+08, -1.876578287892178E00, -3.178799710706510E-2,
		 * 1.031461835283901E+03, Math.PI / 2. - 1.535552685601132E00, 1.484255164403220E00), earth));
		 */
		return s;
	}

}
