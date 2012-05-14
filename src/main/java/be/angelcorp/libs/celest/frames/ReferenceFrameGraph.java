package be.angelcorp.libs.celest.frames;

import java.util.Iterator;
import java.util.List;

import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.DijkstraShortestPath;

import be.angelcorp.libs.celest.time.IJulianDate;

public class ReferenceFrameGraph implements IReferenceFrameGraph {

	private final WeightedGraph<IReferenceFrame<?>, IReferenceFrameTransformFactory<?, ?>>	graph;

	public ReferenceFrameGraph(WeightedGraph<IReferenceFrame<?>, IReferenceFrameTransformFactory<?, ?>> graph) {
		this.graph = graph;
	}

	protected <T extends IReferenceFrame<T>, V extends IReferenceFrame<V>> List<IReferenceFrameTransformFactory<?, ?>>
			findPath(T from, V to) {
		DijkstraShortestPath<IReferenceFrame<?>, IReferenceFrameTransformFactory<?, ?>> alg =
				new DijkstraShortestPath<>(graph, from, to);
		return alg.getPathEdgeList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WeightedGraph<IReferenceFrame<?>, IReferenceFrameTransformFactory<?, ?>> getGraph() {
		return graph;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends IReferenceFrame<T>, V extends IReferenceFrame<V>> IReferenceFrameTransform<T, V>
			getTransform(T from, V to, IJulianDate epoch) throws ReferenceFrameTransformationException {

		List<IReferenceFrameTransformFactory<?, ?>> path = findPath(from, to);

		if (path == null)
			throw new ReferenceFrameTransformationException(
					"No ReferenceFrame transformation path exists between frame '%s' and '%s'", from, to);

		Iterator<IReferenceFrameTransformFactory<?, ?>> it = path.iterator();
		IReferenceFrameTransformFactory<?, ?> currentFactory = it.next();
		IReferenceFrameTransform<?, ?> transformation = currentFactory.getTransform(epoch);

		while (it.hasNext()) {
			currentFactory = it.next();
			IReferenceFrameTransform currentTranform = currentFactory.getTransform(epoch);
			transformation = transformation.add(currentTranform);
		}

		return (IReferenceFrameTransform<T, V>) transformation;
	}

}
