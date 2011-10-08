package be.angelcorp.libs.celest.stateVector;

import be.angelcorp.libs.celest.math.Cartesian;
import be.angelcorp.libs.math.linear.Vector3D;

/**
 * 
 * {@link StateVector} in the form of the Cartesian position R (x,y,z) and velocity V (&#7819;, &#7823;,
 * &#380;).
 * 
 * <pre>
 * Elements: { x,   y,   z,    &#7819;,     &#7823;,     &#380;  }
 * Units:    {[m], [m], [m], [m/s], [m/s], [m/s]}
 * </pre>
 * 
 * @author Simon Billemont
 * 
 */
public interface ICartesianElements extends Cartesian, IStateVector {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract ICartesianElements clone();

	/**
	 * Get the instantaneous position
	 * <p>
	 * <b>Unit: [m]</b>
	 * </p>
	 * 
	 * @return instantaneous position vector
	 */
	public abstract Vector3D getR();

	/**
	 * Get the instantaneous velocity
	 * <p>
	 * <b>Unit: [m/s<sup>2</sup>]</b>
	 * </p>
	 * 
	 * @return instantaneous velocity vector
	 */
	public abstract Vector3D getV();

	/**
	 * Set the instantaneous position
	 * <p>
	 * <b>Unit: [m]</b>
	 * </p>
	 * 
	 * @param r
	 *            new instantaneous position vector [m]
	 */
	public abstract void setR(Vector3D r);

	/**
	 * Set the instantaneous velocity
	 * <p>
	 * <b>Unit: [m/s<sup>2</sup>]</b>
	 * </p>
	 * 
	 * @param v
	 *            new instantaneous velocity vector [m/s^2]
	 */
	public abstract void setV(Vector3D v);

}