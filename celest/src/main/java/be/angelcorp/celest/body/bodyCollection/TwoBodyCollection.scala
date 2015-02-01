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
package be.angelcorp.celest.body.bodyCollection

import java.util.Collection
import scala.collection.JavaConversions._
import be.angelcorp.celest.body.CelestialBody

/**
 * Implementation of `ITwoBodyCollection`, this is a collection holding always just two bodies.
 * There are some methods to allow for getting the other body in the collection if one is known.
 *
 * @param body1 First body in the collection (usually the center body).
 * @param body2 Second body in the collection (usually the satellite body).
 *
 * @author Simon Billemont
 * @see ITwoBodyCollection
 */
class TwoBodyCollection(val body1: CelestialBody, val body2: CelestialBody) extends ITwoBodyCollection {

  override def getBodies: Collection[CelestialBody] = List(body1, body2)

  override def getBody1 = body1

  override def getBody2 = body2

  override def other(body: CelestialBody): CelestialBody = if (body eq body1) body2 else body1

}
