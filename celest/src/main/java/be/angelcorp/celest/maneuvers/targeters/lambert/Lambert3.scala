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
import be.angelcorp.celest.math._
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.maneuvers.targeters.TPBVP
import be.angelcorp.celest.state.PosVel
import com.google.common.base.Preconditions._
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem

class Lambert3[F <: BodyCenteredSystem]
(val r1: PosVel[F], val r2: PosVel[F],
 val departureEpoch: Epoch, val arrivalEpoch: Epoch,
 val frame: F, val N: Double = 0,
 val prograde: Boolean = true, val leftBranch: Boolean = true) extends TPBVP[F] {

  val longWay = {
    val r1 = this.r1.position
    val r2 = this.r1.position
    val progradeIsLong = (r1.x * r2.y - r2.x * r1.y < 0.0)
    if (prograde) progradeIsLong else !progradeIsLong
  }

  override lazy val trajectory = {
    val r1vec = this.r1.position
    val r2vec = this.r2.position

    // manipulate input
    val tol = 1E-12
    // optimum for numerical noise v.s. actual precision
    val r1 = r1vec.norm // magnitude of r1vec
    val r2 = r2vec.norm // magnitude of r2vec
    val r1unit = r1vec / r1 // unit vector of r1vec
    val r2unit = r2vec / r2 // unit vector of r2vec
    val crsprod = r1vec * r2vec // cross product of r1vec and r2vec
    val mcrsprd = crsprod.norm // magnitude of that cross product
    val th1unit = (crsprod / mcrsprd) * r1unit // unit vectors in the tangential-directions
    val th2unit = (crsprod / mcrsprd) * r2unit
    val tf = abs(arrivalEpoch.relativeToS(departureEpoch))

    val dth = if (longWay) 2 * Pi - r1vec.angle(r2vec) else r1vec.angle(r2vec)

    // define constants
    val c = sqrt(pow(r1, 2) + pow(r2, 2) - 2 * r1 * r2 * cos(dth))
    val s = (r1 + r2 + c) / 2
    val T = sqrt(8 * frame.centerBody.μ / pow(s, 3)) * tf
    val q = sqrt(r1 * r2) / s * cos(dth / 2)

    // general formulae for the initial values (Gooding)

    // some initial values
    val (a_, b_, c_, d_) = Lambert3.LancasterBlanchard(0, q, N)
    val T0 = a_
    val Td = T0 - T
    val phr = mod(2 * atan2(1 - pow(q, 2), 2 * q), 2 * Pi)

    // single-revolution case
    val x0 = if (N == 0) {
      val x01 = T0 * Td / 4 / T
      val x0 =
        if (Td > 0) {
          x01
        } else {
          val x01 = Td / (4 - Td)
          val x02 = -sqrt(-Td / (T + T0 / 2))
          val W = x01 + 1.7 * sqrt(2 - phr / Pi)
          val x03 = if (W >= 0) x01 else x01 + pow(-W, 1 / 16).*(x02 - x01)
          val lambda = 1 + x03 * (1 + x01) / 2 - 0.03 * pow(x03, 2) * sqrt(1 + x01)
          lambda * x03
        }

      // this estimate might not give a solution
      if (x0 < -1)
        throw new ArithmeticException("Lambert targeter cannot find a good initial guess")
      else x0
      // multi-revolution case
    } else {
      // determine minimum Tp(x)
      val xMpi = 4 / (3 * Pi * (2 * N + 1))
      val xM0 = if (phr < Pi)
        xMpi * pow(phr / Pi, 1 / 8)
      else if (phr > Pi)
        xMpi * (2 - pow(2 - phr / Pi, 1 / 8))
      else
        0

      // use Halley's method
      var xM = xM0
      var Tp = Double.PositiveInfinity
      var iterations = 0
      while (abs(Tp) > tol) {
        // iterations
        iterations = iterations + 1
        // compute first three derivatives
        val (a_, b_, c_, d_) = Lambert3.LancasterBlanchard(xM, q, N)
        Tp = b_
        val Tpp = c_
        val Tppp = d_
        // new value of xM
        val xMp = xM
        xM = xM - 2 * Tp * Tpp / (2 * pow(Tpp, 2) - Tp * Tppp)
        // escape clause
        if (mod(iterations, 7) != 0) xM = (xMp + xM) / 2
        // the method might fail. Exit in that case
        if (iterations > 25)
          throw new ArithmeticException("Lambert targeter did not converge with 25 iterations")
      }

      // xM should be elliptic (-1 < x < 1)
      // (this should be impossible to go wrong)
      if ((xM < -1) || (xM > 1))
        throw new ArithmeticException("Lambert targeter solution should be elliptical")

      // corresponding time
      val (a_, b_, c_, d_) = Lambert3.LancasterBlanchard(xM, q, N)
      val TM = a_

      // T should lie above the minimum T
      if (TM > T)
        throw new ArithmeticException("Lambert targeter solution time of flight should be higher than the minimally allowed")

      // find two initial values for second solution (again with lambda-type patch)

      // some initial values
      val TmTM = T - TM
      val T0mTM = T0 - TM
      val (a_1, b_1, c_1, d_1) = Lambert3.LancasterBlanchard(xM, q, N)
      Tp = b_1
      val Tpp = c_1
      val Tppp = d_1

      // first estimate (only if m > 0)
      if (leftBranch) {
        val x = sqrt(TmTM / (Tpp / 2 + TmTM / pow(1 - xM, 2)))
        val W_ = xM + x
        val W = 4 * W_ / (4 + TmTM) + pow(1 - W_, 2)
        val x0 = x * (1 - (1 + N + (dth - 1 / 2)) / (1 + 0.15 * N) * x * (W / 2 + 0.03 * x * sqrt(W))) + xM

        // first estimate might not be able to yield possible solution
        if (x0 > 1)
          throw new ArithmeticException("Lambert targeter first estimate might not be able to yield possible solution")
        else x0
        // second estimate (only if m > 0)
      } else {
        val x0 = if (Td > 0)
          xM - sqrt(TM / (Tpp / 2 - TmTM * (Tpp / 2 / T0mTM - 1 / pow(xM, 2))))
        else {
          val x00 = Td / (4 - Td)
          val W = x00 + 1.7 * sqrt(2 * (1 - phr))
          val x03 = if (W >= 0) x00 else x00 - sqrt(pow(-W, 1 / 8)) * (x00 + sqrt(-Td / (1.5 * T0 - Td)))
          val W2 = 4 / (4 - Td)
          val lambda = (1 + (1 + N + 0.24 * (dth - 1 / 2)) / (1 + 0.15 * N) * x03 * (W2 / 2 - 0.03 * x03 * sqrt(W2)))
          x03 * lambda
        }

        // estimate might not give solutions
        if (x0 < -1)
          throw new ArithmeticException("Lambert targeter first estimate might not be able to yield possible solution")
        else x0
      }
    }

    var x = x0
    var Tx = Double.PositiveInfinity
    var iterations = 0
    while (abs(Tx) > tol) {
      // iterations
      iterations = iterations + 1
      // compute function value, and first two derivatives
      val (a_, b_, c_, d_) = Lambert3.LancasterBlanchard(x, q, N)
      val Tp = b_
      val Tpp = c_

      // find the root of the *difference* between the function value [T_x] and the required time [T]
      Tx = a_ - T
      // new value of x
      val xp = x
      x = x - 2 * Tx * Tp / (2 * pow(Tp, 2) - Tx * Tpp)
      // escape clause
      if (mod(iterations, 7) != 0) x = (xp + x) / 2
      // Halley's method might fail
      if (iterations > 25)
        throw new ArithmeticException("Lambert targeter did not converge with 25 iterations")
    }

    // calculate terminal velocities

    // constants required for this calculation
    val gamma = sqrt(frame.centerBody.μ * s / 2)
    val (sigma, rho, z) =
      if (c == 0) {
        val sigma = 1.0
        val rho = 0.0
        val z = abs(x)
        (sigma, rho, z)
      } else {
        val sigma = 2 * sqrt(r1 * r2 / (c * c)) * sin(dth / 2)
        val rho = (r1 - r2) / c
        val z = sqrt(1 + q * q * (x * x - 1))
        (sigma, rho, z)
      }

    // radial component
    val Vr1 = gamma * ((q * z - x) - rho * (q * z + x)) / r1
    val Vr1vec = r1unit * Vr1
    val Vr2 = -gamma * ((q * z - x) + rho * (q * z + x)) / r2
    val Vr2vec = r2unit * Vr2

    // tangential component
    val Vtan1 = sigma * gamma * (z + q * x) / r1
    val Vtan1vec = th1unit * Vtan1
    val Vtan2 = sigma * gamma * (z + q * x) / r2
    val Vtan2vec = th2unit * Vtan2

    // Cartesian velocity
    val V1 = Vtan1vec + Vr1vec
    val V2 = Vtan2vec + Vr2vec

    // also determine minimum/maximum distance
    // val a = s/2/(1 - x*x); // semi-major axis

    new LambertTrajectory2(new PosVel(r1vec, V1, frame),
      new PosVel(r2vec, V2, frame),
      departureEpoch, arrivalEpoch, frame
    )
  }

}

object Lambert3 {

  def LancasterBlanchard(x: Double, q: Double, m: Double): (Double, Double, Double, Double) = {
    checkArgument(x > -1) // impossible; negative eccentricity

    // compute parameter E
    val E = x * x - 1

    // T(x), T'(x), T''(x)
    if (x == 1) {
      // exactly parabolic; solutions known exactly
      // T(x)
      val T = 4 / 3 * (1 - pow(q, 3))
      // T'(x)
      val Tp = 4 / 5 * (pow(q, 5) - 1)
      // T''(x)
      val Tpp = Tp + 120 / 70 * (1 - pow(q, 7))
      // T'''(x)
      val Tppp = 3 * (Tpp - Tp) + 2400 / 1080 * (pow(q, 9) - 1)
      (T, Tp, Tpp, Tppp)
    } else if (abs(x - 1) < 1E-2) {
      // near-parabolic; compute with series
      // T(x)
      val T = sigmax(-E) - pow(q, 3) * sigmax(-E * q * q)
      // T'(x)
      val Tp = 2 * x * (pow(q, 5) * dsigdx(-E * q * q) - dsigdx(-E))
      // T''(x)
      val Tpp = Tp / x + 4 * x * x * (d2sigdx2(-E) - pow(q, 7) * d2sigdx2(-E * q * q))
      // T'''(x)
      val Tppp = 3 * (Tpp - Tp / x) / x + 8 * x * x * (pow(q, 9) * d3sigdx3(-E * q * q) - d3sigdx3(-E))
      (T, Tp, Tpp, Tppp)
    } else {
      // all other cases
      // compute all substitution functions
      val y = sqrt(abs(E))
      val z = sqrt(1 + q * q * E)
      val f = y * (z - q * x)
      val g = x * z - q * E
      val d = if (E < 0) (atan2(f, g) + Pi * m) else log(max(0, f + g))
      // T(x)
      val T = 2 * (x - q * z - d / y) / E
      //  T'(x)
      val Tp = (4 - 4 * pow(q, 3) * x / z - 3 * x * T) / E
      // T''(x)
      val Tpp = (-4 * pow(q, 3) / z * (1 - (q * q * x * x) / (z * z)) - 3 * T - 3 * x * Tp) / E
      // T'''(x)
      val Tppp = (4 * pow(q, 3) / pow(z, 2) * ((1 - (q * q * x * x) / (z * z)) + 2 * q * q * x / (z * z) * (z - x)) - 8 * Tp - 7 * x * Tpp) / E
      (T, Tp, Tpp, Tppp)
    }
  }

  /**
   * Series approximation to T(x) (used for near-parabolic cases). This uses 25 precomputed
   * coefficients (from Mathematica):
   * <pre>
   * \[Sigma][u_] := 2 (ArcSin[u^(1/2)] - u^(1/2) (1 - u)^(1/2))/u^(3/2);
   * N[Series[\[Sigma][u], {u, 0, 25}], 18]
   * </pre>
   */
  def sigmax(y: Double) =
    List(1.333333333333333333, 0.400000000000000000, 0.214285714285714286, 0.138888888888888889, 0.0994318181818181818, 0.0757211538461538462, 0.0601562500000000000, 0.0492876838235294118, 0.0413432874177631579, 0.0353277297247023810, 0.0306429655655570652, 0.0269100952148437500, 0.0238785567107024016, 0.0213766920155492322, 0.0192833523596486738, 0.0175108421932567250, 0.0159942781818764550, 0.0146847307631695593, 0.0135446768791343157, 0.0125449093302893566, 0.0116623895460073728, 0.0108787263331275123, 0.0101790790399428125, 0.00955135411245743055, 0.00898560805595974778, 0.00847359793654468290).
      zipWithIndex.foldLeft(0.0)((sum, x) => sum + x._1 * pow(y, x._2))

  /**
   * Series approximation to D(T(x))/dx (used for near-parabolic cases). This uses 25 precomputed
   * coefficients (from Mathematica):
   * <pre>
   * \[Sigma][u_] := 2 (ArcSin[u^(1/2)] - u^(1/2) (1 - u)^(1/2))/u^(3/2);
   * N[Series[D[\[Sigma][u], u], {u, 0, 25}], 18]
   * </pre>
   */
  def dsigdx(y: Double) =
    List(0.400000000000000000, 0.428571428571428571, 0.416666666666666667, 0.397727272727272727, 0.378605769230769231, 0.360937500000000000, 0.345013786764705882, 0.330746299342105263, 0.317949567522321429, 0.306429655655570652, 0.296011047363281250, 0.286542680528428819, 0.277896996202140019, 0.269966933035081433, 0.262662632898850874, 0.255908450910023281, 0.249640422973882508, 0.243804183824417683, 0.238353277275497775, 0.233247790920147456, 0.228453252995677758, 0.223939738878741875, 0.219681144586520903, 0.215654593343033947, 0.211839948413617073, 0.208219411113457072).
      zipWithIndex.foldLeft(0.0)((sum, x) => sum + x._1 * pow(y, x._2))

  /**
   * Series approximation to D2(T(x)) / dx (used for near-parabolic cases). This uses 25 precomputed
   * coefficients (from Mathematica):
   * <pre>
   * \[Sigma][u_] := 2 (ArcSin[u^(1/2)] - u^(1/2) (1 - u)^(1/2))/u^(3/2);
   * N[Series[D[D[\[Sigma][u], u], u], {u, 0, 25}], 18]
   * </pre>
   */
  def d2sigdx2(y: Double) =
    List(0.428571428571428571, 0.833333333333333333, 1.19318181818181818, 1.51442307692307692, 1.80468750000000000, 2.07008272058823529, 2.31522409539473684, 2.54359654017857143, 2.75786690090013587, 2.96011047363281250, 3.15196948581271701, 3.33476395442568023, 3.50957012945605863, 3.67727686058391224, 3.83862676365034921, 3.99424676758212012, 4.14467112501510061, 4.29035899095895995, 4.43170802748280166, 4.56906505991355516, 4.70273451645357938, 4.83298518090345986, 4.96005564688978077, 5.08415876192680974, 5.20548527783642679, 5.32420687189234530).
      zipWithIndex.foldLeft(0.0)((sum, x) => sum + x._1 * pow(y, x._2))

  /**
   * Series approximation to D2(T(x)) / dx (used for near-parabolic cases). This uses 25 precomputed
   * coefficients (from Mathematica):
   * <pre>
   * \[Sigma][u_] := 2 (ArcSin[u^(1/2)] - u^(1/2) (1 - u)^(1/2))/u^(3/2);
   * N[Series[D[D[D[\[Sigma][u], u], u], u], {u, 0, 25}], 18]
   * </pre>
   */
  def d3sigdx3(y: Double) =
    List(0.833333333333333333, 2.38636363636363636, 4.54326923076923077, 7.21875000000000000, 10.3504136029411765, 13.8913445723684211, 17.8051757812500000, 22.0629352072010870, 26.6409942626953125, 31.5196948581271701, 36.6824034986824825, 42.1148415534727035, 47.8045991875908592, 53.7407746911048889, 59.9137015137318018, 66.3147380002416097, 72.9361028463023192, 79.7707444946904299, 86.8122361383575480, 94.0546903290715876, 101.492688798972657, 109.121224231575177, 116.935651524316624, 124.931646668074243, 133.105171797308632, 141.452445282902564).
      zipWithIndex.foldLeft(0.0)((sum, x) => sum + x._1 * pow(y, x._2))


}
