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
package be.angelcorp.libs.celest.stateVector;

import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.util.MathUtils;

/**
 * Basis for a state derivative vector that hold the state of a celestial body. This implements a default
 * human readable toString using the vector notation if the state derivative.
 * 
 * @author Simon Billemont
 * @see IStateDerivativeVector
 */
public abstract class StateDerivativeVector implements IStateDerivativeVector {

	/**
	 * Restore the StateDerivativeVector from a vector
	 * 
	 * @param vector
	 *            Vector to restore the the state from
	 * @return State vector as contained in the given vector
	 */
	public static IStateDerivativeVector fromVector(RealVector vector) {
		throw new UnsupportedOperationException("This method must be overwritten");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract IStateDerivativeVector clone();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(IStateVector obj) {
		RealVector v1 = toVector();
		RealVector v2 = obj.toVector();

		if (v1.getDimension() != v2.getDimension())
			return false;

		for (int i = 0; i < v1.getDimension(); i++)
			if (!MathUtils.equals(v1.getEntry(i), v2.getEntry(i)))
				return false;

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(IStateVector obj, double eps) {
		RealVector v1 = toVector();
		RealVector v2 = obj.toVector();

		if (v1.getDimension() != v2.getDimension())
			return false;

		for (int i = 0; i < v1.getDimension(); i++) {
			if (!MathUtils.equals(v1.getEntry(i), v2.getEntry(i), v1.getEntry(i) * eps))
				return false;
		}

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract CartesianDerivative toCartesianDerivative();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return toVector().toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract RealVector toVector();
}
