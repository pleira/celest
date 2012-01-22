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
package be.angelcorp.libs.celest.trajectory;

import junit.framework.TestCase;

import org.apache.commons.math.FunctionEvaluationException;
import org.junit.Test;

import be.angelcorp.libs.celest.stateVector.CartesianElements;
import be.angelcorp.libs.celest.stateVector.StateVector;
import be.angelcorp.libs.celest.time.JulianDate;

public class TestDiscreteTrajectory extends TestCase {

	@Test(expected = FunctionEvaluationException.class)
	public void testInalidDiscreteTrajectory() {
		DiscreteTrajectory trajectory = new DiscreteTrajectory();

		// Add a state a t=0
		StateVector s1 = new CartesianElements();
		trajectory.addState(new JulianDate(0), s1);

		try {
			trajectory.evaluate(new JulianDate(-1)); // There is no state on or before -1 so exception
			fail("The should be not state at t=-1, because the first state is at t=0");
		} catch (FunctionEvaluationException success) {
		}
	}

	@Test
	public void testValidDiscreteTrajectory() throws FunctionEvaluationException {
		DiscreteTrajectory trajectory = new DiscreteTrajectory();

		// Add various states at various times
		StateVector s1 = new CartesianElements();
		StateVector s2 = new CartesianElements();
		StateVector s3 = new CartesianElements();
		trajectory.addState(new JulianDate(0), s1);
		trajectory.addState(new JulianDate(10), s2);
		trajectory.addState(new JulianDate(20), s3);

		assertEquals(s1, trajectory.evaluate(new JulianDate(0))); // Equal begin time as s1
		assertEquals(s1, trajectory.evaluate(new JulianDate(5))); // In between s1 and s2
		assertEquals(s2, trajectory.evaluate(new JulianDate(15))); // Same as above but s2
		assertEquals(s3, trajectory.evaluate(new JulianDate(20))); // Same insertion time as s3
		assertEquals(s3, trajectory.evaluate(new JulianDate(25))); // Time after the last insertion
	}

}
