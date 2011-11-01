/**
 * Copyright (C) 2011 simon <aodtorusan@gmail.com>
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
package be.angelcorp.libs.celest.body.bodyCollection;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import be.angelcorp.libs.celest.body.CelestialBody;

/**
 * Basic implementation of a {@link IBodyCollection} using a {@link HashSet}
 * 
 * @author simon
 * 
 */
public class BasicBodyCollection extends HashSet<CelestialBody> implements IBodyCollection {

	/**
	 * Create a body collection based on a Hashset (order is not preserved!) and the given bodies
	 * 
	 * @param bodies
	 *            Bodies to add the the set.
	 */
	public BasicBodyCollection(CelestialBody... bodies) {
		addAll(Arrays.asList(bodies));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<CelestialBody> getBodies() {
		return this;
	}

}
