package be.angelcorp.libs.celest.maneuvers.targeters.lambert;

import junit.framework.TestCase;

import org.apache.commons.math.MathException;
import org.junit.Ignore;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.constants.Constants;
import be.angelcorp.libs.celest.state.positionState.CartesianElements;
import be.angelcorp.libs.celest.state.positionState.ICartesianElements;
import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.celest.time.JulianDate;
import be.angelcorp.libs.celest.trajectory.ITrajectory;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.util.physics.Time;

@Ignore
public class TestLambertUV extends TestCase {

	public void testRossettaLeg1() throws MathException {
		// Rosetta Earth (launch) - Earth (swingby1)
		// data from NASA horizons, results generated using GTOP lambert targetter

		// [x2,y2,z2] = sph2cart(225*degree, 0*degree, au);
		// [x1,y1,z1] = sph2cart(30*degree, 21*degree, au);
		// [V1, V2] = lambert([x1,y1,z1]/1e3, [x2,y2,z2]/1e3, 300, 0, 1.32712428e11)
		// Result:
		// V1 = 27.4408 -14.0438 0.5468 (km/s)
		// V2 = -24.5242 18.6776 -0.4856 (km/s)

		CelestialBody center = new CelestialBody();
		center.setMass(Constants.mu2mass(1.32712428E20));

		Vector3D r1 = new Vector3D(1.364377463519496E11, 6.129036612130551E10, 2.784835397959758E09);
		Vector3D r2 = new Vector3D(3.730051396741382E09, -1.495513611895726E11, 0.);

		IJulianDate departure = JulianDate.getJ2000();
		IJulianDate arrival = JulianDate.getJ2000().add(300, Time.day);
		LambertUV lambert = new LambertUV(
				new CartesianElements(r1, Vector3D.ZERO),
				new CartesianElements(r2, Vector3D.ZERO),
				center, departure, arrival, false);

		ITrajectory trajectory = lambert.getTrajectory();
		Vector3D v1Expected = new Vector3D(2.744082030955964E04, -1.404383002109151E04, 5.468193199081889e+002);
		Vector3D v2Expected = new Vector3D(-2.452424882838209E04, 1.867758520103548E04, -4.856158493467824e+002);

		// Check to 1 mm/s accuracy:
		ICartesianElements state1 = trajectory.evaluate(departure).toCartesianElements();
		assertTrue(state1.equals(new CartesianElements(r1, v1Expected), 1e-3));
		ICartesianElements state2 = trajectory.evaluate(arrival).toCartesianElements();
		assertTrue(state1.equals(new CartesianElements(r2, v2Expected), 1e-3));
	}
}
