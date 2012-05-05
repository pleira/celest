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

import be.angelcorp.libs.celest.time.IJulianDate;

public interface ITimeStandard {

	/**
	 * Returns the number of seconds to add to {@link TimeStandards#TAI} to get this
	 * {@link TimeStandards}, so;
	 * 
	 * <pre>
	 * T<sub>this</sub>  = T<sub>TAI</sub>  + this.offsetFromTAI(JD<sub>TAI</sub>)
	 * JD<sub>this</sub> = JD<sub>TAI</sub> + Time.convert(this.offsetFromTAI(JD<sub>TAI</sub>), Time.second, Time.day)
	 * </pre>
	 * 
	 * @return The number of seconds between this {@link TimeStandards} and {@link TimeStandards#TAI}.
	 */
	public abstract double offsetFromTAI(IJulianDate JD_tai);

	/**
	 * Returns the number of seconds to add to this {@link TimeStandards} to get
	 * {@link TimeStandards#TAI}, so;
	 * 
	 * <pre>
	 * T<sub>TAI</sub>  = T<sub>this</sub>  + this.offsetToTAI(JD<sub>this</sub>)
	 * JD<sub>TAI</sub> = JD<sub>this</sub> + Time.convert(this.offsetToTAI(JD<sub>this</sub>), Time.second, Time.day)
	 * </pre>
	 * 
	 * @return The number of seconds between this {@link TimeStandards} and {@link TimeStandards#TAI}.
	 */
	public abstract double offsetToTAI(IJulianDate JD_this);

}
