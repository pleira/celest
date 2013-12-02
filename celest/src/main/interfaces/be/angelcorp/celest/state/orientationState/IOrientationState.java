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
package be.angelcorp.celest.state.orientationState;

import be.angelcorp.celest.body.Satellite;
import be.angelcorp.celest.state.IState;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.math.rotation.IRotation;
import org.apache.commons.math3.linear.RealVector;

/**
 * This is the interface for the orientation or attitude of a specific body's (e.g. the current
 * oritentation and oritational rates).
 * <p/>
 * <p>
 * {@link IOrientationState}'s extending this class should also implement the following for convenience:
 * </p>
 * <p/>
 * <pre>
 * public static {@link IOrientationState} fromVector(RealVector vector);
 * </pre>
 * <p/>
 * <p>
 * Furthermore it is also handy to implement the following routine:
 * </p>
 * <p/>
 * <pre>
 * public static [StateVectorClass] as({@link IOrientationState} state, {@link Satellite} center);
 * </pre>
 */
public interface IOrientationState extends IState {

    /**
     * Tests if two {@link IOrientationState} are equal.
     *
     * @param obj Compare the current {@link IOrientationState} with this ones.
     * @return true if all elements match exactly.
     */
    public abstract boolean equals(IOrientationState obj);

    /**
     * Tests if two {@link IOrientationState}'s are equal.
     * <p/>
     * <p>
     * It tests using a a relative error eps and applies the following test to each element:
     * </p>
     * <p/>
     * <pre>
     * abs(vx1 - vx2) &lt; eps
     * </pre>
     *
     * @param obj Compare the current {@link IOrientationState} with this ones.
     * @param eps Relative error to check against.
     * @return true if all elements are within the given tolerance range.
     */
    public abstract boolean equals(IOrientationState obj, double eps);

    /**
     * Get the current orientation of the body.
     *
     * @return The current {@link IRotation}.
     */
    public abstract IRotation getRotation();

    /**
     * Get the orientation rate of the body. The rate is stored in a 3d vector, where each element is the
     * rotation rate along the specified axes:
     * <p/>
     * <pre>
     * {&alpha;, &beta;, &gamma;}
     * </pre>
     * <p/>
     * <p>
     * Where &alpha; is the rotation rate around the first axis (x), &beta; the second (y), and &gamma;
     * the third (z). The units are [rad/s].
     * </p>
     */
    public abstract Vector3D getRotationRate();

    /**
     * Convert the current {@link IOrientationState} to an equivalent vector form.
     *
     * @return Vector equivalent of the {@link IOrientationState}.
     */
    @Override
    public abstract RealVector toVector();

}