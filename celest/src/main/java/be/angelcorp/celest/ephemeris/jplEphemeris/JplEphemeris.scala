package be.angelcorp.celest.ephemeris.jplEphemeris

import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.time.{TimeRange, Epoch}
import be.angelcorp.celest.state.PosVel
import be.angelcorp.celest.universe.Universe
import be.angelcorp.celest.time.JulianDate
import be.angelcorp.celest.time.timeStandard.TimeStandards.TDB
import be.angelcorp.celest.body.Body
import be.angelcorp.celest.physics.Units
import be.angelcorp.celest.frameGraph.ReferenceSystem

trait JplEphemeris[F <: ReferenceSystem] {

  def frame: F

  def records: Iterator[DataRecord]

  def getRecord(index: Int): DataRecord

  def getRecord(epoch: Epoch): DataRecord = getRecord(epoch2index(epoch))

  def metadata: Metadata

  def epoch2index(time: Epoch) = {
    // Index of the record that contains the epoch
    //floor( time.relativeTo(metadata.range.start) / metadata.range.step ).toInt
    math.ceil(time.relativeTo(metadata.range.start) / metadata.range.step).toInt - 1
  }

  /**
   * This function computes the Lunar librations in the form of three Euler angles, and their time derivatives.
   *
   * @param epoch Epoch for which position is desired (Julian Date).
   * @return Libration angles for the Moon {l [rad], lDot[rad/s]}.
   */
  def interpolateLibration(epoch: Epoch) = {
    val (l, lDot) = interpolate(epoch, 12)
    (Vec3(l), Vec3(lDot))
  }

  /**
   * This function computes the Earth nutation angles ψ (nutation in longitude) and ε (nutation in obliquity).
   * Also their time derivatives are returned.
   *
   * @param epoch Epoch for which position is desired (Julian Date).
   * @return Nutation angles {ψ, ε, d(ψ), d(ε)}.
   */
  def interpolateNutation(epoch: Epoch) = {
    val (nutation, nutationDerivatives) = interpolate(epoch, 11, 2)
    (nutation(0), nutation(1), nutationDerivatives(0), nutationDerivatives(1))
  }

  /**
   * This function computes position and velocity vectors for a selected planetary body.
   *
   * The results are all with respect to the solar barycenter, except for the moon, which is with respect to the earth-moon barycenter.
   *
   * Also note that the ephemeris for Earth, is actually that of the earth-moon barycenter.
   *
   * @param epoch Epoch for which position is desired (Julian Date)
   * @param body  Solar system body for which position is desired
   * @return State of the body at the time.
   */
  def interpolateState(epoch: Epoch, body: JDEBody): PosVel[F] =
    if (body == Earth()) {
      val emb = interpolateState(epoch, EMB())
      val moon = interpolateState(epoch, MoonGEO())

      // Translate from the Earth-Moon barycenter to Earth
      val s = 1.0 + metadata.EMRAT
      val p = emb.position - moon.position / s
      val v = emb.velocity - moon.velocity / s

      new PosVel(p, v, frame)
    } else if (body == Moon()) {
      val emb = interpolateState(epoch, EMB())
      val moon = interpolateState(epoch, MoonGEO())

      /* Translate from Geocentered to Solar System barycentric */
      val s = 1.0 + metadata.EMRAT
      val p = emb.position + moon.position * (1.0 - 1.0 / s)
      val v = emb.velocity + moon.velocity * (1.0 - 1.0 / s)
      new PosVel(p, v, frame)
    } else if (body == SSB()) {
      PosVel(0, 0, 0, 0, 0, 0, frame)
    } else {
      val (p, v) = interpolate(epoch, body) // Results in [km] and [km/s]
      new PosVel(Vec3(p) * 1000.0, Vec3(v) * 1000.0, frame)
    }

  /**
   * Compute the Chebeyshev polynomials and interpolate them to the specified epoch.
   *
   * Can be used to extract states, librations, and nutations.
   *
   * - Planetary position/state: [km] and [km/s]
   * - Nutation: [rad]
   * - Libration: [rad]
   *
   * @param epoch       Epoch at which to evaluate the ephemeris.
   * @param id          Id of what to extract, either numeric or use JDEBody.id
   * @param components  Number of componenets to extract.
   * @return Interpolated Chebeyshev values and their first derivatives in two seperate arrays (of size components).
   */
  def interpolate(epoch: Epoch, id: Int, components: Int = 3) = {
    // Fetch the required data record
    val record = getRecord(epoch)
    // Number of coefficients per variable
    val N = metadata.coeffPtr(id).nrCoefficients
    // Number of granules in current record
    val G = metadata.coeffPtr(id).nrGranules

    // Compute the normalized time, and the Chebeyshev coefficients
    val (tc, a) = record.coefficients(epoch, id, components)

    // Compute interpolating polynomials and the resulting interpolated position & velocity
    // With some ugly 'high performance' while loops

    val p = Array.ofDim[Double](components)
    val v = Array.ofDim[Double](components)
    val (seriesT, seriesTdot) = chebyshevTTdotSeries(N)(tc) // Chebeyshev T(t) and dT(t)/dt series
    var i = 0
    while (i < components) {
      // Compute the position component value
      var P_Sum = 0.0
      var j = N - 1
      while (j > -1) {
        P_Sum = P_Sum + a(j + i * N) * seriesT(j)
        j = j - 1
      }

      // Compute the velocity component value
      var V_Sum = 0.0
      j = N - 1
      while (j > 0) {
        V_Sum = V_Sum + a(j + i * N) * seriesTdot(j)
        j -= 1
      }

      p(i) = P_Sum
      v(i) = V_Sum * 2.0 * G.toDouble / (record.span * 86400.0)
      i += 1
    }
    (p, v)
  }

  /**
   * Retrieve an IEphemeris object that creates the ephemeris for a specified celestial body using this JPL ephemeris.
   *
   * @param body Body for which to generate the ephemeris.
   */
  def body(body: JDEBody) = new Body[F] {
    val μ: Double = {
      val gm = body match {
        case Mercury() => metadata.tags.getOrElse("GM1", throw JplConstantException("GM1"))
        case Venus() => metadata.tags.getOrElse("GM2", throw JplConstantException("GM2"))
        case EMB() => metadata.tags.getOrElse("GMB", throw JplConstantException("GMB"))
        case Earth() => metadata.EMRAT * metadata.tags.getOrElse("GMB", throw JplConstantException("GMB")) / (1.0 + metadata.EMRAT)
        case Mars() => metadata.tags.getOrElse("GM4", throw JplConstantException("GM4"))
        case Jupiter() => metadata.tags.getOrElse("GM5", throw JplConstantException("GM5"))
        case Saturn() => metadata.tags.getOrElse("GM6", throw JplConstantException("GM6"))
        case Uranus() => metadata.tags.getOrElse("GM7", throw JplConstantException("GM7"))
        case Neptune() => metadata.tags.getOrElse("GM8", throw JplConstantException("GM8"))
        case Pluto() => metadata.tags.getOrElse("GM9", throw JplConstantException("GM9"))
        case MoonGEO() => metadata.tags.getOrElse("GMB", throw JplConstantException("GMB")) / (1.0 + metadata.EMRAT)
        case Moon() => metadata.tags.getOrElse("GMB", throw JplConstantException("GMB")) / (1.0 + metadata.EMRAT)
        case Sun() => metadata.tags.getOrElse("GMS", throw JplConstantException("GM1"))
        case SSB() => List("GMS", "GM1", "GM2", "GMB", "GM4", "GM5", "GM6", "GM7", "GM8", "GM9").map(tag => {
          metadata.tags.getOrElse(tag, throw JplConstantException(tag))
        }).sum
      }
      gm * math.pow(metadata.AU * 1E3, 3) / math.pow(Units.julianDay, 2)
    }

    def orbit(epoch: Epoch): PosVel[F] = interpolateState(epoch, body)
  }

  /**
   * Chebyshev polynomial of the first kind T(t).
   *
   * @param maxDegree Exclusive maximum degree of the series expansion (must be >= 3)
   * @param t Normalized time [0, 1]
   */
  private def chebyshevTSeries(maxDegree: Int)(t: Double): Array[Double] = {
    val T = Array.ofDim[Double](maxDegree)
    T(0) = 1
    T(1) = t
    T(2) = 2 * t * t - 1
    var degree = 4
    while (degree < maxDegree) {
      T(degree - 1) = 2 * t * T(degree - 2) - T(degree - 3)
      degree += 1
    }
    T
  }

  /**
   * Computes the value of two series, the Chebyshev polynomial of the first T(t) and its
   * first derivative with respect to normalized time d(T(t))/dt.
   *
   * @param maxDegree Exclusive maximum degree of the series expansion (must be >= 3)
   * @param t Normalized time [0, 1]
   */
  private def chebyshevTTdotSeries(maxDegree: Int)(t: Double): (Array[Double], Array[Double]) = {
    val T = Array.ofDim[Double](maxDegree)
    val Tdot = Array.ofDim[Double](maxDegree)

    T(0) = 1
    T(1) = t
    T(2) = 2 * t * t - 1
    Tdot(0) = 0.0
    Tdot(1) = 1.0
    Tdot(2) = 4.0 * t

    var degree = 4
    while (degree < maxDegree) {
      T(degree - 1) = 2.0 * t * T(degree - 2) - T(degree - 3)
      Tdot(degree - 1) = 2.0 * t * Tdot(degree - 2) - Tdot(degree - 3) + 2.0 * T(degree - 2)
      degree += 1
    }
    (T, Tdot)
  }

  /**
   * Chebyshev polynomial of the second kind U(t).
   *
   * @param maxDegree Maximum degree of the series expansion (must be >= 3)
   * @param t Normalized time [0, 1]
   */
  private def chebyshevUSeries(maxDegree: Int)(t: Double): Array[Double] = {
    val U = Array.ofDim[Double](maxDegree)
    U(0) = 1
    U(1) = 2 * t
    U(2) = 4 * t * t - 1
    for (degree <- 4 to maxDegree)
      U(degree - 1) = 2 * t * U(degree - 2) - U(degree - 3)
    U
  }

}

class DataRecord(val metadata: Metadata, val data: Array[Double])(implicit universe: Universe) {
  val begin = JulianDate(data(0), TDB)
  // Beginning time of the record
  val end = JulianDate(data(1), TDB)
  // Ending    time of the record
  val span = end relativeTo begin // Time step in this record [days]

  def coefficients(time: Epoch, target: Int, components: Int = 3) = {
    // Entry point of the data
    val c = metadata.coeffPtr(target).entryPoint - 1
    // Number of coefficients per variable
    val n = metadata.coeffPtr(target).nrCoefficients
    // Number of granules in current record
    val g = metadata.coeffPtr(target).nrGranules

    // tc  = normalized time of the epoch in the Tchebeyshev interval
    // pos = Offset of the coefficients in the data buffer
    val (tc, pos) =
    // Compute normalized time, and offset in the data with the coefficients.
    // If T_span is covered by a single granule this is easy.
    // If not, the granule that contains the interpolation time is found,
    // and an offset from the array entry point for the ephemeris body is used to load the coefficients.
      if (g == 1) {
        (2.0 * time.relativeTo(begin) / span - 1.0, c)
      } else if (g > 1) {
        // Compute subgranule interval
        val T_sub = span / g.toDouble
        // Index of the sub-granule to use
        val offset = math.max(0, math.ceil(time.relativeTo(begin) / T_sub).toInt - 1)
        // Time of the beginning of the sub-granule
        val T_seg = begin + offset * T_sub
        (2.0 * time.relativeTo(T_seg) / T_sub - 1.0, c + 3 * offset * n)
      } else {
        throw new RuntimeException("Number of granules must be >= 1: check header data.")
      }

    // Return the normalized time, and the sub-array containing the required coefficients
    (tc, data.slice(pos, pos + components * n))
  }

}

/**
 * Information contained in the first record of the JPL ephemeris data file.
 *
 * This contains all the requited metadata information to be able to locate records and
 * compute the states of planetary bodies.
 *
 * @param recordEntries Number of double entries per data record.
 * @param label1        First line of the label description.
 * @param label2        Second line of the label description.
 * @param label3        Third line of the label description.
 * @param tags          A list of tags/constants used in the generation of the ephemeris file.
 * @param range         Time range if the ephemeris (start, stop, and time interval).
 * @param AU            The astronomical unit in [km].
 * @param EMRAT         The Earth/Moon mass ratio.
 * @param coeffPtr      List of entry points for the coefficients of the different bodies.
 * @param headerID      Identifies of the ephemeris type, aka DENUM, eg 405 for DE405.
 */
class Metadata(val recordEntries: Int,
               val label1: String,
               val label2: String,
               val label3: String,
               val tags: Map[String, Double],
               val range: TimeRange,
               val AU: Double,
               val EMRAT: Double,
               val coeffPtr: List[RecordMetadata],
               val headerID: Int) {
  val numConst = tags.size
}

/**
 * Metadata required for locating the coefficients of a single body in a record.
 *
 * @param entryPoint      Entry point of the coefficients in the data array. NOTE STARTS AT 1 !!!
 * @param nrCoefficients  Total number of required coefficients.
 * @param nrGranules      Number of sub-granuls.
 */
class RecordMetadata(val entryPoint: Int, val nrCoefficients: Int, val nrGranules: Int)

/**
 * Body for which the state can be computed used the JPL ephemeris.
 * @param id Index of the body in the metadata coeffPtr List
 */
sealed abstract class JDEBody(val id: Int)

/** Coordinates of Mercury relative to the solar barycenter */
case class Mercury() extends JDEBody(0)

/** Coordinates of Venus relative to the solar barycenter */
case class Venus() extends JDEBody(1)

/** Coordinates of the Earth-Moon relative to the solar barycenter */
case class EMB() extends JDEBody(2)

/** Coordinates of Earth relative to the solar barycenter */
case class Earth() extends JDEBody(-1)

/** Coordinates of Mars relative to the solar barycenter */
case class Mars() extends JDEBody(3)

/** Coordinates of Jupiter relative to the solar barycenter */
case class Jupiter() extends JDEBody(4)

/** Coordinates of Saturn relative to the solar barycenter */
case class Saturn() extends JDEBody(5)

/** Coordinates of Uranus relative to the solar barycenter */
case class Uranus() extends JDEBody(6)

/** Coordinates of Neptune relative to the solar barycenter */
case class Neptune() extends JDEBody(7)

/** Coordinates of Pluto relative to the solar barycenter */
case class Pluto() extends JDEBody(8)

/** Coordinates of the Moon relative to the Earth (geocenter) */
case class MoonGEO() extends JDEBody(9)

/** Coordinates of the Moon relative to the solar barycenter */
case class Moon() extends JDEBody(-1)

/** Coordinates of the Sun relative to the solar barycenter */
case class Sun() extends JDEBody(10)

/** Solar barycenter */
case class SSB() extends JDEBody(-1)

object JDEBody {
  implicit def body2int(body: JDEBody): Int = body.id

  implicit def int2body(body: Int): JDEBody = body match {
    case 0 => Mercury()
    case 1 => Venus()
    case 2 => EMB()
    case 3 => Mars()
    case 4 => Jupiter()
    case 5 => Saturn()
    case 6 => Uranus()
    case 7 => Neptune()
    case 8 => Pluto()
    case 9 => Moon()
    case 10 => Sun()
    case id => throw new java.lang.IllegalArgumentException(s"JPL DE body ID out of bounds, must be [0, 10], was: $id")
  }
}
