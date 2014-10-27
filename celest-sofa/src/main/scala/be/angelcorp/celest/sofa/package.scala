package be.angelcorp.celest

import be.angelcorp.celest.math.geometry.Mat3
import org.bridj.Pointer

package object sofa {

  /**
   * This a __mutable__ `Matrix3D` that is backed by a 3x3 BridJ c-array.
   * It serves as a bridge between the Sofa representation of matrix, and that of celest.
   * Transforming this matrix into an immutable form can be done via the `result` method.
   *
   * @param mtx 3x3 c backing array.
   */
  implicit class SofaMatrix( val mtx: Pointer[Pointer[java.lang.Double]] ) {
    def this() = this( Pointer.allocateDoubles(3,3) )
    def m00: Double = mtx(0)(0)
    def m01: Double = mtx(0)(1)
    def m02: Double = mtx(0)(2)
    def m10: Double = mtx(1)(0)
    def m11: Double = mtx(1)(1)
    def m12: Double = mtx(1)(2)
    def m20: Double = mtx(2)(0)
    def m21: Double = mtx(2)(1)
    def m22: Double = mtx(2)(2)
    /** Create an immutable copy of this Matrix3D */
    def result: Mat3 = Mat3( m00, m01, m02, m10, m11, m12, m20, m21, m22 )
  }
  object SofaMatrix {
    /** Get the single pointer view of a sofa matrix */
    implicit def to1DArray( sMtx: SofaMatrix ) = sMtx.mtx.get
  }

  implicit def cdouble2double( value: Pointer[Double] ): Double = value.get

}
