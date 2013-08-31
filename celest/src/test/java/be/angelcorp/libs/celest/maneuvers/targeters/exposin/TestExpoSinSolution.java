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

import be.angelcorp.libs.celest.constants.SolarConstants;
import be.angelcorp.libs.celest.state.PosVel;
import be.angelcorp.libs.celest.time.Epoch;
import be.angelcorp.libs.celest.unit.CelestTest;
import be.angelcorp.libs.celest.universe.DefaultUniverse;
import be.angelcorp.libs.celest.universe.Universe;
import be.angelcorp.libs.math.functions.ExponentialSinusoid;
import be.angelcorp.libs.util.physics.Time;

public class TestExpoSinSolution extends CelestTest {

    public static Universe universe = new DefaultUniverse();

	/**
	 * <p>
	 * Source of the data:<br/>
	 * <b>Tatiana Paulino, "Analytical representations of low-thrust trajectories", Master's Thesis,
	 * Delft University of Technology, 2008.</b><br/>
	 * Table 9.1 & 9.2
	 * </p>
	 */
	public void testExposin1() throws Exception {
		// INPUT scenario 1
		double r1 = 151366683.169E3;
		double r2 = 206953872.627E3;
		double k2 = 0.7013;
		double gamma = -0.03858;
		double dTheta = 1.9532 + 0 * (2 * PI);

		// EXPECTED OUTPUT
		double R_tol = 1E1;
		// No date is given so I cannot verify the hyperbolic excess velocities (relative to the planet)
		// double V1 = 4.2057E3 + velocity of the Earth?
		// double V2 = 7.6969E3 + velocity of the Mars?
		// double V_tol = 1E-1;
		double tof = Time.convert((123.4091 + 126.2335) / 2, Time.day);
		double tof_tol = Time.convert(2, Time.day);
		// double fuel = 27.5688 or 27.1462

		ExpoSinSolutionSet solutionset = new ExpoSinSolutionSet(r1, r2, k2, dTheta, SolarConstants.mu());

		double calculated_tof = solutionset.value(gamma);
		assertEquals(tof, calculated_tof, tof_tol);

		Epoch t1 = universe.J2000_EPOCH();
		Epoch t2 = universe.J2000_EPOCH().add(calculated_tof, Time.second);
		ExponentialSinusoid solution = solutionset.getExpoSin(gamma);
		ExpoSinTrajectory trajectory = new ExpoSinTrajectory(solution, SolarConstants.body(), t1);

		PosVel c1 = trajectory.apply(t1);
        PosVel c2 = trajectory.apply(t2);
		// Equal R
		CelestTest.assertEqualsAngle(dTheta, c1.position().angle(c2.position()), 1e-3);
		assertEquals(r1, c1.position().norm(), R_tol);
		assertEquals(r2, c2.position().norm(), R_tol);
		// Equal V
		// assertEquals(V1, c1.getV().getNorm(), V_tol);
		// assertEquals(V2, c2.getV().getNorm(), V_tol);
	}

	/**
	 * <p>
	 * Source of the data:<br/>
	 * <b>Tatiana Paulino, "Analytical representations of low-thrust trajectories", Master's Thesis,
	 * Delft University of Technology, 2008.</b><br/>
	 * Table 9.1 & 9.2
	 * </p>
	 */
	public void testExposin2() throws Exception {
		// INPUT scenario 1
		double r1 = 150950940.668E3;
		double r2 = 207035807.816E3;
		double k2 = 0.3192;
		double gamma = 0.02342;
		double dTheta = 1.7915 + 1 * (2 * PI);

		// EXPECTED OUTPUT
		double R_tol = 1E1;
		// No date is given so I cannot verify the hyperbolic excess velocities (relative to the planet)
		// double V1 = 0.7339E3 + velocity of the Earth?
		// double V2 = 0.2569E3 + velocity of the Mars?
		// double V_tol = 1E-1;
		double tof = Time.convert((624.0333 + 624.3738) / 2, Time.day);
		double tof_tol = Time.convert(0.5, Time.day);
		// double fuel = 132.3082 or 124.1012

		ExpoSinSolutionSet solutionset = new ExpoSinSolutionSet(r1, r2, k2, dTheta, SolarConstants.mu());

		double calculated_tof = solutionset.value(gamma);
		assertEquals(tof, calculated_tof, tof_tol);

		Epoch t1 = universe.J2000_EPOCH();
		Epoch t2 = universe.J2000_EPOCH().add(calculated_tof, Time.second);
		ExponentialSinusoid solution = solutionset.getExpoSin(gamma);
		ExpoSinTrajectory trajectory = new ExpoSinTrajectory(solution, SolarConstants.body(), t1);

        PosVel c1 = trajectory.apply(t1);
        PosVel c2 = trajectory.apply(t2);
		// Equal R
		CelestTest.assertEqualsAngle(dTheta, c1.position().angle(c2.position()), 1e-3);
		assertEquals(r1, c1.position().norm(), R_tol);
		assertEquals(r2, c2.position().norm(), R_tol);
		// Equal V
		// assertEquals(V1, c1.getV().getNorm(), V_tol);
		// assertEquals(V2, c2.getV().getNorm(), V_tol);
	}

	/**
	 * <p>
	 * Source of the data:<br/>
	 * <b>Tatiana Paulino, "Analytical representations of low-thrust trajectories", Master's Thesis,
	 * Delft University of Technology, 2008.</b><br/>
	 * Table 9.1 & 9.2
	 * </p>
	 */
	public void testExposin3() throws Exception {
		// INPUT scenario 1
		double r1 = 147943444.631E3;
		double r2 = 222257727.478E3;
		double k2 = 0.1524;
		double gamma = 0.01048;
		double dTheta = 0.0419 + 3 * (2 * PI);

		// EXPECTED OUTPUT
		double R_tol = 1E1;
		// No date is given so I cannot verify the hyperbolic excess velocities (relative to the planet)
		// double V1 = 0.3218E3 + velocity of the Earth?
		// double V2 = 0.0818E3 + velocity of the Mars?
		// double V_tol = 1E-1;
		double tof = Time.convert((1552.3217 + 1552.6437) / 2, Time.day);
		double tof_tol = Time.convert(0.5, Time.day);
		// double fuel = 183.4770 or 167.8474

		ExpoSinSolutionSet solutionset = new ExpoSinSolutionSet(r1, r2, k2, dTheta, SolarConstants.mu());

		double calculated_tof = solutionset.value(gamma);
		assertEquals(tof, calculated_tof, tof_tol);

		Epoch t1 = universe.J2000_EPOCH();
		Epoch t2 = universe.J2000_EPOCH().add(calculated_tof, Time.second);
		ExponentialSinusoid solution = solutionset.getExpoSin(gamma);
		ExpoSinTrajectory trajectory = new ExpoSinTrajectory(solution, SolarConstants.body(), t1);

		PosVel c1 = trajectory.apply(t1);
        PosVel c2 = trajectory.apply(t2);
		// Equal R
		CelestTest.assertEqualsAngle(dTheta, c1.position().angle(c2.position()), 1e-3);
		assertEquals(r1, c1.position().norm(), R_tol);
		assertEquals(r2, c2.position().norm(), R_tol);
		// Equal V
		// assertEquals(V1, c1.getV().getNorm(), V_tol);
		// assertEquals(V2, c2.getV().getNorm(), V_tol);
	}

}
