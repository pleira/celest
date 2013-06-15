package be.angelcorp.libs.celest.math

object Sequences {

	def sequence[T](start: T)(f: T => T) = Iterator.iterate(start)(f)

}
