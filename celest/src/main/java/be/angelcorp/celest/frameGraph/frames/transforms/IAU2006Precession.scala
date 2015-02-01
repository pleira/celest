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

import be.angelcorp.celest.math.geometry.Mat3

import scala.math._
import be.angelcorp.celest.time.{Epochs, Epoch}
import be.angelcorp.celest.universe.Universe
import be.angelcorp.celest.frameGraph._
import be.angelcorp.celest.physics.Units._
import be.angelcorp.celest.frameGraph.transformations.ConstantRotationTransformFactory
import be.angelcorp.celest.time.timeStandard.TimeStandards.TT

/**
 * IAU 2006 precession theory. Computes the precession of the ecliptic for the MOD (mean of date) to J2000 (or EME2000) reference frame. This implementation is based on [1] and [2]
 * <p>
 * This theory is also known as the P03 precession theory of [3]
 * </p>
 * =References=
 * 1) D. Vallado et al. ,<b>"Implementation Issues Surrounding the New IAU Reference Systems for Astrodynamics"</b>, 16th AAS/AIAA Space Flight Mechanics Conference, Florida, January 2006<br/>
 * 2) G. Petit, B. Luzum (eds.).,<b>"IERS Conventions (2010)"</b>, IERS Technical Note 36, Frankfurt am Main: Verlag des Bundesamts für Kartographie und Geodäsie, 2010. 179 pp., ISBN 3-89888-989-6<br/>
 * 3) N. Capitaine, P.T. Wallace, and J. Chapront, <b>"Expressions for IAU 2000 precession quantities"</b>, Astron. Astrophys., 2003, 412(2), pp. 567-586, doi:10.1051/0004-6361:20031539
 *
 * @author Simon Billemont
 */
class IAU2006Precession[F0 <: ReferenceSystem, F1 <: ReferenceSystem](val fromFrame: F0, val toFrame: F1)(implicit universe: Universe)
  extends ConstantRotationTransformFactory[F0, F1] {

  /**
   * Finds the precession matrix at a given date according to the IAU 2006 Precession model.
   */
  def rotationMatrix(epoch: Epoch) = {
    // Julian centuries TT from the J2000.0 epoch
    val t = epoch.inTimeStandard(TT).relativeTo(Epochs.J2000) / 36525.0

    Mat3.rotateX(arcSeconds(-IAU2006Precession.ε0)) dot
      Mat3.rotateZ(arcSeconds(IAU2006Precession.ψA(t))) dot
      Mat3.rotateX(arcSeconds(IAU2006Precession.ωA(t))) dot
      Mat3.rotateZ(arcSeconds(-IAU2006Precession.χA(t)))
  }

  def cost(epoch: Epoch) = 100.0

}

object IAU2006Precession {

  /** Ecliptic constant in [arcseconds], reference [2] section 5.6.2 */
  val ε0 = 84381.406

  /**
   * Ecliptic pole x.
   *
   * Reference [2] equation 5.38.
   *
   * @param t Julian centuries since the J2000 epoch in TT (or TDB)
   * @return Ecliptic pole x [arcseconds].
   */
  def PA(t: Double) = 4.199094 * t + 0.1939873 * pow(t, 2) - 0.00022466 * pow(t, 3) - 0.000000912 * pow(t, 4) + 0.0000000120 * pow(t, 5)

  /**
   * Ecliptic pole y.
   *
   * Reference [2] equation 5.38.
   *
   * @param t Julian centuries since the J2000 epoch in TT (or TDB)
   * @return Ecliptic pole y [arcseconds].
   */
  def QA(t: Double) = -46.811015 * t + 0.0510283 * pow(t, 2) + 0.00052413 * pow(t, 3) - 0.000000646 * pow(t, 4) - 0.0000000172 * pow(t, 5)

  /**
   * Precession quantity: longitude of the equator referred to the ecliptic of epoch.
   *
   * Reference [2] equation 5.39.
   *
   * @param t Julian centuries since the J2000 epoch in TT (or TDB)
   * @return Longitude [arcseconds].
   */
  def ψA(t: Double) = 5038.481507 * t - 1.0790069 * pow(t, 2) - 0.00114045 * pow(t, 3) + 0.000132851 * pow(t, 4) - 0.0000000951 * pow(t, 5)

  /**
   * Precession quantity: obliquity of the equator referred to the ecliptic of epoch.
   *
   * Reference [2] equation 5.39.
   *
   * @param t Julian centuries since the J2000 epoch in TT (or TDB)
   * @return Obliquity [arcseconds].
   */
  def ωA(t: Double) = ε0 + 0.025754 * t + 0.0512623 * pow(t, 2) - 0.00772503 * pow(t, 3) - 0.000000467 * pow(t, 4) + 0.0000003337 * pow(t, 5)

  /**
   * Precession of the ecliptic along the equator.
   *
   * Reference [2] equation 5.40.
   *
   * @param t Julian centuries since the J2000 epoch in TT (or TDB)
   * @return Precession of the ecliptic along the equator [arcseconds].
   */
  def χA(t: Double) = 10.556403 * t - 2.3814292 * pow(t, 2) - 0.00121197 * pow(t, 3) + 0.000170663 * pow(t, 4) - 0.0000000560 * pow(t, 5)

  /**
   * The mean obliquity of the ecliptic, of date.
   * Also part of the four Fukushima-Williams angles (γ_, φ_, ψ_, εA).
   *
   * Reference [2] equation 5.40.
   *
   * @param t Julian centuries since the J2000 epoch in TT (or TDB)
   * @return The mean obliquity of date [arcseconds].
   */
  def εA(t: Double) = ε0 - 46.836769 * t - 0.0001831 * pow(t, 2) + 0.00200340 * pow(t, 3) - 0.000000576 * pow(t, 4) - 0.0000000434 * pow(t, 5)

  /**
   * The GCRS right ascension of the intersection of the ecliptic of date with the GCRS equator.
   * Also part of the four Fukushima-Williams angles (γ_, φ_, ψ_, εA).
   *
   * Reference [2] equation 5.40.
   *
   * @param t Julian centuries since the J2000 epoch in TT (or TDB)
   * @return Right ascension wrt the GCRS equator [arcseconds].
   */
  def γ_(t: Double) = -0.052928 + 10.556378 * t + 0.4932044 * pow(t, 2) - 0.00031238 * pow(t, 3) - 0.000002788 * pow(t, 4) + 0.0000000260 * pow(t, 5)

  /**
   * The obliquity of the ecliptic of date on the GCRS equator.
   * Also part of the four Fukushima-Williams angles (γ_, φ_, ψ_, εA).
   *
   * Reference [2] equation 5.40.
   *
   * @param t Julian centuries since the J2000 epoch in TT (or TDB)
   * @return Obliquity wrt the GCRS equator [arcseconds].
   */
  def φ_(t: Double) = +84381.412819 - 46.811016 * t + 0.0511268 * pow(t, 2) + 0.00053289 * pow(t, 3) - 0.000000440 * pow(t, 4) - 0.0000000176 * pow(t, 5)

  /**
   * The precession angle plus bias in longitude along the ecliptic of date plus bias in longitude along the ecliptic of date.
   * Also part of the four Fukushima-Williams angles (γ_, φ_, ψ_, εA).
   *
   * Reference [2] equation 5.40.
   *
   * @param t Julian centuries since the J2000 epoch in TT (or TDB)
   * @return Precession wrt the GCRS equator [arcseconds].
   */
  def ψ_(t: Double) = -0.041775 + 5038.48148400 * t + 1.5584175 * pow(t, 2) - 0.00018522 * pow(t, 3) - 0.000026452 * pow(t, 4) - 0.0000000148 * pow(t, 5)

}
