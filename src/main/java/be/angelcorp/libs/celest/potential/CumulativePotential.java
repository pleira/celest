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

import java.util.List;

import be.angelcorp.libs.math.linear.Vector3D;

/**
 * Create a GravitationalPotential that is the result of the summation of a set of independant
 * GravitationalPotentials
 * 
 * @author simon
 * 
 */
public class CumulativePotential implements GravitationalPotential {

	/**
	 * List containing potentials that are to be summed
	 */
	private List<GravitationalPotential>	potentials;

	public CumulativePotential(List<GravitationalPotential> potentials) {
		this.potentials = potentials;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3D evaluate(Vector3D point) {
		Vector3D sum = new Vector3D(0, 0, 0);
		for (GravitationalPotential pot : potentials) {
			sum = sum.add(pot.evaluate(point));
		}
		return sum;
	}

}
