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

public class KeplerHyperbola extends KeplerEquations {

	public KeplerHyperbola(KeplerElements k) {
		super(k);
	}

	@Override
	public double arealVel(double mu, double a, double e) {
		return Math.sqrt(a * mu * (1 - e * e)) / 2;
	}

	@Override
	public double perifocalDistance(double a, double e) {
		return a * (1 - e);
	}

	@Override
	public double period(double n) {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public double totEnergyPerMass(double mu, double a) {
		return mu / (2 * a);
	}

	@Override
	public double velocitySq(double mu, double r, double a) {
		return mu / (2 / r - 1 / a);
	}

}
