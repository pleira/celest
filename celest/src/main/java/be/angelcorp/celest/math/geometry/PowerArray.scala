package be.angelcorp.celest.math.geometry


import spire.algebra._   // provides algebraic type classes
import spire.math._      // provides functions, types, and type classes
import spire.implicits._ // provides infix operators, instances and conversions

class PowerArray(val w: Array[Double]) {
  
  implicit val ev = CoordinateSpace.array[Double](3)
  
  def elemProd(v: Array[Double]) =
      Array[Double]( w._x*v._x, w._y*v._y, w._z*v._z )
      
  def *(v: Array[Double]) =
      elemProd(v)

  def angle(v2: Array[Double]): Double = {
    val normProduct = w.norm * v2.norm
    if (normProduct == 0)
      throw new ArithmeticException("Cannot calculate angle between vectos, dot product is 0.")

    val dot = w dot v2
    val threshold = normProduct * 0.9999
    if ((dot < -threshold) || (dot > threshold)) {
      val v3 = this cross v2
      if (dot >= 0)
        asin(v3.norm / normProduct)
      else
        pi - asin(v3.norm / normProduct)
    } else
      acos(dot / normProduct)
  }

   def cross(v: Array[Double]) =  {
    Array[Double]( 
       w._y * v._z - v._y * w._z,
       w._z * v._x - v._z * w._x,
       w._x * v._y - v._x * w._y)
  }

  def normSq = w.norm * w.norm
   
   
}

object PowerArray {
  implicit def arrayToPowerArray(a: Array[Double]) = new PowerArray(a)

  def spherical(azimuth: Double, elevation: Double, radius: Double = 1): Array[Double] = {
    val cosDelta = cos(elevation)
    Array[Double](
      radius * cos(azimuth) * cosDelta,
      radius * sin(azimuth) * cosDelta,
      radius * sin(elevation)
    )
  }
  
}