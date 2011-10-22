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
package be.angelcorp.libs.celest.time;

import junit.framework.TestCase;

public class TestTimeUtils extends TestCase {

	public void testConvtime() {
		// Fundamentals of Astrodynamics and Applications Example 3-7, page 201
		// May 14, 2004, 10:43 Mountain Standard Time
		// <=>
		// UT1=16:42:59.5367 TAI=16:43:32.0000 TT=16:44:04.1840 TDB=16:44:04.1856 TUT1=0.042633717246
		// TTT=0.042633737732 TTDB=0.042633737732
		double[] arr = TimeUtils.convtime(2004, 4, 14, 10, 43, 0, +6, -0.463326, 32);
		assertEquals(TimeUtils.hms_sec(16, 42, 59.5367), arr[0], 1E-4); // UT1
		// assertEquals(0.042633717246, arr[1], 1E-12); // T_UT1, julian centuries of UT1
		// JD of UT1
		// UTC
		assertEquals(TimeUtils.hms_sec(16, 43, 32.0), arr[4], 1E-16); // TAI
		assertEquals(TimeUtils.hms_sec(16, 44, 04.1840), arr[5], 1E-4); // TT
		// assertEquals(0.042633737732, arr[6], 1E-12); // T_TT, julian centuries of TT
		// JD of tt
		// Geocentric coordinated time
		assertEquals(TimeUtils.hms_sec(16, 44, 04.1856), arr[9], 1E-4); // TDB
		// assertEquals(0.042633737732, arr[10], 1E-12); // T_TDB, julian centuries of TDB
		// JD of tdb
	}

	public void testDayofweek() {
		// dayofweek(double)
	}

	public void testDayofyear() {
		// Fundamentals of Astrodynamics and Applications Example 3-11, page 206
		// May 8, 1992 == 129
		assertEquals(129, TimeUtils.dayofyear(1992, 5, 8, 0, 0, 0), 1E-16);
	}

	public void testDayofyear_mdhms() {
		// Fundamentals of Astrodynamics and Applications Example 3-12, page 207
		// March 18, 2001 12:14 PM == 77.5097222
		int[] arr = TimeUtils.dayofyear_mdhms(2001, 77.5097222);
		// TODO: change return from int[] so we have double precision for seconds
		assertEquals(3, arr[0]); // Month
		assertEquals(18, arr[1]); // Day
		assertEquals(12, arr[2]); // Hour
		assertEquals(14, arr[3]); // Min
		assertEquals(0, arr[4]); // Sec
	}

	public void testDms_rad() {
		// Fundamentals of Astrodynamics and Applications Example 3-8, page 204
		// -35° -15m -53.63s == -0.6154886 rad
		assertEquals(-0.6154886, TimeUtils.dms_rad(-35, -15, -53.63), 1E-7);
	}

	public void testGregorian_jd() {
		// Fundamentals of Astrodynamics and Applications Example 3-4, page 190
		// Oct 26, 1996 2:20 PM UT == 2450383.09722222
		assertEquals(2450383.09722222, TimeUtils.gregorian_jd(1996, 10, 26, 14, 20, 0), 1E-8);
	}

	public void testGstime() {
		// Fundamentals of Astrodynamics and Applications Example 3-5, page 194
		// August 20, 1992, 12:14 PM UT1
		double jdut1 = TimeUtils.jday(1992, 8, 20, 12, 14, 0);
		assertEquals(152.578787886 * (Math.PI / 180.), TimeUtils.gstime(jdut1), 1E-8);
	}

	public void testHms_rad() {
		// Fundamentals of Astrodynamics and Applications Example 3-9, page 205
		// 15h 15m 53.63s == 3.996341 rad
		assertEquals(3.996341, TimeUtils.hms_rad(15, 15, 53.63), 1E-6);
	}

	public void testHms_sec() {
		// Fundamentals of Astrodynamics and Applications Example 3-10, page 206
		// 13h 22m 45.98s == 48165.98
		assertEquals(48165.98, TimeUtils.hms_sec(13, 22, 45.98), 1E-2);
	}

	public void testHms_ut() {
		// hms_ut(int, int, double)
	}

	public void testInvjday() {
		// Fundamentals of Astrodynamics and Applications Example 3-13, page 209
		// JD 2449877.3458762 == 1995, June 8 20:18:3.70368
		int[] arr = TimeUtils.invjday(2449877.3458762);
		// TODO: change return from int[] so we have double precision for seconds
		assertEquals(1995, arr[0]); // year
		assertEquals(6, arr[1]); // month
		assertEquals(8, arr[2]); // day
		assertEquals(20, arr[3]); // hour
		assertEquals(18, arr[4]); // minute
		assertEquals(4, arr[5]); // = 3.70368 second
	}

	public void testJd2sse() {
		// jd2sse(double)
	}

	public void testJday() {
		// Fundamentals of Astrodynamics and Applications Example 3-4, page 190
		// Oct 26, 1996 2:20 PM UT == 2450383.09722222
		assertEquals(2450383.09722222, TimeUtils.jday(1996, 10, 26, 14, 20, 0), 1E-8);
	}

	public void testLstime() {
		// Fundamentals of Astrodynamics and Applications Example 3-5, page 194
		// August 20, 1992, 12:14 PM UT1 at 104° W long ==
		double jdut1 = TimeUtils.jday(1992, 8, 20, 12, 14, 0);
		double[] arr = TimeUtils.lstime(-104. * Math.PI / 180., jdut1);
		assertEquals(48.578787886 * (Math.PI / 180.), arr[0], 1E-8);
		assertEquals(152.578787886 * (Math.PI / 180.), arr[1], 1E-8);
	}

	public void testRad_Dms() {
		// Fundamentals of Astrodynamics and Applications Example 3-8, page 204
		// -35° -15m -53.63s == -0.6154886 rad
		int[] arr = TimeUtils.rad_dms(-0.6154886);
		// TODO: change return from int[] so we have double precision for seconds
		assertEquals(-35, arr[0]); // Degrees
		assertEquals(-15, arr[1]); // Min
		assertEquals(-54, arr[2]); // =53.63 Sec
	}

	public void testRad_hms() {
		// Fundamentals of Astrodynamics and Applications Example 3-9, page 205
		// 15h 15m 53.63s == 3.996341 rad
		int[] arr = TimeUtils.rad_hms(3.996341);
		// TODO: change return from int[] so we have double precision for seconds
		assertEquals(15, arr[0]); // Hour
		assertEquals(15, arr[1]); // Min
		assertEquals(54, arr[2]); // =53.63 Sec
	}

	public void testSec_hms() {
		// Fundamentals of Astrodynamics and Applications Example 3-10, page 206
		// 13h 22m 45.98s == 48165.98
		int[] arr = TimeUtils.sec_hms(48165.98);
		// TODO: change return from int[] so we have double precision for seconds
		assertEquals(13, arr[0]); // Hour
		assertEquals(22, arr[1]); // Min
		assertEquals(46, arr[2]); // =45.98 Sec
	}

	public void testUt_hms() {
		// hms_ut(double)
	}
}