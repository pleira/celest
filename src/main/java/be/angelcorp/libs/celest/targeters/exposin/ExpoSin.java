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
package be.angelcorp.libs.celest.targeters.exposin;

import org.apache.commons.math.MathException;

import be.angelcorp.libs.celest.constants.SolarConstants;
import be.angelcorp.libs.celest.stateVector.StateVector;
import be.angelcorp.libs.celest.targeters.TPBVP;
import be.angelcorp.libs.math.linear.Vector3D;

import com.lyndir.lhunath.lib.system.logging.Logger;

/**
 * 
 * @author simon
 * 
 */
public class ExpoSin extends TPBVP {

	private double				mu_center	= SolarConstants.mu;
	private ExpoSinTrajectory	trajectory;

	private int					N			= 0;
	private double				assumeK2	= 1. / 12;
	private boolean				clockwise	= true;
	private static final Logger	logger		= Logger.get(ExpoSin.class);

	public ExpoSin(StateVector r1, StateVector r2, double dT) {
		super(r1, r2, dT);
	}

	public void assumeK2(double assumeK2) {
		this.assumeK2 = assumeK2;
	}

	public void assumeK2FromN() {
		assumeK2FromN(N);
	}

	public void assumeK2FromN(int N) {
		this.assumeK2 = 1 / (2 * N);
	}

	public void assumeN(int n) {
		N = n;
	}

	public ExpoSinSolutionSet getSolutionSet() {
		Vector3D r1vec = this.r1.toCartesianElements().R;
		Vector3D r2vec = this.r2.toCartesianElements().R;
		final double r1 = r1vec.getNorm();
		final double r2 = r2vec.getNorm();

		double psi = Math.acos(r1vec.dot(r2vec) / (r1 * r2));
		if (!clockwise)
			psi = -psi + 2 * Math.PI;
		final double theta = psi + 2 * Math.PI * N;

		return new ExpoSinSolutionSet(r1, r2, dT, assumeK2, theta, mu_center);
	}

	@Override
	public ExpoSinTrajectory getTrajectory() throws MathException {
		ExpoSinSolutionSet solutions = getSolutionSet();
		double gammaOptimal = solutions.getOptimalSolution();
		logger.dbg("Gamma optimal: %f", gammaOptimal);

		trajectory = new ExpoSinTrajectory(solutions.getExpoSin(gammaOptimal));
		return trajectory;
	}

	public boolean isClockwise() {
		return clockwise;
	}

	public void setClockwise(boolean clockwise) {
		this.clockwise = clockwise;
	}

	public void setMu(double mu) {
		this.mu_center = mu;
	}
}
