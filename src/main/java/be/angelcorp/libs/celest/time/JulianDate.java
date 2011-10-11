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
 * <p>
 * WARNING: Any arithmetic in this class is only accurate to the second ! (double => int for seconds)
 * </p>
 * 
 * @author Simon Billemont
 * 
 */
// TODO: remove (int) sec casts
public class JulianDate implements IJulianDate {

	double	date;

	public JulianDate(Date date) {
		setDate(date);
	}

	public JulianDate(double date) {
		setJD(date);
	}

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

}
