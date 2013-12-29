/**
 * Copyright (C) 2012 Simon Billemont <simon@angelcorp.be>
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

package be.angelcorp.celest.frameGraph

/**
 * Basic implementation of a ReferenceFrame, that can be identified solely by its name.
 *
 * @author Simon Billemont
 */
class NamedReferenceFrame(val name: String) extends ReferenceSystem

object NamedReferenceFrame {

  def unapply(frame: ReferenceSystem): Option[String] = frame match {
    case nrf: NamedReferenceFrame => Some(nrf.name)
    case _ => None
  }

}
