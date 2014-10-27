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
import java.util.{GregorianCalendar, Calendar}
import javax.inject.Inject
import be.angelcorp.celest.resources.{ResourceDescription, Resources}
import org.slf4j.LoggerFactory
import be.angelcorp.celest.universe.Universe
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.time.timeStandard.TimeStandard
import be.angelcorp.celest.time.timeStandard.TimeStandardAnnotations.UTC

import scala.util.Success

class DefaultEarthOrientationData @Inject()(implicit universe: Universe) extends EarthOrientationData(new util.TreeMap(), universe.instance[TimeStandard, UTC]) {
  private val logger = LoggerFactory.getLogger(getClass)

  /**
   * Tries to load an additional EOP data file from the IERS: EOP 08 C04 (IAU2000) yearly file containing:
   * <ul>
   * <li>x/y pole</li>
   * <li>UT1-UTC</li>
   * <li>LOD</li>
   * <li>dX/dY (smoothed values at 1-day intervals)</li>
   * </ul>
   * Loaded values are with respect to IAU 2006/2000A precession-nutation model and consistent with ITRF2008.
   * @param epoch Epoch that the datafile should contain.
   */
  def loadMoreData(epoch: Epoch) {
    // Load a IERS EOP 08 C04 (IAU2000A) - yearly file

    // Year data to retrieve
    val cal = new GregorianCalendar()
    cal.setTime(epoch.date)
    val year = cal.get(Calendar.YEAR).toString
    val filename = "eopc04_IAU2000.%s".format(year takeRight 2)

    Resources.findArchive(ResourceDescription("org.iers.products.eop.long-term.c04_08", "iau2000", extension = "zip")).flatMap( res => {
      res.findEntry( filename )
    } ) match {
      case Success(source) =>
        val newData = EarthOrientationData(source.openSource())
        data.putAll(newData.data)
      case _ =>
        logger.warn(s"Could not find file $filename in artifact org.iers.products.eop.long-term.c04_08:iau2000 for Earth orientation data on $epoch")
    }
  }

  override def onFail(epoch: Epoch, Δt: Double) = {
    loadMoreData(epoch)
    findEntry(epoch, Δt) match {
      case Some(entry) => entry
      case None => super.onFail(epoch, Δt)
    }
  }

}
