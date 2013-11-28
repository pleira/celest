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

package be.angelcorp.celest.universe

import be.angelcorp.celest.time.timeStandard._
import be.angelcorp.celest.frameGraph.ReferenceFrameGraph
import be.angelcorp.celest.time.JulianDate
import be.angelcorp.libs.util.physics.Time
import com.google.inject.Injector

/**
 * Contains all the context information regarding a simulation, such as the reference frame/time data.
 */
trait Universe {

  /** International Atomic Time (TAI) */
  def TAI: ITimeStandard

  /** Barycentric Coordinate Time (TCB) */
  def TCB: ITimeStandard

  /** Geocentric Coordinate Time (TCG) */
  def TCG: ITimeStandard

  /** Barycentric Dynamical Time (TDB) */
  def TDB: ITimeStandard

  /** Terrestrial Dynamical Time (TDT, is now TT) */
  def TDT: ITimeStandard

  /** Terrestrial Time (TT) */
  def TT: ITimeStandard

  /** Coordinated Universal Time (UTC) */
  def UTC: ITimeStandard

  /** Universal Time (UT1) */
  def UT1: ITimeStandard

  /** The J2000 epoch in JulianDate form. */
  lazy val J2000_EPOCH = new JulianDate(2451545.0, TT)(this)
  /** The J1950 epoch in JulianDate form. */
  lazy val J1950_EPOCH = new JulianDate(2433282.5, TT)(this)
  /** The J1900 epoch in JulianDate form. */
  lazy val J1900_EPOCH = new JulianDate(2415020.0, TT)(this)
  /** The B1950 epoch in JulianDate form. */
  lazy val B1950_EPOCH = new JulianDate(2433282.42345905, TT)(this)

  /** The starting epoch of the TAI timeline: 1 January 1977 00:00:00 TAI (same as TAI/TT/TCG/TCB). */
  lazy val TAI_EPOCH = new JulianDate(2443144.5003725, TT)(this)
  /** The starting epoch of the TT timeline: 1 January 1977 00:00:00 TAI (same as TAI/TT/TCG/TCB). */
  lazy val TT_EPOCH = TAI_EPOCH
  /** The starting epoch of the TCG timeline: 1 January 1977 00:00:00 TAI (same as TAI/TT/TCG/TCB). */
  lazy val TCG_EPOCH = TAI_EPOCH
  /** The starting epoch of the TCB timeline: 1 January 1977 00:00:00 TAI (same as TAI/TT/TCG/TCB). */
  lazy val TCB_EPOCH = TAI_EPOCH
  /** The starting epoch of the TDB timeline. */
  lazy val TDB_EPOCH = TAI_EPOCH.add(-65.5, Time.microsecond)

  /** Reference frame graph */
  def frames: ReferenceFrameGraph

  /** Dependency injector */
  def injector: Injector

  def epoch(year: Int) = new JulianDate(year, 1, 1, 12, 0, 0, TT)(this)

}
