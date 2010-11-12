package be.angelcorp.libs.celest.eom;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.linear.RealVector;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.constants.EarthConstants;
import be.angelcorp.libs.celest.math.CartesianMultivariateVectorFunction;
import be.angelcorp.libs.celest.stateVector.CartesianElements;
import be.angelcorp.libs.math.linear.Vector3D;

/**
 * Function that holds the calculates the acceleration in Cartesian coordinates when in the presence of
 * another spherical body
 * 
 * @author Simon Billemont
 * 
 */
public abstract class TwoBodyCartesian implements CartesianMultivariateVectorFunction {

	/**
	 * A simple twobody problem where the other body remains resembles the earth, and remains at <0,0,0>
	 */
	public static final CartesianMultivariateVectorFunction	ZERO;
	static {
		ZERO = new TwoBodyCartesian() {
			@Override
			public CelestialBody getCenterBody() {
				return new CelestialBody(new CartesianElements(), EarthConstants.mass);
			}
		};
	}

	protected abstract CelestialBody getCenterBody();

	@Override
	public RealVector value(RealVector point) throws FunctionEvaluationException,
			IllegalArgumentException {
		Vector3D R0 = new Vector3D(point.getSubVector(0, 3).toArray());
		R0 = R0.subtract(getCenterBody().getState().toCartesianElements().getR());
		Vector3D a = R0.multiply(getCenterBody().getMu() / (Math.pow(R0.getNorm(), 3))).negate();
		return a;
	}

}
