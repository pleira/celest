/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.celest.state

import be.angelcorp.celest.math.geometry.Vec3

import math._
import be.angelcorp.celest.frameGraph.ReferenceSystem

/**
 * Spherical elements are basically the length of the radius r and velocity vector v, and its orientation
 * with some right ascension &alpha;/flight path azimuth &psi; and declination &delta;/flight path angle
 * &gamma;
 *
 * <pre>
 * Elements: { r,    &alpha;,     &delta;,     V,     &gamma;,     &psi;  }
 * Units:    {[m], [rad], [rad], [m/s], [rad], [rad]}
 * </pre>
 *
 * <p>
 * This is similar to ADBARV {&alpha;,&delta;,&beta;,r,&psi;,v} elements where &beta; = 90 - &gamma;
 * </p>
 *
 * @param r Distance from the center of the orbited object to the satellite [m],
 * @param α Right ascension [rad],
 * @param δ Declination [rad],
 * @param v Instantaneous velocity of the satellite wrt the orbited body [m/s],
 * @param γ Flight path angle, in orbit plane angle between the radius vector and velocity vector [rad],
 * @param ψ Flight path azimuth, out of plane angle of the velocity vector [rad],
 * @param frame Frame in which this set of elements is defined.
 *
 * @author Simon Billemont
 */
class Spherical[F <: ReferenceSystem]
(val r: Double,
 val α: Double,
 val δ: Double,
 val v: Double,
 val γ: Double,
 val ψ: Double,
 val frame: F) extends Orbit[F] {

  def radius = r

  def rightAscension = α

  def declination = δ

  def velocity = v

  def flightPathAngle = γ

  def flightPathAzimuth = ψ

  def toPosVel = {
    // See http://www.cdeagle.com/omnum/pdf/csystems.pdf
    // Orbital Mechanics with Numerit: Astrodynamic Coordinates
    val cosD = cos(δ)
    val sinD = sin(δ)
    val cosAl = cos(α)
    val sinAl = sin(α)
    val cosA = cos(ψ)
    val sinA = sin(ψ)
    val cosG = cos(γ)
    val sinG = sin(γ)

    val R = Vec3(
      r * cosD * cosAl,
      r * cosD * sinAl,
      r * sinD)
    val V = Vec3(
      v * (cosAl * (-cosA * cosG * sinD + sinG * cosD) - sinA * cosG * sinAl),
      v * (sinAl * (-cosA * cosG * sinD + sinG * cosD) + sinA * cosG * cosAl),
      v * (cosA * cosG * cosD + sinG * cosD))

    new PosVel(R, V, frame)
  }

}

object Spherical {

  /**
   * Create a set of [[be.angelcorp.celest.state.Spherical]] from another
   * [[be.angelcorp.celest.state.Orbit]]. Chooses itself what the best way of converting is.
   *
   * @param orbit Orbit to convert
   */
  def apply[F <: ReferenceSystem](orbit: Orbit[F]): Spherical[F] = orbit match {
    case s: Spherical[F] => s
    case o => Spherical(o.toPosVel)
  }

  /**
   * Convert [[be.angelcorp.celest.state.PosVel]] to [[be.angelcorp.celest.state.Spherical]] elements.
   *
   * @param posvel Cartesian elements to convert [m] and [m/s]
   */
  def apply[F <: ReferenceSystem](posvel: PosVel[F]): Spherical[F] = {
    // See http://www.cdeagle.com/omnum/pdf/csystems.pdf
    // Orbital Mechanics with Numerit: Astrodynamic Coordinates
    val R = posvel.position
    val V = posvel.velocity

    val r = R.norm
    val v = V.norm
    val α = atan2(R.y, R.x)
    val δ = atan2(R.z, sqrt(pow(R.x, 2) + pow(R.y, 2)))
    val γ = Pi / 2.0 - R.angle(V) // TODO: Quadrant corrections ?
    val ψ = atan2(r * (R.x * V.y - R.y * V.x), R.y * (R.y * V.z - R.z * V.y) - R.x * (R.z * V.x - R.x * V.z))

    new Spherical(r, α, δ, v, γ, ψ, posvel.frame)
  }


}
