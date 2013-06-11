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

package be.angelcorp.libs.celest.data

import be.angelcorp.libs.celest.universe.Universe
import be.angelcorp.libs.celest.time.IJulianDate
import java.util

class DefaultEarthOrientationData(implicit universe: Universe) extends EarthOrientationData( new  util.TreeMap() ) {

  /**
   * Tries to load an additional EOP data file from the IERS: EOP 08 C04 (IAU2000) yearly file containing:
   * <ul>
   *  <li>x/y pole</li>
   *  <li>UT1-UTC</li>
   *  <li>LOD</li>
   *  <li>dX/dY (smoothed values at 1-day intervals)</li>
   * </ul>
   * Loaded values are with respect to IAU 2006/2000A precession-nutation model and consistent with ITRF2008.
   * @param epoch Epoch that the datafile should contain.
   */
  def loadMoreData( epoch: IJulianDate ) {
    // Load a IERS EOP 08 C04 (IAU2000A) - yearly file

    // Year data to retrieve
    val year     = epoch.getDate.getYear.toString
    val filename = "eopc04_IAU2000.%s".format( year takeRight 2  )

    getDataFile( "iers/eop/long-term/c04_08/iau2000/"+filename ) match {
      case Some(content) => {
        val newData = EarthOrientationData(content)
        data.putAll( newData.data )
      }
      case _ =>
    }
  }

  override def onFail(epoch: IJulianDate, Δt: Double) = {
    loadMoreData(epoch)
    findEntry( epoch, Δt ) match {
      case Some(entry) => entry
      case None        => super.onFail(epoch, Δt)
    }
  }

}
