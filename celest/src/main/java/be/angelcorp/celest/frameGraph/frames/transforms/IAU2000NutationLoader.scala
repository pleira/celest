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

package be.angelcorp.celest.frameGraph.frames.transforms

import be.angelcorp.celest.resources.{ResourceDescription, Resources}

import scala.io.Source
import be.angelcorp.celest.universe.Universe

object IAU2000NutationLoader {

  def parseIERS2010(content: Source, longitudeFile: Boolean) = {
    var inFirstSection = true
    val sectionChange = """j\s+=\s+([0-9]*).*""".r

    (for (line <- content.getLines().map(_.trim)) yield line match {
      case line if line.isEmpty => None
      case sectionChange(section) =>
        inFirstSection = section == "0"
        None
      case thisLine if thisLine.head.isDigit =>
        // 1E-6 to convert from μas => arcseconds
        val numbers = thisLine.split( """\s+""").map(_.toDouble)
        val A_i = if (longitudeFile && inFirstSection) numbers(1) * 1E-6 else 0
        val Ap_i = if (longitudeFile && !inFirstSection) numbers(1) * 1E-6 else 0
        val App_i = if (longitudeFile && inFirstSection) numbers(2) * 1E-6 else 0
        val Appp_i = if (longitudeFile && !inFirstSection) numbers(2) * 1E-6 else 0
        val B_i = if (!longitudeFile && inFirstSection) numbers(2) * 1E-6 else 0
        val Bp_i = if (!longitudeFile && !inFirstSection) numbers(2) * 1E-6 else 0
        val Bpp_i = if (!longitudeFile && inFirstSection) numbers(1) * 1E-6 else 0
        val Bppp_i = if (!longitudeFile && !inFirstSection) numbers(1) * 1E-6 else 0
        val l = numbers(3)
        val lp = numbers(4)
        val F = numbers(5)
        val D = numbers(6)
        val Ω = numbers(7)
        val L_Me = numbers(8)
        val L_Ve = numbers(9)
        val L_E = numbers(10)
        val L_Ma = numbers(11)
        val L_J = numbers(12)
        val L_Sa = numbers(13)
        val L_U = numbers(14)
        val L_Ne = numbers(15)
        val p_A = numbers(16)
        Some(new IAU2000NutationEntry(A_i, Ap_i, App_i, Appp_i, B_i, Bp_i, Bpp_i, Bppp_i, l, lp, F, D, Ω, L_Me, L_Ve, L_E, L_Ma, L_J, L_Sa, L_U, L_Ne, p_A))
      case _ => None
    }).flatten.toList
  }

  def parseMHB2000(content: Source): List[IAU2000NutationEntry] = {
    // Remove comment and empty lines
    val datalines = content.getLines().filter(_.headOption match {
      case Some(char) => char != '#'
      case None => false
    })

    // Map the data lines onto a IAU2000NutationEntry
    datalines.map(line => {
      val numbers = line.split( """\s+""").map(_.toDouble)
      // 1E-7 to convert from 0.1 μas => arcseconds
      val A_i = numbers(5) * 1E-7
      val Ap_i = numbers(6) * 1E-7
      val App_i = numbers(7) * 1E-7
      val Appp_i = 0.0
      val B_i = numbers(10) * 1E-7
      val Bp_i = 0.0
      val Bpp_i = numbers(8) * 1E-7
      val Bppp_i = numbers(9) * 1E-7
      val l = numbers(0)
      val lp = numbers(1)
      val F = numbers(2)
      val D = numbers(3)
      val Ω = numbers(4)
      new IAU2000NutationEntry(A_i, Ap_i, App_i, Appp_i, B_i, Bp_i, Bpp_i, Bppp_i, l, lp, F, D, Ω)
    }).toList
  }

  def parseMHB2000Planet(content: Source): List[IAU2000NutationEntry] = {
    // Remove comment and empty lines
    val datalines = content.getLines().filter(_.headOption match {
      case Some(char) => char != '#'
      case None => false
    })

    // Map the data lines onto a IAU2000NutationEntry
    datalines.map(line => {
      // 1E-7 to convert from 0.1 μas => arcseconds
      val numbers = line.split( """\s+""").map(_.toDouble)
      val A_i = numbers(13) * 1E-7
      val Ap_i = 0.0
      val App_i = numbers(14) * 1E-7
      val Appp_i = 0.0
      val B_i = numbers(15) * 1E-7
      val Bp_i = 0.0
      val Bpp_i = numbers(16) * 1E-7
      val Bppp_i = 0.0
      val l = numbers(0)
      val lp = 0.0
      val F = numbers(1)
      val D = numbers(2)
      val Ω = numbers(3)
      val L_Me = numbers(4)
      val L_Ve = numbers(5)
      val L_E = numbers(6)
      val L_Ma = numbers(7)
      val L_J = numbers(8)
      val L_Sa = numbers(9)
      val L_U = numbers(10)
      val L_Ne = numbers(11)
      val p_A = numbers(12)
      new IAU2000NutationEntry(A_i, Ap_i, App_i, Appp_i, B_i, Bp_i, Bpp_i, Bppp_i, l, lp, F, D, Ω, L_Me, L_Ve, L_E, L_Ma, L_J, L_Sa, L_U, L_Ne, p_A)
    }).toList
  }

  def IERS2010(implicit universe: Universe): List[IAU2000NutationEntry] = {
    // Download the nutation in longitude coefficients (IAU 2000_R06 expression) derived from the IAU 2000A
    // lunisolar and planetary components with slight IAU 2006 adjustments (provided by N. Capitaine).
    parseIERS2010(getZipEntrySource("org.iers.conv2010", "chapter5", "tab5.3a.txt").getOrElse(Source.fromString("")), longitudeFile = true) :::
      parseIERS2010(getZipEntrySource("org.iers.conv2010", "chapter5", "tab5.3b.txt").getOrElse(Source.fromString("")), longitudeFile = false)
  }

  def MHB2000_2000A(implicit universe: Universe): List[IAU2000NutationEntry] = {
    parseMHB2000(getZipEntrySource("org.iers", "MHB2000", "iau00a_nutation_ls.tab").getOrElse(Source.fromString(""))) :::
      parseMHB2000Planet(getZipEntrySource("org.iers", "MHB2000", "iau00a_nutation_pl.tab").getOrElse(Source.fromString("")))
  }

  def MHB2000_2000B(implicit universe: Universe): List[IAU2000NutationEntry] = {
    parseMHB2000(getZipEntrySource("org.iers", "MHB2000", "iau00b_nutation.tab").getOrElse(Source.fromString("")))
  }

  @Deprecated
  def getZipEntrySource( groupId: String, artifactId: String, filename: String )(implicit universe: Universe): Option[Source] =
    Resources.findArchive( ResourceDescription(groupId, artifactId, extension = "zip") ).flatMap( _.findEntry(filename) ).map( _.openSource() ).toOption

}
