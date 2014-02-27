
#include <sofa.h>

#include "../utilities.h"
#include <iomanip>
#include "sofam.h"

/**
 * Generates the results used in the following test cases;
 * - 
 */
void precessionNutationPolarMotion() {
    std::cout << std::setprecision(16);

    double epoch_utc_jd, epoch_utc_fraction;         // UTC
    iauDtf2d("UTC",  2013, 04, 6, 7, 51, 28.386009, &epoch_utc_jd, &epoch_utc_fraction);
    
    double dut1  = -0.439962; // s
    double dat   = 32;        // s
    double lod   =  0.001556; // s
    double xp    = -0.140682; // "
    double yp    =  0.333309; // "
    double ddpsi = -0.052195;
    double ddeps = -0.003875;
    double dx    = -0.000199;
    double dy    = -0.000252;
}
