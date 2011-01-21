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
package be.angelcorp.libs.celest.eom;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.linear.RealVector;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.constants.EarthConstants;
import be.angelcorp.libs.celest.math.CartesianMultivariateVectorFunction;
import be.angelcorp.libs.celest.stateVector.CartesianElements;
import be.angelcorp.libs.math.linear.Vector3D;

/**
 * Function that holds the calculates the acceleration in Cartesian coordinates when in the presence of
 * another spherical body
 * 
 * @author Simon Billemont
 * 
 */
public abstract class TwoBodyCartesian implements CartesianMultivariateVectorFunction {

	/**
	 * A simple twobody problem where the other body remains resembles the earth, and remains at <0,0,0>
	 */
	public static final CartesianMultivariateVectorFunction	ZERO;
	static {
		ZERO = new TwoBodyCartesian() {
			@Override
			public CelestialBody getCenterBody() {
				return new CelestialBody(new CartesianElements(), EarthConstants.mass);
			}
		};
	}

	protected abstract CelestialBody getCenterBody();

	@Override
	public RealVector value(RealVector point) throws FunctionEvaluationException {
		double[] p = point.toArray();
		Vector3D R0 = new Vector3D(p[0], p[1], p[2]);
		R0 = R0.subtract(getCenterBody().getState().toCartesianElements().getR());
		return getCenterBody().getGravitationalPotential().evaluate(R0);
	}

}
