/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.celest.frameGraph

import scala.collection.JavaConverters._
import org.jgrapht.WeightedGraph
import org.slf4j.LoggerFactory
import org.jgrapht.alg.DijkstraShortestPath
import be.angelcorp.celest.time.Epoch
import org.jgrapht.graph.DefaultDirectedWeightedGraph


/**
 * Implementation of the ReferenceFrameGraph. This is the graph that contains all the reference
 * frameGraph, and the possible transforms between them.
 *
 * @param graph This is the JGraphT that actually describes the ReferenceFrames and there connections
 *
 * @author Simon Billemont
 */
class ReferenceFrameGraphImpl(graph: WeightedGraph[ReferenceSystem, ReferenceFrameTransformFactory[_, _]]) extends ReferenceFrameGraph {

  val logger = LoggerFactory.getLogger(getClass)

  def attachFrame(frame: ReferenceSystem) {
    graph.addVertex(frame)
  }


  def attachTransform[F1 <: ReferenceSystem, F2 <: ReferenceSystem](frame1: F1, frame2: F2, transform: ReferenceFrameTransformFactory[F1, F2]) {
    if (!graph.containsVertex(frame1))
      logger.debug("Tried to add transform between frame {} and {}, but frame {} does not exist in the graph", Array[Object](frame1, frame2, frame1))
    else if (!graph.containsVertex(frame2))
      logger.debug("Tried to add transform between frame {} and {}, but frame {} does not exist in the graph", Array[Object](frame1, frame2, frame2))
    else
      graph.addEdge(frame1, frame2, transform)
  }

  /**
   * Find the path between two ReferenceFrame instance that should be in the graph.
   *
   * @param from  Origin of path.
   * @param to    Destination of the path.
   * @return A path describing all the nodes to visit (in sequence) that lead from the origin to the destination.
   */
  private def findPath(from: ReferenceSystem, to: ReferenceSystem): Option[Seq[ReferenceFrameTransformFactory[_, _]]] = {
    val alg = new DijkstraShortestPath(graph, from, to)
    val path = alg.getPathEdgeList
    if (path == null) None else Some(path.asScala)

  }

  /**
   * Find the path between two ReferenceFrame instance that should be in the graph.
   * <p>
   * Note: the first preficate match is used
   * </p>
   *
   * @param from  Predicate to find the origin of path.
   * @param to    Predicate to find the destination of the path.
   * @return A path describing all the nodes to visit (in sequence) that lead from the origin to the
   *         destination.
   */
  def findPath(from: ReferenceSystem => Boolean, to: ReferenceSystem => Boolean): Option[Seq[ReferenceFrameTransformFactory[_, _]]] = {
    val all_frames = graph.vertexSet()

    var to_instance: ReferenceSystem = null
    var from_instance: ReferenceSystem = null
    var from_found: Boolean = false
    var to_found: Boolean = false
    for (frame <- all_frames.asScala) {
      if (!from_found && from(frame)) {
        from_instance = frame
        from_found = true
      }
      if (!to_found && to(frame)) {
        to_instance = frame
        to_found = true
      }
      // TODO, break early?
      //      if (from_found && to_found)
      //        break
    }

    if (from == null || to == null)
      None
    else
      findPath(from_instance, to_instance)
  }

  def findReferenceFrame(frame_predicate: (ReferenceSystem) => Boolean): Option[ReferenceSystem] =
    graph.vertexSet().asScala.find(frame_predicate)

  def findReferenceFrameTransforms(frame: ReferenceSystem): Iterable[ReferenceFrameTransformFactory[_, _]] = {
    new Iterable[ReferenceFrameTransformFactory[_, _]] {
      def iterator = new Iterator[ReferenceFrameTransformFactory[_, _]] {

        /** Flag indicating that no more elements can be retrieved */
        var hasNext = false

        /** The next element returned by Iterator#next() */
        private var nextItem: ReferenceFrameTransformFactory[_, _] = null
        /** Iterator to iterator over the remaining transforms possibly connected to the given frame */
        val transforms = graph.edgeSet().iterator()

        // Upon construction, retrieve the next element
        retrieveNext()

        override def next() = {
          val tmp = nextItem
          retrieveNext()
          tmp
        }

        private def retrieveNext() {
          import scala.util.control.Breaks._

          hasNext = false
          breakable {
            while (transforms.hasNext) {
              val transform = transforms.next()
              if (graph.getEdgeSource(transform) == frame || graph.getEdgeTarget(transform) == frame) {
                nextItem = transform
                hasNext = true
                break()
              }
            }
          }
        }
      }

    }

  }

  def findReferenceFrameTransforms(frame_predicate: (ReferenceSystem) => Boolean): Iterable[ReferenceFrameTransformFactory[_, _]] =
  // Locate the frame matching the predicate
    findReferenceFrame(frame_predicate) match {
      case Some(frame) =>
        // Else return all the connected IReferenceFrameTransformFactories
        findReferenceFrameTransforms(frame)
      case _ =>
        // If no frame was found, return an empty iterator
        Iterable()
    }

  def getReferenceFrames: Iterable[ReferenceSystem] =
    graph.vertexSet().asScala

  def getTransform(from: (ReferenceSystem) => Boolean, to: (ReferenceSystem) => Boolean, epoch: Epoch): Option[ReferenceFrameTransform[_, _]] = {
    // Get the respective factory for the given input
    val factory = getTransformFactory(from, to)
    // Create a new transform for the specific epoch
    factory.map(_.transform(epoch))
  }

  def getTransform[F <: ReferenceSystem, T <: ReferenceSystem](from: F, to: T, epoch: Epoch): Option[ReferenceFrameTransform[F, T]] = {
    // Get the respective factory for the given input
    val factory = getTransformFactory(from, to)
    // Create a new transform for the specific epoch
    factory.map(_.transform(epoch))
  }

  def getTransformFactory(from: (ReferenceSystem) => Boolean, to: (ReferenceSystem) => Boolean): Option[ReferenceFrameTransformFactory[_, _]] = {
    // Find the path between the two first matching frameGraph
    val path = findPath(from, to)

    // Create the respective factory from the path
    path.flatMap(p => pathToTransformFactory(p).asInstanceOf[Option[ReferenceFrameTransformFactory[_, _]]])
  }

  def getTransformFactory[F <: ReferenceSystem, T <: ReferenceSystem](from: F, to: T): Option[ReferenceFrameTransformFactory[F, T]] = {
    // Find the path between the two first matching frameGraph
    val path = findPath(from, to)

    // Create the respective factory from the path
    path.flatMap(p => pathToTransformFactory(p).asInstanceOf[Some[ReferenceFrameTransformFactory[F, T]]])
  }

  /**
   * Create an ReferenceFrameTransformFactory from a known path over various compatible ReferenceFrame nodes
   * in the frame graph.
   *
   * @param path The path leading from one specific frame to another frame.
   * @return An ReferenceFrameTransformFactory to convert a state in the origin frame to the destination frame.
   */
  private def pathToTransformFactory(path: Seq[ReferenceFrameTransformFactory[_, _]]) = {
    if (path != null) {
      // TODO: Get rid of the type system forcing of asInstanceOf
      val factory = path.reduceLeft((factory, thisFactory) => {
        type F0 = ReferenceSystem
        type F1 = ReferenceSystem
        type F2 = ReferenceSystem
        factory.asInstanceOf[ReferenceFrameTransformFactory[F0, F1]].add(thisFactory.asInstanceOf[ReferenceFrameTransformFactory[F1, F2]])
      })
      Some(factory.asInstanceOf[ReferenceFrameTransformFactory[_ <: ReferenceSystem, _ <: ReferenceSystem]])
    } else None
  }

}

object ReferenceFrameGraphImpl {

  /**
   * Predicate to locate the exact instance of a ReferenceFrame in the ReferenceFrameGraph.
   *
   * @param frame Instance of an ReferenceFrame to locate.
   * @return Predicate to search for the instance.
   */
  def exactFrame(frame: ReferenceSystem) = (fr: ReferenceSystem) => fr.equals(frame)

  def apply() = new ReferenceFrameGraphImpl(graph)

  def graph = new DefaultDirectedWeightedGraph[ReferenceSystem, ReferenceFrameTransformFactory[_, _]](classOf[ReferenceFrameTransformFactory[_, _]])

}
