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
package be.angelcorp.libs.celest.time;

import java.util.Date;

import be.angelcorp.libs.util.physics.Time;

/**
 * A container for a julian date
 * 
 * @author Simon Billemont
 * 
 */
public interface IJulianDate extends Comparable<IJulianDate> {

	/**
	 * Add time to this JulianDate
	 * 
	 * @param dt
	 *            Amount of time to add
	 * @param format
	 *            Format of dt
	 * @return This
	 */
	IJulianDate add(double dt, Time format);

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
	 * Get the internal julian date, but in the given external form
	 * 
	 * @param form
	 *            Form to return the date in
	 * @return Date represented by this julian date in the given form
	 */
	double getJD(JulianDateForm form);

	/**
	 * Get the amount of Julian days that this date is after the specified epoch:
	 * 
	 * <pre>
	 * this_time - epoch_time
	 * </pre>
	 * 
	 * @param epoch
	 *            Epoch to which to find the relative time
	 * @param timeformat
	 *            Format in which to return the time difference
	 * @return Amount of time from the passed epoch to this epoch
	 */
	double relativeTo(IJulianDate epoch, Time timeformat);

	/**
	 * 
	 * Get the amount of Julian days that this date is after the specified epoch:
	 * 
	 * <pre>
	 * this_time - epoch_time
	 * </pre>
	 * 
	 * @param epoch
	 *            Epoch to which to find the relative time
	 * @return Amount of julian days from the passed epoch to this epoch
	 */
	double relativeTo(JulianDate epoch);

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

	/**
	 * Set the julian date
	 * 
	 * @param date
	 *            date number in a form given by form
	 * @param form
	 *            Form that the given date is in
	 */
	void setJD(double date2, JulianDateForm form);

}