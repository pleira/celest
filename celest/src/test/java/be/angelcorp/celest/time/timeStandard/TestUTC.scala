/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package be.angelcorp.celest.time.timeStandard

import be.angelcorp.celest.time.TimeUtils._
import be.angelcorp.celest.time._
import be.angelcorp.celest.time.timeStandard.TimeStandards.TT
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class TestUTC extends FlatSpec with ShouldMatchers {

  "UTC" should "select transform symmetrically" in {
    implicit val universe = new MockTimeUniverse()
    val utc = new UTCTime(new MockTime(-32.184))
    val base_date = new JulianDate(2451545.0, utc)

    expect(base_date.jd) {
      base_date.inTimeStandard(TT).inTimeStandard(utc).jd
    }
  }

  /**
   * Conversion between various time standards, test date and results from:
   * <p>
   * D. Vallado et al. ,
   * <b>"Implementation Issues Surrounding the New IAU Reference Systems for Astrodynamics"</b>, 16th
   * AAS/AIAA Space Flight Mechanics Conference, Florida, January 2006
   * </p>
   */
  it should "conform to the 'Implementation Issues Surrounding the New IAU Reference Systems for Astrodynamics' validation data" in {
    // April 6, 2004, 7:51:28.386009 UTC = JD 2453101.827411875
    // dut1 -0.439962 s dat 32 s
    implicit val universe = new MockTimeUniverse()
    val date = new JulianDate(2004,  4,  6,  0,  0, 28352.5700090000, TT) // Test epoch in TT, see paper.

    // Reference seconds in the current day TCG
    val utc_seconds = 28288.3860090000

    // 1E-4 due to the precision restriction of JulianDate
    val jd_utc = date.inTimeStandard(new UTCTime(new MockTime(-32.184)))
    secondsInDay(jd_utc.jd) should be(utc_seconds plusOrMinus 1E-4)
  }

  /**
   * Conversion between various time standards, test date and results from:
   * <p>
   * D.A. Vallado, <b>"Fundamentals of Astrodynamics and Applications"</b>, 2007, ISBN:
   * 978-0-387-71831-6, p201 example 3-7. Important: The book contains errata for this example!
   * </p>
   */
  it should "conform to the 'Fundamentals of Astrodynamics and Applications' validation data" in {
    // Reference epoch in the book are for April 6, 2004, 16:43:00.0000 UTC or 16:44:04.1840 TT (not May 14)
    implicit val universe = new MockTimeUniverse()
    val jd_tt = new JulianDate(2004, 4,  6, 16, 44,  4.1840, TT)

    val utc = new UTCTime(new MockTime(-32.184))
    utc.offsetFromTT(jd_tt) should be(-64.1840 plusOrMinus 1E-4)
  }

  it should "select the correct UTC-TAI offset" in {
    // See IERSm Bulletin C 43
    // from 2009 January 1, 0h UTC, to 2012 July 1 0h UTC : UTC-TAI = - 34s
    // from 2012 July 1, 0h UTC, until further notice : UTC-TAI = - 35s
    implicit val universe = new MockTimeUniverse()
    val utc = new UTCTime(new MockTime(-32.184))

    // Just after (0.5s later) switch epoch in UTC-TAI=-35
    val jd_tt_1 = new JulianDate(TimeUtils.jday(2012, 7, 1, 0, 0, 0), TT).addS(35.5 + 32.184)
    // Just before (-0.5s) switch epoch in UTC-TAI=-34
    val jd_tt_2 = new JulianDate(TimeUtils.jday(2012, 7, 1, 0, 0, 0), TT).addS(34.5 + 32.184)

    utc.offsetFromTT(jd_tt_1) should be(-35 - 32.184 plusOrMinus 1E-16)
    utc.offsetFromTT(jd_tt_2) should be(-34 - 32.184 plusOrMinus 1E-16)
  }

}
