import be.angelcorp.celest.frameGraph.frames.{ITRS, EME2000, GCRS}
import be.angelcorp.celest.frameGraph.{ReferenceFrameTransformFactory, ReferenceFrameTransform, ReferenceFrameGraph}
import be.angelcorp.celest.state.{Orbit, Keplerian}

implicit val universe = new be.angelcorp.celest.universe.DefaultUniverse

// This is a reference to the graph describing all the reference frame transformations
// The specifics of how this is created/retrieved are handles by the universe
val frameGraph = universe.instance[ReferenceFrameGraph]

// A starting orbit
val gcrf  = universe.instance[GCRS] // The system in which our orbit will be defined 
val epoch = be.angelcorp.celest.time.Epochs.J2000 // Epoch on which the instantanious orbit parameters are valid
val k_gcrf: Keplerian[GCRS] = new Keplerian(7000E3, 0.1, 0, 0, 0, 0, gcrf)

// Note that at this point we have a set of kepler elements in GCRS.
// This can only be manipulated within this system.
// Changing the system can be done as follows:
val j2000 = universe.instance[EME2000]
  val transformation: Option[ReferenceFrameTransform[GCRS, EME2000]] = frameGraph.getTransform(gcrf, j2000, epoch)
val pv_j2000: Orbit[EME2000] = transformation match {
  case Some( t: ReferenceFrameTransform[GCRS, EME2000] ) => t.transform( k_gcrf )
  case _ => throw new RuntimeException("No transformation was found from the GCRS to EME2000 system!")
}

// One can also transform over multiple frame-graph links at once:
val pv_itrf: Orbit[ITRS] = frameGraph.getTransform(gcrf, universe.instance[ITRS], epoch).get.transform( k_gcrf )

// If you require the same transformation numerous number of times, it is more efficient to first resolve the
// transformation path, and afterwards instantiate multiple transformations from that path:
val transformationFactory: ReferenceFrameTransformFactory[GCRS, ITRS] =
    frameGraph.getTransformFactory( gcrf, universe.instance[ITRS] ).get

// Now transformations for many different epochs can be retrieved without the requirement to look at the framegraph again
val transforms: Seq[ReferenceFrameTransform[GCRS, ITRS]] =
    // Get 10 transforms, on {start epoch, epoch + 1 day, epoch + 2 days, ..., epoch + 10 days}
    for ( e <- epoch to epoch + 10) yield {
      transformationFactory.transform( e )
    }