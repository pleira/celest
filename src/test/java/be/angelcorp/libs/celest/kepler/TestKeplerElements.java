package be.angelcorp.libs.celest.kepler;

import junit.framework.TestCase;

import org.apache.commons.math.linear.RealVector;

import be.angelcorp.libs.celest.stateVector.KeplerElements;

public class TestKeplerElements extends TestCase {

	public void testEquationType() {
		KeplerElements kCirc = new KeplerElements(1E3, 0, 1, 0.9, 0.8, 0.7);
		assertTrue(KeplerCircular.class.isInstance(kCirc.getOrbitEqn()));

		KeplerElements kEll = new KeplerElements(1E3, 0.5, 1, 0.9, 0.8, 0.7);
		assertTrue(KeplerEllipse.class.isInstance(kEll.getOrbitEqn()));

		KeplerElements kPara = new KeplerElements(1E3, 1, 1, 0.9, 0.8, 0.7);
		assertTrue(KeplerParabola.class.isInstance(kPara.getOrbitEqn()));

		KeplerElements kHyper = new KeplerElements(1E3, 2, 1, 0.9, 0.8, 0.7);
		assertTrue(KeplerHyperbola.class.isInstance(kHyper.getOrbitEqn()));
	}

	public void testVectorizing() {
		KeplerElements k = new KeplerElements(1E3, 0.2, 1, 0.9, 0.8, 0.7);

		RealVector vector = k.toVector();
		assertEquals(vector.getDimension(), 6);

		KeplerElements k2 = KeplerElements.fromVector(vector);
		assertTrue(k.equals(k2));
	}

}
