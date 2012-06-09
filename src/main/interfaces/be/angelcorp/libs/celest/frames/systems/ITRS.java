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
package be.angelcorp.libs.celest.frames.systems;

import be.angelcorp.libs.celest.frames.ITRF;

/**
 * The International Terrestrial Reference System (ITRS) is a world spatial reference system co-rotating
 * with the Earth in its diurnal motion in space. The realizations of the {@link ITRS} are the
 * {@link ITRF} frames defined by IERS services.
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
 * @author Simon Billemont
 */
public interface ITRS extends ECEF {

}
