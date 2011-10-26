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

import static java.lang.Math.PI;
import junit.framework.TestCase;
import be.angelcorp.libs.celest.unit.Tests;

public class TestHourMinSec extends TestCase {

	public void testHourMinSec() {
		double precision_1s_hms_in_rad = (1 / 3600.0) * (15. * PI / 180.); // ~7E-5

		HourMinSec hms = new HourMinSec(12, 59, 22.1567587);
		assertEquals(12, hms.getHour());
		assertEquals(59, hms.getMinute());
		assertEquals(22.1567587, hms.getSecond(), 1E-16);
		Tests.assertEquals(new double[] { 12, 59, 22.1567587 }, hms.getTime().toArray(), 1E-16);

		IDegreeMinSec dms = new DegreeMinSec(180, 59, 22.1567587);
		hms = new HourMinSec(dms);
		Tests.assertEquals(hms.getRadian(), dms.getRadian(), precision_1s_hms_in_rad);
		dms = new DegreeMinSec(12, 01, 59.1564);
		hms.setRadian(dms.getRadian());
		Tests.assertEquals(hms.getRadian(), dms.getRadian(), precision_1s_hms_in_rad);

		hms = new HourMinSec(0, 0, 0);
		hms.setHour(12);
		Tests.assertEquals(new double[] { 12, 0, 0 }, hms.getTime().toArray(), 1E-16);
		hms.setMinute(59);
		Tests.assertEquals(new double[] { 12, 59, 0 }, hms.getTime().toArray(), 1E-16);
		hms.setSecond(22.1567587);
		Tests.assertEquals(new double[] { 12, 59, 22.1567587 }, hms.getTime().toArray(), 1E-16);
	}
}
