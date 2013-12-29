package be.angelcorp.celest.frameGraph.frames

import be.angelcorp.celest.frameGraph.ReferenceSystem

/**
 * A reference system which is for which the the non-inertial effects are small enough not to be required for accurate
 * orbit modelling. This effectively means that orbits should only be defined in a PseudoInertialSystem.
 */
trait PseudoInertialSystem extends ReferenceSystem
