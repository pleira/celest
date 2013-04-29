/**
 * Copyright (C) 2012 Simon Billemont <simon@angelcorp.be>
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

package be.angelcorp.libs.celest.frames.implementations.transforms

import be.angelcorp.libs.celest.time.{JulianDate, IJulianDate}
import be.angelcorp.libs.util.physics.{Time, Angle}
import be.angelcorp.libs.math.MathUtils2._
import math._
import be.angelcorp.libs.celest.universe.Universe

class IAU1980(implicit universe: Universe) {

	/* Table of IAY 1980 arguments and coefficients */
	/* Sin and cos [0.1 mas per Julian century] */
	lazy val coefficients = List(
		// l, l',  F,  D, Om,    long A,long A',   obli B, obli B'
		(  0,  0,  0,  0,  1, -171996.0, -174.2,  92025.0,    8.9 ),
		(  0,  0,  0,  0,  2,    2062.0,    0.2,   -895.0,    0.5 ),
		( -2,  0,  2,  0,  1,      46.0,    0.0,    -24.0,    0.0 ),
		(  2,  0, -2,  0,  0,      11.0,    0.0,      0.0,    0.0 ),
		( -2,  0,  2,  0,  2,      -3.0,    0.0,      1.0,    0.0 ),
		(  1, -1,  0, -1,  0,      -3.0,    0.0,      0.0,    0.0 ),
		(  0, -2,  2, -2,  1,      -2.0,    0.0,      1.0,    0.0 ),
		(  2,  0, -2,  0,  1,       1.0,    0.0,      0.0,    0.0 ),
		(  0,  0,  2, -2,  2,  -13187.0,   -1.6,   5736.0,   -3.1 ),
		(  0,  1,  0,  0,  0,    1426.0,   -3.4,     54.0,   -0.1 ),
		(  0,  1,  2, -2,  2,    -517.0,    1.2,    224.0,   -0.6 ),
		(  0, -1,  2, -2,  2,     217.0,   -0.5,    -95.0,    0.3 ),
		(  0,  0,  2, -2,  1,     129.0,    0.1,    -70.0,    0.0 ),
		(  2,  0,  0, -2,  0,      48.0,    0.0,      1.0,    0.0 ),
		(  0,  0,  2, -2,  0,     -22.0,    0.0,      0.0,    0.0 ),
		(  0,  2,  0,  0,  0,      17.0,   -0.1,      0.0,    0.0 ),
		(  0,  1,  0,  0,  1,     -15.0,    0.0,      9.0,    0.0 ),
		(  0,  2,  2, -2,  2,     -16.0,    0.1,      7.0,    0.0 ),
		(  0, -1,  0,  0,  1,     -12.0,    0.0,      6.0,    0.0 ),
		( -2,  0,  0,  2,  1,      -6.0,    0.0,      3.0,    0.0 ),
		(  0, -1,  2, -2,  1,      -5.0,    0.0,      3.0,    0.0 ),
		(  2,  0,  0, -2,  1,       4.0,    0.0,     -2.0,    0.0 ),
		(  0,  1,  2, -2,  1,       4.0,    0.0,     -2.0,    0.0 ),
		(  1,  0,  0, -1,  0,      -4.0,    0.0,      0.0,    0.0 ),
		(  2,  1,  0, -2,  0,       1.0,    0.0,      0.0,    0.0 ),
		(  0,  0, -2,  2,  1,       1.0,    0.0,      0.0,    0.0 ),
		(  0,  1, -2,  2,  0,      -1.0,    0.0,      0.0,    0.0 ),
		(  0,  1,  0,  0,  2,       1.0,    0.0,      0.0,    0.0 ),
		( -1,  0,  0,  1,  1,       1.0,    0.0,      0.0,    0.0 ),
		(  0,  1,  2, -2,  0,      -1.0,    0.0,      0.0,    0.0 ),
		(  0,  0,  2,  0,  2,   -2274.0,   -0.2,    977.0,   -0.5 ),
		(  1,  0,  0,  0,  0,     712.0,    0.1,     -7.0,    0.0 ),
		(  0,  0,  2,  0,  1,    -386.0,   -0.4,    200.0,    0.0 ),
		(  1,  0,  2,  0,  2,    -301.0,    0.0,    129.0,   -0.1 ),
		(  1,  0,  0, -2,  0,    -158.0,    0.0,     -1.0,    0.0 ),
		( -1,  0,  2,  0,  2,     123.0,    0.0,    -53.0,    0.0 ),
		(  0,  0,  0,  2,  0,      63.0,    0.0,     -2.0,    0.0 ),
		(  1,  0,  0,  0,  1,      63.0,    0.1,    -33.0,    0.0 ),
		( -1,  0,  0,  0,  1,     -58.0,   -0.1,     32.0,    0.0 ),
		( -1,  0,  2,  2,  2,     -59.0,    0.0,     26.0,    0.0 ),
		(  1,  0,  2,  0,  1,     -51.0,    0.0,     27.0,    0.0 ),
		(  0,  0,  2,  2,  2,     -38.0,    0.0,     16.0,    0.0 ),
		(  2,  0,  0,  0,  0,      29.0,    0.0,     -1.0,    0.0 ),
		(  1,  0,  2, -2,  2,      29.0,    0.0,    -12.0,    0.0 ),
		(  2,  0,  2,  0,  2,     -31.0,    0.0,     13.0,    0.0 ),
		(  0,  0,  2,  0,  0,      26.0,    0.0,     -1.0,    0.0 ),
		( -1,  0,  2,  0,  1,      21.0,    0.0,    -10.0,    0.0 ),
		( -1,  0,  0,  2,  1,      16.0,    0.0,     -8.0,    0.0 ),
		(  1,  0,  0, -2,  1,     -13.0,    0.0,      7.0,    0.0 ),
		( -1,  0,  2,  2,  1,     -10.0,    0.0,      5.0,    0.0 ),
		(  1,  1,  0, -2,  0,      -7.0,    0.0,      0.0,    0.0 ),
		(  0,  1,  2,  0,  2,       7.0,    0.0,     -3.0,    0.0 ),
		(  0, -1,  2,  0,  2,      -7.0,    0.0,      3.0,    0.0 ),
		(  1,  0,  2,  2,  2,      -8.0,    0.0,      3.0,    0.0 ),
		(  1,  0,  0,  2,  0,       6.0,    0.0,      0.0,    0.0 ),
		(  2,  0,  2, -2,  2,       6.0,    0.0,     -3.0,    0.0 ),
		(  0,  0,  0,  2,  1,      -6.0,    0.0,      3.0,    0.0 ),
		(  0,  0,  2,  2,  1,      -7.0,    0.0,      3.0,    0.0 ),
		(  1,  0,  2, -2,  1,       6.0,    0.0,     -3.0,    0.0 ),
		(  0,  0,  0, -2,  1,      -5.0,    0.0,      3.0,    0.0 ),
		(  1, -1,  0,  0,  0,       5.0,    0.0,      0.0,    0.0 ),
		(  2,  0,  2,  0,  1,      -5.0,    0.0,      3.0,    0.0 ),
		(  0,  1,  0, -2,  0,      -4.0,    0.0,      0.0,    0.0 ),
		(  1,  0, -2,  0,  0,       4.0,    0.0,      0.0,    0.0 ),
		(  0,  0,  0,  1,  0,      -4.0,    0.0,      0.0,    0.0 ),
		(  1,  1,  0,  0,  0,      -3.0,    0.0,      0.0,    0.0 ),
		(  1,  0,  2,  0,  0,       3.0,    0.0,      0.0,    0.0 ),
		(  1, -1,  2,  0,  2,      -3.0,    0.0,      1.0,    0.0 ),
		( -1, -1,  2,  2,  2,      -3.0,    0.0,      1.0,    0.0 ),
		( -2,  0,  0,  0,  1,      -2.0,    0.0,      1.0,    0.0 ),
		(  3,  0,  2,  0,  2,      -3.0,    0.0,      1.0,    0.0 ),
		(  0, -1,  2,  2,  2,      -3.0,    0.0,      1.0,    0.0 ),
		(  1,  1,  2,  0,  2,       2.0,    0.0,     -1.0,    0.0 ),
		( -1,  0,  2, -2,  1,      -2.0,    0.0,      1.0,    0.0 ),
		(  2,  0,  0,  0,  1,       2.0,    0.0,     -1.0,    0.0 ),
		(  1,  0,  0,  0,  2,      -2.0,    0.0,      1.0,    0.0 ),
		(  3,  0,  0,  0,  0,       2.0,    0.0,      0.0,    0.0 ),
		(  0,  0,  2,  1,  2,       2.0,    0.0,     -1.0,    0.0 ),
		( -1,  0,  0,  0,  2,       1.0,    0.0,     -1.0,    0.0 ),
		(  1,  0,  0, -4,  0,      -1.0,    0.0,      0.0,    0.0 ),
		( -2,  0,  2,  2,  2,       1.0,    0.0,     -1.0,    0.0 ),
		( -1,  0,  2,  4,  2,      -2.0,    0.0,      1.0,    0.0 ),
		(  2,  0,  0, -4,  0,      -1.0,    0.0,      0.0,    0.0 ),
		(  1,  1,  2, -2,  2,       1.0,    0.0,     -1.0,    0.0 ),
		(  1,  0,  2,  2,  1,      -1.0,    0.0,      1.0,    0.0 ),
		( -2,  0,  2,  4,  2,      -1.0,    0.0,      1.0,    0.0 ),
		( -1,  0,  4,  0,  2,       1.0,    0.0,      0.0,    0.0 ),
		(  1, -1,  0, -2,  0,       1.0,    0.0,      0.0,    0.0 ),
		(  2,  0,  2, -2,  1,       1.0,    0.0,     -1.0,    0.0 ),
		(  2,  0,  2,  2,  2,      -1.0,    0.0,      0.0,    0.0 ),
		(  1,  0,  0,  2,  1,      -1.0,    0.0,      0.0,    0.0 ),
		(  0,  0,  4, -2,  2,       1.0,    0.0,      0.0,    0.0 ),
		(  3,  0,  2, -2,  2,       1.0,    0.0,      0.0,    0.0 ),
		(  1,  0,  2, -2,  0,      -1.0,    0.0,      0.0,    0.0 ),
		(  0,  1,  2,  0,  1,       1.0,    0.0,      0.0,    0.0 ),
		( -1, -1,  0,  2,  1,       1.0,    0.0,      0.0,    0.0 ),
		(  0,  0, -2,  0,  1,      -1.0,    0.0,      0.0,    0.0 ),
		(  0,  0,  2, -1,  2,      -1.0,    0.0,      0.0,    0.0 ),
		(  0,  1,  0,  2,  0,      -1.0,    0.0,      0.0,    0.0 ),
		(  1,  0, -2, -2,  0,      -1.0,    0.0,      0.0,    0.0 ),
		(  0, -1,  2,  0,  1,      -1.0,    0.0,      0.0,    0.0 ),
		(  1,  1,  0, -2,  1,      -1.0,    0.0,      0.0,    0.0 ),
		(  1,  0, -2,  2,  0,      -1.0,    0.0,      0.0,    0.0 ),
		(  2,  0,  0,  2,  0,       1.0,    0.0,      0.0,    0.0 ),
		(  0,  0,  2,  4,  2,      -1.0,    0.0,      0.0,    0.0 ),
		(  0,  1,  0,  1,  0,       1.0,    0.0,      0.0,    0.0 )
	)

	/**
	 *  Calculate the nutation parameters dpsi, deps according to IAU 1980
	 *  nutation up to 0.01sec terms from S. Tab 3.222.1
	 *
	 * @param date Epoch to calculate the nutation parameters.
	 * @return (dPsi, dEpsilon) [rad]
	 */
	def nutation(date: IJulianDate) = {
		val ArcSeconds = Angle.convert(1,    Angle.ARCSECOND, Angle.RADIAN) // as => rad
		val U2R   = Angle.convert(1E-4, Angle.ARCSECOND, Angle.RADIAN) // 0.1 mas => rad
		val dt = date.relativeTo( universe.J2000_EPOCH, Time.century_julian )

		// Mean longitude of Moon minus mean longitude of Moon's perigee.
		val el = mod(
			(485866.733 + (715922.633 + (31.310 + 0.064 * dt) * dt) * dt) * ArcSeconds + mod(1325.0 * dt, 1.0) * TwoPi,
			TwoPi
		)

		// Mean longitude of Sun minus mean longitude of Sun's perigee.
		val elp = mod(
			(1287099.804 + (1292581.224 + (-0.577 - 0.012 * dt) * dt) * dt) * ArcSeconds + mod(99.0 * dt, 1.0) * TwoPi,
			TwoPi
		)

		// Mean longitude of Moon minus mean longitude of Moon's node.
		val f = mod(
			(335778.877 + (295263.137 + (-13.257 + 0.011 * dt) * dt) * dt) * ArcSeconds + mod(1342.0 * dt, 1.0) * TwoPi,
			TwoPi
		)

		// Mean elongation of Moon from Sun.
		val d = mod(
			(1072261.307 + (1105601.328 + (-6.891 + 0.019 * dt) * dt) * dt) * ArcSeconds + mod(1236.0 * dt, 1.0) * TwoPi,
			TwoPi
		)

		// Longitude of the mean ascending node of the lunar orbit on the ecliptic, from the mean equinox of date. */
		val om = mod(
			(450160.280 + (-482890.539 + (7.455 + 0.008 * dt) * dt) * dt) * ArcSeconds + mod(-5.0 * dt, 1.0) * TwoPi,
			TwoPi
		)

		/* Sum the nutation terms, ending with the biggest. */
		val (dpsi, deps) = coefficients.foldLeft((0.0,0.0))( (dpde, coef) => {
			val (l_, lPrime_ , f_, d_, om_, a_, aPrime_, b_, bPrime_) = coef
			val arg = l_ * el + lPrime_ * elp + f_ * f + d_ * d + om_ * om

			val s = a_ + aPrime_ * dt
			val c = b_ + bPrime_ * dt

			val dpsi = if (s != 0.0) dpde._1 + s * sin(arg) else dpde._1
			val deps = if (c != 0.0) dpde._2 + c * cos(arg) else dpde._2

			(dpsi, deps)
		} )

		/* Convert results from 0.1 mas units to radians. */
		(dpsi * U2R, deps * U2R)
	}

}
