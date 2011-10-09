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
package be.angelcorp.libs.celest.time;

import java.util.Date;

/**
 * A container for a julian date
 * 
 * @author Simon Billemont
 * 
 */
public interface IJulianDate {

	/**
	 * Get the julian date in a standard date object
	 * 
	 * @return JD in normal date format
	 */
	public abstract Date getDate();

	/**
	 * Get the julian date
	 * 
	 * @return JD
	 */
	public abstract double getJD();

	/**
	 * Set the julian date
	 * 
	 * @param date
	 *            Standard date object
	 */
	public abstract void setDate(Date date);

	/**
	 * Set the julian date
	 * 
	 * @param date
	 *            JD
	 */
	public abstract void setJD(double date);

}