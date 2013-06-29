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
package be.angelcorp.libs.celest.kepler

import org.apache.commons.math3.linear.RealVector

import be.angelcorp.libs.celest.state.positionState.IKeplerElements
import be.angelcorp.libs.celest.state.positionState.KeplerElements
import be.angelcorp.libs.celest.unit.CelestTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class TestKeplerElements extends FlatSpec with ShouldMatchers {

  "KeplerElements" should "return the correct equations type" in {
		assert( new KeplerElements(1E3,   0, 1, 0.9, 0.8, 0.7).getOrbitEqn.isInstanceOf[KeplerCircular]  )
    assert( new KeplerElements(1E3, 0.5, 1, 0.9, 0.8, 0.7).getOrbitEqn.isInstanceOf[KeplerEllipse]   )
    assert( new KeplerElements(1E3,   1, 1, 0.9, 0.8, 0.7).getOrbitEqn.isInstanceOf[KeplerParabola]  )
    assert( new KeplerElements(1E3,   2, 1, 0.9, 0.8, 0.7).getOrbitEqn.isInstanceOf[KeplerHyperbola] )
	}

	it should "correctly serialize to a vector"in {
		val k = new KeplerElements(1E3, 0.2, 1, 0.9, 0.8, 0.7)

		val vector = k.toVector

    expect(6)(vector.getDimension)

    expect(1E3)(vector.getEntry(0))
    expect(0.2)(vector.getEntry(1))
    expect(1  )(vector.getEntry(2))
    expect(0.9)(vector.getEntry(3))
    expect(0.8)(vector.getEntry(4))
    expect(0.7)(vector.getEntry(5))

		val k2 = KeplerElements.fromVector(vector)
		assert(k.equals(k2))
	}

}
