/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
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
package be.angelcorp.celest.time

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class TestDegreeMinSec extends FlatSpec with ShouldMatchers {

  "DegreeMinSec" should "generate convert to the correct number or radians" in {
    val precision_1s_dms_in_rad = (1 / 3600.0) * (math.Pi / 180.0); // ~4E-6

    val dms1 = new DegreeMinSec(180, 59, 22.1567587)
    // Reference value from matlab:  fprintf('%16.16f\n', deg2rad( dms2degrees([180, 59, 22.1567587]) ))
    dms1.rad should be(3.1588624768985385 plusOrMinus 1E-15)

    val hms = new HourMinSec(12, 3, 57.4771172)
    val dms2 = DegreeMinSec(hms)
    // Reference value from matlab:  fprintf('%16.16f\n', deg2rad( dms2degrees([180, 59, 22.1567587]) ))
    dms2.rad should be(3.1588624768985385 plusOrMinus 1E-11)
  }

}
