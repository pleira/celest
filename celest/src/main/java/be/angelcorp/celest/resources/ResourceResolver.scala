package be.angelcorp.celest.resources

import scala.util.Try

trait ResourceResolver {

  /**
   * Finds, resolves, and returns the requested resource as an [[ArchiveResource]].
   * If the resource was not found, of if it could not be retrieved, a Failure is returned.
   *
   * @param description Description of th resource to resolve.
   */
  def find( description: ResourceDescription ): Try[Resource]

  /**
   * Finds, resolves, and returns the requested resource as an [[ArchiveResource]].
   * If the resource was not found, it could not be retrieved, or if it was not an archive, a Failure is returned.
   *
   * @param description Description of th resource to resolve.
   */
  def findArchive( description: ResourceDescription ): Try[ArchiveResource]

}
