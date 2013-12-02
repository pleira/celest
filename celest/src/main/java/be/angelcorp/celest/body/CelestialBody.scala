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


/**
 * Representation of a celestial body (eg planet/sun/satellite).
 *
 * @author simon
 */
trait CelestialBody {

  /**
   * Get the gravitational parameter of the celestial body (μ = G * m). This is the result of both
   * the dry and wet mass of the body.
   * <p>
   * <b>Unit: [m<sup>3</sup> / s<sup>2</sup>]</b>
   * </p>
   *
   * @return Standard gravitational parameter of the body.
   */
  def μ: Double

  /**
   * Get the total mass of the celestial body.
   * <p>
   * <b>Unit: [kg]</b>
   * </p>
   *
   * @return Mass of the body [kg].
   */
  def mass: Double

}
