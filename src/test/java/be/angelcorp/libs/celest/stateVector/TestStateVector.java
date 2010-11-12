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
