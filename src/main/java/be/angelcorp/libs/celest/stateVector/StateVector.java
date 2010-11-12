package be.angelcorp.libs.celest.stateVector;

import java.util.Iterator;

import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.linear.RealVector.Entry;

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

	public boolean equals(StateVector obj) {
		RealVector v1 = toVector();
		RealVector v2 = obj.toVector();
		RealVector error = v1.subtract(v2);

		double max = Double.NEGATIVE_INFINITY;
		for (Iterator<Entry> it = error.iterator(); it.hasNext();) {
			Entry type = it.next();
			if (max < type.getValue())
				max = type.getValue();
		}
		return max < 1E-9;
	}

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
