package be.angelcorp.libs.celest.state;

import org.apache.commons.math.linear.RealVector;

/**
 * An {@link IState} is the representation of a set of variables that are dependent on time and each
 * other. Linked to a set of {@link IState} is usually an {@link IStateEquation} which contains the first
 * and possibly second order differential equations which relate the different variables together to form
 * eg. the equations of motion.
 * 
 * @author Simon Billemont
 * 
 */
public interface IState extends Cloneable {

	/**
	 * Create a new {@link IState} with identical properties
	 */
	public abstract IState clone();

	/**
	 * Get the vector representation of all the variables in the state.
	 * 
	 * @return A statevector which contains all the state variables.
	 */
	public abstract RealVector toVector();

}
