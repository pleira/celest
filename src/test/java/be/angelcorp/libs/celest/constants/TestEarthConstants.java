package be.angelcorp.libs.celest.constants;

import junit.framework.TestCase;

import org.apache.commons.math.FunctionEvaluationException;

import be.angelcorp.libs.celest.stateVector.CartesianElements;
import be.angelcorp.libs.celest.stateVector.NonSignuarElements;
import be.angelcorp.libs.celest.stateVector.SphericalElements;
import be.angelcorp.libs.celest.time.JulianDate;
import be.angelcorp.libs.celest.trajectory.KeplerVariationTrajectory;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.util.physics.Angle;
import be.angelcorp.libs.util.physics.Length;
import be.angelcorp.libs.util.physics.Time;

public class TestEarthConstants extends TestCase {

	public void testOrbit() throws FunctionEvaluationException {
		KeplerVariationTrajectory trajectory = EarthConstants.orbit;

		// Validation state based on JPL Horizons data Body: Earth (399)
		// Frame: @Sun, ecliptic, J2000
		// AU/days/Degree

		NonSignuarElements state_actual_k = trajectory.evaluate(JulianDate.getJ2000());
		CartesianElements state_actual_c = state_actual_k.toCartesianElements();
		SphericalElements state_actual_s = new SphericalElements(state_actual_c, state_actual_k.getCenterbody());
		CartesianElements state_true_c = new CartesianElements(
				new Vector3D(-1.771351029694605E-01, 9.672416861070041E-01, -4.092421117973204E-06)
						.multiply(Length.AU.getScaleFactor()),
				new Vector3D(-1.720762505701730E-02, -3.158782207802509E-03, 1.050630211603302E-07)
						.multiply(Length.AU.getScaleFactor() / Time.day.getScaleFactor()));
		SphericalElements state_true_s = new SphericalElements(state_true_c, state_actual_k.getCenterbody());
		// error see http://ssd.jpl.nasa.gov/?planet_pos
		assertEquals(state_true_c.getR().getNorm(), state_actual_c.getR().getNorm(), 15e6); // R
		assertEquals(state_true_s.getRightAscension(), state_actual_s.getRightAscension(),
				Angle.convert(40, Angle.ARCSECOND)); // RA
		assertEquals(state_true_s.getDeclination(), state_actual_s.getDeclination(),
				Angle.convert(15, Angle.ARCSECOND)); // DEC

		state_actual_k = trajectory.evaluate(new JulianDate(2452000d));
		state_actual_c = state_actual_k.toCartesianElements();
		state_actual_s = new SphericalElements(state_actual_c, state_actual_k.getCenterbody());
		state_true_c = new CartesianElements(
				new Vector3D(-9.813457035727633E-01, -1.876731681458192E-01, 1.832964606032273E-06)
						.multiply(Length.AU.getScaleFactor()),
				new Vector3D(2.958686601622319E-03, -1.696218721190317E-02, -5.609085586954323E-07)
						.multiply(Length.AU.getScaleFactor() / Time.day.getScaleFactor()));
		state_true_s = new SphericalElements(state_true_c, state_actual_k.getCenterbody());
		assertEquals(state_true_c.getR().getNorm(), state_actual_c.getR().getNorm(), 15e6); // R
		assertEquals(state_true_s.getRightAscension(), state_actual_s.getRightAscension(),
				Angle.convert(40, Angle.ARCMINUTE)); // RA TODO; why arcminute accuracy here ?
		assertEquals(state_true_s.getDeclination(), state_actual_s.getDeclination(),
				Angle.convert(15, Angle.ARCSECOND)); // DEC

	}
}
