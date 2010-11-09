package be.angelcorp.libs.celest.kepler;

import be.angelcorp.libs.celest.stateVector.KeplerElements;

public class KeplerHyperbola extends KeplerEquations {

	public KeplerHyperbola(KeplerElements k) {
		super(k);
	}

	@Override
	public double arealVel(double mu, double a, double e) {
		return Math.sqrt(a * mu * (1 - e * e)) / 2;
	}

	@Override
	public double perifocalDistance(double a, double e) {
		return a * (1 - e);
	}

	@Override
	public double period(double n) {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public double totEnergyPerMass(double mu, double a) {
		return mu / (2 * a);
	}

	@Override
	public double velocitySq(double mu, double r, double a) {
		return mu / (2 / r - 1 / a);
	}

}
