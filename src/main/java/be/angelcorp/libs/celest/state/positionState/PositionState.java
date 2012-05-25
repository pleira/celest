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
package be.angelcorp.libs.celest.state.positionState;

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;

/**
 * Abstract base for a {@link IPositionState} that hold the positional state of a celestial body.
 * 
 * @author Simon Billemont
 * @see IPositionState
 */
public abstract class PositionState implements IPositionState {

	/**
	 * Restore the {@link PositionState} from a vector
	 * 
	 * @param vector
	 *            Vector to restore the the state from
	 * @return {@link IPositionState} as contained in the given vector
	 */
	public static IPositionState fromVector(RealVector vector) {
		throw new UnsupportedOperationException("This method must be overwritten");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(IPositionState obj) {
		RealVector v1 = toVector();
		RealVector v2 = obj.toVector();

		if (v1.getDimension() != v2.getDimension())
			return false;

		for (int i = 0; i < v1.getDimension(); i++)
			if (!Precision.equals(v1.getEntry(i), v2.getEntry(i)))
				return false;

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(IPositionState obj, double eps) {
		RealVector v1 = toVector();
		RealVector v2 = obj.toVector();

		if (v1.getDimension() != v2.getDimension())
			return false;

		for (int i = 0; i < v1.getDimension(); i++) {
			double e1 = v1.getEntry(i);
			if (!Precision.equals(e1, v2.getEntry(i), (e1 == 0) ? eps : Math.abs(e1) * eps))
				return false;
		}

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return toVector().toString();
	}

}
