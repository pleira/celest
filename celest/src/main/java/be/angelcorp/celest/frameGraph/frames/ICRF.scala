package be.angelcorp.celest.frameGraph.frames

/**
 * The International Celestial Reference Frame (ICRF) realizes an ideal reference system, the International Celestial
 * Reference System ([[be.angelcorp.celest.frameGraph.frames.ICRS]]), by precise equatorial coordinates of extragalactic
 * radio sources observed in Very Long Baseline Interferometry (VLBI) programmes.
 *
 * IERS: http://www.iers.org/nn_11256/IERS/EN/DataProducts/ICRF/ICRF/icrf.html
 */
case class ICRF1() extends ICRS

/**
 * Extension of [[be.angelcorp.celest.frameGraph.frames.ICRF1]] with updated and additional sources:
 *
 * The ICRF maintenance and extension catalogue ICRF-Ext.1 contains updated coordinates of the 608 ICRF extragalactic
 * radio sources and of 59 new sources. The data added to the ICRF spanned December 1994 through April 1999 from both
 * geodetic and astrometric observing programs.
 *
 * IERS: http://www.iers.org/nn_11256/IERS/EN/DataProducts/ICRF/ICRFExt/icrf__ext.html
 */
case class ICRF1Ext1() extends ICRS

/**
 * Extension of [[be.angelcorp.celest.frameGraph.frames.ICRF1Ext1]] with updated and additional sources:
 *
 * The ICRF maintenance and extension catalogue ICRF-Ext.2 contains updated coordinates of the 608 ICRF extragalactic
 * radio sources and of 109 new sources. VLBI data obtained between 1999 May and the end of 2002 May together with
 * older data were used.
 *
 * IERS: http://www.iers.org/nn_11256/IERS/EN/DataProducts/ICRF/ICRFExt/icrf__ext.html
 */
case class ICRF1Ext2() extends ICRS

/**
 * The International Celestial Reference Frame (ICRF) realizes an ideal reference system, the International Celestial
 * Reference System ([[be.angelcorp.celest.frameGraph.frames.ICRS]]), by precise equatorial coordinates of extragalactic
 * radio sources observed in Very Long Baseline Interferometry (VLBI) programmes.
 *
 * This implementation is based on the equatorial coordinates of 3414 extragalactic radio sources in J2000.0 observed with VLBI.
 *
 * IERS: http://www.iers.org/nn_11256/IERS/EN/DataProducts/ICRF/ICRF2/icrf2.html
 */
case class ICRF2() extends ICRS
