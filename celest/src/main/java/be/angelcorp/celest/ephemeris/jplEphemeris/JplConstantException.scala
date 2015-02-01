package be.angelcorp.celest.ephemeris.jplEphemeris

class JplConstantException(message: String, cause: Throwable) extends Exception(message, cause)

object JplConstantException {

  def apply(tag: String) = new JplConstantException(s"Failed to find the tag '$tag' in the JPL ephemeris constants", null)

}