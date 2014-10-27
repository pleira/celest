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
package be.angelcorp.celest.math.rotation

import be.angelcorp.celest.math.geometry.{Mat3, Vec3}

import scala.math._

/**
 * Quaternion rotation in 3D space:
 *
 * <pre>
 * q = { q<sub>0</sub> + q<sub>1</sub> i + q<sub>2</sub> j + q<sub>3</sub> j }
 * </pre>
 *
 * <p>
 * Where q<sub>0</sub> is the scalar part of the quaternion and q<sub>1-3</sub> the vectorial part.
 * </p>
 *
 * @author Simon Billemont
 */
class Quaternion(val q0: Double, val q1: Double, val q2: Double, val q3: Double) extends Rotation {
  import Quaternion._

  /**
   * Revert a rotation.
   * Build a rotation which reverse the effect of another rotation.
   * This means that if r(u) = v, then r.revert(v) = u.
   */
  override def inverse() = new Quaternion(-q0, q1, q2, q3)

  /** Get the normalized axis of the rotation. */
  def axis = {
    val squaredSine = q1 * q1 + q2 * q2 + q3 * q3
    if (squaredSine == 0) {
      Vec3(1, 0, 0)
    } else {
      val inverse = (if (q0 < 0) 1 else -1) / sqrt(squaredSine)
      Vec3((q1 * inverse).toFloat, (q2 * inverse).toFloat, (q3 * inverse).toFloat)
    }
  }

  /** Get the angle of the rotation in radians (between 0 and &pi;) */
  def angle = {
    if ((q0 < -0.1) || (q0 > 0.1)) {
      2 * asin(sqrt(q1 * q1 + q2 * q2 + q3 * q3))
    } else if (q0 < 0) {
      2 * acos(-q0)
    } else {
      2 * acos(q0)
    }
  }

  /**
   * Get the Cardan or Euler angles corresponding to the instance.
   *
   * <p>
   *   The equations show that each rotation can be defined by two different values of the Cardan or Euler
   *   angles set. For example if Cardan angles are used, the rotation defined by the angles a<sub>1</sub>,
   *   a<sub>2</sub> and a<sub>3</sub> is the same as the rotation defined by the angles &pi; + a<sub>1</sub>,
   *   &pi; - a<sub>2</sub> and &pi; + a<sub>3</sub>. This method implements the following arbitrary choices:
   * </p>
   * <ul>
   *  <li>For Cardan angles, the chosen set is the one for which the second angle is between -&pi;/2 and &pi;/2 (i.e its cosine is positive),</li>
   *  <li>for Euler angles, the chosen set is the one for which the second angle is between 0 and &pi; (i.e its sine is positive).</li>
   * </ul>
   * <p>
   *   Cardan and Euler angle have a very disappointing drawback: all of them have singularities. This means that if
   *   the instance is too close to the singularities corresponding to the given rotation order, it will be impossible
   *   to retrieve the angles. For Cardan angles, this is often called gimbal lock. There is <em>nothing</em> to do to
   *   prevent this, it is an intrinsic problem with Cardan and Euler representation (but not a problem with the
   *   rotation itself, which is perfectly well defined). For Cardan angles, singularities occur when the second
   *   angle is close to -&pi;/2 or +&pi;/2, for Euler angle singularities occur when the second angle is close to
   *   0 or &pi;, this implies that the identity rotation is always singular for Euler angles!
   * </p>
   * @param order rotation order to use
   * @return an array of three angles, in the order specified by the set
   */
  def angles(order: RotationOrder) = {
    def singularity() = throw new Exception("CardanEulerSingularityException")
    if (order == XYZ) {
      val v1 = applyTo(Vec3.z)
      val v2 = applyInverseTo(Vec3.x)
      if ((v2.z < -0.9999999999) || (v2.z > 0.9999999999))
        singularity()
      Vec3(
        atan2(-v1.y, v1.z).toFloat,
        asin(v2.z).toFloat,
        atan2(-v2.y, v2.x).toFloat
      )
    } else if (order == XZY) {
      val v1 = applyTo(Vec3.y)
      val v2 = applyInverseTo(Vec3.x)
      if ((v2.y < -0.9999999999) || (v2.y > 0.9999999999))
        singularity()
      Vec3(
        atan2(v1.z, v1.y).toFloat,
        -asin(v2.y).toFloat,
        atan2(v2.z, v2.x).toFloat
      )
    } else if (order == YXZ) {
      val v1 = applyTo(Vec3.z)
      val v2 = applyInverseTo(Vec3.y)
      if ((v2.z < -0.9999999999) || (v2.z > 0.9999999999))
        singularity()
      Vec3(
        atan2(v1.x, v1.z).toFloat,
        -asin(v2.z).toFloat,
        atan2(v2.x, v2.y).toFloat
      )
    } else if (order == YZX) {
      val v1 = applyTo(Vec3.x)
      val v2 = applyInverseTo(Vec3.y)
      if ((v2.x < -0.9999999999) || (v2.x > 0.9999999999))
        singularity()
      Vec3(
        atan2(-v1.z, v1.x).toFloat,
        asin(v2.x).toFloat,
        atan2(-v2.z, v2.y).toFloat
      )
    } else if (order == ZXY) {
      val v1 = applyTo(Vec3.y)
      val v2 = applyInverseTo(Vec3.z)
      if ((v2.y < -0.9999999999) || (v2.y > 0.9999999999))
        singularity()
      Vec3(
        atan2(-v1.x, v1.y).toFloat,
        asin(v2.y).toFloat,
        atan2(-v2.x, v2.z).toFloat
      )
    } else if (order == ZYX) {
      val v1 = applyTo(Vec3.x)
      val v2 = applyInverseTo(Vec3.z)
      if ((v2.x < -0.9999999999) || (v2.x > 0.9999999999))
        singularity()
      Vec3(
        atan2(v1.y, v1.x).toFloat,
        -asin(v2.x).toFloat,
        atan2(v2.y, v2.z).toFloat
      )
    } else if (order == XYX) {
      val v1 = applyTo(Vec3.x)
      val v2 = applyInverseTo(Vec3.x)
      if ((v2.x < -0.9999999999) || (v2.x > 0.9999999999))
        singularity()
      Vec3(
        atan2(v1.y, -v1.z).toFloat,
        acos(v2.x).toFloat,
        atan2(v2.y, v2.z).toFloat
      )
    } else if (order == XZX) {
      val v1 = applyTo(Vec3.x)
      val v2 = applyInverseTo(Vec3.x)
      if ((v2.x < -0.9999999999) || (v2.x > 0.9999999999))
        singularity()
      Vec3(
        atan2(v1.z, v1.y).toFloat,
        acos(v2.x).toFloat,
        atan2(v2.z, -v2.y).toFloat
      )
    } else if (order == YXY) {
      val v1 = applyTo(Vec3.y)
      val v2 = applyInverseTo(Vec3.y)
      if ((v2.y < -0.9999999999) || (v2.y > 0.9999999999))
        singularity()
      Vec3(
        atan2(v1.x, v1.z).toFloat,
        acos(v2.y).toFloat,
        atan2(v2.x, -v2.z).toFloat
      )
    } else if (order == YZY) {
      val v1 = applyTo(Vec3.y)
      val v2 = applyInverseTo(Vec3.y)
      if ((v2.y < -0.9999999999) || (v2.y > 0.9999999999))
        singularity()
      Vec3(
        atan2(v1.z, -v1.x).toFloat,
        acos(v2.y).toFloat,
        atan2(v2.z, v2.x).toFloat
      )
    } else if (order == ZXZ) {
      val v1 = applyTo(Vec3.z)
      val v2 = applyInverseTo(Vec3.z)
      if ((v2.z < -0.9999999999) || (v2.z > 0.9999999999))
        singularity()
      Vec3(
        atan2(v1.x, -v1.y).toFloat,
        acos(v2.z).toFloat,
        atan2(v2.x, v2.y).toFloat
      )
    } else { // last possibility is ZYZ
      val v1 = applyTo(Vec3.z)
      val v2 = applyInverseTo(Vec3.z)
      if ((v2.z < -0.9999999999) || (v2.z > 0.9999999999))
        singularity()
      Vec3(
        atan2(v1.y, v1.x).toFloat,
        acos(v2.z).toFloat,
        atan2(v2.y, -v2.x).toFloat
      )
    }

  }

  override def toQuaternion = this

  override def toMatrix = {
    // products
    val q0q0 = q0 * q0
    val q0q1 = q0 * q1
    val q0q2 = q0 * q2
    val q0q3 = q0 * q3
    val q1q1 = q1 * q1
    val q1q2 = q1 * q2
    val q1q3 = q1 * q3
    val q2q2 = q2 * q2
    val q2q3 = q2 * q3
    val q3q3 = q3 * q3

    // create the matrix

    Mat3(
      (2.0 * (q0q0 + q1q1) - 1.0).toFloat,
      (2.0 * (q1q2 + q0q3)).toFloat,
      (2.0 * (q1q3 - q0q2)).toFloat,

      (2.0 * (q1q2 - q0q3)).toFloat,
      (2.0 * (q0q0 + q2q2) - 1.0).toFloat,
      (2.0 * (q2q3 + q0q1)).toFloat,

      (2.0 * (q1q3 + q0q2)).toFloat,
      (2.0 * (q2q3 - q0q1)).toFloat,
      (2.0 * (q0q0 + q3q3) - 1.0).toFloat
    )
  }

  /**  The norm of the quaternion. */
  def norm =
    sqrt(q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3)

  /** Computes the normalized quaternion. */
  def normalized = {
    val norm = this.norm
    new Quaternion(q0 / norm, q1 / norm, q2 / norm, q3 / norm)
  }


  override def applyTo(u: Vec3) = {
    val s = q1 * u.x + q2 * u.y + q3 * u.z

    Vec3(
      (2 * (q0 * (u.x * q0 - (q2 * u.z - q3 * u.y)) + s * q1) - u.x).toFloat,
      (2 * (q0 * (u.y * q0 - (q3 * u.x - q1 * u.z)) + s * q2) - u.y).toFloat,
      (2 * (q0 * (u.z * q0 - (q1 * u.y - q2 * u.x)) + s * q3) - u.z).toFloat
    )
  }

  override def applyInverseTo(u: Vec3) = {
    val s = q1 * u.x + q2 * u.y + q3 * u.z
    val m0 = -q0

    Vec3(
      (2 * (m0 * (u.x * m0 - (q2 * u.z - q3 * u.y)) + s * q1) - u.x).toFloat,
      (2 * (m0 * (u.y * m0 - (q3 * u.x - q1 * u.z)) + s * q2) - u.y).toFloat,
      (2 * (m0 * (u.z * m0 - (q1 * u.y - q2 * u.x)) + s * q3) - u.z).toFloat
    )
  }

  /**
   * Apply the instance to another rotation.
   * Applying the instance to a rotation is computing the composition in an order compliant with the following rule:
   * let u be any vector and v its image by r (i.e. r.applyTo(u) = v),
   * let w be the image of v by the instance (i.e. applyTo(v) = w),
   * then w = comp.applyTo(u),
   * where comp = applyTo(r).
   */
  override def applyTo(r: Rotation) = {
    val q = r.toQuaternion
    new Quaternion(
      q.q0 * q0 - (q.q1 * q1 + q.q2 * q2 + q.q3 * q3),
      q.q1 * q0 + q.q0 * q1 + (q.q2 * q3 - q.q3 * q2),
      q.q2 * q0 + q.q0 * q2 + (q.q3 * q1 - q.q1 * q3),
      q.q3 * q0 + q.q0 * q3 + (q.q1 * q2 - q.q2 * q1)
    )
  }

  /**
   * Apply the inverse of the instance to another rotation.
   * Applying the inverse of the instance to a rotation is computing the composition in an order compliant with the following rule:
   * let u be any vector and v its image by r (i.e. r.applyTo(u) = v),
   * let w be the inverse image of v by the instance (i.e. applyInverseTo(v) = w),
   * then w = comp.applyTo(u),
   * where comp = applyInverseTo(r).
   */
  override def applyInverseTo(r: Rotation) = {
    val q = r.toQuaternion
    new Quaternion(
      -q.q0 * q0 - (q.q1 * q1 + q.q2 * q2 + q.q3 * q3),
      -q.q1 * q0 + q.q0 * q1 + (q.q2 * q3 - q.q3 * q2),
      -q.q2 * q0 + q.q0 * q2 + (q.q3 * q1 - q.q1 * q3),
      -q.q3 * q0 + q.q0 * q3 + (q.q1 * q2 - q.q2 * q1)
    )
  }

  /**
   * Compute the <i>distance</i> between two rotations.
   *
   * <p>
   *   The <i>distance</i> is intended here as a way to check if two rotations are almost similar (i.e. they
   *   transform vectors the same way) or very different. It is mathematically defined as the angle of the rotation
   *   r that prepended to one of the rotations gives the other one:
   * </p>
   * <pre>
   * r<sub>1</sub>(r) = r<sub>2</sub>
   * </pre>
   * <p>
   *   This distance is an angle between 0 and &pi;. Its value is the smallest possible upper bound of the angle in
   *   radians between r<sub>1</sub>(v) and r<sub>2</sub>(v) for all possible vectors v. This upper bound is reached
   *   for some v. The distance is equal to 0 if and only if the two rotations are identical.
   * </p>
   * <p>
   *    Comparing two rotations should always be done using this value rather than for example comparing the components
   *    of the quaternions. It is much more stable, and has a geometric meaning. Also comparing quaternions components
   *    is error prone since for example quaternions (0.36, 0.48, -0.48, -0.64) and (-0.36, -0.48, 0.48, 0.64) represent
   *    exactly the same rotation despite their components are different (they are exact opposites).
   * </p>
   */
  def distanceTo(r: Rotation) =
    applyInverseTo(r).angle

  override def toString =
    s"Quaternion($q0, $q1, $q2, $q3)"

  /**
   * Dot product between this Quaterion and a given one:
   * <pre>
   *   q0 * q.q0 + q1 * q.q1 + q2 * q.q2 + q3 * q.q3
   * </pre>
   */
  def dot( q: Quaternion ) =
    q0 * q.q0 + q1 * q.q1 + q2 * q.q2 + q3 * q.q3

  def unary_- =
    new Quaternion(-q0, -q1, -q2, -q3)

  /**
   * Creates a quaternion value as an interpolation between this and another quaternion based on slerp (spherical linear interpolation).
   *
   * @param q The second, target quaternion.
   * @param t The amount to interpolate between the two quaternions.
   */
  def interpolate(q: Quaternion, t: Double) = if (this == q) this else {
    var _q = q
    var result = this dot _q

    if (result < 0.0f) {
      // Negate the second quaternion and the result of the dot product
      _q = -_q
      result = -result
    }

    // Set the first and second scale for the interpolation
    var scale0 = 1 - t
    var scale1 = t

    // Check if the angle between the 2 quaternions was big enough to warrant such calculations
    if ((1 - result) > 0.1f) {// Get the angle between the 2 quaternions,
      // and then store the sin() of that angle
      val theta = acos(result)
      val invSinTheta = 1.0 / sin(theta)

      // Calculate the scale for q1 and q2, according to the angle and it's sine value
      scale0 = sin((1 - t) * theta) * invSinTheta
      scale1 = sin(t * theta) * invSinTheta
    }

    // Calculate the x, y, z and w values for the quaternion by using a special form of linear interpolation for quaternions.
    val q0 = (scale0 * this.q0) + (scale1 * _q.q0)
    val q1 = (scale0 * this.q1) + (scale1 * _q.q1)
    val q2 = (scale0 * this.q2) + (scale1 * _q.q2)
    val q3 = (scale0 * this.q3) + (scale1 * _q.q3)
    new Quaternion(q0, q1, q2, q3)
  }
}

object Quaternion {

  /** A Quaternion representing the IDENTITY rotation */
  def identity = new Quaternion(1, 0, 0, 0)

  /** Build a rotation from an axis and an angle. */
  def apply(axis: Vec3, angle: Double): Quaternion = {
    val norm = axis.length
    val halfAngle = -0.5 * angle
    val coeff = sin(halfAngle) / norm

    val q0 = cos(halfAngle)
    val q1 = coeff * axis.x
    val q2 = coeff * axis.y
    val q3 = coeff * axis.z
    new Quaternion(q0, q1, q2, q3)
  }


  /** Build a rotation from a 3X3 matrix. */
  def apply(mtx: Mat3, threshold: Double): Quaternion = {
    // compute a "close" orthogonal matrix
    val ort = mtx.orthogonalized(threshold)
    apply(ort)
  }

  /** Build a rotation from a 3X3 matrix. */
  def apply(ort: Mat3): Quaternion = {
    // There are different ways to compute the quaternions elements from the matrix. They all involve computing one
    // element from the diagonal of the matrix, and computing the three other ones using a formula involving a
    // division by the first element, which unfortunately can be zero.
    //
    // Since the norm of the quaternion is 1, we know at least one element has an absolute value greater or
    // equal to 0.5, so it is always possible to select the right formula and avoid division by zero and even
    // numerical inaccuracy. Checking the elements in turn and using the first one greater than 0.45 is safe
    // (this leads to a simple test since qi = 0.45 implies 4 qi^2 - 1 = -0.19)
    var s = ort.m00 + ort.m11 + ort.m22
    if (s > -0.19) {
      // compute q0 and deduce q1, q2 and q3
      val q0 = 0.5 * sqrt(s + 1.0)
      val inv = 0.25 / q0
      val q1 = inv * (ort.m12 - ort.m21)
      val q2 = inv * (ort.m20 - ort.m02)
      val q3 = inv * (ort.m01 - ort.m10)
      new Quaternion(q0, q1, q2, q3)
    } else {
      s = ort.m00 - ort.m11 - ort.m22
      if (s > -0.19) {
        // compute q1 and deduce q0, q2 and q3
        val q1 = 0.5 * sqrt(s + 1.0)
        val inv = 0.25 / q1
        val q0 = inv * (ort.m12 - ort.m21)
        val q2 = inv * (ort.m01 + ort.m10)
        val q3 = inv * (ort.m02 + ort.m20)
        new Quaternion(q0, q1, q2, q3)
      } else {
        s = ort.m11 - ort.m00 - ort.m22
        if (s > -0.19) {
          // compute q2 and deduce q0, q1 and q3
          val q2 = 0.5 * sqrt(s + 1.0)
          val inv = 0.25 / q2
          val q0 = inv * (ort.m20 - ort.m02)
          val q1 = inv * (ort.m01 + ort.m10)
          val q3 = inv * (ort.m21 + ort.m12)
          new Quaternion(q0, q1, q2, q3)
        } else {
          // compute q3 and deduce q0, q1 and q2
          s = ort.m22 - ort.m00 - ort.m11
          val q3 = 0.5 * sqrt(s + 1.0)
          val inv = 0.25 / q3
          val q0 = inv * (ort.m01 - ort.m10)
          val q1 = inv * (ort.m02 + ort.m20)
          val q2 = inv * (ort.m21 + ort.m12)
          new Quaternion(q0, q1, q2, q3)
        }
      }
    }
  }

  /**
   * Build the rotation that transforms a pair of vector into another pair.
   *
   * <p>Except for possible scale factors, if the instance were applied to
   * the pair (u<sub>1</sub>, u<sub>2</sub>) it will produce the pair
   * (v<sub>1</sub>, v<sub>2</sub>).</p>
   *
   * <p>If the angular separation between u<sub>1</sub> and u<sub>2</sub> is
   * not the same as the angular separation between v<sub>1</sub> and
   * v<sub>2</sub>, then a corrected v'<sub>2</sub> will be used rather than
   * v<sub>2</sub>, the corrected vector will be in the (v<sub>1</sub>,
   * v<sub>2</sub>) plane.</p>
   *
   * @param u1 first vector of the origin pair
   * @param u2 second vector of the origin pair
   * @param v1 desired image of u1 by the rotation
   * @param v2 desired image of u2 by the rotation
   */
  def apply(u1: Vec3, u2: Vec3, v1: Vec3, v2: Vec3): Quaternion = {
    // build orthonormalized base from u1, u2
    // this fails when vectors are null or colinear, which is forbidden to define a rotation
    val ou3 = (u1 * u2).normalized
    val ou2 = (ou3 * u1).normalized
    val ou1 = u1.normalized

    // build an orthonormalized base from v1, v2
    // this fails when vectors are null or colinear, which is forbidden to define a rotation
    val ov3 = (v1 * v2).normalized
    val ov2 = (ov3 * v1).normalized
    val ov1 = v1.normalized

    def linearCombination(a1: Double, b1: Double, a2: Double, b2: Double, a3: Double, b3: Double) = (a1 * b1 + a2 * b2 + a3 * b3).toFloat

    // buid a matrix transforming the first base into the second one
    val mtx = Mat3(
      linearCombination(ou1.x, ov1.x, ou2.x, ov2.x, ou3.x, ov3.x),
      linearCombination(ou1.y, ov1.x, ou2.y, ov2.x, ou3.y, ov3.x),
      linearCombination(ou1.z, ov1.x, ou2.z, ov2.x, ou3.z, ov3.x),

      linearCombination(ou1.x, ov1.y, ou2.x, ov2.y, ou3.x, ov3.y),
      linearCombination(ou1.y, ov1.y, ou2.y, ov2.y, ou3.y, ov3.y),
      linearCombination(ou1.z, ov1.y, ou2.z, ov2.y, ou3.z, ov3.y),

      linearCombination(ou1.x, ov1.z, ou2.x, ov2.z, ou3.x, ov3.z),
      linearCombination(ou1.y, ov1.z, ou2.y, ov2.z, ou3.y, ov3.z),
      linearCombination(ou1.z, ov1.z, ou2.z, ov2.z, ou3.z, ov3.z)
    )

    apply(mtx)
  }

  /**
   * Build one of the rotations that transform one vector into another one.
   *
   * <p>
   * Except for a possible scale factor, if the instance were applied to the vector u it will produce the vector v.
   * There is an infinite number of such rotations, this constructor choose the one with the smallest associated
   * angle (i.e. the one whose axis is orthogonal to the (u, v) plane). If u and v are colinear, an arbitrary
   * rotation axis is chosen.
   * </p>
   * @param u origin vector
   * @param v desired image of u by the rotation
   */
  def apply(u: Vec3, v: Vec3): Quaternion = {
    val normProduct = u.length * v.length
    val dot = u dot v

    if (dot < ((2.0e-15 - 1.0) * normProduct)) {
      // special case u = -v: we select a PI angle rotation around an arbitrary vector orthogonal to u
      val w = u * (if (u.y != 0 || u.z != 0) Vec3.x else Vec3.y)
      new Quaternion(0.0, -w.x, -w.y, -w.z)
    } else {
      // general case: (u, v) defines a plane, we select
      // the shortest possible rotation: axis orthogonal to this plane
      val q0 = sqrt(0.5 * (1.0 + dot / normProduct))
      val coeff = 1.0 / (2.0 * q0 * normProduct)
      val q = v * u
      val q1 = coeff * q.x
      val q2 = coeff * q.y
      val q3 = coeff * q.z
      new Quaternion(q0, q1, q2, q3)
    }
  }

  /** Build a rotation from three Cardan or Euler elementary rotations.

    * <p>Cardan rotations are three successive rotations around the
    * canonical axes X, Y and Z, each axis being used once. There are
    * 6 such sets of rotations (XYZ, XZY, YXZ, YZX, ZXY and ZYX). Euler
    * rotations are three successive rotations around the canonical
    * axes X, Y and Z, the first and last rotations being around the
    * same axis. There are 6 such sets of rotations (XYX, XZX, YXY,
    * YZY, ZXZ and ZYZ), the most popular one being ZXZ.</p>
    * <p>Beware that many people routinely use the term Euler angles even
    * for what really are Cardan angles (this confusion is especially
    * widespread in the aerospace business where Roll, Pitch and Yaw angles
    * are often wrongly tagged as Euler angles).</p>

    * @param order order of rotations to use
    * @param alpha1 angle of the first elementary rotation
    * @param alpha2 angle of the second elementary rotation
    * @param alpha3 angle of the third elementary rotation
    */
  def apply(order: RotationOrder, alpha1: Double, alpha2: Double, alpha3: Double): Quaternion = {
    val r1 = apply(order.axis1, alpha1)
    val r2 = apply(order.axis2, alpha2)
    val r3 = apply(order.axis3, alpha3)
    r1.applyTo(r2.applyTo(r3))
  }

  def apply(r: Rotation) =
    r.toQuaternion

  sealed abstract class RotationOrder(val axis1: Vec3, val axis2: Vec3, val axis3: Vec3)
  object XYZ extends RotationOrder(Vec3.x, Vec3.y, Vec3.z)
  object XZY extends RotationOrder(Vec3.x, Vec3.z, Vec3.y)
  object YXZ extends RotationOrder(Vec3.y, Vec3.x, Vec3.z)
  object YZX extends RotationOrder(Vec3.y, Vec3.z, Vec3.x)
  object ZXY extends RotationOrder(Vec3.z, Vec3.x, Vec3.y)
  object ZYX extends RotationOrder(Vec3.z, Vec3.y, Vec3.z)
  object XYX extends RotationOrder(Vec3.x, Vec3.y, Vec3.x)
  object XZX extends RotationOrder(Vec3.x, Vec3.z, Vec3.x)
  object YXY extends RotationOrder(Vec3.y, Vec3.x, Vec3.y)
  object YZY extends RotationOrder(Vec3.y, Vec3.z, Vec3.y)
  object ZXZ extends RotationOrder(Vec3.z, Vec3.x, Vec3.z)
  object ZYZ extends RotationOrder(Vec3.z, Vec3.y, Vec3.z)

}
