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

import be.angelcorp.celest.math.functions.ExponentialSinusoid
import be.angelcorp.celest.math.geometry.{Mat3, Vec3}

import math._
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem
import org.apache.commons.math3.analysis.{FunctionUtils, UnivariateFunction}
import org.apache.commons.math3.analysis.integration.LegendreGaussIntegrator
import org.apache.commons.math3.analysis.function.Inverse
import org.apache.commons.math3.analysis.solvers.RiddersSolver
import be.angelcorp.celest.state.PosVel
import be.angelcorp.celest.trajectory.Trajectory

/**
 * Creates a trajectory from a known [[be.angelcorp.celest.maneuvers.targeters.exposin.ExpoSin]] solution to the
 * [[be.angelcorp.celest.maneuvers.targeters.TPBVP]]. This allows you to perform all common
 * [[be.angelcorp.celest.trajectory.Trajectory]] operations of that `ExpoSin` solution.
 *
 * @param exposin Exposin describing the orbit.
 * @param gamma   Value of the flight path angle at departure for this specific solution.
 * @param frame   Reference system in which the trajectory takes place.
 * @param epoch   Epoch at which the transfer starts.
 *
 * @author Simon Billemont
 */
class ExpoSinTrajectory[F <: BodyCenteredSystem](val exposin: ExponentialSinusoid, val frame: F, val epoch: Epoch, val gamma: Double = Double.NaN) extends Trajectory[F] {

  /**
   * Returns the planar postion in the trajectory at the given time. The position at departure is
   * &lt;r1, 0, 0&gt;, and moves around in the XY plane over time.
   */
  def apply(evalEpoch: Epoch) = {
    // Travel time from the start position [s]
    val t = evalEpoch.relativeToS(epoch)

    // Equation of d(theta)/dt
    val thetaDot = new ExpoSinAngularRate(exposin, frame.centerBody.μ)

    // Find the angle (theta) of the satellite at the time t
    val theta = if (math.abs(t) > 1E-16) {
      // Equation of dt/d(theta) - t
      val rootFunction = new UnivariateFunction() {
        val integrator = new LegendreGaussIntegrator(3, 1e-8, 1e-8)

        override def value(theta: Double) = {
          val tof = integrator.integrate(102400, FunctionUtils.compose(new Inverse(), thetaDot), 0, theta)
          tof - t
        }
      }
      val est = thetaDot.value(0) * t

      // Find the point where the tof(theta) == t
      new RiddersSolver().solve(64, rootFunction, 1E-6, 2 * est, est)
    } else 0

    // Find the radius for the current theta
    val c = cos(exposin.getK2 * theta + exposin.getPhi)
    val theta_dot = thetaDot.value(theta)
    val r = exposin.value(theta)
    val r_dot = theta_dot * (exposin.getQ0 + exposin.getK1 * exposin.getK2 * c) * r

    // Compose the radial vector
    val R_norm = Vec3(Math.cos(theta), Math.sin(theta), 0)
    val R = R_norm * r

    // Compute the scale of the velocity components
    val V_r = r_dot
    val V_theta = r * theta_dot
    val V_theta_norm = Vec3.z cross R_norm

    // TODO: make inherit this rotation from the ExpoSin input states
    val rotation = Mat3.identity()
    // Compose the velocity vector from radial and tangential velocities
    val V = rotation * (R.normalized * V_r) + (V_theta_norm * V_theta)

    // Convert to the position in cartesian elements (in plane coordinates)
    new PosVel(R, V, frame)
  }

}
