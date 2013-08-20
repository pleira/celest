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

import be.angelcorp.libs.celest.frames.IReferenceFrame;
import be.angelcorp.libs.celest.state.Orbit;
import be.angelcorp.libs.celest.state.PosVel;
import be.angelcorp.libs.celest.universe.DefaultUniverse;
import be.angelcorp.libs.celest.universe.Universe;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.celest.time.JulianDate;
import be.angelcorp.libs.celest.unit.CelestTest;
import be.angelcorp.libs.util.physics.Time;
import scala.Option;

public class TestCompositeTrajectory extends CelestTest {

    public static Universe universe = new DefaultUniverse();

	/**
	 * test state that can return a given constant in the tovector form
	 * 
	 * @author simon
	 */
	public class TestState implements Orbit {
		private final IJulianDate	constant;

		public TestState(IJulianDate t) {
			constant = t;
		}

        @Override
        public Option<IReferenceFrame> frame() {
            throw new UnsupportedOperationException();
        }

        @Override
        public PosVel toPosVel() {
            throw new UnsupportedOperationException();
        }
    }

	/**
	 * Test trajectory that returns the time at which it is evalutated
	 * 
	 * @author simon
	 * 
	 */
	public class TestTrajectory implements ITrajectory {
		private double	id;

		public TestTrajectory(double id) {
			this.id = id;
		}

		@Override
		public Orbit evaluate(IJulianDate t) {
			return new TestState(t.add(id, Time.day));
		}

	}

	@Test
	public void testCompositeTrajectory() {
		CompositeTrajectory trajectory = new CompositeTrajectory();

		// Add various trajectories at various times
		ITrajectory t1 = new TestTrajectory(100);
		ITrajectory t2 = new TestTrajectory(200);
		ITrajectory t3 = new TestTrajectory(300);
		trajectory.addTrajectory(t1, new JulianDate(0, universe));
		trajectory.addTrajectory(t2, new JulianDate(10, universe));
		trajectory.addTrajectory(t3, new JulianDate(20, universe));

		// Equal begin time as t1
		assertEquals(((TestState)trajectory.evaluate(new JulianDate(0, universe))).constant.getJD(), 100, 1E-16);
		// In between t1 and t2, t1 should be used
        assertEquals(((TestState)trajectory.evaluate(new JulianDate(5, universe))).constant.getJD(), 105, 1E-16);
		// Same as above but s2
        assertEquals(((TestState)trajectory.evaluate(new JulianDate(15, universe))).constant.getJD(), 215, 1E-16);
        // Same insertion time as s3
        assertEquals(((TestState)trajectory.evaluate(new JulianDate(20, universe))).constant.getJD(), 320, 1E-16);
        // Time after the last insertion
        assertEquals(((TestState)trajectory.evaluate(new JulianDate(25, universe))).constant.getJD(), 325, 1E-16);
	}

	@Test(expected = ArithmeticException.class)
	public void testInalidDiscreteTrajectory() {
		CompositeTrajectory trajectory = new CompositeTrajectory();

		// Add a state a t=0
		ITrajectory t1 = new TestTrajectory(0);
		trajectory.addTrajectory(t1, new JulianDate(0, universe));

		try {
			trajectory.evaluate(new JulianDate(-1, universe)); // There is no state on or before -1 so exception
			fail("The should be not state at t=-1, because the first state is at t=0");
		} catch (ArithmeticException success) {
		}
	}

}
