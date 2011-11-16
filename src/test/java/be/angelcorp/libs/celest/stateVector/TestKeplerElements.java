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

import java.util.Set;

import junit.framework.AssertionFailedError;
import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.constants.Constants;
import be.angelcorp.libs.celest.constants.EarthConstants;
import be.angelcorp.libs.celest.constants.SolarConstants;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.util.physics.Angle;
import be.angelcorp.libs.util.physics.Length;

import com.google.common.collect.Sets;

public class TestKeplerElements extends TestStateVector<KeplerElements> {

	/**
	 * {@link StateVectorComparisonCase} specialization for {@link KeplerElements}, because
	 * KeplerElements#as requires a {@link CelestialBody}
	 * 
	 * @author simon
	 */
	private class KeplerStateVectorComparison<InitiationState extends IStateVector> extends
			StateVectorComparisonCase<InitiationState, KeplerElements> {

		private CelestialBody	body	= TestKeplerElements.sun;

		public KeplerStateVectorComparison(
				InitiationState testValidationState, KeplerElements testInitiationState) {
			super(testValidationState, testInitiationState);
		}

		public KeplerStateVectorComparison(
				InitiationState testValidationState, KeplerElements testInitiationState, CelestialBody body) {
			super(testValidationState, testInitiationState);
			this.body = body;
		}

		@Override
		protected KeplerElements doConvert(InitiationState sourceState) {
			try {
				return (KeplerElements) KeplerElements.class.getDeclaredMethod("as", IStateVector.class,
						CelestialBody.class).invoke(null, sourceState, body);
			} catch (Exception e) {
				throw new AssertionFailedError("Could not invoke comparison");
			}
		}
	}

	private static final CelestialBody	sun	= SolarConstants.body;

	@Override
	public Set<StateVectorComparisonCase<?, KeplerElements>> getTestStateVectors() {
		Set<StateVectorComparisonCase<?, KeplerElements>> s = Sets.newHashSet();

		// -------------------------------------------
		// CARTESIAN
		// -------------------------------------------

		/* Object with 0 eccentricity */
		/* (2002 TZ300) (TransNeptunian Object) see http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=2002%20TZ300 */
		// s.add(new KeplerStateVectorComparison<CartesianElements>(
		// new CartesianElements(
		// new Vector3D(0,0,0),
		// new Vector3D(0,0,0)),
		// new KeplerElements(Length.convert(43.69, Length.AU), 0,
		// Angle.convert(3.58, Angle.DEG), Angle.convert(334.67, Angle.DEG),
		// Angle.convert(55.46, Angle.DEG), 0, sun)));

		/* (2000 QF226) (TransNeptunian Object) see http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=2000%20QF226 */
		// s.add(new KeplerStateVectorComparison<CartesianElements>(
		// new CartesianElements(
		// new Vector3D(0,0,0),
		// new Vector3D(0,0,0)),
		// new KeplerElements(Length.convert(46.49, Length.AU), 0, Angle.convert(2.25,
		// Angle.DEG), Angle.convert(296.90, Angle.DEG), Angle.convert(41.00, Angle.DEG), 0,
		// SolarConstants.body)));

		/* Object near 0 eccentricity */
		// The earth : )
		s.add(new KeplerStateVectorComparison<CartesianElements>(
				new CartesianElements(
						new Vector3D(-3.316179628957708e+10, +1.423339778393807e+11, +1.671050435305531e+10),
						new Vector3D(-2.950283371376224e+04, -6.692107319756996e+03, -1.547093138889844e+03)),
				EarthConstants.solarOrbit));

		/* Objects highly elliptical orbits */
		/* 2212 Hephaistos (1978 SB) (Apollo [NEO]), see http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=2212 */
		s.add(new KeplerStateVectorComparison<CartesianElements>(
				new CartesianElements(
						new Vector3D(-2.973349749923214e+10, -4.462450401495075e+10, -5.241039648539343e+09),
						new Vector3D(+5.568161770637649e+04, -3.568999965732438e+04, -1.201301879893813e+04)),
				new KeplerElements(Length.convert(2.167001873, Length.AU), 0.8338, Angle.convert(11.74,
						Angle.DEG), Angle.convert(208.56, Angle.DEG), Angle.convert(28.27, Angle.DEG), 0,
						SolarConstants.body)));
		/* 105140 (2000 NL10) (Aten [NEO]), see http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=105140 */
		// M = Angle.convert(293.30, Angle.DEG) => trueA = -2.70862
		s.add(new KeplerStateVectorComparison<CartesianElements>(
				new CartesianElements(
						new Vector3D(1.56873959425955e+011, 2.36432916810675e+010, 7.61843509822574e+010),
						new Vector3D(-1.63473158034754e+004, 1.07405165918640e+004, -1.24696362548472e+004)),
				new KeplerElements(Length.convert(0.9143, Length.AU), 0.8171,
						Angle.convert(32.52, Angle.DEG), Angle.convert(281.56, Angle.DEG), Angle.convert(
								237.44, Angle.DEG), -2.70863, SolarConstants.body)));

		/* Hyperbolic orbit */
		// TODO

		/* Equatorial non-circular */
		// TODO
		/* Equatorial circular */
		// TODO
		/* NonEquatorial circular */
		// TODO

		// -------------------------------------------
		// Spherical
		// -------------------------------------------

		// From jpl horizons (http://ssd.jpl.nasa.gov/horizons.cgi)
		// Converted to spherical using Fundamentals of astrodynamics and applications matlab code
		// Moon position:
		// JDCT = 2455562.500000000 = A.D. 2011-Jan-01 00:00:00.0000 (CT)
		// X =-1.947136151107762E+05 Y =-3.249790482942117E+05 Z =-1.934593293850985E+04 [km]
		// VX= 8.680230862574665E-01 VY=-5.629777269508974E-01 VZ= 7.784227958608481E-02 [km/s]
		CelestialBody jpl_earth = new CelestialBody();
		jpl_earth.setMass(Constants.mu2mass(398600.440E9));
		KeplerElements kMoon = new KeplerElements(3.903213584163071E+08, 4.074916709908236e-002,
				9.218093894982124E-2, 4.850831512485626E00,
				4.757761494574442E00, 1.079822859502195E00, jpl_earth);
		s.add(new KeplerStateVectorComparison<CartesianElements>(
				new CartesianElements(
						new Vector3D(-1.15037391547548e+008, -3.64408052048568e+008, -1.21513857899935e+007),
						new Vector3D(+9.67536480042629e+002, -3.46497896859408e+002, +8.78970130662041e+001)),
				kMoon, jpl_earth));
		s.add(new KeplerStateVectorComparison<SphericalElements>(
				new SphericalElements(3.823277207168130E+08, -1.876578287892178E00, -3.178799710706510E-2,
						1.031461835283901E+03, Math.PI / 2. - 1.535552685601132E00, 1.484255164403220E00),
				kMoon, jpl_earth));

		return s;
	}
}
