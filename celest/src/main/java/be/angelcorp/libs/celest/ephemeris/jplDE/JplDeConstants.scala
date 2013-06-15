package be.angelcorp.libs.celest.ephemeris.jplDE

object JplDeConstants {

	val MERCURY = 0
	val VENUS = 1
	val EARTH = 2
	/* Earth-Moon Barycenter */
	val MARS = 3
	val JUPITER = 4
	val SATURN = 5
	val URANUS = 6
	val NEPTUNE = 7
	val PLUTO = 8
	val MOON = 9
	/* Relative to geocenter */
	val SUN = 10
	val SS_BARY = 11
	val EM_BARY = 12
	val NUTATIONS = 13
	val LIBRATIONS = 14

	val INTERNAL_Mercury = 0
	val INTERNAL_Venus = 1
	val INTERNAL_Earth_Moon_barycenter = 2
	val INTERNAL_Mars = 3
	val INTERNAL_Jupiter = 4
	val INTERNAL_Saturn = 5
	val INTERNAL_Uranus = 6
	val INTERNAL_Neptune = 7
	val INTERNAL_Pluto = 8
	val INTERNAL_Moon_geocentric = 9
	val INTERNAL_Sun = 10
	val INTERNAL_Nutations = 11
	val INTERNAL_Librations = 12

	val EPHEMERIS_NR = "DENUM"
	// Planetary ephemeris number
	val LUNAR_EPHEMERIS_NR = "LENUM"
	//Lunar ephemeris number"
	val FORWARD_INTEGRATION_DATE = "TDATEF"
	// Date of the Forward Integration
	val BACKWARD_INTEGRATION_DATE = "TDATEB"
	// Date of the Backward Integration
	val SPEED_OF_LIGHT = "CLIGHT"
	// Speed of light (km/s).
	val AU = "AU"
	// Number of kilometers per astronomical unit.
	val EARTH_MOON_MASS_RATIO = "EMRAT"
	// Earth-Moon mass ratio.
	val GM_MERCURY = "GM1"
	// GM for 1st planet [au^3/day^2].
	val GM_VENUS = "GM2"
	// GM for 2nd planet [au^3/day^2].
	val GM_EARTH = "GM3"
	// GM for 3rd planet [au^3/day^2].
	val GM_MARS = "GM4"
	// GM for 4th planet [au^3/day^2].
	val GM_JUPITER = "GM5"
	// GM for 5th planet [au^3/day^2].
	val GM_SATURN = "GM6"
	// GM for 6th planet [au^3/day^2].
	val GM_URANUS = "GM7"
	// GM for 7th planet [au^3/day^2].
	val GM_NEPTUNE = "GM8"
	// GM for 8th planet [au^3/day^2].
	val GM_PLUTO = "GM9"
	// GM for 9th planet [au^3/day^2].
	val GM_EARTH_MOON = "GMB"
	//GM for the Earth-Moon Barycenter [au**3/day**2].
	val GM_SUN = "GMS"
	// Sun (= k**2) [au**3/day**2].
	val EPOCH = "JDEPOC"
	// Epoch (JED) of initial conditions, normally JED 2440400.5.
	val CENTER = "CENTER"
	// Reference center for the initial conditions. (Sun: 11,  Solar System Barycenter: 12)
	val LUNAR_PHASE_ANGLE = "PHASE"
	// The phase angle of the moon's rotation.
	val LUNAR_LOVE_NR = "LOVENO"
	// The Love Number, k2, for the moon.
	val RADIUS_MERCURY = "RAD1"
	// Radius of 1st planet [km].
	val RADIUS_VENUS = "RAD2"
	// Radius of 2nd planet [km].
	val RADIUS_EARTH = "RAD3"
	// Radius of 3rd planet [km].
	val RADIUS_MARS = "RAD4"
	// Radius of 4th planet [km].
	val RADIUS_JUPITER = "RAD5"
	// Radius of 5th planet [km].
	val RADIUS_SATURN = "RAD6"
	// Radius of 6th planet [km].
	val RADIUS_URANUS = "RAD7"
	// Radius of 7th planet [km].
	val RADIUS_NEPTUNE = "RAD8"
	// Radius of 8th planet [km].
	val RADIUS_PLUTO = "RAD9" // Radius of 9th planet [km].
	//  X1...DZ9        Initial conditions for the numerical integration, given at "JDEPOC", with respect to "CENTER".
	//  MA0001...MA0324 GM's of asteroid number 0001 ... 0234 [au**3/day**2].
	//  XL...ZDL        Initial conditions of the libration angles.

}
