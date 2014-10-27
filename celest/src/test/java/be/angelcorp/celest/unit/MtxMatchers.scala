package be.angelcorp.celest.unit

import be.angelcorp.celest.math.geometry.Vec3
import org.scalactic.Prettifier
import org.scalatest.matchers.{BeMatcher, MatchResult, Matcher}

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


  case class Vec3Spread(pivot: Vec3, tolerance: Vec3) extends MtxSpread[Vec3] {
    require(tolerance.x >= 0 && tolerance.y >= 0 && tolerance.z >= 0, "tolerance elements must be zero or greater, but was " + tolerance)
    private val max = pivot + tolerance
    private val min = pivot - tolerance
    def isWithin(vector: Vec3): Boolean = {
      val error = vector - pivot
      val xerr = math.abs(error.x) < tolerance.x
      val yerr = math.abs(error.y) < tolerance.y
      val zerr = math.abs(error.z) < tolerance.z
      xerr && yerr && zerr
    }
  }
  implicit class Vec3Pivot( pivot: Vec3 ) {
    def +-( tolerance: Double ): MtxSpreadMatcher[Vec3] = this +- Vec3(tolerance)
    def +-( tolerance: Vec3 ):   MtxSpreadMatcher[Vec3] = new MtxSpreadMatcher( new Vec3Spread(pivot, tolerance) )
  }

}
