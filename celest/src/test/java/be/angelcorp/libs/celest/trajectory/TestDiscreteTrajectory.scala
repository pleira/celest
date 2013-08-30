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
package be.angelcorp.libs.celest.trajectory

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import be.angelcorp.libs.celest.universe.DefaultUniverse
import be.angelcorp.libs.celest.time.JulianDate
import be.angelcorp.libs.celest.state.PosVel

@RunWith(classOf[JUnitRunner])
class TestDiscreteTrajectory extends FlatSpec with ShouldMatchers  {

  implicit val universe = new DefaultUniverse

  "DiscreteTrajectory" should "correctly query the embedded states" in {
		val trajectory = new DiscreteTrajectory()

		// Add various states at various times
    val s1 = PosVel()
    val s2 = PosVel()
    val s3 = PosVel()
		trajectory.states.put(new JulianDate(0 ), s1)
		trajectory.states.put(new JulianDate(10), s2)
		trajectory.states.put(new JulianDate(20), s3)

    expect(s1) { trajectory(new JulianDate(0 )) } // Equal begin time as s1
    expect(s1) { trajectory(new JulianDate(5 )) } // In between s1 and s2
    expect(s2) { trajectory(new JulianDate(15)) } // Same as above but s2
    expect(s3) { trajectory(new JulianDate(20)) } // Same insertion time as s3
    expect(s3) { trajectory(new JulianDate(25)) } // Time after the last insertion
	}

  it should "fail when queried for an epoch before the first state" in {
    val trajectory = new DiscreteTrajectory()

    // Add a state a t=0
    trajectory.states.put(new JulianDate(0), PosVel())

    intercept[ArithmeticException] {
      trajectory(new JulianDate(-1)) // There is no state on or before -1 so exception
    }
  }

}
