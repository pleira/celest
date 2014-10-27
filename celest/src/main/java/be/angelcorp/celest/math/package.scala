package be.angelcorp.celest

import scala.math._

package object math {

  def TwoPi = 2 * Pi

  /**
   * Returns true if both arguments are equal or within the range of allowed error (inclusive).
   * <p>
   * Two NaNs are considered equals, as are two infinities with same sign.
   * </p>
   *
   * @param x
	 *            first value
   * @param y
	 *            second value
   * @param eps
	 *            the amount of absolute error to allow
   * @return true if the values are equal or within range of each other
   */
  def equalsAngle(x: Double, y: Double, eps: Double) = {
    val x_normalized = mod(x, TwoPi)
    val y_normalized = mod(y, TwoPi)

    min((2 * Pi) - abs(x - y), abs(x - y)) < eps
  }

  /**
   * Modulo operation where the result is guaranteed between [0 b) <br/>
   * a mod b
   *
   * @return a % b
   */
  def mod(a: Double, b: Double) = {
    val r = a % b
    if (r < 0) r + b else r
  }

  /**
   * Find the quadrant number of the given angle <br/>
   * x+ (inclusive) and y+ (exclusive) => 0 <br/>
   * x- (inclusive) and y+ (exclusive) => 1 <br/>
   * x- (inclusive) and y- (exclusive) => 2 <br/>
   * x+ (inclusive) and y- (exclusive) => 3 <br/>
   *
   * @param alpha
	 *            Angle to check [rad]
   * @return Quadrant number
   */
  def quadrant(alpha: Double): Int = {
    val alpha_normalized = mod(alpha, TwoPi)
    floor(2 * alpha_normalized / Pi).toInt
  }

  /**
   * Adds/subtracts &pi;/2 until alpha is in the same quadrant as beta;
   *
   * @param alpha
	 *            Angle to modify [rad]
   * @param beta
	 *            Reference angle to modify [rad]
   * @return Angle alpha +n &pi;/2 so alpha is in the same quadrant as beta
   */
  def quadrantFix(alpha: Double, beta: Double) = {
    val qAlpha = quadrant(alpha)
    val qBeta  = quadrant(beta)
    alpha + (qBeta - qAlpha) * Pi / 2
  }

  /**
   * Find the semicircle number of the given angle <br/>
   * x+ (inclusive) over y+ (inclusive) to x- (exclusive) => 0 <br/>
   * x- (inclusive) over y- (inclusive) to x+ (exclusive) => 1 <br/>
   *
   * @param alpha
	 *            Angle to check [rad]
   * @return Semicircle number
   */
  def semiCircle(alpha: Double): Int = {
    val alpha_normalized = mod(alpha, TwoPi)
    floor(alpha_normalized / Pi).toInt
  }

  /**
   * Adds/subtracts &pi; until alpha is in the same half-angle plane ( D=[0,&pi;) and D=[&pi;,2&pi;) )
   * as beta;
   *
   * @param alpha
	 *            Angle to modify [rad]
   * @param beta
	 *            Reference angle to modify [rad]
   * @return Angle alpha +n &pi; so alpha is in the same half-angle plane as beta
   */
  def semiCircleFix(alpha: Double, beta: Double) = {
    val qAlpha = semiCircle(alpha)
    val qBeta = semiCircle(beta)
    alpha + (qBeta - qAlpha) * Pi
  }

}
