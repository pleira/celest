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
package be.angelcorp.libs.celest.time.timeStandard;

import be.angelcorp.libs.celest.time.HourMinSec;
import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.celest.time.JulianDate;
import be.angelcorp.libs.celest.time.TimeUtils;
import be.angelcorp.libs.celest.time.dateStandard.DateStandards;
import be.angelcorp.libs.celest.unit.CelestTest;
import be.angelcorp.libs.celest.universe.DefaultUniverse;
import be.angelcorp.libs.celest.universe.Universe;
import be.angelcorp.libs.util.physics.Time;

public class TestTimeStandards extends CelestTest {

    public static Universe universe = new DefaultUniverse();

	private double getSecondsInDay(IJulianDate date) {
		return Time.convert(
				(date.getJD() - 0.5) - date.getJulianDate(DateStandards.JULIAN_DAY_NUMBER), Time.day_julian);
	}

	/**
	 * Conversion between various time standards, test date and results from:
	 * <p>
	 * D. Vallado et al. ,
	 * <b>"Implementation Issues Surrounding the New IAU Reference Systems for Astrodynamics"</b>, 16th
	 * AAS/AIAA Space Flight Mechanics Conference, Florida, January 2006
	 * </p>
	 */
	public void testDate1() throws Exception {
		// April 6, 2004, 7:51:28.386009 UTC = JD 2453101.827411875
		// dut1 -0.439962 s dat 32 s

		JulianDate date = new JulianDate(2453101.827411875104167, universe.UTC(), universe);

		// Seconds in the current day for:
		double ut1 = 28287.9460470000;
		double utc = 28288.3860090000;
		double tai = 28320.3860090000;
		double tt = 28352.5700090000;
		double tdb = 28352.5716651154;
		double tcg = 28353.1695861742;
		double tcb = 28365.9109901113;

		assertEquals(utc, getSecondsInDay(date), 1E-5);
		assertEquals(tai, getSecondsInDay(date.getJulianDate(universe.TAI())), 2E-5);
		assertEquals(tt,  getSecondsInDay(date.getJulianDate(universe.TT())),  2E-5);
		assertEquals(tcg, getSecondsInDay(date.getJulianDate(universe.TCG())), 3E-5);
		assertEquals(tdb, getSecondsInDay(date.getJulianDate(universe.TDB())), 1E-5);
		// TODO: not implemented yet
		// assertEquals(ut1, getSecondsInDay(date.getJulianDate(TimeStandards.UT1)), 1E-5);
		// assertEquals(tcb, getSecondsInDay(date.getJulianDate(TimeStandards.TCB)), 1E-5);
	}

	/**
	 * Conversion between various time standards, test date and results from:
	 * <p>
	 * D.A. Vallado, <b>"Fundamentals of Astrodynamics and Applications"</b>, 2007, ISBN:
	 * 978-0-387-71831-6, p201 example 3-7
	 * </p>
	 */
	public void testDate2() throws Exception {
		double jd_base = 2453102;
		double ut1 = new HourMinSec(16, 42, 59.5367).getDayFraction();
		double utc = new HourMinSec(16, 43, 00.0000).getDayFraction();
		double tai = new HourMinSec(16, 43, 32.0000).getDayFraction();
		double tt = new HourMinSec(16, 44, 04.1840).getDayFraction();
		double tdb = new HourMinSec(16, 44, 04.1856).getDayFraction();
		double tcb = new HourMinSec(16, 44, 17.5255).getDayFraction();
		double tcg = new HourMinSec(16, 44, 04.7836).getDayFraction();

        IJulianDate jd_tai = new JulianDate(jd_base + tai, universe.TAI(), universe);
		// JulianDate jd_ut1 = jd_tai.getJulianDate(TimeStandards.UT1); //TODO: implement UT1
		IJulianDate jd_utc = jd_tai.getJulianDate(universe.UTC());
        IJulianDate jd_tt = jd_tai.getJulianDate(universe.TT());
        IJulianDate jd_tdb = jd_tai.getJulianDate(universe.TDB());
		// JulianDate jd_tcb = jd_tai.getJulianDate(TimeStandards.TCB);//TODO: implement TCB
        IJulianDate jd_tcg = jd_tai.getJulianDate(universe.TCG());

		double accuracy = Time.convert(0.0001, Time.second, Time.day_julian);
		// assertEquals(jd_base + ut1, jd_ut1.getJD(), accuracy);//TODO: implement UT1
		assertEquals(jd_base + utc, jd_utc.getJD(), accuracy);
		assertEquals(jd_base + tai, jd_tai.getJD(), accuracy);
		assertEquals(jd_base + tt, jd_tt.getJD(), accuracy);
		assertEquals(jd_base + tdb, jd_tdb.getJD(), accuracy);
		// assertEquals(jd_base + tcb, jd_tcb.getJD(), accuracy);//TODO: implement TCB
		assertEquals(jd_base + tcg, jd_tcg.getJD(), accuracy);
	}

	public void testFromToTAI() throws Exception {
		// Some random date: 2012 May 5 15:56:12.1
		JulianDate date = new JulianDate(2456053.164029, universe);

		double TAItoTT = universe.TAI().offsetToTT(date);
		double TAIfromTT = universe.TAI().offsetFromTT(date);
		assertEquals(0, TAItoTT + TAIfromTT, 1E-16);

		double TTtoTT = universe.TT().offsetToTT(date);
		double TTfromTT = universe.TT().offsetFromTT(date);
		assertEquals(0, TTtoTT + TTfromTT, 1E-16);

		double UTCtoTT = universe.UTC().offsetToTT(date);
		double UTCfromTT = universe.UTC().offsetFromTT(date);
		assertEquals(0, UTCtoTT + UTCfromTT, 1E-16);

		double TCGtoTT = universe.TCG().offsetToTT(date);
		double TCGfromTT = universe.TCG().offsetFromTT(date);
		assertEquals(0, TCGtoTT + TCGfromTT, 1E-16);

		double TDBtoTT = universe.TDB().offsetToTT(date);
		double TDBfromTT = universe.TDB().offsetFromTT(date);
		assertEquals(0, TDBtoTT + TDBfromTT, 1E-16);
	}

	public void testUTCLeap() {
		// See IERSm Bulletin C 43
		// from 2009 January 1, 0h UTC, to 2012 July 1 0h UTC : UTC-TAI = - 34s
		// from 2012 July 1, 0h UTC, until further notice : UTC-TAI = - 35s

		// Just after (0.5s later) switch epoch in UTC-TAI=-35
		JulianDate jd_tt_1 = new JulianDate(TimeUtils.jday(2012, 7, 1, 0, 0, 0), universe.TT(), universe).add(35.5+32.184, Time.second);
		// Just before (-0.5s) switch epoch in UTC-TAI=-34
		JulianDate jd_tt_2 = new JulianDate(TimeUtils.jday(2012, 7, 1, 0, 0, 0), universe.TT(), universe).add(34.5+32.184, Time.second);

		assertEquals(-35-32.184, universe.UTC().offsetFromTT(jd_tt_1), 1E-16);
		assertEquals(-34-32.184, universe.UTC().offsetFromTT(jd_tt_2), 1E-16);
	}
}
