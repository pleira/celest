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


/**
 * A {@link IReferenceFrame} is the logic capable of producing a specific reference frame at specified
 * epochs.
 * 
 * <p>
 * All {@link IReferenceFrame}'s are linked together using {@link IReferenceFrameTransform} and form a
 * graph. This graph is used to find {@link IReferenceFrameTransform} between two arbitrary
 * {@link IReferenceFrame}'s.
 * </p>
 * 
 * @author Simon Billemont
 * 
 * @param <F>
 *            Implementing frame class.
 */
public interface IReferenceFrame<F extends IReferenceFrame<F>> {

}
