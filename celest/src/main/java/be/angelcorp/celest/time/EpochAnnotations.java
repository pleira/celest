package be.angelcorp.celest.time;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This class contains a set of annotations that can be used to retrieve a specific epoch from the universe injector.
 */
public abstract class EpochAnnotations {

    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface J2000 {
    }

    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface J1950 {
    }

    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface J1900 {
    }

    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface B1950 {
    }

    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface TAI_EPOCH {
    }

    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface TT_EPOCH {
    }

    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface TCG_EPOCH {
    }

    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface TCB_EPOCH {
    }

    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface TDB_EPOCH {
    }

}
