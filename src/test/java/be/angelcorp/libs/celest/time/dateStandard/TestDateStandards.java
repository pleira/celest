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
package be.angelcorp.libs.celest.time.dateStandard;

import static be.angelcorp.libs.celest.time.dateStandard.DateStandards.ANSI_DATE;
import static be.angelcorp.libs.celest.time.dateStandard.DateStandards.CJD;
import static be.angelcorp.libs.celest.time.dateStandard.DateStandards.CNES_JULIAN_DAY;
import static be.angelcorp.libs.celest.time.dateStandard.DateStandards.DJD;
import static be.angelcorp.libs.celest.time.dateStandard.DateStandards.DUBLIN_JULIAN_DAY;
import static be.angelcorp.libs.celest.time.dateStandard.DateStandards.JD;
import static be.angelcorp.libs.celest.time.dateStandard.DateStandards.JDN;
import static be.angelcorp.libs.celest.time.dateStandard.DateStandards.JULIAN_DATE;
import static be.angelcorp.libs.celest.time.dateStandard.DateStandards.JULIAN_DAY_NUMBER;
import static be.angelcorp.libs.celest.time.dateStandard.DateStandards.MJD;
import static be.angelcorp.libs.celest.time.dateStandard.DateStandards.MODIFIED_JULIAN_DAY;
import static be.angelcorp.libs.celest.time.dateStandard.DateStandards.REDUCED_JULIAN_DAY;
import static be.angelcorp.libs.celest.time.dateStandard.DateStandards.RJD;
import static be.angelcorp.libs.celest.time.dateStandard.DateStandards.TJD;
import static be.angelcorp.libs.celest.time.dateStandard.DateStandards.TRUNCATED_JULIAN_DAY;
import static be.angelcorp.libs.celest.time.dateStandard.DateStandards.UNIX_TIME;
import be.angelcorp.libs.celest.unit.CelestTest;

public class TestDateStandards extends CelestTest {

	public void testDateStandards() {
		// based on http://en.wikipedia.org/wiki/Julian_day numbers
		assertEquals(2455853.72924, JULIAN_DATE.toJD(2455853.72924), 1E-6);
		assertEquals(2455853.72924, JD.toJD(2455853.72924), 1E-6);
		assertEquals(2455853.0, JULIAN_DAY_NUMBER.toJD(2455853), 1E-16);
		assertEquals(2455853.0, JDN.toJD(2455853), 1E-16);
		assertEquals(2455853.72924, CNES_JULIAN_DAY.toJD(22571.22924), 1E-6);
		assertEquals(2455853.72924, CJD.toJD(22571.22924), 1E-6);
		assertEquals(2455853.72924, REDUCED_JULIAN_DAY.toJD(55853.72924), 1E-6);
		assertEquals(2455853.72924, RJD.toJD(55853.72924), 1E-6);
		assertEquals(2455853.72924, MODIFIED_JULIAN_DAY.toJD(55853.22924), 1E-6);
		assertEquals(2455853.72924, MJD.toJD(55853.22924), 1E-6);
		assertEquals(2455853.72924, TRUNCATED_JULIAN_DAY.toJD(15853.22924), 1E-6);
		assertEquals(2455853.72924, TJD.toJD(15853.22924), 1E-6);
		assertEquals(2455853.72924, DUBLIN_JULIAN_DAY.toJD(40833.72924), 1E-6);
		assertEquals(2455853.72924, DJD.toJD(40833.72924), 1E-6);
		assertEquals(2455853.5, ANSI_DATE.toJD(150041), 1E-16);
		assertEquals(2455853.72924, UNIX_TIME.toJD(1319002206.33598), 1E-6);

		assertEquals(2455853.72924, JULIAN_DATE.fromJD(2455853.72924), 1E-6);
		assertEquals(2455853.72924, JD.fromJD(2455853.72924), 1E-6);
		assertEquals(2455853, JULIAN_DAY_NUMBER.fromJD(2455853.72924), 1E-16);
		assertEquals(2455853, JDN.fromJD(2455853.72924), 1E-16);
		assertEquals(22571.22924, CNES_JULIAN_DAY.fromJD(2455853.72924), 1E-6);
		assertEquals(22571.22924, CJD.fromJD(2455853.72924), 1E-6);
		assertEquals(55853.72924, REDUCED_JULIAN_DAY.fromJD(2455853.72924), 1E-6);
		assertEquals(55853.72924, RJD.fromJD(2455853.72924), 1E-6);
		assertEquals(55853.22924, MODIFIED_JULIAN_DAY.fromJD(2455853.72924), 1E-6);
		assertEquals(55853.22924, MJD.fromJD(2455853.72924), 1E-6);
		assertEquals(15853.22924, TRUNCATED_JULIAN_DAY.fromJD(2455853.72924), 1E-6);
		assertEquals(15853.22924, TJD.fromJD(2455853.72924), 1E-6);
		assertEquals(40833.72924, DUBLIN_JULIAN_DAY.fromJD(2455853.72924), 1E-6);
		assertEquals(40833.72924, DJD.fromJD(2455853.72924), 1E-6);
		assertEquals(150041, ANSI_DATE.fromJD(2455853.72924), 1E-16);
		assertEquals(1319002206.33598, UNIX_TIME.fromJD(2455853.72924), 1E-4);
	}

}
