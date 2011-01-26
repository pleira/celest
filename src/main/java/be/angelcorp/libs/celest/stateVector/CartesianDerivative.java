/**
 * Copyright (C) 2011 Simon Billemont <aodtorusan@gmail.com>
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 3.0 Unported
 * (CC BY-NC 3.0) (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *        http://creativecommons.org/licenses/by-nc/3.0/
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

import be.angelcorp.libs.celest.math.Cartesian;
import be.angelcorp.libs.math.linear.Vector3D;

public class CartesianDerivative extends StateDerivativeVector implements Cartesian {

	public static CartesianDerivative fromVector(RealVector v) {
		return new CartesianDerivative(v.getData());
	}

	public Vector3D	V;
	public Vector3D	A;

	public CartesianDerivative() {
		this(Vector3D.ZERO, Vector3D.ZERO);
	}

	public CartesianDerivative(double[] d) {
		if (d.length != 6)
			throw new ArrayIndexOutOfBoundsException(String.format(
					"Array must be length 6 [vx, vy, vz, ax, ay, az], only %d elements given", d.length));
		this.V = new Vector3D(d[0], d[1], d[2]);
		this.A = new Vector3D(d[3], d[4], d[5]);
	}

	public CartesianDerivative(Vector3D V, Vector3D A) {
		this.V = V;
		this.A = A;
	}

	@Override
	public StateDerivativeVector clone() {
		return new CartesianDerivative(V.clone(), A.clone());
	}

	public Vector3D getA() {
		return A;
	}

	public Vector3D getV() {
		return V;
	}

	public void setA(Vector3D a) {
		A = a;
	}

	public void setV(Vector3D v) {
		V = v;
	}

	@Override
	public CartesianDerivative toCartesianDerivative() {
		return this;
	}

	@Override
	public RealVector toVector() {
		return new ArrayRealVector(new double[] {
				V.getX(), V.getY(), V.getZ(),
				A.getX(), A.getY(), A.getZ() });
	}

}
