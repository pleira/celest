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
package be.angelcorp.libs.celest.eom.forcesmodel;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.constants.Constants;
import be.angelcorp.libs.celest.math.Cartesian;
import be.angelcorp.libs.celest.physics.EM.IEMspectrum;
import be.angelcorp.libs.celest.physics.quantities.ObjectForce;
import be.angelcorp.libs.math.linear.Vector3D;

/**
 * Force created by a stars radiation pressure on a satellite. Internally uses cartesian form.
 * 
 * @author simon
 * 
 */
public class SolarRadiationPressure_C extends ObjectForce implements Cartesian {

	/**
	 * Cross sectional (reflective) area
	 * <p>
	 * <b>Unit: [m<sup>2</sup>]</b>
	 * </p>
	 */
	private double			area;
	/**
	 * Satellite coefficient of reflectivity
	 * <p>
	 * <b>Unit: [-]</b>
	 * </p>
	 */
	private double			CR;
	/**
	 * Body that emits a flux that pushes on the spacecraft
	 */
	private CelestialBody	star;
	/**
	 * Flux that the star emits
	 */
	private IEMspectrum		spectrum;

	/**
	 * Solar radiation pressure force
	 * 
	 * @param satellite
	 *            Satellite the force acts upon
	 * @param area
	 *            Effective area of the satellite {@link SolarRadiationPressure_C#area}
	 * @param CR
	 *            Reflectivity coefficient {@link SolarRadiationPressure_C#CR}
	 * @param star
	 *            The star that creates emits the flux {@link SolarRadiationPressure_C#star}
	 * @param emmition
	 *            The emmision sprectum of the star {@link SolarRadiationPressure_C#spectrum}
	 */
	public SolarRadiationPressure_C(CelestialBody satellite, double area, double CR,
			CelestialBody star, IEMspectrum emmition) {
		super(satellite);
		this.area = area;
		this.CR = CR;
		this.star = star;
		this.spectrum = emmition;
	}

	/**
	 * @see SolarRadiationPressure_C#area
	 */
	public double getArea() {
		return area;
	}

	/**
	 * @see SolarRadiationPressure_C#CR
	 */
	public double getCR() {
		return CR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3D getForce() {
		return toAcceleration().multiply(getObject().getMass());
	}

	/**
	 * @see SolarRadiationPressure_C#spectrum
	 */
	public IEMspectrum getSpectrum() {
		return spectrum;
	}

	/**
	 * @see SolarRadiationPressure_C#star
	 */
	public CelestialBody getStar() {
		return star;
	}

	/**
	 * @see SolarRadiationPressure_C#area
	 */
	public void setArea(double area) {
		this.area = area;
	}

	/**
	 * @see SolarRadiationPressure_C#CR
	 */
	public void setCR(double cR) {
		CR = cR;
	}

	/**
	 * @see SolarRadiationPressure_C#spectrum
	 */
	public void setSpectrum(IEMspectrum spectrum) {
		this.spectrum = spectrum;
	}

	/**
	 * @see SolarRadiationPressure_C#star
	 */
	public void setStar(CelestialBody star) {
		this.star = star;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3D toAcceleration() {
		// Relative position vector of spacecraft w.r.t. Sun (from the sun to s/c)
		Vector3D d = getObject().getState().toCartesianElements().getR().subtract(
						getStar().getState().toCartesianElements().getR());

		double dnorm = d.getNorm();
		double dcube = dnorm * dnorm * dnorm;

		double Ls = getSpectrum().totalFlux() / d.getNormSq(); // [W]

		double factor = CR * (area / getObject().getMass()) *
							Ls / (4 * Math.PI * Constants.SPEED_LIGHT * dcube);

		return d.multiply(factor);
	}

}
