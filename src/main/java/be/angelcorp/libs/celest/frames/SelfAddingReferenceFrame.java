package be.angelcorp.libs.celest.frames;

import org.jgrapht.WeightedGraph;

import be.angelcorp.libs.celest.time.IJulianDate;

public abstract class SelfAddingReferenceFrame<F extends SelfAddingReferenceFrame<F, P>, P extends IReferenceFrame<P>>
		implements IReferenceFrame<F> {

	public SelfAddingReferenceFrame(IReferenceFrameGraph referenceFrameGraph, IReferenceFrame<?> parent) {
		WeightedGraph<IReferenceFrame<?>, IReferenceFrameTransformFactory<?, ?>> graph = referenceFrameGraph.getGraph();

		graph.addVertex(this);

		if (parent != null) {
			graph.addEdge(parent, this, getTransformFromParent());
			graph.addEdge(this, parent, getTransformToParent());
		}

	}

	protected abstract IReferenceFrameTransformFactory<P, F> getTransformFromParent();

	protected IReferenceFrameTransformFactory<F, P> getTransformToParent() {
		return new IReferenceFrameTransformFactory<F, P>() {
			@Override
			public double getCost(IJulianDate epoch) {
				return SelfAddingReferenceFrame.this.getTransformFromParent().getCost(epoch);
			}

			@Override
			public IReferenceFrameTransform<F, P> getTransform(IJulianDate epoch) {
				return SelfAddingReferenceFrame.this.getTransformFromParent().getTransform(epoch).inverse();
			}
		};
	}

}
