package be.angelcorp.celest.maneuvers.targeters.qlaw

import be.angelcorp.celest.state.Keplerian
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.frameGraph.frames.BodyCenteredSystem

class QLaw[F <: BodyCenteredSystem](
  target: Keplerian[F],
  force:  Double = 1,
  Wa: Double = 0,
  We: Double = 0,
  Wi: Double = 0,
  Wω: Double = 0,
  WΩ: Double = 0,
  Wp: Double = 0,
  kn: Double = 4,
  km: Double = 4,
  kr: Double = 4 ) {

  class QMath(val k: Keplerian[F], val mass: Double) extends QLawMath[F] {
    val acceleration = force / mass
    override def km: Double = QLaw.this.km
    override def kn: Double = QLaw.this.kn
    override def kr: Double = QLaw.this.kr
    override def Wa: Double = QLaw.this.Wa
    override def We: Double = QLaw.this.We
    override def Wi: Double = QLaw.this.Wi
    override def Wω: Double = QLaw.this.Wω
    override def WΩ: Double = QLaw.this.WΩ
    override def Wp: Double = QLaw.this.Wp
    override def aT: Double = QLaw.this.target.a
    override def eT: Double = QLaw.this.target.e
    override def iT: Double = QLaw.this.target.i
    override def ωT: Double = QLaw.this.target.ω
    override def ΩT: Double = QLaw.this.target.Ω
  }
  
  def acceleration( t: Epoch, k: Keplerian[F], m: Double ) = {
    val q = new QMath( k, m )
    q.accelerationVector
  }

}
