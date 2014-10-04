/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
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
package be.angelcorp.celest.maneuvers.targeters.exposin

import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem
import be.angelcorp.celest.maneuvers.targeters.TPBVP
import be.angelcorp.celest.state.PosVel
import be.angelcorp.celest.time.Epoch
import org.slf4j.LoggerFactory

import scala.math._

/**
 * Exponential sinusoid solution (low thrust solution) to the Lambert problem ( a two-point boundary
 * value problem, TPBVP). It assumes a constant low thrust by the spacecraft in the direction of the
 * instantaneous velocity vector.
 *
 * @param r1             StateVector that defines the starting position [m]
 * @param r2             StateVector that defines the wanted end position [m]
 * @param departureEpoch Epoch of departure (epoch at r1) [jd]
 * @param arrivalEpoch   Epoch of arrival (epoch at r2) [jd]
 * @param frame          Frame in which the motion takes place.
 *
 * @author Simon Billemont
 */
class ExpoSin[F <: BodyCenteredSystem](val r1: PosVel[F], val r2: PosVel[F], val departureEpoch: Epoch, val arrivalEpoch: Epoch, val frame: F) extends TPBVP[F] {
  val logger = LoggerFactory.getLogger(getClass)

  /**
   * Amount of rotations to perform around the center body in order to arrive at r2.
   * <p>
   * <b>Unit: [-]</b>
   * </p>
   */
  var N = 0

  /**
   * Exposin parameter k2 (Winding parameter). This must be assumed. Set using:
   * <ul>
   * <li>{@link ExpoSin#assumeK2(double)} Set the k2 directly</li>
   * <li>{@link ExpoSin#assumeK2FromN(int)} Set the k2 using a given amount of revolutions as guide</li>
   * </ul>
   * <p>
   * <b>Unit: [-]</b>
   * </p>
   */
  var assumeK2 = 1.0 / 12.0

  /**
   * Set the k2 parameter (winding parameter) used in the exposin solution by inferring it from
   * the set amount of revolutions around the center body.
   */
  def assumeK2FromN() {
    assumeK2FromN(N)
  }

  /**
   * Set the k2 parameter (winding parameter) used in the exposin solution by inferring it from
   * the given amount of revolutions around the center body.
   *
   * @param N Revolutions to assume [N]
   */
  def assumeK2FromN(N: Int) {
    assumeK2 = 1./ (2 * N)
  }

  /**
   * Get all the possible exposin solutions for reaching r2 from r1 (without time constraignt). It
   * is a function of a generic parameter gamma.
   *
   * @return All possible exposin solutions
   */
  def solutionSet = {
    val r1vec = this.r1.toPosVel.position
    val r2vec = this.r2.toPosVel.position
    val r1 = r1vec.norm
    val r2 = r2vec.norm

    val psi = acos(r1vec.dot(r2vec) / (r1 * r2));
    val theta = psi + 2 * Pi * N

    new ExpoSinSolutionSet(r1, r2, assumeK2, theta, frame.centerBody.μ)
  }

  override def trajectory = {
    // Create a set of exposin solutions leading from r1 to r2
    val solutions = solutionSet
    // Find the curve for which the tof is equal the the required tof
    val gammaOptimal = solutions.getOptimalSolution(travelTime)
    logger.debug("Gamma optimal: {}", gammaOptimal)

    // Create a trajectory from the found solution
    val solution = solutions.getExpoSin(gammaOptimal)
    new ExpoSinTrajectory(solution, frame, departureEpoch, gammaOptimal)
  }

}
