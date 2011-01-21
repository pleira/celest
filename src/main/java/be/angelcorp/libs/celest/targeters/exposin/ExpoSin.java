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
package be.angelcorp.libs.celest.targeters.exposin;

import org.apache.commons.math.MathException;

import be.angelcorp.libs.celest.constants.SolarConstants;
import be.angelcorp.libs.celest.stateVector.StateVector;
import be.angelcorp.libs.celest.targeters.TPBVP;
import be.angelcorp.libs.math.linear.Vector3D;

import com.lyndir.lhunath.lib.system.logging.Logger;

/**
 * 
 * Exponential sinusoid solution (low thrust solution) to the Lambert problem ( a two-point boundary
 * value problem, TPBVP). It assumes a constant low thrust by the spacecraft in the direction of the
 * instantaneous velocity vector.
 * 
 * @author simon
 * 
 */
public class ExpoSin extends TPBVP {

	private static final Logger	logger		= Logger.get(ExpoSin.class);

	/**
	 * Standard gravitational parameter of the center body (body with the strongest influence on the
	 * satellite). Default is the standard gravitational parameter of the sun.
	 * <p>
	 * <b>Unit: [m<sup>3</sup>/s<sup>2</sup>]</b>
	 * </p>
	 */
	private double				mu_center	= SolarConstants.mu;
	/**
	 * Amount of rotations to perform around the center body in order to arrive at r2.
	 * <p>
	 * <b>Unit: [-]</b>
	 * </p>
	 */
	private int					N			= 0;
	/**
	 * Exposin parameter k2 (Winding parameter). This must be assumed. Set using:
	 * <ul>
	 * <li>{@link ExpoSin#assumeK2()} Set the k2 using this.N as guide</li>
	 * <li>{@link ExpoSin#assumeK2(int)} Set the k2 using a given amount of revolutions as guide</li>
	 * <li>{@link ExpoSin#assumeK2(double)} Set the k2 directly</li>
	 * </ul>
	 * <p>
	 * <b>Unit: [-]</b>
	 * </p>
	 */
	private double				assumeK2	= 1. / 12;

	/**
	 * Exposin problem with the given boundary conditions
	 * 
	 * @param r1
	 *            StateVector that defines the starting position [m]
	 * @param r2
	 *            StateVector that defines the wanted end position [m]
	 * @param dT
	 *            Time between the start position and end position [s]
	 */
	public ExpoSin(StateVector r1, StateVector r2, double dT) {
		super(r1, r2, dT);
	}

	/**
	 * Set the k2 parameter (winding parameter) used in the exposin solution directly.
	 * 
	 * @param assumeK2
	 *            New value for k2 [-]
	 */
	public void assumeK2(double assumeK2) {
		this.assumeK2 = assumeK2;
	}

	/**
	 * Set the k2 parameter (winding parameter) used in the exposin solution by inferring it from the set
	 * amount of revolutions around the center body.
	 */
	public void assumeK2FromN() {
		assumeK2FromN(N);
	}

	/**
	 * Set the k2 parameter (winding parameter) used in the exposin solution by inferring it from the
	 * given amount of revolutions around the center body.
	 * 
	 * @param N
	 *            Revolutions to assume [N]
	 */
	public void assumeK2FromN(int N) {
		this.assumeK2 = 1 / (2 * N);
	}

	/**
	 * Get all the possible exposin solutions for reaching r2 from r1 (without time constraignt). It is a
	 * function of a generic parameter gamma.
	 * 
	 * @return All possible exposin solutions
	 */
	public ExpoSinSolutionSet getSolutionSet() {
		Vector3D r1vec = this.r1.toCartesianElements().R;
		Vector3D r2vec = this.r2.toCartesianElements().R;
		double r1 = r1vec.getNorm();
		double r2 = r2vec.getNorm();

		double psi = Math.acos(r1vec.dot(r2vec) / (r1 * r2));
		double theta = psi + 2 * Math.PI * N;

		return new ExpoSinSolutionSet(r1, r2, dT, assumeK2, theta, mu_center);
	}

	@Override
	public ExpoSinTrajectory getTrajectory() throws MathException {
		ExpoSinSolutionSet solutions = getSolutionSet();
		double gammaOptimal = solutions.getOptimalSolution();
		logger.dbg("Gamma optimal: %f", gammaOptimal);

		return new ExpoSinTrajectory(solutions.getExpoSin(gammaOptimal));
	}

	/**
	 * Set the standard gravitational parameter of the center body (body with the stronges gravitational
	 * force)
	 * 
	 * @param mu
	 *            Standard gravitational parameter [m^3/s^2]
	 */
	public void setMu(double mu) {
		this.mu_center = mu;
	}

	/**
	 * Set the amount of revolutions that are to be made around the center body (default:0)
	 * 
	 * @param n
	 *            Nr of revolutions
	 */
	public void setN(int n) {
		N = n;
	}
}
