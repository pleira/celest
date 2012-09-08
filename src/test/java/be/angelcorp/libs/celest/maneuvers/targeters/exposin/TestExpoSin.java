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
import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.state.positionState.CartesianElements;
import be.angelcorp.libs.celest.state.positionState.ICartesianElements;
import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.celest.time.JulianDate;
import be.angelcorp.libs.celest.unit.CelestTest;
import be.angelcorp.libs.math.functions.ExponentialSinusoid;
import be.angelcorp.libs.math.linear.ImmutableVector3D;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.math.linear.Vector3D$;
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
public class TestExpoSin extends CelestTest {

	public void testN0() throws Exception {
		double r1 = 151366683.169E3;
		double r2 = 206953872.627E3;
		double dTheta = 1.9532 + 0 * (2 * PI);
		double k2 = 0.7013;
		double dt = Time.convert(130, Time.day);

		IJulianDate t1 = JulianDate.getJ2000();
		IJulianDate t2 = JulianDate.getJ2000().add(dt, Time.second);

		ICartesianElements s1 = new CartesianElements(
				new ImmutableVector3D(r1, 0, 0), Vector3D$.MODULE$.ZERO());
		ICartesianElements s2 = new CartesianElements(
				Vector3D$.MODULE$.apply(dTheta, 0).multiply(r2), Vector3D$.MODULE$.ZERO());

		ExpoSin exposin = new ExpoSin(s1, s2, t1, t2);
		exposin.assumeK2(k2);

		// Results as computed by the Matlab routine:
		Vector3D ml_V1 = new ImmutableVector3D(-2.951216831366131e+002, +3.310652568212942e+004, 0);
		Vector3D ml_V2 = new ImmutableVector3D(-2.572543389259841e+004, -2.886383731363452e+003, 0);
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

		ExpoSinSolutionSet solutionSet = exposin.getSolutionSet();
		assertEquals(ml_gamma_m, solutionSet.getDomain().lowerBound, 1E-1);
		assertEquals(ml_gamma_M, solutionSet.getDomain().upperBound, 1E-1);

		ExpoSinTrajectory trajectory = exposin.getTrajectory();
		ExponentialSinusoid shape = trajectory.getExposin();
		assertEquals(ml_k0, shape.getK0(), ml_k0 * 1E-8);
		assertEquals(ml_k1, shape.getK1(), 1E-8);
		assertEquals(ml_k2, shape.getK2(), 1E-8);
		assertEqualsAngle(ml_phi, shape.getPhi(), 1E-8);
		assertEquals(0, shape.getQ0(), 1E-16);
		assertEqualsAngle(ml_gamma1, trajectory.getGamma(), 1E-8);

		ICartesianElements c1 = trajectory.evaluate(t1);
		ICartesianElements c2 = trajectory.evaluate(t2);
		assertEquals(r1, c1.getR().norm(), 1e-16);
		assertEquals(r2, c2.getR().norm(), 1);
		assertEquals(ml_V2.x(), c2.getV().x(), 1E-1);
		assertEquals(ml_V2.y(), c2.getV().y(), 1E-1);
		assertEquals(ml_V2.z(), c2.getV().z(), 1E-1);
		assertEquals(ml_V1.x(), c1.getV().x(), 1E-1);
		assertEquals(ml_V1.y(), c1.getV().y(), 1E-1);
		assertEquals(ml_V1.z(), c1.getV().z(), 1E-1);
	}

	public void testN3() throws Exception {
		double k2 = 1. / 12.;
		double dt = Time.convert(1E-3, Time.day);
		int N = 3;
		ICartesianElements r1 = new CartesianElements(new ImmutableVector3D(2, 0, 0), Vector3D$.MODULE$.ZERO());
		ICartesianElements r2 = new CartesianElements(new ImmutableVector3D(0.2, -1, 0), Vector3D$.MODULE$.ZERO());

		IJulianDate t1 = JulianDate.getJ2000();
		IJulianDate t2 = JulianDate.getJ2000().add(dt, Time.second);
		CelestialBody center = new CelestialBody();
		center.setMu(1E4);

		ExpoSin exposin = new ExpoSin(r1, r2, t1, t2);
		exposin.setCenter(center);
		exposin.setN(N);
		exposin.assumeK2(k2);

		// Results as computed by the Matlab routine:
		Vector3D ml_V1 = new ImmutableVector3D(+4.279665287258510e+001, +5.461017978436156e+001, 0);
		Vector3D ml_V2 = new ImmutableVector3D(-8.557155856649688e+001, -4.647847748181721e+001, 0);
		double ml_k0 = 2.494219696832175e-004;
		double ml_k1 = 1.300955416481242e+001;
		double ml_k2 = 8.333333333333333e-002;
		double ml_phi = 7.628619402579907e-001;
		double ml_tf = 8.640000000000001e+001;
		double ml_N = 3;
		double ml_dth = 2.022295668848377e+001;
		double ml_gamma1 = 6.647073338793147e-001;
		double ml_gamma_m = -1.459915193676833e+000;
		double ml_gamma_M = 1.459299027659339e+000;

		ExpoSinSolutionSet solutionSet = exposin.getSolutionSet();
		assertEquals(ml_gamma_m, solutionSet.getDomain().lowerBound, 1E-1);
		assertEquals(ml_gamma_M, solutionSet.getDomain().upperBound, 1E-1);

		ExpoSinTrajectory trajectory = exposin.getTrajectory();
		ExponentialSinusoid shape = trajectory.getExposin();
		assertEquals(ml_k0, shape.getK0(), ml_k0 * 1E-2);
		assertEquals(ml_k1, shape.getK1(), 1E-2);
		assertEquals(ml_k2, shape.getK2(), 1E-8);
		assertEqualsAngle(ml_phi, shape.getPhi(), 1E-2);
		assertEquals(0, shape.getQ0(), 1E-16);
		assertEqualsAngle(ml_gamma1, trajectory.getGamma(), 1E-2);

		ICartesianElements c1 = trajectory.evaluate(t1);
		ICartesianElements c2 = trajectory.evaluate(t2);
        assertEquals(r1.getR().norm(), c1.getR().norm(), 1e-15);
        assertEquals(r2.getR().norm(), c2.getR().norm(), 1);
        assertEquals(ml_V2.x(), c2.getV().x(), 1E-1);
        assertEquals(ml_V2.y(), c2.getV().y(), 1E-1);
        assertEquals(ml_V2.z(), c2.getV().z(), 1E-1);
        assertEquals(ml_V1.x(), c1.getV().x(), 1E-1);
        assertEquals(ml_V1.y(), c1.getV().y(), 1E-1);
        assertEquals(ml_V1.z(), c1.getV().z(), 1E-1);
	}
}
