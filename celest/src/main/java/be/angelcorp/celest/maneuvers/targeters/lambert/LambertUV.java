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
package be.angelcorp.celest.maneuvers.targeters.lambert;

import be.angelcorp.celest.body.CelestialBody;
import be.angelcorp.celest.maneuvers.targeters.TPBVP;
import be.angelcorp.celest.state.PosVel;
import be.angelcorp.celest.time.Epoch;
import be.angelcorp.libs.math.linear.Vector3D;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.Add;
import org.apache.commons.math3.analysis.function.Constant;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.util.Precision;

import static java.lang.Math.*;

/**
 * Solve the Lambert problem ( {@link TPBVP}, using the Universal Variables technique. Based on the
 * algorithms provided by Bate, Mueller, & White (Fundamental of Astrodynamics), and Vallado (Fundamental
 * of Astrodynamics and Applications)
 *
 * @author Simon Billemont
 */
public class LambertUV extends TPBVP {

    private class LambertFunctionUV implements UnivariateFunction {
        double A, r1norm, r2norm;

        private LambertFunctionUV(double A, double r1norm, double r2norm) {
            this.A = A;
            this.r1norm = r1norm;
            this.r2norm = r2norm;
        }

        public double computeY(double z) {
            double C, S;
            if (z > Precision.EPSILON) {
                C = (1d - cos(sqrt(z))) / z;
                S = (sqrt(z) - sin(sqrt(z))) / pow(z, 3d / 2d);
            } else if (z < -Precision.EPSILON) {
                C = (1d - cosh(sqrt(-z))) / z;
                S = (sinh(sqrt(-z)) - sqrt(-z)) / pow(-z, 3d / 2d);
            } else {
                C = 1d / 2d;
                S = 1d / 6d;
            }
            return computeY(z, C, S);
        }

        private double computeY(double z, double C, double S) {
            double y = r1norm + r2norm + A * (z * S - 1d) / sqrt(C);
            return y;
        }

        @Override
        public double value(double z) {
            double C, S;

            if (z > Precision.EPSILON) {
                C = (1d - cos(sqrt(z))) / z;
                S = (sqrt(z) - sin(sqrt(z))) / pow(z, 3d / 2d);
            } else if (z < -Precision.EPSILON) {
                C = (1d - cosh(sqrt(-z))) / z;
                S = (sinh(sqrt(-z)) - sqrt(-z)) / pow(-z, 3d / 2d);
            } else {
                C = 1d / 2d;
                S = 1d / 6d;
            }

            double y = computeY(z, C, S);

            double x = sqrt(y / C);
            double currentDT = (pow(x, 3) * S + A * sqrt(y)) / centerbody.getMu();
            return currentDT;
        }
    }

    /**
     * Body at the center of the Keplerian motion, r1 and r2 are measured wrt this body. This us usually
     * The Earth for near Earth orbits, and the Sun for interplanetary missions.
     */
    private CelestialBody centerbody;

    /**
     * Indicates if the direction of motion is the small angle between r1 and r2, or the large angle
     * between r1 and r2.
     */
    private boolean shortWay;

    /**
     * Create a Lambert targetter using the universal variables technique.
     *
     * @param r1         Starting point R1
     * @param r2         Arrival point R2
     * @param centerbody Central body of the Keplerian motion (useually the Sun or the Earth)
     * @param departure  Epoch of departure (epoch at r1)
     * @param arrival    Epoch of arrival (epoch at r2)
     * @param shortWay   Use short arc transfer (transfer solution over the smallest angle between R1 and R2)
     */
    public LambertUV(PosVel r1, PosVel r2, CelestialBody centerbody, Epoch departure,
                     Epoch arrival, boolean shortWay) {
        super(r1, r2, departure, arrival);
        this.centerbody = centerbody;
        this.shortWay = shortWay;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LambertTrajectory getTrajectory() {
        Vector3D r1 = this.r1.position();
        Vector3D r2 = this.r2.position();
        double r1norm = r1.norm();
        double r2norm = r2.norm();

        double cosDnu = r1.dot(r2) / (r1norm * r2norm);
        double A = sqrt(r1norm * r2norm * (1d + cosDnu));

        if (abs(A) < Precision.EPSILON)
            throw new ArithmeticException("Cannot compute Lambert solution for a problem where " +
                    "A = sqrt(r1norm * r2norm * (1 + cos(Dnu) )) == 0");

        if (!shortWay)
            A = -A;

        double dT = getdT();
        LambertFunctionUV func = new LambertFunctionUV(A, r1norm, r2norm);
        BrentSolver solver = new BrentSolver(1e-12);
        double z = solver.solve(100, FunctionUtils.combine(new Add(), new Constant(-dT), func),
                -4d * PI, 5. * PI * PI, 0);

        double y = func.computeY(z);
        double f = 1d - y / r1norm;
        double g = A * sqrt(y / centerbody.getMu());
        double g_dot = 1d - y / r2norm;

        // Compute the initial velocities at beginning and end points
        // Vector3D v1 = r2.subtract(r1.multiply(f)).divide(g);
        // Vector3D v2 = r2.multiply(g_dot).subtract(r1).divide(g);

        return new LambertTrajectory(r1, r2, this.r1.frame().get(), departureEpoch, arrivalEpoch, f, g, g_dot);
    }
}
