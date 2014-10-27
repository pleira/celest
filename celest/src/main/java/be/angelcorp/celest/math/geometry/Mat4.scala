package be.angelcorp.celest.math.geometry

import be.angelcorp.celest.math.geometry

class Mat4( var m00: Double, var m01: Double, var m02: Double, var m03: Double,
            var m10: Double, var m11: Double, var m12: Double, var m13: Double,
            var m20: Double, var m21: Double, var m22: Double, var m23: Double,
            var m30: Double, var m31: Double, var m32: Double, var m33: Double) {

  val size = 16

  def r0 = Vec4( m00, m01, m02, m03 )
  def r1 = Vec4( m10, m11, m12, m13 )
  def r2 = Vec4( m20, m21, m22, m23 )
  def r3 = Vec4( m30, m31, m32, m33 )

  def c0 = Vec4( m00, m10, m20, m30 )
  def c1 = Vec4( m01, m11, m21, m31 )
  def c2 = Vec4( m02, m12, m22, m32 )
  def c3 = Vec4( m03, m13, m23, m33 )

  def mtx3x3 = new Mat3( m00, m01, m02, m10, m11, m12, m20, m21, m22 )

  def mtx3x3_=(mtx: Mat3) {
    m00 = mtx.m00; m01 = mtx.m01; m02 = mtx.m02
    m10 = mtx.m10; m11 = mtx.m11; m12 = mtx.m12
    m20 = mtx.m20; m21 = mtx.m21; m22 = mtx.m22
  }

  /** Copy the elements of the given matrix into this matrix */
  def copyOf( mtx: Mat4 ) {
    m00 = mtx.m00; m01 = mtx.m01; m02 = mtx.m02; m03 = mtx.m03
    m10 = mtx.m10; m11 = mtx.m11; m12 = mtx.m12; m13 = mtx.m13
    m20 = mtx.m20; m21 = mtx.m21; m22 = mtx.m22; m23 = mtx.m23
    m30 = mtx.m30; m31 = mtx.m31; m32 = mtx.m32; m33 = mtx.m33
  }

  def +(s: Double) =
    Mat4(
      m00+s, m01+s, m02+s, m03+s,
      m10+s, m11+s, m12+s, m13+s,
      m20+s, m21+s, m22+s, m23+s,
      m30+s, m31+s, m32+s, m33+s
    )
  def +=(s: Double) {
    m00 += s; m01 += s; m02 += s; m03 += s
    m10 += s; m11 += s; m12 += s; m13 += s
    m20 += s; m21 += s; m22 += s; m23 += s
    m30 += s; m31 += s; m32 += s; m33 += s
  }
  def +(mtx: Mat4) =
    Mat4(
      m00+mtx.m00, m01+mtx.m01, m02+mtx.m02, m03+mtx.m03,
      m10+mtx.m10, m11+mtx.m11, m12+mtx.m12, m13+mtx.m13,
      m20+mtx.m20, m21+mtx.m21, m22+mtx.m22, m23+mtx.m23,
      m30+mtx.m30, m31+mtx.m31, m32+mtx.m32, m33+mtx.m33
    )
  def +=(mtx: Mat4) {
    m00 += mtx.m00; m01 += mtx.m01; m02 += mtx.m02; m03 += mtx.m03
    m10 += mtx.m10; m11 += mtx.m11; m12 += mtx.m12; m13 += mtx.m13
    m20 += mtx.m20; m21 += mtx.m21; m22 += mtx.m22; m23 += mtx.m23
    m30 += mtx.m30; m31 += mtx.m31; m32 += mtx.m32; m33 += mtx.m33
  }
  def -(s: Double) =
    Mat4(
      m00-s, m01-s, m02-s, m03-s,
      m10-s, m11-s, m12-s, m13-s,
      m20-s, m21-s, m22-s, m23-s,
      m30-s, m31-s, m32-s, m33-s
    )
  def -=(s: Double) {
    m00 -= s; m01 -= s; m02 -= s; m03 -= s
    m10 -= s; m11 -= s; m12 -= s; m13 -= s
    m20 -= s; m21 -= s; m22 -= s; m23 -= s
    m30 -= s; m31 -= s; m32 -= s; m33 -= s
  }
  def -(mtx: Mat4) =
    Mat4(
      m00-mtx.m00, m01-mtx.m01, m02-mtx.m02, m03-mtx.m03,
      m10-mtx.m10, m11-mtx.m11, m12-mtx.m12, m13-mtx.m13,
      m20-mtx.m20, m21-mtx.m21, m22-mtx.m22, m23-mtx.m23,
      m30-mtx.m30, m31-mtx.m31, m32-mtx.m32, m33-mtx.m33
    )
  def -=(mtx: Mat4) {
    m00 -= mtx.m00; m01 -= mtx.m01; m02 -= mtx.m02; m03 -= mtx.m03
    m10 -= mtx.m10; m11 -= mtx.m11; m12 -= mtx.m12; m13 -= mtx.m13
    m20 -= mtx.m20; m21 -= mtx.m21; m22 -= mtx.m22; m23 -= mtx.m23
    m30 -= mtx.m30; m31 -= mtx.m31; m32 -= mtx.m32; m33 -= mtx.m33
  }
  def *(s: Double) =
    Mat4(
      m00*s, m01*s, m02*s, m03*s,
      m10*s, m11*s, m12*s, m13*s,
      m20*s, m21*s, m22*s, m23*s,
      m30*s, m31*s, m32*s, m33*s
    )
  def *=(s: Double) {
    m00 *= s; m01 *= s; m02 *= s; m03 *= s
    m10 *= s; m11 *= s; m12 *= s; m13 *= s
    m20 *= s; m21 *= s; m22 *= s; m23 *= s
    m30 *= s; m31 *= s; m32 *= s; m33 *= s
  }
  def *(mtx: Mat4) =
    Mat4(
      m00*mtx.m00, m01*mtx.m01, m02*mtx.m02, m03*mtx.m03,
      m10*mtx.m10, m11*mtx.m11, m12*mtx.m12, m13*mtx.m13,
      m20*mtx.m20, m21*mtx.m21, m22*mtx.m22, m23*mtx.m23,
      m30*mtx.m30, m31*mtx.m31, m32*mtx.m32, m33*mtx.m33
    )
  def *=(mtx: Mat4) {
    m00 *= mtx.m00; m01 *= mtx.m01; m02 *= mtx.m02; m03 *= mtx.m03
    m10 *= mtx.m10; m11 *= mtx.m11; m12 *= mtx.m12; m13 *= mtx.m13
    m20 *= mtx.m20; m21 *= mtx.m21; m22 *= mtx.m22; m23 *= mtx.m23
    m30 *= mtx.m30; m31 *= mtx.m31; m32 *= mtx.m32; m33 *= mtx.m33
  }
  def /(s: Double) =
    Mat4(
      m00/s, m01/s, m02/s, m03/s,
      m10/s, m11/s, m12/s, m13/s,
      m20/s, m21/s, m22/s, m23/s,
      m30/s, m31/s, m32/s, m33/s
    )
  def /=(s: Double) {
    m00 /= s; m01 /= s; m02 /= s; m03 /= s
    m10 /= s; m11 /= s; m12 /= s; m13 /= s
    m20 /= s; m21 /= s; m22 /= s; m23 /= s
    m30 /= s; m31 /= s; m32 /= s; m33 /= s
  }
  def /(mtx: Mat4) =
    Mat4(
      m00/mtx.m00, m01/mtx.m01, m02/mtx.m02, m03/mtx.m03,
      m10/mtx.m10, m11/mtx.m11, m12/mtx.m12, m13/mtx.m13,
      m20/mtx.m20, m21/mtx.m21, m22/mtx.m22, m23/mtx.m23,
      m30/mtx.m30, m31/mtx.m31, m32/mtx.m32, m33/mtx.m33
    )
  def /=(mtx: Mat4) {
    m00 /= mtx.m00; m01 /= mtx.m01; m02 /= mtx.m02; m03 /= mtx.m03
    m10 /= mtx.m10; m11 /= mtx.m11; m12 /= mtx.m12; m13 /= mtx.m13
    m20 /= mtx.m20; m21 /= mtx.m21; m22 /= mtx.m22; m23 /= mtx.m23
    m30 /= mtx.m30; m31 /= mtx.m31; m32 /= mtx.m32; m33 /= mtx.m33
  }

  /** Matrix multiplication of two matrices. */
  def dot(mtx: Mat4): Mat4 = {
    val result = Mat4()
    dot(mtx, result)
    result
  }

  /** Matrix multiplication of two matrices. */
  def dot(mtx: Mat4, result: Mat4) {
    val m00 = this.m00 * mtx.m00 + this.m01 * mtx.m10 + this.m02 * mtx.m20 + this.m03 * mtx.m30
    val m01 = this.m00 * mtx.m01 + this.m01 * mtx.m11 + this.m02 * mtx.m21 + this.m03 * mtx.m31
    val m02 = this.m00 * mtx.m02 + this.m01 * mtx.m12 + this.m02 * mtx.m22 + this.m03 * mtx.m32
    val m03 = this.m00 * mtx.m03 + this.m01 * mtx.m13 + this.m02 * mtx.m23 + this.m03 * mtx.m33
    val m10 = this.m10 * mtx.m00 + this.m11 * mtx.m10 + this.m12 * mtx.m20 + this.m13 * mtx.m30
    val m11 = this.m10 * mtx.m01 + this.m11 * mtx.m11 + this.m12 * mtx.m21 + this.m13 * mtx.m31
    val m12 = this.m10 * mtx.m02 + this.m11 * mtx.m12 + this.m12 * mtx.m22 + this.m13 * mtx.m32
    val m13 = this.m10 * mtx.m03 + this.m11 * mtx.m13 + this.m12 * mtx.m23 + this.m13 * mtx.m33
    val m20 = this.m20 * mtx.m00 + this.m21 * mtx.m10 + this.m22 * mtx.m20 + this.m23 * mtx.m30
    val m21 = this.m20 * mtx.m01 + this.m21 * mtx.m11 + this.m22 * mtx.m21 + this.m23 * mtx.m31
    val m22 = this.m20 * mtx.m02 + this.m21 * mtx.m12 + this.m22 * mtx.m22 + this.m23 * mtx.m32
    val m23 = this.m20 * mtx.m03 + this.m21 * mtx.m13 + this.m22 * mtx.m23 + this.m23 * mtx.m33
    val m30 = this.m30 * mtx.m00 + this.m31 * mtx.m10 + this.m32 * mtx.m20 + this.m33 * mtx.m30
    val m31 = this.m30 * mtx.m01 + this.m31 * mtx.m11 + this.m32 * mtx.m21 + this.m33 * mtx.m31
    val m32 = this.m30 * mtx.m02 + this.m31 * mtx.m12 + this.m32 * mtx.m22 + this.m33 * mtx.m32
    val m33 = this.m30 * mtx.m03 + this.m31 * mtx.m13 + this.m32 * mtx.m23 + this.m33 * mtx.m33

    result.m00 = m00; result.m01 = m01; result.m02 = m02; result.m03 = m03
    result.m10 = m10; result.m11 = m11; result.m12 = m12; result.m13 = m13
    result.m20 = m20; result.m21 = m21; result.m22 = m22; result.m23 = m23
    result.m30 = m30; result.m31 = m31; result.m32 = m32; result.m33 = m33
  }

  /** Matrix vector multiplication */
  def dot(vector: Vec4): Vec4 = new Vec4(
    m00 * vector.x + m01 * vector.y + m02 * vector.z + m03 * vector.w,
    m10 * vector.x + m11 * vector.y + m12 * vector.z + m13 * vector.w,
    m20 * vector.x + m21 * vector.y + m22 * vector.z + m23 * vector.w,
    m30 * vector.x + m31 * vector.y + m32 * vector.z + m33 * vector.w
  )
  /**
   * Matrix vector multiplication.
   * Alias for ''dot''
   */
  def *(vector: Vec4) = dot(vector)

  /** Matrix vector multiplication */
  def dot(vector: Vec4, result: Vec4) {
    val x = m00 * vector.x + m01 * vector.y + m02 * vector.z + m03 * vector.w
    val y = m10 * vector.x + m11 * vector.y + m12 * vector.z + m13 * vector.w
    val z = m20 * vector.x + m21 * vector.y + m22 * vector.z + m23 * vector.w
    val w = m30 * vector.x + m31 * vector.y + m32 * vector.z + m33 * vector.w
    result.x = x
    result.y = y
    result.z = z
    result.w = w
  }
  /**
   * Matrix vector multiplication.
   * Alias for ''dot''
   */
  def *(vector: Vec4, result: Vec4) = dot(vector, result)

  /** Transpose of this matrix stored in a new matrix. */
  def transpose: Mat4 = {
    val result = Mat4()
    transpose(result)
    result
  }

  /** Transpose of this matrix stored in the given matrix. */
  def transpose(result: Mat4) {
    val m00 = this.m00; val m01 = this.m10; val m02 = this.m20; val m03 = this.m30
    val m10 = this.m01; val m11 = this.m11; val m12 = this.m21; val m13 = this.m31
    val m20 = this.m02; val m21 = this.m12; val m22 = this.m22; val m23 = this.m32
    val m30 = this.m03; val m31 = this.m13; val m32 = this.m23; val m33 = this.m33

    result.m00 = m00; result.m01 = m01; result.m02 = m02; result.m03 = m03
    result.m10 = m10; result.m11 = m11; result.m12 = m12; result.m13 = m13
    result.m20 = m20; result.m21 = m21; result.m22 = m22; result.m23 = m23
    result.m30 = m30; result.m31 = m31; result.m32 = m32; result.m33 = m33
  }

  def determinant = {
    + m00 * ((m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32) - m13 * m22 * m31 - m11 * m23 * m32 - m12 * m21 * m33) +
    - m01 * ((m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32) - m13 * m22 * m30 - m10 * m23 * m32 - m12 * m20 * m33) +
    + m02 * ((m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31) - m13 * m21 * m30 - m10 * m23 * m31 - m11 * m20 * m33) +
    - m03 * ((m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31) - m12 * m21 * m30 - m10 * m22 * m31 - m11 * m20 * m32)
  }

  private def determinant3x3(t00: Double, t01: Double, t02: Double, t10: Double, t11: Double, t12: Double, t20: Double, t21: Double, t22: Double) =
    + t00 * (t11 * t22 - t12 * t21) + t01 * (t12 * t20 - t10 * t22) + t02 * (t10 * t21 - t11 * t20)

  def inverse: Mat4 = {
    val result = Mat4()
    inverse(result)
    result
  }

  def inverse(result: Mat4) {
    val d = determinant

    if (d != 0) {
      val determinant_inv = 1f/d
      // first row
      val t00 =  determinant3x3(m11, m12, m13, m21, m22, m23, m31, m32, m33)
      val t01 = -determinant3x3(m10, m12, m13, m20, m22, m23, m30, m32, m33)
      val t02 =  determinant3x3(m10, m11, m13, m20, m21, m23, m30, m31, m33)
      val t03 = -determinant3x3(m10, m11, m12, m20, m21, m22, m30, m31, m32)
      // second row
      val t10 = -determinant3x3(m01, m02, m03, m21, m22, m23, m31, m32, m33)
      val t11 =  determinant3x3(m00, m02, m03, m20, m22, m23, m30, m32, m33)
      val t12 = -determinant3x3(m00, m01, m03, m20, m21, m23, m30, m31, m33)
      val t13 =  determinant3x3(m00, m01, m02, m20, m21, m22, m30, m31, m32)
      // third row
      val t20 =  determinant3x3(m01, m02, m03, m11, m12, m13, m31, m32, m33)
      val t21 = -determinant3x3(m00, m02, m03, m10, m12, m13, m30, m32, m33)
      val t22 =  determinant3x3(m00, m01, m03, m10, m11, m13, m30, m31, m33)
      val t23 = -determinant3x3(m00, m01, m02, m10, m11, m12, m30, m31, m32)
      // fourth row
      val t30 = -determinant3x3(m01, m02, m03, m11, m12, m13, m21, m22, m23)
      val t31 =  determinant3x3(m00, m02, m03, m10, m12, m13, m20, m22, m23)
      val t32 = -determinant3x3(m00, m01, m03, m10, m11, m13, m20, m21, m23)
      val t33 =  determinant3x3(m00, m01, m02, m10, m11, m12, m20, m21, m22)

      // transpose and divide by the determinant
      result.m00 = t00*determinant_inv
      result.m11 = t11*determinant_inv
      result.m22 = t22*determinant_inv
      result.m33 = t33*determinant_inv
      result.m01 = t10*determinant_inv
      result.m10 = t01*determinant_inv
      result.m20 = t02*determinant_inv
      result.m02 = t20*determinant_inv
      result.m12 = t21*determinant_inv
      result.m21 = t12*determinant_inv
      result.m03 = t30*determinant_inv
      result.m30 = t03*determinant_inv
      result.m13 = t31*determinant_inv
      result.m31 = t13*determinant_inv
      result.m32 = t23*determinant_inv
      result.m23 = t32*determinant_inv
    } else throw new Exception
  }

  /** Negate this matrix in-place. */
  def negate() {
    m00 = -m00; m01 = -m01; m02 = -m02; m03 = -m03
    m10 = -m10; m11 = -m11; m12 = -m12; m13 = -m13
    m20 = -m20; m21 = -m21; m22 = -m22; m23 = -m23
    m30 = -m30; m31 = -m31; m32 = -m32; m33 = -m33
  }

  /** Negate this matrix and store in a new matrix. */
  def unary_- = Mat4(
    -m00, -m01, -m02, -m03,
    -m10, -m11, -m12, -m13,
    -m20, -m21, -m22, -m23,
    -m30, -m31, -m32, -m33
  )

  /** Negate this matrix and store in the given matrix. */
  def negate(result: Mat4) {
    result.m00 = -m00; result.m01 = -m01; result.m02 = -m02; result.m03 = -m03
    result.m10 = -m10; result.m11 = -m11; result.m12 = -m12; result.m13 = -m13
    result.m20 = -m20; result.m21 = -m21; result.m22 = -m22; result.m23 = -m23
    result.m30 = -m30; result.m31 = -m31; result.m32 = -m32; result.m33 = -m33
  }

  def toLongString = {
    """[ %+12.8f %+12.8f %+12.8f %+12.8f ]
      |[ %+12.8f %+12.8f %+12.8f %+12.8f ]
      |[ %+12.8f %+12.8f %+12.8f %+12.8f ]
      |[ %+12.8f %+12.8f %+12.8f %+12.8f ]
    """.stripMargin.format( m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33 )
  }

  override def toString = s"{{$m00, $m01, $m02, $m03}, {$m10, $m11, $m12, $m13}, {$m20, $m21, $m22, $m23}, {$m30, $m31, $m32, $m33}}"

  def scale(scale: Double): Mat4 =
    geometry.scale(this, scale)

  def scale(scale: Vec3): Mat4 =
    geometry.scale(this, scale)

  def scale(scale: Vec3, result: Mat4) =
    geometry.scale(this, scale, result)

  def scale(scale: Double, result: Mat4) =
    geometry.scale(this, scale, result)

  def scale(scaleX: Double, scaleY: Double, scaleZ: Double, result: Mat4) =
    geometry.scale(this, scaleX, scaleY, scaleZ, result)

  def rotate(angle: Double, axis: Vec3): Mat4 =
    geometry.rotate(this, angle, axis)

  def rotate(angle: Double, axis: Vec3, result: Mat4) =
    geometry.rotate(this, angle, axis, result)

  def translate( offset: Vec3 ): Mat4 =
    geometry.translate(this, offset)

  def translate( offset: Vec3, result: Mat4 ) =
    geometry.translate(this, offset, result)

}

object Mat4 {

  /**
   * The identity matrix:
   * <pre>
   *   [1 0 0 0]
   *   [0 1 0 0]
   *   [0 0 1 0]
   *   [0 0 0 1]
   * </pre>
   */
  def identity() = Mat4(
    1, 0, 0, 0,
    0, 1, 0, 0,
    0, 0, 1, 0,
    0, 0, 0, 1
  )

  /**
   * The zero matrix:
   * <pre>
   *   [0 0 0 0]
   *   [0 0 0 0]
   *   [0 0 0 0]
   *   [0 0 0 0]
   * </pre>
   */
  def zero()= Mat4(
    0, 0, 0, 0,
    0, 0, 0, 0,
    0, 0, 0, 0,
    0, 0, 0, 0
  )

  /**
   * Construct a matrix from a set of column-vectors
   * @param c0 Vector containing the elements for column 0
   * @param c1 Vector containing the elements for column 1
   * @param c2 Vector containing the elements for column 2
   * @param c3 Vector containing the elements for column 3
   */
  def columns( c0: Vec4, c1: Vec4, c2: Vec4, c3: Vec4 ) = Mat4(
    c0.x, c1.x, c2.x, c3.x,
    c0.y, c1.y, c2.y, c3.y,
    c0.z, c1.z, c2.z, c3.z,
    c0.w, c1.w, c2.w, c3.w
  )

  /**
   * Construct a matrix from a set of row-vectors
   * @param r0 Vector containing the elements for row 0
   * @param r1 Vector containing the elements for row 1
   * @param r2 Vector containing the elements for row 2
   * @param r3 Vector containing the elements for row 3
   */
  def rows( r0: Vec4, r1: Vec4, r2: Vec4, r3: Vec4 ) = Mat4(
    r0.x, r0.y, r0.z, r0.w,
    r1.x, r1.y, r1.z, r1.w,
    r2.x, r2.y, r2.z, r2.w,
    r3.x, r3.y, r3.z, r3.w
  )

  /**
   * Construct a matrix with a constant on the diagonal:
   * <pre>
   *   [s 0 0 0]
   *   [0 s 0 0]
   *   [0 0 s 0]
   *   [0 0 0 s]
   * </pre>
   * @param s The constant on the diagonal.
   */
  def diag( s: Double ) = Mat4(
    s, 0, 0, 0,
    0, s, 0, 0,
    0, 0, s, 0,
    0, 0, 0, s
  )

  /**
   * Construct a matrix where the elements on the diagonal originate from a matrix:
   * <pre>
   *   [x 0 0 0]
   *   [0 y 0 0]
   *   [0 0 z 0]
   *   [0 0 0 w]
   * </pre>
   * @param v Vector from which to copy the elements.
   */
  def diag( v: Vec4 ) = Mat4(
    v.x, 0,  0,  0,
     0, v.y, 0,  0,
     0,  0, v.z, 0,
     0,  0,  0, v.w
  )

  /**
   * Returns the identity matrix
   * @see Mat4.identity()
   */
  def apply(): Mat4 =
    identity()

  /**
   * Constructs a matrix where each element is a fixed constant:
   * <pre>
   *   [s s s s]
   *   [s s s s]
   *   [s s s s]
   *   [s s s s]
   * </pre>
   * @param s Constant to fill the matrix with.
   */
  def apply(s: Double): Mat4 =
    Mat4(s, s, s, s, s, s, s, s, s, s, s, s, s, s, s, s)

  /**
   * Constuct a matrix from its elements
   * <pre>
   *   [m00 m01 m02 m03]
   *   [m10 m11 m12 m13]
   *   [m20 m21 m22 m23]
   *   [m30 m31 m32 m33]
   * </pre>
   */
  def apply(m00: Double, m01: Double, m02: Double, m03: Double,
            m10: Double, m11: Double, m12: Double, m13: Double,
            m20: Double, m21: Double, m22: Double, m23: Double,
            m30: Double, m31: Double, m32: Double, m33: Double) =
    new Mat4( m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33 )

  /**
   * Create a copy of an existing matrix.
   * @param mtx Matrix to clone.
   */
  def apply( mtx: Mat4 ): Mat4 = Mat4(
    mtx.m00, mtx.m01, mtx.m02, mtx.m03,
    mtx.m10, mtx.m11, mtx.m12, mtx.m13,
    mtx.m20, mtx.m21, mtx.m22, mtx.m23,
    mtx.m30, mtx.m31, mtx.m32, mtx.m33
  )

  /**
   * Create a copy of an existing matrix, filling the remaining elments according to the identity matrix.
   */
  def apply( mtx: Mat3 ): Mat4 = Mat4(
    mtx.m00, mtx.m01, mtx.m02,  0,
    mtx.m10, mtx.m11, mtx.m12,  0,
    mtx.m20, mtx.m21, mtx.m22,  0,
       0,       0,       0,     1
  )

  /**
   * Create a affine transformation matrix that translates by the given deltas.
   */
  def translate( dx: Double, dy: Double, dz: Double ): Mat4 = Mat4(
    1,  0,  0,   0,
    0,  1,  0,   0,
    0,  0,  1,   0,
    dx, dy, dz,  1
  )

  /**
   * Create a affine transformation matrix that translates by the given vector.
   */
  def translate( v: Vec3 ): Mat4 =
    translate( v.x, v.y, v.z )

  /**
   * Create a affine transformation matrix that rotates according to the given axis-angle rotation.
   */
  def rotate( angle: Double, axis: Vec3 ) = Mat4( Mat3.rotate(angle, axis) )

  /**
   * Create a affine transformation matrix that rotates around the X-axis.
   */
  def rotateX( angle: Double ) = Mat4( Mat3.rotateX(angle) )

  /**
   * Create a affine transformation matrix that rotates around the Y-axis.
   */
  def rotateY( angle: Double ) = Mat4( Mat3.rotateY(angle) )

  /**
   * Create a affine transformation matrix that rotates around the Z-axis.
   */
  def rotateZ( angle: Double ) = Mat4( Mat3.rotateZ(angle) )

  /**
   * Create a affine transformation matrix that scales by the given scalar.
   */
  def scale( s: Double ): Mat4 = scale( Vec3(s, s, s) )

  /**
   * Create a affine transformation matrix that scales by the given vector.
   */
  def scale( s: Vec3 ): Mat4 = Mat4(
    s.x, 0,   0,   0,
    0,   s.y, 0,   0,
    0,   0,   s.z, 0,
    0,   0,   0,   1
  )

}

