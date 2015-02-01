package be.angelcorp.celest.math

object Sequences {

  def sequence[T](start: T)(f: T => T) = Iterator.iterate(start)(f)

}
