/**
 * Copyright (C) 2010 Simon Billemont <aodtorusan@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.libs.celest.stateVector;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.apache.commons.math.linear.RealVector;

public abstract class TestStateVector extends TestCase {

	public abstract StateVector getTestStateVector();

	public void testVectorConversion() {
		StateVector state = getTestStateVector();

		RealVector vector = state.toVector();
		StateVector state2;
		try {
			state2 = (StateVector) state.getClass().getDeclaredMethod("fromVector", RealVector.class)
							.invoke(state, vector);
		} catch (Exception e) {
			throw new AssertionFailedError("Could not invoke comparison");
		}

		assertTrue(state.equals(state2));
	}

}
