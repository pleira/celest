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

import static java.lang.Math.PI;
import static java.lang.Math.floor;
import static java.lang.Math.round;

/**
 * Large suite of time utils, based partially on the code of fundamentals of astrodynamics and
 * applications, by David Vallado
 * 
 * @author simon
 * 
 */
public abstract class TimeUtils {

	public enum type {
		JULIAN, GREGORIAN
	}

	/**
	 * Find the time parameters and julian century values for inputs of utc or ut1. numerous outputs are
	 * found as shown in the local variables. Because calucations are in utc, you must include timezone
	 * if ( you enter a local time, otherwise it should be zero.
	 * 
	 * @param year
	 *            year ; 1900 .. 2100
	 * @param mon
	 *            month ; 1 .. 12
	 * @param day
	 *            day ; 1 .. 28,29,30,31
	 * @param hr
	 *            universal time hour ; 0 .. 23
	 * @param min
	 *            universal time min ; 0 .. 59
	 * @param sec
	 *            universal time sec (utc) ; 0.0 .. 59.999
	 * @param timezone
	 *            offset to utc from local site ; 0 .. 23 hr
	 * @param dut1
	 *            delta of ut1 - utc ; sec
	 * @param dat
	 *            delta of utc - tai ; sec
	 * 
	 * @return double[] containing;
	 *         <ul>
	 *         <li>universal time ;sec</li>
	 *         <li>julian centuries of ut1</li>
	 *         <li>julian date of ut1</li>
	 *         <li>coordinated universal time ;sec</li>
	 *         <li>atomic time ;sec</li>
	 *         <li>terrestrial dynamical time ;sec</li>
	 *         <li>julian centuries of tdt</li>
	 *         <li>julian date of tdt</li>
	 *         <li>geocentric coordinate time ; sec</li>
	 *         <li>terrestrial barycentric time ; sec</li>
	 *         <li>julian centuries of tdb</li>
	 *         <li>julian date of tdb</li>
	 *         </ul>
	 */
	public static double[] convtime(int year, int mon, int day, int hr, int min, double sec, int timezone,
			double dut1, int dat) {
		double ut1, tut1, jdut1, utc, tai, tt, ttt, jdtt, tcg, tdb, ttdb, jdtdb, tcb;
		double deg2rad, sectemp, me;
		int localhr, hrtemp, mintemp;

		deg2rad = PI / 180.0;

		// ------------------------ implementation ------------------
		// double jd = jday(year, mon, day, 0, 0, 0.0);
		// double mjd = jd - 2400000.5;
		// double mfme = hr*60.0 + min + sec/60.0;

		// ------------------ start if ( ut1 is known ------------------
		localhr = timezone + hr;

		utc = hms_sec(localhr, min, sec);
		ut1 = utc + dut1;
		int[] tmpArr = hms_sec(ut1);
		hrtemp = tmpArr[0];
		mintemp = tmpArr[1];
		sectemp = tmpArr[2];
		jdut1 = jday(year, mon, day, hrtemp, mintemp, sectemp);
		tut1 = (jdut1 - 2451545.0) / 36525.0;

		tai = utc + dat;

		tt = tai + 32.184; // sec
		tmpArr = hms_sec(tt);
		hrtemp = tmpArr[0];
		mintemp = tmpArr[1];
		sectemp = tmpArr[2];
		jdtt = jday(year, mon, day, hrtemp, mintemp, sectemp);
		ttt = (jdtt - 2451545.0) / 36525.0;

		tcg = tt + 6.969290134e-10 * (jdut1 - 2443144.5) * 86400.0; // sec

		me = 357.5277233 + 35999.05034 * ttt;
		me = Math.IEEEremainder(me, 360.0);
		me = me * deg2rad;
		tdb = tt + 0.001657 * Math.sin(me) + 0.00001385 * Math.sin(2.0 * me);
		tmpArr = hms_sec(tdb);
		hrtemp = tmpArr[0];
		mintemp = tmpArr[1];
		sectemp = tmpArr[2];
		jdtdb = jday(year, mon, day, hrtemp, mintemp, sectemp);
		ttdb = (jdtdb - 2451545.0) / 36525.0;

		tcb = tdb + 1.55051976772e-8 * (jdtt - 2443144.5) * 86400.0; // sec
		return new double[] { ut1, tut1, jdut1, utc, tai, tt, ttt, jdtt, tcg, tdb, ttdb, jdtdb, tcb };
	}

	/**
	 * Function to find the day of the week. Integers are used for the days, 1 = 'Sun', 2 = 'Mon', ... 7
	 * = 'Sat'.
	 * 
	 * @param jd
	 *            Julian date of interest
	 * 
	 * @return dayofweek - answer 1 to 7
	 */
	public static int dayofweek(double jd) {
		// ----- Be sure jd is at 0.0 h on the day of interest -----
		jd = floor(jd); // floor(0.5+jd)

		int temp = (int) floor(jd - 7 * floor((jd + 1) / 7) + 2);
		return temp;
	}

	/**
	 * Convert the day of the year, days, to the equivalent month day, hour, minute and second.
	 * 
	 * @param year
	 *            year e.g. 1900 .. 2100
	 * @param days
	 *            julian day of the year e.g. 0 .. 366.0
	 * 
	 * @return int[] containing:
	 *         <ul>
	 *         <li>month e.g. 1 .. 12</li>
	 *         <li>day e.g. 1 .. 28,29,30,31</li>
	 *         <li>hour e.g. 0 .. 23</li>
	 *         <li>minute e.g. 0 .. 59</li>
	 *         <li>second e.g. 0.0 .. 59.999</li>
	 *         </ul>
	 */
	public static int[] days2mdhms(int year, double days) {
		int mon, day, hr, minute;
		double sec;

		int i, inttemp, dayofyr;
		double temp;
		int lmonth[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		dayofyr = (int) floor(days);
		/* ----------------- find month and day of month ---------------- */
		if ((year % 4) == 0)
			lmonth[1] = 29;

		i = 1;
		inttemp = 0;
		while ((dayofyr > inttemp + lmonth[i - 1]) && (i < 12)) {
			inttemp = inttemp + lmonth[i - 1];
			i++;
		}
		mon = i;
		day = dayofyr - inttemp;

		/* ----------------- find hours minutes and seconds ------------- */
		temp = (days - dayofyr) * 24.0;
		hr = (int) floor(temp);
		temp = (temp - hr) * 60.0;
		minute = (int) floor(temp);
		sec = (temp - minute) * 60.0;
		return new int[] { mon, day, hr, minute, (int) round(sec) };
	}

	/**
	 * Convert degrees, minutes and seconds into radians.
	 * 
	 * @param dms
	 *            dms ; rad
	 * 
	 * @return double[] containing;
	 *         <ul>
	 *         <li>degrees ; 0 .. 360</li>
	 *         <li>minutes ; 0 .. 59</li>
	 *         <li>seconds ; 0.0 .. 59.99</li>
	 *         </ul>
	 */
	public static int[] dms_rad(double dms) {
		double rad2deg = 180 / PI;
		double temp = dms * rad2deg;

		int deg = (int) floor(temp);
		int min = (int) floor((temp - deg) * 60.0);
		double sec = (temp - deg - min / 60.0) * 3600.0;
		return new int[] { deg, min, (int) round(sec) };
	}

	/**
	 * Convert degrees, minutes and seconds into radians.
	 * 
	 * @param deg
	 *            degrees ; 0 .. 360
	 * @param min
	 *            minutes ; 0 .. 59
	 * @param sec
	 *            seconds ; 0.0 .. 59.99
	 * 
	 * @return result ; rad
	 */
	public static double dms_rad(int deg, int min, double sec) {
		double rad2deg = 180 / PI;
		return (deg + min / 60.0 + sec / 3600.0) / rad2deg;
	}

	/**
	 * find the fractional days through a year given the year, month, day, hour, minute and second.
	 * 
	 * 
	 * @param year
	 *            year e.g. 1900 .. 2100
	 * @param mon
	 *            month e.g. 1 .. 12
	 * @param day
	 *            day e.g. 1 .. 28,29,30,31
	 * @param hr
	 *            hour e.g. 0 .. 23
	 * @param min
	 *            minute e.g. 0 .. 59
	 * @param sec
	 *            second e.g. 0.0 .. 59.999
	 * 
	 * @return day of year plus fraction of a day
	 */
	public static double finddays(int year, int month, int day, int hr, int minute, double sec) {
		double days;
		int lmonth[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int i;

		if (((year - 1900) % 4) == 0)
			lmonth[1] = 29;

		i = 1;
		days = 0.0;
		while ((i < month) && (i < 12)) {
			days = days + lmonth[i - 1];
			i = i + 1;
		}
		days = days + day + hr / 24.0 + minute / 1440.0 + sec / 86400.0;
		return days;
	}

	/**
	 * Find the greenwich sidereal time (iau-82).
	 * 
	 * @param jdut1
	 *            julian date in ut1
	 * 
	 * @return greenwich sidereal time ; 0 to 2pi rad
	 * 
	 */
	public static double gstime(double jdut1) {
		double twopi = 2.0 * PI;
		double deg2rad = PI / 180.0;
		double temp, tut1;

		tut1 = (jdut1 - 2451545.0) / 36525.0;
		temp = -6.2e-6 * tut1 * tut1 * tut1 + 0.093104 * tut1 * tut1 +
					(876600.0 * 3600 + 8640184.812866) * tut1 + 67310.54841; // sec
		temp = Math.IEEEremainder(temp * deg2rad / 240.0, 2 * PI); // 360/86400 = 1/240, to deg, to rad

		// ------------------------ check quadrants ---------------------
		if (temp < 0.0)
			temp += twopi;

		return temp;
	}

	/**
	 * Converts hours, minutes and seconds into radians.
	 * 
	 * 
	 * @param hms
	 *            Radians
	 * 
	 * @return int[] containing:
	 *         <ul>
	 *         <li>hours ; 0 .. 24</li>
	 *         <li>minutes ; 0 .. 59</li>
	 *         <li>seconds ; 0.0 .. 59.99</li>
	 *         </ul>
	 */
	public static int[] hms_rad(double hms) {
		int hr, min;
		double sec;
		double rad2deg = 180. / PI;
		double temp;

		// ------------------------ implementation ------------------
		temp = 15.0 / rad2deg;
		temp = hms / temp;
		hr = (int) (temp);
		min = (int) ((temp - hr) * 60.0);
		sec = (temp - hr - min / 60.0) * 3600.0;
		return new int[] { hr, min, (int) round(sec) };
	}

	/**
	 * Converts hours, minutes and seconds into radians.
	 * 
	 * @param hr
	 *            hours ; 0 .. 24
	 * @param min
	 *            minutes ; 0 .. 59
	 * @param sec
	 *            seconds ; 0.0 .. 59.99
	 * 
	 * @return radians
	 * 
	 */
	public static double hms_rad(int hr, int min, double sec) {
		return hr + min / 60.0 + sec / 3600.0;
	}

	/**
	 * Convert hours, minutes and seconds to seconds from the beginning of the day.
	 * 
	 * @param utsec
	 *            seconds ; 0.0 .. 86400.0
	 * 
	 * @return int[] containing
	 *         <ul>
	 *         <li>hours ;0 .. 24</li>
	 *         <li>minutes ; 0 .. 59</li>
	 *         <li>seconds ;0.0 .. 59.99</li>
	 *         </ul>
	 * 
	 */
	public static int[] hms_sec(double utsec) {
		int hr, min;
		double sec, temp;

		// ------------------------ implementation ------------------
		temp = utsec / 3600.0;
		hr = (int) floor(temp);
		min = (int) floor((temp - hr) * 60.0);
		sec = (temp - hr - min / 60.0) * 3600.0;
		return new int[] { hr, min, (int) round(sec) };
	}

	/**
	 * Convert seconds from the beginning of the day to hours, minutes and seconds.
	 * 
	 * @param hr
	 *            hours ;0 .. 24
	 * @param min
	 *            minutes ; 0 .. 59
	 * @param sec
	 *            seconds ;0.0 .. 59.99
	 * 
	 * @return seconds ; 0.0 .. 86400.0
	 * 
	 */
	public static double hms_sec(int hr, int min, double sec) {
		return hr * 3600.0 + min * 60.0 + sec;
	}

	/**
	 * Converts hours, minutes and seconds into universal time.
	 * 
	 * @param ut
	 *            universal time
	 * @return int[] containing:
	 *         <ul>
	 *         <li>hours ; 0 .. 24</li>
	 *         <li>minutes ; 0 .. 59</li>
	 *         <li>seconds ; 0.0 .. 59.99</li>
	 *         </ul>
	 */
	public static int[] hms_ut(double ut) {
		int hr = (int) floor(ut * 0.01);
		int min = (int) floor(ut - hr * 100.0);
		double sec = (ut - hr * 100.0 - min) * 100.0;
		return new int[] { hr, min, (int) round(sec) };
	}

	/**
	 * Converts hours, minutes and seconds into universal time.
	 * 
	 * @param hr
	 *            hours ; 0 .. 24
	 * @param min
	 *            minutes ; 0 .. 59
	 * @param sec
	 *            seconds ; 0.0 .. 59.99
	 * 
	 * @return universal time
	 * 
	 */
	public static double hms_ut(int hr, int min, double sec) {
		return hr * 100.0 + min + sec * 0.01;
	}

	/**
	 * find the year, month, day, hour, minute and second given the julian date. tu can be ut1, tdt, tdb,
	 * etc.
	 * 
	 * @param jd
	 *            julian date
	 * 
	 * @return int[] containing:
	 *         <ul>
	 *         <li>year e.g. 1900 .. 2100</li>
	 *         <li>month e.g. 1 .. 12</li>
	 *         <li>day e.g. 1 .. 28,29,30,31</li>
	 *         <li>hour e.g. 0 .. 23</li>
	 *         <li>minute e.g. 0 .. 59</li>
	 *         <li>second e.g. 0.0 .. 59.999</li>
	 *         </ul>
	 */
	public static int[] invjday(double jd) {
		int year;

		int leapyrs;
		double days, tu, temp;

		/* --------------- find year and days of the year --------------- */
		temp = jd - 2415019.5;
		tu = temp / 365.25;
		year = 1900 + (int) floor(tu);
		leapyrs = (int) floor((year - 1901) * 0.25);

		// nudge by 8.64x10-7 sec to get even outputs
		days = temp - ((year - 1900) * 365.0 + leapyrs) + 0.00000000001;

		/* ------------ check for case of beginning of a year ----------- */
		if (days < 1.0) {
			year = year - 1;
			leapyrs = (int) floor((year - 1901) * 0.25);
			days = temp - ((year - 1900) * 365.0 + leapyrs);
		}

		/* ----------------- find remaing data ------------------------- */
		int[] mdhms = days2mdhms(year, days);
		double sec = mdhms[4]; // - 0.00000086400;
		return new int[] { year, mdhms[0], mdhms[1], mdhms[2], mdhms[3], (int) round(sec) };
	}

	/**
	 * Find the modified Julian date from the normal Julian date
	 * 
	 * @param jd
	 *            Julian date
	 * @return Modified Julian date
	 */
	public static double jd_mjd(double jd) {
		return jd - 2400000.5;
	}

	/**
	 * Find the seconds since epoch (1 Jan 2000) given the julian date
	 * 
	 * @param jd
	 *            julian date
	 * @return seconds since epoch 1 jan 2000
	 */
	public static double jd2sse(double jd) {
		double temp;
		temp = (jd - 2451544.5) * 86400.0;
		return temp;
	}

	/**
	 * Find the julian date given the year, month, day, and time. the julian date is defined by each
	 * elapsed day since noon, jan 1, 4713 bc.
	 * 
	 * @param year
	 *            year e.g. 1900 .. 2100
	 * @param mon
	 *            month e.g. 1 .. 12
	 * @param day
	 *            day e.g. 1 .. 28,29,30,31
	 * @param hr
	 *            universal time hour e.g. 0 .. 23
	 * @param minute
	 *            universal time min e.g. 0 .. 59
	 * @param sec
	 *            universal time sec e.g. 0.0 .. 59.999
	 * @return julian date
	 */
	public static double jday(int year, int mon, int day, int hr, int minute, double sec) {
		return 367.0 * year -
				floor((7 * (year + floor((mon + 9) / 12.0))) * 0.25) +
				floor(275 * mon / 9.0) +
				day + 1721013.5 +
				((sec / 60.0 + minute) / 60.0 + hr) / 24.0; // ut in days
		// - 0.5*sgn(100.0*year + mon - 190002.5) + 0.5;
	}

	/**
	 * Find the julian date given the year, month, day, and time. the julian date is defined by each
	 * elapsed day since noon, jan 1, 4713 bc.
	 * 
	 * @param year
	 *            year e.g. all, 1900 .. 2100
	 * @param mon
	 *            month e.g. 1 .. 12
	 * @param day
	 *            day e.g. 1 .. 28,29,30,31
	 * @param hr
	 *            universal time hour e.g. 0 .. 23
	 * @param min
	 *            universal time min e.g. 0 .. 59
	 * @param sec
	 *            universal time sec e.g. 0.0 .. 59.999
	 * @param whichtype
	 *            julian or gregorian calender
	 * @return julian date
	 */
	public static double jdayall(int year, int mon, int day, int hr, int minute, double sec, type whichtype) {
		double b, jd;
		if (mon <= 2) {
			year = year - 1;
			mon = mon + 12;
		}
		/* --------- use for julian calender, every 4 years --------- */
		if (whichtype == type.JULIAN) {
			b = 0.0;
			return b;
		} else {
			/* ---------------------- use for gregorian ----------------- */
			b = 2 - floor(year * 0.01) + floor(floor(year * 0.01) * 0.25);
			jd = floor(365.25 * (year + 4716)) +
					floor(30.6001 * (mon + 1)) +
					day + b - 1524.5 +
					((sec / 60.0 + minute) / 60.0 + hr) / 24.0; // ut in days
			return jd;
		}
	}

	/**
	 * Find the local sidereal time at a given location.
	 * 
	 * @param lon
	 *            site longitude (west -) ; -2pi to 2pi rad
	 * @param jdut1
	 *            julian date in ut1
	 * 
	 * @return double[] containing;
	 *         <ul>
	 *         <li>local sidereal time ; 0.0 to 2pi rad</li>
	 *         <li>greenwich sidereal time ; 0.0 to 2pi rad</li>
	 *         </ul>
	 */
	public static double[] lstime(double lon, double jdut1) {
		double lst, gst;
		double twopi = 2.0 * PI;

		gst = gstime(jdut1);
		lst = lon + gst;

		/* ------------------------ check quadrants --------------------- */
		lst = Math.IEEEremainder(lst, twopi);
		if (lst < 0.0)
			lst = lst + twopi;
		return new double[] { lst, gst };
	}

	/**
	 * Convert the modified julian date to the normal julian date
	 * 
	 * @param mjd
	 *            Modified julian date
	 * @return Julian date
	 */
	public static double mjd_jd(double mjd) {
		return mjd + 2400000.5;
	}

}
