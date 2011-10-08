package be.angelcorp.libs.celest.stateVector;

import org.apache.commons.math.linear.RealVector;

/**
 * This hold the derivatives of the state of a given body
 * 
 * <p>
 * StateDerivatives extending this class should also implement the following for convenience:
 * </p>
 * 
 * <pre>
 * public static IStateDerivativeVector fromVector(RealVector vector);
 * </pre>
 * 
 * @author simon
 * 
 */
public interface IStateDerivativeVector {

	/**
	 * Create a new StateDerivativeVector with identical properties
	 */
	public abstract IStateDerivativeVector clone();

	/**
	 * Convert the StateDerivativeVector to an equivalent Cartesian one (V,A in Cartesian coordinates)
	 * 
	 * @return Cartesian equivalent state derivative vector
	 */
	public abstract CartesianDerivative toCartesianDerivative();

	@Override
	public abstract String toString();

	/**
	 * Convert the current state vector to an equivalent vector form
	 * 
	 * @return Vector equivalent of the state vector
	 */
	public abstract RealVector toVector();

}