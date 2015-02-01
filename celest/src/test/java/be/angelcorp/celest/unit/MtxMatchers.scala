package be.angelcorp.celest.unit

import org.scalactic.Prettifier
import org.scalatest.matchers.{BeMatcher, MatchResult, Matcher}

import spire.algebra._
import spire.math._
import spire.implicits._ // provides infix operators, instances and conversions

/**
 * Matchers for testing matrices
 */
trait MtxMatchers {

  private val wasPlusOrMinus    = "{0} was {1} plus or minus {2}"
  private val wasNotPlusOrMinus = "{0} was not {1} plus or minus {2}"

  trait MtxSpread[T] {
    def pivot: T
    def tolerance: T
    def isWithin(n: T): Boolean
    override def toString: String = Prettifier.default(pivot) + " +- " + Prettifier.default(tolerance)
  }

  class MtxSpreadMatcher[T](spread: MtxSpread[T]) extends BeMatcher[T] {
    def apply(left: T) = {
      MatchResult(
        spread.isWithin(left),
        wasNotPlusOrMinus,
        wasPlusOrMinus,
        Vector(left, spread.pivot, spread.tolerance)
      )
    }
  }


  case class Vec3Spread(pivot: Array[Double], tolerance: Array[Double]) extends MtxSpread[Array[Double]] {
    {
      implicit val ev = CoordinateSpace.array[Double](3)
    require(tolerance._x >= 0 && tolerance._y >= 0 && tolerance._z >= 0, "tolerance elements must be zero or greater, but was " + tolerance)
    }

    private val max = pivot + tolerance
    private val min = pivot - tolerance
    def isWithin(vector: Array[Double]): Boolean = {
      val error = vector - pivot
      implicit val ev = CoordinateSpace.array[Double](3)
      val xerr = abs(error._x) < tolerance._x
      val yerr = abs(error._y) < tolerance._y
      val zerr = abs(error._z) < tolerance._z
      xerr && yerr && zerr
    }
  }
  implicit class Vec3Pivot( pivot: Array[Double] ) {
    def +-( tolerance: Double ): MtxSpreadMatcher[Array[Double]] = this +- Array[Double](tolerance, tolerance, tolerance) // FIXME
    def +-( tolerance: Array[Double] ):   MtxSpreadMatcher[Array[Double]] = new MtxSpreadMatcher( new Vec3Spread(pivot, tolerance) )
  }

}
