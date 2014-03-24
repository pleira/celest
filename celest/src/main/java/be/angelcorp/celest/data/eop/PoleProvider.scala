package be.angelcorp.celest.data.eop

import be.angelcorp.celest.time.Epoch

trait PoleProvider {

  /**
   * Return the pole coordinates on a specified epoch.
   *
   * @param epoch Epoch on which to retrieve the orientation data of the pole.
   * @return (x_p, y_p) in [rad].
   */
  def polarCoordinatesOn(epoch: Epoch): (Double, Double)

}
