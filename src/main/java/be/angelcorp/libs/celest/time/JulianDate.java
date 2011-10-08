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

/**
 * Basic julian date stuff
 * 
 * @author Simon Billemont
 * 
 */
public class JulianDate implements IJulianDate {

	public static double decimal_day(long day, long hour, long minute, long second) {
		return day + decimal_hour(hour, minute, second) / 24;
	}

	public static double decimal_hour(long hour, long minute, long second) {
		return hour + (double) minute / 60 + (double) second / 3600;
	}

	public static double toJuliandate(long year, long month, long day, long hour, long minute,
			long second) {
		/* after Oct 15th, 1582 */
		long j_year = year;
		long j_month = month;
		long A, B, C, D;

		if (month == 1 || month == 2) {
			j_month = month + 12;
			j_year = year - 1;
		}
		A = (j_year / 100);
		B = 2 - A + (A / 4);
		C = (long) (365.25 * j_year);
		D = (long) (30.6001 * (j_month + 1));
		return B + C + D + decimal_day(day, hour, minute, second) + 1720994.5;
	}

	double	date;

	public JulianDate(Date date) {
		setDate(date);
	}

	public JulianDate(double date) {
		setJD(date);
	}

	@Override
	public Date getDate() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getJD() {
		return date;
	}

	@Override
	public void setDate(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.clear();
		cal.setTime(date);

		this.date = toJuliandate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_WEEK), cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJD(double date) {
		this.date = date;
	}

}
