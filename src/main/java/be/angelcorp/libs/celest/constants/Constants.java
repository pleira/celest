/**
 * Copyright (C) 2010 Simon Billemont <aodtorusan@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.libs.celest.constants;

/**
 * Various physical constants
 * <p>
 * From http://en.wikipedia.org/wiki/Physical_constant
 * </p>
 * 
 * @author Simon Billemont, TUDelft, Faculty Aerospace Engineering (aodtorusan@gmail.com or
 *         s.billemont@student.tudelft.nl)
 * 
 */
public abstract class Constants {

	/**
	 * Universal gravitational constant
	 * <p>
	 * <b>Unit: [(m<sup>3</sup>)/(kg s<sup>2</sup>)]</b>
	 * </p>
	 */
	public static final double	GRAVITATIONAL_CONSTANT	= 6.6742800E-11d;
	/**
	 * Speed of light in a vacuum
	 * <p>
	 * <b>Unit: [m/s]</b>
	 * </p>
	 */
	public static final double	SPEED_LIGHT				= 299792458;
	/**
	 * Planck's constant
	 * <p>
	 * <b>Unit: [J s]</b>
	 * </p>
	 */
	public static final double	PLANCK					= 6.6260686E-34;
	/**
	 * Reduced Planck's constant
	 * <p>
	 * <b>Unit: [J s]</b>
	 * </p>
	 */
	public static final double	PLANCK_REDUCED			= 1.0545716E-34;

	/**
	 * Magnetic constant (vacuum permeability)
	 * <p>
	 * <b>Unit: [N/A]</b>
	 * </p>
	 */
	public static final double	PERMEABILITY			= 1.2566370E-6;
	/**
	 * Electric constant (vacuum permittivity)
	 * <p>
	 * <b>Unit: [F/m]</b>
	 * </p>
	 */
	public static final double	PERMITTIVITY			= 8.8541878E-12;

	/**
	 * Convert from mass to gravitational parameter
	 * <p>
	 * <b>Unit: [kg] to [m<sup>3</sup>/s<sup>2</sup>]</b>
	 * </p>
	 * 
	 * @param mass
	 *            Mass of a celestial object [kg]
	 * @return Standard gravitational parameter [m<sup>3</sup>/s<sup>2</sup>]
	 */
	public static double mass2mu(double mass) {
		return GRAVITATIONAL_CONSTANT * mass;
	}

	/**
	 * Convert from gravitational parameter to mass
	 * <p>
	 * <b>Unit: [m<sup>3</sup>/s<sup>2</sup>] to [kg]</b>
	 * </p>
	 * 
	 * @param mass
	 *            Standard gravitational parameter [m<sup>3</sup>/s<sup>2</sup>]
	 * @return Mass of a celestial object [kg]
	 */
	public static double mu2mass(double mu) {
		return mu / GRAVITATIONAL_CONSTANT;
	}

}
