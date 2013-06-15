/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
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
package be.angelcorp.libs.celest.frames.implementations

import be.angelcorp.libs.celest.frames.IReferenceFrame
import be.angelcorp.libs.celest.frames.systems.ITRS
import com.google.common.base.Predicate
import be.angelcorp.libs.celest.universe.Universe

/**
 * The ITRF (International Terrestrial Reference Frame) is an implementation of the
 * [[be.angelcorp.libs.celest.frames.systems.ITRS]] system, a standardized
 * [[be.angelcorp.libs.celest.frames.systems.ECEF]] reference system. It comes in several definitions based
 * on different years:
 * <p>
 * 2008, 2005, 2000, 1997, 1996, 1994, 1993, 1992, 1991, 1990, 1989, 1988
 * </p>
 * 
 * <p>
 * Also see:
 * <p>
 * <ul>
 * <li> G�rard Petit, Brian Luzum, <b>"IERS Conventions (2010)"</b>, IERS Technical Note No. 36,
 * International Earth Rotation and Reference Systems Service (IERS), 2010, [online]
 * <a href="http://www.iers.org/TN36/">http://www.iers.org/TN36/</a></li>
 * </ul>
 *
 * @param year Year of the ITRS realization.
 *
 * @author Simon
 */
class ITRF(val year: Int = 2008)(implicit universe: Universe) extends IReferenceFrame with ITRS {

 	/**
	 * Get the epoch of the {@link ITRS} realization.
	 */
	def epoch = universe.epoch(year)

  override def toString() = "ICRF" + year

}

object ITRF {

  /**
   * Create a [[com.google.common.base.Predicate]] that is capable of identifying a specific {@link ITRF} frame in a
   * collection of {@link IReferenceFrame}'s.
   *
   * @param year Year of the ITRF frame to search for.
   * @return A Predicate to identify the ITRF frames of a specific year.
   */
  def itrfPreficate(year: Int) = new Predicate[IReferenceFrame] {
      override def apply(input: IReferenceFrame) =
        (classOf[ITRF].isAssignableFrom(input.getClass)) && ( input.asInstanceOf[ITRF].year == year)
    }

}
