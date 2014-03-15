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


/**
 * A reference system that is centered at te Earth center of mass.
 */
trait GeocentricSystem extends BodyCenteredSystem

/**
 * The International Terrestrial Reference System (ITRS) is a world spatial reference system co-rotating with the
 * Earth in its diurnal motion in space. The realizations of the [[be.angelcorp.celest.frameGraph.frames.ITRS]]
 * are the [[be.angelcorp.celest.frameGraph.frames.ITRF]] frameGraph defined by IERS services.
 *
 * This frame is centered at the center of mass of the Earth including oceans and atmosphere. The frame axes are linked
 * to the ICRS through the Earth orientation parameters (EOP). This frame co-rotates with the Earth.
 *
 * See frame graph documentation.
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
 * @author Simon Billemont
 */
trait ITRS extends GeocentricSystem {
  def year: Int
}

/**
 * The Terrestrial Intermediate Reference System (TIRS).
 *
 * See frame graph documentation.
 *
 * @author Simon Billemont
 */
trait TIRS extends GeocentricSystem

/**
 * The Celestial Intermediate Reference System (CIRS).
 *
 * See frame graph documentation.
 *
 * @author Simon Billemont
 */
trait CIRS extends GeocentricSystem

/**
 * The Earth Reference System (ERS).
 *
 * This frame is linked to the Terestrial Intermediate Reference System ([[be.angelcorp.celest.frameGraph.frames.TIRS]]) through the Earth rotation.
 * This frame is also linked to the Mean Of Date system ([[be.angelcorp.celest.frameGraph.frames.MOD]]) through nutation of the Earth rotation axis.
 *
 * See frame graph documentation.
 *
 * @author Simon Billemont
 */
trait ERS extends GeocentricSystem

/**
 * The Mean Of Date system (MODs).
 *
 * This frame is linked to the Earth Reference System/True Of Date system through the Nutation of the Earth rotation axis.
 * This frame is linked to the J2000|EME2000 / GCRS through the precession of the Earth rotation axis.
 *
 * See frame graph documentation.
 *
 * @author Simon Billemont
 */
trait MOD extends GeocentricSystem

/**
 * The J2000 or EME2000 reference system (J2000s).
 *
 * See frame graph documentation.
 *
 * @author Simon Billemont
 */
trait EME2000 extends GeocentricSystem

/**
 * The Geocentric Celestial Reference System (TIRS).
 *
 * See frame graph documentation.
 *
 * @author Simon Billemont
 */
trait GCRS extends GeocentricSystem
