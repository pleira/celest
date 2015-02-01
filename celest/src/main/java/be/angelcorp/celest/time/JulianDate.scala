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

import java.util._
import be.angelcorp.celest.time.timeStandard.TimeStandard
import be.angelcorp.celest.time.dateStandard.DateStandard
import be.angelcorp.celest.time.TimeUtils._
import be.angelcorp.celest.time.timeStandard.TimeStandards._
import be.angelcorp.celest.universe.Universe

/**
 * Basic Julian Date, a time/date of a specific epoch. Internally the representation is handled as a
 * Julian Date number (see [[be.angelcorp.celest.time.dateStandard.DateStandards]]),
 * in a specific time standard (UTC/TAI/TT/... see [[be.angelcorp.celest.time.timeStandard.TimeStandard]]).
 *
 * <p>
 * The general accuracy of this class is generally lower a millisecond.
 * </p>
 *
 * <p>
 * WARNING: Any date (yr/mo/day/hr/min/sec) arithmetic in this class is only accurate to the second !
 * (double => int for seconds).
 * </p>
 *
 * @param jd            Set the internal epoch to the given JD.
 * @param timeStandard  Time standard that the epoch is given in.
 *
 * @author Simon Billemont
 */
case class JulianDate(jd: Double, timeStandard: TimeStandard)(implicit universe: Universe) extends Epoch {

  /**
   * Create a Julian Date from a given [[java.util.Date]].
   *
   * @param date Date to convert to a Julian Date.
   * @param timeStandard Time standard that the epoch is given in.
   */
  def this(date: Date, timeStandard: TimeStandard)(implicit universe: Universe) = this(
  {
    val cal = new GregorianCalendar()
    cal.clear()
    cal.setTime(date)

    jday(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
      cal.get(Calendar.DATE), cal.get(Calendar.HOUR_OF_DAY),
      cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND))
  }, timeStandard
  )

  /**
   * Create a Julian Date from the given Julian Date number.
   *
   * @param date Set the internal date to the given JD (UTC time).
   */
  def this(date: Double)(implicit universe: Universe) = this(date, UTC)

  /**
   * Create a Julian Date from a given date in the given form.
   *
   * @param date Date to create the Julian Date (UTC time).
   * @param dateStandard Date standard that the given epoch is in.
   */
  def this(date: Double, dateStandard: DateStandard)(implicit universe: Universe) = this(dateStandard.toJD(date), UTC)

  /**
   * Create a Julian Date from a given date in the given form.
   *
   * @param date Date to create the Julian Date.
   * @param dateStandard Date standard that the given epoch is in.
   * @param timeStandard Time standard that the epoch is given in.
   */
  def this(date: Double, dateStandard: DateStandard, timeStandard: TimeStandard)(implicit universe: Universe) =
    this(dateStandard.toJD(date), timeStandard)

  /**
   * Create a Julian date from a specified day in the year (UTC).
   *
   * @param year Year of the epoch.
   * @param days Fractional days in the year of the epoch.
   */
  def this(year: Int, days: Double)(implicit universe: Universe) =
    this(TimeUtils.jday(year, 1, 1, 0, 0, 0) - 1.0 + days, UTC)

  /**
   * Create a Julian date from a specified day in the year.
   *
   * @param year Year of the epoch.
   * @param days Fractional days in the year of the epoch.
   * @param timeStandard Time standard that the epoch is given in.
   */
  def this(year: Int, days: Double, timeStandard: TimeStandard)(implicit universe: Universe) =
    this(TimeUtils.jday(year, 0, 0, 0, 0, 0) + days, timeStandard)

  /**
   * Create a Julian date from a specified calendar date.
   *
   * @param year    Year of the epoch.
   * @param month   Month of the epoch.
   * @param day     Day of the epoch.
   * @param hour    Hour of the epoch.
   * @param minute  Minutes of the epoch.
   * @param seconds Seconds of the epoch.
   */
  def this(year: Int, month: Int, day: Int, hour: Int, minute: Int, seconds: Double)(implicit universe: Universe) =
    this(TimeUtils.jday(year, month, day, hour, minute, seconds), UTC)

  /**
   * Create a Julian date from a specified calendar date.
   *
   * @param year    Year of the epoch.
   * @param month   Month of the epoch.
   * @param day     Day of the epoch.
   * @param hour    Hour of the epoch.
   * @param minute  Minutes of the epoch.
   * @param seconds Seconds of the epoch.
   * @param timeStandard Time standard that the epoch is given in.
   */
  def this(year: Int, month: Int, day: Int, hour: Int, minute: Int, seconds: Double, timeStandard: TimeStandard)(implicit universe: Universe) =
    this(TimeUtils.jday(year, month, day, hour, minute, seconds), timeStandard)

  def add(dt: Double) = new JulianDate(jd + dt, timeStandard)

  override def compareTo(other: Epoch) = jd.compareTo(other.jd)

  def date = {
    val dateArr = TimeUtils.invjday(jd)
    val calender = new GregorianCalendar(dateArr._1, dateArr._2, dateArr._3, dateArr._4, dateArr._5, math.round(dateArr._6).toInt)
    calender.getTime
  }

  def inTimeStandard(timeStandard: TimeStandard) =
    if (this.timeStandard.equals(timeStandard))
      this
    else {
      /* First convert this to TT form */
      val offset = this.timeStandard.offsetToTT(this)
      val this_tt = new JulianDate(jd, TT).addS(offset)

      /* Then convert the TT jd form to the requested type */
      val offset2 = timeStandard.offsetFromTT(this_tt)
      new JulianDate(this_tt.jd, timeStandard).addS(offset2)
    }

  override def hashCode() = jd.hashCode ^ timeStandard.hashCode

  def relativeTo(epoch: Epoch) = inTimeStandard(timeStandard).jd - epoch.jd

  override def toString = "%fJD %s".format(jd, timeStandard.getClass.getSimpleName)

}
