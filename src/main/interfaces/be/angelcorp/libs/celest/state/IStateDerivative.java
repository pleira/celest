package be.angelcorp.libs.celest.state;

/**
 * An {@link IStateDerivative} is the representation of the derivative variables of a specific
 * {@link IState} type for a specific set of conditions.
 * 
 * @author Simon Billemont
 * 
 */
public interface IStateDerivative extends IState {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IStateDerivative clone();

}
