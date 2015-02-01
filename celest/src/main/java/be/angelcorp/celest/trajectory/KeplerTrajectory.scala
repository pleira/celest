/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.celest.trajectory

import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.state.Keplerian
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem

/**
 * Create a trajectory based on the ideal 2 body equations of motion as derived by Kepler.
 * The initial elements propagated using along the trajectory by modifying there mean anomaly.
 *
 * @param referenceEpoch    Epoch on which the reference elements are valid.
 * @param referenceElements Initial Kepler elements that are propagated.
 *
 * @author Simon Billemont
 * @see IKeplerTrajectory
 */
class KeplerTrajectory[F <: BodyCenteredSystem](referenceEpoch: Epoch, referenceElements: Keplerian[F]) extends Trajectory[F] {

  def apply(epoch: Epoch) = {
    val n = referenceElements.quantities.meanMotion
    val dt = epoch.relativeToS(referenceEpoch)
    // Delta between reference and now in [s]
    val dM = n * dt
    new Keplerian(
      referenceElements.a,
      referenceElements.e,
      referenceElements.i,
      referenceElements.ω,
      referenceElements.Ω,
      referenceElements.meanAnomaly + dM,
      referenceElements.frame
    )
  }

}
