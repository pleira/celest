package be.angelcorp.celest.math.rotation

import be.angelcorp.celest.math.geometry.Vec3

trait Rotation {

	def applyInverseTo(rotation2: Rotation): Rotation

  def applyInverseTo(vector: Vec3): Vec3

  def applyTo(rotation2: Rotation): Rotation

  def applyTo(vector: Vec3): Vec3

  def inverse(): Rotation

  def toQuaternion: Quaternion

  def toMatrix: RotationMatrix

}
