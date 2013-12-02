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
package be.angelcorp.celest.body.bodyCollection;

import be.angelcorp.celest.body.CelestialBody;

/**
 * This is a collection holding always just two bodies. There are some methods to allow for getting the
 * other body in the collection if one is known.
 *
 * @author Simon Billemont
 */
public interface ITwoBodyCollection extends IBodyCollection {

    /**
     * Get the fist body in the collection (usually center body)
     */
    public abstract CelestialBody getBody1();

    /**
     * Get the second body in the collection (usually the satellites)
     */
    public abstract CelestialBody getBody2();

    /**
     * Find the other body when one body is known. If the given body is not in the collection, the first
     * body is returned.
     *
     * @param body One body in the twobody system
     * @return the other body
     */
    public abstract CelestialBody other(CelestialBody body);

}
