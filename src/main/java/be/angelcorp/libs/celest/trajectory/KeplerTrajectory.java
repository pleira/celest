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
package be.angelcorp.libs.celest.trajectory;

import org.apache.commons.math.FunctionEvaluationException;

import be.angelcorp.libs.celest.kepler.KeplerEquations;
import be.angelcorp.libs.celest.stateVector.IKeplerElements;

/**
 * Implements a {@link IKeplerTrajectory}, a trajectory based on Kepler elements propagated using
 * classical Keplerian theory.
 * 
 * @author Simon Billemont
 * @see IKeplerTrajectory
 */
public class KeplerTrajectory implements IKeplerTrajectory {

	/**
	 * Initial Kepler elements that are propagated
	 */
	private final IKeplerElements	k;

	/**
	 * Create a trajectory based on propagated classical (Kepler) elements.
	 * 
	 * @param k
	 *            Initial Kepler elements at time 0.
	 */
	public KeplerTrajectory(IKeplerElements k) {
		this.k = k;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IKeplerElements evaluate(double t) throws FunctionEvaluationException {
		double n = KeplerEquations.meanMotion(k.getCenterbody().getMu(), k.getSemiMajorAxis());
		double dM = n * t;
		IKeplerElements k2 = k.clone();
		k2.setTrueAnomaly(k2.getOrbitEqn().trueAnomalyFromMean(k.getOrbitEqn().meanAnomaly() + dM));
		return k2;
	}

}
