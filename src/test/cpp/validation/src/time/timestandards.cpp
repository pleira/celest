
#include <utility>
#include <fstream>
#include <iomanip>

#include <sofa.h>

#include "utilities.h"

std::pair<double, double> jd(int year, int month, int day, int hour, int min, double sec) {
    double mjd_epoch, mjd, fraction;
    iauCal2jd(year, month, day, &mjd_epoch, &mjd);
    double jd = mjd_epoch + mjd;
    iauTf2d( '+', hour, min, sec, &fraction );
    return std::pair< double, double >( jd, fraction );
}

void writeToFile( std::ostream& file, double jd1, double f1, const std::string& s1, double jd2, double f2, const std::string& s2 ) {
    file << s1 << ','  << jd1 << ','  << f1 << ','  << s2 << ','  << jd2 << ','  << f2 << std::endl;
}

void timestandards() {
    std::pair<double, double> epoch;

    std::ofstream file( base_directory() + "time/timestandards.csv" );
    file << "# This file lists a number of epochs in two different time standards.\n";
    file << "# timescale1, jdn1, fraction1, timescale2, jdn2, fraction2\n";
    file << std::setprecision(18);

    
    { // TT <=> TAI
        double tai_jd, tai_fraction;
        
        epoch = jd( 1960, 1, 1, 1, 1, 0.0);
        iauTttai(epoch.first, epoch.second, &tai_jd, &tai_fraction);
        writeToFile( file, epoch.first, epoch.second, "TT", tai_jd, tai_fraction, "TAI" );

        epoch = jd( 2010, 12, 15, 20, 59, 29.9);
        iauTttai(epoch.first, epoch.second, &tai_jd, &tai_fraction);
        writeToFile( file, epoch.first, epoch.second, "TT", tai_jd, tai_fraction, "TAI" );
    }

    { // TT <=> TCG
        double tcg_jd, tcg_fraction;

        epoch = jd( 1960, 1, 1, 1, 1, 0.0);
        iauTttcg(epoch.first, epoch.second, &tcg_jd, &tcg_fraction);
        writeToFile( file, epoch.first, epoch.second, "TT", tcg_jd, tcg_fraction, "TCG" );

        epoch = jd( 2000, 1, 1, 1, 1, 0.0);
        iauTttcg(epoch.first, epoch.second, &tcg_jd, &tcg_fraction);
        writeToFile( file, epoch.first, epoch.second, "TT", tcg_jd, tcg_fraction, "TCG" );

        epoch = jd( 2013, 4, 30, 20, 14, 47.951346);
        iauTttcg(epoch.first, epoch.second, &tcg_jd, &tcg_fraction);
        writeToFile( file, epoch.first, epoch.second, "TT", tcg_jd, tcg_fraction, "TCG" );
    }

    { // TT <=> TDB
        double tdb_jd, tdb_fraction;

        //double dtr = iauDtdb ( tt1, tt2, ut, elon, u, v ); // TDB-TT

        //epoch = jd( 1960, 1, 1, 1, 1, 0.0);
        //iauTttdb(epoch.first, epoch.second, dtr, &tdb_jd, &tdb_fraction);
        //writeToFile( file, epoch.first, epoch.second, "TT", tdb_jd, tdb_fraction, "TDB" );

        std::cerr << "TT <=> TDB conversions not verified" << std::endl;
    }

    { // TT <=> UT1
        std::cerr << "TT <=> UT1 conversions not verified" << std::endl;
    }

    { // TAI <=> UT1
        std::cerr << "TAI <=> UT1 conversions not verified" << std::endl;
    }

    { // TAI <=> UTC
        std::cerr << "TAI <=> UTC conversions not verified" << std::endl;
    }

    { // TCB <=> TDB
        std::cerr << "TCB <=> TDB conversions not verified" << std::endl;
    }

    { // UT1 <=> UTC
        std::cerr << "UT1 <=> UTC conversions not verified" << std::endl;
    }

}
