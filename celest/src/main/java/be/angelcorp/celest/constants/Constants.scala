/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
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
package be.angelcorp.celest.constants

/**
 * Various physical constants
 * <p>
 * From http://en.wikipedia.org/wiki/Physical_constant
 * </p>
 *
 * @author Simon Billemont, TUDelft, Faculty Aerospace Engineering (aodtorusan@gmail.com or
 *         s.billemont@student.tudelft.nl)
 */
object Constants {

  /**
   * Universal gravitational constant
   * <p>
   * <b>Unit: [(m<sup>3</sup>)/(kg s<sup>2</sup>)]</b>
   * </p>
   */
  val GRAVITATIONAL_CONSTANT = 6.6742800E-11

  /**
   * Speed of light in a vacuum
   * <p>
   * <b>Unit: [m/s]</b>
   * </p>
   */
  val SPEED_LIGHT = 299792458E0

  /**
   * Planck's constant
   * <p>
   * <b>Unit: [J s]</b>
   * </p>
   */
  val PLANCK = 6.6260686E-34

  /**
   * Reduced Planck's constant
   * <p>
   * <b>Unit: [J s]</b>
   * </p>
   */
  val PLANCK_REDUCED = 1.0545716E-34

  /**
   * Magnetic constant (vacuum permeability)
   * <p>
   * <b>Unit: [N/A]</b>
   * </p>
   */
  val PERMEABILITY = 1.2566370E-6

  /**
   * Electric constant (vacuum permittivity)
   * <p>
   * <b>Unit: [F/m]</b>
   * </p>
   */
  val PERMITTIVITY = 8.8541878E-12

  /**
   * Convert from mass to gravitational parameter
   * <p>
   * <b>Unit: [kg] to [m<sup>3</sup>/s<sup>2</sup>]</b>
   * </p>
   *
   * @param mass Mass of a celestial object [kg]
   * @return Standard gravitational parameter [m<sup>3</sup>/s<sup>2</sup>]
   */
  def mass2mu(mass: Double) = GRAVITATIONAL_CONSTANT * mass

  /**
   * Convert from gravitational parameter to mass
   * <p>
   * <b>Unit: [m<sup>3</sup>/s<sup>2</sup>] to [kg]</b>
   * </p>
   *
   * @param mu Standard gravitational parameter [m<sup>3</sup>/s<sup>2</sup>]
   * @return Mass of a celestial object [kg]
   */
  def mu2mass(mu: Double) = mu / GRAVITATIONAL_CONSTANT

}
