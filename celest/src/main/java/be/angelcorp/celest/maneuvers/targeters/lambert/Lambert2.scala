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

package be.angelcorp.celest.maneuvers.targeters.lambert

import math._
import org.apache.commons.math3.util.FastMath
import be.angelcorp.celest.maneuvers.targeters.TPBVP
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.state.PosVel
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem

/**
 * This function solves the high-thrust Lambert problem. It does this using an algorithm developed by
 * Dr. D. Izzo from the European Space Agency [1].
 *
 * This targetter is faster then Lambert3, but might fail to find a solution in some cases (especially for higher N).
 * In such a case, use either Lambert3 or LambertRobust.
 *
 * For problems with N > 0, there are generally two solutions. By default, the left branch solution will be returned.
 *
 * Izzo, D. ESA Advanced Concepts team. Code used available in MGA.M,
 * [online] http://www.esa.int/gsp/ACT/inf/op/globopt.htm. Last retreived Nov, 2009.
 *
 * Based on:
 * Robust solver for Lambert's orbital-boundary value problem [matlab package] by Rody Oldenhuis
 *
 * @param r1 Initial position.
 * @param r2 Required target position.
 * @param departureEpoch Departure date.
 * @param arrivalEpoch Required arrival date.
 * @param frame Body centered frame in which the transfer motion takes place.
 * @param N Amount of complete revolutions around the center body to perform before arriving at the target destination.
 * @param prograde True if the transfer motion is prograde around the center body [default].
 * @param leftBranch  True returns the left branch solution [default].
 */
class Lambert2[F <: BodyCenteredSystem]
(val r1: PosVel[F], val r2: PosVel[F],
 val departureEpoch: Epoch, val arrivalEpoch: Epoch,
 val frame: F, val N: Double = 0,
 val prograde: Boolean = true, val leftBranch: Boolean = true) extends TPBVP[F] {

  val longWay = {
    val r1 = this.r1.position
    val r2 = this.r1.position
    val progradeIsLong = r1.x * r2.y - r2.x * r1.y < 0.0
    if (prograde) progradeIsLong else !progradeIsLong
  }

  // Allows mapping on tuples of two doubles
  implicit def t2mapper(t: (Double, Double)) = new {
    def map[R](f: Double => Double) = (f(t._1), f(t._2))
  }

  override lazy val trajectory = {
    val origin = this.r1.position
    val destination = this.r2.position

    // Undimentionalize all units;
    val r1 = origin.norm
    val r1vec = origin / r1
    val r2vec = destination / r1
    val V = sqrt(frame.centerBody.μ / r1)
    val T = r1 / V
    val tf = abs(arrivalEpoch.relativeToS(departureEpoch) / T)

    // relevant geometry parameters (non dimensional)
    val mr2vec = r2vec.norm

    val dth = if (longWay) 2 * Pi - origin.angle(destination) else origin.angle(destination)

    // derived quantities
    val c = sqrt(1 + mr2vec * mr2vec - 2 * mr2vec * cos(dth)) // non-dimensional chord
    val s = (1 + mr2vec + c) / 2 // non-dimensional semi-perimeter
    val a_min = s / 2 // minimum energy ellipse semi major axis
    val Lambda = sqrt(mr2vec) * cos(dth / 2) / s // lambda parameter (from BATTIN's book)
    val planeNormal = (r1vec cross r2vec).normalized // orbit plane unit vector

    // Initial values
    val logt = log(tf); // avoid re-computing the same value

    // single revolution (1 solution)
    val inn1 = if (N == 0) -0.5233 else if (leftBranch) +0.7234 else -0.5234
    val inn2 = if (N == 0) +0.5233 else if (leftBranch) +0.5234 else -0.2234
    var x1 = if (N == 0) log(1 + inn1) else tan(inn1 * Pi / 2)
    var x2 = if (N == 0) log(1 + inn2) else tan(inn2 * Pi / 2)

    val longwaySign = if (longWay) -1 else 1

    // since (inn1, inn2) < 0, initial estimate is always ellipse
    val xx = (inn1, inn2)
    val aa = xx map (x => a_min / (1 - x * x))
    val bbeta = aa map (a => longwaySign * 2 * asin(sqrt((s - c) / 2 / a)))
    val aalfa = xx map (x => 2 * acos(x))

    // evaluate the time of flight via Lagrange expression
    val y12 = (aa._1 * sqrt(aa._1) * ((aalfa._1 - sin(aalfa._1)) - (bbeta._1 - sin(bbeta._1)) + 2 * Pi * N),
      aa._2 * sqrt(aa._2) * ((aalfa._2 - sin(aalfa._2)) - (bbeta._2 - sin(bbeta._2)) + 2 * Pi * N))

    // initial estimates for y
    var (y1, y2) =
      if (N == 0)
        (log(y12._1) - logt, log(y12._2) - logt)
      else
        (y12._1 - tf, y12._2 - tf)

    // Solve for x

    // Newton-Raphson iterations
    // NOTE - the number of iterations will go to infinity in case m > 0  and there is no solution.
    var err = Double.PositiveInfinity
    var iterations = 0
    var xnew = 0.0
    val tol = 1E-12
    var bad = false
    while (err > tol && !bad) {
      iterations = iterations + 1

      xnew = (x1 * y2 - y1 * x2) / (y2 - y1)

      val x = if (N == 0) exp(xnew) - 1 else atan(xnew) * 2 / Pi
      val a = a_min / (1 - x * x)
      var alfa = 0.0
      var beta = 0.0

      if (x < 1) {
        // ellipse
        beta = longwaySign * 2 * asin(sqrt((s - c) / 2 / a))
        // make 100.4% sure it's in (-1 <= xx <= +1)
        alfa = 2 * acos(max(-1, min(1, x)))
      } else {
        // hyperbola
        alfa = 2 * FastMath.acosh(x)
        beta = longwaySign * 2 * FastMath.asinh(sqrt((s - c) / (-2 * a)))
      }
      // evaluate the time of flight via Lagrange expression
      val tof = if (a > 0)
        a * sqrt(a) * ((alfa - sin(alfa)) - (beta - sin(beta)) + 2 * Pi * N)
      else
        -a * sqrt(-a) * ((sinh(alfa) - alfa) - (sinh(beta) - beta))
      // new value of y
      val ynew = if (N == 0) log(tof) - logt else tof - tf
      // save previous and current values for the next iteration (prevents getting stuck between two values)
      x1 = x2
      x2 = xnew
      y1 = y2
      y2 = ynew
      // update error
      err = abs(x1 - xnew)
      // escape clause
      if (iterations > 15) {
        bad = true
      }
    }


    // If the Newton-Raphson scheme failed, try to solve the problem with the other Lambert targeter.
    if (bad) {
      throw new ArithmeticException("Lambert2 failed to converge to a solution, use an alternative targeter instead eg. Lambert3")
    } else {
      // convert converged value of x
      val x = if (N == 0) exp(xnew) - 1 else atan(xnew) * 2 / Pi

      // The solution has been evaluated in terms of log(x+1) or tan(x*pi/2), we now need the conic.
      // As for transfer angles near to pi the Lagrange-coefficients technique goes singular (dg
      // approaches a zero/zero that is numerically bad) we here use a different technique for those
      // cases. When the transfer angle is exactly equal to pi, then the ih unit vector is not
      // determined. The remaining equations, though, are still valid.

      // Solution for the semi-major axis
      val a = a_min / (1 - x * x)

      // Calculate psi
      val (eta2, eta) =
        if (x < 1) {
          // ellipse
          val beta = longwaySign * 2 * asin(sqrt((s - c) / 2 / a))
          // make 100.4% sure it's in (-1 <= xx <= +1)
          val alfa = 2 * acos(max(-1, min(1, x)))
          val psi = (alfa - beta) / 2
          val eta2 = 2 * a * pow(sin(psi), 2) / s
          val eta = sqrt(eta2)
          (eta2, eta)
        } else {
          // hyperbola
          val beta = longwaySign * 2 * FastMath.asinh(sqrt((c - s) / 2 / a))
          val alfa = 2 * FastMath.acosh(x)
          val psi = (alfa - beta) / 2
          val eta2 = -2 * a * pow(sinh(psi), 2) / s
          val eta = sqrt(eta2)
          (eta2, eta)
        }

      // unit of the normalized normal vector
      val ih = planeNormal * longwaySign

      // unit vector for normalized [r2vec]
      val r2n = r2vec / mr2vec

      // cross-products
      val crsprd1 = ih cross r1vec
      val crsprd2 = ih cross r2n

      // radial and tangential directions for departure velocity
      val Vr1 = 1 / eta / sqrt(a_min) * (2 * Lambda * a_min - Lambda - x * eta)
      val Vt1 = sqrt(mr2vec / a_min / eta2 * pow(sin(dth / 2), 2))

      // radial and tangential directions for arrival velocity
      val Vt2 = Vt1 / mr2vec
      val Vr2 = (Vt1 - Vt2) / tan(dth / 2) - Vr1

      // terminal velocities
      val V1 = ((r1vec * Vr1) + (crsprd1 * Vt1)) * V
      val V2 = ((r2n   * Vr2) + (crsprd2 * Vt2)) * V

      new LambertTrajectory2(new PosVel(origin, V1, this.r1.frame),
        new PosVel(destination, V2, this.r1.frame),
        departureEpoch, arrivalEpoch, frame)
    }
  }

}
