/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
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

import java.io.InputStreamReader
import java.math.BigInteger
import java.nio.ByteOrder
import java.nio.file.{Files, Paths}

import be.angelcorp.celest.data._
import be.angelcorp.celest.ephemeris.jplEphemeris
import be.angelcorp.celest.resources.{PathResource, ResourceDescription, Resources}
import be.angelcorp.celest.universe.DefaultUniverse
import be.angelcorp.celest.util.MsvcX86Alignment
import com.google.common.hash.Hashing
import org.scalatest.{ParallelTestExecution, FlatSpec, Matchers}

import scala.io.Source
import scala.util.{Failure, Success}


/**
 * Check the JPL epehemeris utilities.
 *
 * This is done using the DE405 ephemeris, with a subset between 1980 and 2040
 */
class TestJplEphemeris extends FlatSpec with Matchers with ParallelTestExecution {

  implicit val universe = new DefaultUniverse

  // The test data files
  lazy val de405Binary = PathResource( { Resources.find( ResourceDescription("be.angelcorp.celest.test.ephemeris", "DE405-1980-2020", "20131119", "bin") ) match {
    case Success( res ) => res
    case Failure( ex  ) => fail( "Could not retrieve binary de405 date file", ex )
  } } )
  lazy val de405AsciiHeader = Resources.findArchive( ResourceDescription("gov.nasa.jpl.ssd.pub.eph.planets.ascii", "de405", "20070705", "zip") ).flatMap( _.findEntry( "header.405" ) ) match {
    case Success( res ) => res
    case Failure( ex  ) => fail( "Could not retrieve de405 test header file", ex )
  }
  lazy val de405AsciiData = Seq("ascp1980.405", "ascp2000.405", "ascp2020.405").map(name => {
    Resources.findArchive( ResourceDescription("gov.nasa.jpl.ssd.pub.eph.planets.ascii", "de405", "20070705", "zip") ).flatMap( _.findEntry( name ) )  match {
      case Success( res ) => res
      case Failure( ex  ) => fail( "Could not retrieve de405 ascii def file " + name, ex )
    }
  })
  lazy val de405Testpo = Resources.findArchive( ResourceDescription("gov.nasa.jpl.ssd.pub.eph.planets.ascii", "de405", "20070705", "zip") ).flatMap( _.findEntry( "testpo.405" ) ) match {
    case Success( res ) => res
    case Failure( ex  ) => fail( "Could not retrieve de405 expected test results file", ex )
  }

  def testEphemeris(ephemeris: JplEphemeris[_], testFile: Source) {
    jplEphemeris.test(ephemeris, testFile).drop(1).map(entry => {
      val line = entry._1
      val (denum, caldate, jd, targ, cent, coord, value) = entry._2
      val (expectedValue, trueValue) = entry._3

      val threshold = targ match {
        case 14 => Double.PositiveInfinity // Nutations
        case 15 => Double.PositiveInfinity // Libration
        case _ => 1.5E-12 // AU or AU/day
      }
      val delta = math.abs(trueValue - expectedValue)

      // Only test dates between 1980 and 2040
      if (2444209 < jd && jd < 2466185 && (delta.isNaN || delta >= threshold))
        fail(s"Ephemeris is out of bounds, expected $expectedValue, but was $trueValue (delta $delta >= threshold $threshold) for: \n $line")
    })
  }

  "BinaryEphemeris" should "pass the testpo.405 test" in {
    val ephemeris = jplEphemeris.fromBinary(de405Binary.path, 405)
    testEphemeris(ephemeris, de405Testpo.openSource())
  }

  it should "generate the correct binary ephemeris" in {
    val ephemeris = jplEphemeris.fromBinary(de405Binary.path, 405)

    val result = Files.createTempFile("testephemeris", ".bin")
    result.toFile.deleteOnExit()
    jplEphemeris.toBinary(ephemeris, result, ByteOrder.LITTLE_ENDIAN, MsvcX86Alignment.instance)

    val hash = com.google.common.io.Files.hash(result.toFile, Hashing.sha1())
    val hashString = bytesToHexString(hash.asBytes())

    // Note this is not exactly equal the result as if the ascii was parsed and converted
    // The only difference is that in record 1, the range of the ephemeris has been autodetected (shruck to what records exist), and is no longer the full range of DE 405
    hashString should be("69b885966ed49586a4d7b8eb024ff3d3bcddf8a5")
  }

  "AsciiEphemeris" should "pass the testpo.405 test" in {
    val header = de405AsciiHeader.openReader()
    val dataFiles = de405AsciiData.map( _.openReader() )

    val ephemeris = jplEphemeris.fromAscii(header, dataFiles)
    testEphemeris(ephemeris, de405Testpo.openSource())
  }

  it should "generate the correct binary ephemeris" in {
    val headerFile = de405AsciiHeader.openReader()
    val dataFiles = de405AsciiData.map( _.openReader() )
    val ephemeris = jplEphemeris.fromAscii(headerFile, dataFiles)

    val result = Files.createTempFile("testephemeris", ".bin")
    result.toFile.deleteOnExit()
    jplEphemeris.toBinary(ephemeris, result, ByteOrder.LITTLE_ENDIAN, MsvcX86Alignment.instance)

    val hash = com.google.common.io.Files.hash(result.toFile, Hashing.sha1())
    val hashString = bytesToHexString(hash.asBytes())

    hashString should be("d84a034385836bf150e9646a93392db4f2da2697")
  }

  "JPL ascii ephemeris" should "be convertable into binary form" in {
    val headerFile = de405AsciiHeader.openReader()
    val dataFiles = de405AsciiData.map( _.openReader() )

    val result = Files.createTempFile("testephemeris", ".bin")
    result.toFile.deleteOnExit()
    jplEphemeris.asciiReader2binary(headerFile, dataFiles, result, ByteOrder.LITTLE_ENDIAN, MsvcX86Alignment.instance)

    val hash = com.google.common.io.Files.hash(result.toFile, Hashing.sha1())
    val hashString = bytesToHexString(hash.asBytes())

    hashString should be("d84a034385836bf150e9646a93392db4f2da2697")
  }

  private def bytesToHexString(bytes: Array[Byte]) = {
    val hexString = new BigInteger(1, bytes).toString(16)
    if (hexString.length() % 2 == 0) hexString else "0" + hexString
  }

}
