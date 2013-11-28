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

import be.angelcorp.celest.time.Epoch;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ode.SecondOrderDifferentialEquations;

/**
 * A set of first differential equations that relate to a specific type of {@link IState} variables, in
 * the form of:
 * <p/>
 * <pre>
 * dy/dt = f(t, y)
 * </pre>
 * <p/>
 * If the system can be described by a set of second order differential equations, than these state
 * equations should also implement the {@link SecondOrderDifferentialEquations} interface.
 *
 * @author Simon Billemont
 */
public interface IStateEquation<Y, DY> {

    /**
     * Compute the value of the state derivatives for a known {@link IState}:
     * <p/>
     * <pre>
     * dy/dt = f(t, y)
     * </pre>
     *
     * @param t Time of the {@link IState} evaluation,
     * @param y Dependent state variables
     * @return The derivatives of the dependent state variables
     */
    DY calculateDerivatives(Epoch t, Y y);

    /**
     * TODO: is this required?
     */
    Y createState(RealVector y);

    /**
     * Get the number of state equations. This must be the same number as parameters that are in the
     * vector representation of {@link IState} {@link Y} should have.
     *
     * @return Number of embedded state equations.
     */
    int getDimension();

}
