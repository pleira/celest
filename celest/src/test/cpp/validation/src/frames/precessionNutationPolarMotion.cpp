
#include <sofa.h>

#include "../utilities.h"
#include <iomanip>
#include "sofam.h"

/**
 * Generates the results used in the following test cases;
 * - TestPolarMotion
 * - TestEarthRotation
 * - TestIAU2000Nutation
 * - TestIAU2000NutationEntry
 * - TestJ2000FrameBias
 * - TestIAU2006Precession
 */
void precessionNutationPolarMotion() {
    std::cout << std::setprecision(16);

    double epoch_jd, epoch_fraction;         // TT
    iauDtf2d("TT",  2013, 04, 27, 12, 33, 18.1938271, &epoch_jd, &epoch_fraction);
    double epoch_ut1_jd       = epoch_jd;
    double epoch_ut1_fraction = epoch_fraction - (67.0675098/86400.);
    std::cout << "Epoch: 2013/04/27 12h33m18.1938271s TT  = " << (epoch_jd     + epoch_fraction    ) << "jd TT"  << std::endl; 
    std::cout << " == " << (epoch_ut1_jd + epoch_ut1_fraction) << "jd UT1" << std::endl; 

    /************************************************************************/
    /* Precession                                                           */
    /************************************************************************/
    double   epsilon_0, psi_A, omega_A, P_A, Q_A, pi_A, Pi_A, epsilon_A, chi_A, z_A, zeta_A, theta_A, p_A;
    double gam, phi, psi;
    iauP06e(epoch_jd, epoch_fraction,
            &epsilon_0, &psi_A, &omega_A, &P_A, &Q_A, &pi_A, &Pi_A, &epsilon_A, &chi_A, &z_A, &zeta_A, &theta_A, &p_A,
            &gam, &phi, &psi);
    
    std::cout << "IAU2006 nutation epsilon_0: " << epsilon_0 << std::endl;
    std::cout << "IAU2006 nutation psi_A:     " << psi_A     << std::endl;
    std::cout << "IAU2006 nutation omega_A:   " << omega_A   << std::endl;
    std::cout << "IAU2006 nutation P_A:       " << P_A       << std::endl;
    std::cout << "IAU2006 nutation Q_A:       " << Q_A       << std::endl;
    std::cout << "IAU2006 nutation pi_A:      " << pi_A      << std::endl;
    std::cout << "IAU2006 nutation Pi_A:      " << Pi_A      << std::endl;
    std::cout << "IAU2006 nutation epsilon_A: " << epsilon_A << std::endl;
    std::cout << "IAU2006 nutation chi_A:     " << chi_A     << std::endl;
    std::cout << "IAU2006 nutation z_A:       " << z_A       << std::endl;
    std::cout << "IAU2006 nutation zeta_A:    " << zeta_A    << std::endl;
    std::cout << "IAU2006 nutation theta_A:   " << theta_A   << std::endl;
    std::cout << "IAU2006 nutation p_A:       " << p_A       << std::endl;
    std::cout << "IAU2006 nutation gam:       " << gam       << std::endl;
    std::cout << "IAU2006 nutation phi:       " << phi       << std::endl;
    std::cout << "IAU2006 nutation psi:       " << psi       << std::endl;

    /* Precession matrix: mean of date to J2000.0. */
    double P[3][3];
    iauIr(P);
    iauRz(-chi_A,     P);
    iauRx(+omega_A,   P);
    iauRz(+psi_A,     P);
    iauRx(-epsilon_0, P);
    std::cout << "IAU2000a N:" << std::endl;
    printMatrix(P);

    /************************************************************************/
    /* Nutation                                                             */
    /************************************************************************/
    // IAU2000A
    double dpsi, deps;
    iauNut00a(epoch_jd, epoch_fraction, &dpsi, &deps);
    std::cout << "IAU2000a dpsi:" << dpsi << std::endl;
    std::cout << "IAU2000a deps:" << deps << std::endl;

    double N[3][3];
    iauNum00a(epoch_jd, epoch_fraction, N);
    std::cout << "IAU2000a N:" << std::endl;
    printMatrix(N);

    // IAU2006A
    iauNut06a(epoch_jd, epoch_fraction, &dpsi, &deps);
    std::cout << "IAU2006a dpsi:" << dpsi << std::endl;
    std::cout << "IAU2006a deps:" << deps << std::endl;

    iauNum06a(epoch_jd, epoch_fraction, N);
    std::cout << "IAU2006a N:" << std::endl;
    printMatrix(N);

    /************************************************************************/
    /* Bias                                                                 */
    /************************************************************************/
    
    double rb[3][3], rp[3][3], rbp[3][3];
    iauBp00(epoch_jd, epoch_fraction, rb, rp, rbp);
    std::cout << "J2000 Bias:" << std::endl;
    printMatrix(rb);

    /************************************************************************/
    /* Equinox based gst rotation angle                                     */
    /************************************************************************/

    double era = iauEra00(epoch_ut1_jd, epoch_ut1_fraction);
    std::cout << "Earth rotation angle [rad]:" << era << std::endl;

    double eoe = iauEe00a(epoch_jd, epoch_fraction);
    std::cout << "EquationOfEquinoxes [rad]:" << eoe << std::endl;
    
    double r_gst[3][3];
    double gst = iauGst00a(epoch_ut1_jd, epoch_ut1_fraction, epoch_jd, epoch_fraction);
    std::cout << "Theta GAST [rad]:" << gst << std::endl;
    
    iauIr(r_gst);
    iauRz(-gst, r_gst);
    std::cout << "R_GST matrix:" << std::endl;
    printMatrix(r_gst);

    /************************************************************************/
    /* Polar motion                                                         */
    /************************************************************************/

    double W[3][3];
    double xp = 100 * DMAS2R; // = 100 [mas]
    double yp = 200 * DMAS2R; // = 200 [mas]
    iauPom00( xp, yp, 0.0, W );

    std::cout << "W (polar motion xp=100mas yp=200mas) matrix:" << std::endl;
    printMatrix(W);

}
