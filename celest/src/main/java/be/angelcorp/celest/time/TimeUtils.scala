/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.celest.time

import math._
import be.angelcorp.celest.math._

/**
 * Large suite of time utils, based partially on the code of fundamentals of astrodynamics and
 * applications, by David Vallado
 *
 * @author Simon Billemont
 */
object TimeUtils {

  /**
   * Function to find the day of the week. Integers are used for the days,
   *
   * 1 = Sun,
   * 2 = Mon,
   * ...
   * 7 = Sat.
   *
   * @param jd Julian date of interest
   *
   * @return dayofweek - answer 1 to 7
   */
  def dayOfWeek(jd: Double): Int = {
    // ----- Be sure jd is at 0.0 h on the day of interest -----
    val jd2 = floor(0.5 + jd)

    floor(jd2 - 7 * floor((jd2 + 1) / 7) + 2).toInt
  }

  /**
   * find the fractional days through a year given the year, month, day, hour, minute and second.
   *
   *
   * @param year  Year e.g. 1900 .. 2100
   * @param month Month e.g. 1 .. 12
   * @param day   Day e.g. 1 .. 28,29,30,31
   * @param hr    Hour e.g. 0 .. 23
   * @param minute Minute e.g. 0 .. 59
   * @param sec   Second e.g. 0.0 .. 59.999
   * @return Day of year plus fraction of a day.
   */
  def dayofyear(year: Int, month: Int, day: Int, hr: Int, minute: Int, sec: Double): Double = {
    var lmonth = Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    if (((year - 1900) % 4) == 0)
      lmonth(1) = 29

    var i = 1
    var days = 0.0
    while ((i < month) && (i < 12)) {
      days = days + lmonth(i - 1)
      i = i + 1
    }
    days + day + hr / 24.0 + minute / 1440.0 + sec / 86400.0
  }

  /**
   * Convert the day of the year, days, to the equivalent month day, hour, minute and second.
   *
   * @param year Year e.g. 1900 .. 2100
   * @param days Julian day of the year e.g. 0 .. 366.0
   *
   * @return Tuple containing:
   *         <ul>
   *         <li>Year   e.g. 1900 .. 2100</li>
   *         <li>Month  e.g. 1 .. 12</li>
   *         <li>Day    e.g. 1 .. 28,29,30,31</li>
   *         <li>Hour   e.g. 0 .. 23</li>
   *         <li>Minute e.g. 0 .. 59</li>
   *         <li>Second e.g. 0.0 .. 59.999</li>
   *         </ul>
   */
  def calenderDate(year: Int, days: Double) = {
    val lmonth = Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    val dayofyr = floor(days).toInt
    /* ----------------- find month and day of month ---------------- */
    if ((year % 4) == 0)
      lmonth(1) = 29

    var i = 0
    var inttemp = 0
    while ((dayofyr > inttemp + lmonth(i)) && (i < 12)) {
      inttemp = inttemp + lmonth(i)
      i = i + 1
    }
    val mon = i + 1
    val day = dayofyr - inttemp

    /* ----------------- find hours minutes and seconds ------------- */
    var temp = (days - dayofyr) * 24.0
    val hr = temp.toInt
    temp = (temp - hr) * 60.0
    val minute = temp.toInt
    val sec = (temp - minute) * 60.0
    (year, mon, day, hr, minute, sec)
  }

  /**
   * Convert degrees, minutes and seconds into radians.
   *
   * @param deg Degrees ; 0 .. 360
   * @param min Minutes ; 0 .. 59
   * @param sec Seconds ; 0.0 .. 59.99
   *
   * @return result ; rad
   */
  def dms_rad(deg: Int, min: Int, sec: Double) =
    Pi * (deg + min / 60.0 + sec / 3600.0) / 180.0

  /**
   * Converts hours, minutes and seconds into radians.
   *
   * @param hr  Hours ; 0 .. 24
   * @param min Minutes ; 0 .. 59
   * @param sec Seconds ; 0.0 .. 59.99
   *
   * @return Angle in radians [rad]
   */
  def hms_rad(hr: Int, min: Int, sec: Double): Double =
    (hr + min / 60.0 + sec / 3600.0) * (15.0 * Pi / 180.0)

  /**
   * Convert seconds from the beginning of the day to hours, minutes and seconds.
   *
   * @param hr  Hours ;0 .. 24
   * @param min Minutes ; 0 .. 59
   * @param sec Seconds ;0.0 .. 59.99
   *
   * @return Seconds ; 0.0 .. 86400.0 [sec]
   */
  def hms_sec(hr: Int, min: Int, sec: Double): Double =
    hr * 3600.0 + min * 60.0 + sec

  /**
   * Converts hours, minutes and seconds into universal time.
   *
   * @param hr  Hours ; 0 .. 24
   * @param min Minutes ; 0 .. 59
   * @param sec Seconds ; 0.0 .. 59.99
   *
   * @return Universal time
   */
  @deprecated
  def hms_ut(hr: Int, min: Int, sec: Double) =
    hr * 100.0 + min + sec * 0.01

  /**
   * Find the julian date given the year, month, day, and time. the julian date is defined by each
   * elapsed day since noon, jan 1, 4713 bc.
   *
   * <p>
   * This routine uses an accurate calculation for JD, when the year is outside the 1900-2100 interval.
   * When it is inside this interval, use [[be.angelcorp.celest.time.TimeUtils# j d a y ( I n t, I n t, I n t, I n t, I n t, D o u b l e )]].
   * </p>
   *
   * @param year Year e.g. all, 1900 .. 2100
   * @param mon  Month e.g. 1 .. 12
   * @param day  Day e.g. 1 .. 28,29,30,31
   * @param hr   Universal time hour e.g. 0 .. 23
   * @param minute Universal time min e.g. 0 .. 59
   * @param sec  Universal time sec e.g. 0.0 .. 59.999
   * @return Julian date
   */
  def gregorian_jd(year: Int, mon: Int, day: Int, hr: Int, minute: Int, sec: Double): Double = {
    val (yr, mn) = if (mon <= 2) {
      (year - 1, mon + 12)
    } else (year, mon)

    val b = 2 - floor(yr * 0.01) + floor(floor(yr * 0.01) * 0.25)
    val jd = floor(365.25 * (yr + 4716)) +
      floor(30.6001 * (mn + 1)) +
      day + b - 1524.5 +
      ((sec / 60.0 + minute) / 60.0 + hr) / 24.0; // ut in days
    jd
  }

  /**
   * Find the greenwich sidereal time (iau-82).
   *
   * @param jdut1 Julian date in ut1
   *
   * @return Greenwich sidereal time angle; 0 to 2pi [rad]
   */
  def gstime(jdut1: Double): Double = {
    val tut1 = (jdut1 - 2451545.0) / 36525.0
    val temp1 = -6.2e-6 * tut1 * tut1 * tut1 + 0.093104 * tut1 * tut1 +
      (876600.0 * 3600.0 + 8640184.812866) * tut1 + 67310.54841 // sec
    val temp2 = temp1 * (Pi / 180.0) / 240.0 // 360/86400 = 1/240, to deg, to rad
    mod(temp2, 2 * Pi)
  }

  /**
   * Find the year, month, day, hour, minute and second for a specified Julian date.
   *
   * @param jd Julian date.
   *
   * @return Tuple containing:
   *         <ul>
   *         <li>year e.g. 1900 .. 2100</li>
   *         <li>month e.g. 1 .. 12</li>
   *         <li>day e.g. 1 .. 28,29,30,31</li>
   *         <li>hour e.g. 0 .. 23</li>
   *         <li>minute e.g. 0 .. 59</li>
   *         <li>second e.g. 0.0 .. 59.999</li>
   *         </ul>
   */
  def invjday(jd: Double) = {
    /* --------------- find year and days of the year --------------- */
    val temp = jd - 2415019.5
    val tu = temp / 365.25
    var year = 1900 + floor(tu).toInt
    var leapyrs = floor((year - 1901) * 0.25).toInt

    // nudge by 8.64x10-7 sec to get even outputs
    var days = temp - ((year - 1900) * 365.0 + leapyrs) + 0.00000000001

    /* ------------ check for case of beginning of a year ----------- */
    if (days < 1.0) {
      year = year - 1
      leapyrs = floor((year - 1901) * 0.25).toInt
      days = temp - ((year - 1900) * 365.0 + leapyrs)
    }

    /* ----------------- find remaing data ------------------------- */
    calenderDate(year, days)
    // Do we need to denudge the seconds with - 0.00000086400 ?
  }

  /**
   * Find the seconds since epoch (1 Jan 2000) given the julian date
   *
   * @param jd Julian date.
   * @return Seconds since epoch 1 jan 2000
   */
  def jd_sse(jd: Double) = (jd - 2451544.5) * 86400.0

  /**
   * Find the Julian Date given the year, month, day, and time. the Julian Date is defined by each
   * elapsed day since noon, jan 1, 4713 bc.
   * <p>
   * This formulation only accurate over the interval year 1900-2100, otherwise use
   * [[be.angelcorp.celest.time.TimeUtils# g r e g o r i a n _ j d ( I n t, I n t, I n t, I n t, I n t, D o u b l e )]]
   * </p>
   *
   * @param year Year e.g. 1900 .. 2100
   * @param mon  Month e.g. 1 .. 12
   * @param day  Day e.g. 1 .. 28,29,30,31
   * @param hr   Universal time hour e.g. 0 .. 23
   * @param minute Universal time min e.g. 0 .. 59
   * @param sec  Universal time sec e.g. 0.0 .. 59.999
   * @return Julian date.
   */
  def jday(year: Int, mon: Int, day: Int, hr: Int, minute: Int, sec: Double): Double =
    367.0 * year -
      floor((7 * (year + floor((mon + 9) / 12.0))) * 0.25) +
      floor(275 * mon / 9.0) +
      day + 1721013.5 + ((sec / 60.0 + minute) / 60.0 + hr) / 24.0

  // ut in days
  // - 0.5*sgn(100.0*year + mon - 190002.5) + 0.5;

  /**
   * Find the local sidereal time at a given location.
   *
   * @param lon   Site longitude (west -) ; -2pi to 2pi [rad]
   * @param jdut1 Julian date in UT1
   *
   * @return Tuple containing;
   *         <ul>
   *         <li>local sidereal time ; 0.0 to 2pi rad</li>
   *         <li>greenwich sidereal time ; 0.0 to 2pi rad</li>
   *         </ul>
   */
  def lstime(lon: Double, jdut1: Double) = {
    val gst = gstime(jdut1)
    val lst = mod(lon + gst, 2.0 * Pi)
    (lst, gst)
  }

  /**
   * Convert an angle in radians  into degrees, minutes and seconds.
   *
   * @param rad Angle in radians [rad]
   *
   * @return Tuple containing;
   *         <ul>
   *         <li>degrees ; 0 .. 360</li>
   *         <li>minutes ; 0 .. 59</li>
   *         <li>seconds ; 0.0 .. 59.99</li>
   *         </ul>
   */
  def rad_dms(rad: Double) = {
    val temp = rad * 180 / Pi

    val deg = temp.toInt
    val min = ((temp - deg) * 60.0).toInt
    val sec = (temp - deg - min / 60.0) * 3600.0
    (deg, min, sec)
  }

  /**
   * Converts an angle in radians into hours, minutes and seconds.
   *
   * @param rad Angle in radians [rad].
   *
   * @return Tuple containing:
   *         <ul>
   *         <li>hours ; 0 .. 24</li>
   *         <li>minutes ; 0 .. 59</li>
   *         <li>seconds ; 0.0 .. 59.99</li>
   *         </ul>
   */
  def rad_hms(rad: Double) = {
    val temp = (rad / 15.0) * (180.0 / Pi)
    // [([rad]/[deg/hr]) * [deg/rad]] = [hr]
    val hr = temp.toInt
    val min = ((temp - hr) * 60.0).toInt
    val sec = (temp - hr - min / 60.0) * 3600.0
    (hr, min, sec)
  }

  /**
   * Convert seconds from the beginning of the day to hours, minutes and seconds.
   *
   * @param utsec Seconds in a day UT; 0.0 .. 86400.0 [sec]
   *
   * @return Tuple containing
   *         <ul>
   *         <li>hours ;0 .. 24</li>
   *         <li>minutes ; 0 .. 59</li>
   *         <li>seconds ;0.0 .. 59.99</li>
   *         </ul>
   *
   */
  def sec_hms(utsec: Double) = {
    val temp = utsec / 3600.0
    val hr = floor(temp).toInt
    val min = floor((temp - hr) * 60.0).toInt
    val sec = (temp - hr - min / 60.0) * 3600.0
    (hr, min, sec)
  }

  /**
   * Converts universal time into hours, minutes and seconds.
   *
   * @param ut universal time
   * @return Tuple containing:
   *         <ul>
   *         <li>hours ; 0 .. 24</li>
   *         <li>minutes ; 0 .. 59</li>
   *         <li>seconds ; 0.0 .. 59.99</li>
   *         </ul>
   */
  @deprecated
  def ut_hms(ut: Double) = {
    val hr = floor(ut * 0.01).toInt
    val min = floor(ut - hr * 100.0).toInt
    val sec = (ut - hr * 100.0 - min) * 100.0
    (hr, min, sec)
  }

  /**
   * Separates the day-fraction from a julian day
   * @param jd Julian date.
   * @return Fraction in the current day, starting from midnight.
   */
  def dayFraction(jd: Double) = (jd + 0.5 - floor(jd)) % 1.0

  // JD starts at midday so add 0.5 to align to midnight

  /**
   * Computes the number of seconds in the current day from a Julian date.
   * @param jd Julian date.
   * @return Seconds in the current day, starting from midnight.
   */
  def secondsInDay(jd: Double) = dayFraction(jd) * 86400.0

}
