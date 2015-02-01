package be.angelcorp.celest.resources

import be.angelcorp.celest.universe.Universe

import scala.util.Try

object Resources {

  def find( description: ResourceDescription )(implicit universe: Universe): Try[Resource] =
    universe.instance[ResourceResolver].find( description )

  def findArchive( description: ResourceDescription )(implicit universe: Universe): Try[ArchiveResource] =
    universe.instance[ResourceResolver].findArchive( description )

}
