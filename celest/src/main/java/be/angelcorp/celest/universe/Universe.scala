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

  /** Reference frame graph */
  def frames: ReferenceFrameGraph

  /** Dependency injector */
  def injector: Injector

  def epoch(year: Int) = new JulianDate(year, 1, 1, 12, 0, 0, TT)(this)

}
