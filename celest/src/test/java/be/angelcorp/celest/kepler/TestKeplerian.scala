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

import be.angelcorp.celest.state.Keplerian
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class TestKeplerian extends FlatSpec with ShouldMatchers {

  "KeplerElements" should "return the correct equations type" in {
    assert(new Keplerian(1E3, 0, 1, 0.9, 0.8, 0.7, null).quantities.isInstanceOf[KeplerCircular[Null]])
    assert(new Keplerian(1E3, 0.5, 1, 0.9, 0.8, 0.7, null).quantities.isInstanceOf[KeplerEllipse[Null]])
    assert(new Keplerian(1E3, 1, 1, 0.9, 0.8, 0.7, null).quantities.isInstanceOf[KeplerParabola[Null]])
    assert(new Keplerian(1E3, 2, 1, 0.9, 0.8, 0.7, null).quantities.isInstanceOf[KeplerHyperbola[Null]])
  }

}
