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

import javax.inject.{Singleton, Inject}
import be.angelcorp.celest.universe.Universe
import be.angelcorp.celest.body.CelestialBodies._

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
class ITRF @Inject()(val year: Int = 2008)(implicit universe: Universe) extends ITRS {
  override lazy val centerBody = earth
  override val toString = "ITRF" + year
}

@Singleton
class ITRF2000 @Inject()(implicit universe: Universe) extends ITRF(2000) {
  override val year = 2000
}

/** Default implementation of the [[be.angelcorp.celest.frameGraph.frames.TIRS]] reference system. */
@Singleton
class TIRF @Inject()(implicit universe: Universe) extends TIRS {
  override lazy val centerBody = earth
}

/** Default implementation of the [[be.angelcorp.celest.frameGraph.frames.CIRS]] reference system.  */
@Singleton
class CIRF @Inject()(implicit universe: Universe) extends CIRS {
  override lazy val centerBody = earth
}

/** Default implementation of the [[be.angelcorp.celest.frameGraph.frames.ERS]] reference system. */
class ERF @Inject()(implicit universe: Universe) extends ERS {
  override lazy val centerBody = earth
}

/** Default implementation of the [[be.angelcorp.celest.frameGraph.frames.MOD]] reference system. */
@Singleton
class MODFrame @Inject()(implicit universe: Universe) extends MOD {
  override lazy val centerBody = earth
}

/** Default implementation of the [[be.angelcorp.celest.frameGraph.frames.EME2000]] reference system. */
@Singleton
class EME2000Frame @Inject()(implicit universe: Universe) extends EME2000 {
  override lazy val centerBody = earth
}

/** Default implementation of the [[be.angelcorp.celest.frameGraph.frames.GCRS]] reference system. */
@Singleton
class GCRF @Inject()(implicit universe: Universe) extends GCRS {
  override lazy val centerBody = earth
}
