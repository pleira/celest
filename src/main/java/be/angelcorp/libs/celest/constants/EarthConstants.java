/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *        http://www.opensource.org/licenses/NOSL3.0
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
import be.angelcorp.libs.celest.stateVector.KeplerElements;
import be.angelcorp.libs.celest.stateVector.NonSignuarElements;
import be.angelcorp.libs.celest.stateVector.NonSingularDerivative;
import be.angelcorp.libs.celest.time.JulianDate;
import be.angelcorp.libs.celest.trajectory.KeplerVariationTrajectory;
import be.angelcorp.libs.util.physics.Angle;
import be.angelcorp.libs.util.physics.Length;
import be.angelcorp.libs.util.physics.Time;

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
	public static final double				mass				= 5.9736E24;
	/**
	 * Standard gravitational parameter of the earth
	 * <p>
	 * <b>Unit: [m<sup>3</sup>/s<sup>2</sup>]</b>
	 * </p>
	 */
	public static final double				mu					= Constants.mass2mu(mass);
	/**
	 * Mean density of the entire earth (average)
	 * <p>
	 * <b>Unit: [kg/m<sup>3</sup>]</b>
	 * </p>
	 */
	public static final double				meanDensity			= 5.515E3;								// kg
																										// per
																										// m^3

	/**
	 * Mean earth radius
	 * <p>
	 * <b>Unit: [m]</b>
	 * </p>
	 */
	public static final double				radiusMean			= 6371E3;								// m
	/**
	 * Radius of the earth at the equator (great circle)
	 * <p>
	 * <b>Unit: [m]</b>
	 * </p>
	 */
	public static final double				radiusEquatorial	= 6378.1E3;							// m
	/**
	 * Radius of the earth at the poles (great circle)
	 * <p>
	 * <b>Unit: [m]</b>
	 * </p>
	 */
	public static final double				radiusPolar			= 6356.8E3;							// m

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
	public static final double				flattening			= 0.0033528;
	/**
	 * Circumference of the earth (not confirmed mean circumference)
	 * <p>
	 * <b>Unit: [m]</b>
	 * </p>
	 */
	public static final double				circumference		= 40075.16E3;
	/**
	 * Earth total surface area
	 * <p>
	 * <b>Unit: [m<sup>2</sup>]</b>
	 * </p>
	 */
	public static final double				surfaceArea			= 510072000E6;
	/**
	 * Earth total volume
	 * <p>
	 * <b>Unit: [m<sup>3</sup>]</b>
	 * </p>
	 */
	public static final double				volume				= (1.08321E9 * 10E12);

	/**
	 * Semi-major axis (a) of the orbit of the earth around the sun
	 * <p>
	 * <b>Unit: [m]</b>
	 * </p>
	 */
	public static final double				semiMajorAxis		= 149.598261E9;
	/**
	 * Eccentricity (e) of the orbit of the earth around the sun
	 * <p>
	 * <b>Unit: [-]</b>
	 * </p>
	 */
	public static final double				eccentricity		= 0.01671123;
	/**
	 * Inclination (i) of the orbit of the earth around the sun
	 * <p>
	 * <b>Unit: [rad]</b>
	 * </p>
	 */
	public static final double				inclination			= 0.124878;
	/**
	 * Argument of perigee (&omega;) of the orbit of the earth around the sun
	 * <p>
	 * <b>Unit: [rad]</b>
	 * </p>
	 */
	public static final double				argPerigee			= 1.99330267;
	/**
	 * Right ascension of the ascending node (&Omega;) of the orbit of the earth around the sun
	 * <p>
	 * <b>Unit: [rad]</b>
	 * </p>
	 */
	public static final double				raan				= 6.08665;

	/**
	 * Kepler state vector of the earth, around the sub
	 */
	public static final KeplerElements		solarOrbit;
	static {
		solarOrbit = new KeplerElements(semiMajorAxis, eccentricity, inclination,
				argPerigee, raan, 0, SolarConstants.body);
	}
	/**
	 * Celestial body representation of the earth around the sun
	 */
	public static CelestialBody				body				= new CelestialBody(solarOrbit, mass);
	/**
	 * Celestial body representation of the earth in the center
	 */
	public static CelestialBody				bodyCenter			= new CelestialBody(
																		new CartesianElements(), mass);

	public static KeplerVariationTrajectory	orbit;
	static {
		// Based on: http://ssd.jpl.nasa.gov/?planet_pos
		CelestialBody jpl_sun = new CelestialBody();
		jpl_sun.setMass(Constants.mu2mass(1.32712440018E20));

		double a0 = Length.convert(1.00000261, Length.AU);
		double e0 = 0.01671123;
		double i0 = Angle.convert(-0.00001531, Angle.DEG);
		double w_bar0 = Angle.convert(102.93768193, Angle.DEG);
		double W0 = 0;
		double L0 = Angle.convert(100.46457166, Angle.DEG);
		NonSignuarElements keplerAtJ2000 = new NonSignuarElements(a0, e0, i0, w_bar0, W0, L0, jpl_sun);

		double centuryToS = Time.convert(100, Time.year);
		double da0 = Length.convert(0.00000562, Length.AU) / centuryToS;
		double de0 = -0.00004392 / centuryToS;
		double di0 = Angle.convert(-0.01294668, Angle.DEG) / centuryToS;
		double dw_bar0 = Angle.convert(0.32327364, Angle.DEG) / centuryToS;
		double dW0 = 0;
		double dL0 = Angle.convert(35999.37244981, Angle.DEG) / centuryToS;
		NonSingularDerivative keplerVariation = new NonSingularDerivative(da0, de0, di0, dw_bar0, dW0, dL0);

		orbit = new KeplerVariationTrajectory(JulianDate.getJ2000(), keplerAtJ2000, keplerVariation);
	}
}
