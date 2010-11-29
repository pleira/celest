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

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.stateVector.KeplerElements;

/**
 * Different constants specific to the earth. Has constants in the following categories:
 * <ul>
 * <li>Orbital parameters</li>
 * <li>Physical constants</li>
 * <li>Geometric constants</li>
 * </ul>
 * 
 * @author Simon Billemont, TUDelft, Faculty Aerospace Engineering (aodtorusan@gmail.com or
 *         s.billemont@student.tudelft.nl)
 */
public abstract class EarthConstants {

	/**
	 * Mass of the earth
	 * <p>
	 * <b>Unit: [kg]</b>
	 * </p>
	 */
	public static final double			mass				= 5.9736E24;
	/**
	 * Standard gravitational parameter of the earth
	 * <p>
	 * <b>Unit: [m<sup>3</sup>/s<sup>2</sup>]</b>
	 * </p>
	 */
	public static final double			mu					= Constants.mass2mu(mass);
	/**
	 * Mean density of the entire earth (average)
	 * <p>
	 * <b>Unit: [kg/m<sup>3</sup>]</b>
	 * </p>
	 */
	public static final double			meanDensity			= 5.515E3;								// kg
																									// per
																									// m^3

	/**
	 * Mean earth radius
	 * <p>
	 * <b>Unit: [m]</b>
	 * </p>
	 */
	public static final double			radiusMean			= 6371E3;								// m
	/**
	 * Radius of the earth at the equator (great circle)
	 * <p>
	 * <b>Unit: [m]</b>
	 * </p>
	 */
	public static final double			radiusEquatorial	= 6378.1E3;							// m
	/**
	 * Radius of the earth at the poles (great circle)
	 * <p>
	 * <b>Unit: [m]</b>
	 * </p>
	 */
	public static final double			radiusPolar			= 6356.8E3;							// m

	/**
	 * How the earth is "deformed" relative to a perfect sphere
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
	public static final double			flattening			= 0.0033528;
	/**
	 * Circumference of the earth (not confirmed mean circumference)
	 * <p>
	 * <b>Unit: [m]</b>
	 * </p>
	 */
	public static final double			circumference		= 40075.16E3;
	/**
	 * Earth total surface area
	 * <p>
	 * <b>Unit: [m<sup>2</sup>]</b>
	 * </p>
	 */
	public static final double			surfaceArea			= 510072000E6;
	/**
	 * Earth total volume
	 * <p>
	 * <b>Unit: [m<sup>3</sup>]</b>
	 * </p>
	 */
	public static final double			volume				= (1.08321E9 * 10E12);

	/**
	 * Semi-major axis (a) of the orbit of the earth around the sun
	 * <p>
	 * <b>Unit: [m]</b>
	 * </p>
	 */
	public static final double			semiMajorAxis		= 149.598261E9;
	/**
	 * Eccentricity (e) of the orbit of the earth around the sun
	 * <p>
	 * <b>Unit: [-]</b>
	 * </p>
	 */
	public static final double			eccentricity		= 0.01671123;
	/**
	 * Inclination (i) of the orbit of the earth around the sun
	 * <p>
	 * <b>Unit: [rad]</b>
	 * </p>
	 */
	public static final double			inclination			= 0.124878;
	/**
	 * Argument of perigee (&omega;) of the orbit of the earth around the sun
	 * <p>
	 * <b>Unit: [rad]</b>
	 * </p>
	 */
	public static final double			argPerigee			= 1.99330267;
	/**
	 * Right ascension of the ascending node (&Omega;) of the orbit of the earth around the sun
	 * <p>
	 * <b>Unit: [rad]</b>
	 * </p>
	 */
	public static final double			raan				= 6.08665;

	/**
	 * Kepler state vector of the earth, around the sub
	 */
	public static final KeplerElements	solarOrbit;
	static {
		solarOrbit = new KeplerElements(semiMajorAxis, eccentricity, inclination,
				argPerigee, raan, 0);
	}
	/**
	 * Celestial body representation of the earth
	 */
	public static CelestialBody			body				= new CelestialBody(solarOrbit, mass);
}
