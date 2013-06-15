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
package be.angelcorp.libs.celest.kepler

import be.angelcorp.libs.celest.state.positionState.IKeplerElements
import math._

class KeplerCircular(k: IKeplerElements) extends KeplerEllipse(k) {

	def arealVel(µ: Double, a: Double) = sqrt(µ * a) / 2
	override def arealVel(µ: Double, a: Double, e: Double) = arealVel(µ, a)

	/**
	 * Areal velocity in function of Semi-major axis and the Mean motion
	 * 
	 * @param a Semi-major axis
	 * @param n Mean motion
	 * @return Areal velocity \dot{A}
	 */
	def arealVelFromMeanMotion(a: Double, n: Double)  = a * a * n / 2

	override def semiLatusRectum() = k.getSemiMajorAxis
	override def getApocenter()    = k.getSemiMajorAxis
	override def getPericenter()   = k.getSemiMajorAxis

	def perifocalDistance(a: Double) = a

	override def period(n: Double) = 2 * Pi / n

	override def totEnergyPerMass(µ: Double, a: Double) = -µ / (2 * a)

	override def visViva(r: Double) = k.getCenterbody.getMu / r

}

object KeplerCircular {

	def vc(r: Double, µ: Double) = sqrt(µ / r)

}
