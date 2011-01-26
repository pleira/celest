/**
 * Copyright (C) 2011 Simon Billemont <aodtorusan@gmail.com>
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 3.0 Unported
 * (CC BY-NC 3.0) (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *        http://creativecommons.org/licenses/by-nc/3.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.libs.celest.unit;

import junit.framework.Assert;

import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;

public abstract class Tests extends Assert {

	/**
	 * Tests if the elements between two vectors are all within the given tolerance
	 */
	public static void assertEquals(double[] expected, double[] actual, double tol) {
		assertEquals("Elements of the vectors are not equal", expected, actual, tol);
	}

	/**
	 * Tests if the elements between two vectors are all within the given tolerance
	 */
	public static void assertEquals(RealVector expected, RealVector actual, double tol) {
		assertEquals(expected.getData(), actual.getData(), tol);
	}

	/**
	 * Tests if the elements between two vectors are all within the given tolerance
	 */
	public static void assertEquals(String message, double[] expected, double[] actual, double tol) {
		assertEquals("Vectors must be the same length", expected.length, actual.length);
		for (int i = 0; i < expected.length; i++)
			assertEquals(message, expected[i], actual[i], tol);
	}

	/**
	 * Tests if the elements between two vectors are all within the given tolerance
	 */
	public static void assertEquals(String message, double[] expected, double[] actual, double[] tol) {
		assertEquals("Vectors must be the same length", expected.length, actual.length);
		for (int i = 0; i < expected.length; i++)
			assertEquals(message, expected[i], actual[i], tol[i]);
	}

	/**
	 * Tests if the elements between two vectors are all within the given tolerance
	 */
	public static void assertEquals(String message, RealVector expected, RealVector actual, double tol) {
		assertEquals(message, expected.getData(), actual.getData(), tol);
	}

	/**
	 * Check if two angles are equal within the given range. Note this will test 2*pi and 0 as equal (or
	 * any value close to it). This means that the actual angles are compared, and not the numeric values
	 * ( since 2*pi and 0 are not within linear tolerance, but the domain folds back).
	 * 
	 * @param a
	 *            Angle 1 [rad]
	 * @param b
	 *            Angle 2 [rad]
	 * @param tol
	 *            Tolerance to which the angles need to be equal [rad]
	 */
	public static void assertEqualsAngle(double a, double b, double tol) {
		assertEqualsAngle("Angle not equal <" + a + "> and <" + b + "> with <" + tol + ">", a, b, tol);
	}

	/**
	 * Check if two angles are equal within the given range. Note this will test 2*pi and 0 as equal (or
	 * any value close to it)
	 * 
	 * <p>
	 * This is a computational intensive test, it uses 10 Math calls, with 8 of these trigonometric
	 * functions
	 * </p>
	 * TODO: find a faster way of doing this
	 * 
	 * @param message
	 * @param a
	 *            Angle 1
	 * @param b
	 *            Angle 2
	 * @param tol
	 *            Tolerance to which the angles need to be equal
	 */
	public static void assertEqualsAngle(String message, double a, double b, double tol) {
		boolean sinTrue = Math.abs(Math.asin(Math.sin(a)) - Math.asin(Math.sin(b))) < tol;
		boolean cosTrue = Math.abs(Math.acos(Math.cos(a)) - Math.acos(Math.cos(b))) < tol;
		assertTrue(message, sinTrue && cosTrue);
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
	 * @see Tests#getStatistics(double[], double[])
	 */
	public static SummaryStatistics getStatistics(RealVector v1, RealVector v2) {
		return getStatistics(v1.getData(), v2.getData());
	}
}
