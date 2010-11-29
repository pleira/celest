package be.angelcorp.libs.celest.potential;

import java.util.List;

import be.angelcorp.libs.math.linear.Vector3D;

/**
 * Create a GravitationalPotential that is the result of the summation of a set of independant
 * GravitationalPotentials
 * 
 * @author simon
 * 
 */
public class CumulativePotential implements GravitationalPotential {

	/**
	 * List containing potentials that are to be summed
	 */
	private List<GravitationalPotential>	potentials;

	public CumulativePotential(List<GravitationalPotential> potentials) {
		this.potentials = potentials;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3D evaluate(Vector3D point) {
		Vector3D sum = new Vector3D(0, 0, 0);
		for (GravitationalPotential pot : potentials) {
			sum = sum.add(pot.evaluate(point));
		}
		return sum;
	}

}
