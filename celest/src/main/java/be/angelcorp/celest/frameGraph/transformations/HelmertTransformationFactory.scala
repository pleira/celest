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
package be.angelcorp.celest.frameGraph.transformations

import be.angelcorp.celest.frameGraph.ReferenceSystem
import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.math.rotation.AxisAngle
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.physics.Units

/**
 * An [[be.angelcorp.celest.frameGraph.ReferenceFrameTransformFactory]] which produces transformations based on a set
 * of Helmert transformation parameters;
 * <p/>
 * <ul>
 * <li>\(T = \{T_x, T_y, T_z\}\): Translation offset between F0 and F1 [m]</li>
 * <li>\(s\): Radius scaling parameter [-]</li>
 * <li>\(R = \{\omega_x, \omega_y, \omega_z\}\): Rotation axis scaled with the rotation size [rad]</li>
 * </ul>
 * <p>
 * Optionally also there derivatives can be taken into account;
 * </p>
 * <ul>
 * <li>\(\dot{T} = \{\dot{T}_x, \dot{T}_y, \dot{T}_z\}\): Velocity between frameGraph F0 and F1 [m/s]</li>
 * <li>\(\dot{s}\): Rate of change of the scaing parameter[-/s]</li>
 * <li>\(\dot{R} = \{\dot{\omega}_x, \dot{\omega}_y, \dot{\omega}_z\}\): Rotation rate between F0 and F1
 * [rad/s]</li>
 * </ul>
 * <p>
 * In which case the Helmert parameters are recalculated for the specific epoch for which the transform
 * is generated using;
 * </p>
 * $$ X = X0 + \Delta t * \dot{X} $$
 * <p>
 * Where
 * </p>
 * <ul>
 * <li>\(X, \dot{X}\): Is one of the Helmert parameters and its derivative</li>
 * <li>\(\Delta t\): The time difference between the epoch at which the Helmert parameters where valid
 * (specified) and the epoch at for which the transformation must be generated.</li>
 * </ul>
 * <p/>
 * <p/>
 * Work based on:
 * <p/>
 * <ul>
 * <li> Gérard Petit, Brian Luzum, <b>"IERS Conventions (2010)"</b>, IERS Technical Note No. 36,
 * International Earth Rotation and Reference Systems Service (IERS), 2010, [online]
 * <a href="http://www.iers.org/TN36/">http://www.iers.org/TN36/</a></li>
 * </ul>
 *
 * @param helmert_epoch Epoch at which the initial parameters are valid.
 * @param T0            Translation between F0 and F1 at helmert_epoch: $$ T(t_0) = \{ x, y, z \} [m] $$.
 * @param dT0           Relative velocity between F0 and F1 at helmert_epoch: $$ \dot{T}(t_0) = \{ \dot{x}, \dot{y}, \dot{z} \} [m/s] $$
 * @param R0            Rotation axis, scaled with the rotation angle. This is the rotation transform between F0 and F1 at helmert_epoch: $$ R(t_0) = \{ \omega_x, \omega_y, \omega_z \} [rad] $$
 * @param dR0           Rotation rate between F0 and F1 at helmert_epoch: $$ \dot{R}(t_0) = \{ \dot{\omega}_x, \dot{\omega}_y, \dot{\omega}_z \} [rad] $$
 * @param s0            Radius scaling parameter at helmert_epoch: $$ s(t_0) = { s } [-] $$
 * @param ds0           Radius scaling parameter derivative at helmert_epoch: $$ \dot{s}(t_0) = { \dot{s} } [-/s] $$
 *
 * @tparam F0 Create a transformation from this frame.
 * @tparam F1 Create a transformation from to frame.
 * @author Simon Billemont
 */
class HelmertTransformationFactory[F0 <: ReferenceSystem, F1 <: ReferenceSystem]
(val fromFrame: F0, val toFrame: F1, val helmert_epoch: Epoch,
 val T0: Vec3, val s0: Double, val R0: Vec3, val dT0: Vec3, val ds0: Double, val dR0: Vec3)
  extends KinematicTransformationFactory[F0, F1] {

  /** {@inheritDoc} */
  override def calculateParameters(epoch: Epoch): TransformationParameters = {
    val dt = epoch.relativeToS(helmert_epoch)

    val T = T0 + (dT0 * dt)
    val R = R0 + (dR0 * dt)
    val s = s0 + ds0 * dt

    val rotation = new AxisAngle(R.normalized, R.norm)

    new TransformationParameters(epoch, T, dT0, Vec3.zero, rotation, dR0, Vec3.zero)
  }

  /** {@inheritDoc} */
  // Update parameters;
  // 4*3 vector operations = 4*3*3
  // 3 scalar operations = 3
  // Calculate rotation;
  // 2 vector operations = 2*3
  override def cost(epoch: Epoch) = 45

}

object HelmertTransformationFactory {

  /**
   * Create a {@link HelmertTransformationFactory} using non-SI units, but the units as provided in the
   * IERS bulletins:
   * <p>
   * \begin{array}{rl}
   * T & [mm] \\
   * \dot{T} & [mm/year] \\
   * S & [ppb = 10^{-9}] \\
   * \dot{S} & [ppb/year] \\
   * R & [mas = milliarcsecconds] \\
   * \dot{R} & [mas/year]
   * \end{array}
   * </p>
   *
   * @return A new { @link HelmertTransformationFactory} with the supplied parameters
   * @see HelmertTransformationFactory#HelmertTransformationFactory(be.angelcorp.celest.time.Epoch, double, double,
   *      double, double, double, double, double, double, double, double, double, double, double,
   *      double)
   */
  def fromIERSunits[F0 <: ReferenceSystem, F1 <: ReferenceSystem](from: F0, to: F1, epoch: Epoch,
                                                                  Tx: Double, Ty: Double, Tz: Double, S: Double, Rx: Double, Ry: Double, Rz: Double,
                                                                  dTx: Double, dTy: Double, dTz: Double, dS: Double, dRx: Double, dRy: Double, dRz: Double): HelmertTransformationFactory[F0, F1] = {
    fromIERSunits(from, to, epoch, Vec3(Tx, Ty, Tz), S, Vec3(Rx, Ry, Rz), Vec3(dTx, dTy, dTz), dS, Vec3(dRx, dRy, dRz))
  }

  /**
   * Create a {@link HelmertTransformationFactory} using non-SI units, but the units as provided in the
   * IERS bulletins:
   * <p>
   * \begin{array}{rl}
   * T & [mm] \\
   * \dot{T} & [mm/year] \\
   * S & [ppb = 10^{-9}] \\
   * \dot{S} & [ppb/year] \\
   * R & [mas = milliarcsecconds] \\
   * \dot{R} & [mas/year]
   * \end{array}
   * </p>
   *
   * @return A new { @link HelmertTransformationFactory} with the supplied parameters
   * @see HelmertTransformationFactory#HelmertTransformationFactory(be.angelcorp.celest.time.Epoch, Vec3, double,
   *      Vec3, Vec3, double, Vec3)
   */
  def fromIERSunits[F0 <: ReferenceSystem, F1 <: ReferenceSystem](from: F0, to: F1, epoch: Epoch, T: Vec3, S: Double, R: Vec3, dT: Vec3, dS: Double, dR: Vec3): HelmertTransformationFactory[F0, F1] = {
    val mm2m = Units.millimeter
    val y2s = Units.julianYear
    val mas2rad = Units.arcSecond(1E-3) // mas = milliarcsecond

    val T2 = T * mm2m
    val dT2 = dT * (mm2m / y2s)
    val S2 = S * 1E-9
    val dS2 = dS * 1E-9 / y2s
    val R2 = R * mas2rad
    val dR2 = dR * (mas2rad / y2s)

    new HelmertTransformationFactory(from, to, epoch, T2, S2, R2, dT2, dS2, dR2)
  }

  /**
   * Create a new instance of a {@link HelmertTransformationFactory}, using a set of Helmert
   * parameters:
   * <p>
   * $$ \{T_x, T_y, T_z, s, \omega_x, \omega_y, \omega_z\} $$
   * </p>
   * In addition also the the epoch for which these elements where specified and optionally there time
   * derivatives can be supplied.
   *
   * @param epoch Epoch at which the initial parameters are valid.
   * @param Tx    Translation x component at Helmert parameters epoch [m].
   * @param Ty    Translation y component at Helmert parameters epoch [m].
   * @param Tz    Translation z component at Helmert parameters epoch [m].
   * @param S     Scaling parameter at Helmert parameters epoch [-].
   * @param Rx    Rotation axis x component at Helmert parameters epoch [rad].
   * @param Ry    Rotation axis y component at Helmert parameters epoch [rad].
   * @param Rz    Rotation axis z component at Helmert parameters epoch [rad].
   * @param dTx   Velocity x component at Helmert parameters epoch [m/s].
   * @param dTy   Velocity y component at Helmert parameters epoch [m/s].
   * @param dTz   Velocity z component at Helmert parameters epoch [m/s].
   * @param dS    Scaling parameter derivative at Helmert parameters epoch [-/s].
   * @param dRx   Rotation rate x component at Helmert parameters epoch [rad/s].
   * @param dRy   Rotation rate y component at Helmert parameters epoch [rad/s].
   * @param dRz   Rotation rate z component at Helmert parameters epoch [rad/s].
   * @see HelmertTransformationFactory#HelmertTransformationFactory(be.angelcorp.celest.time.Epoch, Vec3, double,
   *      Vec3, Vec3, double, Vec3)
   */
  def fromSIunits[F0 <: ReferenceSystem, F1 <: ReferenceSystem](from: F0, to: F1, epoch: Epoch,
                                                                Tx: Double, Ty: Double, Tz: Double, S: Double, Rx: Double, Ry: Double, Rz: Double,
                                                                dTx: Double, dTy: Double, dTz: Double, dS: Double, dRx: Double, dRy: Double, dRz: Double) = {
    new HelmertTransformationFactory(from, to, epoch, Vec3(Tx, Ty, Tz), S, Vec3(Rx, Ry, Rz), Vec3(dTx, dTy, dTz), dS, Vec3(dRx, dRy, dRz))
  }

}
