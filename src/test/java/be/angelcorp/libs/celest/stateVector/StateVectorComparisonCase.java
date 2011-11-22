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

import junit.framework.Assert;
import be.angelcorp.libs.util.exceptions.GenericRuntimeException;

public class StateVectorComparisonCase<InitiationState extends IStateVector, ValidationState extends IStateVector> {

	public final InitiationState	testInitiationState;
	public final ValidationState	testValidationState;

	/**
	 * equals eps when testing:
	 * 
	 * <pre>
	 * testValidationState == convert(testInitiationState)
	 * </pre>
	 */
	public Double					eps_forward	= null;
	/**
	 * equals eps when testing:
	 * 
	 * <pre>
	 * testValidationState.toCartesianElements() == testInitiationState.toCartesianElements()
	 * </pre>
	 */
	public Double					eps_reverse	= null;

	public StateVectorComparisonCase(InitiationState testInitiationState, ValidationState testValidationState) {
		this.testInitiationState = testInitiationState;
		this.testValidationState = testValidationState;
	}

	protected void doForwardCompare(ValidationState validationState, ValidationState testState) {
		if (eps_forward != null)
			Assert.assertTrue(String.format("Could not conversion (forwared) with expected: %s and true: %s",
					validationState, testState), validationState.equals(testState, eps_forward));
		else
			Assert.assertTrue(String.format("Could not conversion (forwared) with expected: %s and true: %s",
					validationState, testState), validationState.equals(testState));
	}

	protected ValidationState doForwardConvert(InitiationState sourceState) {
		try {
			return (ValidationState) sourceState.getClass().getDeclaredMethod("as", IStateVector.class)
					.invoke(null, sourceState);
		} catch (Throwable e) {
			throw new GenericRuntimeException(e, "Could not invoke comparison");
		}
	}

	protected void doReverseCompare(ICartesianElements validationState, ICartesianElements testState) {
		if (eps_reverse != null) {
			boolean value = validationState.equals(testState, eps_reverse);
			Assert.assertTrue(String.format("Could not conversion (reverse) with expected: %s and true: %s",
					validationState, testState), value);
		} else {
			boolean value = validationState.equals(testState);
			Assert.assertTrue(String.format("Could not conversion (reverse) with expected: %s and true: %s",
					validationState, testState), value);
		}
	}

	protected ICartesianElements doReverseConvert(ValidationState testValidationState2) {
		return testValidationState2.toCartesianElements();
	}

	public void runForwardTest() {
		ValidationState converted = doForwardConvert(testInitiationState);
		doForwardCompare(testValidationState, converted);
	}

	public void runReverseTest() {
		ICartesianElements converted = doReverseConvert(testValidationState);
		doReverseCompare(testInitiationState.toCartesianElements(), converted);
	}

	public StateVectorComparisonCase<InitiationState, ValidationState> setEps_forward(Double eps_forward) {
		this.eps_forward = eps_forward;
		return this;
	}

	public StateVectorComparisonCase<InitiationState, ValidationState> setEps_reverse(Double eps_reverse) {
		this.eps_reverse = eps_reverse;
		return this;
	}
}
