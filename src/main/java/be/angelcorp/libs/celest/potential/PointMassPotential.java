package be.angelcorp.libs.celest.potential;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.math.linear.Vector3D;

/**
 * Create an ideal gravitational potential of a point mass or a homogeneous sphere
 * 
 * @author simon
 * 
 */
public class PointMassPotential implements GravitationalPotential {

	/**
	 * Body that creates the current potential, (uses its mass or mu)
	 */
	private CelestialBody	body;

	/**
	 * Construct a potential based on a single celestial body
	 * 
	 * @param body
	 *            Body generating the potential
	 */
	public PointMassPotential(CelestialBody body) {
		this.body = body;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3D evaluate(Vector3D point) {
		return point.multiply(body.getMu() / Math.pow(point.getNorm(), 3)).negate();
	}
}
