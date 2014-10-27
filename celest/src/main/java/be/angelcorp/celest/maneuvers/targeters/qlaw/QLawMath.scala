package be.angelcorp.celest.maneuvers.targeters.qlaw

import be.angelcorp.celest.math.geometry.Vec3

import scala.math._
import be.angelcorp.celest.state.Keplerian
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem

trait QLawMath[F <: BodyCenteredSystem] {
  import be.angelcorp.celest.maneuvers.targeters.qlaw.QLawMath._

  def aT: Double
  def eT: Double
  def iT: Double
  def ωT: Double
  def ΩT: Double

  def Wa: Double
  def We: Double
  def Wi: Double
  def Wω: Double
  def WΩ: Double
  def Wp: Double

  def km: Double
  def kn: Double
  def kr: Double

  def acceleration: Double
  val k: Keplerian[F]

  import k._            // Imports symbols such as a/e/i/ω/Ω
  import k.quantities._ // Imports symbols such as h/p/sin??/cos??

  val coef1 = sqrt(1 - e*e * pow(sinω,2))
  val coef2 = sqrt(1 - e*e * pow(cosω,2))
  lazy val coef3 = pow(p,2) * pow(cosνxx,2) + pow(p + rxx,2) * pow(sinnuxx,2)
  lazy val coef4 = Wa * Sa * delta_a_Sq/pow(daxx, 2) + We * Se * delta_e_Sq/pow(dexx, 2) + Wi * Si * delta_i_Sq/pow(dixx, 2) + Wω * Sω * delta_ω_Sq/pow(dωxx, 2) + WΩ * SΩ * delta_Ω_Sq/pow(dΩxx, 2)

  /** Sa( a ) */
  lazy val Sa = pow(1.0 + pow(abs((a - aT)/(km * aT)), kn), 1.0/kr)
  /** Se( e ) */
  val Se = 1.0
  /** Si( i ) */
  val Si = 1.0
  /** Sω( ω ) */
  val Sω = 1.0
  /** SΩ( Ω ) */
  val SΩ = 1.0

  /** dSa( a ) / da */
  lazy val dSada = {
    val param1 = abs((a - aT)/(km * aT))
    (kn * pow(param1, kn - 1) * pow(1.0 + pow(param1, kn), 1.0/kr - 1.0) * dAbs((a - aT)/(km * aT)))/(aT*km*kr)
  }
  /** dSe( e ) / de */
  val dSede = 0.0
  /** dSi( i ) / di */
  val dSidi = 0.0
  /** dSω( ω ) / dω */
  val dSωdω = 0.0
  /** dSΩ( Ω ) / dΩ */
  val dSΩdΩ = 0.0

  /** Penalty function P( a, e, i, ω, Ω ) */
  val P    = 0.0
  /** Penalty function derivative wrt the semi-major axis: dP( a, e, i, ω, Ω ) / da */
  val dPda = 0.0
  /** Penalty function derivative wrt the eccentricity: dP( a, e, i, ω, Ω ) / de */
  val dPde = 0.0
  /** Penalty function derivative wrt the inclination: dP( a, e, i, ω, Ω ) / di */
  val dPdi = 0.0
  /** Penalty function derivative wrt the arg of pericenter: dP( a, e, i, ω, Ω ) / dω */
  val dPdω = 0.0
  /** Penalty function derivative wrt the raan: dP( a, e, i, ω, Ω ) / dΩ */
  val dPdΩ = 0.0

  /** cos( νxx(e) ) */
  lazy val cosνxx = {
    //double p1 = (1. - e * e)/(2. * pow(e, 3) );
    //double p2 = sqrt(1./4. * pow((1. - e * e) / pow(e,3),2) + 1./27.);
    //return pow(p1 + p2, 1./3.) - pow( - p1 + p2, 1./3.) - 1./e;


    // Mathematica:
    // cosnuxx[e_] := ((1 - e^2)/(2 e^3) + Sqrt[ 1/4 ((1 - e^2)/e^3)^2 + 1/27])^(1/ 3) - (-((1 - e^2)/(2 e^3)) + Sqrt[1/4 ((1 - e^2)/e^3)^2 + 1/27])^( 1/3) - 1/e;
    // ns = Evaluate[cosnuxx[e]]
    // s = FullSimplify[Normal[Series[ns, {e, 0.35, 14}]]]
    //
    // The error is about 1E-6% for 0 <= e < 0.8
    // The error rises to about 1% at e = 0.95
    // The error drop back to 0% at e = 1
    (1.6985935414010897e-6
      + e * (  -0.6667378326783717
      + e * (   0.0013875799759697537
      + e * (  -0.2389146179769917
      + e * (   0.13845623547681435
      + e * (  -0.9614141802916267
      + e * (   3.8177043592278115
      + e * ( -13.382569895662629
      + e * (  35.69553162510561
      + e * ( -73.4118970017969
      + e * ( 113.8996983750523
      + e * (-129.71843243037767
      + e * ( 102.65916156482882
      + e * ( -50.74254293851554 + 11.91197713650763 * e))))))))))))))
  }

  lazy val rxx = (a * ( 1 - e*e )) / ( 1 + e * cosνxx )
  lazy val delta_a_Sq = pow(a - aT,2)
  lazy val delta_e_Sq = pow(e - eT,2)
  lazy val delta_i_Sq = pow(i - iT,2)
  lazy val delta_ω_Sq = pow(acoscos(ω - ωT),2)
  lazy val delta_Ω_Sq = pow(acoscos(Ω - ΩT),2)

  /** νxx(e) */
  lazy val νxx = {
    acos( cosνxx )
//    Pi / 2.0
//      + (    2.0 * e)         / 3.0
//      + (   22.0 * pow(e, 3)) / 81.0
//      + (   74.0 * pow(e, 5)) / 405.0
//      + ( 2098.0 * pow(e, 7)) / 15309.0
//      + (18554.0 * pow(e, 9)) / 177147.0
//      + (50570.0 * pow(e,11)) / 649539.0
  }

  /** sin( νxx(e) ) */
  lazy val sinnuxx = sin( νxx )

  /** d(νxx(e))/de */
  lazy val dnuxxde = {
    // Series expansion of d(nuxx)/de to simplify the calculation significantly
    // Accurate to order O[e]^11.
    // Note represents the mean of d../de for values of e<0.02 as the derivatve function oscilates very heavely in this region.
    // Mathematica: Series[Evaluate[D[\[Nu]xx[e], e]], {e, 0, 10}]
    // The error for 0.004 < e < 0.01 => 3%
    //               0.01  < e < 0.6  => 0.1%
    //               0.6   < e < 0.95 => 3%
    + 2.0 / 3.0
    + (   22.0 * pow(e, 2)) / 27.0
    + (   74.0 * pow(e, 4)) / 81.0
    + ( 2098.0 * pow(e, 6)) / 2187.0
    + (18554.0 * pow(e, 8)) / 19683.0
    + (50570.0 * pow(e,10)) / 59049.0
  }

  /** daxx( a, e, acceleration ) */
  lazy val daxx =  2.0 * acceleration * sqrt(( pow(a, 3) * (1.0+e))/(k.μ * (1.0-e)))
  /** dexx( a, e, acceleration ) */
  lazy val dexx = (2.0 * p * acceleration) / h
  /** dixx( a, e, ω, acceleration ) */
  lazy val dixx = (p * acceleration) / (coef1 * h - e * h * abs(cosω))
  /** dωxx( a, e, acceleration ) */
  lazy val dωxx = (sqrt(coef3) * acceleration) / (e * h)
  /** dΩxx( a, e, ω, acceleration ) */
  lazy val dΩxx = ( p * acceleration ) / ( sini * h * (coef2 - e * abs(sinω) ))

  /** d(daxx( a, e, acceleration )) / da */
  lazy val ddaxxda = (3.0 * acceleration * sqrt((pow(a, 3)*(1 + e))/(k.μ - e*k.μ)))/a
  /** d(dexx( a, e, acceleration )) / da */
  lazy val ddexxda = -(((-1.0 + e*e) * (a*(-1.0 + e*e) + 2.0 * p) * acceleration)/(h*p))
  /** d(dixx( a, e, ω, acceleration ) / da */
  lazy val ddixxda = -((e*e - 1.0) * (a*(e*e -1.0) + 2.0 * p) * acceleration) / (2.0 * h * p * (coef1 - e * abs(cosω) ))
  /** d(dωxx( a, e, acceleration ) /da */
  lazy val ddωxxda = {
    ((e*e - 1.0) * k.μ * acceleration * (
      + coef3
      + 2.0 * a * (e*e - 1.0) * p * pow(cosνxx,2)
      - (2.0 * p * (p + rxx) * (2.0 + e * cosνxx) * pow(sinnuxx,2))/(1.0 + e * cosνxx)
    ) ) / ( 2.0 * sqrt(coef3) * e * pow(h,3))
  }
  /** d(dΩxx( a, e, ω, acceleration )) / da */
  lazy val ddΩxxda = -( (e*e - 1.0) * (a * (e*e - 1.0) + 2.0 * p) * acceleration) / ( sini * 2.0 * h * p * (coef2 - e * abs(sinω)) )

  /** d(dexx( a, e, acceleration )) / de */
  lazy val ddaxxde = (-2.0 * sqrt((pow(a,3)*(1 + e))/(k.μ - e * k.μ)) * acceleration)/(-1 + e*e)
  /** d(dixx( a, e, ω, acceleration ) / de */
  lazy val ddexxde = -(2 * a * e * acceleration) / h
  /** d(dixx( a, e, ω, acceleration ) / de */
  lazy val ddixxde =
    ( acceleration * (
        + coef1 * (e * a * (e - 1) + p) * (e * apocenter + p) * abs( cosω )
        + e * p * ( - (a * pow(coef1,2)) + p * pow(sinω,2))
      ) ) / (
      h * p * coef1 * pow(coef1 - e * abs(cosω),2)
    )
  /** d(dωxx( a, e, acceleration ) /de */
  lazy val ddωxxde =
    ( k.μ * acceleration * (
      +  2.0 * a * coef3
      - (2.0 * p * coef3) / (e*e)
      + (p * (
        +   4.0 * pow(a,2) * e * (e*e - 1.0) * pow(cosνxx,2)
        - ( 2.0 * (p + rxx) * (4.0 * a * e + cosνxx * (6.0 * a * e*e + p + 2.0 * a * pow(e,3) * cosνxx)) * pow(sinnuxx,2))/pow(1.0 + e*cosνxx,2)
        + (
        + ( 2.0 * e * p * (p + rxx) * pow(sinnuxx,3)) / pow(1.0 + e*cosνxx,2)
          + rxx*(2*p + rxx)*sin(2*νxx)
        ) * dnuxxde
      ) ) / e
    ) ) / ( 2.0 * pow(h,3) * sqrt(coef3) )
  /** d(dΩxx( a, e, ω, acceleration )) / de */
  lazy val ddΩxxde =
    ( acceleration * (
      + (a * e * (e - 1) + p) * coef2 * (p + e * apocenter) * abs(sinω)
        +  e * p * (-(a*pow(coef2,2)) + p * pow(cosω,2)))
      ) / (
      sini * h * p * coef2 * pow(coef2 - e * abs(sinω),2)
    )

  /** d(dixx( a, e, ω, acceleration ) / dω */
  lazy val ddixxdω = (e * p * acceleration * sinω * ( e * cosω - coef1 * dAbs(cosω) )) / ( h * coef1 * pow(coef1 - e * abs(cosω),2) )
  /** d(dΩxx( a, e, ω, acceleration )) / dω */
  lazy val ddΩxxdω = (e * p * cosω * acceleration * (-e * sinω + coef2 * dAbs(sinω) )) / ( h * coef2 * pow(coef2 - e * abs(sinω),2) * sini)

  /** Q( a, e, i, ω, Ω ) */
  lazy val Q = (1 + Wp * P) * coef4
  /** d(Q( a, e, i, ω, Ω )) / da */
  lazy val dQda =
    (1 + Wp * P) * (
      + (2.0 *(a - aT)  * Wa * Sa )          / pow(daxx,2)
      + (      delta_a_Sq * Wa * dSada)        / pow(daxx,2)
      - (2.0 * delta_a_Sq * Wa * Sa * ddaxxda) / pow(daxx,3)
      - (2.0 * delta_e_Sq * We * Se * ddexxda) / pow(dexx,3)
      - (2.0 * delta_i_Sq * Wi * Si * ddixxda) / pow(dixx,3)
      - (2.0 * delta_ω_Sq * Wω * Sω * ddωxxda) / pow(dωxx,3)
      - (2.0 * delta_Ω_Sq * WΩ * SΩ * ddΩxxda) / pow(dΩxx,3)
    ) + Wp * coef4 * dPda
  /** d(Q( a, e, i, ω, Ω )) / de */
  lazy val dQde =
    (1 + Wp * P) * (
      + (2.0 *(e - eT)  * We * Se)           / pow(dexx,2)
      + (      delta_e_Sq * We * dSede)        / pow(dexx,2)
      - (2.0 * delta_a_Sq * Wa * Sa * ddaxxde) / pow(daxx,3)
      - (2.0 * delta_e_Sq * We * Se * ddexxde) / pow(dexx,3)
      - (2.0 * delta_i_Sq * Wi * Si * ddixxde) / pow(dixx,3)
      - (2.0 * delta_ω_Sq * Wω * Sω * ddωxxde) / pow(dωxx,3)
      - (2.0 * delta_Ω_Sq * WΩ * SΩ * ddΩxxde) / pow(dΩxx,3)
    ) + Wp * coef4 * dPde
  /** d(Q( a, e, i, ω, Ω )) / di */
  lazy val dQdi = (1 + Wp * P) * ((i - iT) * Wi * (2.0 * Si + (i - iT) * dSidi))/pow(dixx,2) + Wp * coef4 * dPdi
  /** d(Q( a, e, i, ω, Ω )) / dω */
  lazy val dQdω = {
    (1 + Wp * P) * (
      + (2.0 * acoscos(ω - ωT) * Wω * Sω)     / pow(dωxx,2)
      + (      delta_ω_Sq * Wω * dSωdω)         / pow(dωxx,2)
      - (2.0 * delta_i_Sq * Wi *  Si * ddixxdω) / pow(dixx,3)
      - (2.0 * delta_Ω_Sq * WΩ *  SΩ * ddΩxxdω) / pow(dΩxx,3)
    ) + Wp * coef4 * dPdω
  }
  /** d(Q( a, e, i, ω, Ω )) / dΩ */
  lazy val dQdΩ = (1.0 + Wp * P) * ( WΩ * acoscos(Ω - ΩT) * (2.0 * SΩ + acoscos(Ω - ΩT) * dSΩdΩ)) / pow(dΩxx,2) + Wp * coef4 * dPdΩ

  /** F( a, e, i, ω, Ω ) */
  lazy val F =
    (acceleration / (e * h)) * (
      - p                     * cosν * dQdω
      + p * e                 * sinν * dQde
      + 2.0 * a*a * e*e  * sinν * dQda
    )
  /** G( a, e, i, ω, Ω ) */
  lazy val G =
    (acceleration / h ) * (
      + (((p + radius) * sinν) / e)       * dQdω
      + ( cosν * p + (e + cosν) * radius) * dQde
      + ((2.0 * a*a * p )/radius)         * dQda
    )
  /** H( a, e, i, ω, Ω ) */
  lazy val H =
    ((acceleration * radius) / h) * (
      + (1.0/sini)  * sinu * dQdΩ
      - (cosi/sini) * sinu * dQdω
      +               cosu * dQdi
    )



  /** d(Q( a, e, i, ω, Ω )) / dt */
  def dQdt( F: Double, G: Double, H: Double, α: Double, β: Double ) = {
    val sinα = sin(α)
    val cosα = cos(α)
    val sinβ = sin(β)
    val cosβ = cos(β)
    F * cosβ * sinα + G * cosβ * cosα + H * sinβ
  }
  /** Optimal d(Q( a, e, i, ω, Ω )) / dt over all α and β */
  lazy val (α, β, dQdt) = {
    val FSq = F * F
    val GSq = G * G
    (atan2(F, G), atan2(-H, -sqrt(FSq + GSq)), -sqrt(FSq + GSq + H * H))
  }

  /** The optimal acceleration vector over all α and β */
  def accelerationVector: Vec3 = accelerationVector( α, β )

  def accelerationVector( α: Double, β: Double ): Vec3 = {
    val sinα = sin(α); val cosα = cos(α)
    val sinβ = sin(β); val cosβ = cos(β)
    Vec3(acceleration * cosβ * sinα, acceleration * cosβ * cosα, acceleration * sinβ)
  }

  def error( k: Keplerian[F] ) =
    new Keplerian(
      if (Wa != 0) a - aT else 0,
      if (We != 0) e - eT else 0,
      if (Wi != 0) i - iT else 0,
      if (Wω != 0) acoscos(ω - ωT) else 0,
      if (WΩ != 0) acoscos(Ω - ΩT) else 0,
      0,
      frame
    )

  def targetAchieved( k: Keplerian[F] ) =
    (Wa == 0 ||     abs(a   - aT) < 0.01 * aT ) &&
      (We == 0 ||     abs(e - eT) < 0.001     ) &&
      (Wi == 0 ||     abs(i - iT) < 0.02      ) &&
      (Wω == 0 || acoscos(ω - ωT) < 0.02      ) &&
      (WΩ == 0 || acoscos(Ω - ΩT) < 0.02      ) ;

}

object QLawMath {
  /** First derivative of abs(x) */
  def dAbs(x: Double) = if (x > 0) 1 else -1 // Ignores that dAbs(0) is undefined !

  /** Computes acoscos(alpha)) */
  def acoscos(alpha: Double) = {
    //return abs( mod(alpha + PI, PI2) - PI );
    acos(cos(alpha))
  }


}
