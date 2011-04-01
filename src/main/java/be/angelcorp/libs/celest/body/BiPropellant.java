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
package be.angelcorp.libs.celest.body;

public class BiPropellant extends Propellant {

	private Propellant	primairy;
	private double		primairyPercent;
	private Propellant	secondairy;

	public BiPropellant(double isp, Propellant primairy, double primairyPercent, Propellant secondairy) {
		super(isp);
		this.primairy = primairy;
		this.primairyPercent = primairyPercent;
		this.secondairy = secondairy;
	}

	@Override
	public void consumeMass(double dM) {
		primairy.consumeMass(dM * primairyPercent);
		secondairy.consumeMass(dM * (1 - primairyPercent));
	}

	@Override
	public double getPropellantMass() {
		return primairy.getPropellantMass() + secondairy.getPropellantMass();
	}

	@Override
	public void setPropellantMass(double propellant) {
		primairy.setPropellantMass(propellant * primairyPercent);
		secondairy.setPropellantMass(propellant * (1 - primairyPercent));
	}
}
