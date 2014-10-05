package be.angelcorp.celest.sofa

import org.bridj.Pointer
import org.scalatest.{FlatSpec, Matchers}

class TestPackage extends FlatSpec with Matchers {

  "the sofa package" should "correctly use sofa matrices" in {
    val m = Pointer.allocateDoubles(3,3)
    m(0)(0) = 0
    m(0)(1) = 1
    m(0)(2) = 2
    m(1)(0) = 3
    m(1)(1) = 4
    m(1)(2) = 5
    m(2)(0) = 6
    m(2)(1) = 7
    m(2)(2) = 8

    val sofaMtx = new SofaMatrix(m)
    sofaMtx.m00 should equal (m(0)(0))
    sofaMtx.m01 should equal (m(0)(1))
    sofaMtx.m02 should equal (m(0)(2))
    sofaMtx.m10 should equal (m(1)(0))
    sofaMtx.m11 should equal (m(1)(1))
    sofaMtx.m12 should equal (m(1)(2))
    sofaMtx.m20 should equal (m(2)(0))
    sofaMtx.m21 should equal (m(2)(1))
    sofaMtx.m22 should equal (m(2)(2))
  }


}
