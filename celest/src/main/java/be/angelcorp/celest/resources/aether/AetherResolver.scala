package be.angelcorp.celest.resources.aether

import java.util.zip.ZipFile
import javax.inject.Inject

import be.angelcorp.celest.resources._
import org.eclipse.aether.artifact.DefaultArtifact

import scala.collection.JavaConverters._
import scala.io.Source
import scala.util.{Failure, Success, Try}

class AetherResolver@Inject()(val aether: AetherInterface) extends ResourceResolver {


  /**
   * Resolves the given dependency, downloads it to the local repository if it is not present, and returns a list
   * containing the artifact with all its transient dependencies.
   *
   * This function searches for a specific version in the given repositories.
   *
   * For more information visit LINKY
   *
   * @param groupId    Group ID of the root artifact.
   * @param artifactId Artifact ID of the root artifact.
   * @param extension  Extension of the artifact to retrieve.
   * @param version    Specific version of the artifact to resolve.
   * @return A list of the resolved & fetched artifact with its dependencies.
   */
  def getArtifacts(groupId: String, artifactId: String, extension: String, version: String) = {
    val artifact = new DefaultArtifact(groupId, artifactId, extension, version)
    aether.resolve(artifact)
  }

  /**
   * Resolves the given dependency, downloads it to the local repository if it is not present, and returns a list
   * containing the artifact with all its transient dependencies.
   *
   * This function searches for the latest available version available in the given repository. This is done based on
   * a version pattern, for more information see [[org.eclipse.aether.util.version.GenericVersionScheme]].
   *
   * For more information visit LINKY
   *
   * @param groupId    Group ID of the root artifact.
   * @param artifactId Artifact ID of the root artifact.
   * @param extension  Extension of the artifact to retrieve.
   * @param version    Versioning pattern.
   * @return A list of the resolved & fetched artifact with its dependencies.
   */
  def getLatestArtifacts(groupId: String, artifactId: String, extension: String, version: String = "[0,)") = {
    val artifact = new DefaultArtifact(groupId, artifactId, extension, version)
    aether.resolveLatest(artifact)
  }

  /**
   * Resolves the given dependency, downloads it to the local repository if it is not present, and returns the path
   * to the artifact.
   *
   * This function searches for a specific version in the given repositories.
   *
   * For more information visit LINKY
   *
   * @param groupId    Group ID of the root artifact.
   * @param artifactId Artifact ID of the root artifact.
   * @param extension  Extension of the artifact to retrieve.
   * @param version    Specific version of the artifact to resolve.
   * @return The path to the root artifact.
   */
  def getPath(groupId: String, artifactId: String, extension: String, version: String) =
    getArtifacts(groupId, artifactId, extension, version).head.getFile.toPath

  /**
   * Resolves the given dependency, downloads it to the local repository if it is not present, and returns the path
   * to the artifact.
   *
   * This function searches for the latest available version available in the given repository. This only works for
   * purely numerical verioning schemes.
   *
   * For more information visit LINKY
   *
   * @param groupId    Group ID of the root artifact.
   * @param artifactId Artifact ID of the root artifact.
   * @param extension  Extension of the artifact to retrieve.
   * @return The path to the root artifact.
   */
  def getPath(groupId: String, artifactId: String, extension: String) =
    getLatestArtifacts(groupId, artifactId, extension).head.getFile.toPath

  /**
   * Resolves the given zip dependency, downloads it to the local repository if it is not present, and returns the
   * the artifact as a [[java.util.zip.ZipFile]].
   *
   * This function searches for a specific version in the given repositories.
   *
   * For more information visit LINKY
   *
   * @param groupId    Group ID of the root artifact.
   * @param artifactId Artifact ID of the root artifact.
   * @param version    Specific version of the artifact to resolve.
   * @return Zipfile of the root artifact.
   */
  def getZip(groupId: String, artifactId: String, version: String) =
    getArtifacts(groupId, artifactId, "zip", version).headOption.map(a => new ZipFile(a.getFile))

  /**
   * Resolves the given zip dependency, downloads it to the local repository if it is not present, and returns the
   * the artifact as a [[java.util.zip.ZipFile]].
   *
   * This function searches for the latest available version available in the given repository. This only works for
   * purely numerical verioning schemes.
   *
   * For more information visit LINKY
   *
   * @param groupId    Group ID of the root artifact.
   * @param artifactId Artifact ID of the root artifact.
   * @return Zipfile of the root artifact.
   */
  def getZip(groupId: String, artifactId: String) =
    getLatestArtifacts(groupId, artifactId, "zip").headOption.map(a => new ZipFile(a.getFile))

  /**
   * Resolves the given zip dependency, downloads it to the local repository if it is not present, and returns an
   * optional [[java.io.InputStream]] to a specific entry in that zip file and None if it is not found.
   *
   * This function searches for a specific version in the given repositories.
   *
   * For more information visit LINKY
   *
   * @param groupId    Group ID of the root artifact.
   * @param artifactId Artifact ID of the root artifact.
   * @param version    Specific version of the artifact to resolve.
   * @param filename   Filename of the file to open.
   * @return Input stream for a specific file in the artifact zipfile.
   */
  def getZipEntryStream(groupId: String, artifactId: String, version: String, filename: String) = {
    getZip(groupId, artifactId, version).flatMap( zip =>
        zip.entries().asScala.find(_.getName == filename).map {
          case zipEntry => zip.getInputStream(zipEntry)
        }
    )
  }

  /**
   * Resolves the given zip dependency, downloads it to the local repository if it is not present, and returns an
   * optional [[java.io.InputStream]] to a specific entry in that zip file and None if it is not found.
   *
   * This function searches for the latest available version available in the given repository. This only works for
   * purely numerical verioning schemes.
   *
   * For more information visit LINKY
   *
   * @param groupId    Group ID of the root artifact.
   * @param artifactId Artifact ID of the root artifact.
   * @param filename   Filename of the file to open.
   * @return Input stream for a specific file in the artifact zipfile.
   */
  def getZipEntryStream(groupId: String, artifactId: String, filename: String) = {
    getZip(groupId, artifactId).flatMap {
      case zip =>
        zip.entries().asScala.find(_.getName == filename).map {
          case zipEntry => zip.getInputStream(zipEntry)
        }
    }
  }

  /**
   * Resolves the given zip dependency, downloads it to the local repository if it is not present, and returns an
   * optional [[scala.io.Source]] to a specific entry in that zip file and None if it is not found.
   *
   * This function searches for a specific version in the given repositories.
   *
   * For more information visit LINKY
   *
   * @param groupId    Group ID of the root artifact.
   * @param artifactId Artifact ID of the root artifact.
   * @param version    Specific version of the artifact to resolve.
   * @param filename   Filename of the file to open.
   * @return Source stream for a specific file in the artifact zipfile.
   */
  def getZipEntrySource(groupId: String, artifactId: String, version: String, filename: String) =
    getZipEntryStream(groupId, artifactId, version, filename).map(stream => Source.fromInputStream(stream))

  /**
   * Resolves the given zip dependency, downloads it to the local repository if it is not present, and returns an
   * optional [[scala.io.Source]] to a specific entry in that zip file and None if it is not found.
   *
   * This function searches for the latest available version available in the given repository. This only works for
   * purely numerical verioning schemes.
   *
   * For more information visit LINKY
   *
   * @param groupId    Group ID of the root artifact.
   * @param artifactId Artifact ID of the root artifact.
   * @param filename   Filename of the file to open.
   * @return Source stream for a specific file in the artifact zipfile.
   */
  def getZipEntrySource(groupId: String, artifactId: String, filename: String) =
    getZipEntryStream(groupId, artifactId, filename).map(stream => Source.fromInputStream(stream))

  override def find(description: ResourceDescription): Try[PathResource] = {
    val artifact = new DefaultArtifact(description.groupId, description.artifactId, description.classifier, description.extension, description.version)
    Try( {
      val resolved = aether.resolve(artifact)
      new PathResource( resolved.head.getFile.toPath )
    } )
  }

  override def findArchive(description: ResourceDescription): Try[ArchiveResource] = {
    find( description ).flatMap( res => ArchiveResource( res ) match {
      case Some( r ) => Success( r )
      case _ => Failure( new Exception(s"Resource '$res' described by '$description' is not an resource archive!") )
    } )
  }

}
