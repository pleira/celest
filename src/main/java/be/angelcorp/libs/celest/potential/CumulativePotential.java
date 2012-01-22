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

import java.util.Arrays;
import java.util.List;

import be.angelcorp.libs.math.linear.Vector3D;

import com.google.common.collect.Lists;

/**
 * Implementation of {@link ICumulativePotential}
 * 
 * @author simon
 * @see ICumulativePotential
 */
public class CumulativePotential implements ICumulativePotential {

	/**
	 * List containing potentials that are to be summed
	 */
	private List<IGravitationalPotential>	potentials;

	/**
	 * Create a potential resulting from a set of other gravity potentials
	 * 
	 * @param potentials
	 *            List of other potentials
	 */
	public CumulativePotential(IGravitationalPotential... potentials) {
		this.potentials = Lists.newLinkedList(Arrays.asList(potentials));

	}

	/**
	 * Create a potential resulting from a set of other gravity potentials
	 * 
	 * @param potentials
	 *            List of other potentials
	 */
	public CumulativePotential(List<IGravitationalPotential> potentials) {
		this.potentials = potentials;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3D evaluate(Vector3D point) {
		Vector3D sum = new Vector3D(0, 0, 0);
		for (IGravitationalPotential pot : potentials) {
			sum = sum.add(pot.evaluate(point));
		}
		return sum;
	}

}
