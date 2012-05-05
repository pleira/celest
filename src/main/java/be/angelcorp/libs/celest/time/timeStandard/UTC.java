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

import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.Constant;

import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.util.physics.Time;

/**
 * Coordinated Universal Time.
 * 
 * <p>
 * Conversions based on:<br>
 * <a href="ftp://maia.usno.navy.mil/ser7/tai-utc.dat">ftp://maia.usno.navy.mil/ser7/tai-utc.dat</a>
 * </p>
 * 
 * @author Simon Billemont
 * 
 */
@Immutable
public class UTC implements ITimeStandard {

	/** Map with key=JD_TAI, value=TAI-UTC [sec] */
	private static final TreeMap<Double, UnivariateFunction>	TAI_UTC;
	static {
		TAI_UTC = new TreeMap<>();
		TAI_UTC.put(0., new Constant(0)); // Not defined before 1961 so no offset
		TAI_UTC.put(2437300.5, new UTC_TAI_Function(1.4228180, 37300., 0.001296)); // 1961 JAN 1
		TAI_UTC.put(2437512.5, new UTC_TAI_Function(1.3728180, 37300., 0.001296)); // 1961 AUG 1
		TAI_UTC.put(2437665.5, new UTC_TAI_Function(1.8458580, 37665., 0.0011232));// 1962 JAN 1
		TAI_UTC.put(2438334.5, new UTC_TAI_Function(1.9458580, 37665., 0.0011232));// 1963 NOV 1
		TAI_UTC.put(2438395.5, new UTC_TAI_Function(3.2401300, 38761., 0.001296)); // 1964 JAN 1
		TAI_UTC.put(2438486.5, new UTC_TAI_Function(3.3401300, 38761., 0.001296)); // 1964 APR 1
		TAI_UTC.put(2438639.5, new UTC_TAI_Function(3.4401300, 38761., 0.001296)); // 1964 SEP 1
		TAI_UTC.put(2438761.5, new UTC_TAI_Function(3.5401300, 38761., 0.001296)); // 1965 JAN 1
		TAI_UTC.put(2438820.5, new UTC_TAI_Function(3.6401300, 38761., 0.001296)); // 1965 MAR 1
		TAI_UTC.put(2438942.5, new UTC_TAI_Function(3.7401300, 38761., 0.001296)); // 1965 JUL 1
		TAI_UTC.put(2439004.5, new UTC_TAI_Function(3.8401300, 38761., 0.001296)); // 1965 SEP 1
		TAI_UTC.put(2439126.5, new UTC_TAI_Function(4.3131700, 39126., 0.002592)); // 1966 JAN 1
		TAI_UTC.put(2439887.5, new UTC_TAI_Function(4.2131700, 39126., 0.002592)); // 1968 FEB 1
		TAI_UTC.put(2441317.5, new Constant(10.)); // 1972 JAN 1
		TAI_UTC.put(2441499.5, new Constant(11.)); // 1972 JUL 1
		TAI_UTC.put(2441683.5, new Constant(12.)); // 1973 JAN 1
		TAI_UTC.put(2442048.5, new Constant(13.)); // 1974 JAN 1
		TAI_UTC.put(2442413.5, new Constant(14.)); // 1975 JAN 1
		TAI_UTC.put(2442778.5, new Constant(15.)); // 1976 JAN 1
		TAI_UTC.put(2443144.5, new Constant(16.)); // 1977 JAN 1
		TAI_UTC.put(2443509.5, new Constant(17.)); // 1978 JAN 1
		TAI_UTC.put(2443874.5, new Constant(18.)); // 1979 JAN 1
		TAI_UTC.put(2444239.5, new Constant(19.)); // 1980 JAN 1
		TAI_UTC.put(2444786.5, new Constant(20.)); // 1981 JUL 1
		TAI_UTC.put(2445151.5, new Constant(21.)); // 1982 JUL 1
		TAI_UTC.put(2445516.5, new Constant(22.)); // 1983 JUL 1
		TAI_UTC.put(2446247.5, new Constant(23.)); // 1985 JUL 1
		TAI_UTC.put(2447161.5, new Constant(24.)); // 1988 JAN 1
		TAI_UTC.put(2447892.5, new Constant(25.)); // 1990 JAN 1
		TAI_UTC.put(2448257.5, new Constant(26.)); // 1991 JAN 1
		TAI_UTC.put(2448804.5, new Constant(27.)); // 1992 JUL 1
		TAI_UTC.put(2449169.5, new Constant(28.)); // 1993 JUL 1
		TAI_UTC.put(2449534.5, new Constant(29.)); // 1994 JUL 1
		TAI_UTC.put(2450083.5, new Constant(30.)); // 1996 JAN 1
		TAI_UTC.put(2450630.5, new Constant(31.)); // 1997 JUL 1
		TAI_UTC.put(2451179.5, new Constant(32.)); // 1999 JAN 1
		TAI_UTC.put(2453736.5, new Constant(33.)); // 2006 JAN 1
		TAI_UTC.put(2454832.5, new Constant(34.)); // 2009 JAN 1
		TAI_UTC.put(2456109.5, new Constant(35.)); // 2012 JUL 1
	}

	/** Singleton UTC instance */
	private static UTC											instance	= new UTC();

	/** Get singleton UTC instance */
	public static UTC get() {
		return instance;
	}

	/** {@inheritDoc} */
	@Override
	public double offsetFromTAI(IJulianDate JD_tai) {
		double jd = JD_tai.getJD();

		Entry<Double, UnivariateFunction> entry = TAI_UTC.floorEntry(jd);
		double offset = -entry.getValue().value(jd);

		// Check if the leap second pushes over a TAI - UTC bound
		if (jd + Time.convert(offset, Time.day_julian) < entry.getKey()) {
			entry = TAI_UTC.floorEntry(jd - 1);
			offset = -entry.getValue().value(jd);
		}

		return offset;
	}

	/** {@inheritDoc} */
	@Override
	public double offsetToTAI(IJulianDate JD_utc) {
		double jd = JD_utc.getJD();

		Entry<Double, UnivariateFunction> entry = TAI_UTC.floorEntry(jd);
		double offset_TAI = entry.getValue().value(jd);

		return offset_TAI;
	}

}
