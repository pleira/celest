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

package be.angelcorp.celest.body

import be.angelcorp.celest.universe.Universe
import com.google.inject.Key
import be.angelcorp.celest.body.CelestialBodyAnnotations._

/**
 * A set of short-hand functions to retrieve predefined celestial bodies (see [[be.angelcorp.celest.body.CelestialBodyAnnotations]]) from the universe.
 */
object CelestialBodies {

  /** See [[be.angelcorp.celest.body.CelestialBodyAnnotations.Mercury]] */
  def mercury(implicit universe: Universe) = universe.injector.getInstance(Key.get(classOf[Body], classOf[Mercury]))

  /** See [[be.angelcorp.celest.body.CelestialBodyAnnotations.Venus]] */
  def venus(implicit universe: Universe) = universe.injector.getInstance(Key.get(classOf[Body], classOf[Venus]))

  /** See [[be.angelcorp.celest.body.CelestialBodyAnnotations.Earth]] */
  def earth(implicit universe: Universe) = universe.injector.getInstance(Key.get(classOf[Body], classOf[Earth]))

  /** See [[be.angelcorp.celest.body.CelestialBodyAnnotations.Moon]] */
  def moon(implicit universe: Universe) = universe.injector.getInstance(Key.get(classOf[Body], classOf[Moon]))

  /** See [[be.angelcorp.celest.body.CelestialBodyAnnotations.EarthMoonBarycenter]] */
  def earthMoonBarycenter(implicit universe: Universe) = universe.injector.getInstance(Key.get(classOf[Body], classOf[EarthMoonBarycenter]))

  /** See [[be.angelcorp.celest.body.CelestialBodyAnnotations.Mars]] */
  def mars(implicit universe: Universe) = universe.injector.getInstance(Key.get(classOf[Body], classOf[Mars]))

  /** See [[be.angelcorp.celest.body.CelestialBodyAnnotations.Jupiter]] */
  def jupiter(implicit universe: Universe) = universe.injector.getInstance(Key.get(classOf[Body], classOf[Jupiter]))

  /** See [[be.angelcorp.celest.body.CelestialBodyAnnotations.Saturn]] */
  def saturn(implicit universe: Universe) = universe.injector.getInstance(Key.get(classOf[Body], classOf[Saturn]))

  /** See [[be.angelcorp.celest.body.CelestialBodyAnnotations.Uranus]] */
  def uranus(implicit universe: Universe) = universe.injector.getInstance(Key.get(classOf[Body], classOf[Uranus]))

  /** See [[be.angelcorp.celest.body.CelestialBodyAnnotations.Neptune]] */
  def neptune(implicit universe: Universe) = universe.injector.getInstance(Key.get(classOf[Body], classOf[Neptune]))

  /** See [[be.angelcorp.celest.body.CelestialBodyAnnotations.Pluto]] */
  def pluto(implicit universe: Universe) = universe.injector.getInstance(Key.get(classOf[Body], classOf[Pluto]))

  /** See [[be.angelcorp.celest.body.CelestialBodyAnnotations.Sun]] */
  def sun(implicit universe: Universe) = universe.injector.getInstance(Key.get(classOf[Body], classOf[Sun]))

  /** See [[be.angelcorp.celest.body.CelestialBodyAnnotations.SolarSystemBarycenter]] */
  def solarSystemBarycenter(implicit universe: Universe) = universe.injector.getInstance(Key.get(classOf[Body], classOf[SolarSystemBarycenter]))

}
