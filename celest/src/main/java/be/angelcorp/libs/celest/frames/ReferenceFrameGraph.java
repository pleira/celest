/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *        http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.libs.celest.frames;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.angelcorp.libs.celest.time.Epoch;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

/**
 * Implementation of the {@link IReferenceFrameGraph}. This is the graph that contains all the reference
 * frames, and the possible transforms between them.
 * 
 * @author Simon Billemont
 */
public class ReferenceFrameGraph implements IReferenceFrameGraph {

	/**
	 * Predicate to locate the exact instance of a {@link IReferenceFrame} in the
	 * {@link IReferenceFrameGraph}.
	 * 
	 * @param frame
	 *            Instance of an {@link IReferenceFrame} to locate.
	 * @return Predicate to search for the instance.
	 */
	public static Predicate<IReferenceFrame> exactFrame(final IReferenceFrame frame) {
		return new Predicate<IReferenceFrame>() {
			@Override
			public boolean apply(IReferenceFrame input) {
				return input == frame;
			}
		};
	}

	/** This is the JGraphT that actually describes the {@link IReferenceFrame}'s and there connections */
	private final WeightedGraph<IReferenceFrame, IReferenceFrameTransformFactory<?, ?>>	graph;

	private static final Logger															logger	= LoggerFactory
																										.getLogger(ReferenceFrameGraph.class);

	/**
	 * Create a new empty {@link IReferenceFrameGraph} using a specified JGraphT instance as graph
	 * back-end.
	 * 
	 * @param graph
	 *            Graph to use as back-end.
	 */
	public ReferenceFrameGraph(WeightedGraph<IReferenceFrame, IReferenceFrameTransformFactory<?, ?>> graph) {
		this.graph = graph;
	}

	/***
	 * {@inheritDoc}
	 */
	@Override
	public void attachFrame(IReferenceFrame frame) {
		graph.addVertex(frame);
	}

	/***
	 * {@inheritDoc}
	 */
	@Override
	public <F1 extends IReferenceFrame, F2 extends IReferenceFrame> void attachTransform(F1 frame1, F2 frame2,
			IReferenceFrameTransformFactory<F1, F2> transform) {
		if (!graph.containsVertex(frame1)) {
			logger.debug("Tried to add transform between frame {} and {}, but frame {} does not exist in the graph",
					new Object[] { frame1, frame2, frame1 });
			return;
		}
		if (!graph.containsVertex(frame2)) {
			logger.debug("Tried to add transform between frame {} and {}, but frame {} does not exist in the graph",
					new Object[] { frame1, frame2, frame2 });
			return;
		}
		graph.addEdge(frame1, frame2, transform);
	}

	/**
	 * Find the path between two {@link IReferenceFrame} instance that should be in the graph.
	 * 
	 * @param from
	 *            Origin of path.
	 * @param to
	 *            Destination of the path.
	 * @return A path describing all the nodes to visit (in sequence) that lead from the origin to the
	 *         destination.
	 */
	protected List<IReferenceFrameTransformFactory<?, ?>> findPath(IReferenceFrame from, IReferenceFrame to) {
		DijkstraShortestPath<IReferenceFrame, IReferenceFrameTransformFactory<?, ?>> alg =
				new DijkstraShortestPath<>(graph, from, to);
		return alg.getPathEdgeList();
	}

	/**
	 * Find the path between two {@link IReferenceFrame} instance that should be in the graph.
	 * <p>
	 * Note: the first
	 * </p>
	 * 
	 * @param from
	 *            Predicate to find the origin of path.
	 * @param to
	 *            Predicate to find the destination of the path.
	 * @return A path describing all the nodes to visit (in sequence) that lead from the origin to the
	 *         destination.
	 * @throws ReferenceFrameTransformationException
	 *             When no match was found for either the origin or destination frame.
	 */
	protected List<IReferenceFrameTransformFactory<?, ?>> findPath(Predicate<IReferenceFrame> from,
			Predicate<IReferenceFrame> to) throws ReferenceFrameTransformationException {
		Set<IReferenceFrame> all_frames = graph.vertexSet();

		IReferenceFrame to_instance = null, from_instance = null;
		boolean from_found = false, to_found = false;
		for (IReferenceFrame iReferenceFrame : all_frames) {
			if (!from_found && from.apply(iReferenceFrame)) {
				from_instance = iReferenceFrame;
				from_found = true;
			}
			if (!to_found && to.apply(iReferenceFrame)) {
				to_instance = iReferenceFrame;
				to_found = true;
			}
			if (from_found && to_found)
				break;
		}

		if (from == null || to == null)
			throw new ReferenceFrameTransformationException(
					"Could not find any frame matching the from or to frame predicate. " +
							"Did you forget to attach the frame to the framegraph?");

		return findPath(from_instance, to_instance);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IReferenceFrame findReferenceFrame(Predicate<IReferenceFrame> frame_predicate) {
		IReferenceFrame frame = null;
		for (IReferenceFrame iReferenceFrame : graph.vertexSet()) {
			if (frame_predicate.apply(iReferenceFrame)) {
				frame = iReferenceFrame;
				break;
			}
		}
		return frame;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<IReferenceFrameTransformFactory<?, ?>> findReferenceFrameTransforms(final IReferenceFrame frame) {
		return new Iterator<IReferenceFrameTransformFactory<?, ?>>() {

			/** Flag indicating that no more elements can be retrieved */
			private boolean											hasNext;
			/** The next element returned by {@link Iterator#next()} */
			private IReferenceFrameTransformFactory<?, ?>			next;
			/** Iterator to iterator over the remaining transforms possibly connected to the given frame */
			final Iterator<IReferenceFrameTransformFactory<?, ?>>	transforms	= graph.edgeSet().iterator();

			{
				// Upon construction, retrieve the next element
				retrieveNext();
			}

			@Override
			public boolean hasNext() {
				return hasNext;
			}

			@Override
			public IReferenceFrameTransformFactory<?, ?> next() {
				IReferenceFrameTransformFactory<?, ?> tmp = next;
				retrieveNext();
				return tmp;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

			private void retrieveNext() {
				do {
					if (transforms.hasNext()) {
						IReferenceFrameTransformFactory<?, ?> transform = transforms.next();
						if (graph.getEdgeSource(transform) == frame || graph.getEdgeTarget(transform) == frame) {
							next = transform;
							break;
						}
					} else {
						hasNext = false;
						break;
					}
				} while (true);
			}
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<IReferenceFrameTransformFactory<?, ?>> findReferenceFrameTransforms(
			Predicate<IReferenceFrame> frame_predicate) {
		// Locate the frame matching the predicate
		IReferenceFrame frame = findReferenceFrame(frame_predicate);

		// If no frame was found, return an empty iterator
		if (frame == null)
			return Iterators.emptyIterator();

		// Else return all the connected IReferenceFrameTransformFactories
		return findReferenceFrameTransforms(frame);
	}

	/**
	 * Get the reference to the JGraphT back-end of this {@link IReferenceFrameGraph}.
	 * 
	 * @return JGraphT instance of this reference frame.
	 */
	public WeightedGraph<IReferenceFrame, IReferenceFrameTransformFactory<?, ?>> getJGraphT() {
		return graph;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<IReferenceFrame> getReferenceFrames() {
		return graph.vertexSet().iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IReferenceFrameTransform<?, ?> getTransform(Predicate<IReferenceFrame> from, Predicate<IReferenceFrame> to,
			Epoch epoch) throws ReferenceFrameTransformationException {
		// Get the respective factory for the given input
		IReferenceFrameTransformFactory<?, ?> factory = getTransformFactory(from, to);
		// Create a new transform for the specific epoch
		IReferenceFrameTransform<?, ?> transform = factory.getTransform(epoch);

		return transform;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends IReferenceFrame, V extends IReferenceFrame> IReferenceFrameTransform<T, V>
			getTransform(T from, V to, Epoch epoch) throws ReferenceFrameTransformationException {
		// Get the respective factory for the given input
		IReferenceFrameTransformFactory<T, V> factory = getTransformFactory(from, to);
		// Create a new transform for the specific epoch
		IReferenceFrameTransform<T, V> transform = factory.getTransform(epoch);

		return transform;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IReferenceFrameTransformFactory<?, ?> getTransformFactory(Predicate<IReferenceFrame> from,
			Predicate<IReferenceFrame> to) throws ReferenceFrameTransformationException {
		// Find the path between the two first matching frames
		List<IReferenceFrameTransformFactory<?, ?>> path = findPath(from, to);

		// Make sure a path exits
		if (path == null)
			throw new ReferenceFrameTransformationException(
					"No ReferenceFrame transformation path exists between frame '%s' and '%s'", from, to);

		// Create the respective factory from the path
		IReferenceFrameTransformFactory<?, ?> factory = pathToTransformFactory(path);

		return factory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends IReferenceFrame, V extends IReferenceFrame> IReferenceFrameTransformFactory<T, V> getTransformFactory(
			T from, V to) throws ReferenceFrameTransformationException {
		// Find the path between the two first matching frames
		List<IReferenceFrameTransformFactory<?, ?>> path = findPath(from, to);

		// Make sure a path exits
		if (path == null)
			throw new ReferenceFrameTransformationException(
					"No ReferenceFrame transformation path exists between frame '%s' and '%s'", from, to);

		// Create the respective factory from the path
		IReferenceFrameTransformFactory<?, ?> factory = pathToTransformFactory(path);

		return (IReferenceFrameTransformFactory<T, V>) factory;
	}

	/**
	 * Create an {@link IReferenceFrameTransformFactory} from a known path over various compatible
	 * {@link IReferenceFrame} nodes in the frame graph.
	 * 
	 * @param path
	 *            The path leading from one specific frame to another frame.
	 * @return An {@link IReferenceFrameTransformFactory} to convert a state in the origin frame to the
	 *         destination frame.
	 */
	protected IReferenceFrameTransformFactory<?, ?> pathToTransformFactory(
			List<IReferenceFrameTransformFactory<?, ?>> path) {

		Iterator<IReferenceFrameTransformFactory<?, ?>> it = path.iterator();
		IReferenceFrameTransformFactory<?, ?> currentFactory = it.next();

		while (it.hasNext()) {
			IReferenceFrameTransformFactory newFactory = it.next();
			currentFactory = currentFactory.add(newFactory);
		}

		return currentFactory;
	}

}
