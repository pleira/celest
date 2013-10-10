/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
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
package be.angelcorp.libs.celest_examples.examples.quickstart

import math._
import org.slf4j.LoggerFactory
import be.angelcorp.libs.celest_examples.gui.{Services, CelestExample}
import be.angelcorp.libs.celest.universe.DefaultUniverse
import be.angelcorp.libs.celest.kepler._
import be.angelcorp.libs.celest.trajectory.{KeplerTrajectory, CompositeTrajectory}
import be.angelcorp.libs.util.physics.Time
import be.angelcorp.libs.celest.maneuvers.ImpulsiveShot
import be.angelcorp.libs.util.io.CsvWriter
import be.angelcorp.libs.celest.time.Epoch
import be.angelcorp.libs.math.linear.Vector3D
import be.angelcorp.libs.celest.constants.EarthConstants
import be.angelcorp.libs.celest.state.Keplerian
import be.angelcorp.libs.celest.frameGraph
import be.angelcorp.libs.celest.frameGraph.frames.BodyCentered

@CelestExample(
  name = "Quickstart example",
	description = "This is an example that uses a variety of tools in Celest to compute a sample scenario for a GTO orbit; raising of the pericenter using impulsive maneuvers.")
class QuickStart {
 val logger	= LoggerFactory.getLogger(classOf[QuickStart])

  /** Create the default universe (defines time standards and reference frameGraph) */
  implicit val universe = new DefaultUniverse

  /** Create the earth based frame of a generic template, with the earth having the state <0,0,0,0,0,0> (R,V) */
	val earthFrame = new BodyCentered {
    def centerBody = EarthConstants.bodyCenter
  }

  // Time after the three kicks and an additional orbit
  var tf: Epoch = null

  try {
    /* Compute the trajectory of the AEHF satellite for the first three kicks by the LAE engine */
    logger.info("Creating the quickstart trajectory")
    val trajectory = getTrajectory

    /* Now you can simply evaluate the trajectory, and plot the results using an external app */
    val samples = 1000.0
    val ephemerisFile = Services.newFile("quickstart.csv")
    val writer = new CsvWriter(ephemerisFile, Array[String]("t", "rx", "ry", "rz", "vx", "vy", "vz"))
    val t0 = universe.J2000_EPOCH

    logger.info("Saving ephemeris to file {}", ephemerisFile)

    val tFinal = tf.relativeTo(t0, Time.second)
    val states = for ( t <- 0.0 until tFinal by (tFinal / samples) ) yield {
      val time  = t0.add(t, Time.second)
      val state = trajectory(time).toPosVel

      logger.debug("At jd={} the state is: {}", time, state)
      writer.write(t, state.position.x, state.position.y, state.position.z,
                      state.velocity.x, state.velocity.y, state.velocity.z)
      state
    }

    logger.info("Plotting the ephemeris")
    val x = Array.ofDim[Double](states.size)
    val y = Array.ofDim[Double](states.size)
    states.zipWithIndex.map( entry => {
      val state = entry._1
      val index = entry._2

      val n = Vector3D.K
      val r = state.position
      val projected = r - (n * (r dot n))
      x(index) = projected.x
      y(index) = projected.y
    } )

    Services.newPlot().addData(x, y).makeFrame()
  } catch {
    case e: Throwable => logger.error("Unexpected exception when trying to solve the quickstart example: ", e)
  }

	def getTrajectory = {
		// Original orbit
		var Rp = EarthConstants.radiusMean + 190E3
    var Ra = 50000E3
    var k = new Keplerian((Ra + Rp) / 2, eccentricity(Rp, Ra), 0, 0, 0, 0, Some(earthFrame) )

		// The satellite that will perform the specific maneuver
		var satellite = new Satellite(k)

		/* This is the trajectory that will store the different trajectory segments */
		/* You can add trajectory and a time from where this trajectory should be evaluated */
		val trajectory = new CompositeTrajectory

		/* First leg of the trajectory, the orbit as it was injected by the atlas launcher */
		/* Without any intervention, it would keep this orbit */
		val t0 = universe.J2000_EPOCH
		trajectory.trajectories.put(t0, new KeplerTrajectory(t0, k))

		/* Add first kick to the satellite */
		/* Compute dV for LAE (main engine) */
		/* Current speed in apogee */
		val Va = sqrt( visViva(earthFrame.centerBody.getMu(), Ra, k.semiMajorAxis ))
		/* Raise the perigee to this value after 3 kicks */
		Rp = EarthConstants.radiusMean + 19000E3
		/* Total dV for the 3 kicks */
		val dV = sqrt( visViva(earthFrame.centerBody.getMu(), Ra, (Ra + Rp) / 2)) - Va

		/* Time when we reach the kick location (after 1.5 periods */
		var t = t0.add((3.0 / 2.0) * k.quantities.period, Time.second)
		satellite = new Satellite(trajectory(t))

		/* Make the LAE engine */
		val LAE = new ImpulsiveShot(satellite)
		/* Add the first kick of the LAE */
		var state = LAE.kick(dV / 3, satellite.getHydrazineLAE)
		/* Add the next leg to the trajectory */
		k = Keplerian(state)
		trajectory.trajectories.put(t, new KeplerTrajectory(t, k))

		/* Add kick 2 */
		/* The time of the 2nd kick is the time of the first kick plus one orbit */
		t = t.add(k.quantities.period, Time.second)
		/* Same procedure */
		state = LAE.kick(dV / 3, satellite.getHydrazineLAE)
		k = Keplerian(state)
		trajectory.trajectories.put(t, new KeplerTrajectory(t, k))

		/* And the third kick */
		t = t.add(k.quantities.period, Time.second)
		state = LAE.kick(dV / 3, satellite.getHydrazineLAE)
		k = Keplerian(state)
		trajectory.trajectories.put(t, new KeplerTrajectory(t, k))

    // Time after the three kicks and an additional orbit
		tf = t.add(k.quantities.period, Time.second)

		/* Return the composite trajectory */
		trajectory
	}

}