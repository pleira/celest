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
package be.angelcorp.libs.celest.potential;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.math.linear.Vector3D$;

/**
 * 
 * Implementation of a {@link IThinShellPotential}, a potential similar to a spherically symmetric mass
 * distribution outside the shell, and inside it is zero.
 * 
 * @author Simon Billemont
 * @see IThinShellPotential
 * 
 */
public class ThinShellPotential extends PointMassPotential implements IThinShellPotential {

	/**
	 * Radius of the thin shell
	 * <p>
	 * <b>Unit: [m<sup>2</sup>]</b>
	 * </p>
	 */
	private double	r2;

	/**
	 * Create the potential of a thin shell. This is equal to a spherically symmetric distribution
	 * outside the spherical shell, but inside is zero.
	 * 
	 * @param body
	 *            Body creating the potential
	 * @param r
	 *            Radius of the thin shell
	 */
	public ThinShellPotential(CelestialBody body, double r) {
		super(body);
		this.r2 = r * r;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3D evaluate(Vector3D point) {
		if (point.normSq() < r2)
			/* Valid where R is outside the shell, R >= R2 */
			/* U = Constants.GRAVITATIONAL_CONSTANT / r and is constant, so dU/dr = 0 */
			return Vector3D$.MODULE$.ZERO();
		else
			/* Valid where R is inside the shell, R <= R1 */
			/* U = GM/r = spherically symmetric */
			return super.evaluate(point);
	}

}
