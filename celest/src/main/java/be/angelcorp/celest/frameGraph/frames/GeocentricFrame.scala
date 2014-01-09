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
package be.angelcorp.celest.frameGraph.frames

import be.angelcorp.celest.frameGraph.ReferenceSystem

/**
 * The ITRF (International Terrestrial Reference Frame) is the default implementation of the
 * [[be.angelcorp.celest.frameGraph.frames.ITRS]]. It comes in several definitions based
 * on different years:
 * <p>
 * 2013, 2008, 2005, 2000, 1997, 1996, 1994, 1993, 1992, 1991, 1990, 1989, 1988
 * </p>
 *
 * <p>
 * Also see:
 * <p>
 * <ul>
 * <li> Gérard Petit, Brian Luzum, <b>"IERS Conventions (2010)"</b>, IERS Technical Note No. 36,
 * International Earth Rotation and Reference Systems Service (IERS), 2010, [online]
 * <a href="http://www.iers.org/TN36/">http://www.iers.org/TN36/</a></li>
 * </ul>
 *
 * @param year Year of the ITRS realization.
 *
 * @author Simon
 */
case class ITRF(year: Int = 2008) extends ITRS {

  override def toString = "ICRF" + year

}

object ITRF {

  /**
   * Create a predicate that is capable of identifying a specific ITRF frame in a collection of ReferenceFrames.
   *
   * @param year Year of the ITRF frame to search for.
   * @return A Predicate to identify the ITRF frameGraph of a specific year.
   */
  def itrfPreficate(year: Int): ReferenceSystem => Boolean = {
    case ITRF(`year`) => true
    case _ => false
  }

}

/** Default implementation of the [[be.angelcorp.celest.frameGraph.frames.TIRS]] reference system. */
case class TIRF() extends TIRS

/** Default implementation of the [[be.angelcorp.celest.frameGraph.frames.ERS]] reference system. */
case class ERF() extends ERS

/** Default implementation of the [[be.angelcorp.celest.frameGraph.frames.MODSystem]] reference system. */
case class MOD() extends MODSystem

/** Default implementation of the [[be.angelcorp.celest.frameGraph.frames.J2000System]] reference system. */
case class J2000() extends J2000System

/** Default implementation of the [[be.angelcorp.celest.frameGraph.frames.GCRS]] reference system. */
case class GCRF() extends GCRS
