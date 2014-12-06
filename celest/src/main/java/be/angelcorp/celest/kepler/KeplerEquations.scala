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
package be.angelcorp.celest.kepler

import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem
import be.angelcorp.celest.kepler
import be.angelcorp.celest.math.CelestialRotate
import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.state.{Keplerian, PosVel}

import scala.math._


abstract class KeplerEquations[F <: BodyCenteredSystem](k: Keplerian[F]) {

  /** Cached value for sin(i) */
  lazy val sini = sin( k.i )
  /** Cached value for cos(i) */
  lazy val cosi = cos( k.i )
  /** Cached value for sin(ω) */
  lazy val sinω = sin( k.ω )
  /** Cached value for cos(ω) */
  lazy val cosω = cos( k.ω )
  /** Cached value for sin(Ω) */
  lazy val sinΩ = sin( k.Ω )
  /** Cached value for cos(Ω) */
  lazy val cosΩ = cos( k.Ω )
  /** Cached value for sin(ν) */
  lazy val sinν = sin( k.ν )
  /** Cached value for cos(ν) */
  lazy val cosν = cos( k.ν )
  /** Cached value for sin(u) = sin(ν + ω) */
  lazy val sinu = sin( k.ν + k.ω )
  /** Cached value for cos(u) = cos(ν + ω) */
  lazy val cosu = cos( k.ν + k.ω )

  /**
   * Standard gravitational parameter μ [m³/s²], of the body at the center of the Keplerian orbit.
   */
  lazy val μ = k.frame.centerBody.μ

  /**
   * Get the Eccentric (E), Parabolic(B) or Hyperbolic(H) anomaly (Depends on which exact orbit which is returned)
   *
   * @return The Eccentric/Parabolic/Hyperbolic anomaly [rad]
   */
  def anomaly: Double

  /**
   * Get the true anomaly (Depends on which exact orbit which is returned).
   *
   * @return The Eccentric/Parabolic/Hyperbolic anomaly [rad]
   */
  def trueAnomaly: Double

  /**
   * Areal velocity in function of Semi-major axis and the gravitational constant
   *
   * @return Areal velocity \dot{A}
   */
  def arealVel: Double

  /**
   * @see KeplerEquations#arguementOfLatitude(double, double)
   * @return
   */
  lazy val arguementOfLatitude = kepler.arguementOfLatitude(k.argumentOfPeriapsis, trueAnomaly)

  /**
   * Compute the flight path angle &gamma; for the current positions. (Angle between the tangent on the
   * radius vector and the velocity vector
   *
   * @return Flight path angle &gamma; [rad]
   */
  lazy val flightPathAngle = kepler.flightPathAngle(k.eccentricity, trueAnomaly)

  /**
   * Compute the apcocenter distance (distance from the center body to the furthest point in the
   * orbit).
   *
   * @return Apocenter distance [m]
   */
  def apocenter: Double

  /**
   * Compute the pericenter distance (distance from the center body to the closest point in the orbit).
   *
   * @return Pericenter distance [m]
   */
  def pericenter: Double

  /**
   * This function converts Kepler orbital elements (p,e,i,O,w,nu) to ECI cartesian coordinates. This
   * function is derived from algorithm 10 on pg. 125 of David A. Vallado's Fundamentals of
   * Astrodynamics and Applications (2nd Edition) <br/>
   *
   * @return RV in cartesian coordiantes
   */
  lazy val cartesian = {
    val rv = kepler.kepler2cartesian(k.semiMajorAxis, k.eccentricity, k.inclination, k.rightAscension, k.argumentOfPeriapsis, trueAnomaly, μ)
    new PosVel(rv._1, rv._2, k.frame)
  }

  /**
   * Compute the mean motion of the body in the orbit
   *
   * @return Mean motion [rad/s]
   */
  lazy val meanMotion = kepler.meanMotion(μ, k.semiMajorAxis)

  /**
   * Compute the period of the orbit (sidereal)
   *
   * @return Period time [s]
   */
  def period: Double

  /*
	 * @return current radius r [m]
	 */
  lazy val radius = semiLatusRectum / (1 + k.eccentricity * cosν )

  /** Shorthand notation for radius [m] */
  def r = radius

  /**
   * Compute the semi-latus rectum (focal parameter) of the orbit
   *
   * @return semi-latus rectum [m]
   */
  def semiLatusRectum: Double

  /** Short-hand notation for `semiLatusRectum` */
  def p = semiLatusRectum

  /**
   * Compute the total energy per unit mass for the orbit
   *
   * @return Total energy per unit mass [m^2^/s^2^]
   */
  def totEnergyPerMass: Double

  /**
   * Compute the specific relative angular momentum of the orbiting body.
   *
   * @return Specific relative angular momentum [m^2^/s]
   */
  def specificAngularMomentum = sqrt( p * μ )

  /** Short-hand notation for `specificAngularMomentum` [m^2^/s] */
  def h = specificAngularMomentum

  /**
   * @see KeplerEquations#trueLongitude(Vector3D)
   */
  lazy val trueLongitude = {
    val ν = trueAnomaly
    val p = semiLatusRectum
    val R_pqw = Vec3(p * cosν / (1 + k.e * cosν), p * sinν / (1 + k.e * cosν), 0)
    val R = CelestialRotate.PQW2ECI(k.argumentOfPeriapsis, k.rightAscension, k.inclination) * R_pqw
    kepler.trueLongitude(R)
  }

  /**
   * @see KeplerEquations#trueLongitudeOfPeriapseApproximate(Vector3D)
   */
  lazy val trueLongitudeOfPeriapse = {
    val e_unit_vec = Vec3(
      cosΩ * cosω - cosi * sinΩ * sinω,
      sinΩ * cosω + cosi * cosΩ * sinω,
      sini * sinω)
    kepler.trueLongitudeOfPeriapse(e_unit_vec * k.eccentricity)
  }

  /**
   * @see KeplerEquations#trueLongitudeOfPeriapseApproximate(double, double)
   */
  lazy val trueLongitudeOfPeriapseApproximate = kepler.trueLongitudeOfPeriapseApproximate(k.rightAscension, k.argumentOfPeriapsis)

  /**
   * Compute the velocity squared in a given point in the orbit (by given r)
   *
   * @param r
	 * Radius of the position [m]
   * @return velocity squared [m<sup>2</sup>/s<sup>2</sup>]
   */
  def visViva(r: Double) = kepler.visViva(μ, r, k.semiMajorAxis)
}

object KeplerEquations {

  var angleTolarance = 1E-8
  var eccentricityTolarance = 1E-6
  var anomalyIterationTol = 1E-14

}

