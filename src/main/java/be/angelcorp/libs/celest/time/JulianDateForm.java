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
package be.angelcorp.libs.celest.time;

import static java.lang.Math.floor;

/**
 * Transforms between different julian date representations
 * 
 * <p>
 * Conversion:
 * </p>
 * 
 * <pre>
 * if (doFloor)
 *   transformed = floor( a * JD + b)
 * else
 *   transformed =      ( a * JD + b)
 * </pre>
 */
public enum JulianDateForm {
	JULIAN_DATE(1., 0.), // JD
	JULIAN_DAY_NUMBER(1., 0., true), // JDN
	REDUCED_JULIAN_DAY(1., -2400000), // RJD
	MODIFIED_JULIAN_DAY(1., -2400000.5), // MJD
	TRUNCATED_JULIAN_DAY(1., -2440000.5), // TJD, nasa def from Noerdlinger, 1995.
	DUBLIN_JULIAN_DAY(1., -2415020), // DJD
	ANSI_DATE(1., -2305812.5, true),
	UNIX_TIME(86400., -2440587.5 * 86400.);

	double	a;
	double	b;
	boolean	doFloor;

	private JulianDateForm(double a, double b) {
		this(a, b, false);
	}

	private JulianDateForm(double a, double b, boolean doFloor) {
		this.a = a;
		this.b = b;
		this.doFloor = doFloor;
	}

	public double fromJD(double jd) {
		return (doFloor) ? floor(a * jd + b) : a * jd + b;
	}

	/**
	 * <p>
	 * Note some forms are only accurate to a given day due to the dates being integer numbers (like
	 * {@link JulianDateForm#ANSI_DATE}, {@link JulianDateForm#JULIAN_DAY_NUMBER}).
	 * </p>
	 * 
	 * @param date
	 * @return
	 */
	public double toJD(double date) {
		return (date - b) / a;
	}
}