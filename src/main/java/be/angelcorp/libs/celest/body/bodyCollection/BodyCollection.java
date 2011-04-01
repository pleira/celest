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
package be.angelcorp.libs.celest.body.bodyCollection;

import java.util.Collection;

import be.angelcorp.libs.celest.body.CelestialBody;

/**
 * Holds a collection of Celestial bodies.eg a universe or solar system
 * 
 * @author simon
 * 
 */
public interface BodyCollection {

	/**
	 * Get the bodies in the container
	 * 
	 * @return A collection of bodies in the container
	 */
	public Collection<CelestialBody> getBodies();

}