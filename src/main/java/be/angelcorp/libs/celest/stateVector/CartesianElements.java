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

import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealVector;

import be.angelcorp.libs.math.linear.Vector3D;

/**
 * Documentation: {@link ICartesianElements}
 * 
 * @author Simon Billemont
 * @see ICartesianElements
 */
public class CartesianElements extends StateVector implements ICartesianElements {

	/**
	 * Create the {@link CartesianElements} from another {@link StateVector}.
	 * <p>
	 * Guarantees that the return of a {@link CartesianElements} {@link StateVector}, but not necessarily
	 * a clone (can be the same {@link StateVector})
	 * </p>
	 * 
	 * @param state
	 *            {@link StateVector} to convert
	 */
	public static ICartesianElements as(ICartesianElements state) {
		return state.toCartesianElements();
	}

	/**
	 * {@inheritDoc}
	 */
	public static CartesianElements fromVector(RealVector v) {
		return new CartesianElements(v.getData());
	}

	/**
	 * Instantaneous position
	 * <p>
	 * <b>Unit: [m]</b>
	 * </p>
	 */
	public Vector3D	R;

	/**
	 * Instantaneous velocity
	 * <p>
	 * <b>Unit: [m/s<sup>2</sup>]</b>
	 * </p>
	 */
	public Vector3D	V;

	/**
	 * Create an empty set of {@link CartesianElements}. Given by:
	 * <p>
	 * R = <0,0,0> <br/>
	 * V = <0,0,0>
	 * </p>
	 */
	public CartesianElements() {
		this(Vector3D.ZERO, Vector3D.ZERO);
	}

	/**
	 * Create from a 6 dimentional array.
	 * <p>
	 * d = [Rx, Ry, Rz, Vx, Vy, Vz]
	 * </p>
	 * <p>
	 * Note: The array must have a length of 6
	 * </p>
	 * 
	 * @param d
	 *            Array to extract the position and velocity from
	 */
	public CartesianElements(double[] d) {
		if (d.length != 6)
			throw new ArrayIndexOutOfBoundsException(String.format(
					"Array must be length 6 [rx, ry, rz, vx, vy, vz], only %d elements given", d.length));
		this.R = new Vector3D(d[0], d[1], d[2]);
		this.V = new Vector3D(d[3], d[4], d[5]);
	}

	/**
	 * Create from a given position and velocity vector.
	 * <p>
	 * Note, copies the reference, so NOT THE ELEMENTS.
	 * </p>
	 * 
	 * @param R
	 *            Position vector [m]
	 * @param V
	 *            Velocity vector [m/s]
	 */
	public CartesianElements(Vector3D R, Vector3D V) {
		this.R = R;
		this.V = V;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICartesianElements clone() {
		return new CartesianElements(R.clone(), V.clone());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3D getR() {
		return R;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3D getV() {
		return V;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setR(Vector3D r) {
		R = r;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setV(Vector3D v) {
		V = v;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CartesianElements toCartesianElements() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RealVector toVector() {
		return new ArrayRealVector(new double[] {
				R.getX(), R.getY(), R.getZ(),
				V.getX(), V.getY(), V.getZ() });
	}

}
