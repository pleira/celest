package be.angelcorp.celest.resources

class ResourceNotFound( msg: String, cause: Throwable = null ) extends RuntimeException(msg, cause) {

  def this( description: ResourceDescription ) =
    this( s"The resource $description was not found in the repository system." )

}
