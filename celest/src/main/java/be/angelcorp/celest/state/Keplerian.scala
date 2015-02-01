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

import scala.math._
import be.angelcorp.celest.kepler
import be.angelcorp.celest.kepler._
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem

sealed abstract class FastAngle

/**
 * True anomaly.
 *
 * The angle between pericenter of the orbit and the instantaneous position of the satellite. This angle is  measured in
 * the orbit plane, and increasing over time.
 *
 * @param trueAnomaly True anomaly [rad]
 */
case class TrueAnomaly(trueAnomaly: Double) extends FastAngle {

}

/**
 * Mean anomaly.
 *
 * This is a fictitious angle, which varies linearly in time, and is zero at the pericenter location:
 *
 * <pre>
 * M = n * t
 * </pre>
 *
 * Where M is the mean anomaly [rad], n is the orbit mean motion [rad/s], and t is the time since the last
 * pericenter passage [s].
 *
 * @param meanAnomaly True anomaly [rad]
 */
case class MeanAnomaly(meanAnomaly: Double) extends FastAngle

/**
 * Eccentric/Parabolic/Hyperbolic anomaly.
 *
 * Depending on the eccentricity of the orbit, this is either of the following angles:
 *
 * - The Eccentric  anomaly (for e <= 0 < 1)
 * - The Parabolic  anomaly (for e == 1)
 * - The Hyperbolic anomaly (for e >  1)
 *
 * These angles are fictitious angles, that can be used to simplify various expression/relations.
 *
 * @param anomaly True anomaly [rad]
 */
case class Anomaly(anomaly: Double) extends FastAngle

/**
 * Holds the state using the classical Keplerian elements:
 *
 * <pre>
 * Elements: { a,   e,    i,     &omega;,     &Omega;,     &nu;  }
 * Units:    {[m], [-], [rad], [rad], [rad], [rad]}
 * </pre>
 *
 * - Semi-major axis a: This is one half of the major axis, and thus runs from the centre, through a focus, and to the
 * edge of the ellipse.
 * - Eccentricity e: May be interpreted as a measure of how much this shape deviates from a circle.
 * - Inclination i: This element tells you what the angle is between the ecliptic and the orbit. The inclination
 * ranges from 0 to &pi; rad.
 * - Argument of periapsis, &omega;: This is the angle between the orbit's periapsis (the point of closest approach to
 * the central point) and the orbit's ascending node (the point where the body crosses the plane of reference
 * from South to North). The angle is measured in the orbital plane and in the direction of motion.
 * - Right ascension of the ascending node, &Omega;: It is the angle from a reference direction, called the origin of
 * longitude, to the direction of the ascending node, measured in a reference plane.
 *
 * @param a Semi-major axis, a [m].
 * @param e Eccentricity, e [-].
 * @param i Inclination, i [rad].
 * @param ω Argument of periapsis, &omega; [rad].
 * @param Ω Right ascension of the ascending node, &Omega; [rad]
 * @param meanAnomaly Mean anomaly, M [rad]
 * @param frame Frame in which this set of elements is defined.
 *
 * @author Simon Billemont
 */
class Keplerian[F <: BodyCenteredSystem]
(val a: Double,
 val e: Double,
 val i: Double,
 val ω: Double,
 val Ω: Double,
 val meanAnomaly: Double,
 val frame: F) extends Orbit[F] {

  /** A more verbose name for a */
  def semiMajorAxis = a

  /** A more verbose name for e */
  def eccentricity = e

  /** A more verbose name for i */
  def inclination = i

  /** A more verbose name for ω */
  def argumentOfPeriapsis = ω

  /** A more verbose name for Ω */
  def rightAscension = Ω

  /**
   * Finds the satellite position in the orbit using the true anomaly.
   *
   * @return The true anomaly [rad].
   */
  def trueAnomaly = quantities.trueAnomaly

  /** A shorthand notation for `trueAnomaly` */
  def ν = trueAnomaly

  /**
   * Finds the satellite position in the orbit using the generic anomaly (eccentric/hyperbolic/parabolic).
   *
   * @return The anomaly [rad].
   */
  def anomaly = quantities.anomaly

  lazy val quantities = {
    e match {
      case 0 => new KeplerCircular(this)
      case e if e < 1 => new KeplerEllipse(this)
      case e if e == 1 => new KeplerParabola(this)
      case e if e > 1 => new KeplerHyperbola(this)
      case e => throw new Exception("Invalid eccentricity e=" + e)
    }
  }

  def isCircular = e == 0

  def isElliptical = e < 1 && e >= 0

  def isParabolic = e == 1

  def isHyperbolical = e > 1

  def isEquatorial = math.abs(inclination) < KeplerEquations.angleTolarance

  def toPosVel: PosVel[F] = quantities.cartesian

  def propagateFor(seconds: Double) = {
    val Mnew = meanAnomaly + seconds * quantities.meanMotion
    new Keplerian(a, e, i, ω, Ω, Mnew, frame)
  }

  def propagateTo(meanAnomaly: Double) = new Keplerian(a, e, i, ω, Ω, meanAnomaly, frame)

  def propagateTo(angle: FastAngle) = Keplerian(a, e, i, ω, Ω, angle, frame)

  def μ = frame.centerBody.μ

}

object Keplerian {

  /**
   * Create a set of Kepler elements from another [[be.angelcorp.celest.state.Orbit]]. Chooses itself what the best
   * way of converting is.
   *
   * @param orbit Orbit to convert to Keplerian elements.
   */
  def apply[F <: BodyCenteredSystem](orbit: Orbit[F]): Keplerian[F] = orbit match {
    case k: Keplerian[F] => k
    case s: Spherical[F] =>
      // See Fundamentals of astrodynamics - II , K.F. Wakker, p 16-4, eqn 16.1-16.7
      val μ = s.frame.centerBody.μ

      val rv2m = s.r * s.v * s.v / μ
      val a = s.r / (2 - rv2m)
      val e = sqrt(1 - rv2m * (2 - rv2m) * pow(cos(s.γ), 2))
      val E = atan2(sqrt(a / μ) * s.r * s.v * sin(s.γ), a - s.r)
      val trueA = be.angelcorp.celest.math.quadrantFix(2 * atan(sqrt((1 + e) / (1 - e)) * tan(E / 2)), E)
      val i = acos(cos(s.δ) * sin(s.ψ))
      val ω = atan2(sin(s.δ) / sin(i), cos(s.δ) * cos(s.ψ) / sin(i)) - trueA
      val Ω = s.α - atan2(tan(s.δ) / tan(i), cos(s.ψ) / sin(i))

      Keplerian(a, e, i, ω, Ω, TrueAnomaly(trueA), s.frame)
    case _ =>
      val k = kepler.cartesian2kepler(orbit.toPosVel, orbit.frame.centerBody.μ)
      new Keplerian(k._1, k._2, k._3, k._4, k._5, kepler.meanAnomalyFromTrue(k._6, k._2), orbit.frame)
  }

  /**
   * Create the Kepler elements from direct numerical values
   *
   * @param a Semi-major axis [m]
   * @param e Eccentricity [-]
   * @param i Inclination [rad]
   * @param ω Argument of pericenter [rad]
   * @param Ω Right ascension of ascending node [rad]
   * @param fast Fast angle (position of the satellite) [rad]
   * @param frame Frame in which this set of elements is defined.
   */
  def apply[F <: BodyCenteredSystem](a: Double, e: Double, i: Double, ω: Double, Ω: Double, fast: FastAngle, frame: F): Keplerian[F] = {
    val ma = fast match {
      case MeanAnomaly(ma) => ma
      case TrueAnomaly(ta) => kepler.meanAnomalyFromTrue(ta, e)
      case Anomaly(a) => kepler.meanAnomalyFromAnomaly(a, e)
    }
    new Keplerian(a, e, i, ω, Ω, ma, frame)
  }

  /**
   * Create the Kepler elements from a tuple of numerical values:
   * tuple = [a, e, i, ω, Ω, M]
   * @param elements Tuple with the respective Kepler elements.
   * @param frame Frame in which this set of elements is defined.
   */
  def apply[F <: BodyCenteredSystem](elements: (Double, Double, Double, Double, Double, Double), frame: F): Keplerian[F] =
    new Keplerian(elements._1, elements._2, elements._3, elements._4, elements._5, elements._6, frame)

  def unapply[F <: BodyCenteredSystem]( k: Keplerian[F] ): (Double, Double, Double, Double, Double, Double, F) =
    (k.a, k.e, k.i, k.ω, k.Ω, k.meanAnomaly, k.frame)

}
