
#include <utility>
#include <fstream>
#include <iomanip>

#include <sofa.h>

#include "../utilities.h"

double mjd0 = 2400000.5;
double ut1_dates[][2] = {
	// MJD    UT1-UTC [s]
	{ 41322, -0.0602134 }, // 1972   1   6 // TT - UT1 = ~45s
	{ 41323, -0.0634283 }, // 1972   1   7 // TT - UT1 = ~45s
	{ 41324, -0.0665972 }, // 1972   1   8 // TT - UT1 = ~45s
	{ 47537, -0.1266927 }, // 1989   1  11 // TT - UT1 = ~56s
	{ 56364,  0.1876517 }  // 2013   3  13 // TT - UT1 = ~67s
};

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




double TT2UT1(double utc_jd, double utc_fraction, double dt) {
	double tai_jd, tai_fraction;
	iauUtctai(utc_jd, utc_fraction, &tai_jd, &tai_fraction);
	
	double tt_jd, tt_fraction;
    iauTaitt(tai_jd, tai_fraction, &tt_jd, &tt_fraction);
	
	double ut1_jd, ut1_fraction;
	iauTtut1(tt_jd, tt_fraction, dt, &ut1_jd, &ut1_fraction);
	
//    writeToFile( file, tt_jd, tt_fraction, "TT", ut1_jd, ut1_fraction, "UT1" );
    return 0.0;
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

	{ // TT <=> UT1
		TT2UT1( mjd0 + ut1_dates[0][0], + 0                , ut1_dates[0][1] ); 
		TT2UT1( mjd0 + ut1_dates[0][0], + 0.5 -(30./68400.), ut1_dates[0][1] );
		TT2UT1( mjd0 + ut1_dates[1][0], - 0.5              , ut1_dates[1][1] );
		TT2UT1( mjd0 + ut1_dates[1][0], - 0.5 +(30./68400.), ut1_dates[1][1] );
		TT2UT1( mjd0 + ut1_dates[1][0], + 0                , ut1_dates[1][1] );
		TT2UT1( mjd0 + ut1_dates[2][0], + 0                , ut1_dates[2][1] );
		TT2UT1( mjd0 + ut1_dates[3][0], + 0                , ut1_dates[3][1] );
		TT2UT1( mjd0 + ut1_dates[4][0], + 0                , ut1_dates[4][1] );
    }
	
	{ // TT <=> TDB
        double tdb_jd, tdb_fraction;

        //double dtr = iauDtdb ( tt1, tt2, ut, elon, u, v ); // TDB-TT

        //epoch = jd( 1960, 1, 1, 1, 1, 0.0);
        //iauTttdb(epoch.first, epoch.second, dtr, &tdb_jd, &tdb_fraction);
        //writeToFile( file, epoch.first, epoch.second, "TT", tdb_jd, tdb_fraction, "TDB" );

        std::cerr << "TT <=> TDB conversions not verified" << std::endl;
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
