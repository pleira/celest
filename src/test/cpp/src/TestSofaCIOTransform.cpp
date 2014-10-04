
#include "sofa.h"
#include "util.h"

namespace TestSofaCIOTransform {

    /**
     * Based on "A Demonstration of SOFA's Earth Orientation Routines" by Catherine Hohenkerk (HM Nautical Almanac Office, UK)
     * Online: http://syrte.obspm.fr/journees2013/index.php?index=tutorial
     *         http://syrte.obspm.fr/journees2013/powerpoint/Tutorial_SOFA_Demo_jsr13.tar
     */
    void run() {
        /* Civil date, UTC */ 
        int iy = 2013;
        int im = 9;
        int id = 15;
        int ihr = 17;
        int imn = 30;
        double sec = 0e0;

        /* IERS CIP offsets wrt IAU 2006/2000A (radians) */
        double dx = -0.2e-6 * DAS2R;  
        double dy = -0.1e-6*  DAS2R;   

        /* IERS polar motion, coordinates of CIP wrt ITRS (radians)  */
        double xp = +0.1574e0 * DAS2R;   
        double yp = +0.3076e0 * DAS2R;  

        /* UT1-UTC (seconds)  */
        double dut = +0.02792e0; 

        double  utc1, utc2, tai1, tai2, tt1, tt2, ut1a, ut1b;
        iauDtf2d ( "UTC", iy, im, id, ihr, imn, sec, &utc1, &utc2 );
        iauUtctai ( utc1, utc2, &tai1, &tai2 );
        iauTaitt  ( tai1, tai2, &tt1,  &tt2);
        iauUtcut1 ( utc1, utc2, dut, &ut1a, &ut1b);

        { // The actual test
            printf("Test epoch: (%.15f, %.17f) TT\n", tt1, tt2);
            double mtx[3][3];
            iauC2t06a ( tt1, tt2, ut1a, ut1b, xp, yp, mtx );
            printMatrix( "Transformation matrix (GCRF TO ITRF matrix):", mtx );
        }
    }

}
