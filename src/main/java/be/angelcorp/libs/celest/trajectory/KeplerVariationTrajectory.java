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
package be.angelcorp.libs.celest.trajectory;

import org.apache.commons.math.FunctionEvaluationException;

import be.angelcorp.libs.celest.stateVector.KeplerDerivative;
import be.angelcorp.libs.celest.stateVector.KeplerElements;
import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.celest.time.JulianDate;
import be.angelcorp.libs.util.physics.Time;

/**
 * A trajectory that computes the instantaneous {@link KeplerElements} based on an initial set of
 * {@link KeplerElements} and there variation ({@link KeplerDerivative}). Compared to
 * {@link KeplerTrajectory}, this trajectory uses varying Kepler elements whereas
 * {@link KeplerTrajectory} uses pure Keplerian propagation (2 body motion).
 * 
 * @author Simon Billemont
 * 
 */
public class KeplerVariationTrajectory implements ITrajectory {

	private JulianDate			referenceDate;
	private KeplerElements		referenceElements;
	private KeplerDerivative	elementDerivatives;

	private KeplerVariationTrajectory(JulianDate referenceDate, KeplerElements referenceElements,
			KeplerDerivative elementDerivatives) {
		this.referenceDate = referenceDate;
		this.referenceElements = referenceElements;
		this.elementDerivatives = elementDerivatives;
	}

	@Override
	public KeplerElements evaluate(IJulianDate t) throws FunctionEvaluationException {
		double dT = t.relativeTo(referenceDate, Time.second);
		KeplerElements k2 = new KeplerElements(
				referenceElements.getSemiMajorAxis() + dT * elementDerivatives.getSemiMajorAxisVariation(),
				referenceElements.getEccentricity() + dT * elementDerivatives.getEccentricityVariation(),
				referenceElements.getInclination() + dT * elementDerivatives.getInclinationVariation(),
				referenceElements.getArgumentPeriapsis() + dT
						* elementDerivatives.getArgumentPeriapsisVariation(),
				referenceElements.getRaan() + dT * elementDerivatives.getRaanVariation(),
				referenceElements.getTrueAnomaly() + dT * elementDerivatives.getTrueAnomalyVariation(),
				referenceElements.getCenterbody());
		return k2;
	}

}
