/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
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
package be.angelcorp.celest.kepler

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class TestOrbitPropagatorImpl extends FlatSpec with ShouldMatchers {

  /*
  val delta = 1E-3
  // 0.1%

  implicit val universe = new DefaultUniverse
  val earthFrame = new BodyCentered {
    def centerBody: CelestialBody = EarthConstants.bodyCenter
  }

  /**
   * Tests the propagation of geo satellite over one month. The end radius must be within 2m of the
   * start geo radius (0.6m should be achieved)
   */
  "OrbitPropagatorImpl" should "maintain a constant radius for a GEO orbit" in {
    /* Geostationairy start position */
    val x0 = PosVel(42164000, 0, 0, 0, 3074.6663, 0, None)

    /* Create the two body problem */
    val b = new Body(x0, 1)
    val tb = new TwoBody(new TwoBodyCollection(b, earthFrame.centerBody), b)

    // TODO: Broken
    //val integrator = new ClassicalRungeKuttaIntegrator(60.)
    //val propagator = new CommonsMathPropagator(integrator, tb)

    //val ans = propagator.integrate( universe.J2000_EPOCH, universe.J2000_EPOCH.add(1, Time.month), x0)

    //ans.position.norm should be (x0.position.norm plusOrMinus 1.0 )
  }

  it should "correctly propagate a LEO orbit" in {
    val rk4 = new ClassicalRungeKuttaIntegrator(2)
    rk4.addStepHandler(new StepHandler() {
      override def handleStep(interpolator: StepInterpolator, isLast: Boolean) {
        val t = interpolator.getCurrentTime - Epochs.J2000.jd
        val y = interpolator.getInterpolatedState
        if (abs(t - 2) < delta) {
          val step1True = Array[Double](6640305.22, 16251.75, 0.0, -18.08, 8125.86, 0.0)
          CelestTest.assertEquals("Step one is not computed correctly", step1True, y,
            new ArrayRealVector(step1True).map(new Abs()).mapMultiply(delta).toArray)
        } else if (Math.abs(t - 4) < delta) {
          val step2True = Array[Double](6640287.14, 32503.54, 0.0, -36.16, 8125.84, 0.0)
          CelestTest.assertEquals("Step two is not computed correctly", step2True, y,
            new ArrayRealVector(step2True).map(new Abs()).mapMultiply(delta).toArray)
        } else {
          throw new AssertionError("Errr, time steps should either be 2 or 4 but t = " + t + " (integrate from 0 to 4 with steps of 2)")
        }
      }

      override def init(t0: Double, y0: Array[Double], t: Double) {}
    })

    /* Leo orbit */
    val k = new Keplerian(7378137, 0.1, 0, 0, 0, 0, Some(earthFrame))
    val c = k.toPosVel
    /* Create the two body problem */
    val b = new Body(c, 1)
    val tb = new TwoBody(new TwoBodyCollection(b, earthFrame.centerBody), b)

    // TODO: Broken
    //val integrator = new CommonsMathPropagator(rk4, tb)
    //integrator.integrate(universe.J2000_EPOCH, universe.J2000_EPOCH.add(4, Time.day), c)
  }
  */

}
