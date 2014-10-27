package be.angelcorp.celest.math

package object geometry {

  /**
   * Return a vector pointing in the same direction as another
   * @param N Specifies the vector to orient.
   * @param I Specifies the incident vector.
   * @param Nref Specifies the reference vector.
   * @see http://www.opengl.org/sdk/docs/man4/html/faceforward.xhtml
   */
  def faceforward(N: Vec2, I: Vec2, Nref: Vec2) =
    if (Nref.dot(I) < 0) N else -N

  /**
   * Return a vector pointing in the same direction as another
   * @param N Specifies the vector to orient.
   * @param I Specifies the incident vector.
   * @param Nref Specifies the reference vector.
   * @see http://www.opengl.org/sdk/docs/man4/html/faceforward.xhtml
   */
  def faceforward(N: Vec3, I: Vec3, Nref: Vec3) =
    if (Nref.dot(I) < 0) N else -N

  /**
   * Return a vector pointing in the same direction as another
   * @param N Specifies the vector to orient.
   * @param I Specifies the incident vector.
   * @param Nref Specifies the reference vector.
   * @see http://www.opengl.org/sdk/docs/man4/html/faceforward.xhtml
   */
  def faceforward(N: Vec4, I: Vec4, Nref: Vec4) =
    if (Nref.dot(I) < 0) N else -N

  def reflect(I: Vec2, N: Vec2) =
    I - (N * N.dot(I) * 2.0)

  def reflect(I: Vec3, N: Vec3) =
    I - (N * N.dot(I) * 2.0)

  def reflect(I: Vec4, N: Vec4) =
    I - (N * N.dot(I) * 2.0)

  def refract(I: Vec2, N: Vec2, eta: Double) = {
    val dotValue = N dot I
    val k = 1.0 - eta * eta * (1.0 - dotValue * dotValue)
    if(k < 0)
      0
    else
      (I * eta) - (N * (eta * dotValue + scala.math.sqrt(k)) )
  }

  def refract(I: Vec3, N: Vec3, eta: Double) = {
    val dotValue = N dot I
    val k = 1.0 - eta * eta * (1.0 - dotValue * dotValue)
    if(k < 0)
      0
    else
      (I * eta) - (N * (eta * dotValue + scala.math.sqrt(k)))
  }

  def refract(I: Vec4, N: Vec4, eta: Double) = {
    val dotValue = N dot I
    val k = 1.0 - eta * eta * (1.0 - dotValue * dotValue)
    if(k < 0)
      0
    else
      (I * eta) - (N * (eta * dotValue + scala.math.sqrt(k)))
  }

  /** Apply additional scaling to a 3D transformation */
  def scale(mtx: Mat4, scale: Double): Mat4 = {
    val result = Mat4(mtx)
    this.scale(mtx, scale, scale, scale, result)
    result
  }

  /** Apply additional scaling to a 3D transformation */
  def scale(mtx: Mat4, scale: Vec3): Mat4 = {
    val result = Mat4(mtx)
    this.scale(mtx, scale.x, scale.y, scale.z, result)
    result
  }

  /** Apply additional scaling to a 3D transformation */
  def scale(mtx: Mat4, scale: Vec3, result: Mat4) {
    this.scale(mtx, scale.x, scale.y, scale.z, result)
  }

  /** Apply additional scaling to a 3D transformation */
  def scale(mtx: Mat4, scale: Double, result: Mat4) {
    this.scale(mtx, scale, scale, scale, result)
  }

  /** Apply additional scaling to a 3D transformation */
  def scale(mtx: Mat4, scaleX: Double, scaleY: Double, scaleZ: Double, result: Mat4) {
    result.m00 = mtx.m00 * scaleX
    result.m01 = mtx.m01 * scaleX
    result.m02 = mtx.m02 * scaleX
    result.m03 = mtx.m03 * scaleX
    result.m10 = mtx.m10 * scaleY
    result.m11 = mtx.m11 * scaleY
    result.m12 = mtx.m12 * scaleY
    result.m13 = mtx.m13 * scaleY
    result.m20 = mtx.m20 * scaleZ
    result.m21 = mtx.m21 * scaleZ
    result.m22 = mtx.m22 * scaleZ
    result.m23 = mtx.m23 * scaleZ
  }

  /** Apply additional scaling to a 2D transformation */
  def scale(mtx: Mat3, scale: Double): Mat3 = {
    val result = Mat3(mtx)
    this.scale(mtx, scale, scale, result)
    result
  }

  /** Apply additional scaling to a 2D transformation */
  def scale(mtx: Mat3, scale: Vec2): Mat3 = {
    val result = Mat3(mtx)
    this.scale(mtx, scale.x, scale.y, result)
    result
  }

  /** Apply additional scaling to a 2D transformation */
  def scale(mtx: Mat3, scale: Vec2, result: Mat3) {
    this.scale(mtx, scale.x, scale.y, result)
  }

  /** Apply additional scaling to a 2D transformation */
  def scale(mtx: Mat3, scale: Double, result: Mat3) {
    this.scale(mtx, scale, scale, result)
  }

  /** Apply additional scaling to a 2D transformation */
  def scale(mtx: Mat3, scaleX: Double, scaleY: Double, result: Mat3) {
    result.m00 = mtx.m00 * scaleX
    result.m01 = mtx.m01 * scaleX
    result.m02 = mtx.m02 * scaleX
    result.m10 = mtx.m10 * scaleY
    result.m11 = mtx.m11 * scaleY
    result.m12 = mtx.m12 * scaleY
  }

  /** Apply additional rotation to a 3D transformation */
  def rotate(mtx: Mat4, angle: Double, axis: Vec3): Mat4 = {
    val result = Mat4(mtx)
    rotate(mtx, angle, axis, result)
    result
  }

  /** Apply additional rotation to a 3D transformation */
  def rotate(mtx: Mat4, angle: Double, axis: Vec3, result: Mat4) {
    val rot = Mat3.rotate( angle, axis )
    val t00 = mtx.m00 * rot.m00 + mtx.m10 * rot.m01 + mtx.m20 * rot.m02
    val t01 = mtx.m01 * rot.m00 + mtx.m11 * rot.m01 + mtx.m21 * rot.m02
    val t02 = mtx.m02 * rot.m00 + mtx.m12 * rot.m01 + mtx.m22 * rot.m02
    val t03 = mtx.m03 * rot.m00 + mtx.m13 * rot.m01 + mtx.m23 * rot.m02
    val t10 = mtx.m00 * rot.m10 + mtx.m10 * rot.m11 + mtx.m20 * rot.m12
    val t11 = mtx.m01 * rot.m10 + mtx.m11 * rot.m11 + mtx.m21 * rot.m12
    val t12 = mtx.m02 * rot.m10 + mtx.m12 * rot.m11 + mtx.m22 * rot.m12
    val t13 = mtx.m03 * rot.m10 + mtx.m13 * rot.m11 + mtx.m23 * rot.m12
    result.m20 = mtx.m00 * rot.m20 + mtx.m10 * rot.m21 + mtx.m20 * rot.m22
    result.m21 = mtx.m01 * rot.m20 + mtx.m11 * rot.m21 + mtx.m21 * rot.m22
    result.m22 = mtx.m02 * rot.m20 + mtx.m12 * rot.m21 + mtx.m22 * rot.m22
    result.m23 = mtx.m03 * rot.m20 + mtx.m13 * rot.m21 + mtx.m23 * rot.m22
    result.m00 = t00
    result.m01 = t01
    result.m02 = t02
    result.m03 = t03
    result.m10 = t10
    result.m11 = t11
    result.m12 = t12
    result.m13 = t13
  }

  /** Apply additional rotation to a 2D transformation */
  def rotate(mtx: Mat3, angle: Double): Mat3 = {
    val result = Mat3(mtx)
    rotate(mtx, angle, result)
    result
  }

  /** Apply additional rotation to a 2D transformation */
  def rotate(mtx: Mat3, angle: Double, result: Mat3) {
    val c = scala.math.cos(angle)
    val s = scala.math.sin(angle)
    val t00 = mtx.m00 * (+c) + mtx.m10 * (+s)
    val t01 = mtx.m01 * (+c) + mtx.m11 * (+s)
    val t02 = mtx.m02 * (+c) + mtx.m12 * (+s)
    val t10 = mtx.m00 * (-s) + mtx.m10 * (+c)
    val t11 = mtx.m01 * (-s) + mtx.m11 * (+c)
    val t12 = mtx.m02 * (-s) + mtx.m12 * (+c)
    result.m20 = mtx.m20
    result.m21 = mtx.m21
    result.m22 = mtx.m22
    result.m00 = t00
    result.m01 = t01
    result.m02 = t02
    result.m10 = t10
    result.m11 = t11
    result.m12 = t12
  }

  /** Apply additional translation to a 3D transformation */
  def translate( mtx: Mat4, offset: Vec3 ): Mat4 = {
    val result = Mat4(mtx)
    translate(mtx, offset, result)
    result
  }

  /** Apply additional translation to a 3D transformation */
  def translate( mtx: Mat4, offset: Vec3, result: Mat4 ) {
    result.m30 += mtx.m00 * offset.x + mtx.m10 * offset.y + mtx.m20 * offset.z
    result.m31 += mtx.m01 * offset.x + mtx.m11 * offset.y + mtx.m21 * offset.z
    result.m32 += mtx.m02 * offset.x + mtx.m12 * offset.y + mtx.m22 * offset.z
    result.m33 += mtx.m03 * offset.x + mtx.m13 * offset.y + mtx.m23 * offset.z
  }

  /** Apply additional translation to a 2D transformation */
  def translate( mtx: Mat3, offset: Vec2 ): Mat3 = {
    val result = Mat3(mtx)
    translate(mtx, offset, result)
    result
  }

  /** Apply additional translation to a 2D transformation */
  def translate( mtx: Mat3, offset: Vec2, result: Mat3 ) {
    result.m20 += mtx.m00 * offset.x + mtx.m10 * offset.y
    result.m21 += mtx.m01 * offset.x + mtx.m11 * offset.y
    result.m22 += mtx.m02 * offset.x + mtx.m12 * offset.y
  }


}
