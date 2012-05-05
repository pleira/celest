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
package be.angelcorp.libs.celest.time.timeStandard;

import org.apache.commons.math3.analysis.UnivariateFunction;

import be.angelcorp.libs.celest.time.dateStandard.DateStandards;

/**
 * Function for TAI - UTC for dates between 1 Jan 1961 and 1 Jan 1972
 * 
 * <pre>
 * TAI-UTC= a + (MJD - b) &times; c S
 * </pre>
 * 
 * @author Simon Billemont
 */
public class UTC_TAI_Function implements UnivariateFunction {

	/** Time bias [s] */
	private final double	a;
	/** Date bias [Julian days], NOTE includes MJD => JD */
	private final double	b_;
	/** Time bias derivative [s] */
	private final double	c;

	/**
	 * <pre>
	 * TAI-UTC= a + (MJD - b) &times; c S
	 * </pre>
	 * 
	 * @param a
	 *            Time bias [s]
	 * @param b
	 *            Date bias [Julian days]
	 * @param c
	 *            Time bias derivative [s / Julian day]
	 */
	public UTC_TAI_Function(double a, double b, double c) {
		this.a = a;
		this.b_ = DateStandards.MJD.toJD(0) + b;
		this.c = c;
	}

	/**
	 * Compute TAI - UTC at a specific Julian date.
	 */
	@Override
	public double value(double x) {
		double TAI_UTC = a + (x - b_) * c;
		return TAI_UTC;
	}
}
