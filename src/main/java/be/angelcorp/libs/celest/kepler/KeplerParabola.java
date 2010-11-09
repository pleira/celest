package be.angelcorp.libs.celest.kepler;

import be.angelcorp.libs.celest.stateVector.KeplerElements;

public class KeplerParabola extends KeplerEquations {

	public KeplerParabola(KeplerElements k) {
		super(k);
	}

	@Override
	public double arealVel(double mu, double a, double e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double perifocalDistance(double a, double e) {
		return a / 2;
	}

	@Override
	public double period(double n) {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public double totEnergyPerMass(double mu, double a) {
		return 0;
	}

	@Override
	public double velocitySq(double mu, double r, double a) {
		return 2 * mu / r;
	}

}
