package be.angelcorp.celest.time.timeStandard;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This class contains a set of annotations that can be used to retrieve a specific time standard from the universe injector.
 */
public abstract class TimeStandardAnnotations {

    /**
     * Terrestrial Time (TT)
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface TT {
    }

    /**
     * International Atomic Time (TAI)
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface TAI {
    }

    /**
     * Terrestrial Dynamical Time (TDT, is now TT)
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface TDT {
    }

    /**
     * Barycentric Coordinate Time (TCB)
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface TCB {
    }

    /**
     * Geocentric Coordinate Time (TCG)
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface TCG {
    }

    /**
     * Barycentric Dynamical Time (TDB)
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface TDB {
    }

    /**
     * Coordinated Universal Time (UTC)
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface UTC {
    }

    /**
     * Universal Time (UT1)
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface UT1 {
    }

    /**
     * GPS Time (GPS)
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface GPS {
    }

}
