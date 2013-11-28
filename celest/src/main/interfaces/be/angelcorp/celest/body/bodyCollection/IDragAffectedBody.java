/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
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

package be.angelcorp.celest.body.bodyCollection;

import be.angelcorp.celest.body.IShapedBody;

/**
 * A body that can experience drag forces.
 */
public interface IDragAffectedBody extends IShapedBody {

    /**
     * Drag coefficient for this body.
     * <p/>
     * <p>
     * Usually ranges from 2.2 to 2.8, (NOT THE SAME CD AS FOR AIRCRAFT as Cd contains corrections for the
     * physical models, &rho; and S).
     * </p>
     * <p/>
     * <b>Unit: [-]</b>
     *
     * @return The body drag coefficient [-]
     */
    double getDragCoefficient();

}
