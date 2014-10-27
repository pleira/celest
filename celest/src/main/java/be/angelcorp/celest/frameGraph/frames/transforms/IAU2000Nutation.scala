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
import be.angelcorp.celest.physics.Units._
import be.angelcorp.celest.frameGraph.ReferenceSystem
import be.angelcorp.celest.time.{Epochs, Epoch}
import be.angelcorp.celest.universe.Universe
import be.angelcorp.celest.frameGraph.transformations.ConstantRotationTransformFactory
import be.angelcorp.celest.time.timeStandard.TimeStandards.TT

/**
 * IAU 2000 nutation theory (IAU2000A/IAU2000B), optionally with slight IAU 2006 adjustments).
 * <p>
 * This model finds the forced nutation parameters of the non-rigid Earth axis in longitude (Δψ) and obliquity (Δε).
 * This is required for the equinox based transformation from ERS/TOD (True of date, Earth reference system) to MOD(mean of date) reference frame.
 * </p><p>
 * This implementation is based on [1], [2] and [3]. This model is also known as MHB2000 [4].
 * </p>
 *
 * @param coefficients Coefficients for the nutation in longitude and obliquity.
 * @param IAU2006Corrections True to apply the IAU2006 model corrections.
 *
 *                           =References=
 *                           1) D. Vallado et al. ,<b>"Implementation Issues Surrounding the New IAU Reference Systems for Astrodynamics"</b>, 16th AAS/AIAA Space Flight Mechanics Conference, Florida, January 2006<br/>
 *                           2) G. Petit, B. Luzum (eds.).,<b>"IERS Conventions (2010)"</b>, IERS Technical Note 36, Frankfurt am Main: Verlag des Bundesamts für Kartographie und Geodäsie, 2010. 179 pp., ISBN 3-89888-989-6<br/>
 *                           3) G. H. Kaplan, <b>"The IAU Resolutions on Astronomical Reference Systems, Time Scales, and Earth Rotation Models"</b>, 2005, U.S. Naval Observatory Circular No. 179, [online] http://arxiv.org/abs/astro-ph/0602086
 *                           4) P. M. Mathews, T. A. Herring, and B. A. Buett, <b>"Modeling of nutation and precession: New nutation series for nonrigid Earth, and insights into the Earth's Interior"</b>, 2002, J. Geophys. Res., 107(B4), doi: 10.1029/2001JB000390.
 *
 * @author Simon Billemont
 */
class IAU2000Nutation[F0 <: ReferenceSystem, F1 <: ReferenceSystem]
(val fromFrame: F0, val toFrame: F1,
 coefficients: List[IAU2000NutationEntry], IAU2006Corrections: Boolean = true)(implicit universe: Universe)
  extends ConstantRotationTransformFactory[F0, F1] {

  def cost(epoch: Epoch): Double = 100.0

  def rotationMatrix(epoch: Epoch) = {
    // Julian centuries TT from the J2000.0 epoch
    val t = epoch.inTimeStandard(TT).relativeTo(Epochs.J2000) / 36525.0

    // Nutation angles according to this theory
    val (dψ2000, dε2000) = nutationParameters(t)

    // The mean obliquity of the ecliptic
    val ε_ = IAU2000Nutation.ε_(t)

    // Compute the nutation matrix
    Mat3.rotateX(arcSeconds(-(ε_ + dε2000))) dot Mat3.rotateZ(arcSeconds(-dψ2000)) dot Mat3.rotateX(arcSeconds(ε_))
  }

  /**
   * Calculate the nutation (luni-solar + planetary) in longitude (Δψ), and obliquity (Δε) according to either the IAU2000A/B nutation.
   *
   * @param epoch Epoch at which to compute the nutation parameters.
   * @return The nutation parameters longitude (Δψ) and obliquity (Δε) [arcseconds]
   */
  def nutationParameters(epoch: Epoch): (Double, Double) = {
    // Julian centuries TT from the J2000.0 epoch
    val t = epoch.inTimeStandard(TT).relativeTo(Epochs.J2000) / 36525.0
    nutationParameters(t)
  }

  /**
   * Calculate the nutation (luni-solar + planetary) in longitude (Δψ), and obliquity (Δε) according to either the IAU2000A/B nutation.
   *
   * @param t Julian centuries since the J2000 epoch in TT (or TDB).
   * @return The nutation parameters longitude (Δψ) and obliquity (Δε) [arcseconds]
   */
  def nutationParameters(t: Double): (Double, Double) = {
    // Compute the fundamental arguments

    /** Longitude referred to the ecliptic of date t  */
    val (dψ, dε) = coefficients.foldLeft((0.0, 0.0))((nut, entry) => {
      val (δΔψ, δΔε) = entry.nutationContribution(t)
      ((nut._1 + δΔψ) % 1296000.0, (nut._2 + δΔε) % 1296000.0) // % 1296000.0 == mod 2 pi
    })

    val (dΔψ_FCN, dΔε_FCN) = if (IAU2006Corrections) {
      /* Factor correcting for secular variation of J2. */
      val f_J2 = -2.7774e-6 * t
      /* Corrections, see Eq 5 of P. T. Wallace and N. Capitaine, "Precession-nutation procedures consistent with IAU 2006 resolutions", 2006, DOI: 10.1051/0004-6361:20065897  */
      (dψ * (f_J2 + 0.4697E-6), dε * f_J2)
    } else (0.0, 0.0)

    // Apply any required corrections (a ? mas => as)
    val Δψ2000 = dΔψ_FCN + dψ
    val Δε2000 = dΔε_FCN + dε

    (Δψ2000, Δε2000)
  }

}

object IAU2000Nutation {

  /*
   * The IAU 2000A nutation series is associated with the following offset (originally provided as frame bias in d?bias and d?bias)
   * of the direction of the CIP at J2000.0 from the direction of the pole of the GCRS:
   */
  val ξ0 = arcSeconds(-0.0166170)
  val η0 = arcSeconds(-0.0068192)

  /**
   * Find the corrections equivalent for the equinox based transformation (this) based on the CIO coordinates dX dY.
   *
   * See equation 13 from ref [1].
   *
   * @param dX Celestial pole offset dX [arcseconds].
   * @param dY Celestial pole offset dY [arcseconds].
   * @param t Julian centuries since the J2000 epoch in TT (or TDB).
   * @return The equivalent offset to the nutation longitude (???_FCN) and obliquity (???_FCN) [arcseconds].
   */
  def dXdY_to_δΔεδΔψ(dX: Double, dY: Double, t: Double) = {
    val ε0 = IAU2006Precession.ε0
    val εA = IAU2006Precession.εA(t)
    val ψA = IAU2006Precession.ψA(t)
    val χA = IAU2006Precession.χA(t)

    val fp = ψA * cos(ε0) - χA
    /** Correction to the longditude due to FCN (free core nutation) */
    val δΔε_FCN = (dX - dY * fp) / ((1.0 + pow(fp, 2)) * sin(εA))
    /** Correction to the obliquity due to FCN (free core nutation) */
    val δΔψ_FCN = (dY + dX * fp) / (1.0 + pow(fp, 2))

    (δΔε_FCN, δΔψ_FCN)
  }

  /**
   * The mean obliquity of the ecliptic.
   * <p>
   * This is the angle between the mean equator and ecliptic, or, equivalently, between the ecliptic pole and mean
   * celestial pole of date.
   * </p>
   * @param t Julian centuries since the J2000 epoch in TT (or TDB).
   * @return Mean obliquity of the ecliptic [arcseconds].
   */
  def ε_(t: Double) =
    (84381.406 - 46.836769 * t - 0.0001831 * pow(t, 2) + 0.00200340 * pow(t, 3) - 0.000000576 * pow(t, 5) - 0.0000000434 * pow(t, 5)) % 1296000.0
}

