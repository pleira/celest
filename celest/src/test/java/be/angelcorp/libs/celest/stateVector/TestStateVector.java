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

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.apache.commons.math3.linear.RealVector;

import be.angelcorp.libs.celest.state.positionState.IPositionState;
import be.angelcorp.libs.celest.unit.CelestTest;

public abstract class TestStateVector<T extends IPositionState> extends CelestTest {

	protected Class<T>	clazz;

	public TestStateVector(Class<T> clazz) {
		this.clazz = clazz;
	}

	@SuppressWarnings("unchecked")
	protected T doConvertAs(IPositionState sourceState) {
		try {
			return (T) clazz.getDeclaredMethod("as", IPositionState.class).invoke(null, sourceState);
		} catch (Exception e) {
			throw new AssertionFailedError("Could not convert using as(IStatevector) method.");
		}
	}

	@SuppressWarnings("unchecked")
	protected T doConvertFromVector(RealVector vector) {
		try {
			return (T) clazz.getDeclaredMethod("fromVector", RealVector.class).invoke(null, vector);
		} catch (Exception e) {
			throw new AssertionFailedError("Could not realvector to state using static fromVector(RealVector) method.");
		}
	}

	protected void doTestAs(T true_state, IPositionState state_to_convert) {
		T converted = doConvertAs(state_to_convert);
		equalStateVector(true_state, converted);
	}

	protected void doTestAs(T true_state, IPositionState state_to_convert, double eps) {
		T converted = doConvertAs(state_to_convert);
		equalStateVector(true_state, converted, eps);
	}

	protected void doTestVector(T state, RealVector vector_expected, double eps) {
		RealVector vector_converted = state.toVector();
		CelestTest.assertEquals(vector_expected, vector_converted, eps);

		T state2 = doConvertFromVector(vector_converted);
		equalStateVector(state, state2, eps);
	}

	protected <S extends IPositionState> void equalStateVector(S true_state, S actual) {
		Assert.assertTrue(String.format("Could not conversion (forwared) with expected: %s and true: %s",
				true_state, actual), true_state.equals(actual));
	}

	protected <S extends IPositionState> void equalStateVector(S true_state, S actual, double eps) {
		Assert.assertTrue(String.format("Could not conversion (forwared) with expected: %s and true: %s",
				true_state, actual), true_state.equals(actual, eps));
	}

	public abstract void testAs();

	public abstract void testToCartesianElements();

	public abstract void testVectorConversion();
}