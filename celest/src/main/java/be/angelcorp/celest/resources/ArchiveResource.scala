package be.angelcorp.celest.resources

import java.nio.file.Path

import scala.util.Try

/**
 * Resource containing archive of other files/resources. For example a zip/jar/tar/... resource file.
 */
trait ArchiveResource extends Resource {

  def entries: Iterator[String]

  def findEntry( path: String ): Try[Resource]

}

object ArchiveResource {

  def apply( res: PathResource ): Option[ArchiveResource] =
    ArchiveResource(res.path)

  def apply( path: Path ): Option[ArchiveResource] = {
    val fname = path.toFile.getName
    if ( fname.endsWith(".zip") || fname.endsWith(".jar") ) {
      Some( new ZipResource( path ) )
    } else {
      None
    }
  }

}
