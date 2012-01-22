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
package be.angelcorp.libs.celest.kepler;

import junit.framework.TestCase;

import org.apache.commons.math.linear.ArrayRealVector;

import be.angelcorp.libs.celest.constants.EarthConstants;
import be.angelcorp.libs.celest.stateVector.CartesianElements;
import be.angelcorp.libs.celest.stateVector.ICartesianElements;
import be.angelcorp.libs.celest.stateVector.IKeplerElements;
import be.angelcorp.libs.celest.stateVector.KeplerElements;
import be.angelcorp.libs.celest.unit.Tests;
import be.angelcorp.libs.math.MathUtils2;
import be.angelcorp.libs.math.linear.Vector3D;

public class TestKeplerEquations extends TestCase {

	/************************************
	 * Non-Static function tests
	 ************************************/
	// double arealVel()
	// double arguementOfLatitude()
	// double flightPathAngle()
	// CartesianElements kepler2cartesian()
	// double meanMotion()
	// double period()
	// double radius(double p, double e, double nu)
	// double totEnergyPerMass()
	// double trueAnomalyFromAnomaly()
	// double trueAnomalyFromMean()
	// double trueAnomalyFromMean(double M)
	// double trueLongitude()
	// double trueLongitudeOfPeriapse()
	// double trueLongitudeOfPeriapseApproximate()
	// double visViva(double r)

	public void test_Anomaly() {
		// Delegates calls, several samples:
		// Test based on validated samples in ReferenceKeplerAngles class
		for (ArrayRealVector sample : ReferenceKeplerAngles.all) {
			KeplerElements k = ReferenceKeplerAngles.createKeplerElements(sample);
			double anomaly = k.getOrbitEqn().anomaly();
			ReferenceKeplerAngles.checkEqualAnomaly(sample, anomaly);
		}
	}

	public void test_MeanAnomaly() {
		// Delegates calls, several samples:
		// Test based on validated samples in ReferenceKeplerAngles class
		for (ArrayRealVector sample : ReferenceKeplerAngles.all) {
			KeplerElements k = ReferenceKeplerAngles.createKeplerElements(sample);
			double M = k.getOrbitEqn().meanAnomaly();
			ReferenceKeplerAngles.checkEqualMeanAnomaly(sample, M);
		}
	}

	/************************************
	 * Static function tests
	 ************************************/
	public void testStaticArguementOfLatitude() {
		// public static double arguementOfLatitude(double w, double nu)
		// public static double arguementOfLatitude(Vector3D nodalVector, Vector3D radius)
		// TODO: Add tests
	}

	public void testStaticCartesian2kepler() {
		// cartesian2kepler(CartesianElements, CelestialBody)
		// cartesian2kepler(CartesianElements, double)
		ICartesianElements c = new CartesianElements(
				new Vector3D(10157768.1264, -6475997.0091, 2421205.9518),
				new Vector3D(1099.2953996, 3455.105924, 4355.0978095));
		IKeplerElements k = KeplerEquations.cartesian2kepler(c, EarthConstants.mu);
		double[] expected = new double[] {
				1.216495E7, 0.01404, 0.919398, 2.656017, 5.561776, 3.880560 };
		double[] tol = new ArrayRealVector(expected).mapMultiply(1E-3).toArray();
		Tests.assertEquals(expected, k.toVector().getData(), tol);
		k = KeplerEquations.cartesian2kepler(c, EarthConstants.bodyCenter);
		Tests.assertEquals(expected, k.toVector().getData(), tol);
	}

	public void testStaticCartesian2kepler2D() {
		// cartesian2kepler2D(CartesianElements, CelestialBody)
		ICartesianElements c = new CartesianElements(
				new Vector3D(10157768.1264, -6475997.0091, 2421205.9518),
				new Vector3D(1099.2953996, 3455.105924, 4355.0978095));
		IKeplerElements k = KeplerEquations.cartesian2kepler2D(c, EarthConstants.bodyCenter);
		assertEquals(1.216495E7, k.getSemiMajorAxis(), 1E4);
		assertEquals(0.01404, k.getEccentricity(), 1E-5);
		assertEquals(3.88056, k.getTrueAnomaly(), 1E-2);
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
		IKeplerElements k = new KeplerElements(0, 0, 0, Double.NaN, Double.NaN, 0);
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

	// public static Vector3D getH(Vector3D R, Vector3D V)
	// public static Matrix3D gravityGradient(Vector3D R, double mu)

	public void testStaticKepler2Cartesian() {
		// public static CartesianElements kepler2cartesian(double a, double ecc, double inc, double
		// Omega, double w, double nu, double mu)
		ICartesianElements c = KeplerEquations.kepler2cartesian(
				1.216495E7, 0.01404, 0.919398, 2.656017, 5.561776, 3.880560, EarthConstants.mu);
		// Values from keplerCOE from Matlab Orbital_Library by Richard Rieber
		ICartesianElements c_true = new CartesianElements(
				new Vector3D(1.092882447232868e+007, -5.619415989750504e+006, -1.715953308630781e+005),
				new Vector3D(1.466941526515634e+003, +3.108913288555892e+003, -4.504368922790057e+003));

		assertTrue(c_true.equals(c, 1e-12));
	}

	// Vector3D localGravity(Vector3D r, double mu)
	// double meanMotion(double, double)
	// double trueLongitude(Vector3D radius)
	// double trueLongitudeApproximation(double raan, double w, double nu)
	// double trueLongitudeOfPeriapse(Vector3D eccentricityVector)
	// double trueLongitudeOfPeriapseApproximate(double RAAN, double w)
	// double visViva(double, double, double)

}
