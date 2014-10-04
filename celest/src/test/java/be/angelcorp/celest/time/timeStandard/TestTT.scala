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
import be.angelcorp.celest.time.timeStandard.TimeStandards._
import be.angelcorp.celest.universe.DefaultUniverse
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
 * TT is very hard to test, as it is the time reference to which all other time streams are referenced.
 */
class TestTT extends FlatSpec with ShouldMatchers {

  "TT" should "select transform symmetrically" in {
    implicit val universe = new MockTimeUniverse
    val base_date = new JulianDate(2.0, TT)

    expect(base_date.jd) {
      base_date.inTimeStandard(new MockTime(32)).inTimeStandard(TT).jd
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
    implicit val universe = new MockTimeUniverse
    val date = new JulianDate(2453101.827411875104167, new MockTime(-32 - 32.184))

    // Reference seconds in the current day TT
    val tt_seconds = 28352.5700090000

    // 1E-4 due to the precision restriction of JulianDate
    val jd_tt = date.inTimeStandard(TT)
    secondsInDay(jd_tt.jd) should be(tt_seconds plusOrMinus 1E-4)
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
    implicit val universe = new DefaultUniverse() // <= universe with the under test TT
    val jd_base = 2453102
    val tai = new HourMinSec(16, 43, 32.0000).dayFraction // Reference TAI fraction in day
    val tt = new HourMinSec(16, 44, 04.1840).dayFraction // Reference TT  fraction in day
    val jd_tai = new JulianDate(jd_base + tai, TAI) // Reference epoch in TAI

    val jd_tt = jd_tai.inTimeStandard(TT) // Computed epoch in TT

    // 1E-4 due to the precision restriction of JulianDate
    secondsInDay(jd_tt.jd) should be(secondsInDay(jd_base + tt) plusOrMinus 1E-4)
  }
}
