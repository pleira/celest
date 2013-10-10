package be.angelcorp.libs.celest.state

import be.angelcorp.libs.celest.frameGraph.ReferenceFrame


trait Orbit {

  /**
   * Defines the frame in which this orbit has been defined.
   *
   * @return The orbit reference frame.
   */
  def frame: Option[ReferenceFrame]

  /**
   * Converts this orbit into an equivalent position/velocity (Cartesian) one.
   *
   * @return Cartesian equivalent state vector.
   */
  def toPosVel: PosVel

}
