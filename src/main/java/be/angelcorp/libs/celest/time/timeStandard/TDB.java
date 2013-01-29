package be.angelcorp.libs.celest.time.timeStandard;

import javax.annotation.concurrent.Immutable;
import javax.inject.Singleton;

import be.angelcorp.libs.celest.time.IJulianDate;
import be.angelcorp.libs.celest.time.JulianDate;
import be.angelcorp.libs.util.physics.Time;

/**
 * Barycentric Dynamical Time.
 *
 * <p>
 * For most purposes, one may neglect the difference of less than 2 msec between Barycentric Dynamical
 * Time (TDB) and Terrestrial Time (TT). (Fränz and Harper, <b>"Heliospheric Coordinate Systems"</b>)
 * </p>
 *
 * <p>
 * Conversions based on:<br>
 * [1] D. Vallado et al. ,
 * <b>"Implementation Issues Surrounding the New IAU Reference Systems for Astrodynamics"</b>, 16th
 * AAS/AIAA Space Flight Mechanics Conference, Florida, January 2006
 * </p>
 *
 * @author Simon Billemont
 */
@Immutable
@Singleton
public class TDB implements ITimeStandard {

	/** TDB singleton instance */
    @Deprecated
	private static TDB					instance	= new TDB();

	/** Get the TDB singleton instance */
    @Deprecated
    public static TDB get() {
		return instance;
	}

    @Override
	public double offsetFromTT(IJulianDate JD_tt) {
        // [1] equation 29
        double g  = 357.53 + 0.98560028 * JD_tt.relativeTo( JulianDate.J2000_EPOCH ); // deg
        double g_rad = g * Math.PI / 180.0;
        double TDB = 0.001658 * Math.sin(g_rad) + 0.000014 * Math.sin(2*g_rad);

        return TDB;
	}

	@Override
	public double offsetToTT(IJulianDate JD_tdb) {
		// TODO: More accurate implementation, either rootfinding or an analytical form
		return -offsetFromTT(JD_tdb);
	}
}
