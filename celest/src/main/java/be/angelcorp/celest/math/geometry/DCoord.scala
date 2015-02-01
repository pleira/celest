package be.angelcorp.celest.math.geometry


import spire.algebra._   // provides algebraic type classes
import spire.math._      // provides functions, types, and type classes
import spire.implicits._ // provides infix operators, instances and conversions

object DCoord {

  def zero = Array[Double](0, 0, 0)
  def ones = Array[Double](1, 1, 1)

  def x = Array[Double](1, 0, 0)
  def y = Array[Double](0, 1, 0)
  def z = Array[Double](0, 0, 1)
     
}