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

package be.angelcorp.celest.data.eop

import java.util
import scala.math._
import scala.io.Source
import scala.collection.JavaConverters._
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.universe.Universe
import be.angelcorp.celest.physics.Units._
import be.angelcorp.celest.time.dateStandard.DateStandards
import be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations.UTC
import be.angelcorp.celest.time.timeStandard.TimeStandard
import javax.inject.Inject

class EarthOrientationData(val data: util.TreeMap[Double, EarthOrientationDataEntry], @UTC val utc: TimeStandard) {

  @Inject
  def this(@UTC utc: TimeStandard) = this(new util.TreeMap[Double, EarthOrientationDataEntry](), utc)

  def epochRange = if (data.isEmpty) (Double.NaN, Double.NaN) else (data.firstKey() - 0.5, data.lastKey() + 0.5)

  /**
   * Find the entry closest to the given epoch. If there is no data stored, or the closest entry is further away than
   * the maximum epoch separation, [[scala.None]] is returned.
   *
   * @param epoch Find the eop closest to this epoch.
   * @param Δt_max Maximum allowable time difference between the epoch and the closest entry [days].
   * @return Optionally the closest eop entry to the given epoch.
   */
  def findEntry(epoch: Epoch, Δt_max: Double = 1.0) = {
    val mjd_utc = DateStandards.MJD.fromJD(epoch.inTimeStandard(utc).jd)
    val low = data.floorEntry(mjd_utc)
    val high = data.ceilingEntry(mjd_utc)

    if (low != null && high != null) {
      // Find the closes entry in the offsets map and return its utc-ut1 value
      val closest = if (abs(mjd_utc - low.getKey) < abs(mjd_utc - high.getKey)) low.getValue else high.getValue
      if (abs(mjd_utc - closest.mjd) <= Δt_max) Some(closest) else None
    } else if (low != null || high != null) {
      // Its a boundary value of the map
      val closest = if (low != null) low.getValue else high.getValue
      if (abs(mjd_utc - closest.mjd) <= Δt_max) Some(closest) else None
    } else {
      None
    }
  }

  /**
   * Get the entry closest to the specified epoch. If the closes entry is further away then the set limit by Δt_max,
   * onFail(Epoch, Double) is returned.
   *
   * @param epoch Find the eop closest to this epoch.
   * @param Δt_max Maximum allowable time difference between the epoch and the closest entry [days].
   * @return The closest eop entry to the given epoch.
   */
  def getEntry(epoch: Epoch, Δt_max: Double = 1.0) = findEntry(epoch) match {
    case Some(entry) => entry
    case None => onFail(epoch, Δt_max)
  }

  /**
   * Action to undertake when the current dataset does not contain a valid eop entry for the specified epoch.
   * This function should try to find some matching eop, or throw a RuntimeException.
   *
   * @param epoch Find the eop closest to this epoch.
   * @param Δt_max Maximum allowable time difference between the epoch and the closest entry [days].
   * @return Recovered eop entry that is within Δt_max of the specified epoch.
   */
  def onFail(epoch: Epoch, Δt_max: Double): EarthOrientationDataEntry =
    throw new RuntimeException("Failed to locate EOP data for date " + epoch)

  /** Provider for the excess length of of day. */
  def lod = new ExcessLengthOfDay {
    override def lod(epoch: Epoch) = getEntry(epoch).lod
  }

  /** Provider for the UT1-UTC data. */
  def ut1_utc = new UT1Provider {
    override def UT1_UTC(epoch: Epoch): Double = getEntry(epoch inTimeStandard utc).ut1_utc
  }

  /** Provider for the position of the CIP (celestial intermediate pole) wrt ITRS (x,y) */
  def cip = new PoleProvider {
    override def polarCoordinatesOn(epoch: Epoch) = {
      val eopEntry = getEntry(epoch)
      (eopEntry.x, eopEntry.y)
    }
  }

  /** Provider for the IERS CIP offsets wrt IAU 2006/2000A (dx, dy) */
  def cipOffset = new PoleProvider {
    override def polarCoordinatesOn(epoch: Epoch) = {
      val eopEntry = getEntry(epoch)
      (eopEntry.dx, eopEntry.dy)
    }
  }

}

object EarthOrientationData {

  /**
   * Load an IERS data file with EOP data into a new [[eop.EarthOrientationData]] object.
   *
   * <p>Build for the IERS EOP C04 yearly file text format.</p>
   *
   * @param content IERS data file content
   * @return New eop data container.
   */
  def apply(content: Source)(implicit universe: Universe) = {
    // Convert the downloaded data to the format used by [[EarthOrientationData]]
    val dataEntries = content.getLines().drop(13).filterNot(_.isEmpty).map(line => {
      val entries = line.split( """\s+""")
      // yr mo day mjd x" y" ut1-utc lod dx dy ...
      val year = entries(0).toInt
      val month = entries(1).toInt
      val day = entries(2).toInt
      val mjd = entries(3).toInt
      val x = arcSeconds(entries(4).toDouble)
      val y = arcSeconds(entries(5).toDouble)
      val ut1_utc = entries(6).toDouble
      val lod = entries(7).toDouble
      val dx = arcSeconds(entries(8).toDouble)
      val dy = arcSeconds(entries(9).toDouble)
      new EarthOrientationDataEntry(year, month, day, mjd, x, y, ut1_utc, lod, dx, dy)
    })

    val dataMap = new util.TreeMap[Double, EarthOrientationDataEntry]
    dataMap.putAll(dataEntries.map(x => (x.mjd.toDouble, x)).toMap.asJava)
    new EarthOrientationData(dataMap, universe.instance[TimeStandard, UTC])
  }

}

/**
 * A single EOP data entry.
 * @param year  UTC year of the entry epoch.
 * @param month UTC month of the entry epoch.
 * @param day   UTC day of the entry epoch.
 * @param mjd   UTC mean julian date (mjd) of the epoch.
 * @param x     Pole x coordinate [rad]
 * @param y     Pole y coordinate [rad]
 * @param ut1_utc UT1 - UTC offset [s]
 * @param lod   Excess length of day [s]
 * @param dx    Nutation dx [rad]
 * @param dy    Nutation dy [rad]
 */
class EarthOrientationDataEntry(val year: Int, val month: Int, val day: Int, val mjd: Int,
                                val x: Double, val y: Double,
                                val ut1_utc: Double, val lod: Double,
                                val dx: Double, val dy: Double)

