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
package be.angelcorp.libs.celest.stateVector;

import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealVector;

import be.angelcorp.libs.celest.unit.Tests;
import be.angelcorp.libs.math.linear.Vector3D;

public class TestCartesianElements extends TestStateVector<CartesianElements> {

	public TestCartesianElements() {
		super(CartesianElements.class);
	}

	@Override
	public void testAs() {
		CartesianElements c = new CartesianElements(Vector3D.random(), Vector3D.random());
		doTestAs(c, c);
	}

	public void testConstructor() {
		/* Check default constructor */
		CartesianElements c = new CartesianElements();
		Tests.assertEquals(c.toVector(), new ArrayRealVector(6, 0), 1E-16);

		/* Check array constructor */
		c = new CartesianElements(new double[] { 0, 1, 2, 3, 4, 5 });
		Tests.assertEquals(c.toVector(), new ArrayRealVector(new double[] { 0, 1, 2, 3, 4, 5 }), 1E-16);

		/* Should cash if incorrect number of arguments is given */
		try {
			c = new CartesianElements(new double[] { 0, 1, 2, 3, 4, 5, 6 });
			throw new AssertionError("CartesianElements constructor not crash on array with 7 elements");
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		try {
			c = new CartesianElements(new double[] { 0, 1, 2, 3, 4 });
			throw new AssertionError("CartesianElements constructor not crash on array with 5 elements");
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		/* Check vector constructor */
		c = new CartesianElements(new Vector3D(2, 3, 4), new Vector3D(9, 8, 7));
		Tests.assertEquals(c.toVector(), new ArrayRealVector(new double[] { 2, 3, 4, 9, 8, 7 }), 1E-16);

	}

	public void testGettersSetters() {
		// Check getters
		CartesianElements c = new CartesianElements(new Vector3D(2, 3, 4), new Vector3D(9, 8, 7));
		Tests.assertEquals(c.getR(), new Vector3D(2, 3, 4), 1e-18);
		Tests.assertEquals(c.getV(), new Vector3D(9, 8, 7), 1e-18);

		// Check setters
		c.setR(new Vector3D(1, 2, 3));
		c.setV(new Vector3D(4, 5, 6));
		Tests.assertEquals(c.getR(), new Vector3D(1, 2, 3), 1e-18);
		Tests.assertEquals(c.getV(), new Vector3D(4, 5, 6), 1e-18);

	}

	@Override
	public void testToCartesianElements() {
		CartesianElements c = new CartesianElements(Vector3D.random(), Vector3D.random());
		equalStateVector(c, c.toCartesianElements());
	}

	@Override
	public void testVectorConversion() {
		RealVector v = new ArrayRealVector(new double[] { 1, 2, 3, 4, 5, 6 });
		CartesianElements c = new CartesianElements(new Vector3D(1, 2, 3), new Vector3D(4, 5, 6));
		doTestVector(c, v, 1e-18);
	}

}
