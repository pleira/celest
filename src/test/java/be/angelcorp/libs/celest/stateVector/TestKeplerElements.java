package be.angelcorp.libs.celest.stateVector;

import java.util.Map;
import java.util.Set;

import be.angelcorp.libs.celest.constants.EarthConstants;
import be.angelcorp.libs.celest.constants.SolarConstants;
import be.angelcorp.libs.celest.kepler.KeplerEquations;
import be.angelcorp.libs.celest.kepler.KeplerOrbitTypes;
import be.angelcorp.libs.celest.unit.Tests;
import be.angelcorp.libs.math.linear.Vectors;
import be.angelcorp.libs.util.physics.Angle;
import be.angelcorp.libs.util.physics.Length;

import com.google.common.collect.Maps;

public class TestKeplerElements extends TestStateVector<KeplerElements> {

	private Map<KeplerElements, Double>	testOrbits;

	@Override
	public Set<KeplerElements> getTestStateVectors() {
		testOrbits = Maps.newHashMap();
		/* Object with 0 eccentricity */
		/* (2002 TZ300) (TransNeptunian Object) see http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=2002%20TZ300 */
		// TODO: M = Angle.convert(359.93, Angle.DEG) => trueA
		testOrbits.put(new KeplerElements(Length.convert(43.69, Length.AU), 0,
				Angle.convert(3.58, Angle.DEG), Angle.convert(334.67, Angle.DEG),
				Angle.convert(55.46, Angle.DEG), 0),
				SolarConstants.mu);
		/* (2000 QF226) (TransNeptunian Object) see http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=2000%20QF226 */
		testOrbits.put(new KeplerElements(Length.convert(46.49, Length.AU), 0,
				Angle.convert(2.25, Angle.DEG), Angle.convert(296.90, Angle.DEG),
				Angle.convert(41.00, Angle.DEG), 0), SolarConstants.mu);

		/* Object near 0 eccentricity */
		// The earth : )
		testOrbits.put(EarthConstants.solarOrbit, SolarConstants.mu);

		/* Objects highly elliptical orbits */
		/* 2212 Hephaistos (1978 SB) (Apollo [NEO]), see http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=2212 */
		// TODO: M = Angle.convert(12.63, Angle.DEG) => trueA
		testOrbits.put(new KeplerElements(Length.convert(2.167, Length.AU), 0.8338,
				Angle.convert(11.74, Angle.DEG), Angle.convert(208.56, Angle.DEG),
				Angle.convert(28.27, Angle.DEG), 0), SolarConstants.mu);
		/* 105140 (2000 NL10) (Aten [NEO]), see http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=105140 */
		// TODO: M = Angle.convert(293.30, Angle.DEG) => trueA
		testOrbits.put(new KeplerElements(Length.convert(0.9143, Length.AU), 0.8171,
				Angle.convert(32.52, Angle.DEG), Angle.convert(281.56, Angle.DEG),
				Angle.convert(237.44, Angle.DEG), 0), SolarConstants.mu);

		return testOrbits.keySet();
	}

	@Override
	public void testToCartesianElement(KeplerElements state) {
		CartesianElements cart = state.toCartesianElements(testOrbits.get(state));
		KeplerElements k = KeplerEquations.cartesian2kepler(cart, testOrbits.get(state));

		double angleTol = KeplerEquations.angleTolarance;
		assertEquals(state.a, k.a, 1);
		assertEquals(state.e, k.e, KeplerEquations.eccentricityTolarance);
		Tests.assertEqualsAngle(state.i, k.i, angleTol);
		if (Math.abs(state.i % Math.PI) > angleTol) {
			// equatorial orbits have problems with these parameters,
			// use AlternativeKeplerElements for these
			Tests.assertEqualsAngle(state.raan, k.raan, angleTol);
			if (state.getOrbitType() != KeplerOrbitTypes.Circular) {
				// Similar problem
				Tests.assertEqualsAngle(state.omega, k.omega, angleTol);
				Tests.assertEqualsAngle(state.trueA, k.trueA, angleTol);
			}
		}
		// This should again work for everything
		CartesianElements cart2 = k.toCartesianElements(testOrbits.get(state));
		assertTrue(Vectors.compare(cart.toVector(), cart2.toVector(), state.getSemiMajorAxis() * 1E-6));
	}

}
