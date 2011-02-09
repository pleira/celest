package be.angelcorp.libs.celest.body;

public class Propellant {

	private static final double	G0	= 9.8;

	private double				isp;
	private double				propellantLeft;

	public Propellant(double isp) {
		this(isp, 0);
	}

	public Propellant(double isp, double propellantLeft) {
		this.isp = isp;
		this.propellantLeft = propellantLeft;
	}

	public void consumeDV(CelestialBody body, double dV) {
		// Tsiolkovsky:
		// m0/m1 = e ^ (Dv / Veff)
		// (body.getMass() + dM) / body.getMass() = e ^ (Dv / Veff)
		double m = body.getMass();
		double dM = m * Math.exp(dV / getVeff()) - m;
		consumeMass(dM);
	}

	public void consumeMass(double dM) {
		propellantLeft -= dM;
	}

	public double getIsp() {
		return isp;
	}

	public double getPropellantMass() {
		return propellantLeft;
	}

	/**
	 * Get the effective exhaust velocity
	 * <p>
	 * Veff = Isp * G0;
	 * </p>
	 * 
	 * <p>
	 * <b>Unit: [m/s]</b>
	 * </p>
	 * 
	 * @return
	 */
	public double getVeff() {
		return isp * G0;
	}

	public void setPropellantMass(double propellant) {
		propellantLeft = propellant;
	}
}
