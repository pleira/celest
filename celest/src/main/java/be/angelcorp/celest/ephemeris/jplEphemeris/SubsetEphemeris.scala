package be.angelcorp.celest.ephemeris.jplEphemeris

import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.frameGraph.ReferenceSystem

/**
 * Create a subset of an existing ephemeris object.
 *
 * Note that the old ephemeris is used for actually retrieving the data.
 * If you want to unload the old ephemeris, save this ephemeris object to disk (either ascii/binary) and reload it again.
 *
 * @param ephemeris    Backing ephemeris, provides the baseline records and metadata.
 * @param recordOffset Index of the first record to INCLUDE in this ephemeris.
 * @param recordLimit  Index of the first record to EXCLUDE from this ephemeris.
 */
class SubsetEphemeris[F <: ReferenceSystem](val ephemeris: JplEphemeris[F], val recordOffset: Int, val recordLimit: Int) extends JplEphemeris[F] {

  def frame = ephemeris.frame

  def records: Iterator[DataRecord] =
    ephemeris.records.slice(recordOffset, recordOffset)

  val metadata: Metadata = {
    val m = ephemeris.metadata
    val step = m.range.step
    val start = m.range.start + recordOffset * step
    val end = m.range.start + recordLimit * step
    val range = start until end by step
    new Metadata(m.recordEntries, m.label1, m.label2, m.label3, m.tags, range, m.AU, m.EMRAT, m.coeffPtr, m.headerID)
  }

  def getRecord(index: Int): DataRecord =
    ephemeris.getRecord(index)

}

object SubsetEphemeris {

  /**
   * Create a subset of an existing ephemeris object, covering the specified Epoch range.
   *
   * If the specified start epoch is outside the original ephemeris range, the ephemeris starting epoch is used instead. The same applies for the end epoch.
   *
   * @param ephemeris Backing ephemeris, provides the baseline records and metadata.
   * @param start     Initial (inclusive) epoch that the ephemeris should contain.
   * @param end       Final   (inclusive) epoch that the ephemeris should contain.
   */
  def apply[F <: ReferenceSystem](ephemeris: JplEphemeris[F], start: Epoch, end: Epoch) =
    new SubsetEphemeris(
      ephemeris,
      math.max(ephemeris.epoch2index(start), 0),
      math.min(ephemeris.epoch2index(end), ephemeris.epoch2index(ephemeris.metadata.range.end)) + 1
    )

}
