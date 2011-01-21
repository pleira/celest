/**
 * Copyright (C) 2011 Simon Billemont <aodtorusan@gmail.com>
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 3.0 Unported
 * (CC BY-NC 3.0) (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *        http://creativecommons.org/licenses/by-nc/3.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.libs.celest.constants;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.stateVector.CartesianElements;

/**
 * Different constants specific to the sun. Has constants in the following categories:
 * <ul>
 * <li>Orbital parameters</li>
 * <li>Physical constants</li>
 * <li>Geometric constants</li>
 * </ul>
 * 
 * @author Simon Billemont, TUDelft, Faculty Aerospace Engineering (aodtorusan@gmail.com or
 *         s.billemont@student.tudelft.nl)
 */
public abstract class SolarConstants {

	/**
	 * Mass of the sun
	 * <p>
	 * <b>Unit: [kg]</b>
	 * </p>
	 */
	public static final double	mass				= 1.9891E30;
	/**
	 * Standard gravitational parameter of the sun
	 * <p>
	 * <b>Unit: [m<sup>3</sup>/s<sup>2</sup>]</b>
	 * </p>
	 */
	public static final double	mu					= Constants.mass2mu(mass);
	/**
	 * Mean density of the entire sun (average)
	 * <p>
	 * <b>Unit: [kg/m<sup>3</sup>]</b>
	 * </p>
	 */
	public static final double	meanDensity			= 1.408E3;

	/**
	 * Mean solar radius
	 * <p>
	 * <b>Unit: [m]</b>
	 * </p>
	 */
	public static final double	radiusMean			= 6.955E8;
	/**
	 * Radius of the sun at the equator (great circle)
	 * <p>
	 * <b>Unit: [m]</b>
	 * </p>
	 */
	public static final double	radiusEquatorial	= 4.379E9;

	/**
	 * How the sun is "deformed" relative to a perfect sphere
	 * <p>
	 * flattening = (a - b) / (a) <br />
	 * with:
	 * <ul>
	 * <li>a: distance center to the equator</li>
	 * <li>b: distance center to the poles</li>
	 * </ul>
	 * </p>
	 * <p>
	 * <b>Unit: [-]</b>
	 * </p>
	 */
	public static final double	flattening			= 9E-6;
	/**
	 * Suns total surface area
	 * <p>
	 * <b>Unit: [m<sup>2</sup>]</b>
	 * </p>
	 */
	public static final double	surfaceArea			= 6.0877E18;
	/**
	 * Suns total volume
	 * <p>
	 * <b>Unit: [m<sup>3</sup>]</b>
	 * </p>
	 */
	public static final double	volume				= 1.412E27;

	/**
	 * Celestial body representation of the sun
	 */
	public static CelestialBody	body				= new CelestialBody(new CartesianElements(), mass);
}
