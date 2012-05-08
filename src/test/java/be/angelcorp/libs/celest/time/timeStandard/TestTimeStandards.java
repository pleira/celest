package be.angelcorp.libs.celest.time.timeStandard;

import be.angelcorp.libs.celest.time.HourMinSec;
import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.celest.time.JulianDate;
import be.angelcorp.libs.celest.time.TimeUtils;
import be.angelcorp.libs.celest.time.dateStandard.DateStandards;
import be.angelcorp.libs.celest.unit.CelestTest;
import be.angelcorp.libs.util.physics.Time;

public class TestTimeStandards extends CelestTest {

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

		JulianDate date = new JulianDate(2453101.827411875104167, new UTC());

		// Seconds in the current day for:
		double ut1 = 28287.9460470000;
		double utc = 28288.3860090000;
		double tai = 28320.3860090000;
		double tt = 28352.5700090000;
		double tdb = 28352.5716651154;
		double tcg = 28353.1695861742;
		double tcb = 28365.9109901113;

		assertEquals(utc, getSecondsInDay(date), 1E-5);
		assertEquals(tai, getSecondsInDay(date.getJulianDate(TimeStandards.TAI)), 2E-5);
		assertEquals(tt, getSecondsInDay(date.getJulianDate(TimeStandards.TT)), 2E-5);
		assertEquals(tcg, getSecondsInDay(date.getJulianDate(TCG.get())), 2E-5);
		// TODO: not implemented yet
		// assertEquals(ut1, getSecondsInDay(date.getJulianDate(TimeStandards.UT1)), 1E-5);
		// assertEquals(tdb, getSecondsInDay(date.getJulianDate(TimeStandards.TDB)), 1E-5);
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

		JulianDate jd_tai = new JulianDate(jd_base + tai, TimeStandards.TAI);
		// JulianDate jd_ut1 = jd_tai.getJulianDate(TimeStandards.UT1); //TODO: implement UT1
		JulianDate jd_utc = jd_tai.getJulianDate(new UTC());
		JulianDate jd_tt = jd_tai.getJulianDate(TimeStandards.TT);
		// JulianDate jd_tdb = jd_tai.getJulianDate(TimeStandards.TDB);//TODO: implement TDB
		// JulianDate jd_tcb = jd_tai.getJulianDate(TimeStandards.TCB);//TODO: implement TCB
		JulianDate jd_tcg = jd_tai.getJulianDate(new TCG());

		double accuracy = Time.convert(0.0001, Time.second, Time.day_julian);
		// assertEquals(jd_base + ut1, jd_ut1.getJD(), accuracy);//TODO: implement UT1
		assertEquals(jd_base + utc, jd_utc.getJD(), accuracy);
		assertEquals(jd_base + tai, jd_tai.getJD(), accuracy);
		assertEquals(jd_base + tt, jd_tt.getJD(), accuracy);
		// assertEquals(jd_base + tdb, jd_tdb.getJD(), accuracy);//TODO: implement TDB
		// assertEquals(jd_base + tcb, jd_tcb.getJD(), accuracy);//TODO: implement TCB
		assertEquals(jd_base + tcg, jd_tcg.getJD(), accuracy);
	}

	public void testFromToTAI() throws Exception {
		// Some random date: 2012 May 5 15:56:12.1
		JulianDate date = new JulianDate(2456053.164029);

		double TAItoTAI = TimeStandards.TAI.offsetToTAI(date);
		double TAIfromTAI = TimeStandards.TAI.offsetFromTAI(date);
		assertEquals(0, TAItoTAI + TAIfromTAI, 1E-16);

		double TTtoTAI = TimeStandards.TT.offsetToTAI(date);
		double TTfromTAI = TimeStandards.TT.offsetFromTAI(date);
		assertEquals(0, TTtoTAI + TTfromTAI, 1E-16);

		double UTCtoTAI = UTC.get().offsetToTAI(date);
		double UTCfromTAI = UTC.get().offsetFromTAI(date);
		assertEquals(0, UTCtoTAI + UTCfromTAI, 1E-16);

		double TCGtoTAI = TCG.get().offsetToTAI(date);
		double TCGfromTAI = TCG.get().offsetFromTAI(date);
		assertEquals(0, TCGtoTAI + TCGfromTAI, 1E-16);
	}

	public void testUTCLeap() {
		// See IERSm Bulletin C 43
		// from 2009 January 1, 0h UTC, to 2012 July 1 0h UTC : UTC-TAI = - 34s
		// from 2012 July 1, 0h UTC, until further notice : UTC-TAI = - 35s

		// Exact switch epoch in UTC-TAI=-35
		JulianDate jd_tai_1 = new JulianDate(TimeUtils.jday(2012, 7, 1, 0, 0, 0)).add(35, Time.second);
		// Just before switch epoch in UTC-TAI=-34
		JulianDate jd_tai_2 = new JulianDate(TimeUtils.jday(2012, 7, 1, 0, 0, 0)).add(34, Time.second);

		UTC utc = new UTC();
		assertEquals(-35, utc.offsetFromTAI(jd_tai_1), 1E-16);
		assertEquals(-34, utc.offsetFromTAI(jd_tai_2), 1E-16);
	}
}
