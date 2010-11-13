/**
 * Copyright (C) 2010 Simon Billemont <aodtorusan@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.libs.celest.orbitIntegrator;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.linear.RealVector;

import be.angelcorp.libs.celest.math.Cartesian;
import be.angelcorp.libs.celest.math.CartesianMultivariateVectorFunction;
import be.angelcorp.libs.celest.stateVector.CartesianElements;
import be.angelcorp.libs.math.integrator.rk4.Rk4Integrator;

public class CartRK4Propagator extends Rk4Integrator implements OrbitPropagator, Cartesian {

	public static CartesianElements integrationStep(double t, double dt, CartesianElements x0,
			CartesianMultivariateVectorFunction accelleration) throws FunctionEvaluationException {

		CartRK4Propagator integrator = new CartRK4Propagator(accelleration);
		RealVector result = integrator.integrationStep(t, dt, x0.toVector());

		return CartesianElements.fromVector(result);
	}

	public CartRK4Propagator(final CartesianMultivariateVectorFunction accelleration) {
		this(accelleration, 1);
	}

	public CartRK4Propagator(final CartesianMultivariateVectorFunction accelleration, double stepSize) {
		super(new CartesianMultivariateVectorFunction() {
			@Override
			public RealVector value(RealVector point) throws FunctionEvaluationException {
				/* point: [rx, ry, rz, vx, vy, vz, t] */
				/* derivative: [vx, vy, vz, ax, ay, az], note no t derivative */
				return point.getSubVector(3, 3).append(accelleration.value(point));
			}
		}, stepSize);
	}

}
