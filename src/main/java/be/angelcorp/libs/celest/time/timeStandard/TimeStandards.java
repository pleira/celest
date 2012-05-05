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

public enum TimeStandards implements ITimeStandard {
	/** Ephemeris Time */
	ET
	{
		@Override
		public double offsetFromTAI(IJulianDate JD_tai) {
			// TODO Auto-generated method stub
			// return 0;
			throw new UnsupportedOperationException("Not implemented yet");
		}

		@Override
		public double offsetToTAI(IJulianDate JD_et) {
			// TODO Auto-generated method stub
			// return 0;
			throw new UnsupportedOperationException("Not implemented yet");
		}
	},
	/** Greenwich Mean Time */
	GMT
	{
		@Override
		public double offsetFromTAI(IJulianDate JD_tai) {
			// TODO Auto-generated method stub
			// return 0;
			throw new UnsupportedOperationException("Not implemented yet");
		}

		@Override
		public double offsetToTAI(IJulianDate JD_gmt) {
			// TODO Auto-generated method stub
			// return 0;
			throw new UnsupportedOperationException("Not implemented yet");
		}
	},
	/** International Atomic Time */
	TAI
	{
		@Override
		public double offsetFromTAI(IJulianDate JD_tai) {
			return 0;
		}

		@Override
		public double offsetToTAI(IJulianDate JD_tai) {
			return 0;
		}
	},
	/** Barycentric Coordinate Time */
	TCB
	{
		@Override
		public double offsetFromTAI(IJulianDate JD_tai) {
			// TODO Auto-generated method stub
			// return 0;
			throw new UnsupportedOperationException("Not implemented yet");
		}

		@Override
		public double offsetToTAI(IJulianDate JD_tcb) {
			// TODO Auto-generated method stub
			// return 0;
			throw new UnsupportedOperationException("Not implemented yet");
		}
	},
	/**
	 * Geocentric Coordinate Time
	 * 
	 * @deprecated Use {@link TCG}
	 */
	@Deprecated
	TCG
	{
		@Override
		public double offsetFromTAI(IJulianDate JD_tai) {
			return be.angelcorp.libs.celest.time.timeStandard.TCG.get().offsetFromTAI(JD_tai);
		}

		@Override
		public double offsetToTAI(IJulianDate JD_tcg) {
			return be.angelcorp.libs.celest.time.timeStandard.TCG.get().offsetToTAI(JD_tcg);
		}
	},
	/** Barycentric Dynamical Time */
	TDB
	{
		@Override
		public double offsetFromTAI(IJulianDate JD_tai) {
			// TODO Auto-generated method stub
			// return 0;
			throw new UnsupportedOperationException("Not implemented yet");
		}

		@Override
		public double offsetToTAI(IJulianDate JD_tdb) {
			// TODO Auto-generated method stub
			// return 0;
			throw new UnsupportedOperationException("Not implemented yet");
		}
	},
	/** Terrestrial Dynamical Time */
	TDT
	{
		@Override
		public double offsetFromTAI(IJulianDate JD_tai) {
			// TODO Auto-generated method stub
			// return 0;
			throw new UnsupportedOperationException("Not implemented yet");
		}

		@Override
		public double offsetToTAI(IJulianDate JD_tdt) {
			// TODO Auto-generated method stub
			// return 0;
			throw new UnsupportedOperationException("Not implemented yet");
		}
	},
	/** Terrestrial Time */
	TT
	{
		@Override
		public double offsetFromTAI(IJulianDate JD_tai) {
			return 32.184;
		}

		@Override
		public double offsetToTAI(IJulianDate JD_tt) {
			return -32.184;
		}
	},
	/** Universal Time */
	UT
	{
		@Override
		public double offsetFromTAI(IJulianDate JD_tai) {
			// TODO Auto-generated method stub
			// return 0;
			throw new UnsupportedOperationException("Not implemented yet");
		}

		@Override
		public double offsetToTAI(IJulianDate JD_ut) {
			// TODO Auto-generated method stub
			// return 0;
			throw new UnsupportedOperationException("Not implemented yet");
		}
	},
	/** Universal Time UT0 */
	UT0
	{
		@Override
		public double offsetFromTAI(IJulianDate JD_tai) {
			// TODO Auto-generated method stub
			// return 0;
			throw new UnsupportedOperationException("Not implemented yet");
		}

		@Override
		public double offsetToTAI(IJulianDate JD_ut0) {
			// TODO Auto-generated method stub
			// return 0;
			throw new UnsupportedOperationException("Not implemented yet");
		}
	},
	/** Universal Time UT1 */
	UT1
	{
		@Override
		public double offsetFromTAI(IJulianDate JD_tai) {
			// TODO Auto-generated method stub
			// return 0;
			throw new UnsupportedOperationException("Not implemented yet");
		}

		@Override
		public double offsetToTAI(IJulianDate JD_ut1) {
			// TODO Auto-generated method stub
			// return 0;
			throw new UnsupportedOperationException("Not implemented yet");
		}
	};
}
