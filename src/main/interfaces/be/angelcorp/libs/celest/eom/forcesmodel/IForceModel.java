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

package be.angelcorp.libs.celest.eom.forcesmodel;

import be.angelcorp.libs.celest.body.ICelestialBody;
import be.angelcorp.libs.celest.physics.quantities.Force;
import be.angelcorp.libs.celest.time.IJulianDate;

/**
 * Force model acting on a body.
 *
 * @param <B> Type of body that this model acts on.
 */
public interface IForceModel<B extends ICelestialBody> {

    /**
     * Compute the force acting on the body with a known state and on a known time.
     *
     * @param body Body on which the force acts.
     * @param t Time/date when the force acts.
     * @return The acting force.
     */
    Force evaluate( B body, IJulianDate t);

}
