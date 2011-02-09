/**
 * Copyright (C) 2011 Simon Billemont <aodtorusan@gmail.com>
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 3.0 Unported
 * (CC BY-NC 3.0) (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *        http://creativecommons.org/licenses/by-nc/3.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.libs.celest.kepler;

import be.angelcorp.libs.celest.stateVector.KeplerElements;

public class KeplerCircular extends KeplerEllipse {

	public static double Vc(double r, double mhu) {
		return Math.sqrt(mhu / r);
	}

	public KeplerCircular(KeplerElements k) {
		super(k);
	}

	public double arealVel(double mu, double a) {
		return Math.sqrt(mu * a) / 2;
	}

	@Override
	public double arealVel(double mu, double a, double e) {
		return arealVel(mu, a);
	}

	/**
	 * Areal velocity in function of Semi-major axis and the Mean motion
	 * 
	 * @param a
	 *            Semi-major axis
	 * @param n
	 *            Mean motion
	 * @return Areal velocity \dot{A}
	 */
	public double arealVelFromMeanMotion(double a, double n) {
		return a * a * n / 2;
	}

	@Override
	public double focalParameter() {
		return k.getSemiMajorAxis();
	}

	@Override
	public double getApocenter() {
		return k.getSemiMajorAxis();
	}

	@Override
	public double getPericenter() {
		return k.getSemiMajorAxis();
	}

	public double perifocalDistance(double a) {
		return a;
	}

	@Override
	public double period(double n) {
		return 2 * Math.PI / n;
	}

	@Override
	public double totEnergyPerMass(double mu, double a) {
		return -mu / (2 * a);
	}

	@Override
	public double velocitySq(double mu, double r, double a) {
		return mu / r;
	}

}
