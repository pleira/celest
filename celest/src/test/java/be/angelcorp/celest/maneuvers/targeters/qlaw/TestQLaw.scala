package be.angelcorp.celest.maneuvers.targeters.qlaw

import be.angelcorp.celest.math.geometry.Vec3

import scala.math._
import be.angelcorp.celest.state.{TrueAnomaly, Keplerian}
import be.angelcorp.celest.universe.DefaultUniverse
import be.angelcorp.celest.frameGraph.frames.{BodyCenteredSystem, GCRF}
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator
import org.apache.commons.math3.ode.{ContinuousOutputModel, FirstOrderDifferentialEquations}
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.time.EpochAnnotations.J2000

object TestQLaw extends App {

  implicit val universe = new DefaultUniverse
  val frame = universe.instance[GCRF]

  val k0 = new Keplerian( 7000E3, 0.01, 0.1, 0 ,0 ,0, frame )
  val t0 = universe.instance[Epoch, J2000]
  val kt = new Keplerian( 8000E3, 0.50, 0.1, 0 ,0 ,0, frame )

  val qlaw = new QLaw(kt, Wa = 1, We = 0.1)

  val result = integrate[GCRF](qlaw.acceleration, t0, k0, 1000.0, t0 + 2938800.0 / 86400.0 )

  (result.t0 until result.tf by 1.0/24).foreach( epoch => {
    val (k, m) = result(epoch)
    println( s"${epoch.jd}, $m, ${k.a}, ${k.e}, ${k.i}, ${k.ω}, ${k.Ω}, ${k.ν}" )
  } )


  def integrate[F <: BodyCenteredSystem]( acceleration: ((Epoch, Keplerian[F], Double) => Vec3), t0: Epoch, k0: Keplerian[F], m0: Double, tf: Epoch, Isp: Double = 300 ) = {
    val g0 = 9.80665
    def toArray( k: Keplerian[F], m: Double, array: Array[Double] = null ) = {
      val y = if (array == null) Array.ofDim[Double](7) else array
      y(0) = k.a
      y(1) = k.e
      y(2) = k.i
      y(3) = k.ω
      y(4) = k.Ω
      y(5) = k.ν
      y(6) = m
      y
    }
    def fromArray( y: Array[Double] = null ) = {
      val k = Keplerian(y(0), y(1), y(2), y(3), y(4), TrueAnomaly( y(5) ), k0.frame)
      val m = y(6)
      (k, m)
    }
    val eom = new FirstOrderDifferentialEquations {
      override def computeDerivatives(dt: Double, y: Array[Double], yDot: Array[Double]) {
        val t = t0 + dt / 86400.0
        val (k, m) = fromArray(y)
        import k._ // import the symbols a/e/i/ω/Ω
        import k.quantities._ // import the symbols p/h/sini/...

        val acc  = acceleration(t, k, m)
        val ar = acc.x
        val as = acc.y
        val aw = acc.z

        yDot(0) /*da/dt*/ = (2*(a*a)/h) * ( e * sinν * ar + (p/radius) * as )
        yDot(1) /*de/dt*/ = (1/h) * ( p * sinν * ar + ( (p+ radius) * cosν + radius * e ) * as )
        yDot(2) /*di/dt*/ = ((radius * cosu) / h) * aw
        yDot(3) /*dω/dt*/ = (1/(e*h)) * ( -p * cosν * ar + (p + radius) * sinν * as ) - ((radius * sinu * cosi)/(h * sini)) * aw
        yDot(4) /*dΩ/dt*/ = ((radius * sinu)/(h * sini)) * aw
        yDot(5) /*dν/dt*/ = h/pow(radius,2) + (1/(e * h))*( p * cosν * ar - (p + radius) * sinν * as )
        yDot(6) /*dm/dt*/ = -(acc.norm * m) / (g0 * Isp)
      }
      override val getDimension = 7
    }

    val output = new ContinuousOutputModel()
    val rk4 = new ClassicalRungeKuttaIntegrator(60.0)
    rk4.addStepHandler(output)
//    rk4.addStepHandler(new StepHandler {
//      override def handleStep(interpolator: StepInterpolator, isLast: Boolean) {
//        println( interpolator.getCurrentTime + " => " + interpolator.getInterpolatedState.toList )
//      }
//      override def init(t0: Double, y0: Array[Double], t: Double) {}
//    })

    val y0 = toArray(k0, m0)
    val y  = Array.ofDim[Double]( y0.length )

    rk4.integrate(eom, 0.0, y0, tf relativeToS t0, y )

    val t00 = t0
    new IntegrationOutput[F] {
      implicit def toEpoch( dt: Double) = t00 + dt / 86400.0
      implicit def toDouble( t: Epoch ) = t relativeToS t0
      override val t0: Epoch = output.getInitialTime
      override val tf: Epoch = output.getFinalTime
      override val steps = Nil //TODO
      override def apply(t: Epoch) = {
        output.setInterpolatedTime( t )
        val y = output.getInterpolatedState()
        fromArray( y )
      }
    }
  }

  trait IntegrationOutput[F <: BodyCenteredSystem] {

    val t0: Epoch
    val tf: Epoch

    val steps: Seq[Epoch]
    
    def apply( t: Epoch ): (Keplerian[F], Double)
  }

}
