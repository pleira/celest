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
package be.angelcorp.libs.celest.kepler;

import junit.framework.TestCase;

import org.apache.commons.math.linear.RealVector;

import be.angelcorp.libs.celest.stateVector.IKeplerElements;
import be.angelcorp.libs.celest.stateVector.KeplerElements;

public class TestKeplerElements extends TestCase {

	public void testEquationType() {
		IKeplerElements kCirc = new KeplerElements(1E3, 0, 1, 0.9, 0.8, 0.7);
		assertTrue(KeplerCircular.class.isInstance(kCirc.getOrbitEqn()));

		IKeplerElements kEll = new KeplerElements(1E3, 0.5, 1, 0.9, 0.8, 0.7);
		assertTrue(KeplerEllipse.class.isInstance(kEll.getOrbitEqn()));

		IKeplerElements kPara = new KeplerElements(1E3, 1, 1, 0.9, 0.8, 0.7);
		assertTrue(KeplerParabola.class.isInstance(kPara.getOrbitEqn()));

		IKeplerElements kHyper = new KeplerElements(1E3, 2, 1, 0.9, 0.8, 0.7);
		assertTrue(KeplerHyperbola.class.isInstance(kHyper.getOrbitEqn()));
	}

	public void testVectorizing() {
		IKeplerElements k = new KeplerElements(1E3, 0.2, 1, 0.9, 0.8, 0.7);

		RealVector vector = k.toVector();
		assertEquals(vector.getDimension(), 6);

		IKeplerElements k2 = KeplerElements.fromVector(vector);
		assertTrue(k.equals(k2));
	}

}
