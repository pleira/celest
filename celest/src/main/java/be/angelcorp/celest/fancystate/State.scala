package be.angelcorp.celest.fancystate

import scala.collection.mutable
import be.angelcorp.celest.state.positionState.CartesianDerivative
import be.angelcorp.celest.state.PosVel
import be.angelcorp.celest.frameGraph.ReferenceSystem

/*
class StateBuilder {
  var end: Int = 0

  def allocate(length: Int) = {
    val offset = end
    end = end + length
    offset
  }

  def state = new ArrayState(end)

}

trait State extends mutable.ArrayOps[Double]

class ArrayState(val array: Array[Double]) extends State {
  def this(length: Int) = this(Array.fill(length)(0.0))

  protected[this] def newBuilder = Array.newBuilder[Double]

  def update(idx: Int, elem: Double) = array.update(idx, elem)

  def length = array.length

  def apply(idx: Int): Double = array(idx)

}

class SubState(val backend: State, val length: Int, val offset: Int) extends State {

  protected[this] def newBuilder = Array.newBuilder[Double]

  def update(idx: Int, elem: Double) = backend.update(idx + offset, elem)

  def apply(idx: Int): Double = backend(offset + idx)

}

class CartesianMapping[F <: ReferenceSystem](val offset: Int, val frame: F) extends StateMapping[PosVel[F]] with EmbeddedDerivative[CartesianDerivative] {

  def this(s: StateBuilder) = this(s.allocate(6))

  def apply(s: State) =
    PosVel(s(offset + 0), s(offset + 1), s(offset + 2), s(offset + 3), s(offset + 4), s(offset + 5), frame)

  def update(s: State, x: PosVel[F]) = {
    s(offset + 0) = x.position.x
    s(offset + 1) = x.position.y
    s(offset + 2) = x.position.z
    s(offset + 3) = x.velocity.x
    s(offset + 4) = x.velocity.y
    s(offset + 5) = x.velocity.z
  }

  val derivatives: StateMapping[CartesianDerivative] = new StateMapping[CartesianDerivative] {
    def update(s: State, x: CartesianDerivative) {
      s.update(0 + offset, s(0 + offset) + x.getV.getX)
      s.update(1 + offset, s(1 + offset) + x.getV.getY)
      s.update(2 + offset, s(2 + offset) + x.getV.getZ)
      s.update(3 + offset, s(3 + offset) + x.getA.getX)
      s.update(4 + offset, s(4 + offset) + x.getA.getY)
      s.update(5 + offset, s(5 + offset) + x.getA.getZ)
    }

    def apply(s: State) =
      new CartesianDerivative(s(offset + 0), s(offset + 1), s(offset + 2), s(offset + 3), s(offset + 4), s(offset + 5))
  }
}

trait RealMapping extends StateMapping[Double] {
  def offset: Int

  def apply(s: State) = s(offset)

  def update(s: State, x: Double) = s.update(offset, x)
}

class MassMapping(val offset: Int) extends RealMapping with EmbeddedDerivative[Double] {
  def this(s: StateBuilder) = this(s.allocate(1))

  val derivatives = new StateMapping[Double] {
    def update(s: State, x: Double) {
      s.update(offset, s(offset) + x)
    }

    def apply(s: State) = s(offset)
  }
}

//-------------------------------------------------------------------------------------------------

trait StateMapping[S] {

  def apply(s: State): S

  def update(s: State, x: S)

}

trait SubStateMapping[S] {

  protected def offset: Int

  protected def length: Int

  def subApply(s: State): S

  def subUpdate(s: State, x: S)

  def apply(s: State): S = subApply(new SubState(s, length, offset))

  def update(s: State, x: S) = subUpdate(new SubState(s, length, offset), x)

}

trait EmbeddedDerivative[DX] {
  def derivatives: StateMapping[DX]
}

//-------------------------------------------------------------------------------------------------

class MyState() extends ArrayState(7) {
  def this(pv: PosVel, m: Double) = {
    this()
    //orbitMapping.update(this, pv)
    massMapping.update(this, m)
  }

  val massMapping = MyState.massMapping

  def mass = massMapping(this)

  val orbitMapping = MyState.orbitMapping

  def orbit = orbitMapping(this)

  def mappings = List(orbitMapping, massMapping)
}

object MyState {
  val orbitMapping = new CartesianMapping(0, null)
  val massMapping = new MassMapping(6)

  implicit def myState2orbitMapping(myState: MyState) = myState.orbitMapping

  implicit def myState2massMapping(myState: MyState) = myState.massMapping
}

class PointPot(implicit orbit: CartesianMapping, mass: MassMapping) {

  def apply(t: Double, x: State, dx: State) {
    val mu = 3.9E14

    val m = mass(x)
    val pv = orbit(x)

    val r = pv.position
    val r3 = math.pow(r.norm, 3)
    val f = r * (mu * m / r3)
    val a = f / m

    orbit.derivatives.update(dx, new CartesianDerivative(pv.velocity, a))
  }

}

object Foo extends App {

  val state = new MyState(PosVel(10E6, 0, 0, 0, 8E3, 0), 1.0)
  val eqn = new PointPot()(state, state)

  val x = integrate(0.0, state, 1000, eqn)
  println(x)

  def integrate[T](t0: Double, x0: State, tf: Double, eqn: PointPot) = {
    val x = x0
    val t = t0

    val dx = new ArrayState(x0.length)
    eqn(t, x, dx)
    val xArr = x.zip(dx.array).map(z => {
      val x = z._1
      val dx = z._2
      x + dx * (tf - t0)
    })

    (tf, new ArrayState(xArr))
  }


}
*/