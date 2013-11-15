/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *        http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package be.angelcorp.libs.celest.ephemeris.jplEphemeris

import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import java.nio.file.{Files, Paths, Path}
import org.junit.runner.RunWith
import be.angelcorp.libs.celest.universe.DefaultUniverse
import be.angelcorp.libs.celest.ephemeris.jplEphemeris
import be.angelcorp.libs.celest.data._
import be.angelcorp.libs.celest.util.MsvcX86Alignment
import java.nio.ByteOrder
import com.google.common.hash.Hashing
import java.math.BigInteger


/**
 * Check the JPL epehemeris utilities.
 *
 * This is done using the DE405 ephemeris, with a subset between 1980 and 2040
 */
@RunWith(classOf[JUnitRunner])
class TestJplEphemeris extends FlatSpec with ShouldMatchers {

  implicit val universe = new DefaultUniverse

  def testEphemeris(ephemeris: JplEphemeris, testFile: Path) {
    jplEphemeris.test(ephemeris, testFile).drop(1).map( entry => {
      val line = entry._1
      val (denum, caldate, jd, targ, cent, coord, value) = entry._2
      val (expectedValue, trueValue) = entry._3

      val threshold = targ match {
        case 14 => Double.PositiveInfinity // Nutations
        case 15 => Double.PositiveInfinity // Libration
        case _  => 1.5E-12 // AU or AU/day
      }
      val delta = math.abs(trueValue - expectedValue)

      // Only test dates between 1980 and 2040
      if(2444209 < jd && jd < 2466185 && (delta.isNaN || delta >= threshold))
        fail( s"Ephemeris is out of bounds, expected $expectedValue, but was $trueValue (delta $delta >= threshold $threshold) for: \n $line" )
    } )
  }
/*
  "AsciiEphemeris" should "pass the testpo.405 test" in {
    val dir = Paths.get("C:/tmp/de405")
    val ephemeris = JplEphemeris.fromAscii(
      dir.resolve("header.405"),
      Files.newDirectoryStream(dir, new Filter[Path] {
        def accept(entry: Path) = entry.getFileName.toString.startsWith("ascp")
      }).iterator().asScala.toSeq
    )
    testEphemeris(ephemeris, dir.resolve("testpo.405"))
  }
*/

  "BinaryEphemeris" should "pass the testpo.405 test" in {
    val ephemerisFile = getDataFile( "test/ephemeris/de405/DE405-1980-2020.bin" ) match {
      case Some(data) => data
      case _ => fail("Could not download the correct ephemeris data file")
    }
    val testFile = getDataFile( "test/ephemeris/de405/testpo.405" ) match {
      case Some(data) => data
      case _ => fail("Could not download the correct ephemeris test file")
    }
    val ephemeris = jplEphemeris.fromBinary( ephemerisFile.toPath, 405 )
    testEphemeris(ephemeris, testFile.toPath)
  }

  it should "generate the correct binary ephemeris" in {
    val ephemerisFile = getDataFile( "test/ephemeris/de405/DE405-1980-2020.bin" ) match {
      case Some(data) => data
      case _ => fail("Could not download the correct ephemeris data file")
    }
    val ephemeris = jplEphemeris.fromBinary( ephemerisFile.toPath, 405 )

    val result = Paths.get("generated.bin")
    jplEphemeris.toBinary(ephemeris, result, ByteOrder.LITTLE_ENDIAN, MsvcX86Alignment.instance)

    val hash = com.google.common.io.Files.hash( result.toFile, Hashing.sha1() )
    val hashString = bytesToHexString(hash.asBytes())

    // Note this is not exactly equal the result as if the ascii was parsed and converted
    // The only difference is that in record 1, the range of the ephemeris has been autodetected (shruck to what records exist), and is no longer the full range of DE 405
    hashString should be ( "69b885966ed49586a4d7b8eb024ff3d3bcddf8a5" )

    Files.delete( result )
  }

  "AsciiEphemeris" should "pass the testpo.405 test" in {
    val headerFile = getDataFile( "test/ephemeris/de405/header.405" ) match {
      case Some(data) => data.toPath
      case _ => fail("Could not download the correct ephemeris data file")
    }
    val dataFiles = Seq("ascp1980.405", "ascp2000.405", "ascp2020.405").map( name => {
      getDataFile( "test/ephemeris/de405/" + name ).map( _.toPath)
    } ).flatten
    val testFile = getDataFile( "test/ephemeris/de405/testpo.405" ) match {
      case Some(data) => data
      case _ => fail("Could not download the correct ephemeris test file")
    }
    val ephemeris = jplEphemeris.fromAscii( headerFile, dataFiles )
    testEphemeris(ephemeris, testFile.toPath)
  }

  it should "generate the correct binary ephemeris" in {
    val headerFile = getDataFile( "test/ephemeris/de405/header.405" ) match {
      case Some(data) => data.toPath
      case _ => fail("Could not download the correct ephemeris data file")
    }
    val dataFiles = Seq("ascp1980.405", "ascp2000.405", "ascp2020.405").map( name => {
      getDataFile( "test/ephemeris/de405/" + name ).map( _.toPath)
    } ).flatten
    val ephemeris = jplEphemeris.fromAscii( headerFile, dataFiles )

    val result = Paths.get("generated.bin")
    jplEphemeris.toBinary(ephemeris, result, ByteOrder.LITTLE_ENDIAN, MsvcX86Alignment.instance)

    val hash = com.google.common.io.Files.hash( result.toFile, Hashing.sha1() )
    val hashString = bytesToHexString(hash.asBytes())

    hashString should be ( "d84a034385836bf150e9646a93392db4f2da2697" )

    Files.delete( result )
  }

  "JPL ascii ephemeris" should "be convertable into binary form" in {
    val headerFile = getDataFile( "test/ephemeris/de405/header.405" ) match {
      case Some(data) => data.toPath
      case _ => fail("Could not download the correct ephemeris data file")
    }
    val dataFiles = Seq("ascp1980.405", "ascp2000.405", "ascp2020.405").map( name => {
      getDataFile( "test/ephemeris/de405/" + name ).map( _.toPath)
    } ).flatten

    val result = Paths.get("generated.bin")
    jplEphemeris.ascii2binary(headerFile, dataFiles, result, ByteOrder.LITTLE_ENDIAN, MsvcX86Alignment.instance)

    val hash = com.google.common.io.Files.hash( result.toFile, Hashing.sha1() )
    val hashString = bytesToHexString(hash.asBytes())

    hashString should be ( "d84a034385836bf150e9646a93392db4f2da2697" )

    Files.delete( result )
  }

  private def bytesToHexString(bytes: Array[Byte]) = {
    val hexString = new BigInteger(1, bytes).toString(16)
    if (hexString.length() % 2 == 0) hexString else "0" + hexString
  }

}
