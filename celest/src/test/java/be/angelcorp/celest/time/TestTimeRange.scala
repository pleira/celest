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
package be.angelcorp.celest.time

import be.angelcorp.celest.universe.DefaultUniverse
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class TestTimeRange extends FlatSpec with ShouldMatchers {

  implicit val universe = new DefaultUniverse

  "TimeRange" should "iterate over the correct Epochs" in {
    val start = new JulianDate(1.0)
    val end = new JulianDate(4.0)

    val until1 = TimeRange.apply(start, end)
    until1.contains(new JulianDate(0.0)) should equal(false)
    until1.contains(new JulianDate(1.0)) should equal(true)
    until1.contains(new JulianDate(2.0)) should equal(true)
    until1.contains(new JulianDate(3.0)) should equal(true)
    until1.contains(new JulianDate(4.0)) should equal(false)
    until1.contains(new JulianDate(5.0)) should equal(false)

    val until2 = TimeRange.apply(start, end, 2.0)
    until2.contains(new JulianDate(0.0)) should equal(false)
    until2.contains(new JulianDate(1.0)) should equal(true)
    until2.contains(new JulianDate(2.0)) should equal(false)
    until2.contains(new JulianDate(3.0)) should equal(true)
    until2.contains(new JulianDate(4.0)) should equal(false)
    until2.contains(new JulianDate(5.0)) should equal(false)

    val to1 = TimeRange.inclusive(start, end)
    to1.contains(new JulianDate(0.0)) should equal(false)
    to1.contains(new JulianDate(1.0)) should equal(true)
    to1.contains(new JulianDate(2.0)) should equal(true)
    to1.contains(new JulianDate(3.0)) should equal(true)
    to1.contains(new JulianDate(4.0)) should equal(true)
    to1.contains(new JulianDate(5.0)) should equal(false)

    val to2 = TimeRange.inclusive(start, end, 2.0)
    to2.contains(new JulianDate(0.0)) should equal(false)
    to2.contains(new JulianDate(1.0)) should equal(true)
    to2.contains(new JulianDate(2.0)) should equal(false)
    to2.contains(new JulianDate(3.0)) should equal(true)
    to2.contains(new JulianDate(4.0)) should equal(false)
    to2.contains(new JulianDate(5.0)) should equal(false)
  }

}
