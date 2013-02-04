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
package be.angelcorp.libs.celest.body.bodyCollection;

import java.util.Collection;
import java.util.LinkedList;

import be.angelcorp.libs.celest.body.ICelestialBody;

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
	private ICelestialBody body1;
	/**
	 * Second body in the collection (usually the satellite body)
	 */
	private ICelestialBody body2;

	/**
	 * Create a collection of two known bodies
	 * 
	 * @param body1
	 *            The first body
	 * @param body2
	 *            The second body
	 */
	public TwoBodyCollection(ICelestialBody body1, ICelestialBody body2) {
		this.body1 = body1;
		this.body2 = body2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<ICelestialBody> getBodies() {
		LinkedList<ICelestialBody> l = new LinkedList<ICelestialBody>();
		l.add(body1);
		l.add(body2);
		return l;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICelestialBody getBody1() {
		return body1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICelestialBody getBody2() {
		return body2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICelestialBody other(ICelestialBody body) {
		if (body == body1)
			return body2;
		else
			return body1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBody1(ICelestialBody body1) {
		this.body1 = body1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBody2(ICelestialBody body2) {
		this.body2 = body2;
	}

}
