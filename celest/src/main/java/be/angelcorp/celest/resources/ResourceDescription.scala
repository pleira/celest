package be.angelcorp.celest.resources

trait ResourceDescription {

  /**
   * The group identifier of this resource, for example "org.apache.maven".
   */
  def groupId: String

  /**
   * The artifact identifier of this resource, for example "maven-model".
   */
  def artifactId: String

  /**
   * The version of this resource, for example "1.0-20100529-1213".
   *
   * Note that in case of meta versions like "1.0-SNAPSHOT", the artifact's version depends on the state of the resource.
   * Resources that have been resolved or deployed will usually have the meta version expanded.
   */
  def version: String


  /**
   * The classifier of this resource, for example "sources".
   */
  def classifier: String

  /**
   * The file extension of this resource, for example "jar" or "tar.gz".
   *
   * Note: The file extension is without leading period.
   */
  def extension: String

}

case class DefaultResourceDescription( groupId: String, artifactId: String, version: String = "[0,)", extension: String, classifier: String = "" ) extends ResourceDescription

object ResourceDescription {

  def apply( groupId: String, artifactId: String, version: String = "[0,)", extension: String, classifier: String = "" ) =
    new DefaultResourceDescription( groupId, artifactId, version, extension, classifier )

}
