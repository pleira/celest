/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *        http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.celest.math.rotation

import be.angelcorp.celest.math.geometry.{Vec3, Mat3}

class RotationMatrix(val mtx: Mat3) extends Rotation {

	/**
	 * Converts the given rotation to matrix form and applies the following:
	 * <pre>
	 * result = rotation2 &times; this<sup>T</sup>
	 * </pre>
	 * @param rotation2 Other rotation to applies the inverse of this transform to.
	 */
	override def applyInverseTo(rotation2: Rotation): Rotation = new RotationMatrix( rotation2.toMatrix.mtx dot mtx.transpose )

	/**
	 * Applies the following:
	 * <pre>
	 * result = this<sup>T</sup> &times; vector
	 * </pre>
	 * @param vector Vector to apply the inverse of this transform to.
	 */
	override def applyInverseTo(vector: Vec3) = mtx.transpose * vector

	/**
	 * Converts the given rotation to matrix form and applies the following:
	 * <pre>
	 * result = rotation2 &times; this
	 * </pre>
	 * @param rotation2 Other rotation to applies this rotation to.
	 */
	override def applyTo(rotation2: Rotation): Rotation = new RotationMatrix( rotation2.toMatrix.mtx dot mtx )

	/**
	 * Applies the following:
	 * <pre>
	 * result = this &times; vector
	 * </pre>
	 * @param vector Vector on which to apply this transform.
	 */
	override def applyTo(vector: Vec3) = mtx * vector

	override def inverse(): RotationMatrix = mtx.transpose

	override def toMatrix = this
	override def toQuaternion = Quaternion(mtx)

  implicit def toMat3: Mat3 = mtx

}

object RotationMatrix {

  implicit def fromMat3( mtx: Mat3 ): RotationMatrix = new RotationMatrix( mtx )

	/**
	 * Create a {@link RotationMatrix} from a rotation in {@link AxisAngle} format.
	 * <p>
	 * Based on:
	 * http://www.euclideanspace.com/maths/geometry/rotations/conversions/angleToMatrix/index.htm
	 * </p>
	 *
	 * @param aa AxisAngle to convert.
	 */
	def apply(aa: AxisAngle): RotationMatrix = {
		val c = scala.math.cos(aa.angle)
		val s = scala.math.sin(aa.angle)
		val t = 1.0 - c
		val axis = aa.axis.normalized

		val m00 = c + axis.x * axis.x * t
		val m11 = c + axis.y * axis.y * t
		val m22 = c + axis.z * axis.z * t
		val tmp1 = axis.x * axis.y * t
		val tmp2 = axis.z * s
		val m10 = tmp1 + tmp2
		val m01 = tmp1 - tmp2
		val tmp3 = axis.x * axis.z * t
		val tmp4 = axis.y * s
		val m20 = tmp3 - tmp4
		val m02 = tmp3 + tmp4
		val tmp5 = axis.y * axis.z * t
		val tmp6 = axis.x * s
		val m21 = tmp5 + tmp6
		val m12 = tmp5 - tmp6
		new RotationMatrix( Mat3(m00.toFloat, m01.toFloat, m02.toFloat, m10.toFloat, m11.toFloat, m12.toFloat, m20.toFloat, m21.toFloat, m22.toFloat) )
	}

	/**
	 * Convert an arbitrary {@link Rotation} to a {@link RotationMatrix}.
	 * @param r Rotation to transform.
	 */
	def apply(r: Rotation): RotationMatrix = r.toMatrix

	/**
	 * Convert a {@link Quaternion} rotation to a {@link RotationMatrix}.
	 * @param q Quaternion to convert to a rotation matrix.
	 */
	def apply(q: Quaternion): RotationMatrix = q.toMatrix

}
