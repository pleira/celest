/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
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

package be.angelcorp.celest.body;

import be.angelcorp.celest.math.geometry.Vec3;

/**
 * Representation of a celestial body with a surface geometry.
 *
 * @author Simon Billemont
 */
public interface IShapedBody extends CelestialBody {

    /**
     * Get the cross-sectional area of a body, when looked upon from the provided direction (observer to this body).
     *
     * @param direction Observation direction (vector observer -> body).
     * @return The cross-sectional area of this body [m^2].
     */
    double crossSection(Vec3 direction);

}
