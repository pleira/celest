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
package be.angelcorp.libs.celest.maneuvers.targeters.exposin;

import static org.apache.commons.math3.util.FastMath.abs;
import static org.apache.commons.math3.util.FastMath.cos;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.Inverse;
import org.apache.commons.math3.analysis.integration.LegendreGaussIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.analysis.solvers.RiddersSolver;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.maneuvers.targeters.TPBVP;
import be.angelcorp.libs.celest.state.positionState.CartesianElements;
import be.angelcorp.libs.celest.state.positionState.ICartesianElements;
import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.celest.trajectory.ITrajectory;
import be.angelcorp.libs.math.functions.ExponentialSinusoid;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.math.rotation.IRotation;
import be.angelcorp.libs.math.rotation.RotationMatrix;
import be.angelcorp.libs.util.physics.Time;

/**
 * Creates an {@link ITrajectory} from a known {@link ExpoSin} solution to the {@link TPBVP}. This allows
 * you to perform all common {@link ITrajectory} operations of that {@link ExpoSin} solution.
 * 
 * @author Simon Billemont
 */
public class ExpoSinTrajectory implements ITrajectory {

	private ExponentialSinusoid	exposin;
	private CelestialBody		center;
	private IJulianDate			epoch;
	private double				gamma;

	/**
	 * Create a solution trajectory to an exposin shape based solution.
	 * 
	 * @param exposin
	 *            Exposin describing the orbit
	 * @param center
	 *            Center body of the trajectory e.g. Sun
	 * @param epoch
	 *            Epoch at which the transfer starts
	 */
	public ExpoSinTrajectory(ExponentialSinusoid exposin, CelestialBody center, IJulianDate epoch) {
		this(exposin, Double.NaN, center, epoch);
	}

	/**
	 * Create a solution trajectory to an exposin shape based solution.
	 * 
	 * @param exposin
	 *            Exposin describing the orbit
	 * @param gamma
	 *            Value of the flight path angle at departure for this specific solution
	 * @param center
	 *            Center body of the trajectory e.g. Sun
	 * @param epoch
	 *            Epoch at which the transfer starts
	 */
	public ExpoSinTrajectory(ExponentialSinusoid exposin, double gamma, CelestialBody center, IJulianDate epoch) {
		this.exposin = exposin;
		this.gamma = gamma;
		this.center = center;
		this.epoch = epoch;
	}

	/**
	 * Returns the planar postion in the trajectory at the given time. The position at departure is
	 * &lt;r1, 0, 0&gt;, and moves around in the XY plane over time.
	 */
	@Override
	public ICartesianElements evaluate(IJulianDate evalEpoch) {
		// Travel time from the start position [s]
		final double t = evalEpoch.relativeTo(epoch, Time.second);

		// Equation of d(theta)/dt
		final ExpoSinAngularRate thetaDot = new ExpoSinAngularRate(exposin, center.getMu());

		// Find the angle (theta) of the satellite at the time t
		double theta = 0;
		if (abs(t) > 1E-16) {
			// Equation of dt/d(theta) - t
			UnivariateFunction rootFunction = new UnivariateFunction() {
				UnivariateIntegrator	integrator	= new LegendreGaussIntegrator(3, 1e-8, 1e-8);

				@Override
				public double value(double theta) {
					double tof = integrator.integrate(102400, FunctionUtils.compose(new Inverse(), thetaDot), 0, theta);
					return tof - t;
				}
			};
			double est = thetaDot.value(0) * t;

			// Find the point where the tof(theta) == t
			theta = new RiddersSolver().solve(64, rootFunction, 1E-6, 2 * est, est);
		}

		// Find the radius for the current theta
		double c = cos(exposin.getK2() * theta + exposin.getPhi());
		double theta_dot = thetaDot.value(theta);
		double r = exposin.value(theta);
		double r_dot = theta_dot * (exposin.getQ0() + exposin.getK1() * exposin.getK2() * c) * r;

		// Compose the radial vector
		Vector3D R_norm = new Vector3D(Math.cos(theta), Math.sin(theta), 0);
		Vector3D R = R_norm.multiply(r);

		// Compute the scale of the velocity components
		double V_r = r_dot;
		double V_theta = r * theta_dot;
		Vector3D V_theta_norm = Vector3D.K.cross(R_norm);

		// TODO: make inherit this rotation from the ExpoSin input states
		IRotation rotation = RotationMatrix.NO_ROTATION;
		// Compose the velocity vector from radial and tangential velocities
		Vector3D V = rotation.applyTo(new Vector3D(V_r, R.normalize(), V_theta, V_theta_norm));

		// Convert to the position in cartesian elements (in plane coordinates)
		return new CartesianElements(R, V);
	}

	/**
	 * Retrieve the {@link ExponentialSinusoid} that describes the trajectory
	 * 
	 * @return The internal {@link ExponentialSinusoid} used to compute trajectory positions
	 */
	public ExponentialSinusoid getExposin() {
		return exposin;
	}

	/**
	 * Get the value of the flight path angle &gamma; at departure (r1)
	 * <p>
	 * Note: this value is optional and might be {@link Double#NaN} if it was not set.
	 * </p>
	 * 
	 * @return Flight path angle at r1 [rad]
	 */
	public double getGamma() {
		return gamma;
	}

}
