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

package be.angelcorp.libs.celest.time.timeStandard

import be.angelcorp.libs.celest.universe.Universe
import be.angelcorp.libs.celest.time.IJulianDate
import org.scalatest.exceptions.TestFailedException

class MockTime(val offset: Double) extends ITimeStandard {
  def offsetFromTT(JD_tt: IJulianDate) =  offset
  def offsetToTT(JD_this: IJulianDate) = -offset
}

class MockTimeUniverse extends Universe {
  def frames = throw new TestFailedException("Unsupported mock operation", 0)
  def TT : ITimeStandard = new MockTime(0)
  def TAI: ITimeStandard = throw new TestFailedException("Unsupported mock operation", 0)
  def TCG: ITimeStandard = throw new TestFailedException("Unsupported mock operation", 0)
  def TDT: ITimeStandard = throw new TestFailedException("Unsupported mock operation", 0)
  def TDB: ITimeStandard = throw new TestFailedException("Unsupported mock operation", 0)
  def TCB: ITimeStandard = throw new TestFailedException("Unsupported mock operation", 0)
  def UTC: ITimeStandard = throw new TestFailedException("Unsupported mock operation", 0)
  def UT1: ITimeStandard = throw new TestFailedException("Unsupported mock operation", 0)
}
