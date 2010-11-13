/**
 * Copyright (C) 2010 Simon Billemont <aodtorusan@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.angelcorp.libs.celest;

public class JulianDate {

	public static double decimal_day(long day, long hour, long minute, long second) {
		return day + decimal_hour(hour, minute, second) / 24;
	}

	public static double decimal_hour(long hour, long minute, long second) {
		return hour + (double) minute / 60 + (double) second / 3600;
	}

	public static double toJuliandate(long year, long month, long day, long hour, long minute,
			long second) {
		/* after Oct 15th, 1582 */
		long j_year = year;
		long j_month = month;
		long A, B, C, D;

		if (month == 1 || month == 2) {
			j_month = month + 12;
			j_year = year - 1;
		}
		A = (j_year / 100);
		B = 2 - A + (A / 4);
		C = (long) (365.25 * j_year);
		D = (long) (30.6001 * (j_month + 1));
		return B + C + D + decimal_day(day, hour, minute, second) + 1720994.5;
	}

	double	date;

	public JulianDate(long date) {
		this.date = date;
	}

	public double getDate() {
		return date;
	}

	public void setDate(double date) {
		this.date = date;
	}
}
