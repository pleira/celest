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
package be.angelcorp.celest.math.functions;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialsUtils;

/**
 * <p>
 * Complete algorithm for associated function based on NUMERICAL RECIPES IN FORTRAN 77: THE ART OF
 * SCIENTIFIC COMPUTING (ISBN 0-521-43064-X) chapter Spherical Harmonics 6.8 p 247
 * </p>
 * <p>
 * Recursion equations for associated function based on wakker eq 20.2-2
 * </p>
 * 
 * @author simon
 * 
 */
public abstract class LegendreUtils {

	/**
	 * Computes the associated Legendre function of the first kind P<sub>l,m</sub>(x)
	 * 
	 * <p>
	 * Based on NUMERICAL RECIPES IN FORTRAN 77: THE ART OF SCIENTIFIC COMPUTING (ISBN 0-521-43064-X)
	 * chapter Spherical Harmonics 6.8 p 247
	 * </p>
	 * 
	 * @param l
	 *            Order of the Legendre function
	 * @param m
	 *            Degree of the Legendre function, where 0 <= m <= l
	 * @param x
	 *            Point of evaluations, -1 <= x <= 1
	 * @return
	 */
	public static double associatedLegendreFunctionOfTheFirstKind(int l, int m, double x) {
		double plgndr, fact, pll = 0, pmm, pmmp1, somx2;

		/* Check input */
		if (m < 0 || m > l || Math.abs(x) > 1)
			// pause bad arguments in plgndr
			throw new IllegalArgumentException(
					"Bad arguements given in associatedLegendreFunctionOfTheFirstKind");

		/* Compute Pm,m */
		// Same implementations as ... , but more native
		// LegendreUtils#associatedLegendreFunctionOfTheFirstKindRec2
		pmm = 1.;
		if (m > 0) {
			somx2 = Math.sqrt((1. - x) * (1. + x));
			fact = 1.;
			for (int i = 1; i <= m; i++) {
				pmm = -pmm * fact * somx2;
				fact = fact + 2.;
			}
		}

		/* Compute Pl,m */
		if (l == m) {
			plgndr = pmm;
		} else {
			// Compute Pm+1,m
			pmmp1 = x * (2 * m + 1) * pmm;
			if (l == m + 1)
				plgndr = pmmp1;
			else { // Compute Pl,m, l > m+ 1
				for (int ll = m + 2; ll <= l; ll++) {
					pll = (x * (2 * ll - 1) * pmmp1 - (ll + m - 1) * pmm) / (ll - m);
					pmm = pmmp1;
					pmmp1 = pll;
				}
				plgndr = pll;
			}
		}
		return plgndr;
	}

	/**
	 * Computes the associated Legendre function of the first kind P<sub>l,0</sub>(x) with a recursion
	 * formula
	 * 
	 * <p>
	 * Uses recursion formula: (n+1) P<sub>n+1,0</sub>(x) = (2n+1) x P<sub>n,0</sub>(x) - n
	 * P<sub>n-1,0</sub>(x)
	 * </p>
	 * 
	 * @param l
	 *            Order of the Legendre function
	 * @param x
	 *            Point of evaluations, -1 <= x <= 1
	 * @param pn_10
	 *            Value of P<sub>l-1,0</sub>(x)
	 * @param pn_20
	 *            Value of P<sub>l-2,0</sub>(x)
	 * @return
	 */
	public static double associatedLegendreFunctionOfTheFirstKindRec1(int l, int m,
			double x, double pn_10, double pn_20) {
		return ((2 * l - 1) / l) * x * pn_10 - ((l - 1) / l) * pn_20;
	}

	/**
	 * Computes the associated Legendre function of the first kind P<sub>l,0</sub>(x) with a recursion
	 * furmula
	 * 
	 * <p>
	 * Uses recursion formula: (n+1) P<sub>n+1,0</sub>(x) = (2n+1) x P<sub>n,0</sub>(x) - n
	 * P<sub>n-1,0</sub>(x)
	 * </p>
	 * 
	 * @param l
	 *            Order of the Legendre function
	 * @param x
	 *            Point of evaluations, -1 <= x <= 1
	 * @param pn_10
	 *            Function P<sub>l-1,0</sub>(x)
	 * @param pn_20
	 *            Function P<sub>l-2,0</sub>(x)
	 * @return
	 */
	public static UnivariateFunction associatedLegendreFunctionOfTheFirstKindRec1(final int l,
			final UnivariateFunction pn_10, final UnivariateFunction pn_20) {
		return new UnivariateFunction() {
			@Override
			public double value(double x) {
				return ((2 * l - 1) / l) * x * pn_10.value(x) - ((l - 1) / l) * pn_20.value(x);
			}
		};
	}

	/**
	 * Computes the associated Legendre function of the first kind P<sub>l,m</sub>(x) with a recursion
	 * formula, where l = m
	 * 
	 * <p>
	 * Uses recursion formula: P<sub>n,n</sub>(x) = (2n-1) (1-x<sup>2</sup>)<sup>0.5</sup>
	 * P<sub>n-1,n-1</sub>(x)
	 * </p>
	 * 
	 * @param l
	 *            Order of the Legendre function
	 * @param x
	 *            Point of evaluations, -1 <= x <= 1
	 * @param pl_1l_1
	 *            Value of the function P<sub>l-1,l-1</sub>(x)
	 * @return
	 */
	public static double associatedLegendreFunctionOfTheFirstKindRec2(int l, double x, double pl_1l_1) {
		return (2 * l - 1) * Math.sqrt(1 - x * x) * pl_1l_1;
	}

	/**
	 * Computes the associated Legendre function of the first kind P<sub>l,m</sub>(x) with a recursion
	 * formula, where l = m
	 * 
	 * <p>
	 * Uses recursion formula: P<sub>n,n</sub>(x) = (2n-1) (1-x<sup>2</sup>)<sup>0.5</sup>
	 * P<sub>n-1,n-1</sub>(x)
	 * </p>
	 * 
	 * @param l
	 *            Order of the Legendre function
	 * @param x
	 *            Point of evaluations, -1 <= x <= 1
	 * @param pl_1l_1
	 *            Function P<sub>l-1,l-1</sub>(x)
	 * @return
	 */
	public static UnivariateFunction associatedLegendreFunctionOfTheFirstKindRec2(final int l,
			final UnivariateFunction pl_1l_1) {
		return new UnivariateFunction() {
			@Override
			public double value(double x) {
				return (2 * l - 1) * Math.sqrt(1 - x * x) * pl_1l_1.value(x);
			}
		};
	}

	/**
	 * Computes the associated Legendre function of the first kind P<sub>l,m</sub>(x) with a recursion
	 * formula, where l-1 = m
	 * 
	 * <p>
	 * Uses recursion formula: P<sub>n,n-1</sub>(x) = (2n-1) x P<sub>n-1,n-1</sub>(x)
	 * </p>
	 * 
	 * @param l
	 *            Order of the Legendre function
	 * @param x
	 *            Point of evaluations, -1 <= x <= 1
	 * @param pl_1l_1
	 *            Value of the function P<sub>l-1,l-1</sub>(x)
	 * @return
	 */
	public static double associatedLegendreFunctionOfTheFirstKindRec3(
			int l, double x, double pl_1l_1) {
		return (2 * l - 1) * x * pl_1l_1;
	}

	/**
	 * Computes the associated Legendre function of the first kind P<sub>l,m</sub>(x) with a recursion
	 * formula, where l-1 = m
	 * 
	 * <p>
	 * Uses recursion formula: P<sub>n,n-1</sub>(x) = (2n-1) x P<sub>n-1,n-1</sub>(x)
	 * </p>
	 * 
	 * @param l
	 *            Order of the Legendre function
	 * @param pl_1l_1
	 *            Function P<sub>l-1,l-1</sub>(x)
	 * @return
	 */
	public static UnivariateFunction associatedLegendreFunctionOfTheFirstKindRec3(
			final int l, final UnivariateFunction pl_1l_1) {
		return new UnivariateFunction() {
			@Override
			public double value(double x) {
				return (2 * l - 1) * x * pl_1l_1.value(x);
			}
		};
	}

	/**
	 * Computes the associated Legendre function of the first kind P<sub>l,m</sub>(x) with a recursion
	 * formula, where l-1 = m
	 * 
	 * <p>
	 * Uses recursion formula: P<sub>n,m</sub>(x) = ((2n-1)/(n-m)) x P<sub>n-1,m</sub>(x) -
	 * ((n+m-1)/(n-m)) P<sub>n-2,m</sub>(x)
	 * </p>
	 * 
	 * @param l
	 *            Order of the Legendre function
	 * @param m
	 *            Degree of the Legendre function, where 0 <= m <= l
	 * @param x
	 *            Point of evaluations, -1 <= x <= 1
	 * @param pl_1m
	 *            Value of the function P<sub>l-1,m</sub>(x)
	 * @param pl_2m
	 *            Value of the function P<sub>l-2,m</sub>(x)
	 * @return
	 */
	public static double associatedLegendreFunctionOfTheFirstKindRec4(
			int l, int m, double x, double pl_1m, double pl_2m) {
		return ((2 * l - 1) / (l - m)) * x * pl_1m - ((l + m - 1) / (l - m)) * pl_2m;
	}

	/**
	 * Computes the associated Legendre function of the first kind P<sub>l,m</sub>(x) with a recursion
	 * formula, where l-1 = m
	 * 
	 * <p>
	 * Uses recursion formula: P<sub>n,m</sub>(x) = ((2n-1)/(n-m)) x P<sub>n-1,m</sub>(x) -
	 * ((n+m-1)/(n-m)) P<sub>n-2,m</sub>(x)
	 * </p>
	 * 
	 * @param l
	 *            Order of the Legendre function
	 * @param m
	 *            Degree of the Legendre function, where 0 <= m <= l
	 * @param x
	 *            Point of evaluations, -1 <= x <= 1
	 * @param pl_1m
	 *            Function P<sub>l-1,m</sub>(x)
	 * @param pl_2m
	 *            Function P<sub>l-2,m</sub>(x)
	 * @return
	 */
	public static UnivariateFunction associatedLegendreFunctionOfTheFirstKindRec4(final int l,
			final int m, final UnivariateFunction pl_1m, final UnivariateFunction pl_2m) {
		return new UnivariateFunction() {
			@Override
			public double value(double x) {
				return ((2 * l - 1) / (l - m)) * x * pl_1m.value(x) -
						((l + m - 1) / (l - m)) * pl_2m.value(x);
			}
		};
	}

	/**
	 * Shorthand function
	 * <p>
	 * Computes the value of a Legendre polynomial P<sub>l</sub>(x)
	 * </p>
	 * 
	 * @see be.angelcorp.celest.math.functions.LegendreUtils#legendrePolynomial(int, double)
	 */
	public static double legendreP(int l, double x) {
		return legendrePolynomial(l, x);
	}

	/**
	 * Shorthand function
	 * <p>
	 * Computes the associated Legendre function of the first kind P<sub>l,m</sub>(x)
	 * </p>
	 * 
	 * @see be.angelcorp.celest.math.functions.LegendreUtils#associatedLegendreFunctionOfTheFirstKind(int, int, double)
	 */
	public static double legendreP(int l, int m, double x) {
		return associatedLegendreFunctionOfTheFirstKind(l, m, x);
	}

	/**
	 * Computes the value of a Legendre polynomial P<sub>l</sub>(x)
	 * 
	 * 
	 * @param l
	 *            Order of the Legendre function
	 * @param x
	 *            Point of evaluations, -1 <= x <= 1
	 * @return
	 */
	public static double legendrePolynomial(int l, double x) {
		return PolynomialsUtils.createLegendrePolynomial(l).value(x);
	}

	/**
	 * Computes the value of a Legendre polynomial P<sub>l</sub>(x)
	 * 
	 * 
	 * @param l
	 *            Order of the Legendre function
	 * @param x
	 *            Point of evaluations, -1 <= x <= 1
	 * @return
	 */
	public static PolynomialFunction legendrePolynomialF(int l, double x) {
		return PolynomialsUtils.createLegendrePolynomial(l);
	}
}
