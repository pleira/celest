package be.angelcorp.celest.universe.modules

import javax.inject.Singleton
import be.angelcorp.celest.resources.aether.AetherInterface
import com.google.inject.{Provides, AbstractModule}
import org.eclipse.aether.artifact.DefaultArtifact
import be.angelcorp.celest.ephemeris.jplEphemeris.JplEphemeris
import be.angelcorp.celest.ephemeris.jplEphemeris
import be.angelcorp.celest.universe.Universe
import be.angelcorp.celest.frameGraph.frames.ICRS

class DefaultJplEphemeris(deNumber: Int, groupId: String, artifactId: String, extension: String = "bin") extends AbstractModule {
  def configure() {}

  @Provides
  @Singleton
  def getEphemeris(aether: AetherInterface)(implicit universe: Universe): JplEphemeris[ICRS] = {
    val artifact = new DefaultArtifact(groupId, artifactId, extension, "[0,)")
    val resolvedArtifact = aether.resolveLatest(artifact).head
    jplEphemeris.fromBinary(resolvedArtifact.getFile.toPath, deNumber)
  }

}
