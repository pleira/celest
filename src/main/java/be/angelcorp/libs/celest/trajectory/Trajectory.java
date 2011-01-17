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
package be.angelcorp.libs.celest.trajectory;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.linear.RealVector;

import be.angelcorp.libs.celest.stateVector.StateVector;
import be.angelcorp.libs.math.functions.UnivariateVectorFunction;

/**
 * 
 * @author simon
 * 
 */
public abstract class Trajectory implements UnivariateVectorFunction {

	public abstract StateVector evaluate(double t) throws FunctionEvaluationException;

	@Override
	public RealVector value(double t) throws FunctionEvaluationException {
		return evaluate(t).toVector();
	}
}
