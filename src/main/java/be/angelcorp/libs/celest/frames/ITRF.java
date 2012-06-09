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
package be.angelcorp.libs.celest.frames;

import be.angelcorp.libs.celest.frames.systems.ECEF;
import be.angelcorp.libs.celest.frames.systems.ITRS;
import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.celest.time.JulianDate;
import be.angelcorp.libs.celest.time.timeStandard.TimeStandards;

import com.google.common.base.Predicate;

/**
 * The ITRF (International Terrestrial Reference Frame) is an implementation of the {@link ITRS} system,
 * a standardized {@link ECEF} reference system. It comes in several definitions based on different
 * years:
 * <p>
 * 2008, 2005, 2000, 1997, 1996, 1994, 1993, 1992, 1991, 1990, 1989, 1988
 * </p>
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
 * @author Simon
 */
public class ITRF implements IReferenceFrame, ITRS {

	@Deprecated
	public static IJulianDate epoch(int year) {
		return new JulianDate(year, 1, 1, 12, 0, 0, TimeStandards.TT);
	}

	/**
	 * Create a {@link Predicate} that is capable of identifying a specific {@link ITRF} frame in a
	 * collection of {@link IReferenceFrame}'s.
	 * 
	 * @param year
	 *            Year of the {@link ITRF} frame to search for.
	 * @return A {@link Predicate} to identify the {@link ITRF} frames of a specific year.
	 */
	public static Predicate<IReferenceFrame> itrfPreficate(final int year) {
		return new Predicate<IReferenceFrame>() {
			@Override
			public boolean apply(IReferenceFrame input) {
				if (ITRF.class.isAssignableFrom(input.getClass())) {
					ITRF itrf = (ITRF) input;
					if (itrf.getYear() == year)
						return true;
				}
				return false;
			}
		};
	}

	/** Year of the {@link ITRS} realization. */
	private final int	year;

	/**
	 * Create an {@link ITRF} frame based on a specific year of realization.
	 * 
	 * @param year
	 *            Year of the {@link ITRS} realization.
	 */
	public ITRF(int year) {
		this.year = year;
	}

	/**
	 * Get the epoch of the {@link ITRS} realization.
	 */
	public IJulianDate getEpoch() {
		return epoch(year);
	}

	/**
	 * Get the year of the {@link ITRS} realization.
	 */
	public int getYear() {
		return year;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "ICRF" + year;
	}

}
