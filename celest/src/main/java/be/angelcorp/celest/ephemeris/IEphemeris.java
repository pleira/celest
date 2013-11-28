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
package be.angelcorp.celest.ephemeris;

import be.angelcorp.celest.time.Epoch;
import be.angelcorp.celest.time.JulianDate;

/**
 * {@link IEphemeris} is an interface that allows this object to retrieve a specific ephemeris type at a
 * given {@link JulianDate} or iterate over possible ephemeris values. What it retrieves can be any
 * object, such as a {@link be.angelcorp.celest.state.Orbit} or values such as range/range-rate/name/....
 *
 * @param <S> Type of ephemeris to retrieve
 * @author Simon Billemont
 */
public interface IEphemeris<S> {

    /**
     * Get an iterator that iterates over possible ephmeris values
     *
     * @return The iterator that iterates over all ephemeris values &lsaquo;S&rsaquo;
     */
    // public Iterable<S> getEphemerisIterator();

    /**
     * Get the ephemeris at a given date
     *
     * @param date Epemeris juliandate to retrieve the emphemeris on
     * @return The ephemeris at the specified date (or as close as possible to)
     */
    public S getEphemerisOn(Epoch date);

}
