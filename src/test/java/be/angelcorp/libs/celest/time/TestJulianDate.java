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
import java.util.GregorianCalendar;

import be.angelcorp.libs.celest.time.dateStandard.DateStandards;
import be.angelcorp.libs.celest.time.timeStandard.UTC;
import be.angelcorp.libs.celest.unit.CelestTest;
import be.angelcorp.libs.celest.universe.DefaultUniverse;
import be.angelcorp.libs.celest.universe.Universe;
import be.angelcorp.libs.util.physics.Time;

public class TestJulianDate extends CelestTest {

    public static Universe universe = new DefaultUniverse();

	public void testAdd() {
		// Check if the numerical value of JD gets correctly added
		JulianDate jd0 = new JulianDate(5, universe);
		double dt = 325;// [s]
		JulianDate jd = jd0.add(dt, Time.second);
		assertEquals(5 + Time.convert(dt, Time.second, Time.day), jd.getJD());

		// Check of the value of ITimeStandard is correctly kept after adding
		jd0 = new JulianDate(1., universe.TT(), universe);
		jd = jd0.add(1, Time.day_julian);
		assertEquals(2, jd.getJD(), 1E-16);
		assertEquals(universe.TT(), jd.getTimeStandard());
	}

	public void testITimeStandard() {
		// public abstract IJulianDate getJulianDate(ITimeStandard timeStandard);
		// public abstract ITimeStandard getTimeStandard();

		// J2000 epoch
		// Terrestrial Time: January 1 2000, 12:00:00 TT = 2451545.0 JD TT
		// International Atomic Time: January 1, 2000, 11:59:27.816 TAI = 2451544.99962750 JD TAI
		// Coordinated Universal Time: January 1, 2000, 11:58:55.816 UTC = 2451544.99925713 JD UTC
		JulianDate jd_tt_j2000 = new JulianDate(2451545.0, universe.TT(), universe);
		JulianDate jd_tai_j2000_true = new JulianDate(2451544.99962750, universe.TAI(), universe);
		JulianDate jd_utc_j2000_true = new JulianDate(2451544.99925713, universe.UTC(), universe);
		assertEquals(universe.TT(),  jd_tt_j2000.getTimeStandard());
		assertEquals(universe.TAI(), jd_tai_j2000_true.getTimeStandard());
		assertEquals(universe.UTC(), jd_utc_j2000_true.getTimeStandard());

		IJulianDate jd_tai_j2000_actual = jd_tt_j2000.getJulianDate(universe.TAI());
		IJulianDate jd_utc_j2000_actual = jd_tt_j2000.getJulianDate(universe.UTC());
		assertEquals(universe.TAI(), jd_tai_j2000_actual.getTimeStandard());
		assertEquals(universe.UTC(), jd_utc_j2000_actual.getTimeStandard());

		assertEquals(jd_tai_j2000_true.getJD(), jd_tai_j2000_actual.getJD(), 1E-8);
		assertEquals(jd_utc_j2000_true.getJD(), jd_utc_j2000_actual.getJD(), 1E-8);
	}

	public void testJulianDate() {
		// MATLAB:
		// % October 10, 2004, at 12:21:18 p.m.:
		// fprintf('%.10f\n', juliandate(2004,10,10,12,21,18))
		// 2453289.0147916665
		GregorianCalendar cal = new GregorianCalendar(2004, 10, 10, 12, 21, 18);
		JulianDate jd  = new JulianDate(2453289.0147916665, universe);
		JulianDate jd2 = new JulianDate(cal.getTime(), universe.UTC(), universe);
		JulianDate jd3 = new JulianDate(2453289.0147916665, DateStandards.JULIAN_DATE, universe);
		JulianDate jd4 = new JulianDate(53288.5147916665, DateStandards.MODIFIED_JULIAN_DAY, universe);

		/* Check jd get */
		/* Less then half a second off ? */
		assertEquals(2453289.0147916665, jd.getJD(),  1. / (24 * 3600 * 1000));
		assertEquals(2453289.0147916665, jd2.getJD(), 1. / (24 * 3600 * 1000));
		assertEquals(2453289.0147916665, jd3.getJD(), 1. / (24 * 3600 * 1000));
		assertEquals(2453289.0147916665, jd4.getJD(), 1. / (24 * 3600 * 1000));

		assertEquals(2453289.0147916665, jd.getJulianDate(DateStandards.JULIAN_DATE), 1. / (24 * 3600 * 1000));
		assertEquals(2453289, jd.getJulianDate(DateStandards.JULIAN_DAY_NUMBER), 1e-16);

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
	}

	public void testRelative() {
		JulianDate epoch1 = new JulianDate(0, universe);
		JulianDate epoch2 = new JulianDate(5.22, universe);

		assertEquals(5.22, epoch2.relativeTo(epoch1));
		assertEquals(Time.convert(5.22, Time.day), epoch2.relativeTo(epoch1, Time.second));
	}
}
