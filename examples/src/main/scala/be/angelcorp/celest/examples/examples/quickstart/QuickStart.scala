/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.celest.examples.examples.quickstart

import java.io.FileWriter

import be.angelcorp.celest.constants.EarthConstants
import be.angelcorp.celest.examples.gui.{CelestExample, Services}
import be.angelcorp.celest.frameGraph.frames.GCRS
import be.angelcorp.celest.kepler._
import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.state.Keplerian
import be.angelcorp.celest.time.Epochs
import be.angelcorp.celest.trajectory.{CompositeTrajectory, KeplerTrajectory}
import be.angelcorp.celest.universe.DefaultUniverse
import org.slf4j.LoggerFactory

import scala.math._

@CelestExample(
  name = "Quickstart example",
  description = "This is an example that uses a variety of tools in Celest to compute a sample scenario for a GTO orbit; raising of the pericenter using impulsive maneuvers.")
class QuickStart {
  val logger = LoggerFactory.getLogger(classOf[QuickStart])

  /** Create the default universe (defines time standards and reference frameGraph) */
  implicit val universe = new DefaultUniverse

  // Get the earth based frame used for our example
  // This is a implementation of the Geccentric Celestial Reference System
  // A frame with Earth in it's center and aligned with the celestial reference frame (approximately equal to the J2000 or EME2000 frame)
  val earthFrame = universe.instance[GCRS]

  try {
    /* Compute the trajectory of the AEHF satellite for the first three kicks by the LAE engine */
    logger.info("Creating the quickstart trajectory")
    val (trajectory, tf) = getTrajectory

    /* Now you can simply evaluate the trajectory, and plot the results using an external app */
    val samples = 1000.0
    val ephemerisFile = new FileWriter(Services.newFile("quickstart.csv"))
    ephemerisFile.write("t, rx, ry, rz, vx, vy, vz")
    val t0 = Epochs.J2000

    logger.info("Saving ephemeris to file {}", ephemerisFile)

    val tFinal = tf.relativeToS(t0)
    val states = for (t <- 0.0 until tFinal by (tFinal / samples)) yield {
      val time = t0.addS(t)
      val state = trajectory(time).toPosVel

      logger.debug(s"At jd=$time the state is: $state")
      ephemerisFile.write(s"$t, ${state.position.x}, ${state.position.y}, ${state.position.z}, ${state.velocity.x}, ${state.velocity.y}, ${state.velocity.z}")
      state
    }

    logger.info("Plotting the ephemeris (TODO)")
    val x = Array.ofDim[Double](states.size)
    val y = Array.ofDim[Double](states.size)
    states.zipWithIndex.map(entry => {
      val state = entry._1
      val index = entry._2

      val n = Vec3.z
      val r = state.position
      val projected = r - (n * (r dot n))
      x(index) = projected.x
      y(index) = projected.y
    })
  } catch {
    case e: Throwable => logger.error("Unexpected exception when trying to solve the quickstart example: ", e)
  }

  def getTrajectory = {
    // Original orbit
    val Rp = EarthConstants.radiusMean + 190E3
    val Ra = 50000E3
    val k = new Keplerian((Ra + Rp) / 2, eccentricity(Rp, Ra), 0.01, 0, 0, 0, earthFrame)

    // The satellite that will perform the specific maneuver
    val satelliteT0 = MySatellite(Epochs.J2000, k)

    /* This is the trajectory that will store the different trajectory segments */
    /* You can add trajectory and a time from where this trajectory should be evaluated */
    val trajectory = new CompositeTrajectory(earthFrame)

    /* First leg of the trajectory, the orbit as it was injected by the atlas launcher */
    /* Without any intervention, it would keep this orbit */
    trajectory.trajectories.put(satelliteT0.epoch, new KeplerTrajectory(satelliteT0.epoch, Keplerian(satelliteT0.state)))

    /* Add first kick to the satellite */
    /* Compute dV for LAE (main engine) */
    /* Current speed in apogee */
    val Va = sqrt(visViva(earthFrame.centerBody.μ, Ra, k.semiMajorAxis))
    /* Raise the perigee to this value after 3 kicks */
    val Rp2 = EarthConstants.radiusMean + 19000E3
    /* Total dV for the 3 kicks */
    val dV = sqrt(visViva(earthFrame.centerBody.μ, Ra, (Ra + Rp2) / 2)) - Va

    /* Time and state when we reach the kick location (after 1.5 periods */
    val beforeManeuver15 = satelliteT0.propagateFor((3.0 / 2.0) * k.quantities.period)
    /* Add the first kick of the LAE */
    val satellite15 = beforeManeuver15.kickLAE(dV / 3)
    /* Add the next leg to the trajectory */
    trajectory.trajectories.put(satellite15.epoch, new KeplerTrajectory(satellite15.epoch, Keplerian(satellite15.state)))

    /* Add kick 2, Same procedure */
    val beforeManeuver25 = satellite15.propagateFor(Keplerian(satellite15.state).quantities.period)
    val satellite25 = beforeManeuver25.kickLAE(dV / 3)
    trajectory.trajectories.put(satellite25.epoch, new KeplerTrajectory(satellite25.epoch, Keplerian(satellite25.state)))

    /* And the third kick */
    val beforeManeuver35 = satellite25.propagateFor(Keplerian(satellite25.state).quantities.period)
    val satellite35 = beforeManeuver35.kickLAE(dV / 3)
    trajectory.trajectories.put(satellite35.epoch, new KeplerTrajectory(satellite35.epoch, Keplerian(satellite35.state)))

    // State after the three kicks and an additional orbit
    val satelliteFinal = satellite35.propagateFor(Keplerian(satellite35.state).quantities.period)

    /* Return the composite trajectory, and the final time */
    (trajectory, satelliteFinal.epoch)
  }

}