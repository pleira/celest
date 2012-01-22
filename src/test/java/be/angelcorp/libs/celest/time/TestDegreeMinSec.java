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

import static java.lang.Math.PI;
import junit.framework.TestCase;
import be.angelcorp.libs.celest.unit.Tests;

public class TestDegreeMinSec extends TestCase {

	public void testDegreeMinSec() {
		double precision_1s_dms_in_rad = (1 / 3600.0) * (PI / 180.); // ~4E-6

		DegreeMinSec dms = new DegreeMinSec(180, 59, 22.1567587);
		assertEquals(180, dms.getDegree());
		assertEquals(59, dms.getMinute());
		assertEquals(22.1567587, dms.getSecond(), 1E-16);
		Tests.assertEquals(new double[] { 180, 59, 22.1567587 }, dms.getTime().toArray(), 1E-16);

		IHourMinSec hms = new HourMinSec(12, 59, 22.1567587);
		dms = new DegreeMinSec(hms);
		assertEquals(dms.getRadian(), hms.getRadian(), precision_1s_dms_in_rad);
		hms = new HourMinSec(19, 01, 59.1564);
		dms.setRadian(hms.getRadian());
		assertEquals(dms.getRadian(), hms.getRadian(), precision_1s_dms_in_rad);

		dms = new DegreeMinSec(0, 0, 0);
		dms.setDegree(180);
		Tests.assertEquals(new double[] { 180, 0, 0 }, dms.getTime().toArray(), 1E-16);
		dms.setMinute(59);
		Tests.assertEquals(new double[] { 180, 59, 0 }, dms.getTime().toArray(), 1E-16);
		dms.setSecond(22.1567587);
		Tests.assertEquals(new double[] { 180, 59, 22.1567587 }, dms.getTime().toArray(), 1E-16);
	}

}
