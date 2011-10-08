package be.angelcorp.libs.celest.stateVector;

import org.apache.commons.math.linear.RealVector;

/**
 * This is the interface for each state vector (representation of a body's current location/velocity
 * coordinates).
 * 
 * <p>
 * StateVectors extending this class should also implement the following for convenience:
 * </p>
 * 
 * <pre>
 * public static IStateVector fromVector(RealVector vector);
 * </pre>
 * 
 * <p>
 * Furthermore it is also handy to implement the following routine:
 * </p>
 * 
 * <pre>
 * public static [StateVectorClass] as(IStateVector state, CelestialBody center);
 * </pre>
 */
public interface IStateVector {

	/**
	 * Create a new {@link StateVector} with identical properties
	 */
	public abstract IStateVector clone();

	/**
	 * Tests if two StateVectors are equal. By default, this id done by comparing all elements of the
	 * {@link StateVector#toVector()} output.
	 * 
	 * @param obj
	 *            Compare the current {@link StateVector} with this ones
	 * @return true if they are equal
	 */
	public abstract boolean equals(IStateVector obj);

	/**
	 * Convert the {@link StateVector} to an equivalent Cartesian one (R, V in Cartesian coordinates)
	 * 
	 * @return Cartesian equivalent state vector
	 */
	public abstract ICartesianElements toCartesianElements();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract String toString();

	/**
	 * Convert the current state vector to an equivalent vector form
	 * 
	 * @return Vector equivalent of the state vector
	 */
	public abstract RealVector toVector();

}