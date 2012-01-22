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
package be.angelcorp.libs.celest.trajectory;

import org.apache.commons.math.FunctionEvaluationException;

import be.angelcorp.libs.celest.stateVector.KeplerElements;
import be.angelcorp.libs.celest.stateVector.NonSignuarElements;
import be.angelcorp.libs.celest.stateVector.NonSingularDerivative;
import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.celest.time.JulianDate;
import be.angelcorp.libs.util.physics.Time;

/**
 * A trajectory that computes the instantaneous {@link KeplerElements} based on an initial set of
 * {@link KeplerElements} and there variation ({@link NonSingularDerivative}). Compared to
 * {@link KeplerTrajectory}, this trajectory uses varying Kepler elements whereas
 * {@link KeplerTrajectory} uses pure Keplerian propagation (2 body motion).
 * 
 * @author Simon Billemont
 * 
 */
public class KeplerVariationTrajectory implements ITrajectory {

	private final JulianDate			referenceDate;
	private final NonSignuarElements	referenceElements;
	private final NonSingularDerivative	elementDerivatives;

	public KeplerVariationTrajectory(JulianDate referenceDate, NonSignuarElements referenceElements,
			NonSingularDerivative elementDerivatives) {
		this.referenceDate = referenceDate;
		this.referenceElements = referenceElements;
		this.elementDerivatives = elementDerivatives;
	}

	@Override
	public NonSignuarElements evaluate(IJulianDate t) throws FunctionEvaluationException {
		double dT = t.relativeTo(referenceDate, Time.second);

		double a_new = referenceElements.getSemiMajorAxis()
				+ dT * elementDerivatives.getSemiMajorAxisVariation();
		double e_new = referenceElements.getEccentricity()
				+ dT * elementDerivatives.getEccentricityVariation();
		double i_new = referenceElements.getInclination()
				+ dT * elementDerivatives.getInclinationVariation();
		double w_true_new = referenceElements.getLongitudePerihelion()
				+ dT * elementDerivatives.getLongitudePerihelionVariation();
		double W_new = referenceElements.getRaan()
				+ dT * elementDerivatives.getRaanVariation();
		double lambda_M_new = referenceElements.getMeanLongditude()
				+ dT * elementDerivatives.getMeanLongitudeVariation();

		NonSignuarElements k2 = new NonSignuarElements(a_new, e_new, i_new, w_true_new, W_new, lambda_M_new,
				referenceElements.getCenterbody());

		return k2;
	}

	public NonSingularDerivative getElementDerivatives() {
		return elementDerivatives;
	}

	public JulianDate getReferenceDate() {
		return referenceDate;
	}

	public NonSignuarElements getReferenceElements() {
		return referenceElements;
	}
}
