package be.angelcorp.celest.resources.aether

import java.nio.file.Path
import javax.inject.Named

import com.google.inject.Inject
import org.apache.maven.repository.internal.MavenRepositorySystemUtils
import org.eclipse.aether.artifact.Artifact
import org.eclipse.aether.collection.CollectRequest
import org.eclipse.aether.graph.{Dependency, DependencyFilter}
import org.eclipse.aether.repository.{LocalRepository, RemoteRepository}
import org.eclipse.aether.resolution._
import org.eclipse.aether.util.artifact.JavaScopes
import org.eclipse.aether.util.filter.DependencyFilterUtils
import org.eclipse.aether.{RepositoryException, RepositorySystem, RepositorySystemSession}
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

/**
 * Resolves and fetches resources artifacts from remote repositories and downloads the required artifacts and its
 * dependencies to the local repository.
 */
class AetherInterface @Inject()(val remotes: Seq[RemoteRepository],
                                @Named("local_repository") val localRepository: Path,
                                val system: RepositorySystem) {
  private val logger = LoggerFactory.getLogger(getClass)

  /**
   * List of the resolved artifact, and its transitive dependencies (downloaded to the local repository).
   *
   * @param root  The artifact to work with.
   * @param scope The scope to work with ("runtime", "test", etc.).
   * @return The list of the artifact and its dependencies.
   */
  def resolve(root: Artifact, scope: String = JavaScopes.RUNTIME): List[Artifact] =
    DependencyFilterUtils.classpathFilter(scope) match {
      case null => throw new FilterException(s"failed to create a filter for '$scope'")
      case filter => resolve(root, scope, filter)
    }

  /**
   * List of the latest available version of the artifact, and its transitive dependencies (downloaded to the local repository).
   *
   * @param root  The artifact to work with.
   * @param scope The scope to work with ("runtime", "test", etc.).
   * @return The list of the artifact and its dependencies.
   */
  def resolveLatest(root: Artifact, scope: String = JavaScopes.RUNTIME): List[Artifact] =
    DependencyFilterUtils.classpathFilter(scope) match {
      case null => throw new FilterException(s"failed to create a filter for '$scope'")
      case filter => resolveLatest(root, scope, filter)
    }

  /**
   * Resolves an artifact and its dependencies (downloaded to the local repository).
   *
   * @param root   The artifact to work with.
   * @param scope  The scope to work with ("runtime", "test", etc.).
   * @param filter The filter for the dependencies.
   * @return The list of the artifact and its dependencies.
   */
  def resolve(root: Artifact, scope: String, filter: DependencyFilter): List[Artifact] = {
    val rdep = new Dependency(root, scope)
    val crq = request(rdep)
    fetch(session, new DependencyRequest(crq, filter))
  }

  /**
   * List of the latest available version of the artifact, and its dependencies (downloaded to the local repository).
   *
   * @param root   The artifact to work with.
   * @param scope  The scope to work with ("runtime", "test", etc.).
   * @param filter The filter for the dependencies.
   * @return The list of the artifact and its dependencies.
   */
  def resolveLatest(root: Artifact, scope: String, filter: DependencyFilter): List[Artifact] = {
    val vrq = versionRequest(root)

    root.setVersion(localRepository.synchronized {
      val versionRange = system.resolveVersionRange(session, vrq)
      versionRange.getHighestVersion.toString
    })

    val rdep = new Dependency(root, scope)
    val crq = request(rdep)
    fetch(session, new DependencyRequest(crq, filter))
  }

  /**
   * Fetch required dependencies.
   *
   * @param session The session.
   * @param dreq    Dependency request.
   * @return The list of the artifact and its dependencies.
   */
  def fetch(session: RepositorySystemSession, dreq: DependencyRequest) = {
    try {
      val results = localRepository.synchronized {
        system.resolveDependencies(session, dreq).getArtifactResults
      }.asScala
      results.map(_.getArtifact).toList
    } catch {
      case ex: DependencyResolutionException =>
        val repoString = dreq.getCollectRequest.getRepositories.asScala.map(repo => {
          val auth = repo.getAuthentication
          repo.toString + (if (auth == null) " without authentication" else " with " + auth.toString)
        })
        val cause = new IllegalArgumentException(s"Failed to load '${dreq.getCollectRequest.getRoot}' from '$repoString' into ${session.getLocalRepositoryManager.getRepository.getBasedir}", ex)
        throw new DependencyResolutionException(new DependencyResult(dreq), cause)
    }
  }

  /** Create a collect request for an artifact. */
  private def request(root: Dependency) = {
    val request = new CollectRequest
    request.setRoot(root)
    remotes.foreach(remote => request.addRepository(remote))
    request
  }

  /** Create a versioning request for an artifact. */
  private def versionRequest(artifact: Artifact) = {
    val request = new VersionRangeRequest
    request.setArtifact(artifact)
    remotes.foreach(remote => request.addRepository(remote))
    request
  }

  /** Create RepositorySystemSession. */
  private val session = {
    val session = MavenRepositorySystemUtils.newSession()
    val localRepo = new LocalRepository(localRepository.toFile)
    session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo))
    session
  }

}

class FilterException(msg: String) extends RepositoryException(msg, null)