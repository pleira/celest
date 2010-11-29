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
package be.angelcorp.libs.celest.math;

import be.angelcorp.libs.math.linear.Matrix3D;
import be.angelcorp.libs.math.linear.Vector3D;

/**
 * A set of common rotations that are performed in celestial mechanics
 * 
 * @author simon
 * 
 */
public abstract class CelestialRotate {

	/**
	 * Compute the PQW to ECI transformation matrix.
	 * 
	 * @return Matrix containing the transformation.
	 */

	public static Matrix3D PQW2ECI(double w, double raan, double i) {
		double cw = Math.cos(w);
		double sw = Math.sin(w);
		double craan = Math.cos(raan);
		double sraan = Math.sin(raan);
		double ci = Math.cos(i);
		double si = Math.sin(i);

		Matrix3D m = new Matrix3D();
		double[][] out = m.getDataRef();
		out[0][0] = craan * cw - sraan * sw * ci;
		out[0][1] = -craan * sw - sraan * cw * ci;
		out[0][2] = sraan * si;
		out[1][0] = sraan * cw + craan * sw * ci;
		out[1][1] = -sraan * sw + craan * cw * ci;
		out[1][2] = -craan * si;
		out[2][0] = sw * si;
		out[2][1] = cw * si;
		out[2][2] = ci;

		return m;
	}

	/**
	 * Compute the RSW to ECI transformation matrix.
	 * 
	 * @return Matrix containing the transformation.
	 */

	public static Matrix3D RSW2ECI(Vector3D r, Vector3D v, Vector3D h) {
		Vector3D rhat = r.normalize();
		Vector3D what = h.normalize();
		Vector3D s = what.cross(rhat);
		Vector3D shat = s.normalize();
		Matrix3D out = new Matrix3D();

		out.setColumnVector(0, rhat);
		out.setColumnVector(1, shat);
		out.setColumnVector(2, what);
		return out;
	}
}
