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

import be.angelcorp.celest.data.eop.UT1Provider
import be.angelcorp.celest.time.JulianDate
import be.angelcorp.celest.time.dateStandard.DateStandards._
import be.angelcorp.celest.time.timeStandard.TimeStandards._
import com.google.inject.AbstractModule
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class TestUT1 extends FlatSpec with ShouldMatchers {

  implicit val universe = new MockTimeUniverse {

    override def additionalConfig = new AbstractModule {
      def configure() {
        bind(classOf[TimeStandard]).annotatedWith(classOf[TimeStandardAnnotations.UTC]).toInstance(new MockTime(35))
      }
    }
  }

  "The UT1Container" should "select the correct UT1-UTC offset" in {
    val container = new UT1Container(Map(0.0 -> 5.5, 1.0 -> 6.2, 2.0 -> 7.4, 3.0 -> 9.0))

    expect(6.2) {
      container.UT1_UTC(new JulianDate(1.0, MJD))
    }

    expect(5.5) {
      container.UT1_UTC(new JulianDate(0.5 - 1E-6, MJD))
    }
    expect(6.2) {
      container.UT1_UTC(new JulianDate(0.5, MJD))
    }
    expect(6.2) {
      container.UT1_UTC(new JulianDate(0.5 + 1E-6, MJD))
    }

    expect(6.2) {
      container.UT1_UTC(new JulianDate(1.5 - 1E-6, MJD))
    }
    expect(7.4) {
      container.UT1_UTC(new JulianDate(1.5, MJD))
    }
    expect(7.4) {
      container.UT1_UTC(new JulianDate(1.5 + 1E-6, MJD))
    }
  }

  "DefaultUT1" should "download the correct UT1 historic offsets from IERS" in {
    val ut1 = new UT1Time(UTC)

    expect(-0.0337602) {
      ut1.UT1_UTC(new JulianDate(38048, MJD, UTC))
    } // 1963 / 1 / 19
    expect(-0.4232747) {
      ut1.UT1_UTC(new JulianDate(55931, MJD, UTC))
    } // 2012 / 1 / 5
    expect(0.4044970) {
      ut1.UT1_UTC(new JulianDate(56160, MJD, UTC))
    } // 2012 / 8 / 21
  }

  it should "select Transform symmetrically" in {
    val container = new UT1Container(Map(0.0 -> 5.5, 1.0 -> 6.2, 2.0 -> 7.4, 3.0 -> 9.0, 4.0 -> 6.1, 5.0 -> 3.4, 6.0 -> 1.1, 7.0 -> -0.1, 8.0 -> -1.1, 9.0 -> -2.7))
    val ut1 = new UT1Time(UTC, Map[(Double, Double), UT1Provider]((0.0, 9.0) -> container))

    val base_date = new JulianDate(2.0, MJD, ut1)

    expect(base_date.jd) {
      base_date.inTimeStandard(UTC).inTimeStandard(ut1).jd
    }
  }

  /** Validated using Orekit:
    * System.setProperty("orekit.data.path", "c:/tmp/data") // Requires UTC-TAI.history and from IERS eopc04_08_IAU2000.12 (yearly EOP data for 2012 in IAU2000)
    * val ut1 = TimeScalesFactory.getUT1()
    * val utc = TimeScalesFactory.getUTC()
    * val d_utc = new AbsoluteDate( 2012, 03, 04, 00, 00, 0.0, utc) // Selected a at midnight to prevent interpolation
    * val offset = d_utc.timeScalesOffset( ut1, utc )
    * println( "UTC day: " + d_utc )
    * println( "UT1 - UTC = " + offset )
    * val d_ut1 = d_utc.getComponents( ut1 )
    * println( "UT1 day: " + d_ut1 )
    * println( "Seconds in UT1 day: %16.16f".format(d_ut1.getTime().getSecondsInDay()) )
    *
    * Output:
    * UTC day: 2012-03-04T00:00:00.000
    * UT1 - UTC = -0.4739945999999975
    * UT1 day: 2012-03-03T23:59:59.526
    * Seconds in UT1 day: 86399,5260054000000000
    */
  it should "select correctly apply the UT1-UTC offset" in {
    val ut1 = new UT1Time(UTC)

    val date_utc = new JulianDate(2012,  3,  4,  0,  0, 0.0, UTC)
    val date_ut1 = date_utc.inTimeStandard(ut1)

    ((date_ut1.jd + 0.5) - JULIAN_DAY_NUMBER.fromJD(date_ut1.jd)) * 86400 should be(86399.52600540 plusOrMinus 1E-4) // 1E-4 due to the doule accuracy in JulianDate
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

    val ut1 = new UT1Time(new MockTime(-64.184 /* UTC => TT offset at the test epoch */), // <= UTC
      Map((0.0, Double.PositiveInfinity) -> // UT1-UTC offset
        new UT1Container(Map(53140.0 -> -0.463326))))
    ut1.offsetFromTT(jd_tt) should be(-64.647326 plusOrMinus 1E-6)
  }

}
