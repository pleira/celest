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
package be.angelcorp.celest.math;

import be.angelcorp.libs.math.linear.ImmutableMatrix3D;
import be.angelcorp.libs.math.linear.Matrix3D;
import be.angelcorp.libs.math.linear.Matrix3D$;
import be.angelcorp.libs.math.linear.Vector3D;

/**
 * A set of common rotations that are performed in celestial mechanics
 *
 * @author simon
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

        double m00 = craan * cw - sraan * sw * ci;
        double m01 = -craan * sw - sraan * cw * ci;
        double m02 = sraan * si;
        double m10 = sraan * cw + craan * sw * ci;
        double m11 = -sraan * sw + craan * cw * ci;
        double m12 = -craan * si;
        double m20 = sw * si;
        double m21 = cw * si;
        double m22 = ci;

        return new ImmutableMatrix3D(m00, m01, m02, m10, m11, m12, m20, m21, m22);
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

        return Matrix3D$.MODULE$.columns(rhat, shat, what);
    }
}
