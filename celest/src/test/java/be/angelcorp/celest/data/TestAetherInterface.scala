package be.angelcorp.celest.data

import java.nio.file.Files

import be.angelcorp.celest.resources.aether.AetherInterface
import org.apache.maven.repository.internal.MavenRepositorySystemUtils
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory
import org.eclipse.aether.repository.RemoteRepository
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory
import org.eclipse.aether.spi.connector.transport.TransporterFactory
import org.eclipse.aether.transport.file.FileTransporterFactory
import org.eclipse.aether.transport.http.HttpTransporterFactory
import org.scalatest.{FlatSpec, Matchers}

class TestAetherInterface extends FlatSpec with Matchers {

  "AetherInterface" should "find and load artifacts successfully" in {
    // This is noramlly done via the universe guice plugin
    val remotes = Seq(
      new RemoteRepository.Builder("resources", "default", "http://angelcorp.be/celest/resources").build()
    )
    val local = Files.createTempDirectory("celestTestRepo")
    val locator = MavenRepositorySystemUtils.newServiceLocator()
    locator.addService(classOf[RepositoryConnectorFactory], classOf[BasicRepositoryConnectorFactory])
    locator.addService(classOf[TransporterFactory], classOf[FileTransporterFactory])
    locator.addService(classOf[TransporterFactory], classOf[HttpTransporterFactory])
    val system = locator.getService(classOf[RepositorySystem])

    val aether = new AetherInterface(remotes, local, system)

    val results = aether.resolve(new DefaultArtifact("be.angelcorp.celest.resources.test.data", "testdata", "zip", "1"))
    results.size should be(1)
    val result = results.head
    val zipfile = new java.util.zip.ZipFile(result.getFile)
    import scala.collection.JavaConverters._
    val entries = zipfile.entries.asScala.toList
    entries.size should be(2)
    entries.map(_.getName) should contain("file1.txt")
    entries.map(_.getName) should contain("file2.txt")
  }


}
