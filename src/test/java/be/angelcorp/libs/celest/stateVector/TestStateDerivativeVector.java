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
package be.angelcorp.libs.celest.stateVector;

import java.util.Collection;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.apache.commons.math3.linear.RealVector;

import be.angelcorp.libs.celest.state.positionState.IPositionStateDerivative;

public abstract class TestStateDerivativeVector<T extends IPositionStateDerivative> extends TestCase {

	public abstract Collection<T> getTestStateDerivativeVectors();

	public abstract void testToCartesianDerivative(T state);

	public void testToCartesianElements() {
		for (T state : getTestStateDerivativeVectors())
			testToCartesianDerivative(state);
	}

	public void testVectorConversion() {
		Collection<T> states = getTestStateDerivativeVectors();

		for (T state : states) {
			RealVector vector = state.toVector();
			T state2;
			try {
				state2 = (T) state.getClass().getDeclaredMethod("fromVector", RealVector.class)
						.invoke(state, vector);
			} catch (Exception e) {
				throw new AssertionFailedError("Could not invoke comparison");
			}
			assertTrue(state.equals(state2));
		}
	}

}
