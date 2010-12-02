/**
 * Copyright (C) 2010 Simon Billemont <aodtorusan@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.libs.celest.unit;

import junit.framework.Assert;

public abstract class Tests extends Assert {

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
}
