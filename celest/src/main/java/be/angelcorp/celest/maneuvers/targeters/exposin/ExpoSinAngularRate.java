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
package be.angelcorp.celest.maneuvers.targeters.exposin;

import be.angelcorp.celest.math.functions.ExponentialSinusoid;
import org.apache.commons.math3.analysis.UnivariateFunction;

import static org.apache.commons.math3.util.FastMath.*;

/**
 * Computes the angular rate of an exposin at a given position (angle &theta;):
 * <p/>
 * <pre>
 * d&theta;/dt = (&mu; ( tan<sup>2</sup>&gamma; + k1 k2<sup>2</sup> s + 1 ) ) / r^3
 * tan &gamma; = k1 k2 c
 * c = cos( k2 &theta; + &phi; )
 * s = sin( k2 &theta; + &phi; )
 * </pre>
 *
 * @author simon
 */
public class ExpoSinAngularRate implements UnivariateFunction {

    /**
     * Standard gravitational parameter of the center body.
     * <p>
     * <b>Unit: [m<sup>3</sup>/s<sup>2</sup>]</b>
     * </p>
     */
    protected final double mu;
    /**
     * Basic exponential sinusoid, for which the time of flight can be computed (in function of gamma).
     * Note this only hold valif information for the same gamma where the exposin parameters where
     * constructed with.
     * <p>
     * <b>Unit: tof(&gamma;) = [s]</b>
     * </p>
     */
    protected ExponentialSinusoid exposin;

    /**
     * Create the time of flight function for a given set of exposin parameters
     *
     * @param expsin The exponential sinusoid to find the angular rates of.
     * @param mu     Standard gravitation parameter of the center body
     */
    public ExpoSinAngularRate(ExponentialSinusoid expsin, double mu) {
        this.mu = mu;
        this.exposin = expsin;
    }

    /**
     * Calculate the the quantity of dt/d&theta;
     */
    @Override
    public double value(final double theta) {
        double r = exposin.value(theta);
        double phi = exposin.getPhi();
        double k1 = exposin.getK1();
        double k2 = exposin.getK2();

        double c = cos(k2 * theta + phi);
        double s = sin(k2 * theta + phi);
        double tan_gamma = k1 * k2 * c; // Note: not gamma_1 !

        double theta_dot = sqrt((mu / pow(r, 3)) * (1 / (pow(tan_gamma, 2) + k1 * k2 * k2 * s + 1)));
        return theta_dot;
    }
}