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

/**
 * A ReferenceFrame is a basic label for a frame that can be used inside te frame graph.
 *
 * <p>
 * All ReferenceFrames are linked together using ReferenceFrameTransform and form a graph. This graph is used to
 * find ReferenceFrameTransforms between two arbitrary ReferenceFrames.
 * </p>
 *
 * @author Simon Billemont
 */
trait ReferenceSystem
