package be.angelcorp.libs.celest.state;

import org.apache.commons.math.ode.SecondOrderDifferentialEquations;

import be.angelcorp.libs.celest.time.IJulianDate;

/**
 * A set of first differential equations that relate to a specific type of {@link IState} variables, in
 * the form of:
 * 
 * <pre>
 * dy/dt = f(t, x, y)
 * </pre>
 * 
 * If the system can be described by a set of second order differential equations, than these state
 * equations should also implement the {@link SecondOrderDifferentialEquations} interface.
 * 
 * @author Simon Billemont
 * 
 */
public interface IStateEquation<X extends IState, Y extends IState, DY extends IStateDerivative> {

	/**
	 * Compute the value of the state derivatives for a known {@link IState}: *
	 * 
	 * <pre>
	 * dy/dt = f(t, x, y)
	 * </pre>
	 * 
	 * @param t
	 *            Time of the {@link IState} evaluation,
	 * @param x
	 *            Independent state variables
	 * @param y
	 *            Dependent state variables
	 * @return The derivatives of the dependent state variables
	 */
	DY calculateDerivatives(IJulianDate t, X x, Y y);

}
