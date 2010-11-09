package be.angelcorp.libs.celest.kepler;

import be.angelcorp.libs.celest.stateVector.KeplerElements;

public class KeplerCircular extends KeplerEquations {

	public static double Vc(double r, double mhu) {
		return Math.sqrt(mhu / r);
	}

	public KeplerCircular(KeplerElements k) {
		super(k);
	}

	public double arealVel(double mu, double a) {
		return Math.sqrt(mu * a) / 2;
	}

	@Override
	public double arealVel(double mu, double a, double e) {
		return arealVel(mu, a);
	}

	/**
	 * Areal velocity in function of Semi-major axis and the Mean motion
	 * 
	 * @param a
	 *            Semi-major axis
	 * @param n
	 *            Mean motion
	 * @return Areal velocity \dot{A}
	 */
	public double arealVelFromMeanMotion(double a, double n) {
		return a * a * n / 2;
	}

	public double perifocalDistance(double a) {
		return a;
	}

	@Override
	public double perifocalDistance(double a, double e) {
		return a;
	}

	@Override
	public double period(double n) {
		return 2 * Math.PI / n;
	}

	@Override
	public double totEnergyPerMass(double mu, double a) {
		return -mu / (2 * a);
	}

	@Override
	public double velocitySq(double mu, double r, double a) {
		return mu / r;
	}

}
