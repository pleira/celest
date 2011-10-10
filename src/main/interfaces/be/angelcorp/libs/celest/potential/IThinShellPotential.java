/**
 * Copyright (C) 2011 simon <aodtorusan@gmail.com>
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
package be.angelcorp.libs.celest.potential;

/**
 * Create an ideal gravitational potential of thin spherical shell. This is equivalent to a point mass,
 * when the point considered is outside the shell and when inside the shell 0(see Gauss' law for
 * gravity).
 * 
 * @author Simon Billemont
 * 
 */
public interface IThinShellPotential extends IPointMassPotential {

}