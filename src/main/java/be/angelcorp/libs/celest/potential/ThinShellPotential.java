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
package be.angelcorp.libs.celest.potential;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.math.linear.Vector3D;

/**
 * Create an ideal gravitational potential of thin spherical shell. This is equivalent to a point mass,
 * when the point considered is outside the shell and when inside the shell 0(see Gauss' law for
 * gravity).
 * 
 * @author simon
 * 
 */
public class ThinShellPotential extends PointMassPotential {

	/**
	 * Radius square of the thin shell
	 * <p>
	 * <b>Unit: [m<sup>2</sup>]</b>
	 * </p>
	 */
	private double	r2;

	public ThinShellPotential(CelestialBody body, double r) {
		super(body);
		this.r2 = r * r;
	}

	@Override
	public Vector3D evaluate(Vector3D point) {
		if (point.getNormSq() < r2)
			return Vector3D.ZERO;
		else
			return super.evaluate(point);
	}

}
