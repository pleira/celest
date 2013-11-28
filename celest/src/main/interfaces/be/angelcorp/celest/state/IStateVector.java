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

import be.angelcorp.celest.state.orientationState.IOrientationState;
import org.apache.commons.math3.linear.RealVector;

/**
 * An {@link IStateVector} is a {@link IState} which combines the position, orientation and additional
 * state parameters in a single object.
 * <p/>
 * <p>
 * The method {@link IState#toVector} should combine all the substates in a single {@link RealVector} ,
 * which represents the complete state.
 * </p>
 *
 * @author Simon Billemont
 */
public interface IStateVector extends IState {

    /**
     * Get the additional state variables stored in the {@link IStateVector}. This state can represent
     * variables such as: mass, massflow, power, density, pressure, tempreature, solar flux, ...
     *
     * @return Additional state variables.
     */
    public abstract IState getAdditionalState();

    /**
     * Get the orientiational substate.
     */
    public abstract IOrientationState getOrientation();

    /**
     * Get the position substate.
     */
    public abstract Orbit getPosition();

}
