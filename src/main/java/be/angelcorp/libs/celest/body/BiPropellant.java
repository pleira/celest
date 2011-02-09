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
