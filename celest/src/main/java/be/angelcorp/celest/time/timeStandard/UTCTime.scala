/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
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

import be.angelcorp.celest.time.dateStandard.DateStandards
import be.angelcorp.celest.time.Epoch
import java.util
import javax.inject.Inject
import be.angelcorp.celest.physics.Units
import be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations.TAI

/**
 * Coordinated Universal Time.
 *
 * <p>
 * Conversions based on:<br>
 * <a href="ftp://maia.usno.navy.mil/ser7/tai-utc.dat">ftp://maia.usno.navy.mil/ser7/tai-utc.dat</a>
 * </p>
 *
 * @author Simon Billemont
 *
 */
class UTCTime @Inject()(@TAI TAI: TimeStandard) extends TimeStandard {

  def offsetFromTT(JD_tt: Epoch) = {
    val from_tt = TAI.offsetFromTT(JD_tt)
    val jd = JD_tt.addS(from_tt).jd

    val entry = UTCTime.TAI_UTC.floorEntry(jd)
    var offset = -entry.getValue()(jd)

    // Check if the leap second pushes over a TAI - UTC bound
    if (jd + offset / Units.julianDay < entry.getKey) {
      val entry = UTCTime.TAI_UTC.floorEntry(jd - 1)
      offset = -entry.getValue()(jd)
    }
    offset + from_tt
  }

  def offsetToTT(JD_utc: Epoch) = {
    val jd = JD_utc.jd
    val entry = UTCTime.TAI_UTC.floorEntry(jd)
    val offset_TAI = entry.getValue()(jd)

    offset_TAI + TAI.offsetToTT(JD_utc)
  }

}

object UTCTime {

  /** Map with key=JD_TAI, value=TAI-UTC [sec] (the parameter is the exact query date in JD) */
  lazy val TAI_UTC = {
    val m: util.TreeMap[Double, Double => Double] = new util.TreeMap
    m.put(0.0, UTC_TAI_Function(0.0)); // Not defined before 1961 so no offset
    m.put(2437300.5, UTC_TAI_Function(1.4228180, 37300.0, 0.001296)); // 1961 JAN 1
    m.put(2437512.5, UTC_TAI_Function(1.3728180, 37300.0, 0.001296)); // 1961 AUG 1
    m.put(2437665.5, UTC_TAI_Function(1.8458580, 37665.0, 0.0011232)); // 1962 JAN 1
    m.put(2438334.5, UTC_TAI_Function(1.9458580, 37665.0, 0.0011232)); // 1963 NOV 1
    m.put(2438395.5, UTC_TAI_Function(3.2401300, 38761.0, 0.001296)); // 1964 JAN 1
    m.put(2438486.5, UTC_TAI_Function(3.3401300, 38761.0, 0.001296)); // 1964 APR 1
    m.put(2438639.5, UTC_TAI_Function(3.4401300, 38761.0, 0.001296)); // 1964 SEP 1
    m.put(2438761.5, UTC_TAI_Function(3.5401300, 38761.0, 0.001296)); // 1965 JAN 1
    m.put(2438820.5, UTC_TAI_Function(3.6401300, 38761.0, 0.001296)); // 1965 MAR 1
    m.put(2438942.5, UTC_TAI_Function(3.7401300, 38761.0, 0.001296)); // 1965 JUL 1
    m.put(2439004.5, UTC_TAI_Function(3.8401300, 38761.0, 0.001296)); // 1965 SEP 1
    m.put(2439126.5, UTC_TAI_Function(4.3131700, 39126.0, 0.002592)); // 1966 JAN 1
    m.put(2439887.5, UTC_TAI_Function(4.2131700, 39126.0, 0.002592)); // 1968 FEB 1
    m.put(2441317.5, UTC_TAI_Function(10.0)); // 1972 JAN 1
    m.put(2441499.5, UTC_TAI_Function(11.0)); // 1972 JUL 1
    m.put(2441683.5, UTC_TAI_Function(12.0)); // 1973 JAN 1
    m.put(2442048.5, UTC_TAI_Function(13.0)); // 1974 JAN 1
    m.put(2442413.5, UTC_TAI_Function(14.0)); // 1975 JAN 1
    m.put(2442778.5, UTC_TAI_Function(15.0)); // 1976 JAN 1
    m.put(2443144.5, UTC_TAI_Function(16.0)); // 1977 JAN 1
    m.put(2443509.5, UTC_TAI_Function(17.0)); // 1978 JAN 1
    m.put(2443874.5, UTC_TAI_Function(18.0)); // 1979 JAN 1
    m.put(2444239.5, UTC_TAI_Function(19.0)); // 1980 JAN 1
    m.put(2444786.5, UTC_TAI_Function(20.0)); // 1981 JUL 1
    m.put(2445151.5, UTC_TAI_Function(21.0)); // 1982 JUL 1
    m.put(2445516.5, UTC_TAI_Function(22.0)); // 1983 JUL 1
    m.put(2446247.5, UTC_TAI_Function(23.0)); // 1985 JUL 1
    m.put(2447161.5, UTC_TAI_Function(24.0)); // 1988 JAN 1
    m.put(2447892.5, UTC_TAI_Function(25.0)); // 1990 JAN 1
    m.put(2448257.5, UTC_TAI_Function(26.0)); // 1991 JAN 1
    m.put(2448804.5, UTC_TAI_Function(27.0)); // 1992 JUL 1
    m.put(2449169.5, UTC_TAI_Function(28.0)); // 1993 JUL 1
    m.put(2449534.5, UTC_TAI_Function(29.0)); // 1994 JUL 1
    m.put(2450083.5, UTC_TAI_Function(30.0)); // 1996 JAN 1
    m.put(2450630.5, UTC_TAI_Function(31.0)); // 1997 JUL 1
    m.put(2451179.5, UTC_TAI_Function(32.0)); // 1999 JAN 1
    m.put(2453736.5, UTC_TAI_Function(33.0)); // 2006 JAN 1
    m.put(2454832.5, UTC_TAI_Function(34.0)); // 2009 JAN 1
    m.put(2456109.5, UTC_TAI_Function(35.0)); // 2012 JUL 1
    m
  }

  /**
   * Function for TAI - UTC for dates between 1 Jan 1961 and 1 Jan 1972
   *
   * <pre>
   * TAI-UTC= a + (MJD - b) &times; c S
   * </pre>
   *
   * @param a Time bias [s]
   * @param b Date bias [Julian days]
   * @param c Time bias derivative [s / Julian day]
   */
  def UTC_TAI_Function(a: Double, b: Double, c: Double): Double => Double = {
    val b2 = DateStandards.MJD.toJD(0) + b
    x: Double => a + (x - b2) * c
  }

  /**
   * Function for TAI - UTC for a constant bias
   *
   * <pre>
   * TAI-UTC = c
   * </pre>
   *
   * @param c Time bias [s]
   */
  def UTC_TAI_Function(c: Double): Double => Double = {
    x: Double => c
  }

}

