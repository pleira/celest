/**
 * Copyright (C) 2012 Simon Billemont <simon@angelcorp.be>
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

package be.angelcorp.celest.frameGraph.frames

import be.angelcorp.celest.math.geometry.{Vec3, Mat3}
import be.angelcorp.celest.math.rotation.Rotation
import be.angelcorp.celest.math.rotation.RotationMatrix._
import be.angelcorp.celest.physics.Units._
import be.angelcorp.celest.frameGraph._
import be.angelcorp.celest.time.{Epochs, Epoch}
import be.angelcorp.celest.universe.Universe
import be.angelcorp.celest.frameGraph.transformations.{TransformationParameters, KinematicTransformationFactory}

import scala.math._

/**
 * Defines transfromations over the following reference frameGraph;
 * <ul>
 * <li>GEI_J2000: Geocentric Earth Equatorial (equator at J2000)</li>
 * <li>GEI_D_MEAN: Mean Geocentric Earth Equatorial(mean equator of date)</li>
 * <li>GEI_D_TRUE: True Geocentric Earth Equatorial(true equator of date)</li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li>HAE_J2000: Heliocentric Aries Ecliptic (ecliptic at J2000)</li>
 * <li>HAE_D: Heliocentric Aries Ecliptic (mean ecliptic of date)</li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 *
 * References:
 * [1]: M.Fränz, D. Harper, "Heliospheric Coordinate Systems", Corrected Version of Planetary&Space Science, 50, 217ff.(2002), 12 March 2002
 */
class HeliosphericCoordinateFrames(implicit universe: Universe) {

  val j2000 = Epochs.J2000

  /**
   * ZXZ rotation matrix to use in a coordinate transformation
   * There are 80 sub-expresions in this function (using Mathematica LeafCount):
   * @param omega First Z rotation [rad]
   * @param theta Second X rotation [rad]
   * @param phi Thrid Z rotation [rad]
   * @return Rotation matrix.
   */
  def transform(omega: Double, theta: Double, phi: Double) = Mat3.rotateZXZ(omega, theta, phi)

  def transformFactory[F0 <: ReferenceSystem, F1 <: ReferenceSystem](trans: Epoch => Rotation, from: F0, to: F1) =
    new KinematicTransformationFactory[ReferenceSystem, ReferenceSystem] {
      def calculateParameters(date: Epoch): TransformationParameters = {
        new TransformationParameters(date,
          Vec3.zero, Vec3.zero, Vec3.zero,
          trans(date), Vec3.zero, Vec3.zero
        )
      }

      def cost(epoch: Epoch) = 0

      def fromFrame: ReferenceSystem = from

      def toFrame: ReferenceSystem = to
    }

  /**
   * The epoch day number. This is defined as the fractional number of julian days of 86400 seconds from the J2000 epoch.
   * @param date Date to calculate the epoch day number from.
   * @return Epoch day nr (fractional days since J2000)
   */
  def d0(date: Epoch) = date.relativeTo(j2000)

  /**
   * Number of julian centuries since the J2000 epoch.
   * @param date Epoch.
   * @return Julian centuries since J2000.
   */
  def T0(date: Epoch) = date.relativeTo(j2000) / 36525

  /**
   * Number of julian years since the J2000 epoch.
   * @param date Epoch.
   * @return Julian years since J2000.
   */
  def y0(date: Epoch) = date.relativeTo(j2000) / 36525

  /**
   * Angle between the Earth equatorial plane and the ecliptic (the Earth orbital plane) at the J2000 epoch [rad]
   * <p>
   * See ε_J2000, equation 3 of [1]
   * </p>
   */
  val obliquityAtJ2000 = degrees(23.439291111)

  /**
   * Angle between the Earth equatorial plane and the ecliptic (the Earth orbital plane). The orientation of both
   * planes changes over time by solar, lunar and planetary gravitational forces on the Earth axis and orbit.
   * The continuous change is called 'general precession', the periodic change 'nutation'.
   * <ul>
   * <li>Mean quantities include precessional corrections.</li>
   * <li>True quantities both precessional and nutational corrections.</li>
   * <ul>
   * <p>
   * See ε_0D, equation 4 of [1]
   * </p>
   * @param date Epoch at which to calculate the mean obliquity.
   * @return The Earth obliquity, angle between the Earth equator and ecliptic, at the specified epoch [rad].
   */
  def obliquityMean(date: Epoch) = {
    val T0_ = T0(date)
    //obliquityAtJ2000 + (T0_ * (-0.013004167 + T0_ * (-0.000000164 + 0.000000504 * T0_))) * (Pi / 180.0) // Degrees version
    obliquityAtJ2000 + (-0.000226966 + (-2.86234E-9 + 8.79646E-9 * T0_) * T0_) * T0_ // Radians version
  }

  /**
   * Angle between the Earth equatorial plane and the ecliptic (the Earth orbital plane). The orientation of both
   * planes changes over time by solar, lunar and planetary gravitational forces on the Earth axis and orbit.
   * The continuous change is called 'general precession', the periodic change 'nutation'.
   * <ul>
   * <li>Mean quantities include precessional corrections.</li>
   * <li>True quantities both precessional and nutational corrections.</li>
   * <ul>
   * <p>
   * See Δε, equation 5 of [1]
   * </p>
   * @param date Epoch at which to calculate the true obliquity.
   * @return The Earth obliquity, angle between the Earth equator and ecliptic, at the specified epoch [rad].
   */
  def obliquityTrue(date: Epoch) = {
    val days = d0(date)
    // Degrees version:
    //val degree = Pi / 180
    //( 0.0026 * cos( (125.  - 0.05295 * days) * degree) + 0.0002 * cos( (200.9 + 1.97129 * days) * degree) ) * degree

    // Radian version, -sin(...) = cos(125° + ...):
    val Δε = 3.49066E-6 * cos(3.50637 + 0.0344055 * days) - 0.0000453786 * sin(0.610865 - 0.000924152 * days)
    obliquityMean(date) + Δε
  }

  /**
   * Longitudinal nutation
   * <p>
   * See Δψ, equation 6 of [1]
   * </p>
   * @param date Epoch to calculate the longitudinal nutation on.
   * @return The longitudinal nutation [rad]
   */
  def longitudinalNutation(date: Epoch) = {
    val days = d0(date)

    // Degrees version:
    //val degree = Pi / 180
    //(0.0048 * cos( (125.0 -0.05295 * days) * degree ) + 0.0004 * cos( (200.9 + 1.97129 * days) * degree ) * degree

    // Radian version, -sin(...) = cos(125° + ...):
    6.98132E-6 * cos(3.50637 + 0.0344055 * days) - 0.0000837758 * sin(0.610865 - 0.000924152 * days)
  }

  /**
   * Calculate the ecliptic coordinate differences between to epochs.
   *
   * The orientation of the ecliptic plane of date (εD) with respect to the the ecliptic plane at another date (εF)
   * is defined by the inclination πA, the ascending node longitude ΠA of the plane of date εD relative to the plane
   * of date F, and the difference in the angular distances pA of the vernal equinoxes from the ascending node.
   *
   * <p>
   * See πA ΠA pA, equation 8 of [1]
   * </p>
   *
   * @param date0 Reference epoch.
   * @param date1 Final epoch.
   * @return Difference in inclination (πA), ascending node longitude (ΠA), and vernal equinoxes angular distance (pA)
   */
  def ecliptic(date0: Epoch, date1: Epoch): (Double, Double, Double) = {
    val T = T0(date0)
    val t = date1.relativeTo(date0) / 36525
    (
      ((((0.000598 * T - 0.06603) * T + 47.0029) + ((0.000598 * T - 0.03302) + 0.000060 * t) * t) * t) * arcSecond,
      (629554.982 + (3289.4789 + 0.60622 * T) * T + ((-869.8089 - 0.50491 * T) + 0.03536 * t) * t) * arcSecond,
      (((5029.0966 + (2.22226 - 0.000042 * T) * T) + ((1.11113 - 0.000042 * T) - 0.000006 * t) * t) * t) * arcSecond
      )
  }

  /**
   * Rotation angles to convert the equator of epoch are to the equator of date.
   * <p>
   * See θA ζA zA, equation 12 of [1]
   * </p>
   * @param date Epoch.
   * @return Transformation angles to transform from the equator of epoch are to the equator of date.
   */
  def equator(date: Epoch): (Double, Double, Double) = {
    val T = T0(date)
    (
      T * (0.55675 - 0.00012 * T) * degree,
      T * (0.64062 + 0.0008 * T) * degree,
      T * (0.64062 + 0.00030 * T) * degree
      )
  }

  /**
   * Solar rotation axis coordinates defined by its right ascension α_sun and declination δ_sun with
   * respect to the celestial pole, at the J2000 epoch.
   * <p>
   * See equation 13 of [1]
   * </p>
   */
  val solarRotationAxis = (
    degrees(63.87), // Right ascension α_sun
    degrees(286.13) // Declination δ_sun
    )

  /**
   * Inclination i_sun of the solar equator [rad].
   */
  val solarEclipticInclination = degrees(7.25)

  /**
   * longitude of the ascending node Ω_sun [rad].
   * The time dependence in Ω_sun takes approximate account of the ecliptic precession.
   */
  def solarEclipticLongitude(date: Epoch) = degrees(75.76 + 1.397 * T0(date))

  val solarRotationTimeSidereal = julianDay(25.38)
  val solarRotationTimeSynodic = julianDay(27.2753)

  lazy val frameGraph = {
    val graph = ReferenceFrameGraphImpl()
    graph.attachFrame(GEI_J2000())
    graph.attachFrame(GEI_D_MEAN())
    graph.attachTransform(GEI_J2000(), GEI_D_MEAN(), transform_GEI_J2000_To_GEI_D_MEAN)
    graph.attachFrame(GEI_D_TRUE())
    graph.attachTransform(GEI_D_MEAN(), GEI_D_TRUE(), transform_GEI_D_MEAN_To_GEI_D_TRUE)
    graph.attachFrame(HAE_J2000())
    graph.attachTransform(GEI_J2000(), HAE_J2000(), transform_GEI_J2000_To_HAE_J2000)
    graph.attachFrame(HAE_D())
    graph.attachTransform(HAE_J2000(), HAE_D(), transform_HAE_J2000_To_HAE_D)
    graph.attachTransform(GEI_D_MEAN(), HAE_D(), transform_GEI_D_MEAN_To_HAE_D)
    graph
  }

  case class GEI_J2000() extends NamedReferenceFrame("GEI_J2000: GEI Geocentric Earth Equatorial (equator at J2000)")

  case class GEI_D_MEAN() extends NamedReferenceFrame("GEI_D_MEAN: Mean Geocentric Earth Equatorial(mean equator of date)")

  case class GEI_D_TRUE() extends NamedReferenceFrame("GEI_D_TRUE: True Geocentric Earth Equatorial(true equator of date)")

  case class HAE_J2000() extends NamedReferenceFrame("HAE_J2000: Heliocentric Aries Ecliptic (ecliptic at J2000)")

  case class HAE_D() extends NamedReferenceFrame("HAE_D: Heliocentric Aries Ecliptic (mean ecliptic of date))")

  /**
   * Transformation from Mean Geocentric Earth Equatorial (GEI_D_MEAN, MOD) to True Geocentric Earth Equatorial
   * (GEI_D_TRUE, TOD). The transformation takes precession and nutation of the obliquity and longitudinal node.
   * @return Transformation factory to transform from GEI_D_MEAN or MOD => GEI_D_TRUE or TOD
   */
  def transform_GEI_D_MEAN_To_GEI_D_TRUE = transformFactory(date => {
    val εD = obliquityTrue(date)
    val ε0D = obliquityMean(date)
    // See N(GEI_D, GEI_T), equation 7 of [1]
    Mat3.rotateX(-εD) dot Mat3.rotateZ(-longitudinalNutation(date)) dot Mat3.rotateX(ε0D)
  }, null, null)

  /**
   * Transformation from Heliocentric Aries Ecliptic at J2000 (HAE_J2000) to Heliocentric Aries Ecliptic of date (HAE_D).
   * The corrects the orientation of the ecliptic from at the J2000 epoch to a specific date.
   * @return Transformation factory to transform from HAE_J2000 to HAE_D
   */
  def transform_HAE_J2000_To_HAE_D = transformFactory(date => {
    val (inc, ascNode, ang) = ecliptic(j2000, date)
    // See P(HAE_J2000, HAE_D), equation 9 of [1]
    transform(ascNode, inc, -ang - ascNode)
  }, null, null)

  /**
   * Transformation from Geocentric Earth Equatorial at J2000 (GEI_J2000) to Mean Geocentric Earth Equatorial
   * (GEI_D_MEAN, MOD). Implementations of GEI_J2000 are the ICRF reference frameGraph.
   *
   * The transformation takes precession and nutation of the obliquity and longitudinal node.
   * @return Transformation factory to transform from GEI_J2000 or MOD => GEI_D_MEAN or MOD
   */
  def transform_GEI_J2000_To_GEI_D_MEAN = transformFactory(date => {
    val (θA, ζA, zA) = equator(date)
    // See P(εF, εD), equation 10 of [1]
    transform(Pi / 2 - ζA, θA, -zA - Pi / 2)
  }, null, null)

  /**
   * Transformation from Geocentric Earth Equatorial at J2000 (GEI_J2000) to Heliocentric Aries Ecliptic (HAE_J2000).
   * The transformation takes the obliquity of earth into account.
   * @return Transformation factory to transform from GEI_J2000 or MOD => HAE_J2000
   */
  def transform_GEI_J2000_To_HAE_J2000 = transformFactory(date => {
    // See section "Heliocentric Aries Ecliptic HAE_J2000" of [1]
    transform(0, obliquityAtJ2000, 0)
    // TODO: take into account solar position and velocity
  }, null, null)

  /**
   * Transformation from Mean Geocentric Earth Equatorial (GEI_D_MEAN, MOD) to Heliocentric Aries Ecliptic of date
   * (HAE_D). The transformation takes the obliquity of earth into account.
   * @return Transformation factory to transform from GEI_D_MEAN or MOD => HAE_D
   */
  def transform_GEI_D_MEAN_To_HAE_D = transformFactory(date => {
    val εD = obliquityTrue(date)
    // See section "Heliocentric Aries Ecliptic HAE_D" of [1]
    transform(0, εD, 0)
  }, null, null)


}
