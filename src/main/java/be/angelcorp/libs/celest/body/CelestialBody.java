package be.angelcorp.libs.celest.body;

import be.angelcorp.libs.celest.stateVector.CartesianElements;
import be.angelcorp.libs.celest.stateVector.StateVector;

public class CelestialBody {

	public static final CelestialBody	ZERO	= new CelestialBody(new CartesianElements());
	private StateVector					state;

	public CelestialBody(StateVector state) {
		setState(state);
	}

	public StateVector getState() {
		return state;
	}

	public void setState(StateVector state) {
		this.state = state;
	}

}
