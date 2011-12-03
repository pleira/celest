/**
 * Copyright (C) 2011 simon <aodtorusan@gmail.com>
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
package be.angelcorp.libs.celest.stateVector;

import org.apache.commons.math.linear.RealVector;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.math.Keplerian;

/**
 * Holds the state variation using the variation of the classical Keplerian elements:
 * 
 * <pre>
 * Elements: { da,    de,      i,       &omega;,       &Omega;,       &nu;   }
 * Units:    {[m/s], [-/s], [rad/s], [rad/s], [rad/s], [rad/s]}
 * </pre>
 * 
 * @author Simon Billemont
 * 
 */
public interface IKeplerDerivative extends Keplerian, IStateDerivativeVector {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IKeplerDerivative clone();

	/**
	 * Tests if two sets of kepler derivartives hold the same variables (tests for equal
	 * da,de,di,d&omega;,d&Omega;,d&nu;)
	 * 
	 * @param state2
	 *            Kepler derivative elements to compare with
	 * @return True if the two sets contain the same orbital elements
	 */
	public abstract boolean equals(IKeplerDerivative state2);

	/**
	 * Tests if two sets of kepler derivative elements hold the same variables (tests for equal
	 * da,de,di,d&omega;,d&Omega;,d&nu;) with the given relative accuracy.
	 * 
	 * <pre>
	 * abs(element1 - element2) &lt; eps
	 * </pre>
	 * 
	 * @param state2
	 *            Kepler elements to compare with
	 * @param eps
	 *            Relative accuracy to test
	 * @return True if the two sets contain the same orbital elements
	 */
	public abstract boolean equals(IKeplerDerivative state2, double eps);

	/**
	 * @see KeplerElements#omega
	 */
	public abstract double getArgumentPeriapsisVariation();

	/**
	 * @see KeplerElements#centerbody
	 */
	public abstract CelestialBody getCenterbody();

	/**
	 * @see KeplerElements#e
	 */
	public abstract double getEccentricityVariation();

	/**
	 * @see KeplerElements#i
	 */
	public abstract double getInclinationVariation();

	/**
	 * @see KeplerElements#raan
	 */
	public abstract double getRaanVariation();

	/**
	 * @see KeplerElements#a
	 */
	public abstract double getSemiMajorAxisVariation();

	/**
	 * @see KeplerElements#trueA
	 */
	public abstract double getTrueAnomalyVariation();

	/**
	 * @see KeplerElements#omega
	 */
	public abstract void setArgumentPeriapsisVariation(double domega);

	/**
	 * @see KeplerElements#centerbody
	 */
	public abstract void setCenterbody(CelestialBody centerbody);

	/**
	 * @see KeplerElements#e
	 */
	public abstract void setEccentricityVariation(double de);

	/**
	 * Sets all the Kepler derivative elements at once
	 * 
	 * @param da
	 *            Semi-major axis variation {@link KeplerElements#a} [m]
	 * @param de
	 *            Eccentricity variation {@link KeplerElements#e}[-]
	 * @param di
	 *            Inclination variation {@link KeplerElements#i} [rad]
	 * @param domega
	 *            Argument of pericenter variation {@link KeplerElements#omega} [rad]
	 * @param draan
	 *            Right acsention of the ascending node variation {@link KeplerElements#raan} [rad]
	 * @param dtrueA
	 *            True anomaly variation {@link KeplerElements#trueA} [rad]
	 */
	public abstract void setElements(double da, double de, double di, double domega, double draan,
			double dtrueA);

	/**
	 * @see KeplerElements#i
	 */
	public abstract void setInclinationVariation(double di);

	/**
	 * @see KeplerElements#raan
	 */
	public abstract void setRaanVariation(double draan);

	/**
	 * @see KeplerElements#a
	 */
	public abstract void setSemiMajorAxisVariation(double da);

	/**
	 * @see KeplerElements#trueA
	 */
	public abstract void setTrueAnomalyVariation(double dtrueA);

	/**
	 * {@inheritDoc}
	 * <p>
	 * [ da, de, di, domega, draan, dtrueA ]
	 * </p>
	 */
	@Override
	public abstract RealVector toVector();

}