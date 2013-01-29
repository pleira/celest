/**
 * Copyright (C) 2012 Simon Billemont <simon@angelcorp.be>
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

package be.angelcorp.libs.celest

import org.scalatest.matchers._
import be.angelcorp.libs.math.linear.Vector3D
import org.scalatest.FailureMessages
import org.apache.commons.math3.util.Precision

trait CelestMatchers {

	sealed abstract class VectorTolerance
	case class VectorEqualTolerance() extends VectorTolerance
	{ override def toString = "0" }
	case class VectorDoubleTolerance(eps: Double)   extends VectorTolerance
	{ override def toString = eps.toString }
	case class VectorVectorTolerance(eps: Vector3D) extends VectorTolerance
	{ override def toString = eps.toString }

	def <[T <% Ordered[T]](right: T): Matcher[T] =
		new Matcher[T] {
			def apply(left: T): MatchResult =
				MatchResult(
					left < right,
					"wasNotLessThan",
					"wasLessThan"
				)
		}

	def equalVector[T](right: Vector3D, tolerance: VectorTolerance): Matcher[T] =
		new Matcher[T] {
			def apply(left: T): MatchResult = {
				val matches = tolerance match {
					case VectorEqualTolerance() => false
						//Precision.equals(left.x, right.x) &&
						//Precision.equals(left.y, right.y) &&
						//Precision.equals(left.z, right.z)
					case VectorDoubleTolerance(eps) => false
						//Precision.equals(left.x, right.x, eps) &&
						//Precision.equals(left.y, right.y, eps) &&
						//Precision.equals(left.z, right.z, eps)
					case VectorVectorTolerance(eps) => false
						//Precision.equals(left.x, right.x, eps.x) &&
						//Precision.equals(left.y, right.y, eps.y) &&
						//Precision.equals(left.z, right.z, eps.z)
				}
				MatchResult(
					matches,
					"the vector" + right + " is not equal to " + left + " within absolute tolerance " + tolerance,
					"the vector" + right + " is equal to " + left + " within absolute tolerance " + tolerance
				)
			}
		}
	def equalVector(right: Vector3D):                      Matcher[Vector3D] = equalVector(right, VectorEqualTolerance())
	def equalVector(right: Vector3D, tolerance: Double):   Matcher[Vector3D] = equalVector(right, VectorDoubleTolerance(tolerance))
	def equalVector(right: Vector3D, tolerance: Vector3D): Matcher[Vector3D] = equalVector(right, VectorVectorTolerance(tolerance))

}

object CelestMatchers extends CelestMatchers
