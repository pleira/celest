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

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.LegendreGaussIntegrator;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.state.positionState.CartesianElements;
import be.angelcorp.libs.celest.state.positionState.IPositionState;
import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.celest.trajectory.ITrajectory;
import be.angelcorp.libs.math.functions.ExponentialSinusoid;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.util.physics.Time;

public class ExpoSinTrajectory implements ITrajectory {

	private ExponentialSinusoid	exposin;
	private double				thetaMax;
	private double				gamma;
	private CelestialBody		center;
	private IJulianDate			epoch;

	/**
	 * Create a solution trajectory to an exposin shape based solution.
	 * 
	 * @param exposin
	 *            Exposin describing the orbit
	 * @param thetaMax
	 *            Rotation angle at which the end is reached [rad]
	 * @param gamma
	 *            Start flight path angle (&gamma; at r1) [rad]
	 * @param center
	 *            Center body of the trajectory e.g. Sun
	 * @param epoch
	 *            Epoch at which the transfer starts
	 */
	public ExpoSinTrajectory(ExponentialSinusoid exposin, double thetaMax, double gamma,
			CelestialBody center, IJulianDate epoch) {
		this.exposin = exposin;
		this.thetaMax = thetaMax;
		this.gamma = gamma;
		this.center = center;
		this.epoch = epoch;
	}

	@Override
	public IPositionState evaluate(IJulianDate evalEpoch) {
		UnivariateFunction thetaDot = new UnivariateFunction() {
			@Override
			public double value(double theta) {
				double r = exposin.value(theta);
				double s = Math.sin(exposin.getK2() * theta + exposin.getPhi());
				double thetaDot = (center.getMu() / Math.pow(r, 3))
						* (1 / (Math.pow(Math.tan(gamma), 2) + exposin.getK1() * exposin.getK2()
								* exposin.getK2() * s + 1));
				thetaDot = Math.sqrt(thetaDot);
				return thetaDot;
			}
		};
		double t = evalEpoch.relativeTo(epoch, Time.second);
		double theta = new LegendreGaussIntegrator(5, 1e-12, 1e-12).integrate(50, thetaDot, 0, t);
		double r = exposin.value(theta);
		return new CartesianElements(
				new Vector3D(r * Math.cos(theta), r * Math.sin(theta), 0), Vector3D.ZERO);
	}

	public ExponentialSinusoid getExposin() {
		return exposin;
	}

	public double getGamma() {
		return gamma;
	}

	public double getThetaMax() {
		return thetaMax;
	}

}
