/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
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

package be.angelcorp.celest.frameGraph.frames.transforms

import be.angelcorp.celest.math.geometry.{Vec3, Mat3}
import be.angelcorp.celest.math.rotation.RotationMatrix._
import be.angelcorp.celest.frameGraph._
import be.angelcorp.celest.physics.Units._
import be.angelcorp.celest.time.{Epochs, Epoch}
import be.angelcorp.celest.universe.Universe
import be.angelcorp.celest.frameGraph.transformations.{TransformationParameters, KinematicTransformationFactory}
import be.angelcorp.celest.time.timeStandard.TimeStandards._
import be.angelcorp.celest.data.eop.ExcessLengthOfDay

import scala.math._

/**
 * Generic earth rotation equations
 *
 * =References=
 * 1) D. Vallado et al. ,<b>"Implementation Issues Surrounding the New IAU Reference Systems for Astrodynamics"</b>, 16th AAS/AIAA Space Flight Mechanics Conference, Florida, January 2006<br/>
 * 2) G. Petit, B. Luzum (eds.).,<b>"IERS Conventions (2010)"</b>, IERS Technical Note 36, Frankfurt am Main: Verlag des Bundesamts für Kartographie und Geodäsie, 2010. 179 pp., ISBN 3-89888-989-6<br/>
 * 3) G. H. Kaplan, <b>"The IAU Resolutions on Astronomical Reference Systems, Time Scales, and Earth Rotation Models"</b>, 2005, U.S. Naval Observatory Circular No. 179, [online] http://arxiv.org/abs/astro-ph/0602086
 */
object EarthRotation {

  /** Earth Rotation Angle (ERA) at J2000.0 [rev] reference [2] */
  val θ0 = 0.7790572732640
  /** Rate of advance of ERA [rev/day_ut1] reference [2] */
  val dθdt = 1.00273781191135448

  /**
   * Earth Rotation Angle (ERA) at a specific epoch.
   * See reference [2] eqn 5.14-5.15.
   * @param epoch A specific epoch.
   * @return The earth rotation angle [rad].
   */
  def θ_ERA(epoch: Epoch)(implicit universe: Universe) = {
    val epoch_ut1 = epoch.inTimeStandard(UT1)
    val t = epoch_ut1.jd - 2451545.0

    // Same as ( θ0 + dθdt * t ), but more accurate (see reference [2][3]):
    val revs = (epoch_ut1.fractionInDay + 0.5) + θ0 + (dθdt - 1.0) * t
    revolutions(revs % 1.0)
  }

}

/**
 * The transformation factory to transform from TIRS (terrestrial intermediate reference system) to ERS (Earth reference system).
 * <p>
 * This is based on the earth rotation as a function of time. More specifically, the Greenwich apparent sidereal
 * time (GAST) angle and the earth angular veocity around its z-axis (based on the provided excess length of day).
 * </p>
 *
 * @param nutation    Nutation model to be compatible with.
 * @param lodProvider Provider for the length of day (required for the Earth rotation speed derivative)
 *
 *                    =References=
 *                    1) D. Vallado et al. ,<b>"Implementation Issues Surrounding the New IAU Reference Systems for Astrodynamics"</b>, 16th AAS/AIAA Space Flight Mechanics Conference, Florida, January 2006<br/>
 *                    2) G. Petit, B. Luzum (eds.).,<b>"IERS Conventions (2010)"</b>, IERS Technical Note 36, Frankfurt am Main: Verlag des Bundesamts für Kartographie und Geodäsie, 2010. 179 pp., ISBN 3-89888-989-6<br/>
 *                    3) G. H. Kaplan, <b>"The IAU Resolutions on Astronomical Reference Systems, Time Scales, and Earth Rotation Models"</b>, 2005, U.S. Naval Observatory Circular No. 179, [online] http://arxiv.org/abs/astro-ph/0602086
 */
class EarthRotationGAST[F0 <: ReferenceSystem, F1 <: ReferenceSystem]
(val fromFrame: F0, val toFrame: F1, val nutation: IAU2000Nutation[_, _], val lodProvider: ExcessLengthOfDay)(implicit universe: Universe) extends KinematicTransformationFactory[F0, F1] {

  /**
   * The equation of the equinoxes, used to account for the motion of the equinox due to nutation (the difference
   * between apparent and mean sidereal time at a specific epoch).
   *
   * @param t Julian centuries since the J2000 epoch in TT (or TDB).
   * @return The difference between apparent and mean sidereal time [rad]
   */
  def equationOfEquinoxes(t: Double) = {
    val (fa_l, fa_l2, fa_F, fa_D, fa_Ω) = IAU2000NutationEntry.fundamentalArguments(t)

    val ε = arcSeconds(IAU2000Nutation.ε_(t))
    val (dψ2000, dε2000) = nutation.nutationParameters(t)

    // See [3] equation 2.14.
    // This should be accurate enough. The IERS 2010 Conventions [2] document also lists a more accurate/longer series for improved accuracy.
    arcSeconds(dψ2000 * cos(ε) + 0.00264096 * sin(fa_Ω)
      + 0.00006352 * sin(2 * fa_Ω)
      + 0.00001175 * sin(2 * fa_F - 2 * fa_D + 3 * fa_Ω)
      + 0.00001121 * sin(2 * fa_F - 2 * fa_D + fa_Ω)
      - 0.00000455 * sin(2 * fa_F - 2 * fa_D + 2 * fa_Ω)
      + 0.00000202 * sin(2 * fa_F + 3 * fa_Ω)
      + 0.00000198 * sin(2 * fa_F + fa_Ω)
      - 0.00000172 * sin(3 * fa_Ω)
      - 0.00000087 * t * sin(fa_Ω))
  }

  def cost(epoch: Epoch): Double = 100.0

  /**
   * Computes the rotation matrix and its first angular velocity due to the earth rotation (greenwich apparent sidereal time, GAST).
   *
   * This is required for the transformation from TIRS (terrestrial intermediate reference system) to ERS (Earth reference system).
   *
   * @param epoch The respective epoch to compute earth rotation transformation at.
   * @return The earth rotation transformation matrix and angular velocity.
   */
  def rotationAngle(epoch: Epoch) = {
    // Rotation rate [rad], see reference [1] eqn 11
    val ω = 7.292115146706979E-5 * (1 - lodProvider.lod(epoch) / 86400.0)

    // Rotation matrix,                             Angular velocity [rad]
    (Mat3.rotateZ(-θ_GAST2000(epoch)), Vec3(0, 0, ω))
  }

  /**
   * Computes the Greenwich Mean Sidereal Time (GMST) angle.
   *
   * @param epoch The respective epoch to compute GMST angle at.
   * @return The Greenwich Mean Sidereal Time (GMST) angle [rad].
   */
  def θ_GMST2000(epoch: Epoch): Double =
    θ_GMST2000(epoch, epoch.inTimeStandard(TT).relativeTo(Epochs.J2000) / 36525)


  /**
   * Computes the Greenwich Mean Sidereal Time (GMST) angle.
   *
   * Based on reference [2] eqn 5.32.
   *
   * @param epoch The respective epoch to compute GMST angle at.
   * @param t The julian centuries TT from the J2000.0 epoch of the given epoch.
   * @return The Greenwich Mean Sidereal Time (GMST) angle [rad].
   */
  private def θ_GMST2000(epoch: Epoch, t: Double): Double = {
    val era = EarthRotation.θ_ERA(epoch)
    val temp = arcSeconds(
      0.014506 + 4612.156534 * t + 1.3915817 * pow(t, 2) - 0.00000044 * pow(t, 3) - 0.000029956 * pow(t, 4) - 0.0000000368 * pow(t, 5)
    ) % (2 * Pi)
    era + temp
  }

  /**
   * Calculates the Greenwich apparent sidereal time (GAST) at the specific epoch.
   *
   * @param epoch The respective epoch to compute GAST angle at.
   * @return The Greenwich apparent sidereal time [rad].
   */
  def θ_GAST2000(epoch: Epoch) = {
    // Julian centuries TT from the J2000.0 epoch
    val t = epoch.inTimeStandard(TT).relativeTo(Epochs.J2000) / 36525

    /** Greenwich apparent sidereal time (GAST) [rad], see reference [3] eqn 2.13 */
    θ_GMST2000(epoch, t) + equationOfEquinoxes(t)
  }

  def calculateParameters(epoch: Epoch) = {
    val (r, ω) = rotationAngle(epoch)
    new TransformationParameters(epoch, Vec3.zero, Vec3.zero, Vec3.zero, r, ω, Vec3.zero)
  }

}
