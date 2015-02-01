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

import math._
import be.angelcorp.celest.math._
import be.angelcorp.celest.physics.Units._

/**
 * Single contribution of luni-solar/planertairy effects to the nutation angles according to the IAU2000 nutation theory:
 *
 * <pre>
 * arg = f( t, l, lp, F, D, Ω, L_Me, L_Ve, L_E, L_Ma, L_J, L_Sa, L_U, L_Ne, p_A )
 * δΔψ = (ls + lst * t) * sin( arg ) + (lc + lct * t) * cos( arg )
 * δΔε = (os + ost * t) * sin( arg ) + (oc + oct * t) * cos( arg )
 * </pre>
 *
 * @param ls   ls  * Sin(arg) coefficient.
 * @param lst  lst * t * Sin(arg) coefficient.
 * @param lc   lc  * cos(arg) coefficient.
 * @param lct  lct * t * cos(arg) coefficient.
 * @param l    Multiplier for the mean anomaly of the Moon.
 * @param lp   Multiplier for the mean anomaly of the Sun.
 * @param F    Multiplier for the mean argument of latitude of the Moon.
 * @param D    Multiplier for the mean elongation of the Moon from the Sun.
 * @param Ω    Multiplier for the mean longitude of the Moon’s mean ascending node.
 * @param L_Me Multiplier for the mean heliocentric ecliptic longitudes of the planet Mercury.
 * @param L_Ve Multiplier for the mean heliocentric ecliptic longitudes of the planet Venus.
 * @param L_E  Multiplier for the mean heliocentric ecliptic longitudes of the planet Earth.
 * @param L_Ma Multiplier for the mean heliocentric ecliptic longitudes of the planet Mars.
 * @param L_J  Multiplier for the mean heliocentric ecliptic longitudes of the planet Jupiter.
 * @param L_Sa Multiplier for the mean heliocentric ecliptic longitudes of the planet Saturn.
 * @param L_U  Multiplier for the mean heliocentric ecliptic longitudes of the planet Uranus.
 * @param L_Ne Multiplier for the mean heliocentric ecliptic longitudes of the planet Neptune.
 * @param p_A  Multiplier for the approximation to the general precession in longitude.
 */
class IAU2000NutationEntry(val ls: Double = 0.0, val lst: Double = 0.0, val lc: Double = 0.0, val lct: Double = 0.0,
                           val os: Double = 0.0, val ost: Double = 0.0, val oc: Double = 0.0, val oct: Double = 0.0,
                           val l: Double = 0.0, val lp: Double = 0.0, val F: Double = 0.0, val D: Double = 0.0, val Ω: Double = 0.0,
                           val L_Me: Double = 0.0,
                           val L_Ve: Double = 0.0,
                           val L_E: Double = 0.0,
                           val L_Ma: Double = 0.0,
                           val L_J: Double = 0.0,
                           val L_Sa: Double = 0.0,
                           val L_U: Double = 0.0,
                           val L_Ne: Double = 0.0,
                           val p_A: Double = 0.0) {

  /**
   * Finds the contribution to the nutation of the ecliptic in longitude (δΔψ) and obliquity (δΔε) of this sole series entry.
   *
   * @param t Julian centuries since the J2000 epoch in TT (or TDB).
   * @return [arcseconds]
   */
  def nutationContribution(t: Double) = {
    val arg = argument(t)
    val δΔψ = (ls + lst * t) * sin(arg) + (lc + lct * t) * cos(arg)
    val δΔε = (os + ost * t) * sin(arg) + (oc + oct * t) * cos(arg)
    (δΔψ, δΔε)
  }

  /**
   * Computes the argument used in the IAU2000A/B nutation theory series expansion..
   *
   * @param t Julian centuries since the J2000 epoch in TT (or TDB).
   * @return Arguement for IAU2000A/B [radians]
   */
  def argument(t: Double) = {
    val (fa_l, fa_lp, fa_F, fa_D, fa_Ω) = IAU2000NutationEntry.fundamentalArguments(t)
    val (lMe, lVe, lE, lMa, lJ, lSa, lU, lNe, pA) = IAU2000NutationEntry.planetaryArguments(t)

    // luni-solar nutation series
    val lunisolar = l * fa_l + lp * fa_lp + F * fa_F + D * fa_D + Ω * fa_Ω
    // planetary nutation series
    val planetary = p_A * pA + L_Ne * lNe + L_U * lU + L_Sa * lSa + L_J * lJ + L_Ma * lMa + L_E * lE + L_Ve * lVe + L_Me * lMe

    lunisolar + planetary
  }

}

object IAU2000NutationEntry {

  /**
   * The fundamental luni-solar arguments used in previous nutation theory (Delaunay arguments):
   * <ul>
   * <li>l, the mean anomaly of the Moon</li>
   * <li>l', the mean anomaly of the Sun</li>
   * <li>F, the mean argument of latitude of the Moon</li>
   * <li>D, the mean elongation of the Moon from the Sun</li>
   * <li>?, the mean longitude of the Moon’s mean ascending node</li>
   * </ul>
   *
   * @param t Julian centuries since the J2000 epoch in TT (or TDB).
   * @return The fundamental agreements used in IAU2000A/B in [rad]
   */
  def fundamentalArguments(t: Double) = {
    // Reference [2], equation 5.43
    // The "% 1296000.0" is to reduce the angle to maximum once circle turn
    val F1 = arcSeconds((485868.249036 + t * (1717915923.2178 + t * (31.8792 + t * (0.051635 + t * (-0.00024470))))) % 1296000.0) // mean anomaly of the moon
    val F2 = arcSeconds((1287104.79305 + t * (129596581.0481 + t * (-0.5532 + t * (0.000136 + t * (-0.00001149))))) % 1296000.0) // mean anomaly of the sun
    val F3 = arcSeconds((335779.526232 + t * (1739527262.8478 + t * (-12.7512 + t * (-0.001037 + t * (0.00000417))))) % 1296000.0) // mean argument of the latitude of the moon
    val F4 = arcSeconds((1072260.70369 + t * (1602961601.2090 + t * (-6.3706 + t * (0.006593 + t * (-0.00003169))))) % 1296000.0) // mean elongation of the moon from the sun
    val F5 = arcSeconds((450160.398036 + t * (-6962890.5431 + t * (7.4722 + t * (0.007702 + t * (-0.00005939))))) % 1296000.0) // mean longitude of the ascending node of the moon
    (F1, F2, F3, F4, F5)
  }

  /**
   * The fundamental planetary arguments used in the IAU2000A/B nutation theory (Delaunay arguments):
   * <ul>
   * <li>LMe, Mean heliocentric ecliptic longitudes of the planet Mercury.</li>
   * <li>LVe, Mean heliocentric ecliptic longitudes of the planet Venus.</li>
   * <li>LE,  Mean heliocentric ecliptic longitudes of the planet Earth.</li>
   * <li>LMa, Mean heliocentric ecliptic longitudes of the planet Mars.</li>
   * <li>LJ,  Mean heliocentric ecliptic longitudes of the planet Jupiter.</li>
   * <li>LSa, Mean heliocentric ecliptic longitudes of the planet Saturn.</li>
   * <li>LU,  Mean heliocentric ecliptic longitudes of the planet Uranus.</li>
   * <li>LNe, Mean heliocentric ecliptic longitudes of the planet Neptune.</li>
   * <li>pA,  an approximation to the general precession in longitude.</li>
   * </ul>
   *
   *
   * @param t Julian centuries since the J2000 epoch in TT (or TDB).
   * @return The planetary arguments used in IAU2000A/B [rad]
   */
  def planetaryArguments(t: Double) = {
    // Reference [2], equation 5.44
    val LMe = mod(4.402608842 + 2608.7903141574 * t, 2 * Pi)
    val LVe = mod(3.176146697 + 1021.3285546211 * t, 2 * Pi)
    val LE = mod(1.753470314 + 628.3075849991 * t, 2 * Pi)
    val LMa = mod(6.203480913 + 334.0612426700 * t, 2 * Pi)
    val LJ = mod(0.599546497 + 52.9690962641 * t, 2 * Pi)
    val LSa = mod(0.874016757 + 21.3299104960 * t, 2 * Pi)
    val LU = mod(5.481293872 + 7.4781598567 * t, 2 * Pi)
    val LNe = mod(5.311886287 + 3.8133035638 * t, 2 * Pi)
    val pA = mod(0.02438175 * t + 0.00000538691 * pow(t, 2), 2 * Pi)
    (LMe, LVe, LE, LMa, LJ, LSa, LU, LNe, pA)
  }

}