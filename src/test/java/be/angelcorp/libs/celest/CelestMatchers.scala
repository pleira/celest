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

	sealed abstract class VectorTolerance {
		def check(left: Double, right: Double): Boolean
	}
	case class VectorEqualTolerance() extends VectorTolerance {
		override def check(left: Double, right: Double) = Precision.equals(left, right)
		override def toString = "0"
	}
	case class VectorDoubleTolerance(eps: Double)   extends VectorTolerance {
		override def check(left: Double, right: Double) = Precision.equals(left, right, eps)
		override def toString = eps.toString
	}

	def <[T <% Ordered[T]](right: T): Matcher[T] =
		new Matcher[T] {
			def apply(left: T): MatchResult =
				MatchResult(
					left < right,
					"wasNotLessThan",
					"wasLessThan"
				)
		}

	def equalVector(right: Vector3D, tolerance: VectorTolerance): Matcher[Vector3D] =
		new Matcher[Vector3D] {
			def apply(left: Vector3D) = {
				val (matches, errors) = left.zip(right).zipWithIndex.foldLeft( (true, "") )( (previous, entries) =>
					tolerance.check( entries._1._1, entries._1._2 ) match {
						case true  => previous
						case false => (false, previous._2 + "\nIndex %d not equal: %16.16f <> %16.16f within %s".format(entries._2, entries._1._1, entries._1._2, tolerance))
				} )
				MatchResult(
					matches,
					"the vector" + right + " is not equal to " + left + " within absolute tolerance " + tolerance + errors,
					"the vector" + right + " is equal to " + left + " within absolute tolerance " + tolerance + errors
				)
			}
		}
	def vector(right: Vector3D):                      Matcher[Vector3D] = equalVector(right, VectorEqualTolerance())
	def vector(right: Vector3D, tolerance: Double):   Matcher[Vector3D] = equalVector(right, VectorDoubleTolerance(tolerance))

	def assertVectorEqual(left: Vector3D, right: Vector3D) =
		vector(right).apply(left) match {
			case MatchResult(success, failureMessage, n, m, mn) => assert(success, failureMessage)
		}
	def assertVectorEqual(left: Vector3D, right: Vector3D, tolerance: Double) =
		vector(right, tolerance).apply(left) match {
			case MatchResult(success, failureMessage, n, m, mn) => assert(success, failureMessage)
		}
}

object CelestMatchers extends CelestMatchers
