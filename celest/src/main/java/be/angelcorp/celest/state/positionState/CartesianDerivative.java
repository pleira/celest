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

import be.angelcorp.libs.math.linear.ImmutableVector3D;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.math.linear.Vector3D$;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * Documentation: {@link ICartesianDerivative}
 *
 * @author Simon Billemont
 * @see ICartesianDerivative
 */
public class CartesianDerivative extends StateDerivativeVector implements ICartesianDerivative {

    public static CartesianDerivative fromVector(RealVector v) {
        return new CartesianDerivative(v.toArray());
    }

    public Vector3D V;
    public Vector3D A;

    public CartesianDerivative() {
        this(Vector3D$.MODULE$.ZERO(), Vector3D$.MODULE$.ZERO());
    }

    public CartesianDerivative(double vx, double vy, double vz, double ax, double ay, double az) {
        this(new ImmutableVector3D(vx, vy, vz), new ImmutableVector3D(ax, ay, az));
    }

    public CartesianDerivative(double[] d) {
        if (d.length != 6)
            throw new ArrayIndexOutOfBoundsException(String.format(
                    "Array must be length 6 [vx, vy, vz, ax, ay, az], only %d elements given", d.length));
        this.V = new ImmutableVector3D(d[0], d[1], d[2]);
        this.A = new ImmutableVector3D(d[3], d[4], d[5]);
    }

    public CartesianDerivative(Vector3D V, Vector3D A) {
        this.V = V;
        this.A = A;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ICartesianDerivative clone() {
        return new CartesianDerivative(V.clone(), A.clone());
    }

    @Override
    public boolean equals(ICartesianDerivative other) {
        return V.equals(other.getV()) && A.equals(other.getA());
    }

    @Override
    public boolean equals(ICartesianDerivative other, double eps) {
        return V.equals(other.getV(), eps) && A.equals(other.getA(), eps);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector3D getA() {
        return A;
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
    public void setA(Vector3D a) {
        A = a;
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
    public CartesianDerivative toCartesianDerivative() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RealVector toVector() {
        return new ArrayRealVector(new double[]{
                V.x(), V.y(), V.z(),
                A.x(), A.y(), A.z()});
    }

}
