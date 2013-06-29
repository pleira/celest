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

import java.util
import java.net.URI
import java.io._
import scala.math._
import scala.Some
import scala.collection.mutable
import scala.collection.JavaConverters._
import be.angelcorp.libs.celest.time.IJulianDate
import be.angelcorp.libs.celest.time.dateStandard.DateStandards._
import be.angelcorp.libs.celest.data._
import be.angelcorp.libs.util.physics.Time._
import be.angelcorp.libs.celest.universe.Universe


/**
 * @param utc The UTC reference time scale to use
 * @param containers Map linking an UTC MJD date range to a specific container < [mjd utc min, mjd utc max], provider >
 */
class DefaultUT1( utc: ITimeStandard,
                  val containers: mutable.Map[(Double, Double), UT1Provider] = mutable.Map()
                    )(implicit universe: Universe) extends ITimeStandard with UT1Provider {

  def this( utc: ITimeStandard, immutableContainers: Map[(Double, Double), UT1Provider] )(implicit universe: Universe) =
    this( utc, collection.mutable.Map(immutableContainers.toSeq: _*)  )

  override def offsetFromTT(jd_tt: IJulianDate) = {
    utc.offsetFromTT(jd_tt) + UT1_UTC( jd_tt.getJulianDate( utc ) )
  }

  override def offsetToTT(jd_this: IJulianDate) = {
    var error = - UT1_UTC( jd_this )
    var utc_approximation = jd_this
    var utc_approximation_offset = 0.0
    do {
      utc_approximation = utc_approximation.add(error, second)
      utc_approximation_offset = UT1_UTC( utc_approximation )
      error = utc_approximation.add( utc_approximation_offset, second ).relativeTo( jd_this, second )
    } while (abs(error) > 1E-3)

    utc.offsetToTT( utc_approximation ) - utc_approximation_offset
  }

  def findUTCData( jd_utc: IJulianDate ) = {
    // Download IERS EOP 08 C04 (IAU1980) yearly files

    // Year data to retrieve
    val year     = jd_utc.getDate.getYear.toString
    val filename = "eopc04.%s".format( year takeRight 2  )
    val file     = new File( filename )
    val url      = "ftp://hpiers.obspm.fr/iers/eop/eopc04/%s".format( filename )

    getDataFile( "iers/eop/long-term/c04_08/iau2000/eopc04_08_IAU2000." + (year takeRight 2), List(URI.create(url)) ) match {
      case Some( data ) =>
        val container = EarthOrientationData( data )
        containers.put( container.epochRange, container )
      case None =>
    }

  }

  override def UT1_UTC( jd_utc: IJulianDate ) = UT1_UTC(jd_utc, tryDownload = true)

  def UT1_UTC( jd_utc: IJulianDate, tryDownload: Boolean = true): Double =
    containers.find( e => {
      // A container exists if this jd (jd_utc) is between the two boundaries (meaning both compareTo's have a different sign)
      val mjd_utc = jd_utc.getJulianDate(MJD)
      (e._1._1.compareTo( mjd_utc ) * e._1._2.compareTo( mjd_utc ) <= 0)
    } ) match {
      // We found a matching container, return its UT1-UTC
      case Some((range, container)) => container.UT1_UTC( jd_utc )
      // No entry found, try downloading the relevant data file and try again
      case None => {
        if (tryDownload) {
          findUTCData( jd_utc )
          UT1_UTC( jd_utc, tryDownload = false )
        } else {
          throw new UT1DateOutOfBounds()
        }
      }
    }

}

/**
 * Exception indicating that no UT1-UTC data could e found.
 * @param cause The cause of this exception.
 *              (A <tt>null</tt> value is  permitted, and indicates that the cause is nonexistent or unknown.)
 * @param msg The detailed error message.
 */
class UT1DateOutOfBounds(cause: Throwable = null,
                         msg: String = "Could not find any relavant UT1-UTC data")
                              extends RuntimeException(msg, cause)

/**
 * Interface to a map linking mjd date to UT1-UTC.
 * @param offsets Map that contains the <mjd date, UT1-UTC> data.
 */
class UT1Container(val offsets: util.TreeMap[Double, Double] = new util.TreeMap) extends UT1Provider {

  def this(m: Map[Double, Double]) = {
    this()
    offsets.putAll( m.asJava )
  }

  override def UT1_UTC( jd_utc: IJulianDate ) = {
    val mjd_utc = jd_utc.getJulianDate( MJD )
    val low  = offsets.floorEntry(   mjd_utc )
    val high = offsets.ceilingEntry( mjd_utc )

    if (low != null && high != null) {
      // Find the closes entry in the offsets map and return its utc-ut1 value
      if (abs(mjd_utc - low.getKey) < abs(mjd_utc - high.getKey)) low.getValue else high.getValue
    } else if (low != null || high != null) {
      // Its a boundary value of the map
      if (low != null) low.getValue else high.getValue
    } else {
      throw new UT1DateOutOfBounds()
    }
  }

}
