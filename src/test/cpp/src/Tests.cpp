
#include "util.h"

#define TEST( name ) \
    printf("\n\n----------------------------------------\n"#name"\n----------------------------------------\n"); \
    name::run();

int main() {

    TEST( TestFrames );
//  TEST( TestSofaCelestialPoleTransform );
//  TEST( TestSofaCIOTransform );
// 	TEST( TestSofaEarthRotation );
// 	TEST( TestSofaPolarMotion );

    return 0;
}


