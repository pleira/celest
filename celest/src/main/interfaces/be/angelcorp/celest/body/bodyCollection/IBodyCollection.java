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

import java.util.Collection;

/**
 * Holds a collection of Celestial bodies. This can be e.g. a universe or solar system
 *
 * @author simon
 */
public interface IBodyCollection {

    /**
     * Get the bodies in the container
     *
     * @return A collection of bodies in the container
     */
    public Collection<CelestialBody> getBodies();

}
