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

package be.angelcorp.celest.time.timeStandard

import java.io.File

import be.angelcorp.celest.time.JulianDate
import be.angelcorp.celest.time.timeStandard.TimeStandards._
import be.angelcorp.celest.universe.DefaultUniverse
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.Predef._
import scala.io.Source

class SofaTimeStandard extends FlatSpec with ShouldMatchers {

  implicit val universe = new DefaultUniverse()

  def toTimeStandard(s: String): TimeStandard = s match {
    case "TT" => TT
    case "TAI" => TAI
    case "TDT" => TT
    case "TCB" => TCB
    case "TCG" => TCG
    case "TDB" => TDB
    case "UTC" => UTC
    case _ => throw new UnsupportedOperationException("Unknown TimeStandard: " + s)
  }

  println(new File(".").getAbsolutePath)

  val referenceData: List[(TimeStandard, Double, Double, TimeStandard, Double, Double)] = {
    val reference = getClass.getClassLoader.getResourceAsStream("time/timestandards.csv")
    Source.fromInputStream(reference).getLines().filterNot(_.startsWith("#")).map(s => {
      val entries = s.split(",")
      (
        toTimeStandard(entries(0)), entries(1).toDouble, entries(2).toDouble,
        toTimeStandard(entries(3)), entries(4).toDouble, entries(5).toDouble

        )
    }).toList
  }

  referenceData.foreach {
    entry => {
      "%s @ %f".format(entry._1.getClass.getSimpleName, (entry._2 + entry._3)) should
        "convert to %s @ %f".format(entry._4.getClass.getSimpleName, (entry._5 + entry._6)) in {
        val transf = new JulianDate(entry._2 + entry._3, entry._1).inTimeStandard(entry._4)
        transf.jd should be((entry._5 + entry._6) plusOrMinus (transf.jd * 1E-16))
      }
    }
  }

  /*
    "GEI_J2000" should "transform to GEI_D_MEAN" in {
      val pos_calc = transforms.transform_GEI_J2000_To_GEI_D_MEAN.getTransform( referenceEpoch ).transform(
        new CartesianElements( referenceDistances.get( transforms.GEI_J2000() ).get * Re, Vector3D.ZERO )
      )
      val pos_true = referenceDistances.get( transforms.GEI_D_MEAN() ).get
      assert( pos_true.equals(pos_calc.getR / Re, 5E-3) )
    }
  */
}
