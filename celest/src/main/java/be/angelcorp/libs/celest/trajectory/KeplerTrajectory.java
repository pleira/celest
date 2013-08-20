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
package be.angelcorp.libs.celest.trajectory;

import be.angelcorp.libs.celest.kepler.package$;
import be.angelcorp.libs.celest.state.Keplerian;
import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.util.physics.Time;

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
	private final Keplerian k;
	private final IJulianDate epoch;

	/**
	 * Create a trajectory based on propagated classical (Kepler) elements.
	 * 
	 * @param k
	 *            Initial Kepler elements at time 0.
	 */
	public KeplerTrajectory(Keplerian k, IJulianDate epoch) {
		this.k = k;
		this.epoch = epoch;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Keplerian evaluate(IJulianDate t) {
        double n = k.quantities().meanMotion();
		double dt = t.relativeTo(epoch, Time.second); // Delta between epoch and now in [s]
		double dM = n * dt;
		return new Keplerian(k.a(), k.e(), k.i(), k.ω(), k.Ω(), k.meanAnomaly() + dM, k.frame());
	}

}
