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
package be.angelcorp.libs.celest.time

import be.angelcorp.libs.celest.time.timeStandard.ITimeStandard
import be.angelcorp.libs.celest.universe.Universe
import java.util._
import be.angelcorp.libs.celest.time.dateStandard.IDateStandard
import be.angelcorp.libs.util.physics.Time
import com.sun.org.apache.bcel.internal.generic.ClassObserver

/**
 * Basic Julian Date, a time/date of a specific epoch. Internaly the representation is handled as a
 * Julian Date number (see [[be.angelcorp.libs.celest.time.dateStandard.DateStandards]]),
 * in a specific time standated (UTC/TAI/TT/... see [[be.angelcorp.libs.celest.time.timeStandard.ITimeStandard]]).
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
 * @param date Set the internal epoch to the given JD.
 * @param timeStandard Time standard that the epoch is given in.
 *
 * @author Simon Billemont
 */
// TODO: remove (int) sec casts
class JulianDate(val date: Double, timeStandard: ITimeStandard)(implicit universe: Universe) extends IJulianDate {

	/**
	 * Create a Julian Date from a given [[java.util.Date]].
	 * 
	 * @param date Date to convert to a Julian Date (UTC time).
	 */
	//def this(date: Date)(implicit universe: Universe) = this(date, universe.UTC)

	/**
	 * Create a Julian Date from a given [[java.util.Date]].
	 * 
	 * @param date Date to convert to a Julian Date.
	 * @param timeStandard Time standard that the epoch is given in.
	 */
	def this(date: Date, timeStandard: ITimeStandard)(implicit universe: Universe) = this(
    {
      val cal = new GregorianCalendar()
      cal.clear()
      cal.setTime(date)

      TimeUtils.jday(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
          cal.get(Calendar.DATE), cal.get(Calendar.HOUR_OF_DAY),
          cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND))
    }, timeStandard
  )

	/**
	 * Create a Julian Date from the given Julian Date number.
	 * 
	 * @param date Set the internal date to the given JD (UTC time).
	 */
	def this(date: Double)(implicit universe: Universe) = this(date, universe.UTC)

	/**
	 * Create a Julian Date from a given date in the given form.
	 * 
	 * @param date Date to create the Julian Date (UTC time).
	 * @param dateStandard Date standard that the given epoch is in.
	 */
	def this(date: Double, dateStandard: IDateStandard)(implicit universe: Universe) = this(dateStandard.toJD(date), universe.UTC)

	/**
	 * Create a Julian Date from a given date in the given form.
	 * 
	 * @param date Date to create the Julian Date.
	 * @param dateStandard Date standard that the given epoch is in.
	 * @param timeStandard Time standard that the epoch is given in.
	 */
	def this(date: Double, dateStandard: IDateStandard, timeStandard: ITimeStandard)(implicit universe: Universe) =
    this(dateStandard.toJD(date), timeStandard)

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
    this(TimeUtils.jday(year, month, day, hour, minute, seconds), universe.UTC)

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
	def this(year: Int, month: Int, day: Int, hour: Int, minute: Int, seconds: Double, timeStandard: ITimeStandard)(implicit universe: Universe) =
		this(TimeUtils.jday(year, month, day, hour, minute, seconds), timeStandard)

	override def add(dt: Double, format: Time) = new JulianDate(getJD + Time.convert(dt, format, Time.day), timeStandard)

	override def compareTo(other: IJulianDate) = getJD.compareTo( other.getJD )

	override def equals(obj: Any) = {
		if (obj != null && classOf[IJulianDate].getClass.isAssignableFrom(obj.getClass)) {
			val other = obj.asInstanceOf[IJulianDate]
			compareTo(other) == 0 && timeStandard == other.getTimeStandard
		} else false
	}

	override def getDate = {
		val dateArr = TimeUtils.invjday(date)
		val calender = new GregorianCalendar(dateArr(0), dateArr(1), dateArr(2), dateArr(3), dateArr(4), dateArr(5))
		calender.getTime
	}

	override def getJD = date

  override def getJulianDate(form: IDateStandard) = form.fromJD(date)

	override def getJulianDate(timeStandard: ITimeStandard) =
    if (this.timeStandard.equals(timeStandard))
      this
    else {
     /* First convert this to TT form */
		  val offset = this.timeStandard.offsetToTT(this)
      val this_tt = new JulianDate(getJD(), universe.TT).add(offset, Time.second)

      /* Then convert the TT jd form to the requested type */
      val offset2 = timeStandard.offsetFromTT(this_tt)
      new JulianDate(this_tt.getJD(), timeStandard).add(offset2, Time.second)
    }

	override def getTimeStandard = timeStandard

	override def hashCode()= getJD.hashCode ^ timeStandard.hashCode

	override def relativeTo(epoch: IJulianDate) = getJulianDate(timeStandard).getJD - epoch.getJD

	override def relativeTo(epoch: IJulianDate, timeformat: Time) = {
		val delta_julian_days = getJD - epoch.getJulianDate(timeStandard).getJD
		Time.convert(delta_julian_days, Time.day, timeformat)
	}

	override def toString = "%fJD".format( getJD )

}
