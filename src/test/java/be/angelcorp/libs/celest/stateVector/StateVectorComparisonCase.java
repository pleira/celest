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

	public StateVectorComparisonCase(InitiationState testInitiationState, ValidationState testValidationState) {
		this.testInitiationState = testInitiationState;
		this.testValidationState = testValidationState;
	}

	protected void doCompare(ValidationState validationState, ValidationState testState) {
		Assert.assertTrue(String.format("Could not conversion with expected: %s and true: %s",
				validationState, testState), validationState.equals(testState));
	}

	protected ValidationState doConvert(InitiationState sourceState) {
		try {
			return (ValidationState) sourceState.getClass().getDeclaredMethod("as", IStateVector.class)
					.invoke(null, sourceState);
		} catch (Throwable e) {
			throw new GenericRuntimeException(e, "Could not invoke comparison");
		}
	}

	public void runTest() {
		ValidationState converted = doConvert(testInitiationState);
		doCompare(testValidationState, converted);
	}

}
