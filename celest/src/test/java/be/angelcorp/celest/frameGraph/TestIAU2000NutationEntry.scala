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

package be.angelcorp.celest.frameGraph

import be.angelcorp.celest.frameGraph.frames.transforms.{IAU2000NutationEntry, IAU2000NutationLoader}
import be.angelcorp.celest.math._
import be.angelcorp.celest.physics.Units._
import be.angelcorp.celest.time.timeStandard.TimeStandards.TT
import be.angelcorp.celest.time.{Epochs, JulianDate}
import be.angelcorp.celest.universe.DefaultUniverse
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.math._

/**
 * The numerical values in these test cases where obtained from SOFA using the Microsoft visual studio 2012 debugger,
 * when running the following c++ snippet:
 * <pre>
 * double epoch_jd, epoch_fraction; // UTC
 * iauDtf2d("TT", 2013, 04, 27, 12, 33, 18.1938271, &epoch_jd, &epoch_fraction);
 * std::cout << "Epcoh: 2013/04/27 12h33m18.1938271s TT = " << (epoch_fraction + epoch_jd) << "jd TT"<< std::endl;
 *
 * double dpsi, deps;
 * iauNut00a(epoch_jd, epoch_fraction, &dpsi, &deps);
 * double N[3][3];
 * iauNum00a(epoch_jd, epoch_fraction, N);
 * </pre>
 */
class TestIAU2000NutationEntry extends FlatSpec with ShouldMatchers {

  implicit val universe = new DefaultUniverse

  "IAU2000NutationEntry" should "calculate the same fundamental arguments as SOFA" in {
    val epoch = new JulianDate(2013,  4, 27, 12, 33, 18.1938271, TT)
    val t = epoch.inTimeStandard(TT).relativeTo(Epochs.J2000) / 36525.0

    // Check luni-solar arguments
    val (f1, f2, f3, f4, f5) = IAU2000NutationEntry.fundamentalArguments(t)
    f1 should be(5.8722724388468013 +- arcSecond(1E-3))
    f2 should be(1.9634477857018902 +- arcSecond(1E-3))
    f3 should be(0.25074963882766854 +- arcSecond(1E-3))
    f4 should be(3.5975869706222894 +- arcSecond(1E-3))
    f5 should be(-2.3138999079361473 +- arcSecond(1E-3))

    // Check planetary arguments
    val (f6, f7, f8, f9, f10, f11, f12, f13, f14) = IAU2000NutationEntry.planetaryArguments(t)
    f6 should be(0.027468121921231159 +- arcSecond(1E-3))
    f7 should be(0.98404499026592873 +- arcSecond(1E-3))
    f8 should be(3.7607932041164602 +- arcSecond(1E-3))
    f9 should be(0.43397851046295699 +- arcSecond(1E-3))
    f10 should be(1.3716898237431217 +- arcSecond(1E-3))
    f11 should be(3.7150984233948283 +- arcSecond(1E-3))
    f12 should be(0.19417757651291101 +- arcSecond(1E-3))
    // f13 should be ( 5.8290097934300027    +- arcSecond(1E-3) )  <= Sofa uses MHB2000 expression for the longitude of Neptune not the same as IERS
    f14 should be(0.0032476733302456303 +- arcSecond(1E-3))
  }

  it should "calculate the same luni-solar nutation contributions as SOFA" in {
    val epoch = new JulianDate(2013,  4, 27, 12, 33, 18.1938271, TT)
    val t = epoch.inTimeStandard(TT).relativeTo(Epochs.J2000) / 36525.0

    val sofaCoefficients = IAU2000NutationLoader.MHB2000_2000A

    // These two combined test all the luni-solar entries
    //        l lp  F  D  O     ls     lst      lc      oc      oct     os
    // 5    { 0, 1, 2,-2, 2,-516821.0,1226.0, -524.0,224386.0,-677.0, -174.0},
    // 6    { 1, 0, 0, 0, 0, 711159.0,  73.0, -872.0,  -6750.0,  0.0,  358.0},
    val coef1 = sofaCoefficients(5)
    val coef2 = sofaCoefficients(6)

    mod(coef1.argument(t), 2 * Pi) should be((-3.0748413865800597 + 2 * Pi) +- arcSecond(1E-3))
    val (δψ1, δε1) = coef1.nutationContribution(t)
    δψ1 should be(34984.783632262654E-7 +- 1E-3) // Arcseconds
    δε1 should be(-223784.70399657678E-7 +- 1E-3) // Arcseconds

    mod(coef2.argument(t), 2 * Pi) should be(5.8722724388468013 +- arcSecond(1E-3))
    val (δψ2, δε2) = coef2.nutationContribution(t)
    δψ2 should be(-284873.17840005737E-7 +- 1E-3) // Arcseconds
    δε2 should be(-6331.1086008923703E-7 +- 1E-3) // Arcseconds
  }

  it should "calculate the same planetary nutation contributions as SOFA" in {
    // Note sofa uses the MHB2000 and not those from IAU, so the outcome is sightly off (sofa says up to 0.1 arcsecond).
    val epoch = new JulianDate(2013,  4, 27, 12, 33, 18.1938271, TT)
    val t = epoch.inTimeStandard(TT).relativeTo(Epochs.J2000) / 36525.0

    val sofaCoefficients = IAU2000NutationLoader.MHB2000_2000A

    // These four combined test all the planetary entries (the offset 678 accounts for the luni-solar coefficients)
    //            l  F  D  O  me  ve  ea ma ju sa ur ne  z    ls   lc    os   oc
    // 0        { 0, 0, 0, 0, 0,  0,  8,-16, 4, 5, 0, 0, 0, 1440,   0,    0,   0}
    // 3        { 0, 0, 0, 0, 0,  0,  0,  0, 0, 0,-1, 2, 2,    0,   5,    0,   0}
    // 188      { 0, 2,-2, 1,-1,  0,  2,  0, 0, 0, 0, 0, 0,    0,   8,    4,   1}
    // 223      { 0, 1,-1, 1, 0, -5,  7,  0, 0, 0, 0, 0, 0,  140,  27,   14, -75}

    val coef1 = sofaCoefficients(678 + 0)
    val coef2 = sofaCoefficients(678 + 3)
    val coef3 = sofaCoefficients(678 + 188)
    val coef4 = sofaCoefficients(678 + 223)

    mod(coef1.argument(t), 2 * Pi) should be(3.2226437272138995 +- arcSecond(1))
    val (δψ1, δε1) = coef1.nutationContribution(t)
    δψ1 should be(-116.58580072576588E-7 +- 1E-3) // Arcseconds
    δε1 should be(-0.0 +- 1E-3) // Arcseconds

    // A bit less accurate due to the non-IAU equation for neptune
    // mod(coef2.argument(t), 2*Pi) should be ( 5.1871520498279988 +- arcSecond(1) ) <= equation for Neptune
    val (δψ2, δε2) = coef2.nutationContribution(t)
    δψ2 should be(2.2856386686236778E-7 +- 2E-1) // Arcseconds
    δε2 should be(0.0 +- 2E-1) // Arcseconds

    mod(coef3.argument(t), 2 * Pi) should be((-1.5134557005745162 + 2 * Pi) +- arcSecond(1))
    val (δψ3, δε3) = coef3.nutationContribution(t)
    δψ3 should be(0.45847367380053916E-7 +- 1E-3) // Arcseconds
    δε3 should be(-3.9361166975131505E-7 +- 1E-3) // Arcseconds

    mod(coef4.argument(t), 2 * Pi) should be(3.1782196595204670 +- arcSecond(1))
    val (δψ4, δε4) = coef4.nutationContribution(t)
    δψ4 should be(-32.108525656254621E-7 +- 1E-3) // Arcseconds
    δε4 should be(74.437034526377445E-7 +- 1E-3) // Arcseconds
  }

}
