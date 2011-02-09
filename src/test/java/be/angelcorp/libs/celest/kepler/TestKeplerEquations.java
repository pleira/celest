package be.angelcorp.libs.celest.kepler;

import junit.framework.TestCase;

import org.apache.commons.math.linear.ArrayRealVector;

import be.angelcorp.libs.celest.constants.EarthConstants;
import be.angelcorp.libs.celest.stateVector.CartesianElements;
import be.angelcorp.libs.celest.stateVector.KeplerElements;
import be.angelcorp.libs.celest.unit.Tests;
import be.angelcorp.libs.math.MathUtils2;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.util.physics.Angle;

public class TestKeplerEquations extends TestCase {

	/************************************
	 * Static function tests
	 ************************************/
	public void testStaticCartesian2kepler() {
		// cartesian2kepler(CartesianElements, CelestialBody)
		// cartesian2kepler(CartesianElements, double)
		CartesianElements c = new CartesianElements(
				new Vector3D(10157768.1264, -6475997.0091, 2421205.9518),
				new Vector3D(1099.2953996, 3455.105924, 4355.0978095));
		KeplerElements k = KeplerEquations.cartesian2kepler(c, EarthConstants.mu);
		double[] expected = new double[] {
				1.216495E7, 0.01404, 0.919398, 2.656017, 5.561776, 3.880560 };
		double[] tol = new ArrayRealVector(expected).mapMultiply(1E-3).toArray();
		Tests.assertEquals(expected, k.toVector().getData(), tol);
		k = KeplerEquations.cartesian2kepler(c, EarthConstants.bodyCenter);
		Tests.assertEquals(expected, k.toVector().getData(), tol);
	}

	public void testStaticCartesian2kepler2D() {
		// cartesian2kepler2D(CartesianElements, CelestialBody)
		CartesianElements c = new CartesianElements(
				new Vector3D(10157768.1264, -6475997.0091, 2421205.9518),
				new Vector3D(1099.2953996, 3455.105924, 4355.0978095));
		KeplerElements k = KeplerEquations.cartesian2kepler2D(c, EarthConstants.bodyCenter);
		assertEquals(1.216495E7, k.getSemiMajorAxis(), 1E4);
		assertEquals(0.01404, k.getEccentricity(), 1E-5);
		assertEquals(3.88056, k.getTrueAnomaly(), 1E-2);
	}

	public void testStaticEccentricAnomaly() {
		// eccentricAnomaly(double, double)
		// See http://www.astro.uu.nl/~strous/AA/en/reken/kepler.html
		double M = Angle.convert(60, Angle.DEG);
		double e = 0.01671;
		double E = KeplerEquations.eccentricAnomaly(M, e);
		assertEquals(1.061789204, E, 1E-9);
	}

	public void testStaticEccentricAnomalyFromTrue() {
		// eccentricAnomalyFromTrue(double, double)
		// http://www.wolframalpha.com/input/?i=mean+anomaly+5+radian+eccentricity+0.6
		double E = MathUtils2.mod(KeplerEquations.eccentricAnomalyFromTrue(-2.427, 0.6), 2 * Math.PI);
		assertEquals(4.425, E, 1E-3);
	}

	public void testStaticEccentricity() {
		// eccentricity(double, double)
		assertEquals(0, KeplerEquations.eccentricity(1, 1), 1E-3);
		// Earth to mars orbit perihelion is the Earth's orbital radius, aphelion is the radius of Mars
		double Rp = 1.495978E11;
		double Ra = 2.27987047E11;
		assertEquals(0.207606972, KeplerEquations.eccentricity(Rp, Ra), 1E-9);
	}

	public void testStaticFix2dOrbit() {
		// fix2dOrbit(KeplerElements)
		KeplerElements k = new KeplerElements(0, 0, 0, Double.NaN, Double.NaN, 0);
		KeplerEquations.fix2dOrbit(k);
		assertTrue(!Double.isNaN(k.getRaan()));
		assertTrue(!Double.isNaN(k.getArgumentPeriapsis()));
	}

	public void testStaticFlightPathAngle() {
		// flightPathAngle(double, double)
		// http://www.wolframalpha.com/input/?i=mean+anomaly+5+radian+eccentricity+0.6
		double gamma = MathUtils2.mod(KeplerEquations.flightPathAngle(0.6, -2.427), 2 * Math.PI);
		assertEquals(5.6597, gamma, 1E-4);
	}

	public void testStaticmeanAnomalyFromTrue() {
		// meanAnomalyFromTrue(double, double)
		// http://www.wolframalpha.com/input/?i=mean+anomaly+5+radian+eccentricity+0.6
		double M = MathUtils2.mod(KeplerEquations.meanAnomalyFromTrue(-2.427, 0.6), 2 * Math.PI);
		assertEquals(5, M, 1E-3);
	}

	// meanMotion(double, double)
	// visViva(double, double, double)
	// kepler2cartesian(double, double, double, double, double, double, double)

	// getH(Vector3D, Vector3D)
	// gravityGradient(Vector3D, double)
	// localGravity(Vector3D, double)

	public void testStaticTrueAnomalyFromEccentric() {
		// eccentricAnomaly(double, double)
		// See http://www.astro.uu.nl/~strous/AA/en/reken/kepler.html
		double E = 1.061789204;
		double e = 0.01671;
		double nu = KeplerEquations.trueAnomalyFromEccentric(E, e);
		assertEquals(1.076441274, nu, 1E-9);
	}

	public void testStaticTrueAnomalyFromMean() {
		// trueAnomalyFromMean(double, double)
		// See http://www.astro.uu.nl/~strous/AA/en/reken/kepler.html
		double M = Angle.convert(60, Angle.DEG);
		double e = 0.01671;
		double nu = KeplerEquations.trueAnomalyFromMean(M, e);
		assertEquals(1.076441274, nu, 1E-9);
	}

	/************************************
	 * Non-Static function tests
	 ************************************/
	// KeplerEquations(KeplerElements)
	// arealVel()
	// arealVel(double, double, double)
	// eccentricAnomaly()
	// flightPathAngle()
	// focalParameter()
	// getApocenter()
	// getPericenter()
	// kepler2cartesian()
	// meanAnomalyFromTrue(double)
	// meanMotion()
	// period()
	// period(double)
	// radius(double, double, double)
	// totEnergyPerMass()
	// totEnergyPerMass(double, double)
	// trueAnomalyFromMean(double)
	// velocitySq(double)
	// velocitySq(double, double, double)

}
