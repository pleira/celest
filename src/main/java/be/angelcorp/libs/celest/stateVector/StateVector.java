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

import java.util.Iterator;

import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.linear.RealVector.Entry;

/**
 * Abstract base for a state vector that hold the state of a celestial body.
 * 
 * @author Simon Billemont
 * @see IStateVector
 */
public abstract class StateVector implements IStateVector {

	/**
	 * Restore the {@link StateVector} from a vector
	 * 
	 * @param vector
	 *            Vector to restore the the state from
	 * @return State vector as contained in the given vector
	 */
	public static IStateVector fromVector(RealVector vector) {
		throw new UnsupportedOperationException("This method must be overwritten");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract IStateVector clone();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(IStateVector obj) {
		RealVector v1 = toVector();
		RealVector v2 = obj.toVector();
		RealVector error = v1.subtract(v2);

		double max = Double.NEGATIVE_INFINITY;
		for (Iterator<Entry> it = error.iterator(); it.hasNext();) {
			Entry type = it.next();
			if (max < type.getValue())
				max = type.getValue();
		}
		return max < 1E-9;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return toVector().toString();
	}

}
