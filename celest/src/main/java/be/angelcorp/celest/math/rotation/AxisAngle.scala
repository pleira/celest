package be.angelcorp.celest.math.rotation

import be.angelcorp.celest.math.geometry.{Mat3, Vec3}

class AxisAngle( val axis: Vec3, val angle: Double ) extends Rotation {

	override def applyInverseTo(rotation2: Rotation) = toQuaternion.applyInverseTo(rotation2)

	override def applyInverseTo(vector: Vec3) = {
		val negativeAngle = -angle

		val a = vector * scala.math.cos(negativeAngle)
    val b = (axis * vector) * negativeAngle
    val c = axis * ( axis.dot(vector) * (1 - scala.math.cos(negativeAngle)) ).toDouble
		a + b + c
	}

	override def applyTo(rotation2: Rotation) = toQuaternion.applyTo(rotation2)

	override def applyTo(vector: Vec3) = {
    val a = vector * scala.math.cos(angle)
    val b = (axis * vector) * angle
    val c = axis * ( axis.dot(vector) * (1 - scala.math.cos(angle)) ).toDouble
    a + b + c
	}

  override def inverse() =
    toQuaternion.inverse()

  override def toQuaternion =
    Quaternion(axis, angle)

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
  def apply(q: Quaternion) =
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
    val axis = Vec3(x, y, z)

    val cos = (mtx.m00 + mtx.m11 + mtx.m22 - 1) * 0.5
    val sin = 0.5 * Math.sqrt(x * x + y * y + z * z)
    val angle = Math.atan2(sin, cos)
    new AxisAngle(axis, angle.toDouble)
  }

}
