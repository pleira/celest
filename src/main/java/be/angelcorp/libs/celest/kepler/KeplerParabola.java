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

public class KeplerParabola extends KeplerEquations {

	public KeplerParabola(KeplerElements k) {
		super(k);
	}

	@Override
	public double arealVel(double mu, double a, double e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double focalParameter() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public double getApocenter() {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public double getPericenter() {
		return focalParameter() / 2;
	}

	@Override
	public double period(double n) {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public double totEnergyPerMass(double mu, double a) {
		return 0;
	}

	@Override
	public double velocitySq(double mu, double r, double a) {
		return 2 * mu / r;
	}

}
