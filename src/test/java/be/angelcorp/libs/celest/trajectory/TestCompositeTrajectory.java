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
package be.angelcorp.libs.celest.trajectory;

import junit.framework.TestCase;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealVector;
import org.junit.Test;

import be.angelcorp.libs.celest.stateVector.ICartesianElements;
import be.angelcorp.libs.celest.stateVector.IStateVector;
import be.angelcorp.libs.celest.unit.Tests;

public class TestCompositeTrajectory extends TestCase {

	/**
	 * test state that can return a given constant in the tovector form
	 * 
	 * @author simon
	 */
	public class TestState implements IStateVector {
		private final double	constant;

		public TestState(double d) {
			constant = d;
		}

		@Override
		public TestState clone() {
			throw new UnsupportedOperationException("Not implemented yet");
		}

		@Override
		public boolean equals(IStateVector obj) {
			if (TestState.class.isAssignableFrom(obj.getClass()))
				return ((TestState) obj).constant == constant;
			return false;
		}

		@Override
		public ICartesianElements toCartesianElements() {
			throw new UnsupportedOperationException();
		}

		@Override
		public RealVector toVector() {
			return new ArrayRealVector(new double[] { constant });
		}

	}

	/**
	 * Test trajectory that returns the time at which it is evalutated
	 * 
	 * @author simon
	 * 
	 */
	public class TestTrajectory implements ITrajectory {

		@Override
		public IStateVector evaluate(double t) throws FunctionEvaluationException {
			return new TestState(t);
		}

	}

	@Test
	public void testCompositeTrajectory() throws FunctionEvaluationException {
		CompositeTrajectory trajectory = new CompositeTrajectory();

		// Add various trajectories at various times
		ITrajectory t1 = new TestTrajectory();
		ITrajectory t2 = new TestTrajectory();
		ITrajectory t3 = new TestTrajectory();
		trajectory.addTrajectory(t1, 0);
		trajectory.addTrajectory(t2, 10);
		trajectory.addTrajectory(t3, 20);

		// Equal begin time as t1
		Tests.assertEquals(new double[] { 0 }, trajectory.evaluate(0).toVector().getData(), 1E-16);
		// In between t1 and t2, t1 should be used
		Tests.assertEquals(new double[] { 5 }, trajectory.evaluate(5).toVector().getData(), 1E-16);
		// Same as above but s2
		Tests.assertEquals(new double[] { 5 }, trajectory.evaluate(15).toVector().getData(), 1E-16);
		// Same insertion time as s3
		Tests.assertEquals(new double[] { 0 }, trajectory.evaluate(20).toVector().getData(), 1E-16);
		// Time after the last insertion
		Tests.assertEquals(new double[] { 5 }, trajectory.evaluate(25).toVector().getData(), 1E-16);
	}

	@Test(expected = FunctionEvaluationException.class)
	public void testInalidDiscreteTrajectory() {
		CompositeTrajectory trajectory = new CompositeTrajectory();

		// Add a state a t=0
		ITrajectory t1 = new TestTrajectory();
		trajectory.addTrajectory(t1, 0);

		try {
			trajectory.evaluate(-1); // There is no state on or before -1 so exception
			fail("The should be not state at t=-1, because the first state is at t=0");
		} catch (FunctionEvaluationException success) {
		}
	}

}
