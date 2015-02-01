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
import be.angelcorp.celest.state.NonSingular
import be.angelcorp.celest.state.positionState.NonSingularDerivative
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem

/**
 * A trajectory that computes the instantaneous [[be.angelcorp.celest.state.Keplerian]] elements based
 * based on an initial set of Kepler elements and there derivatives.
 *
 * Compared to [[be.angelcorp.celest.trajectory.KeplerTrajectory]], this trajectory uses
 * varying Kepler elements whereas KeplerTrajectory only propagates a pure Keplerian conic
 * section orbit (2 body motion).
 *
 * @author Simon Billemont
 */
class KeplerVariationTrajectory[F <: BodyCenteredSystem]
(val referenceEpoch: Epoch,
 val referenceElements: NonSingular[F],
 val referenceDerivatives: NonSingularDerivative) extends Trajectory[F] {

  def apply(epoch: Epoch) = {
    val dT = epoch.relativeToS(referenceEpoch)

    val a_new = referenceElements.semiMajorAxis + dT * referenceDerivatives.getSemiMajorAxisVariation
    val e_new = referenceElements.eccentricity + dT * referenceDerivatives.getEccentricityVariation
    val i_new = referenceElements.inclination + dT * referenceDerivatives.getInclinationVariation
    val w_true_new = referenceElements.longitudePerihelion + dT * referenceDerivatives.getLongitudePerihelionVariation
    val W_new = referenceElements.rightAscension + dT * referenceDerivatives.getRaanVariation
    val lambda_M_new = referenceElements.meanLongitude + dT * referenceDerivatives.getMeanLongitudeVariation

    new NonSingular(a_new, e_new, i_new, w_true_new, W_new, lambda_M_new, referenceElements.frame)
  }
}
