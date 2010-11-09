package be.angelcorp.libs.celest.stateVector;

import org.apache.commons.math.linear.RealVector;

public abstract class StateVector {

	public static StateVector fromVector(RealVector vector) {
		throw new UnsupportedOperationException("This method must be overwritten");
	}

	@Override
	public abstract StateVector clone();

	public abstract CartesianElements toCartesianElements();

	public abstract RealVector toVector();

}
