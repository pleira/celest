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
package be.angelcorp.celest.state.positionState;

import be.angelcorp.celest.state.IStateDerivative;
import org.apache.commons.math3.linear.RealVector;

/**
 * This hold the derivatives of the {@link be.angelcorp.celest.state.Orbit} of a given body
 * <p/>
 * <p>
 * Classes extending this class should also implement the following for convenience:
 * </p>
 * <p/>
 * <pre>
 * public static {@link IPositionStateDerivative} fromVector({@link RealVector} vector);
 * </pre>
 *
 * @author Simon Billemont
 */
public interface IPositionStateDerivative extends IStateDerivative {

    /**
     * Tests if two {@link IPositionStateDerivative} are equal. By default, this id done by comparing all
     * elements of the {@link IPositionStateDerivative#toVector()} output. Each element must have an
     * identical value to be considered equal.
     *
     * @param obj Compare the current {@link IPositionStateDerivative} with this ones.
     * @return true if they are equal.
     */
    public abstract boolean equals(IPositionStateDerivative obj);

    /**
     * Tests if two {@link IPositionStateDerivative} are equal. By default, this id done by comparing all
     * elements of the {@link IPositionStateDerivative#toVector()} output.
     * <p/>
     * <p>
     * It tests using a a relative error eps and applies the following test to each element:
     * </p>
     * <p/>
     * <pre>
     * abs(vx1 - vx2) &lt; eps * vx1
     * </pre>
     *
     * @param obj Compare the current {@link IPositionStateDerivative} with this ones.
     * @param eps Relative error to check against.
     * @return true if they are equal.
     */
    public abstract boolean equals(IPositionStateDerivative obj, double eps);

    /**
     * Convert the {@link IPositionStateDerivative} to an equivalent Cartesian one (V,A in Cartesian
     * coordinates)
     *
     * @return Cartesian equivalent state derivative vector
     */
    public abstract CartesianDerivative toCartesianDerivative();

    /**
     * Convert the current state vector to an equivalent vector form
     *
     * @return Vector equivalent of the state vector
     */
    @Override
    public abstract RealVector toVector();

}