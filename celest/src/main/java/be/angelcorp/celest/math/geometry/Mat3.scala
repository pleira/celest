package be.angelcorp.celest.math.geometry

class Mat3(var m00: Double, var m01: Double, var m02: Double,
           var m10: Double, var m11: Double, var m12: Double,
           var m20: Double, var m21: Double, var m22: Double) {

  val size = 16

  def +(s: Double) =
    new Mat3(
      m00+s, m01+s, m02+s,
      m10+s, m11+s, m12+s,
      m20+s, m21+s, m22+s
    )
  def +=(s: Double) {
    m00 += s; m01 += s; m02 += s
    m10 += s; m11 += s; m12 += s
    m20 += s; m21 += s; m22 += s
  }
  def +(mtx: Mat3) =
    new Mat3(
      m00+mtx.m00, m01+mtx.m01, m02+mtx.m02,
      m10+mtx.m10, m11+mtx.m11, m12+mtx.m12,
      m20+mtx.m20, m21+mtx.m21, m22+mtx.m22
    )
  def +=(mtx: Mat3) {
    m00 += mtx.m00; m01 += mtx.m01; m02 += mtx.m02
    m10 += mtx.m10; m11 += mtx.m11; m12 += mtx.m12
    m20 += mtx.m20; m21 += mtx.m21; m22 += mtx.m22
  }
  def -(s: Double) =
    new Mat3(
      m00-s, m01-s, m02-s,
      m10-s, m11-s, m12-s,
      m20-s, m21-s, m22-s
    )
  def -=(s: Double) {
    m00 -= s; m01 -= s; m02 -= s
    m10 -= s; m11 -= s; m12 -= s
    m20 -= s; m21 -= s; m22 -= s
  }
  def -(mtx: Mat3) =
    new Mat3(
      m00-mtx.m00, m01-mtx.m01, m02-mtx.m02,
      m10-mtx.m10, m11-mtx.m11, m12-mtx.m12,
      m20-mtx.m20, m21-mtx.m21, m22-mtx.m22
    )
  def -=(mtx: Mat3) {
    m00 -= mtx.m00; m01 -= mtx.m01; m02 -= mtx.m02
    m10 -= mtx.m10; m11 -= mtx.m11; m12 -= mtx.m12
    m20 -= mtx.m20; m21 -= mtx.m21; m22 -= mtx.m22
  }
  def *(s: Double) =
    new Mat3(
      m00*s, m01*s, m02*s,
      m10*s, m11*s, m12*s,
      m20*s, m21*s, m22*s
    )
  def *=(s: Double) {
    m00 *= s; m01 *= s; m02 *= s
    m10 *= s; m11 *= s; m12 *= s
    m20 *= s; m21 *= s; m22 *= s
  }
  def *(mtx: Mat3) =
    new Mat3(
      m00*mtx.m00, m01*mtx.m01, m02*mtx.m02,
      m10*mtx.m10, m11*mtx.m11, m12*mtx.m12,
      m20*mtx.m20, m21*mtx.m21, m22*mtx.m22
    )
  def *=(mtx: Mat3) {
    m00 *= mtx.m00; m01 *= mtx.m01; m02 *= mtx.m02
    m10 *= mtx.m10; m11 *= mtx.m11; m12 *= mtx.m12
    m20 *= mtx.m20; m21 *= mtx.m21; m22 *= mtx.m22
  }
  def /(s: Double) =
    new Mat3(
      m00/s, m01/s, m02/s,
      m10/s, m11/s, m12/s,
      m20/s, m21/s, m22/s
    )
  def /=(s: Double) {
    m00 /= s; m01 /= s; m02 /= s
    m10 /= s; m11 /= s; m12 /= s
    m20 /= s; m21 /= s; m22 /= s
  }
  def /(mtx: Mat3) =
    new Mat3(
      m00/mtx.m00, m01/mtx.m01, m02/mtx.m02,
      m10/mtx.m10, m11/mtx.m11, m12/mtx.m12,
      m20/mtx.m20, m21/mtx.m21, m22/mtx.m22
    )
  def /=(mtx: Mat3) {
    m00 /= mtx.m00; m01 /= mtx.m01; m02 /= mtx.m02
    m10 /= mtx.m10; m11 /= mtx.m11; m12 /= mtx.m12
    m20 /= mtx.m20; m21 /= mtx.m21; m22 /= mtx.m22
  }

  def dot(mtx: Mat3): Mat3 = {
    val result = Mat3()
    dot(mtx, result)
    result
  }

  /** Matrix multiplication of two matrices. */
  def dot(mtx: Mat3, result: Mat3) {
    val m00 = this.m00 * mtx.m00 + this.m01 * mtx.m10 + this.m02 * mtx.m20
    val m01 = this.m00 * mtx.m01 + this.m01 * mtx.m11 + this.m02 * mtx.m21
    val m02 = this.m00 * mtx.m02 + this.m01 * mtx.m12 + this.m02 * mtx.m22
    val m10 = this.m10 * mtx.m00 + this.m11 * mtx.m10 + this.m12 * mtx.m20
    val m11 = this.m10 * mtx.m01 + this.m11 * mtx.m11 + this.m12 * mtx.m21
    val m12 = this.m10 * mtx.m02 + this.m11 * mtx.m12 + this.m12 * mtx.m22
    val m20 = this.m20 * mtx.m00 + this.m21 * mtx.m10 + this.m22 * mtx.m20
    val m21 = this.m20 * mtx.m01 + this.m21 * mtx.m11 + this.m22 * mtx.m21
    val m22 = this.m20 * mtx.m02 + this.m21 * mtx.m12 + this.m22 * mtx.m22

    result.m00 = m00
    result.m01 = m01
    result.m02 = m02
    result.m10 = m10
    result.m11 = m11
    result.m12 = m12
    result.m20 = m20
    result.m21 = m21
    result.m22 = m22
  }

  /** Matrix vector multiplication */
  def dot(vector: Vec3): Vec3 = Vec3(
    m00 * vector.x + m01 * vector.y + m02 * vector.z,
    m10 * vector.x + m11 * vector.y + m12 * vector.z,
    m20 * vector.x + m21 * vector.y + m22 * vector.z
  )
  /**
   * Matrix vector multiplication.
   * Alias for ''dot''
   */
  def *(vector: Vec3): Vec3 = dot(vector)

  /** Matrix vector multiplication */
  def dot(vector: Vec3, result: Vec3): Unit = {
    val x = m00 * vector.x + m01 * vector.y + m02 * vector.z
    val y = m10 * vector.x + m11 * vector.y + m12 * vector.z
    val z = m20 * vector.x + m21 * vector.y + m22 * vector.z

    result.x = x
    result.y = y
    result.z = z
  }
  /**
   * Matrix vector multiplication.
   * Alias for ''dot''
   */
  def *(vector: Vec3, result: Vec3): Unit = dot(vector, result)

  /** Transpose of this matrix stored in a new matrix. */
  def transpose: Mat3 = {
    val result = Mat3()
    transpose(result)
    result
  }

  /** Transpose of this matrix stored in the given matrix. */
  def transpose(result: Mat3) {
    val m00 = this.m00; val m01 = this.m10; val m02 = this.m20
    val m10 = this.m01; val m11 = this.m11; val m12 = this.m21
    val m20 = this.m02; val m21 = this.m12; val m22 = this.m22

    result.m00 = m00; result.m01 = m01; result.m02 = m02
    result.m10 = m10; result.m11 = m11; result.m12 = m12
    result.m20 = m20; result.m21 = m21; result.m22 = m22
  }

  def determinant = {
    + m00 * (m11 * m22 - m12 * m21)
    + m01 * (m12 * m20 - m10 * m22)
    + m02 * (m10 * m21 - m11 * m20)
  }

  def inverse: Mat3 = {
    val result = Mat3()
    inverse(result)
    result
  }

  def inverse(result: Mat3) {
    val d = determinant

    if (d != 0) {
      val determinant_inv = 1f/d
      
      // get the conjugate matrix
      val t00 =  m11 * m22 - m12 * m21
      val t01 = -m10 * m22 + m12 * m20
      val t02 =  m10 * m21 - m11 * m20
      val t10 = -m01 * m22 + m02 * m21
      val t11 =  m00 * m22 - m02 * m20
      val t12 = -m00 * m21 + m01 * m20
      val t20 =  m01 * m12 - m02 * m11
      val t21 = -m00 * m12 + m02 * m10
      val t22 =  m00 * m11 - m01 * m10

      result.m00 = t00 * determinant_inv
      result.m11 = t11 * determinant_inv
      result.m22 = t22 * determinant_inv
      result.m01 = t10 * determinant_inv
      result.m10 = t01 * determinant_inv
      result.m20 = t02 * determinant_inv
      result.m02 = t20 * determinant_inv
      result.m12 = t21 * determinant_inv
      result.m21 = t12 * determinant_inv
    } else throw new Exception
  }

  /** Negate this matrix in-place. */
  def negate() {
    m00 = -m00; m01 = -m01; m02 = -m02
    m10 = -m10; m11 = -m11; m12 = -m12
    m20 = -m20; m21 = -m21; m22 = -m22
  }

  /** Negate this matrix and store in a new matrix. */
  def unary_- = new Mat3(
    -m00, -m01, -m02,
    -m10, -m11, -m12,
    -m20, -m21, -m22
  )

  /** Negate this matrix and store in the given matrix. */
  def negate(result: Mat3) {
    result.m00 = -m00; result.m01 = -m01; result.m02 = -m02
    result.m10 = -m10; result.m11 = -m11; result.m12 = -m12
    result.m20 = -m20; result.m21 = -m21; result.m22 = -m22
  }

  /**
   * Perfect orthogonality on a 3X3 matrix.
   * @param threshold convergence threshold for the iterative orthogonality correction
   *                  (convergence is reached when the difference between two steps of the Frobenius norm of the correction is below this threshold)
   * @return an orthogonal matrix close to m
   *
   * Exception if the matrix cannot be orthogonalized with the given threshold after 10 iterations
   */
  def orthogonalized(threshold: Double): Mat3 = {
    var x00: Double = m00
    var x01: Double = m01
    var x02: Double = m02
    var x10: Double = m10
    var x11: Double = m11
    var x12: Double = m12
    var x20: Double = m20
    var x21: Double = m21
    var x22: Double = m22
    var fn  = 0.0
    var fn1 = 0.0

    // iterative correction: Xn+1 = Xn - 0.5 * (Xn.Mt.Xn - M)
    var i = 0
    while (i < 10) {

      // Mt.Xn
      val mx00 = m00 * x00 + m10 * x10 + m20 * x20
      val mx10 = m01 * x00 + m11 * x10 + m21 * x20
      val mx20 = m02 * x00 + m12 * x10 + m22 * x20
      val mx01 = m00 * x01 + m10 * x11 + m20 * x21
      val mx11 = m01 * x01 + m11 * x11 + m21 * x21
      val mx21 = m02 * x01 + m12 * x11 + m22 * x21
      val mx02 = m00 * x02 + m10 * x12 + m20 * x22
      val mx12 = m01 * x02 + m11 * x12 + m21 * x22
      val mx22 = m02 * x02 + m12 * x12 + m22 * x22

      // Xn+1
      val o00 = x00 - 0.5 * (x00 * mx00 + x01 * mx10 + x02 * mx20 - m00)
      val o01 = x01 - 0.5 * (x00 * mx01 + x01 * mx11 + x02 * mx21 - m01)
      val o02 = x02 - 0.5 * (x00 * mx02 + x01 * mx12 + x02 * mx22 - m02)
      val o10 = x10 - 0.5 * (x10 * mx00 + x11 * mx10 + x12 * mx20 - m10)
      val o11 = x11 - 0.5 * (x10 * mx01 + x11 * mx11 + x12 * mx21 - m11)
      val o12 = x12 - 0.5 * (x10 * mx02 + x11 * mx12 + x12 * mx22 - m12)
      val o20 = x20 - 0.5 * (x20 * mx00 + x21 * mx10 + x22 * mx20 - m20)
      val o21 = x21 - 0.5 * (x20 * mx01 + x21 * mx11 + x22 * mx21 - m21)
      val o22 = x22 - 0.5 * (x20 * mx02 + x21 * mx12 + x22 * mx22 - m22)

      // correction on each elements
      val corr00 = o00 - m00
      val corr01 = o01 - m01
      val corr02 = o02 - m02
      val corr10 = o10 - m10
      val corr11 = o11 - m11
      val corr12 = o12 - m12
      val corr20 = o20 - m20
      val corr21 = o21 - m21
      val corr22 = o22 - m22

      // Frobenius norm of the correction
      fn1 = corr00 * corr00 + corr01 * corr01 + corr02 * corr02 + corr10 * corr10 + corr11 * corr11 + corr12 * corr12 + corr20 * corr20 + corr21 * corr21 + corr22 * corr22

      // convergence test
      if (math.abs(fn1 - fn) <= threshold)
        return Mat3(o00, o01, o02,
                    o10, o11, o12,
                    o20, o21, o22)

      // prepare next iteration
      x00 = o00; x01 = o01; x02 = o02
      x10 = o10; x11 = o11; x12 = o12
      x20 = o20; x21 = o21; x22 = o22
      fn  = fn1

      i += 1
    }

    // the algorithm did not converge after 10 iterations
    throw new Exception( "UNABLE_TO_ORTHOGONOLIZE_MATRIX" )
  }

  def toLongString = {
    """[ %+12.8f %+12.8f %+12.8f ]
      |[ %+12.8f %+12.8f %+12.8f ]
      |[ %+12.8f %+12.8f %+12.8f ]
    """.stripMargin.format( m00, m01, m02, m10, m11, m12, m20, m21, m22 )
  }

  override def toString = s"{{$m00, $m01, $m02}, {$m10, $m11, $m12}, {$m20, $m21, $m22}}"

}

object Mat3 {

  def identity() = new Mat3(
    1, 0, 0,
    0, 1, 0,
    0, 0, 1
  )

  def zero()= new Mat3(
    0, 0, 0,
    0, 0, 0,
    0, 0, 0
  )

  def columns( c0: Vec3, c1: Vec3, c2: Vec3 ) = new Mat3(
    c0.x, c1.x, c2.x,
    c0.y, c1.y, c2.y,
    c0.z, c1.z, c2.z
  )

  def rows( r0: Vec3, r1: Vec3, r2: Vec3 ) = new Mat3(
    r0.x, r0.y, r0.z,
    r1.x, r1.y, r1.z,
    r2.x, r2.y, r2.z
  )

  def diag( s: Double ) = new Mat3(
    s, 0, 0,
    0, s, 0,
    0, 0, s
  )

  def diag( v: Vec3 ) = new Mat3(
    v.x, 0,  0,
     0, v.y, 0,
     0,  0, v.z
  )

  def apply(): Mat3 = identity()

  def apply( mtx: Mat3 ): Mat3 = new Mat3(
    mtx.m00, mtx.m01, mtx.m02,
    mtx.m10, mtx.m11, mtx.m12,
    mtx.m20, mtx.m21, mtx.m22
  )

  def apply(m00: Double, m01: Double, m02: Double,
            m10: Double, m11: Double, m12: Double,
            m20: Double, m21: Double, m22: Double) =
    new Mat3( m00, m01, m02, m10, m11, m12, m20, m21, m22 )

  /**
   * Rotation around the first axis &lt;1, 0, 0&gt;:
   * <pre>
   *          [ 1,  0,        0      ]
   * R_X(a) = [ 0,  cos(a),   sin(a) ]
   *          [ 0,  -sin(a),  cos(a) ]
   * </pre>
   * @param angle Rotation angle [rad]
   * @return Rotation matrix around the first (X) axis.
   */
  def rotateX(angle: Double) = {
    val s = math.sin(angle)
    val c = math.cos(angle)
    new Mat3( 1,  0,  0,
              0,  c,  s,
              0, -s,  c )
  }

  /**
   * Rotation around the second axis &lt;0, 1, 0&gt;:
   * <pre>
   *          [ cos(a),   0,  -sin(a) ]
   * R_Y(a) = [ 0,        1,  0       ]
   *          [ sin(a),   0,  cos(a)  ]
   * </pre>
   * @param angle Rotation angle [rad]
   * @return Rotation matrix around the second (Y) axis.
   */
  def rotateY(angle: Double) = {
    val s = math.sin(angle)
    val c = math.cos(angle)
    new Mat3( c,  0, -s,
              0,  1,  0,
              s,  0,  c )
  }

  /**
   * Rotation around the third axis &lt;0, 0, 1&gt;:
   * <pre>
   *          [ cos(a),   sin(a),  0 ]
   * R_Z(a) = [ -sin(a),  cos(a),  0 ]
   *          [ 0,        0,       1 ]
   * </pre>
   * @param angle Rotation angle [rad]
   * @return Rotation matrix around the third (Z) axis.
   */
  def rotateZ(angle: Double) = {
    val s = math.sin(angle)
    val c = math.cos(angle)
    new Mat3( c, s, 0,
             -s, c, 0,
              0, 0, 1 )
  }

  /**
   * Rotation sequence of rotateZ rotateX rotateZ:
   * <pre>
   * R_ZXZ(a, b, c) = R_Z(c) . R_X(b) . R_Z(a) =
   * [ cos(a)*cos(c) - cos(b)*sin(a)*sin(c),     cos(c)*sin(a) + cos(a)*cos(b)*sin(c),  sin(b)*sin(c) ]
   * [ -(cos(b)*cos(c)*sin(a)) - cos(a)*sin(c),  cos(a)*cos(b)*cos(c) - sin(a)*sin(c),  cos(c)*sin(b) ]
   * [ sin(a)*sin(b),                             -(cos(a)*sin(b)),                     cos(b)        ]
   * </pre>
   * @param a Angle of the first  rotation around the Z axis [rad].
   * @param b Angle of the second rotation around the X axis [rad].
   * @param c Angle of the thrid  rotation around the Z axis [rad].
   * @return Rotation matrix of the ZXZ rotation sequence.
   */
  def rotateZXZ(a: Double, b: Double, c: Double) = {
    val ca = math.cos(a); val sa = math.sin(a)
    val cb = math.cos(b); val sb = math.sin(b)
    val cc = math.cos(c); val sc = math.sin(c)
    new Mat3(
      ca*cc - cb*sa*sc,     cc*sa + ca*cb*sc,   sb*sc,
      -(cb*cc*sa) - ca*sc,  ca*cb*cc - sa*sc,   cc*sb,
      sa*sb,                -(ca*sb),           cb
    )
  }

  /**
   * Create a rotation matrix from a rotation in axis/angle format.
   * <p>
   * Based on:
   * http://www.euclideanspace.com/maths/geometry/rotations/conversions/angleToMatrix/index.htm
   * </p>
   */
  def rotate(angle: Double, axis: Vec3): Mat3 = {
    val c = math.cos(angle)
    val s = math.sin(angle)
    val t = 1f - c
    val _axis = axis.normalized

    val m00 = c + _axis.x * _axis.x * t
    val m11 = c + _axis.y * _axis.y * t
    val m22 = c + _axis.z * _axis.z * t
    val tmp1 = _axis.x * _axis.y * t
    val tmp2 = _axis.z * s
    val m10 = tmp1 + tmp2
    val m01 = tmp1 - tmp2
    val tmp3 = _axis.x * _axis.z * t
    val tmp4 = _axis.y * s
    val m20 = tmp3 - tmp4
    val m02 = tmp3 + tmp4
    val tmp5 = _axis.y * _axis.z * t
    val tmp6 = _axis.x * s
    val m21 = tmp5 + tmp6
    val m12 = tmp5 - tmp6
    new Mat3(m00, m01, m02,
             m10, m11, m12,
             m20, m21, m22)
  }

  def translate_2D( v: Vec2 ) = Mat3(
    1, 0, v.x,
    0, 1, v.y,
    0, 0,  1
  )

  def rotate_2D( angle: Double ) = {
    val c = math.cos( angle )
    val s = math.sin( angle )
    Mat3(
       c, s, 0,
      -s, c, 0,
       0, 0, 1
    )
  }

}

