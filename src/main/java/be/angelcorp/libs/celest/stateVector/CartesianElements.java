/**
 * Copyright (C) 2010 Simon Billemont <aodtorusan@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.libs.celest.stateVector;

import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.MatrixIndexException;
import org.apache.commons.math.linear.RealVector;

import be.angelcorp.libs.celest.math.Cartesian;
import be.angelcorp.libs.math.linear.Vector3D;

public class CartesianElements extends StateVector implements Cartesian {

	public static CartesianElements fromVector(RealVector v) {
		if (v.getDimension() != 6)
			throw new MatrixIndexException("Vector must have 6 indices: [Rx, Ry, Rz, Vx, Vy, Vz]");
		double[] data = v.getData();
		Vector3D R = new Vector3D(data[0], data[1], data[2]);
		Vector3D V = new Vector3D(data[3], data[4], data[5]);
		return new CartesianElements(R, V);
	}

	public Vector3D	R;
	public Vector3D	V;

	public CartesianElements() {
		this(Vector3D.ZERO, Vector3D.ZERO);
	}

	public CartesianElements(Vector3D R, Vector3D V) {
		this.R = R;
		this.V = V;
	}

	@Override
	public StateVector clone() {
		return new CartesianElements(R.clone(), V.clone());
	}

	public Vector3D getR() {
		return R;
	}

	public Vector3D getV() {
		return V;
	}

	public void setR(Vector3D r) {
		R = r;
	}

	public void setV(Vector3D v) {
		V = v;
	}

	@Override
	public CartesianElements toCartesianElements() {
		return this;
	}

	@Override
	public RealVector toVector() {
		return new ArrayRealVector(new double[] {
				R.getX(), R.getY(), R.getZ(),
				V.getX(), V.getY(), V.getZ() });
	}

}
