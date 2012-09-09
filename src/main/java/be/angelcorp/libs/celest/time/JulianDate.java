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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.annotation.concurrent.Immutable;

import be.angelcorp.libs.celest.time.dateStandard.DateStandards;
import be.angelcorp.libs.celest.time.dateStandard.IDateStandard;
import be.angelcorp.libs.celest.time.timeStandard.ITimeStandard;
import be.angelcorp.libs.celest.time.timeStandard.TimeStandards;
import be.angelcorp.libs.celest.time.timeStandard.UTC;
import be.angelcorp.libs.util.physics.Time;

/**
 * Basic Julian Date, a time/date of a specific epoch. Internaly the representation is handled as a
 * Julian Date number (see {@link DateStandards}), in a specific time standated (UTC/TAI/TT/... see
 * {@link ITimeStandard}).
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
 * @author Simon Billemont
 * 
 */
// TODO: remove (int) sec casts
@Immutable
public class JulianDate implements IJulianDate {

	/** The J2000 epoch in JulianDate form. */
	public static final JulianDate	J2000_EPOCH	= new JulianDate(2451545.0, TimeStandards.TT());

	/** The starting epoch of the TAI timeline (same as TAI/TT/TCG/TCB). */
	public static final JulianDate	TAI_EPOCH	= new JulianDate(2443144.5, TimeStandards.TAI());
	/** The starting epoch of the TT timeline (same as TAI/TT/TCG/TCB). */
	public static final JulianDate	TT_EPOCH	= TAI_EPOCH;
	/** The starting epoch of the TCG timeline (same as TAI/TT/TCG/TCB). */
	public static final JulianDate	TCG_EPOCH	= TAI_EPOCH;
	/** The starting epoch of the TCB timeline (same as TAI/TT/TCG/TCB). */
	public static final JulianDate	TCB_EPOCH	= TAI_EPOCH;
	/** The starting epoch of the TDB timeline. */
	public static final JulianDate	TDB_EPOCH	= TAI_EPOCH.add(-65.5, Time.microsecond);

	/**
	 * The J2000 epoch in JulianDate form.
	 * 
	 * @deprecated Use {@link JulianDate#J2000_EPOCH}
	 */
	@Deprecated
	public static JulianDate getJ2000() {
		return J2000_EPOCH;
	}

	/** Julian Date represented by this class */
	private final double		date;
	/** Time standard of the embedded Julian date */
	private final ITimeStandard	timeStandard;

	/**
	 * Create a Julian Date from a given {@link Date}.
	 * 
	 * @param date
	 *            Date to convert to a Julian Date (UTC time).
	 */
	public JulianDate(Date date) {
		this(date, UTC.get());
	}

	/**
	 * Create a Julian Date from a given {@link Date}.
	 * 
	 * @param date
	 *            Date to convert to a Julian Date.
	 * @param timeStandard
	 *            Time standard that the epoch is given in.
	 */
	public JulianDate(Date date, ITimeStandard timeStandard) {
		Calendar cal = new GregorianCalendar();
		cal.clear();
		cal.setTime(date);

		this.date = TimeUtils.jday(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DATE), cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
		this.timeStandard = timeStandard;
	}

	/**
	 * Create a Julian Date from the given Julian Date number.
	 * 
	 * @param date
	 *            Set the internal date to the given JD (UTC time).
	 */
	public JulianDate(double date) {
		this.date = date;
		this.timeStandard = UTC.get();
	}

	/**
	 * Create a Julian Date from a given date in the given form.
	 * 
	 * @param date
	 *            Date to create the Julian Date (UTC time).
	 * @param dateStandard
	 *            Date standard that the given epoch is in.
	 */
	public JulianDate(double date, IDateStandard dateStandard) {
		this.date = dateStandard.toJD(date);
		this.timeStandard = UTC.get();
	}

	/**
	 * Create a Julian Date from a given date in the given form.
	 * 
	 * @param date
	 *            Date to create the Julian Date.
	 * @param dateStandard
	 *            Date standard that the given epoch is in.
	 * @param timeStandard
	 *            Time standard that the epoch is given in.
	 */
	public JulianDate(double date, IDateStandard dateStandard, ITimeStandard timeStandard) {
		this.date = dateStandard.toJD(date);
		this.timeStandard = timeStandard;
	}

	/**
	 * Create a Julian Date from the given Julian Date number.
	 * 
	 * @param date
	 *            Set the internal epoch to the given JD.
	 * @param timeStandard
	 *            Time standard that the epoch is given in.
	 */
	public JulianDate(double date, ITimeStandard timeStandard) {
		this.date = date;
		this.timeStandard = timeStandard;
	}

	/**
	 * Create a Julian date from a specified calendar date.
	 * 
	 * @param year
	 *            Year of the epoch.
	 * @param month
	 *            Month of the epoch.
	 * @param day
	 *            Day of the epoch.
	 * @param hour
	 *            Hour of the epoch.
	 * @param minute
	 *            Minutes of the epoch.
	 * @param seconds
	 *            Seconds of the epoch.
	 */
	public JulianDate(int year, int month, int day, int hour, int minute, double seconds) {
		this(year, month, day, hour, minute, seconds, UTC.get());
	}

	/**
	 * Create a Julian date from a specified calendar date.
	 * 
	 * @param year
	 *            Year of the epoch.
	 * @param month
	 *            Month of the epoch.
	 * @param day
	 *            Day of the epoch.
	 * @param hour
	 *            Hour of the epoch.
	 * @param minute
	 *            Minutes of the epoch.
	 * @param seconds
	 *            Seconds of the epoch.
	 * @param timeStandard
	 *            Time standard that the epoch is given in.
	 */
	public JulianDate(int year, int month, int day, int hour, int minute, double seconds, ITimeStandard timeStandard) {
		this.date = TimeUtils.jday(year, month, day, hour, minute, seconds);
		this.timeStandard = timeStandard;
	}

	/** {@inheritDoc} */
	@Override
	public JulianDate add(double dt, Time format) {
		JulianDate jd2 = new JulianDate(getJD() + Time.convert(dt, format, Time.day), timeStandard);
		return jd2;
	}

	/** {@inheritDoc} */
	@Override
	public int compareTo(IJulianDate o) {
		return Double.compare(getJD(), o.getJD());
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && IJulianDate.class.isAssignableFrom(obj.getClass())) {
			IJulianDate other = (IJulianDate) obj;
			return compareTo(other) == 0 && timeStandard == other.getTimeStandard();
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public Date getDate() {
		int[] dateArr = TimeUtils.invjday(date);
		GregorianCalendar calender = new GregorianCalendar(
				dateArr[0], dateArr[1], dateArr[2], dateArr[3], dateArr[4], dateArr[5]);
		return calender.getTime();
	}

	/** {@inheritDoc} */
	@Override
	public double getJD() {
		return date;
	}

	/** {@inheritDoc} */
	@Override
	public double getJulianDate(IDateStandard form) {
		return form.fromJD(date);
	}

	/** {@inheritDoc} */
	@Override
	public JulianDate getJulianDate(ITimeStandard timeStandard) {
		if (this.timeStandard.equals(timeStandard))
			return this;

		/* First convert this to TT form */
		double offset = this.timeStandard.offsetToTT(this);
		JulianDate this_tt = new JulianDate(getJD(), TimeStandards.TT()).add(offset, Time.second);

		/* Then convert the TT jd form to the requested type */
		offset = timeStandard.offsetFromTT(this_tt);
		JulianDate jd_new = new JulianDate(this_tt.getJD(), timeStandard).add(offset, Time.second);

		return jd_new;
	}

	/** {@inheritDoc} */
	@Override
	public ITimeStandard getTimeStandard() {
		return timeStandard;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return new Double(getJD()).hashCode() ^ timeStandard.hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public double relativeTo(IJulianDate epoch) {
		return getJulianDate(timeStandard).getJD() - epoch.getJD();
	}

	/** {@inheritDoc} */
	@Override
	public double relativeTo(IJulianDate epoch, Time timeformat) {
		double delta_julian_days = getJD() - epoch.getJulianDate(timeStandard).getJD();
		return Time.convert(delta_julian_days, Time.day, timeformat);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return String.format("%fJD", getJD());
	}
}
