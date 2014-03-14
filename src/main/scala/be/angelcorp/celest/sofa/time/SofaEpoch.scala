package be.angelcorp.celest.sofa.time

import java.util.Date
import scala.math._
import org.bridj.Pointer
import be.angelcorp.celest.time.Epoch
import be.angelcorp.sofa.SofaLibrary
import be.angelcorp.celest.time.timeStandard.{UTCTime, TimeStandard}
import be.angelcorp.celest.time.timeStandard.TimeStandards._
import be.angelcorp.celest.universe.Universe

/**
 * An epoch based on the Sofa epoch format.
 *
 * The Julian Date is apportioned in any convenient way between the arguments dj1 and dj2.
 * For example, JD=2450123.7 could be expressed in any of these ways, among others:
 *
 * <pre>
 *     dj1             dj2
 * 2450123.7           0.0       (JD method)
 * 2451545.0       -1421.3       (J2000 method)
 * 2400000.5       50123.2       (MJD method)
 * 2450123.5           0.2       (date & time method)
 * </pre>
 *
 * Internally most operations are performed via the IAU SOFA routines.
 *
 * @param dj1 First  part of the 2-part JD.
 * @param dj2 Second part of the 2-part JD.
 * @param timeStandard The timestandard accompanying the julian date.
 */
class SofaEpoch(val dj1: Double, val dj2: Double, val timeStandard: TimeStandard)(implicit val tt: TimeStandard) extends Epoch {

  override def add(dt: Double) = {
    val (t1, t2) = if (dt < 1) (max(dj1, dj2), min(dj1, dj2) + dt) else (max(dj1, dj2) + dt, min(dj1, dj2))
    new SofaEpoch(t1, t2, timeStandard)
  }

  override def inTimeStandard(timeStandard: TimeStandard) = {
    if (this.timeStandard.equals(timeStandard))
      this
    else {
      /* First convert this to TT form */
      val offset = this.timeStandard.offsetToTT(this)
      val this_tt = new SofaEpoch(max(dj1, dj2), min(dj1, dj2) + offset / 86400.0, tt)

      /* Then convert the TT jd form to the requested type */
      val offset2 = timeStandard.offsetFromTT(this_tt)
      new SofaEpoch(this_tt.dj1, this_tt.dj2 + offset2 / 86400.0, timeStandard)
    }
  }

  override def jd = dj1 + dj2

  override def date: Date = {
    val year     = Pointer.allocateInt()
    val month    = Pointer.allocateInt()
    val date     = Pointer.allocateInt()
    val fraction = Pointer.allocateDouble()

    SofaLibrary.iauJd2cal(dj1, dj2, year, month, date, fraction)

    var temp = fraction.get() * 24
    val hr   = temp.toInt
    temp = (temp - hr) * 60.0
    val minute = temp.toInt
    val sec = (temp - minute) * 60.0

    new Date(year.get(), month.get(), date.get(), hr, minute, sec.toInt)
  }

  override def relativeTo(epoch: Epoch): Double = epoch match {
    case sepoch: SofaEpoch =>
      val t1 = math.max(dj1, dj2) - math.max(sepoch.dj1, sepoch.dj2)
      val t2 = math.min(dj1, dj2) - math.min(sepoch.dj1, sepoch.dj2)
      t1 + t2
    case _ =>
      val t1 = math.max(dj1, dj2) - epoch.jd
      val t2 = math.min(dj1, dj2)
      t1 + t2
  }

  override def compareTo(epoch: Epoch): Int =
    this.jd.compareTo(epoch.jd)

  override def toString = {
    val iymdf = Pointer.allocateInts(4)
    SofaLibrary.iauJdcalf(8, dj1, dj2, iymdf)
    s"${iymdf(0)}/${iymdf(1)}/${iymdf(2)} ${iymdf(3)}"
  }

  /** Get this epoch as a Besselian Epoch. */
  def besselianEpoch = SofaLibrary.iauEpb(dj1, dj2)

  /** Get this epoch as a Julian Epoch. */
  def julianEpoch = SofaLibrary.iauEpj(dj1, dj2)

  /** Get a SofaEpochBuilder, that is initialized with the variables of this Epoch. */
  def toBuilder = SofaEpoch.builder(dj1, dj2, timeStandard)

}

object SofaEpoch {

  /**
   * A SofaEpochBuilder is a builder for a SofaEpoch. It allows for the modification of it's timestandard, and stores
   * it's 2-part Julian date as c-compatible pointers. This allows it to be passed to Sofa functions, whereafter it can
   * be converted into an immutable `SofaEpoch` via the `result` method.
   */
  class SofaEpochBuilder(var timeStandard: TimeStandard)(implicit val tt: TimeStandard) {
    def this()(implicit tt: TimeStandard) = this( tt )( tt )
    val t1 = Pointer.allocateDouble()
    val t2 = Pointer.allocateDouble()
    def result: SofaEpoch = new SofaEpoch( t1.get, t2.get, timeStandard )
  }

  /** Creates a SofaEpochBuilder, using TT as timestandard, and an uninitialized 2-part Julian date. */
  def builder(implicit tt: TimeStandard): SofaEpochBuilder =
    new SofaEpochBuilder()

  /**
   * Creates a TT based SofaEpochBuilder, initialized with the given default values
   * @param t1 First part of the 2-part Julian date, by default 2451545.0 (J2000.0 epoch in TT) [days]
   * @param t2 Second part of the 2-part Julian date, by default 0.0 [days]
   */
  def builder(t1: Double = 2451545.0, t2: Double = 0.0)(implicit tt: TimeStandard): SofaEpochBuilder =
    builder(t1, t2, tt)

  /**
   * Creates a SofaEpochBuilder, initialized with the given default values
   * @param t1 First part of the 2-part Julian date, by default 2451545.0 (J2000.0 epoch in TT) [days]
   * @param t2 Second part of the 2-part Julian date, by default 0.0 [days]
   * @param timestandard Timestandard for the resulting Epoch, by default TT.
   */
  def builder(t1: Double, t2: Double, timestandard: TimeStandard)(implicit tt: TimeStandard): SofaEpochBuilder = {
    val builder = new SofaEpochBuilder(timestandard)
    builder.t1.set(t1)
    builder.t2.set(t2)
    builder
  }

  /** Create a SofaEpoch from a given TT based calender date */
  def apply( year: Int, month: Int = 1, date: Int = 1, hour: Int = 1, minute: Int = 1, seconds: Double = 0.0)(implicit tt: TimeStandard) : SofaEpoch =
    apply( year, month, date, hour, minute, seconds, tt )

  /** Create a SofaEpoch from a given calender date */
  def apply( year: Int, month: Int, date: Int, hour: Int, minute: Int, seconds: Double, timeStandard: TimeStandard)(implicit tt: TimeStandard) : SofaEpoch = {
    val isUTC = timeStandard.isInstanceOf[UTCTime]
    val label = Pointer.pointerToCString( if (isUTC) "UTC" else "" )

    val t1 = Pointer.allocateDouble()
    val t2 = Pointer.allocateDouble()
    // TODO: Process return code
    SofaLibrary.iauDtf2d(label, year, month, date, hour, minute, seconds, t1, t2)
    new SofaEpoch(t1.get, t2.get, timeStandard)
  }

  /** Create a SofaEpoch from a plain TT based Julian date. */
  def apply( jd: Double)(implicit tt: TimeStandard) : SofaEpoch =
    apply(jd, tt)

  /** Create a SofaEpoch from a plain Julian date. */
  def apply( jd: Double, timeStandard: TimeStandard )(implicit tt: TimeStandard) : SofaEpoch = {
    val t1 = round(jd)
    val t2 = jd - t1
    new SofaEpoch(t1, t2, timeStandard)
  }

  /** Convert an arbitrary to a SofaEpoch */
  def apply( epoch: Epoch )(implicit tt: TimeStandard) : SofaEpoch = epoch match {
    case sofaepoch: SofaEpoch => sofaepoch
    case _ => SofaEpoch( epoch.jd, epoch.timeStandard )
  }

  /**
   * Create a SofaEpoch based on a TT Besselian epoch.
   * @param bep Besselian Epoch in TT (e.g. 1957.3)
   */
  def fromBesselianEpoch( bep: Double )(implicit tt: TimeStandard): SofaEpoch =
    fromBesselianEpoch(bep, tt)

  /**
   * Create a SofaEpoch based on a Besselian epoch.
   * @param bep Besselian Epoch (e.g. 1957.3)
   * @param timeStandard Timestandard of the epoch.
   */
  def fromBesselianEpoch( bep: Double, timeStandard: TimeStandard )(implicit tt: TimeStandard): SofaEpoch = {
    val t1 = Pointer.allocateDouble()
    val t2 = Pointer.allocateDouble()
    SofaLibrary.iauEpb2jd(bep, t1, t2)
    new SofaEpoch(t1.get, t2.get, timeStandard)
  }

  /**
   * Create a SofaEpoch based on a TT Julian epoch.
   * @param jep Julian Epoch in TT (e.g. 1996.8)
   */
  def fromJulianEpoch( jep: Double )(implicit tt: TimeStandard): SofaEpoch =
    fromJulianEpoch(jep, tt)

  /**
   * Create a SofaEpoch based on a Julian epoch.
   * @param jep Julian Epoch (e.g. 1996.8)
   * @param timeStandard Timestandard of the epoch.
   */
  def fromJulianEpoch( jep: Double, timeStandard: TimeStandard )(implicit tt: TimeStandard): SofaEpoch = {
    val t1 = Pointer.allocateDouble()
    val t2 = Pointer.allocateDouble()
    SofaLibrary.iauEpj2jd(jep, t1, t2)
    new SofaEpoch(t1.get, t2.get, timeStandard)
  }

}
