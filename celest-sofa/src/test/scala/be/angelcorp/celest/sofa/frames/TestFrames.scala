package be.angelcorp.celest.sofa.frames

import javax.inject.Singleton

import be.angelcorp.celest.data.eop.{EarthOrientationData, ExcessLengthOfDay, PoleProvider, UT1Provider}
import be.angelcorp.celest.frameGraph.ReferenceFrameGraph
import be.angelcorp.celest.frameGraph.frames._
import be.angelcorp.celest.math.geometry.Vec3
import be.angelcorp.celest.physics.Units._
import be.angelcorp.celest.sofa.time.SofaEpoch
import be.angelcorp.celest.sofa.universe.{SofaFrameTransformations, SofaTime}
import be.angelcorp.celest.state.PosVel
import be.angelcorp.celest.time.Epoch
import be.angelcorp.celest.time.timeStandard.TimeStandards._
import be.angelcorp.celest.universe.DefaultUniverseBuilder
import be.angelcorp.celest.universe.modules.GuiceReferenceFrameGraphProvider
import net.codingwell.scalaguice.ScalaModule
import org.scalatest.{FlatSpec, Matchers}

class TestFrames extends FlatSpec with Matchers {

  "sofa-celest" should "provide the correct frame transformations" in {
    val eop = new EarthOrientationData(null, null) {
      override def lod       = new ExcessLengthOfDay { override def lod(epoch: Epoch): Double = 0.001556 }
      override def ut1_utc   = new UT1Provider  { override def UT1_UTC(jd_utc: Epoch) = -0.439962 }
      override def cip       = new PoleProvider { override def polarCoordinatesOn(epoch: Epoch) = ( arcSeconds(-0.140682), arcSeconds( 0.333309 ) ) }
      override def cipOffset = new PoleProvider { override def polarCoordinatesOn(epoch: Epoch) = ( arcSeconds(-0.000199), arcSeconds(-0.0002529) ) }
    }
    implicit val universe = new DefaultUniverseBuilder with SofaTime with SofaFrameTransformations {
      modules += new ScalaModule {
        override def configure() {
          bind[EarthOrientationData].toInstance(eop)
          bind[ITRS].to[ITRF2000].in[Singleton]
          bind[TIRS].to[TIRF].in[Singleton]
          bind[CIRS].to[CIRF].in[Singleton]
          bind[GCRS].to[GCRF].in[Singleton]
          bind[ReferenceFrameGraph].toProvider[GuiceReferenceFrameGraphProvider].in(classOf[Singleton])
        }
      }
    }.result
    val epoch = SofaEpoch(2004, 4, 6, 7, 51, 28.386009, UTC)(TT)
    val frames = universe.instance[ReferenceFrameGraph]

    val pv_itrf = new PosVel(Vec3(-1033.4793830, 7901.2952754, 6380.3565958), Vec3(-3.225636520, -2.872451450, 5.531924446), universe.instance[ITRS])

    val itrf2trif = frames.getTransform( universe.instance[ITRS], universe.instance[TIRS], epoch ).get
    val pv_tirf = itrf2trif.transform( pv_itrf ).toPosVel
    checkPV( pv_tirf, 1E-7, 1E-9, Vec3(-1033.4750312, 7901.3055856, 6380.3445328), Vec3(-3.225632747, -2.872442511, 5.531931288) )

    val trif2cirf = frames.getTransform( universe.instance[TIRS], universe.instance[CIRS], epoch ).get
    val pv_cirf = trif2cirf.transform( pv_tirf ).toPosVel
    checkPV( pv_cirf, 1E-7, 1E-9, Vec3( 5100.0184047, 6122.7863648, 6380.3445328), Vec3(-4.745380330,  0.790341453, 5.531931288) )
  }

//  "ITRF",                Vec3(-1033.4793830, 7901.2952754, 6380.3565958), Vec3(-3.225636520, -2.872451450, 5.531924446)
//  "PEF iau76",           Vec3(-1033.4750313, 7901.3055856, 6380.3445328), Vec3(-3.225632747, -2.872442511, 5.531931288)
//  "TIRS",
//  "TOD iau76",           Vec3( 5094.5147804, 6127.3664612, 6380.3445328), Vec3(-4.746088567,  0.786077222, 5.531931288)
//  "ERS Eq",              Vec3( 5094.5146278, 6127.3665880, 6380.3445328), Vec3(-4.746088587,  0.786077104, 5.531931288)
//  "CIRS",
//  "MOD iau76",           Vec3( 5094.0290167, 6127.8709363, 6380.2478885), Vec3(-4.746262495,  0.786014149, 5.531791025)
//  "MOD iau76 w corr",    Vec3( 5094.0283745, 6127.8708164, 6380.2485164), Vec3(-4.746263052,  0.786014045, 5.531790562)
//  "J2000 iau76",         Vec3( 5102.5096000, 6123.0115200, 6378.1363000), Vec3(-4.743219600,  0.790536600, 5.533756190)
//  "J2000 Eq a",          Vec3( 5102.5090383, 6123.0119758, 6378.1363118), Vec3(-4.743219766,  0.790536344, 5.533756084)
//  "J2000 Eq b",          Vec3( 5102.5090383, 6123.0119733, 6378.1363142), Vec3(-4.743219766,  0.790536342, 5.533756085)
//  "CIRS",                Vec3( 5100.0184047, 6122.7863648, 6380.3445328), Vec3(-4.745380330,  0.790341453, 5.531931288)
//  "GCRF CIO based",      Vec3( 5102.5089530, 6123.0113955, 6378.1369371), Vec3(-4.743220161,  0.790536492, 5.533755724)
//  "GCRF CIO dx,dy=0",    Vec3( 5102.5089592, 6123.0114033, 6378.1369247), Vec3(-4.743220156,  0.790536498, 5.533755728)
//  "GCRF iau 2000a",      Vec3( 5102.5089579, 6123.0114038, 6378.1369252), Vec3(-4.743220156,  0.790536497, 5.533755728)
//  "GCRF iau 2000b",      Vec3( 5102.5089579, 6123.0114012, 6378.1369277), Vec3(-4.743220156,  0.790536495, 5.533755729)
//  "GCRF iau76 w corr",   Vec3( 5102.5089579, 6123.0114007, 6378.1369282), Vec3(-4.743220157,  0.790536497, 5.533755727)
//  "GCRF iau76 wfb",      Vec3( 5102.5095196, 6123.0109480, 6378.1369135), Vec3(-4.743219990,  0.790536753, 5.533755834)

  def checkPV( pv: PosVel[_], rTol: Double, vTol: Double, rExpected: Vec3, vExpected: Vec3) {
    pv.position.x should be (rExpected.x +- rTol)
    pv.position.y should be (rExpected.y +- rTol)
    pv.position.z should be (rExpected.z +- rTol)
    pv.velocity.x should be (vExpected.x +- vTol)
    pv.velocity.y should be (vExpected.y +- vTol)
    pv.velocity.z should be (vExpected.z +- vTol)
  }

}
