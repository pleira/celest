package be.angelcorp.libs.celest.trajectory;

import org.apache.commons.math.FunctionEvaluationException;

import be.angelcorp.libs.celest.kepler.KeplerEquations;
import be.angelcorp.libs.celest.stateVector.KeplerElements;
import be.angelcorp.libs.celest.stateVector.StateVector;

public class KeplerTrajectory extends Trajectory {

	private final KeplerElements	k;

	public KeplerTrajectory(KeplerElements k) {
		this.k = k;
	}

	@Override
	public StateVector evaluate(double t) throws FunctionEvaluationException {
		double n = KeplerEquations.meanMotion(k.getCenterbody().getMu(), k.getSemiMajorAxis());
		double dM = n * t;
		KeplerElements k2 = k.clone();
		k2.setTrueAnomaly(k2.getOrbitEqn().trueAnomalyFromMean(k.getMeanAnomaly() + dM));
		return k2;
	}

}
