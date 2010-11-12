package be.angelcorp.libs.celest.stateVector;

import org.apache.commons.math.linear.RealVector;

/**
 * State vector that hold the state of a celestial body.
 * 
 * @author simon
 * 
 */
public abstract class StateVector {

	/**
	 * Restore the statevector from a vector
	 * 
	 * @param vector
	 *            Vector to restore the the state from
	 * @return State vector as contained in the given vector
	 */
	public static StateVector fromVector(RealVector vector) {
		throw new UnsupportedOperationException("This method must be overwritten");
	}

	/**
	 * Create a new Statevector with identical properties
	 */
	@Override
	public abstract StateVector clone();

	/**
	 * Convert the statevector to an equivalent Cartesian one (R, V in Cartesian coordinates)
	 * 
	 * @return Cartesian equivalent state vector
	 */
	public abstract CartesianElements toCartesianElements();

	/**
	 * Convert the current state vector to an equivalent vector form
	 * 
	 * @return Vector equivalent of the state vector
	 */
	public abstract RealVector toVector();

}
