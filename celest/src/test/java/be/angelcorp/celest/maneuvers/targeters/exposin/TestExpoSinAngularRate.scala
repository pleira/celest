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
package be.angelcorp.celest.maneuvers.targeters.exposin

import be.angelcorp.celest.math.functions.ExponentialSinusoid
import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.unit.CelestTest
import org.scalatest.{FlatSpec, Matchers}

/**
 * Validation of the Exposin angular rate equations
 * <p>
 * Validation is done against matlab routines by Rody P.S. Oldenhuis:<br/>
 * <a href="http://www.mathworks.com/matlabcentral/fileexchange/29272">Skipping Stone - An interplanetary
 * space mission design tool</a><br />
 * <b>Rody P. Oldenhuis, "Trajectory optimization of a mission to the solar bow shock and minor planets",
 * Master�s Thesis, Delft University of Technology, 2010.</b>
 * </p>
 * <p/>
 * <h2>Results used for validation</h2>
 * <p/>
 * Matlab test code (using skipping stone low thrust routines):
 * <p/>
 * <pre>
 * k0 = 1.5; k1 = -0.1; k2 = 1/2; q0 = 0; phi = 2.5; dTheta = 5; mu = 1;
 * r1 = norm([1.412862648939526,0.,0.]); r2 = norm([0.468315202078896,-1.58314656828111,0.]);
 * angularRate = @(theta) 1/tff(theta, atan(k1 * k2 * cos(k2 * theta + phi)), r1, k2, k2 * dTheta, log(r1/r2) )
 * for theta=0:5, angularRate(theta), end
 * </pre>
 * <p/>
 * Matheamtaice code:
 * <p/>
 * <pre>
 * k0 = 1.5; k1 = -0.1; k2 = 1/2; q0 = 0; \[CurlyPhi] = 2.5; d\[Theta]2 = 5; \[Mu] = 1;
 * r[\[Theta]_] := k0 Exp[ q0 \[Theta] + k1 Sin[k2 \[Theta] + \[CurlyPhi]]]
 * c[\[Theta]_] := Cos[k2 \[Theta] + \[CurlyPhi]]
 * s[\[Theta]_] := Sin[k2 \[Theta] + \[CurlyPhi]]
 * tg[\[Theta]_] := k1 k2 c[\[Theta]]
 * d\[Theta][\[Theta]_] := Sqrt[\[Mu]/r[\[Theta]]^3*1/( tg[\[Theta]]^2 + k1 k2^2 s[\[Theta]] + 1)]
 * dd\[Theta][\[Theta]_] := d\[Theta]'[\[Theta]]
 * NumberForm[Map[d\[Theta], {0, 1, 2, 3, 4, 5}], 16]
 * NumberForm[Map[dd\[Theta], {0, 1, 2, 3, 4, 5}], 16]
 * </pre>
 *
 * @author Simon Billemont
 */
class TestExpoSinAngularRate extends FlatSpec with Matchers with CelestTest {

  "ExpoSinAngularRate" should "be equal to the calculated reference value" in {
    val k0 = 1.5
    val k1 = -0.1
    val k2 = 1.0 / 2
    val q0 = 0
    val phi = 2.5
    val dTheta = 5
    val mu = 1
    val r1 = Vec3(1.412862648939526, 0.0, 0.0)
    val r2 = Vec3(0.468315202078896, -1.58314656828111, 0.0)

    val angularRate = new ExpoSinAngularRate(new ExponentialSinusoid(k0, k1, k2, q0, phi), mu)

    // Matlab results;
    assertEquals(5.994736717283741e-001, angularRate.value(0.0), 1E-14)
    assertEquals(5.562761270281725e-001, angularRate.value(1.0), 1E-14)
    assertEquals(5.136227141636941e-001, angularRate.value(2.0), 1E-14)
    assertEquals(4.811320282409562e-001, angularRate.value(3.0), 1E-14)
    assertEquals(4.644257403076331e-001, angularRate.value(4.0), 1E-14)
    assertEquals(4.658089763181780e-001, angularRate.value(5.0), 1E-14)

    // Matematica results;
    // Basically the same as the matlab output, gives confidence that the equation is typed correctly
    assertEquals(0.599473671728374,  angularRate.value(0.0), 1E-14)
    assertEquals(0.5562761270281725, angularRate.value(1.0), 1E-14)
    assertEquals(0.5136227141636941, angularRate.value(2.0), 1E-14)
    assertEquals(0.4811320282409562, angularRate.value(3.0), 1E-14)
    assertEquals(0.4644257403076332, angularRate.value(4.0), 1E-14)
    assertEquals(0.4658089763181781, angularRate.value(5.0), 1E-14)
  }
}
