package be.angelcorp.libs.celest.time.timeStandard;

import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.celest.time.JulianDate;
import be.angelcorp.libs.celest.time.dateStandard.DateStandards;
import be.angelcorp.libs.celest.unit.CelestTest;
import be.angelcorp.libs.util.physics.Time;

public class TestTimeStandards extends CelestTest {

	private double getSecondsInDay(IJulianDate date) {
		return Time.convert(
				(date.getJD() - 0.5) - date.getJulianDate(DateStandards.JULIAN_DAY_NUMBER), Time.day_julian);
	}

	public void testDate1() throws Exception {
		// April 6, 2004, 7:51:28.386009 UTC = JD 2453101.827411875
		// dut1 -0.439962 s dat 32 s

		JulianDate date = new JulianDate(2453101.827411875104167, UTC.get());

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
}
