package be.angelcorp.libs.celest.frameGraph.systems

import be.angelcorp.libs.celest.frameGraph.ReferenceFrame

/**
 * The international celestial reference system (ICRS). Its center is located at the solar system barycenter.
 *
 * See frame graph documentation.
 */
trait ICRS extends ReferenceFrame with PseudoInertialSystem
