/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
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

package be.angelcorp.libs.celest.time.timeStandard

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import be.angelcorp.libs.celest.time.{JulianDate, IJulianDate}
import be.angelcorp.libs.celest.universe.Universe
import be.angelcorp.libs.celest.frames.IReferenceFrameGraph
import org.scalatest.exceptions.TestFailedException
import be.angelcorp.libs.celest.time.dateStandard.DateStandards._

@RunWith(classOf[JUnitRunner])
class TestUT1 extends FlatSpec with ShouldMatchers {

  class MockTime(val offset: Double) extends ITimeStandard {
    def offsetFromTT(JD_tt: IJulianDate) =  offset
    def offsetToTT(JD_this: IJulianDate) = -offset
  }

  implicit val universe = new Universe(){
    def TAI: ITimeStandard = throw new TestFailedException("Unsupported mock operation", 0)
    def TCB: ITimeStandard = throw new TestFailedException("Unsupported mock operation", 0)
    def TCG: ITimeStandard = throw new TestFailedException("Unsupported mock operation", 0)
    def TDB: ITimeStandard = throw new TestFailedException("Unsupported mock operation", 0)
    def TDT: ITimeStandard = throw new TestFailedException("Unsupported mock operation", 0)
    def TT:  ITimeStandard = new MockTime( 0  )
    def UTC: ITimeStandard = new MockTime( 35 )
    def UT1: ITimeStandard = throw new TestFailedException("Unsupported mock operation", 0)
    def frames: IReferenceFrameGraph = throw new TestFailedException("Unsupported mock operation", 0)
  }

  "The UT1Container" should "select the correct UT1-UTC offset" in {
    val container = new UT1Container( Map( 0.0 -> 5.5, 1.0 -> 6.2, 2.0 -> 7.4, 3.0 -> 9.0 ) )

    expect( 6.2 ) { container.UT1_UTC( new JulianDate( 1.0, MJD ) ) }

    expect( 5.5 ) { container.UT1_UTC( new JulianDate( 0.5 - 1E-6, MJD ) ) }
    expect( 6.2 ) { container.UT1_UTC( new JulianDate( 0.5       , MJD ) ) }
    expect( 6.2 ) { container.UT1_UTC( new JulianDate( 0.5 + 1E-6, MJD ) ) }

    expect( 6.2 ) { container.UT1_UTC( new JulianDate( 1.5 - 1E-6, MJD ) ) }
    expect( 7.4 ) { container.UT1_UTC( new JulianDate( 1.5       , MJD ) ) }
    expect( 7.4 ) { container.UT1_UTC( new JulianDate( 1.5 + 1E-6, MJD ) ) }
  }

  "DefaultUT1" should "download the correct UT1 historic offsets from IERS" in {
    val ut1 = new DefaultUT1( universe.UTC )

    expect( -0.0337602 ) { ut1.UT1_UTC( new JulianDate( 38048, MJD, universe.UTC ) ) } // 1963 / 1 / 19
    expect( -0.4232747 ) { ut1.UT1_UTC( new JulianDate( 55931, MJD, universe.UTC ) ) } // 2012 / 1 / 5
    expect(  0.4044970 ) { ut1.UT1_UTC( new JulianDate( 56160, MJD, universe.UTC ) ) } // 2012 / 8 / 21
  }

  it should "select Transform from and to TAI symmetrically" in {
    val container = new UT1Container( Map( 0.0 -> 5.5, 1.0 -> 6.2, 2.0 -> 7.4, 3.0 -> 9.0, 4.0 -> 6.1, 5.0 -> 3.4, 6.0 -> 1.1, 7.0 -> -0.1, 8.0 -> -1.1, 9.0 -> -2.7 ) )
    val ut1 = new DefaultUT1( universe.UTC, Map[(Double, Double), UT1Provider]( (0.0, 9.0) -> container ) )

    val base_date = new JulianDate( 2.0, MJD, ut1 )

    expect( base_date.getJD  ) { base_date.getJulianDate( universe.UTC ).getJulianDate( ut1 ).getJD }
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
    val ut1 = new DefaultUT1( universe.UTC )

    val date_utc = new JulianDate( 2012, 03, 04, 00, 00, 0.0, universe.UTC )
    val date_ut1 = date_utc.getJulianDate( ut1 )

    ( ( date_ut1.getJD + 0.5 ) - date_ut1.getJulianDate( JULIAN_DAY_NUMBER )) * 86400 should be (86399.52600540 plusOrMinus 1E-4) // 1E-4 due to the doule accuracy in JulianDate
  }

}
