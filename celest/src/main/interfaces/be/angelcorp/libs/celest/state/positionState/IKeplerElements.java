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
package be.angelcorp.libs.celest.state.positionState;

import be.angelcorp.libs.celest.kepler.KeplerOrbitType;
import org.apache.commons.math3.linear.RealVector;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.kepler.KeplerEquations;
import be.angelcorp.libs.celest.math.Keplerian;

/**
 * Holds the state using the classical Keplerian elements:
 * 
 * <pre>
 * Elements: { a,   e,    i,     &omega;,     &Omega;,     &nu;  }
 * Units:    {[m], [-], [rad], [rad], [rad], [rad]}
 * </pre>
 * 
 * @author Simon Billemont
 * 
 */
public interface IKeplerElements extends Keplerian, IPositionState {

	/**
	 * Tests if two sets of kepler elements hold the same variables (tests for equal
	 * a,e,i,&omega;,&Omega;,&nu;)
	 * 
	 * @param state2
	 *            Kepler elements to compare with
	 * @return True if the two sets contain the same orbital elements
	 */
	public abstract boolean equals(IKeplerElements state2);

	/**
	 * Tests if two sets of kepler elements hold the same variables (tests for equal
	 * a,e,i,&omega;,&Omega;,&nu;) with the given relative accuracy.
	 * 
	 * <pre>
	 * abs(element1 - element2) &lt; element1 * eps
	 * </pre>
	 * 
	 * @param state2
	 *            Kepler elements to compare with
	 * @param eps
	 *            Relative accuracy to test
	 * @return True if the two sets contain the same orbital elements
	 */
	public abstract boolean equals(IKeplerElements state2, double eps);

	/**
	 * @see KeplerElements#omega
	 */
	public abstract double getArgumentPeriapsis();

	/**
	 * @see KeplerElements#centerbody
	 */
	public abstract CelestialBody getCenterbody();

	/**
	 * @see KeplerElements#e
	 */
	public abstract double getEccentricity();

	/**
	 * @see KeplerElements#i
	 */
	public abstract double getInclination();

	/**
	 * Get the specific set of Keplerian equations that can be used with this set of Kepler elements
	 * <p>
	 * This means it returns KeplerCircular equations if the orbit is circular, KeplerEllipse if
	 * elliptical and so forth...
	 * </p>
	 * 
	 * @return A set of KeplerEquations to compute additional parameters with
	 */
	public abstract KeplerEquations getOrbitEqn();

	/**
	 * Get the type of orbit we are in (circular, elliptical, parabolic or hyperbolic)
	 */
	public abstract KeplerOrbitType getOrbitType();

	/**
	 * @see KeplerElements#raan
	 */
	public abstract double getRaan();

	/**
	 * @see KeplerElements#a
	 */
	public abstract double getSemiMajorAxis();

	/**
	 * @see KeplerElements#trueA
	 */
	public abstract double getTrueAnomaly();

	/**
	 * @see KeplerElements#omega
	 */
	@Deprecated
	public abstract void setArgumentPeriapsis(double omega);

	/**
	 * @see KeplerElements#centerbody
	 */
	@Deprecated
	public abstract void setCenterbody(CelestialBody centerbody);

	/**
	 * @see KeplerElements#e
	 */
	@Deprecated
	public abstract void setEccentricity(double e);

	/**
	 * Sets all the Kepler elements at once
	 * 
	 * @param a
	 *            Semi-major axis {@link KeplerElements#a} [m]
	 * @param e
	 *            Eccentricity {@link KeplerElements#e}[-]
	 * @param i
	 *            Inclination {@link KeplerElements#i} [rad]
	 * @param omega
	 *            Argument of pericenter {@link KeplerElements#omega} [rad]
	 * @param raan
	 *            Right acsention of the ascending node {@link KeplerElements#raan} [rad]
	 * @param trueA
	 *            True anomaly {@link KeplerElements#trueA} [rad]
	 */
	@Deprecated
	public abstract void setElements(double a, double e, double i, double omega, double raan, double trueA);

	/**
	 * @see KeplerElements#i
	 */
	@Deprecated
	public abstract void setInclination(double i);

	/**
	 * @see KeplerElements#raan
	 */
	@Deprecated
	public abstract void setRaan(double raan);

	/**
	 * @see KeplerElements#a
	 */
	@Deprecated
	public abstract void setSemiMajorAxis(double a);

	/**
	 * @see KeplerElements#trueA
	 */
	@Deprecated
	public abstract void setTrueAnomaly(double trueA);

	/**
	 * {@inheritDoc}
	 * <p>
	 * [ a, e, i, omega, raan, trueA ]
	 * </p>
	 */
	@Override
	public abstract RealVector toVector();

}
