package be.angelcorp.celest.math.rotation

trait Rotation {

	def applyInverseTo(rotation2: Rotation): Rotation

  def applyInverseTo(vector: Array[Double]): Array[Double]

  def applyTo(rotation2: Rotation): Rotation

  def applyTo(vector: Array[Double]): Array[Double]

  def inverse(): Rotation

  def toQuaternion: spire.math.Quaternion[Double]

  def toMatrix: RotationMatrix

}
