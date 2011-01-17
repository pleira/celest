/**
 * Copyright (C) 2010 Simon Billemont <aodtorusan@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.libs.celest.targeters;

import org.apache.commons.math.MathException;

import be.angelcorp.libs.celest.stateVector.StateVector;
import be.angelcorp.libs.celest.trajectory.Trajectory;

/**
 * Find a transfer arc for a two-point boundary value problem (TPBVP)
 * 
 * @author simon
 * 
 */
public abstract class TPBVP {

	protected double		dT;
	protected StateVector	r1;
	protected StateVector	r2;

	public TPBVP(StateVector r1, StateVector r2, double dT) {
		this.r1 = r1;
		this.r2 = r2;
		this.dT = dT;
	}

	public abstract Trajectory getTrajectory() throws MathException;

}
