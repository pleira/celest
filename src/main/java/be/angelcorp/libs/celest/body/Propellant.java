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
package be.angelcorp.libs.celest.body;

public class Propellant {

	private static final double	G0	= 9.8;

	private double				isp;
	private double				propellantLeft;

	public Propellant(double isp) {
		this(isp, 0);
	}

	public Propellant(double isp, double propellantLeft) {
		this.isp = isp;
		this.propellantLeft = propellantLeft;
	}

	/**
	 * Compute the amount of propelant used in ideal conditions (no gravity losses, Tsiolkovsky)
	 * <p>
	 * For non ideal maneuvers,correct the dV term with a &Delta;dV, eg see:<br />
	 * J.Weiss, B. Metzger, M. Gallmeister, Orbit maneuvers with finite thrust, MBB/ERNO, ESA CR(P)-1910,
	 * 3 volumes, 1983
	 * </p>
	 * 
	 * @param body
	 * @param dV
	 */
	public void consumeDV(CelestialBody body, double dV) {
		// Tsiolkovsky:
		// m0/m1 = e ^ (Dv / Veff)
		double m = body.getMass();
		double dM = m - Math.exp(-dV / getVeff()) * m;
		consumeMass(dM);
	}

	public void consumeMass(double dM) {
		propellantLeft -= dM;
	}

	/**
	 * Compute the maximum dV that can be given with this engine (for an impulse maneuver, Tsiolkovsky)
	 * 
	 * @param body
	 *            Body that contains the rocket engine
	 * @return Maximum dV that can be given with the given amount of propellant
	 */
	public double getDvMax(CelestialBody body) {
		double m0 = body.getMass();
		double mProp = getPropellantMass();
		double dV = getVeff() * Math.log(m0 / (m0 - mProp));
		return dV;
	}

	public double getIsp() {
		return isp;
	}

	public double getPropellantMass() {
		return propellantLeft;
	}

	/**
	 * Get the effective exhaust velocity
	 * <p>
	 * Veff = Isp * G0;
	 * </p>
	 * 
	 * <p>
	 * <b>Unit: [m/s]</b>
	 * </p>
	 * 
	 * @return
	 */
	public double getVeff() {
		return isp * G0;
	}

	public void setPropellantMass(double propellant) {
		propellantLeft = propellant;
	}
}
