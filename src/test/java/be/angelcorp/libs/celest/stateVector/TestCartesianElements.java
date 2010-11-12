package be.angelcorp.libs.celest.stateVector;

import be.angelcorp.libs.math.linear.Vector3D;

public class TestCartesianElements extends TestStateVector {

	@Override
	public StateVector getTestStateVector() {
		return new CartesianElements(Vector3D.random(), Vector3D.random());
	}

}
