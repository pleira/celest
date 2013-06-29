/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
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
package be.angelcorp.libs.celest.eom.forcesmodel

import scala.math._

import be.angelcorp.libs.celest.body.CelestialBody
import be.angelcorp.libs.celest.constants.EarthConstants
import be.angelcorp.libs.celest.constants.SolarConstants
import be.angelcorp.libs.celest.kepler.KeplerCircular
import be.angelcorp.libs.celest.state.positionState.KeplerElements
import be.angelcorp.libs.celest.state.positionState.SphericalElements
import be.angelcorp.libs.celest.unit.CelestTest
import be.angelcorp.libs.celest.universe.DefaultUniverse
import be.angelcorp.libs.math.linear.Vector3D
import be.angelcorp.libs.util.physics.Length._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
;

@RunWith(classOf[JUnitRunner])
class TestGravitationalForce  extends FlatSpec with ShouldMatchers  {

  implicit val universe = new DefaultUniverse

  "GravitationalForce" should "generate the correct force / acceleration" in {
		/* Test the f/a in a simple earth system (norm only) */
		val earth = EarthConstants.bodyCenter
    val sat = new CelestialBody( new KeplerElements(10E6, 0, 0, 0, 0, 0, earth), 5)
    val g = new GravitationalForce_C(sat, earth)

    g.getForce.norm       should be ( 19.93d plusOrMinus 1E-1 )
		g.toAcceleration.norm should be ( 3.986d plusOrMinus 1E-2 )

		/* Test the f/a in a simple earth system all components */
		val sat1 = new CelestialBody(new SphericalElements(10E6, Pi / 3, 0, KeplerCircular.vc(10E6, earth.getMu), 0, 0, earth), 5)
		val g1   = new GravitationalForce_C(sat1, earth)
    CelestTest.assertEquals(g1.getForce,       Vector3D(cos(Pi / 3) * -19.93d, sin(Pi / 3) * -19.93d, 0), 1E-1 )
		CelestTest.assertEquals(g1.toAcceleration, Vector3D(cos(Pi / 3) * -3.986d, sin(Pi / 3) * -3.986d, 0), 1E-2 )

		/* Test the f/a in a simple sun system (norm only) */
		val sun  = SolarConstants.body
		val sat2 = new CelestialBody( new KeplerElements( AU convert 1, 0, 0, 0, 0, 0, sun), 5)
    val g2 = new GravitationalForce_C(sat2, sun)

    g2.getForce.norm       should be ( 0.02965d  plusOrMinus 1E-4 )
    g2.toAcceleration.norm should be ( 0.005930d plusOrMinus 1E-5 )
	}
}
