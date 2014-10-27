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

import be.angelcorp.celest.math.geometry.Vec3;
import be.angelcorp.celest.math.geometry.Vec3$;
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

    public Vec3 V;
    public Vec3 A;

    public CartesianDerivative() {
        this(Vec3$.MODULE$.zero(), Vec3$.MODULE$.zero());
    }

    public CartesianDerivative(double vx, double vy, double vz, double ax, double ay, double az) {
        this(new Vec3(vx, vy, vz), new Vec3(ax, ay, az));
    }

    public CartesianDerivative(double[] d) {
        if (d.length != 6)
            throw new ArrayIndexOutOfBoundsException(String.format(
                    "Array must be length 6 [vx, vy, vz, ax, ay, az], only %d elements given", d.length));
        this.V = new Vec3(d[0], d[1], d[2]);
        this.A = new Vec3(d[3], d[4], d[5]);
    }

    public CartesianDerivative(Vec3 V, Vec3 A) {
        this.V = V;
        this.A = A;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ICartesianDerivative clone() {
        return new CartesianDerivative(Vec3$.MODULE$.apply(V), Vec3$.MODULE$.apply(A));
    }

    @Override
    public boolean equals(ICartesianDerivative other) {
        return V.equals(other.getV()) && A.equals(other.getA());
    }

    @Override
    public boolean equals(ICartesianDerivative other, double eps) {
        return V.$minus(other.getV()).norm() < eps && A.$minus(other.getA()).norm() < eps;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vec3 getA() {
        return A;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vec3 getV() {
        return V;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setA(Vec3 a) {
        A = a;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setV(Vec3 v) {
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
