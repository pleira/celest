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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import be.angelcorp.libs.util.physics.Time;

/**
 * Basic Julian Date container
 * 
 * <p>
 * WARNING: Any arithmetic in this class is only accurate to the second ! (double => int for seconds)
 * </p>
 * 
 * @author Simon Billemont
 * 
 */
// TODO: remove (int) sec casts
public class JulianDate implements IJulianDate {

	/**
	 * Julian Date represented by this class
	 */
	double	date;

	/**
	 * Create a Julian Date from a given {@link Date}
	 * 
	 * @param date
	 *            Date to convert to a Julian Date
	 */
	public JulianDate(Date date) {
		setDate(date);
	}

	/**
	 * Create a Julian Date from the given Julian Date number
	 * 
	 * @param date
	 *            Set the internal date to the given JD
	 */
	public JulianDate(double date) {
		setJD(date);
	}

	/**
	 * Create a Julian Date from a given date in the given form
	 * 
	 * @param date
	 *            Date to create the Julian Date
	 * @param form
	 *            Form that the given date is in
	 */
	public JulianDate(double date, JulianDateForm form) {
		setJD(date, form);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(IJulianDate o) {
		return Double.compare(getJD(), o.getJD());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getDate() {
		int[] dateArr = TimeUtils.invjday(date);
		GregorianCalendar calender = new GregorianCalendar(
				dateArr[0], dateArr[1], dateArr[2], dateArr[3], dateArr[4], dateArr[5]);
		return calender.getTime();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getJD() {
		return date;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getJD(JulianDateForm form) {
		return form.fromJD(date);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double relativeTo(IJulianDate epoch, Time timeformat) {
		return Time.convert(getJD() - epoch.getJD(), Time.day, timeformat);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double relativeTo(JulianDate epoch) {
		return getJD() - epoch.getJD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDate(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.clear();
		cal.setTime(date);

		this.date = TimeUtils.jday(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DATE), cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJD(double date) {
		this.date = date;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJD(double date2, JulianDateForm form) {
		this.date = form.toJD(date2);
	}

}
