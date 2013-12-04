/**
 * Copyright (C) 2013 Simon Billemont <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package be.angelcorp.celest.body;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

public abstract class CelestialBodyAnnotations {

    /**
     * Annotation for retrieving the planet Mercury from the Universe
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface Mercury {
    }

    /**
     * Annotation for retrieving the planet Venus from the Universe
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface Venus {
    }

    /**
     * Annotation for retrieving the planet Earth from the Universe
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface Earth {
    }

    /**
     * Annotation for retrieving the Moon from the Universe
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface Moon {
    }

    /**
     * Annotation for retrieving the Earth-Moon barycenter from the Universe
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface EarthMoonBarycenter {
    }

    /**
     * Annotation for retrieving the planet Mars from the Universe
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface Mars {
    }

    /**
     * Annotation for retrieving the planet Jupiter from the Universe
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface Jupiter {
    }

    /**
     * Annotation for retrieving the planet Saturn from the Universe
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface Saturn {
    }

    /**
     * Annotation for retrieving the planet Uranus from the Universe
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface Uranus {
    }

    /**
     * Annotation for retrieving the planet Neptune from the Universe
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface Neptune {
    }

    /**
     * Annotation for retrieving the dwarf planet Pluto from the Universe
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface Pluto {
    }

    /**
     * Annotation for retrieving the Sun from the Universe
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface Sun {
    }

    /**
     * Annotation for retrieving the solar system barycenter from the Universe
     */
    @Qualifier
    @Documented
    @Retention(RUNTIME)
    public static @interface SolarSystemBarycenter {
    }

}
