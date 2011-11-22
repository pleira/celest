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

import java.util.Set;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.apache.commons.math.linear.RealVector;

public abstract class TestStateVector<T extends IStateVector> extends TestCase {

	public Set<StateVectorComparisonCase<? extends IStateVector, T>>	testStateCases	= getTestStateVectors();

	public abstract Set<StateVectorComparisonCase<? extends IStateVector, T>> getTestStateVectors();

	public void testAs() {
		for (StateVectorComparisonCase<? extends IStateVector, T> test : testStateCases)
			test.runForwardTest();
	}

	public void testCartesianElements() {
		boolean anyCartesianElements = false;
		for (StateVectorComparisonCase<? extends IStateVector, T> test : testStateCases)
			if (ICartesianElements.class.isAssignableFrom(test.testInitiationState.getClass())) {
				anyCartesianElements = true;
				test.runReverseTest();
			}
		if (!anyCartesianElements)
			throw new AssertionFailedError(String.format(
					"Could not find any cartesian elements to test toCartesianElements with in %s",
					getClass().getSuperclass()));
	}

	public void testVectorConversion() {
		for (StateVectorComparisonCase<? extends IStateVector, T> test : testStateCases) {
			T state = test.testValidationState;
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
