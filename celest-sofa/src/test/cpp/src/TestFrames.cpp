
#include "sofa.h"
#include "util.h"

/**
 * Based on "Implementation Issues Surrounding the New IAU Reference Systems for Astrodynamics" 
 *   by DAVID A. VALLADO, JOHN H. SEAGO and P. KENNETH SEIDELMANN
 */
namespace TestFrames {
    
    void runLEO() {
        double dut1 = -0.439962;    // s
        double dat  =  32;          // s 
        double lod  =  0.001556;    // s
        double xp   = -0.140682 * DAS2R; // rad 
        double yp   =  0.333309 * DAS2R; // rad
        double ddpsi= -0.052195 * DAS2R; // rad
        double ddeps= -0.003875 * DAS2R; // rad
        double dx   = -0.000199 * DAS2R; // rad
        double dy   = -0.000252 * DAS2R; // rad

        double  utc1, utc2, tai1, tai2, tt1, tt2, ut1a, ut1b;
        iauDtf2d ( "UTC", 2004, 4, 6, 7, 51, 28.386009, &utc1, &utc2 );
        iauUtctai ( utc1, utc2, &tai1, &tai2 );
        iauTaitt  ( tai1, tai2, &tt1,  &tt2 );
        iauUtcut1 ( utc1, utc2, dut1, &ut1a, &ut1b );
        
        double p_itrf[] = {-1033.4793830, 7901.2952754, 6380.3565958 };
        double v_itrf[] = {-3.225636520,  -2.872451450, 5.531924446  };

        double p_tirf[3], v_tirf[3];

        double tirf_itrf[3][3], itrf_tirf[3][3];
        double sp = iauSp00 ( tt1, tt2 );
        iauPom00 ( xp, yp, sp, tirf_itrf );
        iauTr(tirf_itrf, itrf_tirf);
        iauRxp(itrf_tirf, p_itrf, p_tirf);
        iauRxp(itrf_tirf, v_itrf, v_tirf);
        printMatrix("ITRF => TIRF:", itrf_tirf);
        printVector( "Position TIRF:", p_tirf );
        printVector( "Velocity TIRF:", v_tirf );
        // TIRS should be -1033.4750312 7901.3055856 6380.3445328 -3.225632747 -2.872442511 5.531931288

        double era = iauEra00 ( ut1a, ut1b );
        
        //Vector3D( 5100.0184047, 6122.7863648, 6380.3445328), Vector3D(-4.745380330,  0.790341453, 5.531931288)
        
    }

    void runGEO() {
//         June 1, 2004, 0:00:0.000000 UTC, dut1 -0.470905 s dat 32 s lod 0.000000 s
//         xp -0.083853 yp 0.467217, ddpsi -0.053614 ddeps -0.004494, dx -0.000199 dy -0.000252
    }
    
    void run() {
        printf("Running LEO test case:\n");
        runLEO();
        printf("Running GEO test case:\n");
        runGEO();
    }

}
