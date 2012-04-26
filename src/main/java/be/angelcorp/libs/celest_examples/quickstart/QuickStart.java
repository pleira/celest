package be.angelcorp.libs.celest_examples.quickstart;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import be.angelcorp.libs.celest.body.CelestialBody;
import be.angelcorp.libs.celest.body.bodyCollection.TwoBodyCollection;
import be.angelcorp.libs.celest.constants.EarthConstants;
import be.angelcorp.libs.celest.kepler.KeplerEquations;
import be.angelcorp.libs.celest.maneuvers.ImpulsiveShot;
import be.angelcorp.libs.celest.state.positionState.ICartesianElements;
import be.angelcorp.libs.celest.state.positionState.IPositionState;
import be.angelcorp.libs.celest.state.positionState.KeplerElements;
import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.celest.time.JulianDate;
import be.angelcorp.libs.celest.trajectory.CompositeTrajectory;
import be.angelcorp.libs.celest.trajectory.ITrajectory;
import be.angelcorp.libs.celest.trajectory.KeplerTrajectory;
import be.angelcorp.libs.celest_examples.base.CelestExample;
import be.angelcorp.libs.celest_examples.base.Services;
import be.angelcorp.libs.math.linear.Vector3D;
import be.angelcorp.libs.util.io.CsvWriter;
import be.angelcorp.libs.util.physics.Time;

import com.lyndir.lhunath.lib.system.logging.Logger;

@CelestExample(name = "Quickstart example",
		description = "This is an example that uses a veriaty of tools in libs.celest to compute a sample scenario for a GTO orbit; raiging of the pericenter using impulive maneuvers.")
public class QuickStart implements Runnable {

	/** Create the earth based of a generic template, with the earth having the state <0,0,0,0,0,0> (R,V) */
	protected CelestialBody		earth	= EarthConstants.bodyCenter;

	/** The satellite that will perform the specific maneuver */
	protected Satellite			satellite;
	/** Time after the three kicks and an additional orbit */
	public IJulianDate			tf;

	private static final Logger	logger	= Logger.get(QuickStart.class);

	public ITrajectory getTrajectory() throws Exception {
		// Original orbit
		double Rp = EarthConstants.radiusMean + 190E3;
		double Ra = 50000E3;
		KeplerElements k = new KeplerElements((Ra + Rp) / 2, KeplerEquations.eccentricity(Rp, Ra), 0, 0, 0, 0, earth);

		// Make the earth and a satellite
		satellite = new Satellite(k);
		TwoBodyCollection uni = new TwoBodyCollection(earth, satellite);

		/* This is the trajectory that will store the different trajectory segments */
		/* You can add trajectory and a time from where this trajectory should be evaluted */
		CompositeTrajectory trajectory = new CompositeTrajectory();

		/* First leg of the trajectory, the orbit as it was injected by the atlas launcher */
		/* Without any intervention, it would keep this orbit */
		IJulianDate t0 = JulianDate.J2000;
		trajectory.addTrajectory(new KeplerTrajectory(k, t0), t0);

		/* Add first kick to the satellite */
		/* Compute dV for LAE (main engine) */
		/* Current speed in apogee */
		double Va = Math.sqrt(KeplerEquations.visViva(earth.getMu(), Ra, k.getSemiMajorAxis()));
		/* Raise the perigee to this value after 3 kicks */
		Rp = EarthConstants.radiusMean + 19000E3;
		/* Total dV for the 3 kicks */
		double dV = Math.sqrt(KeplerEquations.visViva(earth.getMu(), Ra, (Ra + Rp) / 2)) - Va;

		/* Time when we reach the kick location (after 1.5 periods */
		IJulianDate t = t0.add((3. / 2) * k.getOrbitEqn().period(), Time.second);
		satellite.setState(trajectory.evaluate(t));

		/* Make the LAE engine */
		ImpulsiveShot LAE = new ImpulsiveShot(satellite);
		/* Add the first kick of the LAE */
		IPositionState state = LAE.kick(dV / 3, satellite.getHydrazineLAE());
		/* Add the next leg to the trajectory */
		k = KeplerEquations.cartesian2kepler(state.toCartesianElements(), earth);
		trajectory.addTrajectory(new KeplerTrajectory(k, t), t);

		/* Add kick 2 */
		/* The time of the 2nd kick is the time of the first kick plus one orbit */
		t = t.add(k.getOrbitEqn().period(), Time.second);
		/* Same procedure */
		state = LAE.kick(dV / 3, satellite.getHydrazineLAE());
		k = KeplerEquations.cartesian2kepler(state.toCartesianElements(), earth);
		trajectory.addTrajectory(new KeplerTrajectory(k, t), t);

		/* And the third kick */
		t = t.add(k.getOrbitEqn().period(), Time.second);
		state = LAE.kick(dV / 3, satellite.getHydrazineLAE());
		k = KeplerEquations.cartesian2kepler(state.toCartesianElements(), earth);
		trajectory.addTrajectory(new KeplerTrajectory(k, t), t);

		tf = t.add(k.getOrbitEqn().period(), Time.second);

		/* Return the composite trajectory */
		return trajectory;
	}

	@Override
	public void run() {
		try {
			/* Compute the trajectory of the AEHF satellite for the first three kicks by the LAE engine */
			logger.inf("Creating the quickstart trajectory");
			ITrajectory trajectory = getTrajectory();

			/* Now you can simply evaluate the trajectory, and plot the results using an external app */
			long samples = 1000L;
			File ephemerisFile = Services.newFile("quickstart.csv");
			CsvWriter writer = new CsvWriter(ephemerisFile, "t", "rx", "ry", "rz", "vx", "vy", "vz");
			IJulianDate t0 = JulianDate.J2000;

			logger.inf("Saving ephemeris to file %s", ephemerisFile);
			List<ICartesianElements> states = new LinkedList<>();
			double tFinal = tf.relativeTo(t0, Time.second);
			for (double t = 0; t < tFinal; t += tFinal / samples) {
				IJulianDate time = t0.add(t, Time.second);
				ICartesianElements state = trajectory.evaluate(time).toCartesianElements();

				logger.dbg("At jd=%s the state is: %s", time, state);
				states.add(state);

				writer.write(t, state.getR().getX(), state.getR().getY(), state.getR().getZ(),
						state.getV().getX(), state.getV().getY(), state.getV().getZ());
			}

			logger.inf("Plotting the ephemeris");
			double[] x = new double[states.size()];
			double[] y = new double[states.size()];
			int i = 0;
			for (ICartesianElements input : states) {
				Vector3D n = Vector3D.K;
				Vector3D r = input.getR();
				Vector3D projected = r.subtract(n.multiply(r.dot(n)));
				x[i] = projected.getX();
				y[i] = projected.getY();
				i++;
			}
			Services.newPlot().addData(x, y).makeFrame();
		} catch (Exception e) {
			logger.err(e, "Unexpected exception when trying to solve the quickstart example: ");
		}
	}
}