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
package be.angelcorp.libs.celest.time.timeStandard;

import be.angelcorp.libs.celest.time.Epoch;

public interface ITimeStandard {

	/**
	 * Returns the number of seconds to add to TT to get this timeStandard, so;
	 * 
	 * <pre>
     * offsetFromTT(jd_tt) = this - TT
	 * T<sub>this</sub>  = T<sub>TT</sub>  + this.offsetFromTT(JD<sub>TAI</sub>)
	 * JD<sub>this</sub> = JD<sub>TT</sub> + Time.convert(this.offsetFromTT(JD<sub>TT</sub>), Time.second, Time.day)
	 * </pre>
	 * 
	 * @return The number of seconds between this {@link TimeStandards} and {@link TimeStandards#TAI}.
	 */
	public abstract double offsetFromTT(Epoch JD_tt);

	/**
	 * Returns the number of seconds to add to this {@link TimeStandards} to get TT, so;
	 * 
	 * <pre>
     * offsetToTT(jd_this) = TT - this
	 * T<sub>TT</sub>  = T<sub>this</sub>  + this.offsetToTT(JD<sub>this</sub>)
	 * JD<sub>TT</sub> = JD<sub>this</sub> + Time.convert(this.offsetToTT(JD<sub>this</sub>), Time.second, Time.day)
	 * </pre>
	 * 
	 * @return The number of seconds between this {@link TimeStandards} and {@link TimeStandards#TAI}.
	 */
	public abstract double offsetToTT(Epoch JD_this);

}
