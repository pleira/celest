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

package be.angelcorp.libs.celest.universe

import be.angelcorp.libs.celest.time.timeStandard._

class DefaultUniverse extends Universe {

  val TAI = new TAI()
  val TT  = new TT()
  lazy val TDT = TT
  lazy val TCB = new TCB( J2000_EPOCH )
  lazy val TCG = new TCG( TT_EPOCH    )
  lazy val TDB = new TDB( TT_EPOCH    )
  lazy val UTC = new DefaultUTC( TAI )
  lazy val UT1 = new DefaultUT1( UTC )

  lazy val frames = throw new UnsupportedOperationException("Operation not yet implemented")

}
