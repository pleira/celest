package be.angelcorp.celest.frameGraph.frames

import be.angelcorp.celest.frameGraph.ReferenceSystem

/**
 * The international celestial reference system (ICRS). Its center is located at the solar system barycenter.
 *
 * The ICRS constitutes the set of prescriptions and conventions together with the modelling required to define at any
 * time a triad of axes. The System is realised by VLBI estimates of equatorial coordinates of a set of extragalactic
 * compact radio sources, the International Celestial Reference Frame (ICRF). The ICRS can be connected to the
 * International Terrestrial Reference System (ITRS) by use of the IERS Earth Orientation Parameters (EOP).
 *
 * IERS documenation see: http://www.iers.org/nn_11478/IERS/EN/DataProducts/ICRF/icrf.html
 *
 * See frame graph documentation.
 */
trait ICRS extends ReferenceSystem with PseudoInertialSystem
