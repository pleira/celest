/**
 * Copyright (C) 2012 Simon Billemont <simon@angelcorp.be>
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

package be.angelcorp.celest.ephemeris.jplEphemeris

import java.io.RandomAccessFile
import java.nio.ByteOrder
import java.nio.channels.FileChannel.MapMode
import com.google.common.cache.{LoadingCache, CacheLoader, CacheBuilder}
import be.angelcorp.celest.universe.Universe
import be.angelcorp.celest.frameGraph.frames.ICRS

class BinaryEphemeris(val metadata: Metadata, val file: RandomAccessFile, val endianness: ByteOrder)(implicit val universe: Universe) extends JplEphemeris[ICRS] {

  val frame = universe.instance[ICRS]

  val recordSize = metadata.recordEntries * 8

  // TODO: Guiceify the builder
  val cache = CacheBuilder.newBuilder().maximumSize(100).build(new CacheLoader[Int, DataRecord] {
    def load(record: Int): DataRecord = {
      // Create the data record
      this.synchronized {
        // Load the data for the data record from a memory mapped file section:
        val data = file.getChannel.map(MapMode.READ_ONLY, recordSize * (record + 2), recordSize).order(endianness)
        // Convert the bytebuffer into an array of doubles
        val dataArray = Array.ofDim[Double](data.limit() / 8)
        data.asDoubleBuffer().get(dataArray)
        // Wrap the binary data in a DataRecord
        new DataRecord(metadata, dataArray)
      }
    }
  }).asInstanceOf[LoadingCache[Int, DataRecord]]

  def getRecord(index: Int) = {
    // Find the datarecord referenced by the record index in the cache map
    cache.get(index)
  }

  def records = {
    val lastRecord = (metadata.range.end.relativeTo(metadata.range.start) / metadata.range.step).toInt
    (0 until lastRecord).iterator.map(record => cache.get(record))
  }

}
