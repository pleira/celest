package be.angelcorp.celest.physics;

object Units {
  // ------------------------------------------------------
  // Angle
  // ------------------------------------------------------

  val radian = 1.0
  val degree = math.Pi / 180.0
  val gradian = math.Pi / 200.0
  val arcHour = math.Pi / 12.0
  val arcMinute = math.Pi / 10800.0
  val arcSecond = math.Pi / 648000.0
  val revolution = math.Pi * 2.0

  def radian(value: Double): Double = value

  def radians(value: Double): Double = value

  def degree(value: Double): Double = value * degree

  def degrees(value: Double): Double = value * degree

  def gradian(value: Double): Double = value * gradian

  def gradians(value: Double): Double = value * gradian

  def arcHour(value: Double): Double = value * arcHour

  def arcHours(value: Double): Double = value * arcHour

  def arcMinute(value: Double): Double = value * arcMinute

  def arcMinutes(value: Double): Double = value * arcMinute

  def arcSecond(value: Double): Double = value * arcSecond

  def arcSeconds(value: Double): Double = value * arcSecond

  def revolution(value: Double): Double = value * revolution

  def revolutions(value: Double): Double = value * revolution

  // ------------------------------------------------------
  // Length
  // ------------------------------------------------------

  val nanometer = 1E-9
  val micrometer = 1E-6
  val millimeter = 1E-3
  val centimeter = 1E-2
  val decimeter = 1E-1
  val meter = 1.0
  val kilometer = 1E3
  val inch = 0.0254
  val foot = 0.3048
  val yard = 0.9144
  val mile = 1609.344
  val nauticalMile = 1852.0
  /** Astronomical unit as defined by the IERS 2010 conventions (table 1.1) */
  val astronomicalUnit = 149597870700.0

  def AU = astronomicalUnit

  val lightYear = 9460730472580800.0
  val parsec = astronomicalUnit / arcSecond

  def nanometer(value: Double): Double = value * nanometer

  def nanometers(value: Double): Double = value * nanometer

  def micrometer(value: Double): Double = value * micrometer

  def micrometers(value: Double): Double = value * micrometer

  def millimeter(value: Double): Double = value * millimeter

  def millimeters(value: Double): Double = value * millimeter

  def centimeter(value: Double): Double = value * centimeter

  def centimeters(value: Double): Double = value * centimeter

  def decimeter(value: Double): Double = value * decimeter

  def decimeters(value: Double): Double = value * decimeter

  def meter(value: Double): Double = value

  def meters(value: Double): Double = value

  def kilometer(value: Double): Double = value * kilometer

  def kilometers(value: Double): Double = value * kilometer

  def inch(value: Double): Double = value * inch

  def inches(value: Double): Double = value * inch

  def foot(value: Double): Double = value * foot

  def feet(value: Double): Double = value * foot

  def yard(value: Double): Double = value * yard

  def yards(value: Double): Double = value * yard

  def mile(value: Double): Double = value * mile

  def miles(value: Double): Double = value * mile

  def nauticalMile(value: Double): Double = value * nauticalMile

  def nauticalMiles(value: Double): Double = value * nauticalMile

  def AU(value: Double): Double = value * astronomicalUnit

  def astronomicalUnit(value: Double): Double = value * astronomicalUnit

  def astronomicalUnits(value: Double): Double = value * astronomicalUnit

  def lightYear(value: Double): Double = value * lightYear

  def lightYears(value: Double): Double = value * lightYear

  def parsec(value: Double): Double = value * parsec

  def parsecs(value: Double): Double = value * parsec

  // ------------------------------------------------------
  // Time
  // ------------------------------------------------------

  val nanosecond = 1E-9
  val microsecond = 1E-6
  val millisecond = 1E-3
  val second = 1.0
  val minute = 60.0
  val hour = 3600.0
  val day = 86400.0
  val week = 168 * hour
  val year = 365.25 * day
  val leapYear = 366.25 * day
  val century = 100.0 * year
  val julianMinute = minute
  val julianDay = day
  val julianYear = year
  val julianCentury = century
  val solarMinute = minute
  val solarDay = day
  val solarYear = 31556930.0
  val siderealMinute = 59.83617
  val siderealDay = 86164.09
  val siderealYear = 31558150.0

  def nanosecond(value: Double): Double = value * nanosecond

  def nanoseconds(value: Double): Double = value * nanosecond

  def microsecond(value: Double): Double = value * microsecond

  def microseconds(value: Double): Double = value * microsecond

  def millisecond(value: Double): Double = value * millisecond

  def milliseconds(value: Double): Double = value * millisecond

  def second(value: Double): Double = value

  def seconds(value: Double): Double = value

  def minute(value: Double): Double = value * minute

  def minutes(value: Double): Double = value * minute

  def hour(value: Double): Double = value * hour

  def hours(value: Double): Double = value * hour

  def day(value: Double): Double = value * day

  def days(value: Double): Double = value * day

  def week(value: Double): Double = value * week

  def weeks(value: Double): Double = value * week

  def year(value: Double): Double = value * year

  def years(value: Double): Double = value * year

  def leapYear(value: Double): Double = value * leapYear

  def leapYears(value: Double): Double = value * leapYear

  def century(value: Double): Double = value * century

  def centuries(value: Double): Double = value * century

  def julianMinute(value: Double): Double = value * julianMinute

  def julianMinutes(value: Double): Double = value * julianMinute

  def julianDay(value: Double): Double = value * julianDay

  def julianDays(value: Double): Double = value * julianDay

  def julianYear(value: Double): Double = value * julianYear

  def julianYears(value: Double): Double = value * julianYear

  def julianCentury(value: Double): Double = value * julianCentury

  def julianCenturies(value: Double): Double = value * julianCentury

  def solarMinute(value: Double): Double = value * solarMinute

  def solarMinutes(value: Double): Double = value * solarMinute

  def solarDay(value: Double): Double = value * solarDay

  def solarDays(value: Double): Double = value * solarDay

  def solarYear(value: Double): Double = value * solarYear

  def solarYears(value: Double): Double = value * solarYear

  def siderealMinute(value: Double): Double = value * siderealMinute

  def siderealMinutes(value: Double): Double = value * siderealMinute

  def siderealDay(value: Double): Double = value * siderealDay

  def siderealDays(value: Double): Double = value * siderealDay

  def siderealYear(value: Double): Double = value * siderealYear

  def siderealYears(value: Double): Double = value * siderealYear

}
