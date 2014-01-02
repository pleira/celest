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
package be.angelcorp.celest.unit;

import be.angelcorp.celest.state.Keplerian;
import be.angelcorp.celest.state.PosVel;
import be.angelcorp.celest.state.Spherical;
import be.angelcorp.libs.math.functions.domain.AngularDomain;
import be.angelcorp.libs.math.linear.Matrix3D;
import be.angelcorp.libs.math.rotation.AxisAngle;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public abstract class CelestTest extends TestCase {

    /**
     * Tests if the elements between two vectors are all within the given tolerance
     */
    public static void assertEquals(double[] expected, double[] actual, double tol) {
        assertEquals("Elements of the vectors are not equal", expected, actual, tol);
    }

    /**
     * Tests if the elements between two vectors are all within the given tolerance
     */
    public static void assertEquals(double[] expected, double[] actual, double[] tol) {
        assertEquals("Elements of the vectors are not equal", expected, actual, tol);
    }

    /**
     * Tests if the elements between two vectors are all within the given tolerance
     */
    public static void assertEquals(RealVector expected, RealVector actual, double tol) {
        assertEquals(expected.toArray(), actual.toArray(), tol);
    }

    /**
     * Tests if the elements between two vectors are all within the given tolerance
     */
    public static void assertEquals(String message, double[] expected, double[] actual, double tol) {
        Assert.assertEquals("Vectors must be the same length", expected.length, actual.length);
        for (int i = 0; i < expected.length; i++)
            Assert.assertEquals(message, expected[i], actual[i], tol);
    }

    /**
     * Tests if the elements between two vectors are all within the given tolerance
     */
    public static void assertEquals(String message, double[] expected, double[] actual, double[] tol) {
        Assert.assertEquals("Vectors must be the same length", expected.length, actual.length);
        for (int i = 0; i < expected.length; i++)
            Assert.assertEquals(message, expected[i], actual[i], tol[i]);
    }

    /**
     * Tests if the elements between two vectors are all within the given tolerance
     */
    public static void assertEquals(String message, RealVector expected, RealVector actual, double tol) {
        assertEquals(message, expected.toArray(), actual.toArray(), tol);
    }

    /**
     * Check if two angles are equal within the given range. Note this will test 2*pi and 0 as equal (or
     * any value close to it). This means that the actual angles are compared, and not the numeric values
     * ( since 2*pi and 0 are not within linear tolerance, but the domain folds back).
     *
     * @param a   Angle 1 [rad]
     * @param b   Angle 2 [rad]
     * @param tol Tolerance to which the angles need to be equal [rad]
     */
    public static void assertEqualsAngle(double a, double b, double tol) {
        assertEqualsAngle("Angle not equal <" + a + "> and <" + b + "> with <" + tol + ">", a, b, tol);
    }

    /**
     * Check if two angles are equal within the given range. Note this will test 2*pi and 0 as equal (or
     * any value close to it)
     * <p/>
     * <p>
     * This is a computational intensive test, it uses 10 Math calls, with 8 of these trigonometric
     * functions
     * </p>
     *
     * @param message Failiure message
     * @param a       Angle 1
     * @param b       Angle 2
     * @param tol     Tolerance to which the angles need to be equal
     */
    public static void assertEqualsAngle(String message, double a, double b, double tol) {
        AngularDomain dom = new AngularDomain(a, tol, tol);
        Assert.assertTrue(String.format(
                "The angle of %f rad is not equal to the expected angle %f rad with a tolerance of %f", b, a, tol),
                dom.inDomain(b));
    }

    /**
     * Get statistics on the element wise error vector between two vectors
     * <p>
     * error = v2 - v1
     * </p>
     *
     * @param v1
     * @param v2
     * @return
     */
    public static SummaryStatistics getStatistics(double[] v1, double[] v2) {
        SummaryStatistics stats = new SummaryStatistics();
        for (int i = 0; i < v1.length; i++) {
            stats.addValue(v2[i] - v1[i]);
        }
        return stats;
    }

    /**
     * @see CelestTest#getStatistics(double[], double[])
     */
    public static SummaryStatistics getStatistics(RealVector v1, RealVector v2) {
        return getStatistics(v1.toArray(), v2.toArray());
    }

    public static double matrixError(Matrix3D m1, Matrix3D m2) {
        return new AxisAngle(m2.transpose().cross(m1)).getAngle();
    }

    public static void angleEquals(double expected, double actual, double tolerance) {
        double difference = Math.abs((expected - actual + Math.PI) % (2 * Math.PI) - Math.PI);
        assertTrue(
                String.format("Angle not equal. Expected %.16f != %.16f actual, difference %e > tolerance %e", expected, actual, difference, tolerance),
                difference < tolerance
        );
    }

    public static void assertEquals(PosVel<?> expected, PosVel<?> actual, double positionError, double velocityError) {
        assertEquals(expected.position().norm(), actual.position().norm(), positionError);
        assertEquals(expected.position().x(), actual.position().x(), positionError);
        assertEquals(expected.position().y(), actual.position().y(), positionError);
        assertEquals(expected.position().z(), actual.position().z(), positionError);

        assertEquals(expected.velocity().norm(), actual.velocity().norm(), velocityError);
        assertEquals(expected.velocity().x(), actual.velocity().x(), velocityError);
        assertEquals(expected.velocity().y(), actual.velocity().y(), velocityError);
        assertEquals(expected.velocity().z(), actual.velocity().z(), velocityError);
    }

    public static void assertEquals(Keplerian<?> expected, Keplerian<?> actual,
                                    double deltaSemiMajorAxis, double deltaEccentricity, double deltaInclination,
                                    double deltaPericenter, double deltaRightAscension, double deltaMeanAnomaly) {
        assertEquals(expected.semiMajorAxis(), actual.semiMajorAxis(), deltaSemiMajorAxis);
        assertEquals(expected.eccentricity(), actual.eccentricity(), deltaEccentricity);
        angleEquals(expected.inclination(), actual.inclination(), deltaInclination);
        angleEquals(expected.argumentOfPeriapsis(), actual.argumentOfPeriapsis(), deltaPericenter);
        angleEquals(expected.rightAscension(), actual.rightAscension(), deltaRightAscension);
        angleEquals(expected.meanAnomaly(), actual.meanAnomaly(), deltaMeanAnomaly);
    }

    public static void assertEquals(Spherical<?> expected, Spherical<?> actual,
                                    double deltaRadius, double deltaRightAscension, double deltaDeclination,
                                    double deltaVelocity, double deltaFlightPathAngle, double deltaFlightPathAzimuth) {
        assertEquals(expected.radius(), actual.radius(), deltaRadius);
        angleEquals(expected.rightAscension(), actual.rightAscension(), deltaRightAscension);
        angleEquals(expected.declination(), actual.declination(), deltaDeclination);
        assertEquals(expected.velocity(), actual.velocity(), deltaVelocity);
        angleEquals(expected.flightPathAngle(), actual.flightPathAngle(), deltaFlightPathAngle);
        angleEquals(expected.flightPathAzimuth(), actual.flightPathAzimuth(), deltaFlightPathAzimuth);
    }

}
