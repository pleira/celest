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
package be.angelcorp.libs.celest.body.bodyCollection;

import java.util.Collection;
import java.util.LinkedList;

import be.angelcorp.libs.celest.body.CelestialBody;

/**
 * Implementation of {@link ITwoBodyCollection}, this is a collection holding always just two bodies.
 * There are some methods to allow for getting the other body in the collection if one is known.
 * 
 * @author Simon Billemont
 * @see ITwoBodyCollection
 */
public class TwoBodyCollection implements ITwoBodyCollection {

	/**
	 * First body in the collection (usually the center body)
	 */
	private CelestialBody	body1;
	/**
	 * Second body in the collection (usually the satellite body)
	 */
	private CelestialBody	body2;

	/**
	 * Create a collection of two known bodies
	 * 
	 * @param body1
	 *            The first body
	 * @param body2
	 *            The second body
	 */
	public TwoBodyCollection(CelestialBody body1, CelestialBody body2) {
		this.body1 = body1;
		this.body2 = body2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<CelestialBody> getBodies() {
		LinkedList<CelestialBody> l = new LinkedList<CelestialBody>();
		l.add(body1);
		l.add(body2);
		return l;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CelestialBody getBody1() {
		return body1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CelestialBody getBody2() {
		return body2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CelestialBody other(CelestialBody body) {
		if (body == body1)
			return body2;
		else
			return body1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBody1(CelestialBody body1) {
		this.body1 = body1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBody2(CelestialBody body2) {
		this.body2 = body2;
	}

}
