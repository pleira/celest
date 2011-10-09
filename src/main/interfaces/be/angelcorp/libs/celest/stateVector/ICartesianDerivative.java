/**
 * Copyright (C) 2011 simon <aodtorusan@gmail.com>
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
package be.angelcorp.libs.celest.stateVector;

import be.angelcorp.libs.celest.math.Cartesian;
import be.angelcorp.libs.math.linear.Vector3D;

/**
 * Stores the state derivatives of an object state (usually {@link CartesianElements}). This is done
 * using Cartesian velocity V (&#7819;, &#7823;, &#380;) and its acceleration A (x&#776;, y&#776;,
 * z&#776;).
 * 
 * <pre>
 * Elements: {  &#7819;,     &#7823;,     &#380;,     x&#776;,      y&#776;,      z&#776;  }
 * Units:    {[m/s], [m/s], [m/s], [m/s&#178;], [m/s&#178;], [m/s&#178;]}
 * </pre>
 * 
 * @author Simon Billemont
 * 
 */
public interface ICartesianDerivative extends Cartesian, IStateDerivativeVector {

	@Override
	public abstract ICartesianDerivative clone();

	public abstract Vector3D getA();

	public abstract Vector3D getV();

	public abstract void setA(Vector3D a);

	public abstract void setV(Vector3D v);

}