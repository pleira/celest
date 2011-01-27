package be.angelcorp.libs.celest.trajectory;

import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.math.FunctionEvaluationException;

import be.angelcorp.libs.celest.stateVector.StateVector;

import com.google.common.collect.Maps;

public class DiscreteTrajectory extends Trajectory {

	private TreeMap<Double, StateVector>	states	= Maps.newTreeMap();

	public DiscreteTrajectory() {

	}

	public DiscreteTrajectory(StateVector state) {
		addState(0, state);
	}

	public void addState(double t, StateVector state) {
		states.put(t, state);
	}

	@Override
	public StateVector evaluate(double t) throws FunctionEvaluationException {
		Entry<Double, StateVector> entry = states.floorEntry(t);
		if (entry == null)
			throw new FunctionEvaluationException(t, "No state found before the given time index");
		return entry.getValue();
	}

}
