/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *        http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.celest.frameGraph.transformations;

import be.angelcorp.celest.frameGraph.ReferenceFrame;
import be.angelcorp.celest.time.Epoch;
import be.angelcorp.libs.math.linear.ImmutableVector3D;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.math.linear.Vector3D$;
import be.angelcorp.libs.math.rotation.AxisAngle;
import be.angelcorp.libs.util.physics.Angle;
import be.angelcorp.libs.util.physics.Length;
import be.angelcorp.libs.util.physics.Time;

/**
 * An {@link be.angelcorp.celest.frameGraph.ReferenceFrameTransformFactory} which produces transformations based on a set of Helmert
 * transformation parameters;
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
 * @param <F0> Create a transformation from this frame.
 * @param <F1> Create a transformation from to frame.
 * @author Simon Billemont
 */
public class HelmertTransformationFactory<F0 extends ReferenceFrame, F1 extends ReferenceFrame>
        extends KinematicTransformationFactory<F0, F1> {

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
     * @return A new {@link HelmertTransformationFactory} with the supplied parameters
     * @see HelmertTransformationFactory#HelmertTransformationFactory(be.angelcorp.celest.time.Epoch, double, double,
     * double, double, double, double, double, double, double, double, double, double, double,
     * double)
     */
    public static <F0 extends ReferenceFrame, F1 extends ReferenceFrame> HelmertTransformationFactory<F0, F1> fromIERSunits(
            Epoch epoch,
            double Tx, double Ty, double Tz, double S, double Rx, double Ry, double Rz,
            double dTx, double dTy, double dTz, double dS, double dRx, double dRy, double dRz) {
        return fromIERSunits(epoch,
                new ImmutableVector3D(Tx, Ty, Tz), S, new ImmutableVector3D(Rx, Ry, Rz),
                new ImmutableVector3D(dTx, dTy, dTz), dS, new ImmutableVector3D(dRx, dRy, dRz));
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
     * @return A new {@link HelmertTransformationFactory} with the supplied parameters
     * @see HelmertTransformationFactory#HelmertTransformationFactory(be.angelcorp.celest.time.Epoch, Vector3D, double,
     * Vector3D, Vector3D, double, Vector3D)
     */
    public static <F0 extends ReferenceFrame, F1 extends ReferenceFrame> HelmertTransformationFactory<F0, F1> fromIERSunits(
            Epoch epoch, Vector3D T, double S, Vector3D R, Vector3D dT, double dS, Vector3D dR) {
        double mm2m = Length.MILLIMETER.getScaleFactor();
        double y2s = Time.year_julian.getScaleFactor();
        double mas2rad = Angle.ArcSecond.convert(1E-3);// mas = milliarcsecond

        T = T.multiply(mm2m);
        dT = dT.multiply(mm2m / y2s);
        S = S * 1E-9;
        dS = dS * 1E-9 / y2s;
        R = R.multiply(mas2rad);
        dR = dR.multiply(mas2rad / y2s);

        return new HelmertTransformationFactory<>(epoch, T, S, R, dT, dS, dR);
    }

    /**
     * Epoch at which the initial parameters are valid.
     */
    private final Epoch helmert_epoch;
    /**
     * Translation between F0 and F1 at helmert_epoch:
     * $$ T(t_0) = \{ x, y, z \} [m] $$
     */
    private final Vector3D T0;
    /**
     * Relative velocity between F0 and F1 at helmert_epoch:
     * $$ \dot{T}(t_0) = \{ \dot{x}, \dot{y}, \dot{z} \} [m/s] $$
     */
    private final Vector3D dT0;
    /**
     * Rotation axis, scaled with the rotation angle.
     * This is the rotation transform between F0 and F1 at helmert_epoch:
     * $$ R(t_0) = \{ \omega_x, \omega_y, \omega_z \} [rad] $$
     */
    private final Vector3D R0;
    /**
     * Rotation rate between F0 and F1 at helmert_epoch:
     * $$ \dot{R}(t_0) = \{ \dot{\omega}_x, \dot{\omega}_y, \dot{\omega}_z \} [rad] $$
     */
    private final Vector3D dR0;
    /**
     * Radius scaling parameter at helmert_epoch:
     * $$ s(t_0) = { s } [-] $$
     */
    private final double s0;

    /**
     * Radius scaling parameter derivative at helmert_epoch:
     * $$ \dot{s}(t_0) = { \dot{s} } [-/s] $$
     */
    private final double ds0;

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
     * @see HelmertTransformationFactory#HelmertTransformationFactory(be.angelcorp.celest.time.Epoch, Vector3D, double,
     * Vector3D, Vector3D, double, Vector3D)
     */
    public HelmertTransformationFactory(Epoch epoch,
                                        double Tx, double Ty, double Tz, double S, double Rx, double Ry, double Rz,
                                        double dTx, double dTy, double dTz, double dS, double dRx, double dRy, double dRz) {
        this(epoch,
                new ImmutableVector3D(Tx, Ty, Tz), S, new ImmutableVector3D(Rx, Ry, Rz),
                new ImmutableVector3D(dTx, dTy, dTz), dS, new ImmutableVector3D(dRx, dRy, dRz));
    }

    /**
     * Create a new instance of a {@link HelmertTransformationFactory}, using a set of Helmert parameters
     * \(\{\vec{T}, s, \vec{R}\}\), the epoch for which these elements where specified, and there time
     * derivatives.
     *
     * @param helmert_epoch Epoch at which the initial parameters are valid.
     * @param T0            Translation at Helmert parameters epoch [m].
     * @param dT0           Velocity at Helmert parameters epoch [m/s].
     * @param R0            Rotation axis scaled with rotation angle at Helmert parameters epoch [rad].
     * @param dR0           Rotation rate of the rotation angle at Helmert parameters epoch [rad/s].
     * @param s0            Scaling parameter at Helmert parameters epoch [-].
     * @param ds0           Scaling parameter derivative at Helmert parameters epoch [-/s].
     */
    public HelmertTransformationFactory(Epoch helmert_epoch, Vector3D T0, double s0,
                                        Vector3D R0, Vector3D dT0, double ds0, Vector3D dR0) {

        this.helmert_epoch = helmert_epoch;
        this.T0 = T0;
        this.dT0 = dT0;
        this.R0 = R0;
        this.dR0 = dR0;
        this.s0 = s0;
        this.ds0 = ds0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TransformationParameters calculateParameters(Epoch epoch) {
        final double dt = epoch.relativeTo(helmert_epoch, Time.second);

        Vector3D T = T0.add(dT0.multiply(dt));
        Vector3D R = R0.add(dR0.multiply(dt));
        double s = s0 + ds0 * dt;

        AxisAngle rotation = new AxisAngle(R.normalize(), R.norm());

        TransformationParameters params = new TransformationParameters(epoch,
                T, dT0, Vector3D$.MODULE$.ZERO(), rotation, dR0, Vector3D$.MODULE$.ZERO());
        return params;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double cost(Epoch epoch) {
        // Update parameters;
        // 4*3 vector operations = 4*3*3
        // 3 scalar operations = 3
        // Calculate rotation;
        // 2 vector operations = 2*3
        return 45;
    }

}
