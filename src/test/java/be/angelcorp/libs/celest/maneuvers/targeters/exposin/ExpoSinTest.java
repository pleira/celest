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
package be.angelcorp.libs.celest.maneuvers.targeters.exposin;

import static org.apache.commons.math3.util.FastMath.PI;
import be.angelcorp.libs.celest.state.positionState.CartesianElements;
import be.angelcorp.libs.celest.state.positionState.ICartesianElements;
import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.celest.time.JulianDate;
import be.angelcorp.libs.celest.unit.CelestTest;
import be.angelcorp.libs.math.functions.ExponentialSinusoid;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.util.physics.Time;

/**
 * Validation of the Exposin angular rate equations
 * <p>
 * Validation is done against matlab routines by Rody P.S. Oldenhuis:<br/>
 * <a href="http://www.mathworks.com/matlabcentral/fileexchange/29272">Skipping Stone - An interplanetary
 * space mission design tool</a><br />
 * <b>Rody P. Oldenhuis, "Trajectory optimization of a mission to the solar bow shock and minor planets",
 * Master�s Thesis, Delft University of Technology, 2010.</b>
 * </p>
 * 
 * @author Simon Billemont
 * 
 */
public class ExpoSinTest extends CelestTest {

	public void test1() throws Exception {
		double r1 = 151366683.169E3;
		double r2 = 206953872.627E3;
		double dTheta = 1.9532 + 0 * (2 * PI);
		double k2 = 0.7013;
		double dt = Time.convert(130, Time.day);

		IJulianDate t1 = JulianDate.getJ2000();
		IJulianDate t2 = JulianDate.getJ2000().add(dt, Time.second);

		ICartesianElements s1 = new CartesianElements(
				new Vector3D(r1, 0, 0), Vector3D.ZERO);
		ICartesianElements s2 = new CartesianElements(
				new Vector3D(dTheta, 0).multiply(r2), Vector3D.ZERO);

		ExpoSin exposin = new ExpoSin(s1, s2, t1, t2);
		exposin.assumeK2(k2);

		// Results as computed by the Matlab routine:
		Vector3D ml_V1 = new Vector3D(-2.951216831366131e+002, +3.310652568212942e+004, 0);
		Vector3D ml_V2 = new Vector3D(-2.572543389259841e+004, -2.886383731363452e+003, 0);
		double ml_k0 = 2.272595936284008e+011;
		double ml_k1 = -4.065864323385938e-001;
		double ml_k2 = 7.013000000000000e-001;
		double ml_phi = 1.539528221815391e+000;
		double ml_tf = 1.123200000000000e+007;
		double ml_N = 0;
		double ml_dth = 1.953200000000001e+000;
		double ml_gamma1 = -8.914069361335221e-003;
		double ml_gamma_m = -6.505324139843980e-001;
		double ml_gamma_M = 7.999869059004543e-001;

		ExpoSinTrajectory trajectory = exposin.getTrajectory();
		ExponentialSinusoid shape = trajectory.getExposin();
		assertEquals(ml_k0, shape.getK0(), ml_k0 * 1E-8);
		assertEquals(ml_k1, shape.getK1(), 1E-8);
		assertEquals(ml_k2, shape.getK2(), 1E-8);
		CelestTest.assertEqualsAngle(ml_phi, shape.getPhi(), 1E-8);
		assertEquals(0, shape.getQ0(), 1E-16);
		CelestTest.assertEqualsAngle(ml_gamma1, trajectory.getGamma(), 1E-8);

		ICartesianElements c1 = trajectory.evaluate(t1);
		ICartesianElements c2 = trajectory.evaluate(t2);
		assertEquals(r1, c1.getR().getNorm(), 1e-16);
		assertEquals(r2, c2.getR().getNorm(), 1);
		CelestTest.assertEquals(ml_V2, c2.getV(), 1E-1);
		CelestTest.assertEquals(ml_V1, c1.getV(), 1E-1);

	}
}
