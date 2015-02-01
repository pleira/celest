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
import scala.collection.mutable
import scala.collection.JavaConversions._
import be.angelcorp.celest.body.CelestialBody

/**
 * Basic implementation of a [[be.angelcorp.celest.body.bodyCollection.IBodyCollection]] using a [[scala.collection.mutable.Set]]
 *
 * @param bodySet Collection of bodies in the [[be.angelcorp.celest.body.bodyCollection.IBodyCollection]]
 *
 * @author Simon Billemont
 */
class BasicBodyCollection(val bodySet: mutable.Set[CelestialBody]) extends IBodyCollection {

  override def getBodies: Collection[CelestialBody] = asJavaCollection(bodySet)

}
