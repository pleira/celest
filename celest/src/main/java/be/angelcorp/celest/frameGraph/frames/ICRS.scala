package be.angelcorp.celest.frameGraph.frames

import be.angelcorp.celest.frameGraph.ReferenceSystem

/**
 * The international celestial reference system (ICRS). Its center is located at the solar system barycenter.
 *
 * See frame graph documentation.
 */
trait ICRS extends ReferenceSystem with PseudoInertialSystem
