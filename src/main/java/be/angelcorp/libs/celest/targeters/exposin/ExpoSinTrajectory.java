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

import org.apache.commons.math.FunctionEvaluationException;

import be.angelcorp.libs.celest.stateVector.StateVector;
import be.angelcorp.libs.celest.trajectory.Trajectory;
import be.angelcorp.libs.math.functions.ExponentialSinusoid;

public class ExpoSinTrajectory extends Trajectory {

	private ExponentialSinusoid	exposin;

	public ExpoSinTrajectory(double k0, double k1, double k2, double phi) {
		this(new ExponentialSinusoid(k0, k1, k2, 0, phi));
	}

	public ExpoSinTrajectory(ExponentialSinusoid exposin) {
		this.exposin = exposin;
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

}
