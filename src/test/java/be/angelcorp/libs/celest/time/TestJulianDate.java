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
import java.util.GregorianCalendar;

import junit.framework.TestCase;

public class TestJulianDate extends TestCase {

	public void testJulianDate() {
		// MATLAB:
		// % October 10, 2004, at 12:21:18 p.m.:
		// fprintf('%.10f\n', juliandate(2004,10,10,12,21,18))
		// 2453289.0147916665
		GregorianCalendar cal = new GregorianCalendar(2004, 10, 10, 12, 21, 18);
		JulianDate jd = new JulianDate(2453289.0147916665);
		JulianDate jd2 = new JulianDate(cal.getTime());

		/* Check jd get */
		/* Less then half a second off ? */
		assertEquals(2453289.0147916665, jd.getJD(), 1. / (24 * 3600 * 1000));
		assertEquals(2453289.0147916665, jd2.getJD(), 1. / (24 * 3600 * 1000));

		/* Check date get */
		/* Check conversion to date */
		cal.clear();// Reset the calendar
		cal.setTime(jd.getDate());
		assertEquals(2004, cal.get(Calendar.YEAR));
		assertEquals(10, cal.get(Calendar.MONTH));
		assertEquals(10, cal.get(Calendar.DATE));
		assertEquals(12, cal.get(Calendar.HOUR_OF_DAY));
		assertEquals(21, cal.get(Calendar.MINUTE));
		assertEquals(18, cal.get(Calendar.SECOND));
		cal.clear();// Reset the calendar
		cal.setTime(jd2.getDate());
		assertEquals(2004, cal.get(Calendar.YEAR));
		assertEquals(10, cal.get(Calendar.MONTH));
		assertEquals(10, cal.get(Calendar.DATE));
		assertEquals(12, cal.get(Calendar.HOUR_OF_DAY));
		assertEquals(21, cal.get(Calendar.MINUTE));
		assertEquals(18, cal.get(Calendar.SECOND));

		/* Check the setting */
		// MATLAB:
		// % November 2, 1975, at 18:49:49:
		// fprintf('%.10f\n', juliandate(1975,11,2,18,59,49))
		// 2442719.2915393519
		jd.setJD(812);
		assertEquals(812., jd.getJD(), 1E-15);
		cal.clear();
		cal.set(1975, 11, 02, 18, 59, 49);
		jd.setDate(cal.getTime());
		assertEquals(2442719.2915393519, jd.getJD(), 1. / (24 * 3600 * 1000));
	}
}
