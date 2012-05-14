package be.angelcorp.libs.celest.frames;

import org.jgrapht.WeightedGraph;

import be.angelcorp.libs.celest.time.IJulianDate;

public interface IReferenceFrameGraph {

	public abstract WeightedGraph<IReferenceFrame<?>, IReferenceFrameTransformFactory<?, ?>> getGraph();

	public abstract <T extends IReferenceFrame<T>, V extends IReferenceFrame<V>> IReferenceFrameTransform<T, V>
			getTransform(T from, V to, IJulianDate epoch) throws ReferenceFrameTransformationException;

}