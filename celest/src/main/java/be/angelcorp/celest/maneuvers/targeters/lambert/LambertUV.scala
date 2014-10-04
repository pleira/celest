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
package be.angelcorp.celest.maneuvers.targeters.lambert

import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem
import be.angelcorp.celest.maneuvers.targeters.TPBVP
import be.angelcorp.celest.state.PosVel
import be.angelcorp.celest.time.Epoch
import org.apache.commons.math3.analysis.{FunctionUtils, UnivariateFunction}
import org.apache.commons.math3.analysis.function.{Add, Constant}
import org.apache.commons.math3.analysis.solvers.BrentSolver
import org.apache.commons.math3.util.Precision

import scala.math._


/**
 * Solve the Lambert problem ( {@link TPBVP}, using the Universal Variables technique. Based on the
 * algorithms provided by Bate, Mueller, & White (Fundamental of Astrodynamics), and Vallado (Fundamental
 * of Astrodynamics and Applications)
 *
 * @param r1              Starting point R1.
 * @param r2              Arrival point R2.
 * @param frame           Frame in which the Keplerian motion takes place.
 * @param departureEpoch  Epoch of departure (epoch at r1).
 * @param arrivalEpoch    Epoch of arrival (epoch at r2).
 * @param shortWay        Indicates if the direction of motion is the small angle between r1 and r2, or the large angle between r1 and r2.
 *
 * @author Simon Billemont
 */
class LambertUV[F <: BodyCenteredSystem]
(val r1: PosVel[F], val r2: PosVel[F], val frame: F, val departureEpoch: Epoch, val arrivalEpoch: Epoch, val shortWay: Boolean) extends TPBVP[F] {

  class LambertFunctionUV(A: Double, r1norm: Double, r2norm: Double) extends UnivariateFunction {

    def computeY(z: Double): Double = {
      var C = 0.0
      var S = 0.0
      if (z > Precision.EPSILON) {
        C = (1d - cos(sqrt(z))) / z
        S = (sqrt(z) - sin(sqrt(z))) / pow(z, 3d / 2d)
      } else if (z < -Precision.EPSILON) {
        C = (1d - cosh(sqrt(-z))) / z
        S = (sinh(sqrt(-z)) - sqrt(-z)) / pow(-z, 3d / 2d)
      } else {
        C = 1d / 2d
        S = 1d / 6d
      }

      computeY(z, C, S)
    }

    def computeY(z: Double, C: Double, S: Double): Double =
      r1norm + r2norm + A * (z * S - 1d) / sqrt(C)

    override def value(z: Double) = {
      var C = 0.0
      var S = 0.0
      if (z > Precision.EPSILON) {
        C = (1d - cos(sqrt(z))) / z
        S = (sqrt(z) - sin(sqrt(z))) / pow(z, 3d / 2d)
      } else if (z < -Precision.EPSILON) {
        C = (1d - cosh(sqrt(-z))) / z
        S = (sinh(sqrt(-z)) - sqrt(-z)) / pow(-z, 3d / 2d)
      } else {
        C = 1d / 2d
        S = 1d / 6d
      }

      val y = computeY(z, C, S)

      val x = sqrt(y / C)
      (pow(x, 3) * S + A * sqrt(y)) / frame.centerBody.μ
    }
  }

  /** {@inheritDoc} */
  override def trajectory = {
    val r1 = this.r1.position
    val r2 = this.r2.position
    val r1norm = r1.norm
    val r2norm = r2.norm

    val cosDnu = r1.dot(r2) / (r1norm * r2norm)
    var A = sqrt(r1norm * r2norm * (1d + cosDnu))

    if (abs(A) < Precision.EPSILON)
      throw new ArithmeticException("Cannot compute Lambert solution for a problem where " +
        "A = sqrt(r1norm * r2norm * (1 + cos(Dnu) )) == 0");

    if (!shortWay)
      A = -A;

    val func = new LambertFunctionUV(A, r1norm, r2norm)
    val solver = new BrentSolver(1e-12)
    val z = solver.solve(100, FunctionUtils.combine(new Add(), new Constant(-travelTime), func), -4d * Pi, 5.0 * Pi * Pi, 0)

    val y = func.computeY(z)
    val f = 1d - y / r1norm
    val g = A * sqrt(y / frame.centerBody.μ)
    val g_dot = 1d - y / r2norm

    // Compute the initial velocities at beginning and end points
    // Vector3D v1 = r2.subtract(r1.multiply(f)).divide(g);
    // Vector3D v2 = r2.multiply(g_dot).subtract(r1).divide(g);
    new LambertTrajectory(r1, r2, frame, departureEpoch, arrivalEpoch, f, g, g_dot)
  }
}
