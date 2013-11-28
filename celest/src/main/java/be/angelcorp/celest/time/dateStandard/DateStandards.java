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

import static java.lang.Math.floor;

/**
 * Transforms between different Julian date representations
 * <p>
 * Conversion:
 * </p>
 * <p/>
 * <pre>
 * if (doFloor)
 *   transformed = floor( a * JD + b)
 * else
 *   transformed =      ( a * JD + b)
 * </pre>
 * <p/>
 * See:
 * <ul>
 * <li>US Navy, Time Service Department: http://tycho.usno.navy.mil/</li>
 * <li>CNES STELA Manual: http://logiciels.cnes.fr/STELA/en/Stela-User-Manual.pdf</li>
 * <li>Wikipedia: http://en.wikipedia.org/wiki/Julian_day</li>
 * </ul>
 *
 * @author Simon Billemont
 */
public enum DateStandards implements DateStandard {

    /**
     * Julian date, the default
     */
    JULIAN_DATE(0.),
    /**
     * CNES Julian days, from January 1st of 1950 AC at midnight
     */
    CNES_JULIAN_DAY(-2433282.5),
    /**
     * Julian day number, truncated Julian date
     */
    JULIAN_DAY_NUMBER(1., 0., true),
    /**
     * Reduced Julian day, ...
     */
    REDUCED_JULIAN_DAY(-2400000),
    /**
     * Modified Julian day, reduces JD digits to 5 for modern times, from 00:00 November 17, 1858
     */
    MODIFIED_JULIAN_DAY(-2400000.5),
    /**
     * Truncated Julian day, nasa def from Noerdlinger, 1995.
     */
    TRUNCATED_JULIAN_DAY(-2440000.5),
    /**
     * Dublin Julian day, ...
     */
    DUBLIN_JULIAN_DAY(-2415020),
    /**
     * ANSI date
     */
    ANSI_DATE(1., -2305812.5, true),
    /**
     * Unix time, seconds since January 1 1970
     */
    UNIX_TIME(86400., -2440587.5 * 86400.),

    JD(JULIAN_DATE),
    CJD(CNES_JULIAN_DAY),
    JDN(JULIAN_DAY_NUMBER),
    RJD(REDUCED_JULIAN_DAY),
    MJD(MODIFIED_JULIAN_DAY),
    TJD(TRUNCATED_JULIAN_DAY),
    DJD(DUBLIN_JULIAN_DAY);

    double a;
    double b;
    boolean doFloor;

    private DateStandards(DateStandards other) {
        this(other.a, other.b, other.doFloor);
    }

    private DateStandards(double b) {
        this(1., b, false);
    }

    private DateStandards(double a, double b) {
        this(a, b, false);
    }

    private DateStandards(double a, double b, boolean doFloor) {
        this.a = a;
        this.b = b;
        this.doFloor = doFloor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double fromJD(double jd) {
        return (doFloor) ? floor(a * jd + b) : a * jd + b;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double toJD(double date) {
        return (date - b) / a;
    }
}