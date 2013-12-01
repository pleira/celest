package be.angelcorp.celest.time.timeStandard

import be.angelcorp.celest.time.Epoch

trait TimeStandard {

  /**
   * Returns the number of seconds to add to TT to get this timeStandard, so;
   *
   * <pre>
   * offsetFromTT(jd_tt) = this - TT
   * T<sub>this</sub>  = T<sub>TT</sub>  + this.offsetFromTT(JD<sub>TAI</sub>)
   * JD<sub>this</sub> = JD<sub>TT</sub> + Time.convert(this.offsetFromTT(JD<sub>TT</sub>), Time.second, Time.day)
   * </pre>
   *
   * @return The number of seconds between this time standard and TAI.
   */
  def offsetFromTT(JD_tt: Epoch): Double

  /**
   * Returns the number of seconds to add to this time standard to get TT, so;
   *
   * <pre>
   * offsetToTT(jd_this) = TT - this
   * T<sub>TT</sub>  = T<sub>this</sub>  + this.offsetToTT(JD<sub>this</sub>)
   * JD<sub>TT</sub> = JD<sub>this</sub> + Time.convert(this.offsetToTT(JD<sub>this</sub>), Time.second, Time.day)
   * </pre>
   *
   * @return The number of seconds between this time standard and TAI.
   */
  def offsetToTT(JD_this: Epoch): Double

}
