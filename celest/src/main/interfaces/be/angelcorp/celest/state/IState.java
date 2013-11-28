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
package be.angelcorp.celest.state;

import org.apache.commons.math3.linear.RealVector;

/**
 * An {@link IState} is the representation of a set of variables that are dependent on time and each
 * other. Linked to a set of {@link IState} is usually an {@link IStateEquation} which contains the first
 * and possibly second order differential equations which relate the different variables together to form
 * eg. the equations of motion.
 *
 * @author Simon Billemont
 */
public interface IState extends Cloneable {

    /**
     * Get the vector representation of all the variables in the state.
     *
     * @return A statevector which contains all the state variables.
     */
    public abstract RealVector toVector();

}
