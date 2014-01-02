package be.angelcorp.celest.state

import be.angelcorp.celest.frameGraph.ReferenceSystem


trait Orbit[F <: ReferenceSystem] {

  /**
   * Defines the frame in which this orbit has been defined.
   *
   * @return The orbit reference frame.
   */
  def frame: F

  /**
   * Converts this orbit into an equivalent position/velocity (Cartesian) one.
   *
   * @return Cartesian equivalent state vector.
   */
  def toPosVel: PosVel[F]

}
