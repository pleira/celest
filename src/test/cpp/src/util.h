
#include <stdio.h>

/** Arcseconds to radians */
#define DAS2R (4.848136811095359935899141e-6)

/** Print matrix R */
inline void printMatrix( char text[], double r[3][3] ) {
    printf ( "%s\n", text );
    printf ( "     %+19.15f %+19.15f %+19.15f\n", r[0][0], r[0][1], r[0][2] );
    printf ( "     %+19.15f %+19.15f %+19.15f\n", r[1][0], r[1][1], r[1][2] );
    printf ( "     %+19.15f %+19.15f %+19.15f\n", r[2][0], r[2][1], r[2][2] );
}

/** Print matrix R */
inline void printVector( char text[], double v[3] ) {
    printf ( "%s\n", text );
    printf ( "     %+19.15f %+19.15f %+19.15f\n", v[0], v[1], v[2] );
}

namespace TestFrames                        { void run(); }
namespace TestSofaCelestialPoleTransform    { void run(); }
namespace TestSofaCIOTransform              { void run(); }
namespace TestSofaEarthRotation             { void run(); }
namespace TestSofaPolarMotion               { void run(); }
