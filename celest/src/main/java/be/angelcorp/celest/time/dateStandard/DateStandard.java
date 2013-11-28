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
package be.angelcorp.celest.time.dateStandard;

/**
 * An {@link DateStandard} is a standard that can be used to convert any type of date in the form of a
 * single number from and to a Julian date.
 * <p/>
 * <p>
 * A date is considered a container for a specified amount of time since a predefined epoch.
 * </p>
 *
 * @author Simon Billemont
 */
public interface DateStandard {

    /**
     * Convert a {@link DateStandards#JULIAN_DATE} to this {@link DateStandard}.
     *
     * @param jd {@link DateStandards#JULIAN_DATE} to convert.
     * @return Equivalent date in the format represented by this class.
     */
    public abstract double fromJD(double jd);

    /**
     * Convert this date format to {@link DateStandards#JULIAN_DATE}.
     *
     * @param date Date in the format represented by this class.
     * @return An equivalent {@link DateStandards#JULIAN_DATE}.
     */
    public abstract double toJD(double date);

}