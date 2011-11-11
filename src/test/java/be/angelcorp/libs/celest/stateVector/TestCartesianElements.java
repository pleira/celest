/**
 * Copyright (C) 2011 simon <aodtorusan@gmail.com>
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
package be.angelcorp.libs.celest.stateVector;

import java.util.List;

import be.angelcorp.libs.math.linear.Vector3D;

import com.google.common.collect.Lists;

public class TestCartesianElements extends TestStateVector<CartesianElements> {

	@Override
	public List<CartesianElements> getTestStateVectors() {
		List<CartesianElements> l = Lists.newLinkedList();
		l.add(new CartesianElements(Vector3D.random(), Vector3D.random()));
		return l;
	}

	@Override
	public void testToFromCartesianElements(CartesianElements state) {
		assertEquals(state, state.toCartesianElements());
	}

}
