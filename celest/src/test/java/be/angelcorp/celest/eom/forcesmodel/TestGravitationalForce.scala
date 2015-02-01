/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.celest.eom.forcesmodel

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
;

class TestGravitationalForce extends FlatSpec with ShouldMatchers {

  /*
  implicit val universe = new DefaultUniverse

  "GravitationalForce" should "generate the correct force / acceleration" in {
    /* Test the f/a in a simple earth system (norm only) */
    val earth = frames.BodyCentered(EarthConstants.body)

    val sat = new Body(new Keplerian(10E6, 0, 0, 0, 0, 0, Some(earth)), 5)
    val g = new GravitationalForce_C(sat, earth.centerBody)

    g.getForce.norm should be(19.93d plusOrMinus 1E-1)
    g.toAcceleration.norm should be(3.986d plusOrMinus 1E-2)

    /* Test the f/a in a simple earth system all components */
    val sat1 = new Body(new Spherical(10E6, Pi / 3, 0, KeplerCircular.circularVelocity(10E6, earth.centerBody.getMu), 0, 0, Some(earth)), 5)
    val g1 = new GravitationalForce_C(sat1, earth.centerBody)
    CelestTest.assertEquals(g1.getForce, Vector3D(cos(Pi / 3) * -19.93d, sin(Pi / 3) * -19.93d, 0), 1E-1)
    CelestTest.assertEquals(g1.toAcceleration, Vector3D(cos(Pi / 3) * -3.986d, sin(Pi / 3) * -3.986d, 0), 1E-2)

    /* Test the f/a in a simple sun system (norm only) */
    val sun = new BodyCentered {
      def centerBody: CelestialBody = SolarConstants.body
    }

    val sat2 = new Body(new Keplerian(AU convert 1, 0, 0, 0, 0, 0, Some(sun)), 5)
    val g2 = new GravitationalForce_C(sat2, sun.centerBody)

    g2.getForce.norm should be(0.02965d plusOrMinus 1E-4)
    g2.toAcceleration.norm should be(0.005930d plusOrMinus 1E-5)
  }
  */

}
