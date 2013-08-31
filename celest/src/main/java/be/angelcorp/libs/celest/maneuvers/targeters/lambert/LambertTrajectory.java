/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
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
package be.angelcorp.libs.celest.maneuvers.targeters.lambert;

import be.angelcorp.libs.celest.time.Epoch;
import be.angelcorp.libs.celest.trajectory.Trajectory;
import scala.Some;
import be.angelcorp.libs.celest.frames.BodyCentered;
import be.angelcorp.libs.celest.state.Keplerian;
import be.angelcorp.libs.celest.state.Orbit;
import be.angelcorp.libs.celest.state.PosVel;
import be.angelcorp.libs.celest.trajectory.KeplerTrajectory;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.util.physics.Time;

public class LambertTrajectory implements Trajectory {

	/**
	 * Initial inertial position
	 */
	private final Vector3D		r1;
	/**
	 * Final inertial position
	 */
	private final Vector3D		r2;

	/**
	 * F Part of the f and g functions (not series)
	 */
	private final double		f;
	/**
	 * G Part of the f and g functions (not series)
	 */
	private final double		g;
	/**
	 * G_dot Part of the f and g functions (not series)
	 */
	private final double		g_dot;
	/**
	 * Time at r1
	 */
	private final Epoch departureEpoch;
	/**
	 * Time at r2
	 */
	private final Epoch arrivalEpoch;
	/**
	 * Frame in which the motion takes place
	 */
	private final BodyCentered frame;

	/**
	 * This is the trajectory that the satellite flies from r1 to r2
	 */
	private KeplerTrajectory	traj;

	/**
	 * Create a solution to the classical Lambert problem (pure keplerian two-point boundary value
	 * problem).
	 * 
	 * @param r1
	 *            The origin of the transfer orbit [m]
	 * @param r2
	 *            The destination of the transfer orbit [m]
	 * @param frame
	 *            Frame in which the motion takes place
	 * @param departureEpoch
	 *            Epoch of departure
     * @param arrivalEpoch
     *            Epoch of arrival
	 * @param f
	 *            F function (of the F and G functions, NOT SERIES)
	 * @param g
	 *            G function (of the F and G functions, NOT SERIES)
	 * @param g_dot
	 *            G dot function (of the F and G functions, NOT SERIES)
	 */
	public LambertTrajectory(Vector3D r1, Vector3D r2, BodyCentered frame, Epoch departureEpoch,
			Epoch arrivalEpoch, double f, double g, double g_dot) {
		this.r1 = r1;
		this.r2 = r2;
		this.frame = frame;
		this.departureEpoch = departureEpoch;
		this.arrivalEpoch = arrivalEpoch;
		this.f = f;
		this.g = g;
		this.g_dot = g_dot;
		mkTrajectory();
	}

	/**
	 * Evaluate the unperturbed trajectory.
	 * <p>
	 * {@inheritDoc}
	 * </p>
	 */
	@Override
	public Orbit apply(Epoch t) {
		return traj.apply(t);
	}

	/**
	 * Get the frame in which the motion takes place
	 */
	public BodyCentered getFrame() {
		return frame;
	}

	/**
	 * Get the transfer time from point r1 to r2.
	 * 
	 * @return Transfer time [s]
	 */
	public double getDt() {
		return departureEpoch.relativeTo(arrivalEpoch, Time.second);
	}

	/**
	 * @return F part of the f and g functions (not series)
	 */
	public double getF() {
		return f;
	}

	/**
	 * @return G part of the f and g functions (not series)
	 */
	public double getG() {
		return g;
	}

	/**
	 * @return G dot part of the f and g functions (not series)
	 */
	public double getG_dot() {
		return g_dot;
	}

	/**
	 * Get the arrival point of this lamber solution
	 * 
	 * @return Inertial end position r2 [m]
	 */
	public Vector3D getR1() {
		return r1;
	}

	/**
	 * Get the point of origin of this lamber solution
	 * 
	 * @return Inertial start position r1 [m]
	 */
	public Vector3D getR2() {
		return r2;
	}

	/**
	 * Get the inertial velocity at R1 needed to fly the trajectory to R2
	 * 
	 * @return Inertial velocity at R1 [m/s]
	 */
	public Vector3D getV1() {
		return r2.$minus(r1.$times(f)).$div(g);
	}

	/**
	 * Get the inertial velocity when arriving R2
	 * 
	 * @return Inertial velocity at R2 [m/s]
	 */
	public Vector3D getV2() {
		return r2.multiply(g_dot).$minus(r1).divide(g);
	}

	/**
	 * Create an unperturbed trajectory leading from point r1 to r2 around the given center body.
	 */
	private void mkTrajectory() {
        Keplerian k = Keplerian.apply( new PosVel(r1.clone(), getV1(), new Some<BodyCentered>(frame)));
		traj = new KeplerTrajectory(departureEpoch, k);
	}
}
