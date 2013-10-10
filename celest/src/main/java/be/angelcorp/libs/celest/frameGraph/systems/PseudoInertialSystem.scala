package be.angelcorp.libs.celest.frameGraph.systems

import be.angelcorp.libs.celest.frameGraph.ReferenceFrame

/**
 * A reference system which is for which the the non-inertial effects are small enough not to be required for accurate
 * orbit modelling. This effectively means that orbits should only be defined in a PseudoInertialSystem.
 */
trait PseudoInertialSystem extends ReferenceFrame
