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

import org.apache.commons.math.FunctionEvaluationException;

import be.angelcorp.libs.celest.stateVector.StateVector;
import be.angelcorp.libs.celest.trajectory.Trajectory;
import be.angelcorp.libs.math.functions.ExponentialSinusoid;

public class ExpoSinTrajectory extends Trajectory {

	private ExponentialSinusoid	exposin;
	private double				gamma;

	public ExpoSinTrajectory(double k0, double k1, double k2, double phi, double gamma) {
		this(new ExponentialSinusoid(k0, k1, k2, 0, phi), gamma);
	}

	public ExpoSinTrajectory(ExponentialSinusoid exposin, double gamma) {
		this.exposin = exposin;
		this.gamma = gamma;
	}

	@Override
	public StateVector evaluate(double t) throws FunctionEvaluationException {
		// TODO Auto-generated method stub
		// return null;
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public ExponentialSinusoid getExposin() {
		return exposin;
	}

	public double getGamma() {
		return gamma;
	}

}
