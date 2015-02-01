package be.angelcorp.celest.unit

import be.angelcorp.celest.math.geometry.Mat3
import be.angelcorp.celest.math.rotation.{RotationMatrix, Rotation}
import org.scalactic.Prettifier
import org.scalatest.Matchers.{ResultOfBeWordForAny, AnyShouldWrapper}
import org.scalatest.matchers.{Matcher, MatchResult, BeMatcher}

/**
 * Checks the equality of two rotations (Quaterions, Matrices, Axis-angle, ...)
 *
 * Syntax:
 * (rot: Mat3|Rotation) should be rotation ( (rot: Rotation|Mat3)  +- (tolerance: Double [rad]) )
 */
trait RotationMatchers {

  implicit class RotationBeWord[T <: Rotation]( be: ResultOfBeWordForAny[T] ) {
    def rotation( matcher: RotationSpreadMatcher ): RotationWord = new RotationWord(matcher)
  }

  class RotationWord(matcher: RotationSpreadMatcher) extends BeMatcher[Rotation] {
    override def apply(left: Rotation): MatchResult = matcher.apply(left)
  }



  class RotationSpread( val expected: Rotation, val tolerance: Double ) {
    require(tolerance >= 0, "tolerance must be zero or greater, but was " + tolerance)
    def isWithin(actual: Rotation): Boolean = {
      val error = actual.toQuaternion distanceTo expected.toQuaternion
      math.abs(error) < tolerance
    }
    override def toString: String = Prettifier.default(expected) + " +- " + Prettifier.default(tolerance)
  }
  class RotationSpreadMatcher(spread: RotationSpread) extends Matcher[Rotation] {
    def apply(left: Rotation) = {
      MatchResult(
        spread.isWithin(left),
        s"Rotation $left was not ${spread.expected} plus or minus ${spread.tolerance} rad",
        s"Rotation $left was ${spread.expected} plus or minus ${spread.tolerance} rad"
      )
    }
  }
  implicit class AnglePivot( pivot: Rotation ) {
    def +-( tolerance: Double ) = new RotationSpreadMatcher( new RotationSpread(pivot, tolerance) )
  }
  implicit def mtx2rot(mtx: Mat3): AnyShouldWrapper[RotationMatrix] = new AnyShouldWrapper(new RotationMatrix(mtx))
  implicit def mtx2pivot(mtx: Mat3): AnglePivot = new AnglePivot(new RotationMatrix(mtx))


}
