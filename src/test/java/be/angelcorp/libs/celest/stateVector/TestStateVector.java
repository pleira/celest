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
package be.angelcorp.libs.celest.stateVector;

import java.util.Collection;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.apache.commons.math.linear.RealVector;

public abstract class TestStateVector<T extends StateVector> extends TestCase {

	public abstract Collection<T> getTestStateVectors();

	public abstract void testToCartesianElement(T state);

	public void testToCartesianElements() {
		for (T state : getTestStateVectors())
			testToCartesianElement(state);
	}

	public void testVectorConversion() {
		Collection<T> states = getTestStateVectors();

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
