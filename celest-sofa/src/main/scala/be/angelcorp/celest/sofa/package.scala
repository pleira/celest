package be.angelcorp.celest

import be.angelcorp.libs.math.linear.{ImmutableMatrix3D, Matrix3D}
import org.bridj.Pointer

package object sofa {

  /**
   * This a __mutable__ `Matrix3D` that is backed by a 3x3 BridJ c-array.
   * It serves as a bridge between the Sofa representation of matrix, and that of celest.
   * Transforming this matrix into an immutable form can be done via the `result` method.
   *
   * @param mtx 3x3 c backing array.
   */
  implicit class SofaMatrix( val mtx: Pointer[Pointer[java.lang.Double]] ) extends Matrix3D {
    def this() = this( Pointer.allocateDoubles(3,3) )
    override def m00: Double = mtx(0)(0)
    override def m01: Double = mtx(0)(1)
    override def m02: Double = mtx(0)(2)
    override def m10: Double = mtx(1)(0)
    override def m11: Double = mtx(1)(1)
    override def m12: Double = mtx(1)(2)
    override def m20: Double = mtx(2)(0)
    override def m21: Double = mtx(2)(1)
    override def m22: Double = mtx(2)(2)
    /** Create an immutable copy of this Matrix3D */
    def result: ImmutableMatrix3D = Matrix3D( m00, m01, m02, m10, m11, m12, m20, m21, m22 )
  }
  object SofaMatrix {
    /** Get the single pointer view of a sofa matrix */
    implicit def to1DArray( sMtx: SofaMatrix ) = sMtx.mtx.get
  }

  implicit def cdouble2double( value: Pointer[Double] ): Double = value.get

}
