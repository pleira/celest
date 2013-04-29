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

package be.angelcorp.libs.celest.eom.forcesmodel;

import be.angelcorp.libs.celest.body.IShapedBody;

/**
 * Force model acting on a shaped body.
 *
 * @param <B> Type of shaped body that this model acts on.
 */
public interface ISurfaceForceModel<B extends IShapedBody> extends IForceModel<B> { }
