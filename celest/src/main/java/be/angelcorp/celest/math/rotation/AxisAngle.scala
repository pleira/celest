package be.angelcorp.celest.math.rotation

import be.angelcorp.celest.math.geometry.Mat3
import spire.algebra._   // provides algebraic type classes
import spire.math._      // provides functions, types, and type classes
import spire.implicits._ // provides infix operators, instances and conversions
import be.angelcorp.celest.math.geometry.PowerArray._


class AxisAngle( val axis: Array[Double], val angle: Double ) extends Rotation {

	override def applyInverseTo(rotation2: Rotation) = toQuaternion.applyInverseTo(rotation2)

	override def applyInverseTo(vector: Array[Double]) = {
		val negativeAngle = -angle

		val a = vector :* cos(negativeAngle)
    val b = (axis * vector) :* negativeAngle
    val c = axis :* ( axis.dot(vector) * (1 - cos(negativeAngle)) ).toDouble
		a + b + c
	}
  
	override def applyTo(rotation2: Rotation) = toQuaternion.applyTo(rotation2)

	override def applyTo(vector: Array[Double]) = {
    val a = vector :* cos(angle)
    val b = (axis * vector) :* angle
    val c = axis :* ( axis.dot(vector) * (1 - cos(angle)) ).toDouble
    a + b + c
	}

  override def inverse() = {
    val q = toQuaternion
    - q.real + q.pure
  }
  
  override def toQuaternion = { 
    implicit val ev = CoordinateSpace.array[Double](3)
    AxisAngle.buildQuaternion(axis, angle)
  }

  override def toMatrix =
    RotationMatrix(this)

}

object AxisAngle {

  /**
   * Convert an arbitrary {@link IRotation} to an {@link be.angelcorp.omicron.math.rotation.AxisAngle} format
   */
  def apply(r: Rotation): AxisAngle = apply(r.toQuaternion)

  /**
   * Convert a rotation in {@link Quaternion} format to the {@link be.angelcorp.omicron.math.rotation.AxisAngle} format
   */
  def apply(q: Quaternion[Double]) =
    new AxisAngle(q.axis, q.angle.toDouble)

  /**
   * Convert a rotation in {@link RotationMatrix} format to the {@link be.angelcorp.omicron.math.rotation.AxisAngle} format
   */
  def apply(rotation: RotationMatrix): AxisAngle =
    apply( rotation.mtx )
  
  def apply(mtx: Mat3): AxisAngle = {
    val x = mtx.m21 - mtx.m12
    val y = mtx.m02 - mtx.m20
    val z = mtx.m10 - mtx.m01
    val axis = Array[Double](x, y, z)

    val cos = (mtx.m00 + mtx.m11 + mtx.m22 - 1) * 0.5
    val sin = 0.5 * Math.sqrt(x * x + y * y + z * z)
    val angle = Math.atan2(sin, cos)
    new AxisAngle(axis, angle.toDouble)
  }
  
  /** Build a rotation from an axis and an angle. */
  def buildQuaternion(axis: Array[Double], angle: Double)(implicit ev: CoordinateSpace[Array[Double], Double]): Quaternion[Double] = {
    val norm = axis.length
    val halfAngle = -0.5 * angle
    val coeff = sin(halfAngle) / norm

    val q0 = cos(halfAngle)
    val q1 = coeff * axis._x
    val q2 = coeff * axis._y
    val q3 = coeff * axis._z
    Quaternion(q0, q1, q2, q3)
  }
  
}
