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

	/**
     * International Atomic Time
	 * @deprecated Use TAI
	 */
	TAI
	{
		@Override
		public double offsetFromTT(IJulianDate JD_tt) {
			return -32.184;
		}

		@Override
		public double offsetToTT(IJulianDate JD_tt) {
			return 32.184;
		}
	},
	/**
	 * Barycentric Coordinate Time
	 * @deprecated Use TCB
	 */
	TCB
	{
		@Override
		public double offsetFromTT(IJulianDate JD_tt) {
			// Equal when ignoring periodic signals ?
			return TDB.offsetFromTT(JD_tt);
		}

		@Override
		public double offsetToTT(IJulianDate JD_tcb) {
			// Equal when ignoring periodic signals ?
			return TDB.offsetToTT(JD_tcb);
		}
	},
	/**
	 * Geocentric Coordinate Time
	 * @deprecated Use TCG
	 */
	@Deprecated
	TCG
	{
		@Override
		public double offsetFromTT(IJulianDate JD_tt) {
			return be.angelcorp.libs.celest.time.timeStandard.TCG.get().offsetFromTT(JD_tt);
		}

		@Override
		public double offsetToTT(IJulianDate JD_tcg) {
			return be.angelcorp.libs.celest.time.timeStandard.TCG.get().offsetToTT(JD_tcg);
		}
	},
	/**
	 * Barycentric Dynamical Time
	 * @deprecated Use TDB
	 */
	@Deprecated
	TDB
	{
		@Override
		public double offsetFromTT(IJulianDate JD_tt) {
			return new TDB().offsetFromTT(JD_tt);
		}

		@Override
		public double offsetToTT(IJulianDate JD_tdb) {
            return new TDB().offsetToTT(JD_tdb);
		}
	},
	/**
	 * Terrestrial Dynamical Time
	 * @deprecated Use TDT
	 */
	TDT
	{
		@Override
		public double offsetFromTT(IJulianDate JD_tt) {
			// TODO Auto-generated method stub
			// return 0;
			throw new UnsupportedOperationException("Not implemented yet");
		}

		@Override
		public double offsetToTT(IJulianDate JD_tdt) {
			// TODO Auto-generated method stub
			// return 0;
			throw new UnsupportedOperationException("Not implemented yet");
		}
	},
	/**
	 * Terrestrial Time
	 * @deprecated Use TT
	 */
	TT
	{
		@Override
		public double offsetFromTT(IJulianDate JD_tt) {
			return 0;
		}

		@Override
		public double offsetToTT(IJulianDate JD_tt) {
			return 0;
		}
	},
	/**
	 * Universal Time UT1
	 * @deprecated Use UT1
	 */
	UT1
	{
		@Override
		public double offsetFromTT(IJulianDate JD_tt) {
			// TODO Auto-generated method stub
			// return 0;
			throw new UnsupportedOperationException("Not implemented yet");
		}

		@Override
		public double offsetToTT(IJulianDate JD_ut1) {
			// TODO Auto-generated method stub
			// return 0;
			throw new UnsupportedOperationException("Not implemented yet");
		}
	},
	/**
	 * Coordinated Universal Time
	 * @deprecated Use UTC
	 */
	UTC
	{
		@Override
		public double offsetFromTT(IJulianDate JD_tt) {
			return be.angelcorp.libs.celest.time.timeStandard.UTC.get().offsetFromTT(JD_tt);
		}

		@Override
		public double offsetToTT(IJulianDate JD_tcg) {
			return be.angelcorp.libs.celest.time.timeStandard.UTC.get().offsetToTT(JD_tcg);
		}
	};

}
