/**
 * Copyright (C) 2011 Simon Billemont <aodtorusan@gmail.com>
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 3.0 Unported
 * (CC BY-NC 3.0) (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *        http://creativecommons.org/licenses/by-nc/3.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.libs.celest.body.bodyCollection;

import java.util.Collection;
import java.util.LinkedList;

import be.angelcorp.libs.celest.body.CelestialBody;

public class TwoBodyCollection implements BodyCollection {

	private CelestialBody	body1;
	private CelestialBody	body2;

	public TwoBodyCollection(CelestialBody body1, CelestialBody body2) {
		this.body1 = body1;
		this.body2 = body2;
	}

	@Override
	public Collection<CelestialBody> getBodies() {
		LinkedList<CelestialBody> l = new LinkedList<CelestialBody>();
		l.add(body1);
		l.add(body2);
		return l;
	}

	public CelestialBody getBody1() {
		return body1;
	}

	public CelestialBody getBody2() {
		return body2;
	}

	/**
	 * Find the other body when one body is known
	 * 
	 * @param body
	 *            One body in the twobody syste�
	 * @return the other body
	 */
	public CelestialBody other(CelestialBody body) {
		if (body == body1)
			return body2;
		else
			return body1;
	}

	public void setBody1(CelestialBody body1) {
		this.body1 = body1;
	}

	public void setBody2(CelestialBody body2) {
		this.body2 = body2;
	}

}
